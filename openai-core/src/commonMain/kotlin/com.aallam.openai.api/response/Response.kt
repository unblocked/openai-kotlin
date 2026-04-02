package com.aallam.openai.api.response

import com.aallam.openai.api.core.Usage
import com.aallam.openai.api.model.ModelId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * Response from the responses API.
 */
@Serializable
public data class Response(
    /**
     * Unique identifier for the response.
     */
    @SerialName("id") public val id: ResponseId,
    
    /**
     * The object type, always "response".
     */
    @SerialName("object") public val objectType: String,
    
    /**
     * The creation time in epoch seconds.
     */
    @SerialName("created_at") public val createdAt: Double,
    
    /**
     * The model used for the response.
     */
    @SerialName("model") public val model: ModelId,
    
    /**
     * The output items from the response.
     */
    @SerialName("output") public val output: List<ResponseOutputItem>,
    
    /**
     * The reasoning trace from the model (if requested).
     */
    @SerialName("reasoning") public val reasoning: ReasoningTrace? = null,
    
    /**
     * Usage statistics for the response.
     */
    @SerialName("usage") public val usage: Usage? = null,
    
    /**
     * The status of the response.
     */
    @SerialName("status") public val status: String,
    
    /**
     * The combined output text from all message outputs.
     */
    @SerialName("output_text") public val outputText: String? = null,
    
    /**
     * Error information if the response failed.
     */
    @SerialName("error") public val error: ResponseErrorDetails? = null,
    
    /**
     * Metadata associated with the response.
     */
    @SerialName("metadata") public val metadata: Map<String, String>? = null,

    @SerialName("incomplete_details") public val incompleteDetails: JsonObject? = null,
    @SerialName("instructions") public val instructions: String? = null,
    @SerialName("max_output_tokens") public val maxOutputTokens: Int? = null,
    @SerialName("parallel_tool_calls") public val parallelToolCalls: Boolean? = null,
    @SerialName("previous_response_id") public val previousResponseId: ResponseId? = null,
    @SerialName("store") public val store: Boolean? = null,
    @SerialName("temperature") public val temperature: Double? = null,
    @SerialName("text") public val text: JsonObject? = null,
    @SerialName("tool_choice") public val toolChoice: JsonElement? = null,
    @SerialName("tools") public val tools: JsonElement? = null,
    @SerialName("top_p") public val topP: Double? = null,
    @SerialName("truncation") public val truncation: String? = null,
    @SerialName("user") public val user: String? = null,
) {
    /**
     * Get the first message output content as text, if available.
     */
    public val firstMessageText: String?
        get() = output.filterIsInstance<Message>()
            .firstOrNull()
            ?.content
            ?.filterIsInstance<MessageContent.OutputText>()
            ?.firstOrNull()
            ?.text
}

/**
 * Input items for the responses API.
 */
@Serializable
public sealed interface ResponseInputItem

/**
 * Output items from the responses API.
 */
@Serializable
public sealed interface ResponseOutputItem

/**
 * Error information for a failed response.
 */
@Serializable
public data class ResponseErrorDetails(
    /**
     * The error code.
     */
    @SerialName("code") public val code: String? = null,
    
    /**
     * The error message.
     */
    @SerialName("message") public val message: String? = null,
)
