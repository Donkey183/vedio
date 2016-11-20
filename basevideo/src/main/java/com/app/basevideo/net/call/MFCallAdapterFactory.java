package com.app.basevideo.net.call;

import android.os.Handler;
import android.os.Looper;

import com.app.basevideo.net.BaseHttpResult;
import com.app.basevideo.net.callback.MFResponseFilter;
import com.app.basevideo.net.callback.MFResponseFilterImpl;
import com.app.basevideo.net.utils.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;


public class MFCallAdapterFactory extends CallAdapter.Factory {

    private MainThreadExecutor mainThreadExecutor;

    private MFCallAdapterFactory() {
        mainThreadExecutor = new MainThreadExecutor();
    }

    public static MFCallAdapterFactory created() {
        return new MFCallAdapterFactory();
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations,
                              Retrofit retrofit) {

        if (Types.getRawType(returnType) != MFCall.class) {
            return null;
        }

        if (!(returnType instanceof ParameterizedType)) {
            /* 返回结果应该指定一个泛型，最起码也需要一个ResponseBody作为泛型 */
            throw new IllegalStateException(
                    "MFCall must have generic type (e.g., MFCall<ResponseBody>)");
        }

        final Type responseType = Types.getParameterUpperBound(0,
                (ParameterizedType) returnType);

        return new CallAdapter<MFCall<?>>() {
            @Override
            public Type responseType() {
                return responseType;
            }

            @Override
            public <R> MFCall<?> adapt(Call<R> call) {
                MFCallAdapter adapter = new MFCallAdapter<>(call, mainThreadExecutor);
                MFResponseFilter<BaseHttpResult> filter = new MFResponseFilterImpl();
                adapter.setFilter(filter);
                return adapter;
            }
        };
    }


    class MainThreadExecutor implements Executor {
        Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainHandler.post(command);
        }
    }
}
