package com.socks.jiandan.callback;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public abstract class OnHttpResponseCallBackImpl<T> implements OnHttpResponseCallBack<T> {


    @Override
    public void onSuccess(int statusCode,T t) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onFailure(int statusCode, Throwable throwable) {

    }

    @Override
    public void onCancel() {

    }
}