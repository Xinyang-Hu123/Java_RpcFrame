package Server;

import Server.impl.SimpleRPCServer;
import Server.provider.server.RpcServer;
import Server.provider.server.ServiceProvider;
import common.impl.UserServiceImpl;
import common.service.Userservice;
import netty.NettyRpcserver;
import netty.handler.NettyServerHandler;
import rpcClient.Impl.NettyRpcClient;

public class TestServer {
    public static void main(String[] args) {
        System.setProperty("kryo.unsafe", "false");
        Userservice userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer = new NettyRpcserver(serviceProvider);
        rpcServer.start(9999);

    }
}
