package redrock.netty.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import redrock.netty.util.JsonUtil;

import java.nio.charset.Charset;


/**
 * @author kuangjunlin
 */
public abstract class AbstractHttpJsonDecoder<T> extends MessageToMessageDecoder<T> {
    private final static Charset UTF_8 = Charset.forName("UTF-8");

    protected AbstractHttpJsonDecoder() {
    }

    /**
     * 字符串转换为java对象
     * @param context
     * @param body
     * @return
     */
    protected Object decode(ChannelHandlerContext context, ByteBuf body){
        String content = body.toString(UTF_8);
        System.out.println("this body is:"+content);
        return JsonUtil.fromJson(content, Object.class);
    }
}
