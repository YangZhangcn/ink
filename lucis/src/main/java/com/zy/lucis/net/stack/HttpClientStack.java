package com.zy.lucis.net.stack;

import com.zy.lucis.net.request.Request;
import com.zy.lucis.net.Response;

/**
 * Created by zhangyang on 2017/9/7.
 * api低于9时用httpClient实现
 */
public class HttpClientStack implements HttpStack {
    @Override
    public Response performRequest(Request<?> request) {
        return null;
    }
}
