package com.zy.lucis.net;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.zy.lucis.net.request.Request;

import java.util.concurrent.Executor;

/**
 * Created by zhangyang on 2017/9/7.
 */

public class ResponseDeliver implements Executor {

    Handler mHandler = new Handler(Looper.getMainLooper());


    public void deliverResponse(final Request<?> request, final Response response) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (request != null)
                    request.deliverResponse(response);
            }
        };
        execute(runnable);
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mHandler.post(command);
    }
}
