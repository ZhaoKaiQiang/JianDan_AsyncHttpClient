package com.socks.jiandan.async;

import android.support.annotation.NonNull;

import com.socks.jiandan.callback.OnHttpResponseCallBack;
import com.socks.jiandan.model.FreshNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class Handler4FreshNews extends BaseJsonResponseHandler {

    public Handler4FreshNews(@NonNull OnHttpResponseCallBack<ArrayList<FreshNews>> onHttpResponseCallBack) {
        super(onHttpResponseCallBack);
    }

    @Override
    protected void onSuccess(int statusCode, String rawJsonResponse) {

        try {
            JSONObject resultObj = new JSONObject(rawJsonResponse);
            JSONArray postsArray = resultObj.optJSONArray("posts");
            mHttpResponseCallBack.onSuccess(SUCCESS, FreshNews.parse(postsArray));
        } catch (JSONException e) {
            e.printStackTrace();
            onFailure(FAILED_JSON_PARSE, e);
        }

    }

    @Override
    protected void onFailure(int statusCode, Throwable throwable) {
        mHttpResponseCallBack.onFailure(statusCode, throwable);
    }
}
