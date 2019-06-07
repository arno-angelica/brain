package com.arno.commom.pojo;

import java.io.Serializable;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc 服务和事务管理器交互的 DTO
 * @className TransactionMutualDTO
 * @create Create in 2019/06/07 2019/6/7
 **/
public class TransactionMutualDTO implements Serializable {

    private static final long serialVersionUID = 8890610275408168847L;
    private String groupId;

    private String command;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "TransactionMutualDTO{" +
                "groupId='" + groupId + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}
