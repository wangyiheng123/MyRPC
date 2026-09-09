package com.langchain.common.sevice.provider.server.impl;

import com.langchain.common.sevice.provider.server.RpcServer;
import com.langchain.common.sevice.provider.server.ServiceProvider;
import com.langchain.common.sevice.provider.work.WorkThread;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
public class SimpleRpcServer implements RpcServer {

    private ServiceProvider serviceProvider;

    @Override
    public void start(int port) {

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器启动了！");

            while(true){
                Socket socket = serverSocket.accept();

                //通过线程并发处理每一个连接
                new Thread(new WorkThread(socket,serviceProvider)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        //未来添加这个功能

    }
}
