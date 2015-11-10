package com.socks.jiandan.async;

import android.support.annotation.NonNull;

import com.socks.jiandan.callback.OnHttpResponseCallBack;
import com.socks.jiandan.model.CommentNumber;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class Handler4CommentNumber extends BaseJsonResponseHandler {

    public Handler4CommentNumber(@NonNull OnHttpResponseCallBack<ArrayList<CommentNumber>> onHttpResponseCallBack) {
        super(onHttpResponseCallBack);
    }

    @Override
    protected void onSuccess(int statusCode, String rawJsonResponse) {


        try {
            String url = getRequestURI().toURL().toString();
            JSONObject jsonObject = new JSONObject(rawJsonResponse).getJSONObject("response");
            String[] comment_IDs = url.split("\\=")[1].split("\\,");
            ArrayList<CommentNumber> commentNumbers = new ArrayList<>();

            for (String comment_ID : comment_IDs) {

                if (!jsonObject.isNull(comment_ID)) {
                    CommentNumber commentNumber = new CommentNumber();
                    commentNumber.setComments(jsonObject.getJSONObject(comment_ID).getInt(CommentNumber.COMMENTS));
                    commentNumber.setThread_id(jsonObject.getJSONObject(comment_ID).getString(CommentNumber.THREAD_ID));
                    commentNumber.setThread_key(jsonObject.getJSONObject(comment_ID).getString(CommentNumber.THREAD_KEY));
                    commentNumbers.add(commentNumber);
                } else {
                    //可能会出现没有对应id的数据的情况，为了保证条数一致，添加默认数据
                    commentNumbers.add(new CommentNumber("0", "0", 0));
                }
            }
            mHttpResponseCallBack.onSuccess(SUCCESS, commentNumbers);
        } catch (JSONException e) {
            e.printStackTrace();
            onFailure(FAILED_JSON_PARSE, e);
        } catch (MalformedURLException e) {
            onFailure(FAILED_MALFORMED_URL_EXCEPTION, e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onFailure(int statusCode, Throwable throwable) {
        mHttpResponseCallBack.onFailure(statusCode, throwable);
    }
}
