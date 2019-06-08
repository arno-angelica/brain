package com.arno.manager.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arno.commom.constant.TransactionConstant;
import com.arno.commom.pojo.TransactionMutualDTO;
import com.arno.manager.transaction.DealTransaction;
import com.arno.manager.transaction.BrainTransactionManager;
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

    /**
     * 读取事务管理器回调数据，并处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TransactionMutualDTO result = JSON.parseObject((String) msg, TransactionMutualDTO.class);
        LOGGER.info("接受数据:" + result.toString());

        String groupId = result.getGroupId();
        String command = result.getCommand();
        LOGGER.info("接收command:" + command);
        // 根据回调的 command 做回滚或提交
        DealTransaction dealTransaction = BrainTransactionManager.getDealTransaction(groupId);
        if (command.equals(TransactionConstant.ROLLBACK)) {
            dealTransaction.setType(TransactionConstant.ROLLBACK);
        } else if (command.equals(TransactionConstant.COMMIT)) {
            dealTransaction.setType(TransactionConstant.COMMIT);
        }
        dealTransaction.getTask().signalTask();
    }

    public synchronized Object call(JSONObject data) throws Exception {
        context.writeAndFlush(data.toJSONString());
        return null;
    }
}
