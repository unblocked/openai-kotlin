package com.aallam.openai.api.response

import com.aallam.openai.api.OpenAIDsl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable @SerialName("function_call")
public data class FunctionCall(
    val name: ToolName,
    val callId: ToolCallId,
    val arguments: ToolCallArguments,
    val status: ToolCallStatus,
) : ResponseOutputItem, ResponseInputItem

@Serializable @SerialName("function_call_output")
public data class FunctionCallOutput(
    val callId: ToolCallId,
    val output: ToolCallOutput,
) : ResponseInputItem

@Serializable
public enum class ToolCallStatus {
    @SerialName("completed")
    Completed,

    @SerialName("in_progress")
    InProgress,

    @SerialName("incompleted")
    Incomplete,
    ;
}

@JvmInline
@Serializable
public value class ToolCallId(public val callId: String)

@JvmInline
@Serializable
public value class ToolCallArguments(public val arguments: String)

@JvmInline
@Serializable
public value class ToolCallOutput(public val output: String)

@OpenAIDsl
public class FunctionCallBuilder {
    public var name: ToolName? = null
    public var callId: ToolCallId? = null
    public var arguments: ToolCallArguments? = null
    public var status: ToolCallStatus? = null

    public fun build(): FunctionCall = FunctionCall(
        name = requireNotNull(name) { "name is required" },
        callId = requireNotNull(callId) { "callId is required" },
        arguments = requireNotNull(arguments) { "arguments is required" },
        status = requireNotNull(status) { "status is required" },
    )
}

@OpenAIDsl
public class FunctionCallOutputBuilder {
    public var callId: String? = null
    public var output: String? = null

    public fun build(): FunctionCallOutput = FunctionCallOutput(
        callId = ToolCallId(requireNotNull(callId) { "callId is required" }),
        output = ToolCallOutput(requireNotNull(output) { "output is required" }),
    )
}

