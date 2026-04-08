package rpcClient.Impl;

import common.message.RpcRequest;
import common.message.RpcResponse;
import rpcClient.RpcClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SimpleSocketRpcClient implements RpcClient {

    String host = "127.0.0.1";
    int port = 8080;
    public SimpleSocketRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try {
            Socket socket = new Socket(host,port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(request);
            oos.flush();

            RpcResponse response = (RpcResponse) ois.readObject();
            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
