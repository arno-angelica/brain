package com.arno.manager.aspect;

import com.arno.commom.constant.TransactionConstant;
import com.arno.manager.annotation.Distributed;
import com.arno.manager.transaction.DealTransaction;
import com.arno.manager.transaction.TransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc 对 Distributed 注解做切面
 * @className DistributedAspect
 * @create Create in 2019/06/07 2019/6/7
 **/
@Aspect
public class DistributedAspect {

    @Around("@annotation(com.arno.manager.annotation.Distributed)")
    public void invoke(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Distributed distributed = method.getAnnotation(Distributed.class);
        String groupId;
        if (distributed.isStart()) {
            groupId = TransactionManager.createTransactionGroup();
        } else {
            groupId = TransactionManager.getCurrentGroupId();
        }
        DealTransaction transaction = TransactionManager.createTransaction(groupId);
        if (transaction != null) {
            try {
                joinPoint.proceed();
                TransactionManager.addTransaction(transaction, distributed.isEnd(), TransactionConstant.COMMIT);
            } catch (Exception e) {
                TransactionManager.addTransaction(transaction, distributed.isEnd(), TransactionConstant.ROLLBACK);
                e.printStackTrace();
            } catch (Throwable t) {
                TransactionManager.addTransaction(transaction, distributed.isEnd(), TransactionConstant.ROLLBACK);
                t.printStackTrace();
            }
        }
    }

}
