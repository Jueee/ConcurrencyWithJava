## Fork/Join 框架简介

#### 执行器框架
**执行器框架**采用了一个线程池，该线程池可以执行你发送给执行器的任务，并且针对多个任务重用这些线程。  

这种机制为程序设计人员提供了如下便利。
-	并发应用程序的编程更加简单，因为不再需要担心线程的创建。

-	控制执行器和应用程序所使用的资源更加简单。  
可以创建一个仅使用预定数目线程的执行器。  
如果发送较多的任务，则执行器会将它们先存放在一个队列中，直到有线程可用为止。
-	执行器通过重用线程缩减了创建线程所引入的开销。  
从内部来看，它管理了一个线程池，重用线程来执行多个任务。

#### 分治算法
**分治算法**是一种非常流行的设计方法。为了采用这种方法解决问题，要将问题划分为较小的问题。  

可以采用递归方式重复该过程，直到需要解决的问题变得很小，可以直接解决。  
必须很小心地选择可直接解决的基本用例，问题规模选择不当会导致糟糕的性能。  
这种问题可以使用执行器解决，但是为了更高效地解决问题，Java 7 并发 API 引入了 `Fork/Join` 框架。

该框架基于 `ForkJoinPool` 类，该类是一种特殊的执行器，具有 `fork()` 方法和 `join()` 方法两个操作（以及它们的不同变体），以及一个被称作工作窃取算法的内部算法。

#### Fork/Join 示例
-	[ForkJoinDemo](J1ForkJoinDemo.java)

### Fork/Join 框架的基本特征
`Fork/Join` 框架必须用于解决基于分治方法的问题。  
必须将原始问题划分为较小的问题，直到问题很小，可以直接解决。  

有了这个框架，待实现任务的主方法便如下所示：

```java
if ( problem.size() > DEFAULT_SIZE) {
	divideTasks();
	executeTask();
	taskResults = joinTasksResult();
	return taskResults;
} else {
	taskResults = solveBasicProblem();
	return taskResults;
}
```
该方法最大的好处是可以高效分割和执行子任务，并且获取子任务的结果以计算父任务的结果。

该功能由 `ForkJoinTask` 类提供的如下两个方法支持。
-	`fork()` 方法：该方法可以将一个子任务发送给 Fork/Join 执行器。
-	`join()` 方法：该方法可以等待一个子任务执行结束后返回其结果。

#### 工作窃取算法
`Fork/Join` 框架还有另一个关键特性，即工作窃取算法。该算法确定要执行的任务。  

当一个任务使用 `join()` 方法等待某个子任务结束时，执行该任务的线程将会从任务池中选取另一个等待执行的任务并且开始执行。  
通过这种方式，`Fork/Join` 执行器的线程总是通过改进应用程序的性能来执行任务。

#### 公用池
Java 8在 `Fork/Join` 框架中提供了一种新特性。  
每个Java应用程序都有一个默认的 `ForkJoinPool` ，称作**公用池**。  

可以通过调用静态方法 `ForkJoinPool.commonPool()` 获得这样的公用池，而不需要采用显式方法创建（尽管可以这样做）。  
这种默认的 `Fork/Join` 执行器会自动使用由计算机的可用处理器确定的线程数。  
可以通过更改系统属性值 `java.util.concurrent.ForkJoinPool.common.parallelism` 来修改这一默认行为。 


### Fork/Join 框架的局限性
解决问题时必须要考虑到它的局限性，主要有如下几个方面。
-	不再进行细分的基本问题的规模既不能过大也不能过小。  
按照 Java API 文档的说明，该基本问题的规模应该介于 100 到 10 000 个基本计算步骤之间。

-	数据可用前，不应使用阻塞型 I/O 操作，例如读取用户输入或者来自网络套接字的数据。  
这样的操作将导致 CPU 核资源空闲，降低并行处理等级，进而使性能无法达到最佳。
-	不能在任务内部抛出校验异常，必须编写代码来处理异常（例如，陷入未经校验的RuntimeException ）。


### Fork/Join 框架的组件
`Fork/Join` 框架包括四个基本类。
-	`ForkJoinPool` 类：  
该类实现了 `Executor` 接口和 `ExecutorService` 接口，而执行 `Fork/Join` 任务时将用到 Executor 接口。  
Java 提供了一个默认的 `ForkJoinPool` 对象（称作公用池），但是如果需要，你还可以创建一些构造函数。  
可以指定并行处理的等级（运行并行线程的最大数目）。默认情况下，它将可用处理器的数目作为并发处理等级。

-	`ForkJoinTask` 类：  
这是所有 `Fork/Join` 任务的基本抽象类。  
该类是一个抽象类，提供了 `fork()` 方法和 `join()` 方法，以及这些方法的一些变体。  
该类还实现了 Future 接口，提供了一些方法来判断任务是否以正常方式结束，它是否被撤销，或者是否抛出了一个未校验异常。  
`RecursiveTask` 类、 `RecursiveAction` 类和 `CountedCompleter` 类提供了 `compute()` 抽象方法。  
为了执行实际的计算任务，该方法应该在子类中实现。
-	`RecursiveTask` 类：  
该类扩展了 ForkJoinTask 类。   
RecursiveTask 也是一个抽象类，而且应该作为实现返回结果的 `Fork/Join` 任务的起点。
-	`RecursiveAction` 类：  
该类扩展了 ForkJoinTask 类。   
RecursiveAction 类也是一个抽象类，而且应该作为实现不返回结果的 `Fork/Join` 任务的起点。
-	`CountedCompleter` 类：  
该类扩展了 ForkJoinTask 类。  
该类应作为实现任务完成时触发另一任务的起点。



