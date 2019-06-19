# ConcurrencyWithJava
《精通 Java 并发编程（第2 版 ）》学习笔记  
##### 所用书籍
Mastering Concurrency Programming with Java 9，Javier Fernandez Gonzalez 著。  
[GitHub 地址](https://github.com/PacktPublishing/Mastering-Concurrency-Programming-with-Java-9-Second-Edition)


### [01. 第一步：并发设计原理](src/main/java/com/app/jueee/concurrency/chapter01)
1.	[基本的并发概念](src/main/java/com/app/jueee/concurrency/chapter01/C1基本的并发概念.md)
2.	[并发应用程序中可能出现的问题](src/main/java/com/app/jueee/concurrency/chapter01/C2并发应用程序中可能出现的问题.md)
3.	[设计并发算法的方法论](src/main/java/com/app/jueee/concurrency/chapter01/C3设计并发算法的方法论.md)
4.	[Java 并发 API](src/main/java/com/app/jueee/concurrency/chapter01/C4Java%20并发%20API.md)
5.	[并发设计模式](src/main/java/com/app/jueee/concurrency/chapter01/C5并发设计模式.md)
6.	[设计并发算法的提示和技巧](src/main/java/com/app/jueee/concurrency/chapter01/C6设计并发算法的提示和技巧.md)

### [02. 使用基本元素： Thread 和 Runnable](src/main/java/com/app/jueee/concurrency/chapter02)
1.	[Java 中的线程](src/main/java/com/app/jueee/concurrency/chapter02/C1Java中的线程.md)  
	1.1	Java 中的线程：特征和状态。  
	1.2	Thread 类和 Runnable 接口。  
3.	[第一个例子：矩阵乘法](src/main/java/com/app/jueee/concurrency/chapter02/C2第一个例子：矩阵乘法.md)
4.	[第二个例子：文件搜索](src/main/java/com/app/jueee/concurrency/chapter02/C3第二个例子：文件搜索.md)

### [03. 管理大量线程：执行器](src/main/java/com/app/jueee/concurrency/chapter03)
1.	[执行器简介](src/main/java/com/app/jueee/concurrency/chapter03/C1执行器简介.md)  
	1.1	执行器的基本特征  
	1.2	执行器框架的基本组件  
2.	[第一个例子：k-最近邻算法](src/main/java/com/app/jueee/concurrency/chapter03/C2第一个例子：k-最近邻算法.md)
3.	[第二个例子：客户端/服务器环境下的并发处理](src/main/java/com/app/jueee/concurrency/chapter03/C3第二个例子：客户端-服务器环境下的并发处理.md)
4.	[其他重要方法](src/main/java/com/app/jueee/concurrency/chapter03/C4其他重要方法.md)

### [04. 充分利用执行器](src/main/java/com/app/jueee/concurrency/chapter04)
1.	[执行器的高级特性](src/main/java/com/app/jueee/concurrency/chapter04/C1执行器的高级特性.md)
2.	[第一个例子：高级服务器应用程序](src/main/java/com/app/jueee/concurrency/chapter04/C2第一个例子：高级服务器应用程序.md)
3.	[第二个例子：执行周期性任务](src/main/java/com/app/jueee/concurrency/chapter04/C3第二个例子：执行周期性任务.md)
4.	[有关执行器的其他信息](src/main/java/com/app/jueee/concurrency/chapter04/C4有关执行器的其他信息.md)

### [05. 从任务获取数据： Callable 接口与 Future 接口](src/main/java/com/app/jueee/concurrency/chapter05)
1.	[Callable 接口和 Future 接口简介](src/main/java/com/app/jueee/concurrency/chapter05/C1Callable 接口和 Future 接口简介.md)
2.	[第一个例子：单词最佳匹配算法](src/main/java/com/app/jueee/concurrency/chapter05/C2第一个例子：单词最佳匹配算法.md)
3.	[第二个例子：为文档集创建倒排索引](src/main/java/com/app/jueee/concurrency/chapter05/C3第二个例子：为文档集创建倒排索引.md)
4.	[其他重要方法](src/main/java/com/app/jueee/concurrency/chapter05/C4其他重要方法.md)

### [06. 运行分为多阶段的任务：Phaser类](src/main/java/com/app/jueee/concurrency/chapter06)
1.	[Phaser 类简介](src/main/java/com/app/jueee/concurrency/chapter06/C1Phaser类简介.md)
2.	[第一个例子：关键字抽取算法](src/main/java/com/app/jueee/concurrency/chapter06/C2第一个例子：关键字抽取算法.md)
3.	[第二个例子：遗传算法](src/main/java/com/app/jueee/concurrency/chapter06/C3第二个例子：遗传算法.md)

### [07. 优化分治解决方案：Fork/Join 框架](src/main/java/com/app/jueee/concurrency/chapter07)
1.	[Fork/Join 框架简介](src/main/java/com/app/jueee/concurrency/chapter07/C1Fork-Join框架简介.md)
2.	[第一个例子：k-means 聚类算法](src/main/java/com/app/jueee/concurrency/chapter07/C2第一个例子：k-means聚类算法.md)
3.	[第二个例子：数据筛选算法](src/main/java/com/app/jueee/concurrency/chapter07/C3第二个例子：数据筛选算法.md)
4.	[第三个例子：归并排序算法](src/main/java/com/app/jueee/concurrency/chapter07/C4第三个例子：归并排序算法.md)
5.	[Fork/Join 框架的其他方法](src/main/java/com/app/jueee/concurrency/chapter07/C5Fork-Join框架的其他方法.md)

### [08. 使用并行流处理大规模数据集：MapReduce 模型](src/main/java/com/app/jueee/concurrency/chapter08)
1.	[流的简介](src/main/java/com/app/jueee/concurrency/chapter08/C1流的简介.md)
2.	[第一个例子：数值综合分析应用程序](src/main/java/com/app/jueee/concurrency/chapter08/C2第一个例子：数值综合分析应用程序.md)
3.	[第二个例子：信息检索工具](src/main/java/com/app/jueee/concurrency/chapter08/C3第二个例子：信息检索工具.md)
4.	[约简操作简介](src/main/java/com/app/jueee/concurrency/chapter08/C4约简操作简介.md)

### [09. 使用并行流处理大规模数据集：MapCollect 模型](src/main/java/com/app/jueee/concurrency/chapter09)
1.	[使用流收集数据](src/main/java/com/app/jueee/concurrency/chapter09/C1使用流收集数据.md)
2.	[第一个例子：无索引条件下的数据搜索](src/main/java/com/app/jueee/concurrency/chapter09/C2第一个例子：无索引条件下的数据搜索.md)
3.	[第二个例子：推荐系统](src/main/java/com/app/jueee/concurrency/chapter09/C3第二个例子：推荐系统.md)
4.	[第三个例子：社交网络中的共同联系人](src/main/java/com/app/jueee/concurrency/chapter09/C4第三个例子：社交网络中的共同联系人.md)

### [10. 异步流处理：反应流](src/main/java/com/app/jueee/concurrency/chapter10)
1.	[Java 反应流简介](src/main/java/com/app/jueee/concurrency/chapter10/C1Java反应流简介.md)
2.	[第一个例子：面向事件通知的集中式系统](src/main/java/com/app/jueee/concurrency/chapter10/C2第一个例子：面向事件通知的集中式系统.md)
3.	[第二个例子：新闻系统](src/main/java/com/app/jueee/concurrency/chapter10/C3第二个例子：新闻系统.md)

### [11. 探究并发数据结构和同步工具](src/main/java/com/app/jueee/concurrency/chapter11)
1.	[并发数据结构](src/main/java/com/app/jueee/concurrency/chapter11/C10并发数据结构.md)  
1.1	[阻塞型数据结构和非阻塞型数据结构](src/main/java/com/app/jueee/concurrency/chapter11/C11.阻塞型数据结构和非阻塞型数据结构.md)  
1.2	[并发数据结构](src/main/java/com/app/jueee/concurrency/chapter11/C12.并发数据结构.md)  
1.3	[使用新特性](src/main/java/com/app/jueee/concurrency/chapter11/C13.使用新特性.md)  
1.4	[原子变量](src/main/java/com/app/jueee/concurrency/chapter11/C14.原子变量.md)  
1.5	[变量句柄](src/main/java/com/app/jueee/concurrency/chapter11/C15.变量句柄.md)  
2.	[同步机制](src/main/java/com/app/jueee/concurrency/chapter11/C20同步机制.md)  
2.1	[CommonTask 类](src/main/java/com/app/jueee/concurrency/chapter11/CommonTask.java)  
2.2	[Lock 接口](src/main/java/com/app/jueee/concurrency/chapter11/J22Lock.java)  
2.3	[Semaphore 类](src/main/java/com/app/jueee/concurrency/chapter11/J23Semaphore.java)  
2.4	[CountDownLatch 类](src/main/java/com/app/jueee/concurrency/chapter11/J24CountDownLatch.java)  
2.5	[CyclicBarrier 类](src/main/java/com/app/jueee/concurrency/chapter11/J25CyclicBarrier.java)  
2.6	[CompletableFuture 类](src/main/java/com/app/jueee/concurrency/chapter11/J26CompletableFuture.java)  

### [12. 测试与监视并发应用程序](src/main/java/com/app/jueee/concurrency/chapter12)
1.	[监视并发对象](src/main/java/com/app/jueee/concurrency/chapter12/C10监视并发对象.md)  
1.1	[监视线程](src/main/java/com/app/jueee/concurrency/chapter12/J11MonitorThread.java)  
1.2	[监视锁](src/main/java/com/app/jueee/concurrency/chapter12/J12MonitorLock.java)  
1.3	[监视执行器](src/main/java/com/app/jueee/concurrency/chapter12/J13MonitorExecutor.java)  
1.4	[监视 Fork/Join 框架](src/main/java/com/app/jueee/concurrency/chapter12/J14MonitorForkJoin.java)  
1.5	[监视 Phaser](src/main/java/com/app/jueee/concurrency/chapter12/J15MonitorPhaser.java)  
1.6	[监视流 API](src/main/java/com/app/jueee/concurrency/chapter12/J16MonitorStreamAPI.java)  
2.	[监视并发应用程序](src/main/java/com/app/jueee/concurrency/chapter12/C20监视并发应用程序.md)  
2.1	Overview 选项卡  
2.2	Memory 选项卡  
2.3	Threads 选项卡  
2.4	Classes  选项卡  
2.5	VM Summary 选项卡  
2.6	MBeans 选项卡  
2.7	About 选项卡  
3.	[测试并发应用程序](src/main/java/com/app/jueee/concurrency/chapter12/C30测试并发应用程序.md)  
3.1	[使用MultithreadedTC](src/main/java/com/app/jueee/concurrency/chapter12/C31使用MultithreadedTC.md)  
3.2	[使用 Java Pathfinder](src/main/java/com/app/jueee/concurrency/chapter12/C32.使用JavaPathfinder.md) 

### [13. JVM 中的并发处理：Clojure、带有 GPars 库的 Groovy 以及 Scala](src/main/java/com/app/jueee/concurrency/chapter13)
1.	[Clojure 的并发处理](src/main/java/com/app/jueee/concurrency/chapter13/C1Clojure的并发处理.md)  
1.1	使用 Java 元素  
1.2	引用类型  
1.3	Ref 对象  
1.4	Delay  
1.5	Future  
1.6	Promise  
2.	[Groovy 及其 GPars 库的并发处理](src/main/java/com/app/jueee/concurrency/chapter13/C2Groovy及其GPars库的并发处理.md)  
2.1	使用 Java 元素  
2.2	数据并行处理  
2.3	Fork/Join 处理  
2.4	Actor  
2.5	Agent  
2.6	Dataflow  
3.	[Scala 的并发处理](src/main/java/com/app/jueee/concurrency/chapter13/C3Scala的并发处理.md)  
3.1	Scala 中的 Future 对象  
3.2	Promise  
