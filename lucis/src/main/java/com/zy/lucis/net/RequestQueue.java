package com.zy.lucis.net;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhangyang on 2017/9/7.
 * 请求队列
 */
public final class RequestQueue {

    //请求队列
    private BlockingQueue<Request<?>> mRequestQueue = new PriorityBlockingQueue<>();
    //请求序列化生成器
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);
    //默认核心数
    public static int DEFAULT_CORE_NUM = Runtime.getRuntime().availableProcessors() + 1;

    private int mDisPatcherNum = DEFAULT_CORE_NUM;

    //执行网络请求的线程
    private NetworkExecutor[] mDispatchers = null;
    //网络请求的执行者
    private HttpStack mHttpStack;

    public RequestQueue(int coreNum, HttpStack httpStack) {
        this.mDisPatcherNum = coreNum;
        this.mHttpStack = httpStack == null ? HttpStackFactory.createHttpStack():httpStack;
    }

    private void startNetworkExecutors(){
        mDispatchers = new NetworkExecutor[mDisPatcherNum];
        for (int i = 0; i < mDisPatcherNum; i++) {
            mDispatchers[i] = new NetworkExecutor(mRequestQueue,mHttpStack);
            mDispatchers[i].start();
        }
    }

    public void start(){
        stop();
        startNetworkExecutors();
    }

    public void stop(){
        if (mDispatchers != null && mDispatchers.length > 0){
            for (int i = 0; i < mDisPatcherNum; i++) {
                mDispatchers[i].quit();
            }
        }
    }

    public void addRequest(Request<?> request){
        if (!mRequestQueue.contains(request)){
            request.setSerialNum(generateSrialNumber());
            mRequestQueue.add(request);
        }else {
            Log.d("Lucis","请求队列中已经有该请求");
        }
    }

    private int generateSrialNumber(){
        return mSerialNumGenerator.incrementAndGet();
    }
}
