package com.harvey.mvvmsample.http;

import android.content.Context;
import android.util.Log;


import com.harvey.mvvmsample.http.interceptor.CacheControlInterceptor;
import com.harvey.mvvmsample.http.interceptor.HttpLoggingInterceptor;
import com.harvey.mvvmsample.http.model.HttpBean;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hanhui on 2018/4/16 0016 16:48
 */

public class RetrofitWrapper {
    public static final String TAG = "RetrofitWrapper";
    private volatile static Retrofit retrofit;
    private static OkHttpClient sHttpClient = getClient();

    @NonNull
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitWrapper.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder().client(getClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
                }
            }
        }
        return retrofit;
    }

    public static OkHttpClient getClient() {
        if (sHttpClient == null) {
            return getDefaultClient();
        }
        return sHttpClient;
    }

    public static OkHttpClient getDefaultClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static void initClient(Context context, boolean isDebug) {
        Cache cache = new Cache(new File(context.getCacheDir(), "OkHttpCache"), 1024 * 1024 * 50);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache).addInterceptor(new CacheControlInterceptor(context)).connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        if (isDebug) {
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d(TAG, message);
                }
            });
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        sHttpClient = builder.build();
    }


    public static <T> Disposable subscribe(Flowable<HttpBean<T>> flowable, Consumer<T> onNext, final Consumer<ResponseThrowable> error) {
        return flowable.subscribeOn(Schedulers.io())
                .doOnNext(new BaseHttpConsumer())
                .map(mapHttpBeanToData())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, getThrowableConsumer(error));
    }

    public static <T> Disposable subscribe(Flowable<HttpBean<T>> flowable, Consumer<T> onNext) {
        return flowable.subscribeOn(Schedulers.io())
                .doOnNext(new BaseHttpConsumer())
                .map(mapHttpBeanToData())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext);
    }

    private static Consumer<Throwable> getThrowableConsumer(final Consumer<ResponseThrowable> error) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (error == null) {
                    Log.e(TAG, "请求失败：" + throwable.toString());
                } else {
                    ResponseThrowable throwable1 = ExceptionHandle.handleException(throwable);
                    error.accept(throwable1);
                }
            }
        };
    }

    private static <T> Function mapHttpBeanToData() {

        return new Function<HttpBean<T>, T>() {
            @Override
            public T apply(@NonNull HttpBean<T> tHttpBean) throws Exception {
                return tHttpBean.getData();
            }
        };
    }
}