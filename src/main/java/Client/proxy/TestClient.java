package Client.proxy;


import common.pojo.User;
import common.service.Userservice;
import org.w3c.dom.ls.LSOutput;

public class TestClient {
    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy("127.0.0.1",9999);
        Userservice proxy = clientProxy.getProxy(Userservice.class);

        User user = proxy.getUserById(1);
        System.out.println("从服务端得到的user = " + user.toString());

        User u = User.builder()
                .id(100)
                .userName("wxx")
                .sex(true)
                .build();
        Integer id = proxy.insertUserId(u);
        System.out.println("想用户端插入user的id = " + id);

    }





}
