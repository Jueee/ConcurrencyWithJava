## 使用 MultithreadedTC 测试并发应用程序
### 下载
[https://code.google.com/archive/p/multithreadedtc/downloads](https://code.google.com/archive/p/multithreadedtc/downloads)  

最新版本是 2007 年发布的，不过仍然可以使用它测试小型并发应用程序或者单独测试大型应用程序的部件。  
尽管不能用它测试实际任务或者线程，但是可以使用它测试不同的执行顺序，从而检验是否会导致竞争条件或者死锁。

它基于一个内部时钟进行计时，该时钟可以控制不同线程的执行顺序，以测试该执行顺序是否会导致什么并发问题。

### 使用
首先，需要将两个库关联到项目中。
-	MultithreadedTC 库：最新版本是 1.01 版。

-	JUnit 库：我们使用 4.12 版测试了这个例子。

要使用 MultithreadedTC 库实施测试，要扩展 MultithreadedTestCase 类，该类扩展了 JUnit 库的 Assert 类。  

可以实现如下方法。
-	initialize() ：  
该方法将在测试执行开始时执行。  
如果需要执行初始化代码以创建数据对象、数据库连接等，可以重载该方法。

-	finish() ：  
该方法将在测试执行结束后执行。可以对其重载以实现对测试的验证。
-	threadXXX() ：  
可以为测试中的每个线程实现一个名称以 thread 关键字开头的方法。  

例如，如果想要测试三个线程，就要在自己的类中实现三个方法。

### waitForTick() 方法
MultithreadedTestCase 类提供了 waitForTick() 方法。  
-	该方法接收你要等待的时数作为参数。

-	该方法使调用线程休眠，直到内部时钟达到该时刻为止。  
第一个时刻是时数为 0 的时刻。MultithreadedTC框架以特定时间间隔检查测试线程的状态。  
如果所有运行的线程都在 waitForTick() 方法中等待，那么它将增加时数，并且唤醒所有等待该时刻的线程。

### 缺点
它仅对测试基本的并发代码有用，因此当你实施测试时，不能用它来测试真实的线程代码。

### 测试
-	[MainOk](MultithreadedTC/MainOk.java)  
当测试开始执行时，两个线程（ threadAdd() 和 threadSub() ）以并发方式启动。   
threadAdd() 线程开始执行其代码，而 threadSub() 线程则在 waitForTick() 方法中等待。  
当 threadAdd() 线程完成执行后，MultithreadedTC的内部时钟探测到在 waitForTick() 方法中只有一个线程正在等待，因此它将时数增加到 1 ，并且唤醒执行其代码的线程。

-	[MainKo](MultithreadedTC/MainKo.java)  
将导致一个竞争条件。  
在这种情况下，执行顺序要保证两个线程都首先读取数据的值，然后进行操作，因此最后的结果就不会正确。  
在这种情况下， assertEquals() 方法会抛出一个异常，因为预期的值和实际值不一样。
