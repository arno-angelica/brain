# 尝试手写分布式事务框架

### 暂未实现重试

### 基本思路
1、 对获取连接的数据源做切面。

2、 重写 Connection 的 commit()、rollback()、close() 方法

3、 创建注解对启动事务的方法做切面

### 重写逻辑
主要是另启线程等待事务管理器给到结果，如果为 commit 则进行提交，反之亦然

### 版本说明
#### 20190608-BUG：
1. 新增拦截器，用于接收事务组ID，并放到 ThreadLocal 中
2. 重写 connection.setAutoCommit() 方法，避免在等待提交时变为已提交的状态
3. 删除 connection.close() 的实现，在等待提交时，连接不可关闭。