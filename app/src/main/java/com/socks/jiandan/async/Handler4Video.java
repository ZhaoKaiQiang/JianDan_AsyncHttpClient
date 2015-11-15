package com.socks.jiandan.async;

import android.support.annotation.NonNull;

import com.socks.jiandan.callback.OnHttpResponseCallBackImpl;
import com.socks.jiandan.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class Handler4Video extends BaseJsonResponseHandler {

    public Handler4Video(@NonNull OnHttpResponseCallBackImpl<ArrayList<Video>> onHttpResponseCallBack) {
        super(onHttpResponseCallBack);
    }

    @Override
    protected void onSuccess(int statusCode, String rawJsonResponse) {

        try {
            JSONObject jsonObject = new JSONObject(rawJsonResponse);

            if ("ok".equals(jsonObject.optString("status"))) {

                JSONArray commentsArray = jsonObject.optJSONArray("comments");
                ArrayList<Video> videos = new ArrayList<>();

                for (int i = 0; i < commentsArray.length(); i++) {

                    JSONObject commentObject = commentsArray.getJSONObject(i);
                    JSONObject videoObject = commentObject.optJSONArray("videos").optJSONObject(0);

                    if (videoObject != null) {
                        Video video = new Video();
                        video.setTitle(videoObject.optString("title"));
                        String videoSource = videoObject.optString("video_source");
                        video.setComment_ID(commentObject.optString("comment_ID"));
                        video.setVote_positive(commentObject.optString("vote_positive"));
                        video.setVote_negative(commentObject.optString("vote_negative"));
                        video.setVideo_source(videoSource);

                        if (videoSource.equals("youku")) {
                            video.setUrl(videoObject.optString("link"));
                            video.setDesc(videoObject.optString("description"));
                            video.setImgUrl(videoObject.optString("thumbnail"));
                            video.setImgUrl4Big(videoObject.optString("thumbnail_v2"));
                        } else if (videoSource.equals("56")) {
                            video.setUrl(videoObject.optString("url"));
                            video.setDesc(videoObject.optString("desc"));
                            video.setImgUrl4Big(videoObject.optString("img"));
                            video.setImgUrl(videoObject.optString("mimg"));
                        } else if (videoSource.equals("tudou")) {
                            video.setUrl(videoObject.optString("playUrl"));
                            video.setImgUrl(videoObject.optString("picUrl"));
                            video.setImgUrl4Big(videoObject.optString("picUrl"));
                            video.setDesc(videoObject.optString("description"));
                        }

                        videos.add(video);
                    }
                }

                mHttpResponseCallBack.onSuccess(SUCCESS, videos);
            } else {
                mHttpResponseCallBack.onFailure(FAILED_GET_DATA, getNetWorkException(MESSAGE_GET_DATA));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mHttpResponseCallBack.onFailure(FAILED_JSON_PARSE, e);
        }
    }

    @Override
    protected void onFailure(int statusCode, Throwable throwable) {
        mHttpResponseCallBack.onFailure(statusCode, throwable);
    }
}
