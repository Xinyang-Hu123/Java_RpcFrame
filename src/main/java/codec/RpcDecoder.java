package codec;

import common.message.RpcRequest;
import common.message.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import protocol.MessageType;
import protocol.RpcMessage;
import protocol.protocolConstants;
import serialize.JsonSerializer;
import serialize.KryoSerializer;
import serialize.Serializer;

import java.util.Arrays;
import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() < protocolConstants.HEADER_LENGTH) {
            return;
        }
        in.markReaderIndex();

        //校验魔数
        byte[] magic = new byte[4];
        in.readBytes(magic);
        if(!Arrays.equals(magic, protocolConstants.MAGIC_NUMBER)){
            throw new RuntimeException("非法数据包：魔数不匹配");
        }

        byte version = in.readByte();
        byte msgType = in.readByte();
        byte serializationType = in.readByte();
        long requestId = in.readLong();
        int dataLength = in.readInt();




        // 协议头校验
        if (version != protocolConstants.VERSION) {
            throw new RuntimeException("不支持的协议版本: " + version);
        }
        // 校验序列化类型 (只能是 1 或者 2)
        if (serializationType != 1 && serializationType != 2) {
            throw new RuntimeException("未知的序列化类型: " + serializationType);
        }
        // 校验数据长度 (防 OOM 攻击 限制最大 5MB)
        if (dataLength < 0 || dataLength > 5 * 1024 * 1024) {
            throw new RuntimeException("数据长度不合法或过大: " + dataLength);
        }


        //数据还没到完全就开始等
        if(in.readableBytes() < dataLength){
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[dataLength];
        in.readBytes(data);

        //根据序列化类型选择反序列化器
        Serializer serializer = serializationType == 1
                ? new JsonSerializer()
                : new KryoSerializer();

        //根据消息类型进行反序列化

        Class<?> clazz = msgType == MessageType.REQUEST.getType()
                ? RpcRequest.class
                : RpcResponse.class;

        Object obj = serializer.deserialize(data, clazz);

        RpcMessage message = RpcMessage.builder()
                .messageType(msgType == MessageType.REQUEST.getType()
                ? MessageType.REQUEST : MessageType.RESPONSE)
                .serializerType(serializationType)
                .requestId(requestId)
                .data(obj)
                .build();

        out.add(message);

    }
}
