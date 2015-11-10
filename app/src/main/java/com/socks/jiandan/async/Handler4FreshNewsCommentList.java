package com.socks.jiandan.async;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.callback.OnHttpResponseCallBack;
import com.socks.jiandan.model.Comment4FreshNews;
import com.socks.jiandan.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class Handler4FreshNewsCommentList extends BaseJsonResponseHandler {

    private LoadFinishCallBack mCallBack;

    public Handler4FreshNewsCommentList(@NonNull OnHttpResponseCallBack<ArrayList<Comment4FreshNews>> onHttpResponseCallBack, LoadFinishCallBack callBack) {
        super(onHttpResponseCallBack);
        mCallBack = callBack;
    }

    @Override
    protected void onSuccess(int statusCode, String rawJsonResponse) {

        try {
            JSONObject resultObj = new JSONObject(rawJsonResponse);

            String status = resultObj.optString("status");

            if (status.equals("ok")) {
                String commentsStr = resultObj.optJSONObject("post").optJSONArray("comments")
                        .toString();
                int id = resultObj.optJSONObject("post").optInt("id");
                mCallBack.loadFinish(Integer.toString(id));

                ArrayList<Comment4FreshNews> comment4FreshNewses = (ArrayList<Comment4FreshNews>) JSONParser.toObject(commentsStr,
                        new TypeToken<ArrayList<Comment4FreshNews>>() {
                        }.getType());

                Pattern pattern = Pattern.compile("\\d{7}");

                for (Comment4FreshNews comment4FreshNews : comment4FreshNewses) {
                    Matcher matcher = pattern.matcher(comment4FreshNews.getContent());
                    boolean isHas7Num = matcher.find();
                    boolean isHasCommentStr = comment4FreshNews.getContent().contains("#comment-");
                    //有回复
                    if (isHas7Num && isHasCommentStr || comment4FreshNews.getParentId() != 0) {
                        ArrayList<Comment4FreshNews> tempComments = new ArrayList<>();
                        int parentId = getParentId(comment4FreshNews.getContent());
                        comment4FreshNews.setParentId(parentId);
                        getParenFreshNews(tempComments, comment4FreshNewses, parentId);
                        Collections.reverse(tempComments);
                        comment4FreshNews.setParentComments(tempComments);
                        comment4FreshNews.setFloorNum(tempComments.size() + 1);
                        comment4FreshNews.setContent(getContentWithParent(comment4FreshNews.getContent()));
                    } else {
                        comment4FreshNews.setContent(getContentOnlySelf(comment4FreshNews.getContent()));
                    }
                }

                mHttpResponseCallBack.onSuccess(SUCCESS, comment4FreshNewses);
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

    private void getParenFreshNews(ArrayList<Comment4FreshNews> tempComments, ArrayList<Comment4FreshNews> comment4FreshNewses, int parentId) {

        for (Comment4FreshNews comment4FreshNews : comment4FreshNewses) {
            if (comment4FreshNews.getId() != parentId) continue;
            //找到了父评论
            tempComments.add(comment4FreshNews);
            //父评论又有父评论
            if (comment4FreshNews.getParentId() != 0 && comment4FreshNews.getParentComments() != null) {
                comment4FreshNews.setContent(getContentWithParent(comment4FreshNews.getContent()));
                tempComments.addAll(comment4FreshNews.getParentComments());
                return;
            }
            //父评论没有父评论了
            comment4FreshNews.setContent(getContentOnlySelf(comment4FreshNews.getContent()));
        }
    }


    private int getParentId(String content) {
        try {
            int index = content.indexOf("comment-") + 8;
            int parentId = Integer.parseInt(content.substring(index, index + 7));
            return parentId;
        } catch (Exception ex) {
            return 0;
        }
    }


    private String getContentWithParent(String content) {
        if (content.contains("</a>:"))
            return getContentOnlySelf(content).split("</a>:")[1];
        return content;
    }

    private String getContentOnlySelf(String content) {
        content = content.replace("</p>", "");
        content = content.replace("<p>", "");
        content = content.replace("<br />", "");
        return content;
    }
}
