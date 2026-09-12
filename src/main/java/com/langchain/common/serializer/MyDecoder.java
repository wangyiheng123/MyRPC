package com.langchain.common.serializer;

import com.langchain.common.message.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Short messageType = byteBuf.readShort();

        if (messageType != MessageType.REQUEST.getCode() && messageType != MessageType.RESPONSE.getCode()){
            System.out.println("暂不支持此种数据");
        }
        Short serializerType = byteBuf.readShort();

        Serializer serializer = Serializer.getSerializerByCode(serializerType);

        if (serializer == null){
            throw new RuntimeException("不存在对应的序列化器");
        }

        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Object deserialize = serializer.deserialize(bytes,messageType);
        list.add(deserialize);
    }
}
