package netty.nettyInitializer;

import Server.provider.server.ServiceProvider;
import codec.RpcDecoder;
import codec.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;
import netty.handler.NettyClientHandler;
import netty.handler.NettyServerHandler;
import rpcClient.Impl.NettyRpcClient;
import serialize.KryoSerializer;

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
