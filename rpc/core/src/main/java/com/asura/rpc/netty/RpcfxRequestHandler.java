package com.asura.rpc.netty;

import com.asura.rpc.api.RpcfxResponse;
import com.asura.rpc.hession.SerializeUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class RpcfxRequestHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            RpcfxResponse rpcfxResponse =(RpcfxResponse) msg;
            if (rpcfxResponse.getName() != null) {
                NettyClient.put(rpcfxResponse.getName(), rpcfxResponse);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
            ctx.close();
        }
    }
}
