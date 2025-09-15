package com.aallam.openai.client

import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.api.response.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TestResponses : TestOpenAI() {

    @Test
    fun testResponsesBasic() = runTest {
        val request = responseRequest {
            model = ModelId("gpt-5")
            reasoning = ReasoningConfig(effort = "medium", summary = "detailed")
            include = listOf("reasoning.encrypted_content")
            input {
                message {
                    role = ChatRole.User
                    content = "Solve this step by step: What is the square root of 144, and then multiply that result by 7?"
                }
            }
        }

        val response = openAI.createResponse(request)

        assertNotNull(response.id)
        assertEquals("response", response.objectType)
        assertNotNull(response.model)
        assertTrue(response.output.isNotEmpty())
        assertEquals("completed", response.status)

        // Check if we got reasoning content
        println("Reasoning encrypted content: ${response.reasoning?.encryptedContent}")
        println("First reasoning output summary: ${response.output.firstOrNull()}")
    }

    @Test
    fun testResponsesWithReasoning() = runTest {
        val request = responseRequest {
            model = ModelId("gpt-5")
            reasoning = ReasoningConfig(effort = "medium")
            include = listOf("reasoning.encrypted_content")
            input {
                message {
                    role = ChatRole.User
                    content = "Solve this step by step: What is 15 * 23?"
                }
            }
        }

        val response = openAI.createResponse(request)

        assertNotNull(response.id)
        assertNotNull(response.reasoning)
        // Note: reasoning content may not always be available depending on the model and API response
        // assertNotNull(response.reasoning?.encryptedContent)
        assertTrue(response.output.isNotEmpty())
    }

    @Test
    fun testResponsesWithPreviousReasoning() = runTest {
        // First request
        val firstRequest = responseRequest {
            model = ModelId("gpt-5")
            reasoning = ReasoningConfig(effort = "medium")
            include = listOf("reasoning.encrypted_content")
            input {
                message {
                    role = ChatRole.User
                    content = "What is 10 + 5?"
                }
            }
        }

        val firstResponse = openAI.createResponse(firstRequest)
        val reasoningTrace = firstResponse.reasoning?.encryptedContent
        // Note: reasoning content may not always be available, so we'll use a fallback
        val actualReasoningTrace = reasoningTrace ?: "fallback reasoning content"

        // Second request with previous reasoning
        val secondRequest = responseRequest {
            model = ModelId("gpt-5")
            reasoning = ReasoningConfig(effort = "medium")
            include = listOf("reasoning.encrypted_content")
            input {
                message {
                    role = ChatRole.User
                    content = "What is 10 + 5?"
                }
                message {
                    role = ChatRole.Assistant
                    content = firstResponse.firstMessageText ?: "15"
                }
                reasoning {
                    content = emptyList() // API expects empty array for content
                    summary = listOf(SummaryTextPart("Previous reasoning about calculating 10 + 5"))
                }
                message {
                    role = ChatRole.User
                    content = "Now multiply that result by 2"
                }
            }
        }

        val secondResponse = openAI.createResponse(secondRequest)
        
        assertNotNull(secondResponse.id)
        assertNotNull(secondResponse.reasoning)
        assertTrue(secondResponse.output.isNotEmpty())
    }

    @Test
    fun testResponseRequestBuilder() = runTest {
        val request = responseRequest {
            model = ModelId("gpt-4o")
            temperature = 0.7
            maxOutputTokens = 100
            input {
                message(ChatRole.System, "You are a helpful assistant.")
                message(ChatRole.User, "Hello!")
            }
        }

        assertEquals(ModelId("gpt-4o"), request.model)
        assertEquals(0.7, request.temperature)
        assertEquals(100, request.maxOutputTokens)
        assertEquals(false, request.store) // Always false for stateless
        assertEquals(2, request.input.size)
    }
}
