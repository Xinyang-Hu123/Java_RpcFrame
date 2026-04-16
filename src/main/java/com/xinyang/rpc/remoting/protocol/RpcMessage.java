package com.xinyang.rpc.remoting.protocol;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class RpcMessage implements Serializable {
    private MessageType messageType;
    private byte serializerType; // 1 = JSON 2 = Kryo
    private Long requestId;
    private Object data;  //RpcRequest or RpcResponse
}
