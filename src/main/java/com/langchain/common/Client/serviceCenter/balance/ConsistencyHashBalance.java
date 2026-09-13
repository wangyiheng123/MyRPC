package com.langchain.common.Client.serviceCenter.balance;

import java.util.*;

/*
一致性哈希负载均衡
 */
public class ConsistencyHashBalance implements LoadBalance{

    //虚拟节点的个数
    private static final int VIRTUAL_NUM = 5;

    //哈希环
    private SortedMap<Integer, String> shards = new TreeMap<>();

    //真实节点列表
    private List<String> realNodes = new LinkedList<>();

    private void init(List<String> serviceList){
        for (String service : serviceList){
            realNodes.add(service);
            for (int i = 0;i < VIRTUAL_NUM;i++){
                String virtualNode = service + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash,virtualNode);
            }
        }
    }

    public String getServer(String node,List<String> serviceList){
        init(serviceList);
        int hash = getHash(node);
        Integer key = null;
        SortedMap<Integer,String> subMap = shards.tailMap(hash);
        if (subMap == null){
            key = shards.firstKey();
        }else {
            key = subMap.firstKey();
        }
        String virtualNode = shards.get(key);
        return virtualNode.substring(0, virtualNode.indexOf("&&"));
    }


    @Override
    public String balance(List<String> addressList) {
        String random = UUID.randomUUID().toString();
        return getServer(random,addressList);
    }

    @Override
    public void addNode(String node) {
        if (!realNodes.contains(node)){
            realNodes.add(node);
            for (int i = 0;i < VIRTUAL_NUM;i++){
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash,virtualNode);
            }
        }
    }

    @Override
    public void delNode(String node) {
        if (realNodes.contains(node)){
            realNodes.remove(node);
            for (int i = 0; i < VIRTUAL_NUM; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.remove(hash);
            }
        }
    }

    private static int getHash(String str){
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        if (hash < 0){
            hash = Math.abs(hash);
        }
        return hash;
    }
}
