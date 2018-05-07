package com.harvey.mvvmsample.http.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 缓存机制 在响应请求之后在 data/data/<包名>/cache 下建立一个response 文件夹，保持缓存数据。
 * 这样我们就可以在请求的时候，如果判断到没有网络，自动读取缓存的数据。
 * 同样这也可以实现，在我们没有网络的情况下，重新打开App可以浏览的之前显示过的内容。
 * 也就是：判断网络，有网络，则从网络获取，并保存到缓存中，无网络，则从缓存中获取。
 * Created by hanhui on 2018/4/16 0016 17:12
 */

public class CacheControlInterceptor implements Interceptor {
    private Context mContext;

    public CacheControlInterceptor(Context context) {
        this.mContext = context;
    }

    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isNetworkAvailable(mContext)) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
        }

        Response originalResponse = chain.proceed(request);
        if (isNetworkAvailable(mContext)) {
            String cacheControl = request.cacheControl().toString();
            // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
            return originalResponse.newBuilder().header("Cache-Control", cacheControl).removeHeader("Pragma")
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 7;
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).removeHeader("Pragma")
                    .build();
        }
    }
}
