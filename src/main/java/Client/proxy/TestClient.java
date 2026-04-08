package Client.proxy;


import common.pojo.User;
import common.service.Userservice;
import netty.NettyRpcserver;
import org.w3c.dom.ls.LSOutput;
import rpcClient.Impl.NettyRpcClient;

public class TestClient {
    public static void main(String[] args) {
        System.setProperty("kryo.unsafe", "false");
        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 9999,0);
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
