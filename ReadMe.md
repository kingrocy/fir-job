# 分布式定时任务调度平台

# 设计

任务落在数据库中

将要执行的任务会被扫描 添加到延时队列中

延时队列只提供触发任务执行

任务执行由线程池处理


# 任务存储流程

1. 新增一个任务，根据其延时策略，计算下次执行时间离当前时间差多少时间（相差1分钟之内的直接加入），然后判断此任务是否直接进入延时队列。

然后，再将此任务存入到数据库中。

2. 服务端启动一个线程，一直去扫描数据库中的任务（每10/20/30s执行一次）。。然后将临近执行的任务存入到延时队列中。


# 任务执行流程

1. 客户端启动 需要将自身注册到服务端中。（需要指定appName..）(appName+ip 代表唯一机器)

2. 服务端执行任务，先取出任务。。根据任务所绑定的appName 去取一台机器。

3. 服务端根据机器ip 去向客户端发送消息 

4. 客户端执行完任务 回调告诉服务端执行状态

5. 服务端更新任务执行状态

