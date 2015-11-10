package com.socks.jiandan.async;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.support.annotation.Nullable;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.socks.jiandan.utils.NetWorkUtil;

import java.util.List;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class HttpClientProxy implements ResponseCode {

    private static AsyncHttpClient mHttpClient;

    public static void init() {
        synchronized (HttpClientProxy.class) {
            if (mHttpClient == null) {
                mHttpClient = new AsyncHttpClient();
                mHttpClient.setTimeout(15 * 1000);
                mHttpClient.setMaxRetriesAndTimeout(2, 1000);
            }
        }
    }


    public static AsyncHttpClient getInstance() {
        if (mHttpClient == null) {
            init();
        }

        return mHttpClient;
    }

    public static
    @Nullable
    RequestHandle get(Context context, String url, ResponseHandlerInterface responseHandler) {

        if (NetWorkUtil.isNetWorkConnected(context)) {
            return mHttpClient.get(url, responseHandler);
        } else {
            responseHandler.sendFailureMessage(FAILED_NO_NETWORK, null, null, new NetworkErrorException(MESSAGE_NO_NETWORK));
            return null;
        }
    }

    public static RequestHandle post(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        if (NetWorkUtil.isNetWorkConnected(context)) {
            return mHttpClient.post(context, url, params, responseHandler);
        } else {
            responseHandler.sendFailureMessage(FAILED_NO_NETWORK, null, null, new NetworkErrorException(MESSAGE_NO_NETWORK));
            return null;
        }
    }

    public static void cancelRequest(List<RequestHandle> handles) {
        if (handles != null && handles.size() > 0) {
            for (RequestHandle requestHandle : handles) {
                if (!requestHandle.isFinished()) {
                    requestHandle.cancel(true);
                }
            }
        }
    }

}
