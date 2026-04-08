package protocol;

public class protocolConstants {
    //自定义协议
    public static final byte[] MAGIC_NUMBER = {(byte) 'h',(byte) 'x',(byte) 'y',(byte) '1'};
    public static final byte VERSION = 1;   //万一以后升级呢
    //协议头 4+1+1(请求类型)+1(序列化类型)+8(消息唯一ID)+4(数据字节长度) ==  19byte
    public static final int HEADER_LENGTH = 19;
}
