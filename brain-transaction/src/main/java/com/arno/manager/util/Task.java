package com.arno.manager.util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author LiuLQ
 * @version 1.0.0
 * @desc 任务管理
 * @className HttpClient
 * @create Create in 2019/06/07 2019/6/7
 **/
public class Task {

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    /**
     * 等待任务
     */
    public void waitTask() {
        try {
            lock.lock();
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 唤醒任务
     */
    public void signalTask() {
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
