package com.langchain.common.sevice.provider.server;

import com.langchain.common.sevice.serviceRegister.ServiceRegister;
import com.langchain.common.sevice.serviceRegister.impl.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {

    private Map<String,Object> interfaceProvider;

    private String host;

    private int port;

    private ServiceRegister serviceRegister;

    public ServiceProvider(){
        this.interfaceProvider = new HashMap<>();
    }
    public ServiceProvider(String host,int port){
        this.host = host;
        this.port = port;
        this.interfaceProvider = new HashMap<>();
        this.serviceRegister = new ZKServiceRegister();
    }

    //本地注册服务
    public void provideServiceInterface(Object service){
        String serviceName = service.getClass().getName();
        Class<?>[] interfaceName = service.getClass().getInterfaces();

        for (Class<?> clazz : interfaceName){
            interfaceProvider.put(clazz.getName(),service);
            serviceRegister.register(clazz.getName(), new InetSocketAddress(host,port));
        }
    }

    //获得服务实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
