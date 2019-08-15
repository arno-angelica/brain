package com.arno.manager.aspect;

import com.arno.commom.constant.TransactionConstant;
import com.arno.manager.annotation.Distributed;
import com.arno.manager.transaction.DealTransaction;
import com.arno.manager.transaction.BrainTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc 对 Distributed 注解做切面
 * @className DistributedAspect
 * @create Create in 2019/06/07 2019/6/7
 **/
@Aspect
@Component
public class DistributedAspect implements Ordered {

    /**
     * 处理带有 Distributed 注解的方法
     *
     * @param joinPoint
     */
    @Around("@annotation(com.arno.manager.annotation.Distributed)")
    public void invoke(ProceedingJoinPoint joinPoint) {
        // 取到 MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 拿到 Method
        Method method = signature.getMethod();
        // 取到方法上面的 Distributed 注解
        Distributed distributed = method.getAnnotation(Distributed.class);
        String groupId;
        // 如果为事务的开始节点
        if (distributed.isStart()) {
            // 创建事务组
            groupId = BrainTransactionManager.createTransactionGroup();
        } else {
            // 创建事务
            groupId = BrainTransactionManager.getCurrentGroupId();
        }
        // 定义 DealTransaction 对象
        DealTransaction transaction = BrainTransactionManager.createTransaction(groupId);
        try {
            // 执行方法
            joinPoint.proceed();
            // 方法执行成功，则告知事务管理器，该事务节点为 commit
            BrainTransactionManager.addTransaction(transaction, distributed.isEnd(), TransactionConstant.COMMIT);
        } catch (Throwable e) {
            // 方法执行异常，则告知事务管理器，该事务节点为 rollback
            BrainTransactionManager.addTransaction(transaction, distributed.isEnd(), TransactionConstant.ROLLBACK);
            e.printStackTrace();
        }
    }

    @Override
    public int getOrder() {
        return 10000;
    }
}
