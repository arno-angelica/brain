package com.arno.netty_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc
 * @className NettyMain
 * @create Create in 2019/06/07 2019/6/7
 **/
public class NettyMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyMain.class);

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.start("localhost", 8080);
        LOGGER.info("netty 启动成功");
    }

}
