package com.asura.nio.http;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpWeek2_2 {
    public static void main(String[] args) {
        invokeHttpCliendGet(); // httpclient访问
        invokeOkHttpGet(); // okhttp访问
    }
    private static void invokeHttpCliendGet() {
        CloseableHttpClient httpClient =  HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://localhost:8801/");
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                System.out.println("http client response length" + entity.getContentLength());
                System.out.println("http client response content" + EntityUtils.toString(entity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void invokeOkHttpGet() {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://localhost:8801/").build();
        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println("ok http response length" + response.body().contentLength());
            System.out.println("ok http response content" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // todo 关闭资源 避免server 抛出java.io.IOException: 远程主机强迫关闭了一个现有的连接
    }
}
