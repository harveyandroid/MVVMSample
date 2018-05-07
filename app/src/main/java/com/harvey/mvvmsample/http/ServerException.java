package com.harvey.mvvmsample.http;

/**
 *
 * ServerException发生后，将自动转换为ResponeThrowable返回
 */
public class ServerException extends RuntimeException{

    int code;
    String message;

    public ServerException(String message, int code) {
        super(message);
        this.code = code;
        this.message = message;
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
