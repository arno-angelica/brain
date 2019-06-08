package com.arno.manager.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc 分布式事务注解
 * @className Distributed
 * @create Create in 2019/06/07 2019/6/7
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Distributed {

    /**
     * 是否为事务开始的节点
     * @return
     */
    boolean isStart() default false;

    /**
     * 是否为事务结束的节点
     * @return
     */
    boolean isEnd() default false;

    Class<? extends Throwable>[] rollbackFor() default {};
}
