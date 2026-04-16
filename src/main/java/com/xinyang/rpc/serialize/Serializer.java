package com.xinyang.rpc.serialize;

import java.io.IOException;

public interface Serializer {
    byte[] serialize(Object obj);
    <T> T deserialize(byte[] data, Class<T> clazz);
    byte getType(); // 1 = JSON 2 = Kryo
}
