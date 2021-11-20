package com.asura.rpc.cilent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.asura.rpc.api.Filter;
import com.asura.rpc.api.RpcfxRequest;
import com.asura.rpc.api.RpcfxResponse;
import com.asura.rpc.exception.RPCException;

import com.asura.rpc.netty.NettyClient;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class ByteBuddyIntercept implements InvocationHandler {
    static {
        ParserConfig.getGlobalInstance().addAccept("com.asura");
    }
    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    private final Class<?> serviceClass;
    private final String url;
    private final Filter[] filters;

    public <T> ByteBuddyIntercept(Class<T> serviceClass, String url, Filter... filters) {
        this.serviceClass = serviceClass;
        this.url = url;
        this.filters = filters;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(this.serviceClass.getName());
        request.setMethod(method.getName());
        request.setParams(args);
        if (null!=filters) {
            for (Filter filter : filters) {
                if (!filter.filter(request)) {
                    return null;
                }
            }
        }
        RpcfxResponse response = post(request, url);
        return response.getResult();
    }

    private RpcfxResponse post(RpcfxRequest req, String url) throws MalformedURLException, InterruptedException {
        // netty实现
        URL url1 = new URL(url);
        RpcfxResponse rpcfxResponse =  new NettyClient().invokeService(req, url1.getHost(),url1.getPort());
        return rpcfxResponse;
        /*RpcfxResponse rpcfxResponse = new RpcfxResponse();
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: "+reqJson);

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = null;
        try {
            respJson = client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            rpcfxResponse.setStatus(false);
            rpcfxResponse.setException(RPCException.builder(e.toString(), e.getMessage()));
            return rpcfxResponse;
        }
        System.out.println("resp json: "+respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);*/
    }

}
