package com.langchain.common.Client.serviceCenter;

import com.langchain.common.Client.ZKWatcher.WatchZK;
import com.langchain.common.Client.cache.ServiceCache;
import com.langchain.common.Client.serviceCenter.balance.ConsistencyHashBalance;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetSocketAddress;
import java.util.List;

public class ZKServiceCenter implements ServiceCenter{

    //curator提供的zookeeper客户端
    private CuratorFramework client;

    private static final String ROOT_PATH = "MyRPC";

    private ServiceCache serviceCache;

    public ZKServiceCenter(){
        RetryPolicy policy = new ExponentialBackoffRetry(1000,3);
        this.client = CuratorFrameworkFactory.builder().connectString("localhost:2181").sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
        this.client.start();
        System.out.println("zookeeper 连接成功");
        this.serviceCache = new ServiceCache();
        WatchZK watcher = new WatchZK(client,serviceCache);
        watcher.watchToUpdate(ROOT_PATH);
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {

        try {
            //先从本地缓存中查找
            List<String> serviceList = serviceCache.getServiceAddress(serviceName);
            if (serviceList == null) {
                serviceList = client.getChildren().forPath("/" + serviceName);
            }
            //负载均衡得到地址
            String string = new ConsistencyHashBalance().balance(serviceList);
//            String string = serviceList.get(0);
            return parseAddress(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private InetSocketAddress parseAddress(String address){
        String[] result = address.split(":");
        return new InetSocketAddress(result[0],Integer.parseInt(result[1]));
    }
}
