package com.socks.jiandan.async;

import android.support.annotation.NonNull;

import com.socks.jiandan.callback.OnHttpResponseCallBack;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class Handler4Reply extends BaseJsonResponseHandler {

    public Handler4Reply(@NonNull OnHttpResponseCallBack<Boolean> onHttpResponseCallBack) {
        super(onHttpResponseCallBack);
    }

    @Override
    protected void onSuccess(int statusCode, String rawJsonResponse) {

        try {
            JSONObject resultObj = new JSONObject(rawJsonResponse);
            int code = resultObj.optInt("code");
            if (code == 0) {
                mHttpResponseCallBack.onSuccess(SUCCESS, true);
            } else {
                mHttpResponseCallBack.onSuccess(SUCCESS, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(FAILED_JSON_PARSE, e);

        }
    }

    @Override
    protected void onFailure(int statusCode, Throwable throwable) {
        mHttpResponseCallBack.onFailure(statusCode, throwable);
    }

    /**
     * 包装请求参数
     *
     * @param thread_id
     * @return
     */
    public static HashMap<String, String> getRequestParams(String thread_id, String parent_id,
                                                           String author_name, String author_email,
                                                           String message) {
        HashMap<String, String> params = new HashMap<>();
        params.put("thread_id", thread_id);
        params.put("parent_id", parent_id);
        params.put("author_name", author_name);
        params.put("author_email", author_email);
        params.put("message", message);

        return params;
    }

    /**
     * 包装无父评论的请求参数
     *
     * @param thread_id
     * @param author_name
     * @param author_email
     * @param message
     * @return
     */
    public static HashMap<String, String> getRequestParamsNoParent(String thread_id,
                                                                   String author_name, String author_email,
                                                                   String message) {
        HashMap<String, String> params = new HashMap<>();
        params.put("thread_id", thread_id);
        params.put("author_name", author_name);
        params.put("author_email", author_email);
        params.put("message", message);

        return params;
    }
}
