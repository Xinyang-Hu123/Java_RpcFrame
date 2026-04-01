package Server;

import Server.impl.SimpleRPCServer;
import Server.provider.server.RpcServer;
import Server.provider.server.ServiceProvider;
import common.impl.UserServiceImpl;
import common.service.Userservice;

public class TestServer {
    public static void main(String[] args) {
        Userservice userService = new UserServiceImpl();
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);
        RpcServer rpcServer = new SimpleRPCServer(serviceProvider);
        rpcServer.start(9999);

    }
}
