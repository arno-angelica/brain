package com.arno.commom.pojo;

import java.io.Serializable;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc 服务和事务管理器交互的 BO
 * @className TransactionMutualBO
 * @create Create in 2019/06/07 2019/6/7
 **/
public class TransactionMutualBO implements Serializable {

    private static final long serialVersionUID = 2718575444111084801L;

    private String groupId;

    private String command;

    private String type;

    private String singleId;

    private Boolean endFlag;

    private Integer count;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSingleId() {
        return singleId;
    }

    public void setSingleId(String singleId) {
        this.singleId = singleId;
    }

    public Boolean getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(Boolean endFlag) {
        this.endFlag = endFlag;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
