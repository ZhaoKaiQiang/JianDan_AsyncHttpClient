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

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public class HttpClientProxy implements ResponseCode {

    public static final int METHOD_GET = 0;
    public static final int METHOD_POST = 1;

    private static AsyncHttpClient mHttpClient;

    public static void init() {
        synchronized (HttpClientProxy.class) {
            if (mHttpClient == null) {
                mHttpClient = new AsyncHttpClient();
                mHttpClient.setTimeout(15 * 1000);
                mHttpClient.setMaxRetriesAndTimeout(1, 1000);

                HttpClient client = mHttpClient.getHttpClient();
                if (client instanceof DefaultHttpClient) {
                    mHttpClient.setEnableRedirects(true, true, true);
                }
            }
        }
    }

    public static AsyncHttpClient getInstance() {
        if (mHttpClient == null) {
            init();
        }

        return mHttpClient;
    }

    @Nullable
    public static RequestHandle get(Context context, String url, ResponseHandlerInterface responseHandler) {
        return call(METHOD_GET, context, url, null, responseHandler);
    }

    @Nullable
    public static RequestHandle get(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        return call(METHOD_GET, context, url, params, responseHandler);
    }

    @Nullable
    public static RequestHandle post(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        return call(METHOD_POST, context, url, params, responseHandler);
    }

    @Nullable
    private static RequestHandle call(int method, Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {


        if (NetWorkUtil.isNetWorkConnected(context)) {
            switch (method) {
                case METHOD_GET:
                    if (params == null) {
                        return getInstance().get(context, url, responseHandler);
                    } else {
                        return getInstance().get(context, url, params, responseHandler);
                    }
                case METHOD_POST:
                    return getInstance().post(context, url, params, responseHandler);
                default:
                    return null;
            }
        } else {
            responseHandler.sendFailureMessage(FAILED_NO_NETWORK, null, null, new NetworkErrorException(MESSAGE_NO_NETWORK));
            return null;
        }
    }

    public static void cancelRequest(RequestHandle... handles) {
        if (handles != null && (handles.length > 0)) {
            for (RequestHandle requestHandle : handles) {
                if ((requestHandle != null) && (!requestHandle.isFinished()) && (!requestHandle.isCancelled())) {
                    requestHandle.cancel(true);
                }
            }
        }
    }

    public static void cancelRequest(List<RequestHandle> handles) {
        if (handles != null && (handles.size() > 0)) {
            for (RequestHandle requestHandle : handles) {
                if ((requestHandle != null) && (!requestHandle.isFinished()) && (!requestHandle.isCancelled())) {
                    requestHandle.cancel(true);
                }
            }
        }
    }

    public static void cancelRequest(Object tag) {
        getInstance().cancelRequestsByTAG(tag, true);
    }

}
