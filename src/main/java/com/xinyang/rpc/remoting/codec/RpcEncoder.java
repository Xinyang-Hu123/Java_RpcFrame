package com.xinyang.rpc.remoting.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import com.xinyang.rpc.remoting.protocol.RpcMessage;
import com.xinyang.rpc.remoting.protocol.protocolConstants;
import com.xinyang.rpc.serialize.JsonSerializer;
import com.xinyang.rpc.serialize.KryoSerializer;
import com.xinyang.rpc.serialize.Serializer;

//协议格式：Magic(4) | Version(1) | MsgType(1) | SerializerType(1) | RequestId(8) | DataLength(4) | Data

public class RpcEncoder extends MessageToByteEncoder<RpcMessage> {

//    private final Serializer serializer;
//
//    public RpcEncoder(Serializer serializer) {
//        this.serializer = serializer;
//    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) throws Exception {
        Serializer serializer = msg.getSerializerType() == 1
                ? new JsonSerializer()
                : new KryoSerializer();

        byte[] data = serializer.serialize(msg.getData());

        //协议头
        out.writeBytes(protocolConstants.MAGIC_NUMBER);           // 4字节魔数
        out.writeByte(protocolConstants.VERSION);                 // 1字节版本
        out.writeByte(msg.getMessageType().getType());            // 1字节消息类型
        out.writeByte(serializer.getType());                      // 1字节序列化类型
        out.writeLong(msg.getRequestId());                        // 8字节请求ID
        out.writeInt(data.length);

        // 写数据
        out.writeBytes(data);


    }
}
