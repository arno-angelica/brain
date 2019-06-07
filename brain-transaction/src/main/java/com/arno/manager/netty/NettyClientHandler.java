package com.arno.manager.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arno.commom.constant.TransactionConstant;
import com.arno.commom.pojo.TransactionMutualDTO;
import com.arno.manager.transaction.DealTransaction;
import com.arno.manager.transaction.TransactionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);

    private ChannelHandlerContext context;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TransactionMutualDTO result = JSON.parseObject((String) msg, TransactionMutualDTO.class);
        LOGGER.info("接受数据:" + result.toString());

        String groupId = result.getGroupId();
        String command = result.getCommand();
        LOGGER.info("接收command:" + command);
        // 对事务进行操作
        DealTransaction dealTransaction = TransactionManager.getDealTransaction(groupId);
        if (command.equals("rollback")) {
            dealTransaction.setType(TransactionConstant.ROLLBACK);
        } else if (command.equals("commit")) {
            dealTransaction.setType(TransactionConstant.COMMIT);
        }
        dealTransaction.getTask().signalTask();
    }

    public synchronized Object call(JSONObject data) throws Exception {
        context.writeAndFlush(data.toJSONString());
        return null;
    }
}
