package com.socks.jiandan.async;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.socks.jiandan.callback.OnHttpResponseCallBack;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public abstract class BaseJsonResponseHandler extends BaseJsonHttpResponseHandler implements ResponseCode {

    protected OnHttpResponseCallBack mHttpResponseCallBack;


    public BaseJsonResponseHandler(@NonNull OnHttpResponseCallBack onHttpResponseCallBack) {

        if (onHttpResponseCallBack == null) {
            throw new IllegalArgumentException("OnHttpResponseCallBack can't be null !");
        }

        mHttpResponseCallBack = onHttpResponseCallBack;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
        onSuccess(statusCode, rawJsonResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
        onFailure(statusCode, throwable);
    }

    @Override
    protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        return null;
    }

    protected abstract void onSuccess(int statusCode, String rawJsonResponse);

    protected abstract void onFailure(int statusCode, Throwable throwable);

    protected NetworkErrorException getNetWorkException(String reason) {
        return new NetworkErrorException(reason);
    }

    @Override
    public void onRetry(int retryNo) {
        super.onRetry(retryNo);

    }
}
