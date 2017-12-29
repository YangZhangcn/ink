package com.zy.lucis.net.stack;

import com.zy.lucis.net.request.Request;
import com.zy.lucis.net.Response;

import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicStatusLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

/**
 * Created by zhangyang on 2017/9/7.
 * 用httpUrlConnection实现的httpStack
 */
public class HttpUrlConnStack implements HttpStack {
    @Override
    public Response performRequest(Request<?> request) {
        HttpURLConnection urlConnection = null;
        if (request == null || request.getHttpMethod() == null){
            return null;
        }
        try {
            //新建httpURLConnection
            urlConnection = createUrlConnection(request);
            //设置请求头参数
            setRequestHeaders(urlConnection,request);
            //设置请求体参数
            setRequestParams(urlConnection,request);

            return fetchResponse(urlConnection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpURLConnection createUrlConnection(Request<?> request) throws IOException {
        URL url = new URL(request.getUrl());
        URLConnection urlConnection = url.openConnection();
        urlConnection.setConnectTimeout(30000);
        urlConnection.setReadTimeout(60000);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        return (HttpURLConnection) urlConnection;
    }

    private void setRequestHeaders(HttpURLConnection urlConnection, Request<?> request){
        Set<String> headerKeys = request.getHeaders().keySet();
        for (String headerName : headerKeys){
            urlConnection.addRequestProperty(headerName,request.getHeaders().get(headerName));
        }
    }

    private void setRequestParams(HttpURLConnection urlConnection, Request<?> request) throws IOException {
        Request.HttpMethod httpMethod = request.getHttpMethod();
        urlConnection.setRequestMethod(httpMethod.toString());
        byte[] body = request.getBody();
        if (body != null){
            urlConnection.setDoOutput(true);
            urlConnection.addRequestProperty(Request.HEADER_CONTENT_TYPE,request.getBodyContentType());
            DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream() );
            dataOutputStream.write(body);
            dataOutputStream.close();
        }

    }

    private Response fetchResponse(HttpURLConnection connection) throws IOException {
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP",1,1);
        int responseCode = connection.getResponseCode();
        if (responseCode == -1){
            throw new IOException("could not retrieve response code from HttpUrlConnection.");
        }
        BasicStatusLine basicStatusLine = new BasicStatusLine(protocolVersion, responseCode, connection.getResponseMessage());
        Response response = new Response(basicStatusLine);
        response.setEntity(entityFromConnection(connection));
        return response;
    }

    private HttpEntity entityFromConnection(HttpURLConnection connection){
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            inputStream = connection.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength(connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }
}
