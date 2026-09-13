package com.langchain.common.Client.serviceCenter.balance;

import java.util.List;

/*
轮询法
 */
public class RoundLoadBalance implements LoadBalance{
    private int choose = -1;

    @Override
    public String balance(List<String> addressList) {
        choose++;
        choose = choose % addressList.size();
        return addressList.get(choose);
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
