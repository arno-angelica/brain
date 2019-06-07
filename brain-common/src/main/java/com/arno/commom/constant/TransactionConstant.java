package com.arno.commom.constant;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc 事务管理相关常量
 * @className TransactionConstant
 * @create Create in 2019/06/07 2019/6/7
 **/
public interface TransactionConstant {

    /**
     * 创建事务组
     */
    String CREATE = "create";

    /**
     * 新增事务
     */
    String ADD = "add";

    /**
     * 回滚事务
     */
    String ROLLBACK = "rollback";

    /**
     * 提交事务
     */
    String COMMIT = "commit";

}
