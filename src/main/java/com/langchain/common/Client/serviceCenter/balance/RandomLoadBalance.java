package com.langchain.common.Client.serviceCenter.balance;

import java.util.List;
import java.util.Random;

/*
随机法负载均衡
 */
public class RandomLoadBalance implements LoadBalance{
    @Override
    public String balance(List<String> addressList) {
        Random random = new Random();
        int choose = random.nextInt(0,addressList.size());
        return addressList.get(choose);
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
