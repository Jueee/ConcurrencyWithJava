## 测试与监视并发应用程序
每个应用程序都必须满足最终用户的需求，而测试就是对此进行验证的阶段。  

应用程序必须在可接受的时间里按照指定格式生成有效结果。  
测试阶段的主要目标是尽可能多地检测软件中的错误并进行修正，以提高产品的整体质量。

### 基本内容
1.	[监视并发对象](C10监视并发对象.md)  
1.1	[监视线程](J11MonitorThread.java)  
1.2	[监视锁](J12MonitorLock.java)  
1.3	[监视执行器](J13MonitorExecutor.java)  
1.4	[监视 Fork/Join 框架](J14MonitorForkJoin.java)  
1.5	[监视 Phaser](J15MonitorPhaser.java)  
1.6	[监视流 API](J16MonitorStreamAPI.java)  
2.	[监视并发应用程序](C20监视并发应用程序.md)  
2.1	Overview 选项卡  
2.2	Memory 选项卡  
2.3	Threads 选项卡  
2.4	Classes  选项卡  
2.5	VM Summary 选项卡  
2.6	MBeans 选项卡  
2.7	About 选项卡  
3.	[测试并发应用程序](C30测试并发应用程序.md)  
3.1	[使用MultithreadedTC](C31使用MultithreadedTC.md)  
3.2	[使用 Java Pathfinder](C32.使用JavaPathfinder.md)  

### 小结
-	介绍了一些可以更加方便地测试并发应用程序的机制。

-	学习了如何获取 Java 并发 API 中最重要组件的状态信息，例如线程、锁、执行器或流。
-	学会了如何使用 JConsole 监视常规 Java 应用程序和特殊一点的并发应用程序。
-	学会了如何使用两种不同的工具测试并发应用程序。
