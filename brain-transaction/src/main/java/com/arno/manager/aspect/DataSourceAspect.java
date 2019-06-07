package com.arno.manager.aspect;

import com.arno.manager.connection.DistributedConnection;
import com.arno.manager.transaction.TransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.sql.Connection;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc 给数据源获取连接的方法做切面
 * @className DataSourceAspect
 * @create Create in 2019/06/07 2019/6/7
 **/
@Aspect
public class DataSourceAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection process(ProceedingJoinPoint point) throws Throwable {
        if (TransactionManager.getCurrent() != null) {
            return new DistributedConnection((Connection) point.proceed(), TransactionManager.getCurrent());
        } else {
            return (Connection)point.proceed();
        }
    }

}
