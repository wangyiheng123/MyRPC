package com.langchain.common.serializer;

public interface Serializer {

    //将JAVA对象序列化成字节数组
    byte[] serialize(Object object);

    //将字节数组翻序列化成JAVA对象
    Object deserialize(byte[] data,int messageType);

    int getType();

    static Serializer getSerializerByCode(int code){
        switch (code){
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }

}
