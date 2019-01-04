## Java 并发 API  
### 基本并发类
并发 API 的基本类如下。
-	**Thread 类**：该类描述了执行并发 Java 应用程序的所有线程。

-	**Runnable 接口**：这是 Java 中创建并发应用程序的另一种方式。
-	**ThreadLocal 类**：该类用于存放从属于某一线程的变量。
-	**ThreadFactory 接口**：这是实现 Factory 设计模式的基类，你可以用它来创建定制线程。
  
  
### 同步机制
Java 并发 API 包括多种同步机制，可以支持你：
-	定义用于访问某一共享资源的临界段；
-	在某一共同点上同步不同的任务。

下面是最重要的同步机制。
-	**synchronized 关键字**： synchronized 关键字允许你在某个代码块或者某个完整的方法中定义一个临界段。

-	**Lock 接口**： Lock 提供了比 synchronized 关键字更为灵活的同步操作。   
Lock 接口有多种不同类型：     
	1.	ReentrantLock 用于实现一个可与某种条件相关联的锁；   
	2.	ReentrantRead-WriteLock 将读写操作分离开来；   
	3.	StampedLock 是 Java 8 中增加的一种新特性，它包括三种控制读/写访问的模式。
-	**Semaphore 类**：该类通过实现经典的信号量机制来实现同步。Java 支持二进制信号量和一般信号量。
-	**CountDownLatch 类**：该类允许一个任务等待多项操作的结束。
-	**CyclicBarrier 类**：该类允许多线程在某一共同点上进行同步。
-	**Phaser 类**：该类允许你控制那些分割成多个阶段的任务的执行。  
在所有任务都完成当前阶段之前，任何任务都不能进入下一阶段。
  
  
### 执行器
执行器框架是在实现并发任务时将线程的创建和管理分割开来的一种机制。  
你不必担心线程的创建和管理，只需要关心任务的创建并且将其发送给执行器。  

该框架中涉及的主要类如下。
-	**Executor 接口和 ExecutorService 接口**：它们包含了所有执行器共有的 execute() 方法。

-	**ThreadPoolExecutor 类**：该类允许你获取一个含有线程池的执行器，而且可以定义并行任
务的最大数目。
-	**ScheduledThreadPoolExecutor 类**：这是一种特殊的执行器，可以使你在某段延迟之后执
行任务或者周期性执行任务。
-	**Executors**：该类使执行器的创建更为容易。
-	**Callable 接口**：这是 Runnable 接口的替代接口——可返回值的一个单独的任务。
-	**Future 接口**：该接口包含了一些能获取 Callable 接口返回值并且控制其状态的方法。
  
  
### Fork/Join 框架
Fork/Join 框架定义了一种特殊的执行器，尤其针对采用分治方法进行求解的问题。  
针对解决这类问题的并发任务，它还提供了一种优化其执行的机制。Fork/Join 是为细粒度并行处理量身定制的，因为它的开销非常小，这也是将新任务加入队列中并且按照队列排序执行任务的需要。  

该框架涉及的主要类和接口如下。
-	**ForkJoinPool**：该类实现了要用于运行任务的执行器。

-	**ForkJoinTask**：这是一个可以在 ForkJoinPool 类中执行的任务。
-	**ForkJoinWorkerThread**：这是一个准备在 ForkJoinPool 类中执行任务的线程。
  
  
### 并行流
流和 lambda 表达式可能是 Java 8 中最重要的两个新特性。  
流已经被增加为 Collection 接口和其他一些数据源的方法，它允许处理某一数据结构的所有元素、生成新的结构、筛选数据和使用 MapReduce 方法来实现算法。  
并行流是一种特殊的流，它以一种并行方式实现其操作。  

使用并行流时涉及的最重要的元素如下。
-	**Stream 接口**：该接口定义了所有可以在一个流上实施的操作。

-	**Optional**：这是一个容器对象，可能（也可能不）包含一个非空值。
-	**Collectors**：该类实现了约简（reduction）操作，而该操作可作为流操作序列的一部分使用。
-	**lambda 表达式**：流被认为是可以处理 lambda 表达式的。大多数流方法都会接收一个 lambda
表达式作为参数，这让你可以实现更为紧凑的操作。
  
  
### 并发数据结构
Java API 中的常见数据结构（例如 ArrayList 、 Hashtable 等）并不能在并发应用程序中使用，除非采用某种外部同步机制。  
但是如果你采用了某种同步机制，应用程序就会增加大量的额外计算时间。  
而如果你不采用同步机制，那么应用程序中很可能出现竞争条件。  
如果你在多个线程中修改数据，那么就会出现竞争条件，你可能会面对各种异常（例如 ConcurrentModificationException 和 ArrayIndexOutOfBoundsException ），出现隐性数据丢失，或者应用程序会陷入死循环。  

Java 并发 API 中含有大量可以在并发应用中使用而没有风险的数据结构。  
我们将它们分为以下两大类别。
-	**阻塞型数据结构**：这些数据结构含有一些能够阻塞调用任务的方法，例如，当数据结构为空
而你又要从中获取值时。

-	**非阻塞型数据结构**：如果操作可以立即进行，它并不会阻塞调用任务。否则，它将返回 null
值或者抛出异常。

下面是其中的一些数据结构。
-	**ConcurrentLinkedDeque**：这是一个非阻塞型的列表。  

-	**ConcurrentLinkedQueue**：这是一个非阻塞型的队列。
-	**LinkedBlockingDeque**：这是一个阻塞型的列表。
-	**LinkedBlockingQueue**：这是一个阻塞型的队列。
-	**PriorityBlockingQueue**：这是一个基于优先级对元素进行排序的阻塞型队列。
-	**ConcurrentSkipListMap**：这是一个非阻塞型的 NavigableMap 。
-	**ConcurrentHashMap**：这是一个非阻塞型的哈希表。
-	**AtomicBoolean 、 AtomicInteger 、 AtomicLong 和 AtomicReference**：这些是基本 Java 数据类型的原子实现。