package com.xinyang.rpc.client.proxy;


import com.alibaba.nacos.api.exception.NacosException;
import com.xinyang.rpc.common.pojo.User;
import com.xinyang.rpc.common.service.Userservice;
import com.xinyang.rpc.client.Impl.NettyRpcClient;

public class TestClient {
    public static void main(String[] args) throws NacosException {
        System.setProperty("kryo.unsafe", "false");
        ClientProxy clientProxy = new ClientProxy("user-service");
        Userservice proxy = clientProxy.getProxy(Userservice.class);

        User user = proxy.getUserById(1);
        System.out.println(user);

        User u = User.builder()
                .id(100)
                .userName("hhh")
                .sex(true)
                .build();

        Integer id = proxy.insertUserId(u);
        System.out.println("想服务端插入user id 为 ：" + id);
        NettyRpcClient.shutdown();
    }





}
