package crown.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

/**
 * @author kuangjunlin
 * serverBootstrap.option(NioChannelOption.SO_BACKLOG, 1024);
 * Netty在Linux下值的获取 （io.netty.util.NetUtil）:
 *  · 先尝试: /proc/sys/net/core/somaxcon
 *  · 再尝试: sysctl
 *  · 若最终没获取到, 用默认: 128
 *  使用方式:
 *  javaChannel().bind(localAddress, config.getBacklog());
 *
 *  开启native
 *  Nio替换为Nio
 */

public class Server {
    static ServerBootstrap serverBootstrap;
    static NioEventLoopGroup boss;
    static NioEventLoopGroup worker ;

    static {
        //实例并完善线程名称
        boss = new NioEventLoopGroup
                (0, new DefaultThreadFactory("boss"));
        worker = new NioEventLoopGroup
                (0, new DefaultThreadFactory("worker"));
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        //设置是否启用Nagle算法：将较小的碎片数据连接成更大的报文，提高发送效率
        //默认为false
        serverBootstrap.childOption(NioChannelOption.TCP_NODELAY, true);
        //设置最大的等待连接数量
        serverBootstrap.option(NioChannelOption.SO_BACKLOG, 1024);

        serverBootstrap.childHandler(new ServerInitializer());
    }
    /**
     * 启动服务器
     * @param port
     */
    public static void run(int port){
        try {
            ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
