package com.langchain.common.sevice.provider.work;

import com.langchain.common.message.RpcRequest;
import com.langchain.common.message.RpcResponse;
import com.langchain.common.sevice.provider.server.ServiceProvider;
import lombok.AllArgsConstructor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/*
实现多线程服务端调用
 */
@AllArgsConstructor
public class WorkThread implements Runnable{

    private Socket socket;

    private ServiceProvider serviceProvider;

    @Override
    public void run() {

        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            RpcRequest request = (RpcRequest) ois.readObject();
            RpcResponse response = getResponse(request);
            oos.writeObject(response);
            oos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private RpcResponse getResponse(RpcRequest request) {
        String interfaceName = request.getInterfaceName();

        Object service = serviceProvider.getService(interfaceName);

        try{
            Method method = service.getClass().getMethod(request.getMethodName(),request.getParamsType());
            Object data = method.invoke(service,request.getParams());
            return RpcResponse.sussess(data);
        } catch (Exception e) {
            e.printStackTrace();
            return RpcResponse.fail();
        }
    }
}
