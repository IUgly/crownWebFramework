package crown.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import crown.netty.server.codec.OrderProtocolDecoder;
import crown.netty.server.codec.OrderProtocolEncoder;
import crown.netty.server.handler.DispatchHandler;
import crown.netty.server.handler.MetricHandler;
import crown.netty.server.handler.ServerIdleCheckHandler;

/**
 * @author kuangjunlin
 */
public class ServerInitializer  extends ChannelInitializer<SocketChannel> {

    MetricHandler metricHandler = new MetricHandler();

    //声明一个大小为10的线程池
    UnorderedThreadPoolEventExecutor business = new UnorderedThreadPoolEventExecutor(
            10, new DefaultThreadFactory("business"));
    //使用NioNioEventLoopGroup处理时是单线程
//        NioEventLoopGroup business = new NioEventLoopGroup(0
//                , new DefaultThreadFactory("business"));

    GlobalTrafficShapingHandler globalTrafficShapingHandler = new GlobalTrafficShapingHandler(new NioEventLoopGroup(),
            100 * 1024 * 1024, 100 * 1024 * 1024);

    //设置黑名单
    //Todo
    IpSubnetFilterRule ipSubnetFilterRule
            = new IpSubnetFilterRule(
            "127.0.0.1", 8, IpFilterRuleType.REJECT);
    RuleBasedIpFilter ruleBasedIpFilter = new RuleBasedIpFilter(ipSubnetFilterRule);


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //debug时打印原始数据
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));

        pipeline.addLast("ipfilter", ruleBasedIpFilter);

        //流量整型控制
        pipeline.addLast("TSHandler", globalTrafficShapingHandler);

        //空闲关闭
        pipeline.addLast("idleCheck", new ServerIdleCheckHandler());

        ch.pipeline().addLast(new HttpRequestDecoder());
        ch.pipeline().addLast(new HttpObjectAggregator(65536));
        ch.pipeline().addLast(new OrderProtocolDecoder());

        ch.pipeline().addLast(new HttpResponseEncoder());
        ch.pipeline().addLast(new OrderProtocolEncoder());

        //可视化数据
        pipeline.addLast("metricsHandler", metricHandler);

        //通过导入Log4j包，并配置日志级别以及控制Logging的位置打印想要的信息
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));

        //flush增强，减少了flush次数，牺牲一定延迟，提高吞吐量
        pipeline.addLast("flushEnhance", new FlushConsolidationHandler(5, true));

//      pipeline.addLast(business, new OrderServerProcessHandler());
        ch.pipeline().addLast(new DispatchHandler());
    }
}
