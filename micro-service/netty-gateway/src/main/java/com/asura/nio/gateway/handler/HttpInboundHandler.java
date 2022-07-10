package com.asura.nio.gateway.handler;

import com.asura.nio.gateway.filter.HttpInvalidURLFilter;
import com.asura.nio.gateway.filter.HttpURLFilter;
import com.asura.nio.gateway.route.HttpRouter;
import com.asura.nio.gateway.route.HttpRouterLoadBalancing;
import com.asura.nio.gateway.thread.ThreadPool;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
    private HttpRouter router;
    private HttpURLFilter filter;
    private final List<String> proxys;

    public HttpInboundHandler(List<String> proxys) {
        this.proxys = proxys;
        this.router = new HttpRouterLoadBalancing();
        this.filter = new HttpInvalidURLFilter();

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            handle(fullRequest, ctx, filter);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpURLFilter filter) {
        List<String> urls = proxys;
        System.out.println("filter before:" + urls);
        urls = filter.filter(urls); // 过滤器实现
        System.out.println("filter after:" + urls);
        String url = router.route(urls); // 路由实现
        System.out.println("real url:" + url);
        ThreadPool threadPool = ThreadPool.getInstance();
        threadPool.buildFixThreadPvoidvoiool(12);
        threadPool.getExecutorService().submit(()-> handleGet(fullRequest, ctx, url)); // 丢给对应的服务 -> 处理多线程异步处理
    }

    private void handleGet(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final String url) {
        final HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpClient =  HttpClientBuilder.create().build();
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        try {
            handleResponse(fullRequest, ctx, httpClient.execute(httpGet)); // 处理响应
        } catch (Exception e) {
            handleErrorResponse(ctx, e); // 处理错误发送错误响应
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleErrorResponse(ChannelHandlerContext ctx, Exception e) {
        System.out.println(e.getMessage());
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer("not connect".getBytes()));
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        ctx.flush();
    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final HttpResponse endpointResponse) throws Exception {
        FullHttpResponse response = null;
        try {
            byte[] body = EntityUtils.toByteArray(endpointResponse.getEntity());
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            response.headers().set("Content-Type", endpointResponse.getHeaders("Content-Type"));
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            }
            ctx.flush();
        }

    }


}
