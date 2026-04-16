package com.xinyang.rpc.server.provider.work;

import com.xinyang.rpc.server.provider.server.ServiceProvider;
import com.xinyang.rpc.common.message.RpcRequest;
import com.xinyang.rpc.common.message.RpcResponse;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
@AllArgsConstructor
public class WorkThread implements Runnable {
    private Socket socket;
    private ServiceProvider serviceProvider;

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest = (RpcRequest) ois.readObject();
            RpcResponse response = getResponse(rpcRequest);
            oos.writeObject(response);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException();
        }
    }

    private RpcResponse getResponse(RpcRequest rpcRequest) {
        String interfaceName = rpcRequest.getInterfaceName();
        Object service = serviceProvider.getService(interfaceName);
        Method method = null;
        try {
            method = service.getClass().getMethod(
                    rpcRequest.getMethodName(),
                    rpcRequest.getParamTypes()
            );
            Object invoke = method.invoke(service, rpcRequest.getParams());
            return RpcResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法错误");
            return RpcResponse.fail();
        }

    }
}
