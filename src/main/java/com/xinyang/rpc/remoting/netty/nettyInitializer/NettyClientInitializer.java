package com.xinyang.rpc.remoting.netty.nettyInitializer;

import com.xinyang.rpc.remoting.codec.RpcDecoder;
import com.xinyang.rpc.remoting.codec.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import com.xinyang.rpc.remoting.netty.handler.NettyClientHandler;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new RpcEncoder());
        pipeline.addLast(new RpcDecoder());
        pipeline.addLast(new NettyClientHandler());

    }
}
