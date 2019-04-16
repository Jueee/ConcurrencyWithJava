## Fork/Join 框架的其他方法

### ForkJoinPool
除了使用 ForkJoinPool 类的 `execute()` 方法和 `invoke()` 方法将任务发送给池。  
还可以使用另一个名为 `submit()` 的方法。  

它们之间的主要区别在于： 
-	`execute()` 方法将任务发送给 ForkJoinPool 之后立即返回一个 void 值； 

-	`invoke()` 方法将任务发送给 ForkJoinPool 后，当任务完成执行后方可返回；
-	`submit()` 方法将任务发送给 ForkJoinPool 之后立即返回一个 Future 对象，用以控制任务的状态并且获得其结果。

示例使用的类均基于 ForkJoinTask 类，也可以使用基于 Runnable 接口和 Callable 接口的 ForkJoinPool 任务。  
为实现这一目标，可以使用 submit() 方法。  
该方法有接收Runnable 对象作为参数的版本、接收含有结果的 Runnable 对象作为参数的版本和接收 Callable对象作为参数的版本。  

### ForkJoinTask
ForkJoinTask 类提供了 get(long timeout, TimeUnit unit) 方法来获取某个任务返回的结果。  
该方法在参数中指定了等待任务结果的时间周期。  
如果该任务在这一时间周期结束之前完成了执行，则该方法返回相应结果。  
否则，该方法抛出一个 TimeoutException 异常。

ForkJoinTask 类为 invoke() 方法提供了一种替代方案，即 quietlyInvoke() 方法。  
这两种方法的主要区别在于：
-	`invoke()` 方法返回任务执行的结果或者在必要时抛出异常  

-	`quietlyInvoke()` 方法不返回任务的结果，也不抛出任何异常。   
与示例中用到的 quietlyJoin() 方法相似。
