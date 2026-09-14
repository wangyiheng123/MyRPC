package com.langchain.common.sevice.handler;

import com.langchain.common.message.RpcRequest;
import com.langchain.common.message.RpcResponse;
import com.langchain.common.ratelimit.RateLImit;
import com.langchain.common.sevice.provider.server.ServiceProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;

@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private ServiceProvider serviceProvider;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {
        RpcResponse response = getResponse(request);
        channelHandlerContext.writeAndFlush(response);
        channelHandlerContext.close();
    }

    private RpcResponse getResponse(RpcRequest request){
        //服务限流
        RateLImit rateLImit = serviceProvider.getRateLimitProvider().getRateLimit(request.getInterfaceName());

        if (!rateLImit.getToken()){
            System.out.println("服务限流");
            return RpcResponse.fail();
        }

        //获得服务名
        Object service = serviceProvider.getService(request.getInterfaceName());
        //反射调用方法
        Method method = null;
        try {
            method = service.getClass().getMethod(request.getMethodName(),request.getParamsType());
            Object data =  method.invoke(service,request.getParams());
            return RpcResponse.sussess(data);
        } catch (Exception e) {
            e.printStackTrace();
            return RpcResponse.fail();
        }
    }
}
