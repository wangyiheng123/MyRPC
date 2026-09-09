package com.langchain.common.Client.proxy;

import com.langchain.common.Client.IOClient;
import com.langchain.common.Client.netty.handler.nettyInitializer.NettyRpcClient;
import com.langchain.common.message.RpcRequest;
import com.langchain.common.message.RpcResponse;
import com.langchain.common.rpcClient.RpcClient;
import com.langchain.common.rpcClient.impl.SimpleScoketRpcClient;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {

    private String host;

    private int port;

    private RpcClient rpcClient;

    public ClientProxy(String host,int port,int choose){
        switch (choose){
            case 0:
                rpcClient = new NettyRpcClient();
                break;
            case 1:
                rpcClient = new SimpleScoketRpcClient(host, port);
        }
    }

    public ClientProxy(){
        rpcClient = new NettyRpcClient();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder().interfaceName(method.getDeclaringClass().getName()).methodName(method.getName()).params(args).paramsType(method.getParameterTypes()).build();
        RpcResponse response = rpcClient.sendRequest(request);
        return response.getData();
    }

    public <T> T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},this);
        return (T)o;
    }
}
