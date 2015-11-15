package com.socks.jiandan.async;

import android.support.annotation.NonNull;

import com.socks.jiandan.callback.OnHttpResponseCallBackImpl;
import com.socks.jiandan.model.Comment4FreshNews;
import com.socks.jiandan.utils.logger.Logger;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.MessageFormat;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class Handler4ReplyFreshNews extends BaseJsonResponseHandler {

    public Handler4ReplyFreshNews(@NonNull OnHttpResponseCallBackImpl<Boolean> onHttpResponseCallBack) {
        super(onHttpResponseCallBack);
    }

    @Override
    protected void onSuccess(int statusCode, String rawJsonResponse) {

        try {

            JSONObject resultObj = new JSONObject(rawJsonResponse);
            String result = resultObj.optString("status");
            if (result.equals("ok")) {
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
     * @return
     */
    public static String getRequestURL(String post_id, String parent_id, String parent_name,
                                       String name, String email, String content) {
        content = MessageFormat.format("@<a href=\"#comment-{0}\">{1}</a>: {2}", parent_id, parent_name, content);
        return getRequestURLNoParent(post_id, name, email, content);
    }

    /**
     * 包装无父评论的请求参数
     *
     * @return
     */
    public static String getRequestURLNoParent(String post_id, String name, String email, String content) {
        //方法1 转义
        //content= MessageFormat.format("%40%3Ca+href%3D%27%23comment-{0}%27%3E{1}%3C%2Fa%3E%3A+{2}",parent_id,parent_name, content);
        //方法2 URLEncoder（更优）
        try {
            name = URLEncoder.encode(name, "utf-8");
            content = URLEncoder.encode(content, "utf-8");
        } catch (Exception ex) {
            Logger.d("URLEncoder error");
        }
        return MessageFormat.format("{0}&post_id={1}&content={2}&email={3}&name={4}",
                Comment4FreshNews.URL_PUSH_COMMENT, post_id, content, email, name);
    }
}
