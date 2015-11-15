package com.socks.jiandan.async;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import com.loopj.android.http.TextHttpResponseHandler;
import com.socks.jiandan.callback.OnHttpResponseCallBackImpl;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public abstract class BaseJsonResponseHandler extends TextHttpResponseHandler implements ResponseCode {

    protected OnHttpResponseCallBackImpl mHttpResponseCallBack;

    public BaseJsonResponseHandler(@NonNull OnHttpResponseCallBackImpl onHttpResponseCallBack) {

        if (onHttpResponseCallBack == null) {
            throw new IllegalArgumentException("OnHttpResponseCallBack can't be null !");
        }

        mHttpResponseCallBack = onHttpResponseCallBack;
    }

    @Override
    public void onStart() {
        super.onStart();
        mHttpResponseCallBack.onStart();
    }

    @Override
    public void onFinish() {
        super.onFinish();
        mHttpResponseCallBack.onFinish();
    }

    @Override
    public void onCancel() {
        super.onCancel();
        mHttpResponseCallBack.onCancel();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        onSuccess(statusCode, responseString);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        onFailure(statusCode, throwable);
    }

    protected abstract void onSuccess(int statusCode, String rawJsonResponse);

    protected abstract void onFailure(int statusCode, Throwable throwable);

    protected NetworkErrorException getNetWorkException(String reason) {
        return new NetworkErrorException(reason);
    }

}
