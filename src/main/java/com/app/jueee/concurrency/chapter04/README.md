## 充分利用执行器
探究执行器的高级特性，这些特性使它们成为支持并发应用程序的强大工具。

### 基本内容
1.	[执行器的高级特性](C1执行器的高级特性.md)
2.	[第一个例子：高级服务器应用程序](C2第一个例子：高级服务器应用程序.md)
3.	[第二个例子：执行周期性任务](C3第二个例子：执行周期性任务.md)
4.	[有关执行器的其他信息](C4有关执行器的其他信息.md)

### 小结
-	通过扩展 ThreadPoolExecutor 类实现了自己的执行器，以便按照优先级执行任务，并且度量每个用户任务的执行时间。此外，还引入了一种新的命令支持任务的撤销。  

-	解释了如何使用 ScheduledThreadPoolExecutor 类执行周期性任务。  
	实现了两个版本的新闻阅读器。     
	第一个版本展示了如何使用 ScheduledExecutorService 的基本功能  
	第二个版本展示了如何覆盖 ScheduledExecutorService 类的行为，例如改变任务两次执行之间的延迟时间。