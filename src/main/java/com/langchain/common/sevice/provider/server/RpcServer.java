package com.langchain.common.sevice.provider.server;

public interface RpcServer {

    void start(int port);

    void stop();

}
