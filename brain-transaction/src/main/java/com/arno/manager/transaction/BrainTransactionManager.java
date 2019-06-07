package com.arno.manager.transaction;

import com.arno.commom.constant.TransactionConstant;
import com.arno.commom.pojo.TransactionMutualBO;
import com.arno.manager.netty.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc
 * @className TransactionManager
 * @create Create in 2019/06/07 2019/6/7
 **/
@Component
public class BrainTransactionManager {

    private static final Logger log = LoggerFactory.getLogger(BrainTransactionManager.class);

    private static ThreadLocal<DealTransaction> current = new ThreadLocal<>();
    private static ThreadLocal<String> currentGroupId = new ThreadLocal<>();
    private static ThreadLocal<Integer> transactionCount = new ThreadLocal<>();

    public static Map<String, DealTransaction> LB_TRANSACION_MAP = new HashMap<>();

    private static NettyClient nettyClient;

    @Autowired
    public void setClient(NettyClient client) {
        BrainTransactionManager.nettyClient = client;
    }
    /**
     * 创建事务组，并且返回groupId
     *
     * @return
     */
    public static String createTransactionGroup() {
        String groupId = UUID.randomUUID().toString();
        TransactionMutualBO req = new TransactionMutualBO();
        req.setGroupId(groupId);
        req.setCommand(TransactionConstant.CREATE);
        nettyClient.send(req);
        log.info("创建事务组...");

        currentGroupId.set(groupId);
        return groupId;
    }

    /**
     * 创建分布式事务
     *
     * @param groupId
     * @return
     */
    public static DealTransaction createTransaction(String groupId) {
        String transactionId = UUID.randomUUID().toString();
        DealTransaction dealTransaction = new DealTransaction(groupId, transactionId);
        LB_TRANSACION_MAP.put(groupId, dealTransaction);
        current.set(dealTransaction);
        addTransactionCount();
        log.info("创建事务...");

        return dealTransaction;
    }

    /**
     * 添加事务到事务组
     *
     * @param dealTransaction
     * @param isEnd
     * @param transactionType
     * @return
     */
    public static DealTransaction addTransaction(DealTransaction dealTransaction, Boolean isEnd, String transactionType) {
        TransactionMutualBO data = new TransactionMutualBO();
        data.setGroupId(dealTransaction.getGroupId());
        data.setCommand(TransactionConstant.ADD);
        data.setCount(getTransactionCount());
        data.setSingleId(dealTransaction.getTransactionId());
        data.setEndFlag(isEnd);
        data.setType(transactionType);
        nettyClient.send(data);
        log.info("添加事务...");
        return dealTransaction;
    }

    public static DealTransaction getDealTransaction(String groupId) {
        return LB_TRANSACION_MAP.get(groupId);
    }

    public static DealTransaction getCurrent() {
        return current.get();
    }

    public static String getCurrentGroupId() {
        return currentGroupId.get();
    }

    public static void setCurrentGroupId(String groupId) {
        currentGroupId.set(groupId);
    }

    public static Integer getTransactionCount() {
        return transactionCount.get();
    }

    public static void setTransactionCount(int i) {
        transactionCount.set(i);
    }

    public static Integer addTransactionCount() {
        int i = (transactionCount.get() == null ? 0 : transactionCount.get()) + 1;
        transactionCount.set(i);
        return i;
    }
}
