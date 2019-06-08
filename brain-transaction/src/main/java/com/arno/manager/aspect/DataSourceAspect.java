package com.arno.manager.aspect;

import com.arno.manager.connection.DistributedConnection;
import com.arno.manager.transaction.BrainTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc 给数据源获取连接的方法做切面
 * @className DataSourceAspect
 * @create Create in 2019/06/07 2019/6/7
 **/
@Aspect
@Component
public class DataSourceAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection process(ProceedingJoinPoint point) throws Throwable {
        // 如果分布式事务不为空，那么数据库连接取自定义的 DistributedConnection
        if (BrainTransactionManager.getCurrent() != null) {
            // 拿到 spring 或其他方式得到的数据库连接, 移花接木似的将他们所需的连接变为我们自定义的数据库连接
            return new DistributedConnection((Connection) point.proceed(), BrainTransactionManager.getCurrent());
        } else {
            // 否则取框架各自定义的 Connection
            return (Connection)point.proceed();
        }
    }

}
