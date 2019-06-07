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

    @Around("@annotation(com.arno.manager.annotation.Distributed)")
    public void invoke(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Distributed distributed = method.getAnnotation(Distributed.class);
        String groupId;
        if (distributed.isStart()) {
            groupId = BrainTransactionManager.createTransactionGroup();
        } else {
            groupId = BrainTransactionManager.getCurrentGroupId();
        }
        DealTransaction transaction = BrainTransactionManager.createTransaction(groupId);
        if (transaction != null) {
            try {
                joinPoint.proceed();
                BrainTransactionManager.addTransaction(transaction, distributed.isEnd(), TransactionConstant.COMMIT);
            } catch (Exception e) {
                BrainTransactionManager.addTransaction(transaction, distributed.isEnd(), TransactionConstant.ROLLBACK);
                e.printStackTrace();
            } catch (Throwable t) {
                BrainTransactionManager.addTransaction(transaction, distributed.isEnd(), TransactionConstant.ROLLBACK);
                t.printStackTrace();
            }
        }
    }

    @Override
    public int getOrder() {
        return 10000;
    }
}
