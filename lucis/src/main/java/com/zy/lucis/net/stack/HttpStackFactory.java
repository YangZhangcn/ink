package com.zy.lucis.net.stack;

import android.os.Build;

/**
 * Created by zhangyang on 2017/9/7.
 */

public final class HttpStackFactory {

    public static HttpStack createHttpStack(){
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 9){
            return new HttpUrlConnStack();
        }
        return new HttpClientStack();
    }
}
