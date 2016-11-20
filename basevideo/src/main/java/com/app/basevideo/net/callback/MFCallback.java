package com.app.basevideo.net.callback;

import java.io.IOException;

import retrofit2.Response;

public interface MFCallback<T> {

    void onResponse(T entity, Response<?> response, Throwable throwable);

    /**
     * Called for [200, 300) responses.
     */
    void success(T entity);

    /**
     * Called for failure response
     */
    void failure(Response<?> response);

    /**
     * Called for when occur throwable
     */
    void exception(Throwable t);

    /**
     * Called for 204 and 205
     */
    void noContent(Response<?> response);

    /**
     * Called for 401 responses.
     */
    void unauthenticated(Response<?> response);

    /**
     * Called for [400, 500) responses, except 401.
     */
    void clientError(Response<?> response);

    /**
     * Called for [500, 600) response.
     */
    void serverError(Response<?> response);

    /**
     * Called for network errors while making the call.
     */
    void networkError(IOException e);

    /**
     * Called for unexpected errors while making the call.
     */
    void unexpectedError(Throwable t);
}
