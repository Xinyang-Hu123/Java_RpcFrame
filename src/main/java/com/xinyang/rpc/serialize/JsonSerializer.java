package com.xinyang.rpc.serialize;


import com.alibaba.fastjson2.JSON;

public class JsonSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }

    @Override
    public byte getType() { return 1; }
}
