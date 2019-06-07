package com.arno.manager.netty;

import com.alibaba.fastjson.JSONObject;
import com.arno.commom.constant.NettyConstant;
import com.arno.commom.pojo.TransactionMutualBO;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClient {

    private NettyClientHandler client = null;

    public void start(String hostName, Integer port) {
        client = new NettyClientHandler();
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(NettyConstant.DECODE, new StringDecoder());
                        pipeline.addLast(NettyConstant.ENCODE, new StringEncoder());
                        pipeline.addLast(NettyConstant.HANDLER, client);
                    }
                });

        try {
            b.connect(hostName, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void send(TransactionMutualBO req) {
        try {
            String reqStr = JSONObject.toJSONString(req);
            client.call(JSONObject.parseObject(reqStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
