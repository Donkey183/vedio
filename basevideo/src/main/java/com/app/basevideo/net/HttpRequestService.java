package com.app.basevideo.net;

import com.app.basevideo.base.MFBaseApplication;
import com.app.basevideo.net.call.MFCallAdapterFactory;
import com.app.basevideo.net.utils.UriManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequestService {

    private static final int CONNECT_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 30000;
    private static final int WRITE_TIMEOUT = 30000;
    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    private static Retrofit.Builder retrofitBuilder;

    static {
        if (MFBaseApplication.getInstance().isDebugMode()) {
            HttpRequestService.httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY));
        }

        httpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        httpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        httpClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
//        httpClientBuilder.retryOnConnectionFailure(true);

        HttpRequestService.httpClientBuilder.addInterceptor(new HeaderInterceptor());
        HttpRequestService.retrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(MFCallAdapterFactory.created())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build());
    }

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = HttpRequestService.retrofitBuilder.baseUrl(UriManager.getUriBase()).build();
        return retrofit.create(serviceClass);
    }
}
