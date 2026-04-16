package com.xinyang.rpc.remoting.netty.handler;

import com.xinyang.rpc.common.message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import com.xinyang.rpc.remoting.protocol.RpcMessage;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {

        RpcResponse response = (RpcResponse) msg.getData();
        AttributeKey<RpcResponse> attributeKey = AttributeKey.valueOf("RpcResponse");
        ctx.channel().attr(attributeKey).set(response);
        ctx.channel().close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
