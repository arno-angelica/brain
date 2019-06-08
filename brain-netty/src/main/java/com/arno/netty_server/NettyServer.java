package com.arno.netty_server;

import com.arno.commom.constant.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc netty调用类
 * @className NettyServer
 * @create Create in 2019/05/28 2019/5/28
 **/
public class NettyServer {

    /**
     * 事务管理器的服务端定义
     * @param hostName
     * @param port
     */
    public void start(String hostName, int port) {
        try {
            final ServerBootstrap bootstrap = new ServerBootstrap();
            NioEventLoopGroup eventLookGroup = new NioEventLoopGroup();
            bootstrap.group(eventLookGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(NettyConstant.DECODE, new StringDecoder());
                            pipeline.addLast(NettyConstant.ENCODE, new StringEncoder());
                            pipeline.addLast(NettyConstant.HANDLER, new NettyServerHandler());
                        }
                    });
            bootstrap.bind(hostName, port).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
