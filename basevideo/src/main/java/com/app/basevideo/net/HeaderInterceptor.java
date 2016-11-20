package com.app.basevideo.net;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {


    public HeaderInterceptor() {

    }

    /**
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("User-Agent", "userAgent")
                .addHeader("uuid", "123456uuid")
                .addHeader("deviceId", "deviceId123");
        return chain.proceed(builder.build());
    }

}
