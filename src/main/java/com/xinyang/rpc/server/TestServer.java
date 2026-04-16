package com.xinyang.rpc.server;

import com.xinyang.rpc.registry.nacos.NacosServiceRegistry;
import com.xinyang.rpc.server.provider.server.RpcServer;
import com.xinyang.rpc.server.provider.server.ServiceProvider;
import com.alibaba.nacos.api.exception.NacosException;
import com.xinyang.rpc.common.impl.UserServiceImpl;
import com.xinyang.rpc.common.service.Userservice;
import com.xinyang.rpc.remoting.netty.NettyRpcserver;

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
