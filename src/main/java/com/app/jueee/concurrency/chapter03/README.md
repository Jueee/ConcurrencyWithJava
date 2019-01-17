## 管理大量线程：执行器
实现简单的并发应用程序时，要为每个并发任务创建一个线程并执行。  
这种方式会引发一些重要问题。  

从 Java 5 开始，Java 并发 API 便引入了执行器框架，用以改善那些执行大量并发任务的并发应用程序的性能。

### 基本内容
1.	执行器简介  
	1.1	执行器的基本特征  
	1.2	执行器框架的基本组件  
3.	第一个例子：k-最近邻算法
4.	第二个例子：客户端/服务器环境下的并发处理

### 小结
-	执行器框架的基本特征及其构成组件。  

-	探讨了 Executor 接口，它定义了将 Runnable 任务发送给执行器的基本方法。   
该接口有一个子接口 ExecutorService ，该子接口所包含的方法可向执行器发送返回结果的任务和一个任务列表。
-	ThreadPoolExecutor 类是这两种接口的基本实现：  
增加额外的方法以获取有关执行器状态的信息，以及正在执行的线程或任务的数量。  
为该类创建对象最简单的方式是使用 Executors 工具类，该类包含了创建不同类型执行器的方法。