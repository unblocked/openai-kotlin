package com.aallam.openai.api.response

import com.aallam.openai.api.chat.ChatRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Output items from the responses API.
 */
@Serializable
public sealed interface ResponseOutputItem {
    
    /**
     * A message output item.
     */
    @Serializable
    @SerialName("message")
    public data class Message(
        /**
         * Unique identifier for this output item.
         */
        @SerialName("id") public val id: String,

        /**
         * The role of the message author.
         */
        @SerialName("role") public val role: ChatRole,

        /**
         * The content of the message.
         */
        @SerialName("content") public val content: List<MessageContent>,

        /**
         * The status of the message.
         */
        @SerialName("status") public val status: String? = null,
    ) : ResponseOutputItem

    /**
     * A reasoning output item containing reasoning traces.
     */
    @Serializable
    @SerialName("reasoning")
    public data class Reasoning(
        /**
         * Unique identifier for this output item.
         */
        @SerialName("id") public val id: String,

        /**
         * The reasoning trace.
         */
        @SerialName("reasoning") public val reasoning: ReasoningTrace? = null,

        /**
         * The status of the reasoning.
         */
        @SerialName("status") public val status: String? = null,
    ) : ResponseOutputItem
}

/**
 * Content within a message output.
 */
@Serializable
public sealed interface MessageContent {
    
    /**
     * Text content within a message.
     */
    @Serializable
    @SerialName("output_text")
    public data class Text(
        /**
         * The text content.
         */
        @SerialName("text") public val text: String,

        /**
         * Annotations for the text content.
         */
        @SerialName("annotations") public val annotations: List<String> = emptyList(),
    ) : MessageContent
}
