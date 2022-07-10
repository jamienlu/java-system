package com.asura.nio.gateway.server;

import com.asura.nio.gateway.filter.ActiveFilterHandler;
import com.asura.nio.gateway.handler.HttpInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.List;

public class HttpInitializer extends ChannelInitializer<SocketChannel> {
    private List<String> proxys;

    public HttpInitializer(List<String> proxys) {
        this.proxys = proxys;
    }

    @Override
    public void initChannel(SocketChannel socketChannel) {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast(new HttpServerCodec());
        channelPipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        channelPipeline.addLast(new ActiveFilterHandler());
        channelPipeline.addLast(new HttpInboundHandler(this.proxys));
    }
}
