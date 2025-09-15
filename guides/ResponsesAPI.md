# Responses API Guide

The Responses API is OpenAI's stateless API that provides access to reasoning traces from reasoning models like o1, o3, and others. This guide shows how to use the Responses API in the OpenAI Kotlin SDK.

## Key Features

- **Stateless operation**: No server-side state management (store=false always)
- **Reasoning access**: Get detailed reasoning traces from reasoning models
- **Manual context management**: Full control over conversation history
- **Type-safe**: Full Kotlin type safety for all interactions

## Basic Usage

### Simple Response

```kotlin
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.api.response.*
import com.aallam.openai.client.OpenAI

val openAI = OpenAI(token = "your-api-key")

val response = openAI.createResponse(
    responseRequest {
        model = ModelId("gpt-4o")
        input {
            message {
                role = ChatRole.User
                content = "Hello, how are you?"
            }
        }
    }
)

println("Response: ${response.firstMessageText}")
```

### Accessing Reasoning Traces

To get reasoning traces from reasoning models, include "reasoning.encrypted_content" in your request:

```kotlin
val response = openAI.createResponse(
    responseRequest {
        model = ModelId("o1") // Use a reasoning model
        reasoning = ReasoningConfig(effort = "medium")
        include = listOf("reasoning.encrypted_content") // Request reasoning traces
        input {
            message {
                role = ChatRole.User
                content = "Solve this step by step: What is the square root of 144?"
            }
        }
    }
)

// Access the reasoning trace
val reasoningTrace = response.reasoning?.content
println("Model's reasoning: $reasoningTrace")

// Access the final answer
println("Answer: ${response.firstMessageText}")
```

### Multi-turn Conversations with Reasoning

Since the API is stateless, you need to manually manage conversation history and pass previous reasoning traces:

```kotlin
// First interaction
val firstResponse = openAI.createResponse(
    responseRequest {
        model = ModelId("o1")
        reasoning = ReasoningConfig(effort = "medium")
        include = listOf("reasoning.encrypted_content")
        input {
            message {
                role = ChatRole.User
                content = "What is 15 * 23?"
            }
        }
    }
)

val firstReasoning = firstResponse.reasoning?.content
val firstAnswer = firstResponse.firstMessageText

// Second interaction - include previous context and reasoning
val secondResponse = openAI.createResponse(
    responseRequest {
        model = ModelId("o1")
        reasoning = ReasoningConfig(effort = "medium")
        include = listOf("reasoning.encrypted_content")
        input {
            // Previous conversation
            message {
                role = ChatRole.User
                content = "What is 15 * 23?"
            }
            message {
                role = ChatRole.Assistant
                content = firstAnswer ?: "345"
            }
            // Previous reasoning
            reasoning {
                content = firstReasoning!!
            }
            // New question
            message {
                role = ChatRole.User
                content = "Now divide that result by 5"
            }
        }
    }
)

println("Second answer: ${secondResponse.firstMessageText}")
println("Second reasoning: ${secondResponse.reasoning?.content}")
```

## Configuration Options

### Reasoning Configuration

```kotlin
reasoning = ReasoningConfig(
    effort = "high" // "low", "medium", or "high"
)
```

### Request Parameters

```kotlin
responseRequest {
    model = ModelId("o1")
    temperature = 0.7
    maxOutputTokens = 1000
    topP = 0.9
    reasoning = ReasoningConfig(effort = "medium")
    include = listOf("reasoning.encrypted_content")
    // ... input
}
```

## Best Practices

1. **Always use reasoning models** (o1, o3, etc.) when you want reasoning traces
2. **Include "reasoning.encrypted_content"** in the include parameter to get reasoning traces
3. **Manage context manually** by passing previous messages and reasoning traces
4. **Store reasoning traces** if you need them for future interactions
5. **Use appropriate effort levels** - higher effort may provide more detailed reasoning but takes longer

## Error Handling

```kotlin
try {
    val response = openAI.createResponse(request)
    if (response.error != null) {
        println("Error: ${response.error?.message}")
    } else {
        println("Success: ${response.firstMessageText}")
    }
} catch (e: Exception) {
    println("Request failed: ${e.message}")
}
```

## Model Support

The Responses API works with various OpenAI models:

- **Reasoning models**: o1, o3, o4-mini (provide reasoning traces)
- **Standard models**: gpt-4o, gpt-4o-mini, gpt-5 series (no reasoning traces)
- **Specialized models**: computer-use-preview, gpt-image-1

For reasoning traces, use reasoning models like o1 or o3.
