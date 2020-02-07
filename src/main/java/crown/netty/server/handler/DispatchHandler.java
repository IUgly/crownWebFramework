package crown.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import crown.webframework.framework.servlet.Dispatcher;
import crown.webframework.framework.servlet.Request;
import crown.webframework.framework.servlet.Response;
import crown.netty.common.*;

/**
 * @author kuangjunlin
 */
@Slf4j
public class DispatchHandler extends SimpleChannelInboundHandler<Request> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) {
        final Response response = Response.build(ctx, request);
        try {
            Dispatcher.doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果buf满了，丢掉数据，防止OOM
        if (ctx.channel().isActive() && ctx.channel().isWritable()) {
            ctx.writeAndFlush(response);
        } else {
            ctx.writeAndFlush(new ResponseMessage<>(ResponseStatus.FAIL_503));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
