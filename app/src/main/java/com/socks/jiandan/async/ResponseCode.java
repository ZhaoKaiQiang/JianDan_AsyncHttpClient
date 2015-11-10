package com.socks.jiandan.async;

/**
 * Created by zhaokaiqiang on 15/11/10.
 */
public interface ResponseCode {

    int FAILED_NO_NETWORK = 404;
    int FAILED_JSON_PARSE = 405;
    int FAILED_GET_DATA = 406;
    int FAILED_MALFORMED_URL_EXCEPTION = 407;


    int SUCCESS = 200;

    String MESSAGE_NO_NETWORK = "无网络";
    String MESSAGE_GET_DATA = "获取数据失败";
}
