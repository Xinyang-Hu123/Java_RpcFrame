package Server;

import Nacos.NacosServiceRegistry;
import Server.impl.SimpleRPCServer;
import Server.provider.server.RpcServer;
import Server.provider.server.ServiceProvider;
import com.alibaba.nacos.api.exception.NacosException;
import common.impl.UserServiceImpl;
import common.service.Userservice;
import netty.NettyRpcserver;
import netty.handler.NettyServerHandler;
import rpcClient.Impl.NettyRpcClient;

public class TestServer {
    public static void main(String[] args) throws NacosException {
        System.setProperty("kryo.unsafe", "false");
        Userservice userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);


        NacosServiceRegistry registry = new NacosServiceRegistry();
        registry.register("user-service", "127.0.0.1", 9999);
        System.out.println("服务已注册到Nacos");


        RpcServer rpcServer = new NettyRpcserver(serviceProvider);
        rpcServer.start(9999);

    }
}
