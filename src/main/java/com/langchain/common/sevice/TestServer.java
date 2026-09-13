package com.langchain.common.sevice;

import com.langchain.common.sevice.impl.UserServiceImpl;
import com.langchain.common.sevice.netty.NettyRpcServer;
import com.langchain.common.sevice.provider.server.RpcServer;
import com.langchain.common.sevice.provider.server.ServiceProvider;

public class TestServer {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider("localhost",9999);

        serviceProvider.provideServiceInterface(userService,true);

        RpcServer rpcServer = new NettyRpcServer(serviceProvider);

        rpcServer.start(9999);
    }
}
