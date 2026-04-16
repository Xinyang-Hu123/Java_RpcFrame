package com.xinyang.rpc.remoting.netty.nettyInitializer;

import com.xinyang.rpc.server.provider.server.ServiceProvider;
import com.xinyang.rpc.remoting.codec.RpcDecoder;
import com.xinyang.rpc.remoting.codec.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;
import com.xinyang.rpc.remoting.netty.handler.NettyServerHandler;

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new RpcDecoder());
        pipeline.addLast(new RpcEncoder());
        pipeline.addLast(new NettyServerHandler(serviceProvider));
    }
}
