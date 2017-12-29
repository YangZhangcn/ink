package com.zy.lucis.net.request;

import android.support.annotation.NonNull;

import com.zy.lucis.net.Response;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyang on 2017/9/6.
 * 网络请求的请求类
 */
public abstract class Request<T> implements Comparable<Request<T>> {

    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    public static final String HEADER_CONTENT_TYPE = "content-type";

    //编码方式默认为utf-8
    private String paramEncoding = DEFAULT_PARAMS_ENCODING;

    //默认优先级为普通
    protected Priority mPriority = Priority.NORMAL;

    protected int mSerialNum = 0;

    //是否取消请求
    protected boolean isCancel = false;
    //默认请求方法为get
    HttpMethod mHttpMethod = HttpMethod.GET;
    //地址
    private String mUrl;
    //请求头
    private HashMap<String, String> mHeaders = new HashMap<>();
    //请求参数
    private HashMap<String, String> mParams = new HashMap<>();
    //回调接口
    protected RequestListener<T> mRequestListener;

    /**
     * @param mHttpMethod      请求方式
     * @param mUrl             地址
     * @param mRequestListener 回调
     */
    public Request(HttpMethod mHttpMethod, String mUrl, RequestListener<T> mRequestListener) {
        this.mHttpMethod = mHttpMethod;
        this.mUrl = mUrl;
        this.mRequestListener = mRequestListener;
    }

    public final void deliverResponse(Response response) {
        T result = parseResponse(response);
        if (mRequestListener != null) {
            int stCode = response == null ? -1 : response.getStatusLine().getStatusCode();
            String msg = response == null ? "unknown error" : response.getMessage();
            mRequestListener.onComplete(stCode, result, msg);
        }
    }

    @Override
    public int compareTo(@NonNull Request<T> another) {
        Priority myPriority = this.getPriority();
        Priority anotherPriority = another.getPriority();
        //优先级相同时按照添加至队列的序列号顺序来执行
        return myPriority.equals(anotherPriority) ? getSerialNum() - another.getSerialNum() : myPriority.ordinal() - anotherPriority.ordinal();

    }

    public byte[] getBody() {
        HashMap<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            return encodeParams(params, getParamEncoding());
        }
        return null;
    }

    private byte[] encodeParams(Map<String, String> params, String encoding) {
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(URLEncoder.encode(entry.getKey(), encoding));
                sb.append('=');
                sb.append(URLEncoder.encode(entry.getValue(), encoding));
                sb.append('&');
            }
            return sb.toString().getBytes(encoding);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("encoding not supported:"+encoding,e);
        }
    }

    public String getBodyContentType(){
        return "application/x-www-form-urlencoded; charset="+getParamEncoding();
    }

    public Priority getPriority() {
        return mPriority;
    }

    public String getParamEncoding() {
        return paramEncoding;
    }

    public int getSerialNum() {
        return mSerialNum;
    }

    public void setSerialNum(int SerialNum) {
        this.mSerialNum = SerialNum;
    }

    public String getUrl() {
        return mUrl;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public HashMap<String, String> getParams() {
        return mParams;
    }

    /**
     * 结果解析的方法 子类必须实现
     *
     * @param response
     * @return
     */
    public abstract T parseResponse(Response response);

    /**
     * 请求的方法
     */
    public enum HttpMethod {
        GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

        private String mHttpMethod;

        HttpMethod(String method) {
            mHttpMethod = method;
        }

        @Override
        public String toString() {
            return mHttpMethod;
        }
    }

    /**
     * 请求的优先级
     */
    public enum Priority {
        LOW, NORMAL, HIGH, IMMEDIATE
    }

    /**
     * 网络请求listener，在UI线程执行
     *
     * @param <T>
     */
    public interface RequestListener<T> {
        void onComplete(int stCode, T response, String errMsg);
    }
}
