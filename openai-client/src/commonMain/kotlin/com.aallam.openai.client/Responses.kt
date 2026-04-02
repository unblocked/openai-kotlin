package com.aallam.openai.client

import com.aallam.openai.api.core.RequestOptions
import com.aallam.openai.api.response.Response
import com.aallam.openai.api.response.ResponseChunk
import com.aallam.openai.api.response.ResponseId
import com.aallam.openai.api.response.ResponseRequest
import kotlinx.coroutines.flow.Flow

/**
 * The Responses API provides a stateless interface for generating responses with reasoning support.
 * This API is particularly useful for accessing reasoning traces from reasoning models.
 */
public interface Responses {

    /**
     * Creates a response for the given input.
     *
     * This method always operates in stateless mode (store=false), requiring manual context management.
     * To access reasoning traces, include "reasoning.encrypted_content" in the request's include parameter.
     *
     * @param request the response request containing model, input, and configuration
     * @param requestOptions additional request options
     * @return the generated response with optional reasoning traces
     */
    public suspend fun response(
        request: ResponseRequest,
        requestOptions: RequestOptions? = null
    ): Response

    /**
     * Stream variant of [response].
     *
     * This method streams response chunks as they are generated, allowing real-time access to
     * reasoning summaries and message content as they are produced.
     *
     * @param request the response request containing model, input, and configuration (with stream=true)
     * @param requestOptions additional request options
     * @return a flow of response chunks
     */
    public fun responseStream(
        request: ResponseRequest,
        requestOptions: RequestOptions? = null
    ): Flow<ResponseChunk>

    /**
     * Retrieves a response by its ID.
     *
     * @param id the response ID
     * @param requestOptions additional request options
     * @return the response, or null if not found
     */
    public suspend fun response(
        id: ResponseId,
        requestOptions: RequestOptions? = null
    ): Response?

    /**
     * Deletes a response by its ID.
     *
     * @param id the response ID
     * @param requestOptions additional request options
     * @return true if deleted, false if not found
     */
    public suspend fun delete(
        id: ResponseId,
        requestOptions: RequestOptions? = null
    ): Boolean

    /**
     * Cancels an in-progress response by its ID.
     *
     * @param id the response ID
     * @param requestOptions additional request options
     * @return the cancelled response, or null if not found
     */
    public suspend fun cancel(
        id: ResponseId,
        requestOptions: RequestOptions? = null
    ): Response?
}
