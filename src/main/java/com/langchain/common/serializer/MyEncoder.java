package com.langchain.common.serializer;

import com.langchain.common.message.MessageType;
import com.langchain.common.message.RpcRequest;
import com.langchain.common.message.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyEncoder extends MessageToByteEncoder {

    private Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object object, ByteBuf byteBuf) throws Exception {
        if (object instanceof RpcRequest){
            byteBuf.writeShort(MessageType.REQUEST.getCode());
        }else if(object instanceof RpcResponse){
            byteBuf.writeShort(MessageType.RESPONSE.getCode());
        }

        byteBuf.writeShort(serializer.getType());

        byte[] serialzeBytes = serializer.serialize(object);

        byteBuf.writeInt(serialzeBytes.length);

        byteBuf.writeBytes(serialzeBytes);
    }
}
