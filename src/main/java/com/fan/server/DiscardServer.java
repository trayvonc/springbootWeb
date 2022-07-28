package com.fan.server;

import com.fan.initializer.HttpInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DiscardServer {
    private static int HTTP_PORT = 8888;
    @Resource
    private HttpInitializer httpInitializer;
    public void httpProcess() {
        System.out.println("启动监听http连接----");
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap对象，它是Netty用于启动Nio服务的辅助类启动器
            // 目的是降低服务端的开发复杂度
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 于服务端不同channel(NioSocketChannel.class)
            /**
             * 工作线程注入httpInitializer，
             * 作用是当创建NioServerSocketChannel成功之后
             * 在进行初始化时，将它的channelHandler设置到ChannelPipeline中，
             * 用于处理网络IO事件
             */
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //初始化服务端可连接队列。
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //使用长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //工作线程
                    .childHandler(httpInitializer);
            // 发起异步连接操作，调用同步阻塞方法等待连接成功
            Channel channel = bootstrap.bind(HTTP_PORT).sync().channel();
            // 等待客户端链路关闭
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
