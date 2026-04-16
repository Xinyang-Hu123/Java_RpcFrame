package com.xinyang.rpc.server.impl;

import com.xinyang.rpc.server.provider.server.RpcServer;
import com.xinyang.rpc.server.provider.server.ServiceProvider;
import com.xinyang.rpc.server.provider.work.WorkThread;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


@AllArgsConstructor
public class SimpleRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;

    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务启动了");
            while (true){
                Socket socket = serverSocket.accept();
                new Thread(new WorkThread(socket,serviceProvider)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }
}
