package com.harvey.mvvmsample.http;


import android.util.Log;

import com.harvey.mvvmsample.http.model.HttpBean;

import io.reactivex.functions.Consumer;

/**
 * Created by hanhui on 2018/4/17 0017 11:01
 */

public class BaseHttpConsumer implements Consumer<HttpBean> {

    @Override
    public void accept(HttpBean httpBean) throws Exception {
        Log.e("BaseHttpConsumer", "--accept--error_code=" + httpBean.getError_code());
        if (!httpBean.isSuccess()) {
            throw new ServerException(httpBean.getMsg(), httpBean.getError_code());
        }
    }
}
