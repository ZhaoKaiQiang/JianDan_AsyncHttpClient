package com.socks.jiandan.async;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.socks.jiandan.callback.OnHttpResponseCallBack;
import com.socks.jiandan.model.Joke;
import com.socks.jiandan.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class Handler4Joke extends BaseJsonResponseHandler {

    public Handler4Joke(@NonNull OnHttpResponseCallBack<ArrayList<Joke>> onHttpResponseCallBack) {
        super(onHttpResponseCallBack);
    }

    @Override
    protected void onSuccess(int statusCode, String rawJsonResponse) {

        try {
            String jsonStr = new JSONObject(rawJsonResponse).getJSONArray("comments").toString();

            ArrayList<Joke> jokes = (ArrayList<Joke>) JSONParser.toObject(jsonStr,
                    new TypeToken<ArrayList<Joke>>() {
                    }.getType());
            mHttpResponseCallBack.onSuccess(SUCCESS, jokes);
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
