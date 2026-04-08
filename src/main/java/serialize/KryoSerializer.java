package serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import common.message.RpcRequest;
import common.message.RpcResponse;
import common.pojo.User;

public class KryoSerializer implements Serializer {

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() ->{
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
//        kryo.register(RpcRequest.class);
//        kryo.register(RpcResponse.class);
//        todo 新添加部分
//        kryo.register(Class[].class);   // 解决 Class is not registered: java.lang.Class[] 报错
//        kryo.register(Object[].class);  // 解决 params 数组无法序列化的问题
//        kryo.register(Class.class);     // 注册单 Class 类型
//
//        kryo.register(User.class);
//
        return kryo;
    });



    @Override
    public byte[] serialize(Object obj) {
        Output output = new Output(1024,-1);
        KRYO_THREAD_LOCAL.get().writeObject(output,obj);
        return output.toBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Input input = new Input(bytes);
        return KRYO_THREAD_LOCAL.get().readObject(input,clazz);

    }

    @Override
    public byte getType() {
        return 2;
    }
}
