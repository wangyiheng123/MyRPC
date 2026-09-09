package com.langchain.common.Client.netty.handler.nettyInitializer;

import com.langchain.common.Client.serviceCenter.ServiceCenter;
import com.langchain.common.Client.serviceCenter.ZKServiceCenter;
import com.langchain.common.message.RpcRequest;
import com.langchain.common.message.RpcResponse;
import com.langchain.common.rpcClient.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class NettyRpcClient implements RpcClient {


    private ServiceCenter serviceCenter;

    //Netty中的配置和启动工具
    private static final Bootstrap bootstrap;

    //Netty中的负责工作的线程团队，一个Channel会绑定一个eventLoop，一个eventLoop会处理多个Channel
    private static final EventLoopGroup eventLoopGroup;

    public NettyRpcClient(){
        this.serviceCenter = new ZKServiceCenter();
    }

    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new NettyClientInitializer());
    }
    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        try {
            InetSocketAddress inetSocketAddress = serviceCenter.serviceDiscovery(request.getInterfaceName());
            ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress.getHostName(),inetSocketAddress.getPort()).sync();
            Channel channel = channelFuture.channel();
            //发送数据
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RpcResponse");
            RpcResponse response = channel.attr(key).get();
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
