package com.xinyang.rpc.client.Impl;

import com.xinyang.rpc.common.message.RpcRequest;
import com.xinyang.rpc.common.message.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import com.xinyang.rpc.remoting.netty.nettyInitializer.NettyClientInitializer;
import com.xinyang.rpc.remoting.protocol.MessageType;
import com.xinyang.rpc.remoting.protocol.RpcMessage;
import com.xinyang.rpc.client.RpcClient;


public class NettyRpcClient implements RpcClient {

    private String host;
    private int port;
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;


    public NettyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //Netty初始化
    static{
        eventLoopGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }


    private static final byte KYRO_TYPE = 2;
    private static final long TIMEOUT_SECONDS = 5000;


    @Override
    public RpcResponse sendRequest(RpcRequest request) {

        try {
            //创建一个cF对象
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            //发数据

            RpcMessage rpcMessage = RpcMessage.builder()
                    .messageType(MessageType.REQUEST)
                    .serializerType((byte) 2)
                    .requestId(System.currentTimeMillis())
                    .data(request)
                    .build();


            channel.writeAndFlush(rpcMessage).addListener(future -> {
                if(!future.isSuccess()){
                    System.err.println("发送请求失败: " + future.cause());
                }
            });

            boolean success = channel.closeFuture().await(TIMEOUT_SECONDS);
            if(!success){
                channel.close();
                throw new RuntimeException("RPC调用超时 (5秒)");
            }


            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RpcResponse");
            return (RpcResponse) channel.attr(key).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void shutdown() {
        if (eventLoopGroup != null) {
            eventLoopGroup.shutdownGracefully();
            System.out.println("客户端线程池已优雅关闭");
        }
    }
}
