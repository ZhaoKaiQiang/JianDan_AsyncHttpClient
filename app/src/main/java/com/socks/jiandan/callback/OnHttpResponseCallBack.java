package com.socks.jiandan.callback;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public interface OnHttpResponseCallBack<T> {

    void onSuccess(int statusCode, T t);

    void onFailure(int statusCode, Throwable throwable);

}
