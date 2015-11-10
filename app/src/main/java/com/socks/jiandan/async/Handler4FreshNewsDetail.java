package com.socks.jiandan.async;

import android.support.annotation.NonNull;

import com.socks.jiandan.callback.OnHttpResponseCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class Handler4FreshNewsDetail extends BaseJsonResponseHandler {

    public Handler4FreshNewsDetail(@NonNull OnHttpResponseCallBack<String> onHttpResponseCallBack) {
        super(onHttpResponseCallBack);
    }

    @Override
    protected void onSuccess(int statusCode, String rawJsonResponse) {

        try {
            JSONObject jsonObject = new JSONObject(rawJsonResponse);

            if (jsonObject.opt("status").equals("ok")) {
                JSONObject contentObject = jsonObject.optJSONObject("post");
                mHttpResponseCallBack.onSuccess(SUCCESS, contentObject.optString("content"));
            } else {
                mHttpResponseCallBack.onFailure(FAILED_GET_DATA, getNetWorkException(MESSAGE_GET_DATA));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mHttpResponseCallBack.onFailure(FAILED_GET_DATA, e);
        }
    }

    @Override
    protected void onFailure(int statusCode, Throwable throwable) {
        mHttpResponseCallBack.onFailure(statusCode, throwable);
    }
}