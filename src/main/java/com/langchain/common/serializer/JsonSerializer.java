package com.langchain.common.serializer;

import com.alibaba.fastjson.JSONObject;
import com.langchain.common.message.RpcRequest;
import com.langchain.common.message.RpcResponse;

public class JsonSerializer implements Serializer{
    @Override
    public byte[] serialize(Object object) {
        byte[] bytes = JSONObject.toJSONBytes(object);
        return bytes;
    }

    @Override
    public Object deserialize(byte[] data, int messageType) {
        Object object = null;
        switch (messageType){
            case 0:
                RpcRequest request = JSONObject.parseObject(data,RpcRequest.class);
                Object[] objects = new Object[request.getParamsType().length];
                for (int i = 0; i < objects.length; i++) {
                    Class<?> paramsType = request.getParamsType()[i];
                    if (!paramsType.isAssignableFrom(request.getParams()[i].getClass())){
                        objects[i] = JSONObject.toJavaObject((JSONObject) request.getParams()[i],request.getParamsType()[i]);
                    }else {
                        objects[i] = request.getParams()[i];
                    }
                }
                request.setParams(objects);
                object = request;
                break;
            case 1:
                RpcResponse response = JSONObject.parseObject(data,RpcResponse.class);
                Class<?> dataType = response.getDataType();
                if (!dataType.isAssignableFrom(response.getData().getClass())){
                    response.setData(JSONObject.toJavaObject((JSONObject)response.getData(),dataType));
                }
                object = response;
                break;
            default:
                System.out.println("暂不支持这种消息");
                throw new RuntimeException();
        }
        return object;
    }

    @Override
    public int getType() {
        return 1;
    }
}
