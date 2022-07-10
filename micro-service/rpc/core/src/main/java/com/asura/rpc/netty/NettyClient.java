package com.asura.rpc.netty;

import com.asura.rpc.api.RpcfxRequest;
import com.asura.rpc.api.RpcfxResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyClient {
    // 异步  获取响应的数据源
    private static final Map<String, RpcfxResponse> map = new ConcurrentHashMap<String, RpcfxResponse>();

    public static void put(String uuid, RpcfxResponse object) {
        map.put(uuid, object);
    }

    public RpcfxResponse invokeService(RpcfxRequest request, String host, int port) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = buildChannelFuture(host, port, workerGroup);
            channelFuture.channel().write(request);
            channelFuture.channel().flush();
            channelFuture.channel().closeFuture().sync(); // 是否存在传输耗时 需要判断map异步取+超时
            return map.get(request.getServiceClass());
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void registServices(List<Class> classList, String host, int port) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = buildChannelFuture(host, port, workerGroup);
            channelFuture.channel().write(classList);
            channelFuture.channel().flush();
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private ChannelFuture buildChannelFuture(String host, int port, EventLoopGroup workerGroup) throws InterruptedException {
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                //对象参数类型编码器
                pipeline.addLast("encoder",new ObjectEncoder());
                //对象参数类型解码器
                pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                pipeline.addLast(new RpcfxRequestHandler()); // server处理
            }
        });
        System.out.println("connect:" + host + "_" + port);
        return b.connect(host, port).sync();
    }
}
