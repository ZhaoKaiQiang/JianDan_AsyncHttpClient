package com.socks.jiandan.async;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.callback.OnHttpResponseCallBackImpl;
import com.socks.jiandan.model.Commentator;
import com.socks.jiandan.utils.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class Handler4CommentList extends BaseJsonResponseHandler {

    private LoadFinishCallBack callBack;

    public Handler4CommentList(@NonNull OnHttpResponseCallBackImpl<ArrayList<Commentator>> onHttpResponseCallBack, LoadFinishCallBack callBack) {
        super(onHttpResponseCallBack);
        this.callBack = callBack;
    }

    @Override
    protected void onSuccess(int statusCode, String rawJsonResponse) {

        try {
            JSONObject resultJson = new JSONObject(rawJsonResponse);
            String allThreadId = resultJson.getString("response").replace("[", "").replace
                    ("]", "").replace("\"", "");
            String[] threadIds = allThreadId.split("\\,");

            callBack.loadFinish(resultJson.optJSONObject("thread").optString("thread_id"));

            if (TextUtils.isEmpty(threadIds[0])) {
                mHttpResponseCallBack.onSuccess(SUCCESS, new ArrayList<Commentator>());
            } else {

                //然后根据thread_id再去获得对应的评论和作者信息
                JSONObject parentPostsJson = resultJson.getJSONObject("parentPosts");
                //找出热门评论
                String hotPosts = resultJson.getString("hotPosts").replace("[", "").replace
                        ("]", "").replace("\"", "");
                String[] allHotPosts = hotPosts.split("\\,");

                ArrayList<Commentator> commentators = new ArrayList<>();
                List<String> allHotPostsArray = Arrays.asList(allHotPosts);

                for (String threadId : threadIds) {
                    Commentator commentator = new Commentator();
                    JSONObject threadObject = parentPostsJson.getJSONObject(threadId);

                    //解析评论，打上TAG
                    if (allHotPostsArray.contains(threadId)) {
                        commentator.setTag(Commentator.TAG_HOT);
                    } else {
                        commentator.setTag(Commentator.TAG_NORMAL);
                    }

                    commentator.setPost_id(threadObject.optString("post_id"));
                    commentator.setParent_id(threadObject.optString("parent_id"));

                    String parentsString = threadObject.optString("parents").replace("[", "").replace
                            ("]", "").replace("\"", "");

                    String[] parents = parentsString.split("\\,");
                    commentator.setParents(parents);

                    //如果第一个数据为空，则只有一层
                    if (TextUtil.isNull(parents[0])) {
                        commentator.setFloorNum(1);
                    } else {
                        commentator.setFloorNum(parents.length + 1);
                    }

                    commentator.setMessage(threadObject.optString("message"));
                    commentator.setCreated_at(threadObject.optString("created_at"));
                    JSONObject authorObject = threadObject.optJSONObject("author");
                    commentator.setName(authorObject.optString("name"));
                    commentator.setAvatar_url(authorObject.optString("avatar_url"));
                    commentator.setType(Commentator.TYPE_NORMAL);
                    commentators.add(commentator);
                }

                mHttpResponseCallBack.onSuccess(SUCCESS, commentators);
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
