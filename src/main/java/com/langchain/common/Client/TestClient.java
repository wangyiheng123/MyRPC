package com.langchain.common.Client;

import com.langchain.common.Client.proxy.ClientProxy;
import com.langchain.common.User;
import com.langchain.common.sevice.UserService;

public class TestClient {
    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy();
        UserService userService = clientProxy.getProxy(UserService.class);
        User user = userService.getUserByUserId(10);
        System.out.println(user.getId());
    }
}
