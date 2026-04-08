package netty.handler;

import Server.provider.server.ServiceProvider;
import common.message.RpcRequest;
import common.message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import protocol.MessageType;
import protocol.RpcMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private ServiceProvider serviceProvider;



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {


        RpcRequest request = (RpcRequest) msg.getData();
        //Todo 测试输出，记得删
        System.out.println("服务端收到请求：" + request);
        RpcResponse responseData = getResponse(request);
        RpcMessage responseMsg = RpcMessage.builder()
                .messageType(MessageType.RESPONSE)
                .serializerType(msg.getSerializerType())
                .requestId(msg.getRequestId())
                .data(responseData)
                .build();
        ctx.writeAndFlush(responseMsg);
    }


    private RpcResponse getResponse(RpcRequest request){
        String interfaceName = request.getInterfaceName();
        Object service = serviceProvider.getService(interfaceName);
        Method method = null;
        try {
            method = service.getClass().getMethod(request.getMethodName(),request.getParamTypes());
            Object invoke = method.invoke(service,request.getParams());
            return RpcResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法错误");
            return RpcResponse.fail();
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
