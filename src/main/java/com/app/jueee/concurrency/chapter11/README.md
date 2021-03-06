## 探究并发数据结构和同步工具
数据结构使我们可以存放数据，从而使应用程序可以按照需求以不同的方式读取、转换和写入这些数据。  

选择一种适当的数据结构是获得良好性能的关键。  
做出了糟糕的选择就会大幅度降低算法的性能。  

Java 并发 API 包含一些用于并发应用程序的数据结构，而它们并不会导致数据不一致或者信息丢失。

并发应用程序中的另一个关键点是同步机制。  
通过使用同步机制，可以创建一个临界段（也就是一段一次只能被一个线程执行的代码），进而实现互斥。  
不过，也可以使用同步机制实现两个线程之间的依赖关系，例如一个并发任务必须等待另一个任务完成。


### 基本内容
1.	[并发数据结构](C10并发数据结构.md)  
1.1	[阻塞型数据结构和非阻塞型数据结构](C11.阻塞型数据结构和非阻塞型数据结构.md)  
1.2	[并发数据结构](C12.并发数据结构.md)  
1.3	[使用新特性](C13.使用新特性.md)  
1.4	[原子变量](C14.原子变量.md)  
1.5	[变量句柄](C15.变量句柄.md)  
2.	[同步机制](C20同步机制.md)  
2.1	[CommonTask 类](CommonTask.java)  
2.2	[Lock 接口](J22Lock.java)  
2.3	[Semaphore 类](J23Semaphore.java)  
2.4	[CountDownLatch 类](J24CountDownLatch.java)  
2.5	[CyclicBarrier 类](J25CyclicBarrier.java)  
2.6	[CompletableFuture 类](J26CompletableFuture.java)  

### 小结
-	介绍了并发数据结构  
对 Java 8并发 API中引入的一些新功能进行了详细介绍，这主要涉及 ConcurrentHashMap 类和实现 Collection 接口的类。

-	探讨了同步机制  
对 CompletableFuture 进行了详细介绍，它是Java 8并发 API 中的一个新特性。
