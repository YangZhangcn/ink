package com.zy.lucis.net;

import android.util.Log;

import com.zy.lucis.net.request.Request;
import com.zy.lucis.net.stack.HttpStack;

import java.util.concurrent.BlockingQueue;

/**
 * Created by zhangyang on 2017/9/7.
 */

public class NetworkExecutor extends Thread {

    //网络请求队列
    private BlockingQueue<Request<?>> mRequestQueue;
    //网络请求栈
    private HttpStack httpStack;

    private static ResponseDeliver mResponseDeliver = new ResponseDeliver();

    //是否停止
    private boolean isStop = false;

    public NetworkExecutor(BlockingQueue<Request<?>> mRequestQueue, HttpStack httpStack) {
        this.mRequestQueue = mRequestQueue;
        this.httpStack = httpStack;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (!isStop) {
                Request<?> request = mRequestQueue.take();
                if (request.isCancel()){
                    Log.d("Lucis","取消执行");
                    continue;
                }
                Response response = httpStack.performRequest(request);
                mResponseDeliver.deliverResponse(request,response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void quit(){
        isStop = true;
        interrupt();
    }


}
