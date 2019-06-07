package com.arno.manager.transaction;

import com.arno.manager.util.Task;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc
 * @className DealTransaction
 * @create Create in 2019/06/07 2019/6/7
 **/
public class DealTransaction {

    /**
     * 事务组
     */
    private String groupId;
    /**
     * 事务ID
     */
    private String transactionId;
    /**
     * commit-待提交，rollback-待回滚
     */
    private String  type;

    /**
     * 任务锁
     */
    private Task task = new Task();

    public DealTransaction(String groupId, String transactionId) {
        this.groupId = groupId;
        this.transactionId = transactionId;
        this.task = new Task();
    }

    public DealTransaction(String groupId, String transactionId, String transactionType) {
        this.groupId = groupId;
        this.transactionId = transactionId;
        this.type = transactionType;
    }

    public Task getTask() {
        return task;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
