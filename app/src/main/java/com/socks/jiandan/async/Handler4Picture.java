package com.socks.jiandan.async;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.socks.jiandan.callback.OnHttpResponseCallBackImpl;
import com.socks.jiandan.model.Picture;
import com.socks.jiandan.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class Handler4Picture extends BaseJsonResponseHandler {

    public Handler4Picture(@NonNull OnHttpResponseCallBackImpl<ArrayList<Picture>> onHttpResponseCallBack) {
        super(onHttpResponseCallBack);
    }

    @Override
    protected void onSuccess(int statusCode, String rawJsonResponse) {

        try {
            String jsonStr = new JSONObject(rawJsonResponse).getJSONArray("comments").toString();

            ArrayList<Picture> pictures = (ArrayList<Picture>) JSONParser.toObject(jsonStr,
                    new TypeToken<ArrayList<Picture>>() {
                    }.getType());
            mHttpResponseCallBack.onSuccess(SUCCESS, pictures);
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
