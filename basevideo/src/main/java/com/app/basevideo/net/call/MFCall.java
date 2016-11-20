package com.app.basevideo.net.call;

import com.app.basevideo.net.callback.MFCallback;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;

public interface MFCall<T> extends Cloneable {

    /**
     * synchronize execute
     */
    retrofit2.Response<T> execute() throws IOException;

    /**
     * asynchronous by callback
     */
    void doRequest(MFCallback<T> callback);

    /**
     * cancel the request and be care of IOException
     */
    void cancel();


    boolean isExecuted();

    boolean isCanceled();

    Call<T> clone();

    /**
     * The original HTTP request.
     */
    Request request();
}
