# 尝试手写简单的分布式事务框架

### 暂未实现重试

### 基本思路
1、 对获取连接的数据源做切面。

2、 重写 Connection 的 commit()、rollback()、close() 方法

3、 创建注解对启动事务的方法做切面

### 重写逻辑
主要是另启线程等待事务管理器给到结果，如果为 commit 则进行提交，反之亦然

大概流程如下

![流程](.\image\流程.png)

### 原始逻辑

**大概的画一下，并不是非常细致**

![流程](.\image\原始逻辑.png)

### 现在逻辑

**大概的画一下，并不是非常细致**

![流程](.\image\现有逻辑.png)

### 使用方式
1. 需要开启分布式事务的方法加入 Distributed 和 Spring 的 Transaction 注解
 如下：
```java
@Service
public class TestService {
    @Distributed(isEnd = true)
    @Transactional
    public void test() {
        demoDao.insert("server2");
    }
 }
 @Service
public class TestService {
   @Distributed(isStart = true)
   @Transactional
   public void test() {
       demoDao.insert("server1");
       // 调用 server2，使用 com.arno.manager.util.HttpClient
       HttpClient.get("http://localhost:8082/server2/test");
       int i = 100/0;
   }
}
```
2. spring 需要扫描 com.arno.manager 下的文件
以spring boot 为例
```java
  @SpringBootApplication
  @ComponentScan(basePackages = {"com.example.*", "com.arno.*"})
  public class HandlerApplication {
  
    public static void main(String[] args) {
        SpringApplication.run(HandlerApplication.class, args);
    }
  }
```
### 版本说明
#### 20190608-BUG：
1. 新增拦截器，用于接收事务组ID，并放到 ThreadLocal 中
2. 重写 connection.setAutoCommit() 方法，避免在等待提交时变为已提交的状态
3. 删除 connection.close() 的实现，在等待提交时，连接不可关闭。