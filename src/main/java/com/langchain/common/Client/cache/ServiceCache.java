package com.langchain.common.Client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCache {

    private static Map<String, List<String>> cache = new HashMap<>();

    public void addServiceToCache(String serviceName,String address){
        if (cache.containsKey(serviceName)){
            cache.get(serviceName).add(address);
        }else {
            List<String> addressList = new ArrayList<>();
            addressList.add(address);
            cache.put(serviceName,addressList);
        }
    }

    public void replaceServiceAddress(String serviceName,String oldAddress,String newAddress){
        if (cache.containsKey(serviceName)){
            List<String> address = cache.get(serviceName);
            address.remove(oldAddress);
            address.add(newAddress);
        }else {
            System.out.println("服务不存在！");
        }
    }

    public List<String> getServiceAddress(String serviceName){
        if (!cache.containsKey(serviceName)){
            return null;
        }
        List<String> address = cache.get(serviceName);
        return address;
    }

    public void delete(String serviceName,String address){
        List<String> addressList = cache.get(serviceName);
        addressList.remove(address);
    }

}
