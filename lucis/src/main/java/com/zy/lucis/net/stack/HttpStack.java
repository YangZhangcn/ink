package com.zy.lucis.net.stack;

import com.zy.lucis.net.request.Request;
import com.zy.lucis.net.Response;

/**
 * Created by zhangyang on 2017/9/7.
 * 执行网络请求的接口
 */
public interface HttpStack {

    Response performRequest(Request<?> request);

}
