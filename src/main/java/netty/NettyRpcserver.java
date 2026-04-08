package netty;

import Server.provider.server.RpcServer;
import Server.provider.server.ServiceProvider;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.nettyInitializer.NettyServerInitializer;

public class NettyRpcserver implements RpcServer {
    private final ServiceProvider serviceProvider;
    public NettyRpcserver(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        EventLoopGroup bossGroup = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
        EventLoopGroup workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new NettyServerInitializer(serviceProvider));
            //绑定端口
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            System.out.println("Netty 服务端启动....监听端口：  " + port);
            //等待服务端关闭
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e){
            throw new RuntimeException(e);
        } finally{
            //优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }



    }

    @Override
    public void stop() {

    }
}
