package netty.handler;

import common.message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import protocol.RpcMessage;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        //todo 测试记得删
        System.out.println("客户端Handler收到响应：" + msg);

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
