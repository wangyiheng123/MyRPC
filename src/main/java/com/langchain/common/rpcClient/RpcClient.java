package com.langchain.common.rpcClient;

import com.langchain.common.message.RpcRequest;
import com.langchain.common.message.RpcResponse;

public interface RpcClient {

    RpcResponse sendRequest(RpcRequest request);

}
