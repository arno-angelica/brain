package com.arno.netty_server;

import com.alibaba.fastjson.JSONObject;
import com.arno.commom.constant.TransactionConstant;
import com.arno.commom.pojo.TransactionMutualBO;
import com.arno.commom.pojo.TransactionMutualDTO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc 请求处理
 * @className NettyServerHandler
 * @create Create in 2019/06/07 2019/6/7
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static Map<String, ArrayList<String>> TRANSACTION_TYPE_MAP = new HashMap<>();
    private static Map<String, Boolean> IS_END_MAP = new HashMap<>();
    private static Map<String, Integer> COUNT_MAP = new HashMap<>();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.add(channel);
    }

    /**
     * 读取请求数据，创建事务组或添加保存事务
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TransactionMutualBO message = JSONObject.parseObject(msg + "", TransactionMutualBO.class);
        LOGGER.info("接受到数据为->{}", message.toString());
        // 指令标识：create-创建事务 add-添加事务
        String command = message.getCommand();
        // 事务组
        String groupId = message.getGroupId();
        // 子事务的类型， commit - 提交事务，rollback - 回滚事务
        String transactionType = message.getType();
        // 事务数量
        Integer transactionCount = message.getCount();
        // 是否为结束事务
        Boolean isEnd = message.getEndFlag();

        if (TransactionConstant.CREATE.equals(command)) {
            // 创建事务组
            TRANSACTION_TYPE_MAP.put(groupId, new ArrayList<>());
        } else if (TransactionConstant.ADD.equals(command)) {
            // 加入事务组
            TRANSACTION_TYPE_MAP.get(groupId).add(transactionType);
            // 如果为最后事务节点
            if (isEnd) {
                // 组结束标识置位 true
                IS_END_MAP.put(groupId, true);
                // 记录组中事务数量
                COUNT_MAP.put(groupId, transactionCount);
            }
            TransactionMutualDTO result = new TransactionMutualDTO();
            result.setGroupId(groupId);
            // 如果标识为true, 且事务组数量和map中存放的数量相同
            if (IS_END_MAP.get(groupId) && TRANSACTION_TYPE_MAP.get(groupId)
                    .equals(TRANSACTION_TYPE_MAP.get(groupId).size())) {
                // 如果事务组中存在一条为 rollback 的，则全部回滚
                if (TRANSACTION_TYPE_MAP.get(groupId).contains(TransactionConstant.ROLLBACK)) {
                    result.setCommand(TransactionConstant.ROLLBACK);
                } else {
                    result.setCommand(TransactionConstant.COMMIT);
                }
                sendResult(result, groupId);
            }
        }
    }

    /**
     * 发送结果
     *
     * @param result
     */
    private void sendResult(TransactionMutualDTO result, String groupId) {
        LOGGER.info("响应的数据->{}", result.toString());
        for (Channel channel : channelGroup) {
            channel.writeAndFlush(JSONObject.toJSONString(result));
        }
        // 清除数据
        TRANSACTION_TYPE_MAP.remove(groupId);
        IS_END_MAP.remove(groupId);
        TRANSACTION_TYPE_MAP.remove(groupId);
    }
}
