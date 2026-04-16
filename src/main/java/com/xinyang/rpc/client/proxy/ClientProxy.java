package com.xinyang.rpc.client.proxy;

import com.xinyang.rpc.registry.nacos.NacosServiceRegistry;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xinyang.rpc.common.message.RpcRequest;
import com.xinyang.rpc.common.message.RpcResponse;
import lombok.AllArgsConstructor;
import com.xinyang.rpc.client.Impl.NettyRpcClient;
import com.xinyang.rpc.client.Impl.SimpleSocketRpcClient;
import com.xinyang.rpc.client.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    private String host;
    private int port;
    private RpcClient rpcClient;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramTypes(method.getParameterTypes()).build();
        RpcResponse response = rpcClient.sendRequest(request);
        //防御性判空与状态码校验
        if(response == null){
            throw new RuntimeException("服务调用失败，返回为null");
        }
        //状态码检测
        if(response.getCode() != 200){
            throw new RuntimeException("服务端执行异常: " + response.getMsg());
        }
        return response.getData();
    }

    public <T> T getProxy(Class<T> clazz) {
        Object o = Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                this);
        return (T) o;
    }
    //以下为新加的
    public ClientProxy(String host, int port,int choose) {
        switch (choose) {
            case 0:
                rpcClient = new NettyRpcClient(host, port);
                break;
                case 1:
                    rpcClient = new SimpleSocketRpcClient(host, port);
                    break;
        }
    }
    public ClientProxy(String host, int port) {
        rpcClient = new NettyRpcClient(host, port);
    }

    public ClientProxy(String serviceName) throws NacosException {
        NacosServiceRegistry registry = new NacosServiceRegistry();
        Instance instance = registry.discovery(serviceName);
        String host = instance.getIp();
        int port = instance.getPort();
        this.rpcClient = new NettyRpcClient(host, port);

    }



}
