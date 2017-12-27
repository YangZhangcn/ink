package com.zy.lucis.net;

import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by zhangyang on 2017/9/7.
 * 请求结果类 结果处孙在rawData中
 */
public class Response extends BasicHttpResponse {
    //原始的response主体数据
    private byte[] rawData = new byte[0];

    private String message;

    public Response(StatusLine statusline) {
        super(statusline);
    }

    public Response(ProtocolVersion ver, int code, String reason) {
        super(ver, code, reason);
        message = reason;
    }

    @Override
    public void setEntity(HttpEntity entity) {
        super.setEntity(entity);
        rawData = entityToBytes(entity);
    }

    public byte[] getRawData() {
        return rawData;
    }

    public String getMessage() {
        return message;
    }

    private byte[] entityToBytes(HttpEntity entity){
        try {
            return EntityUtils.toByteArray(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
