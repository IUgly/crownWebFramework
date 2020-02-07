package crown.netty.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import crown.webframework.framework.servlet.Response;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author kuangjunlin
 */
public class OrderProtocolEncoder extends AbstractHttpJsonEncoder<Response> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Response responseBody, List<Object> out){
        //获取服务端的字节流
        ByteBuf body = (ByteBuf) responseBody.getContent();
        //创建一个服务端响应对象
        FullHttpResponse response = new DefaultFullHttpResponse(
                responseBody.getHttpVersion(), OK, Unpooled.wrappedBuffer(body));

        response.headers()
                .set(CONTENT_TYPE, responseBody.getContentType())
                .setInt(CONTENT_LENGTH, response.content().readableBytes());
        //添加到结果中
        out.add(response);
    }
}
