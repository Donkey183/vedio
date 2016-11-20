package com.app.basevideo.net.callback;

import android.support.annotation.CallSuper;

import java.io.IOException;

import retrofit2.Response;


public class MFCallbackAdapter<T> implements MFCallback<T> {
    @Override
    public void onResponse(T entity, Response<?> response, Throwable throwable) {
    }

    @Override
    public void success(T entity) {
        onResponse(entity, null, null);
    }

    @Override
    public void failure(Response<?> response) {
        onResponse(null, response, null);
    }

    @Override
    public void exception(Throwable t) {
        onResponse(null, null, t);
    }

    @Override
    @CallSuper
    public void noContent(Response<?> response) {
        failure(response);
    }

    @Override
    @CallSuper
    public void unauthenticated(Response<?> response) {
        failure(response);

    }

    @Override
    @CallSuper
    public void clientError(Response<?> response) {
        failure(response);

    }

    @Override
    @CallSuper
    public void serverError(Response<?> response) {
        failure(response);

    }

    @Override
    @CallSuper
    public void networkError(IOException e) {
        exception(e);
    }

    @Override
    @CallSuper
    public void unexpectedError(Throwable t) {
        exception(t);
    }
}
