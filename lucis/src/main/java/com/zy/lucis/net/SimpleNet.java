package com.zy.lucis.net;

/**
 * Created by zhangyang on 2017/9/7.
 * simpleNet使用入口类
 */
public class SimpleNet {
    private static RequestQueue mRequestQueue = new RequestQueue(Runtime.getRuntime().availableProcessors()+1,HttpStackFactory.createHttpStack());

    public static void init(){
        mRequestQueue.start();
    }

    public static void add(Request<?> request){
        mRequestQueue.addRequest(request);
    }
}
