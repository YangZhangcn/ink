package com.zy.lucis.net.request;

import com.zy.lucis.net.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangyang on 2017/9/7.
 */
public class JsonRequest extends Request<JSONObject> {
    /**
     * @param mHttpMethod      请求方式
     * @param mUrl             地址
     * @param mRequestListener 回调
     */
    public JsonRequest(HttpMethod mHttpMethod, String mUrl, RequestListener mRequestListener) {
        super(mHttpMethod, mUrl, mRequestListener);
    }

    @Override
    public JSONObject parseResponse(Response response) {
        if (response == null){
            return null;
        }
        String jsonString = new String(response.getRawData());
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
