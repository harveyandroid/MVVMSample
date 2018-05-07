package com.harvey.mvvmsample.http;

/**
 * Created by hanhui on 2018/4/17 0017 11:54
 */

public class ResponseThrowable extends Throwable {

    public int code;
    public String message;

    public ResponseThrowable(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
