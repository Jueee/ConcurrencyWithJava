## JMH 性能测试框架
JMH 是一个由 OpenJDK/Oracle 里面那群开发了 Java 编译器的大牛们所开发的 Micro Benchmark Framework 。  
何谓 Micro Benchmark 呢？简单地说就是在 method 层面上的 benchmark，精度可以精确到微秒级。

[Code Sample](http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/)

### 基本概念
#### Mode
Mode 表示 JMH 进行 Benchmark 时所使用的模式。通常是测量的维度不同，或是测量的方式不同。  

目前 JMH 共有四种模式：
-	**Throughput**  
整体吞吐量，例如“1秒内可以执行多少次调用”。

-	**AverageTime**  
调用的平均时间，例如“每次调用平均耗时xxx毫秒”。
-	**SampleTime**  
随机取样，最后输出取样结果的分布，例如“99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”
-	**SingleShotTime**  
以上模式都是默认一次 iteration 是 1s，唯有 SingleShotTime 是只运行一次。  
往往同时把 warmup 次数设为0，用于测试冷启动时的性能。

#### Iteration
Iteration 是 JMH 进行测试的最小单位。  
在大部分模式下，一次 iteration 代表的是一秒，JMH 会在这一秒内不断调用需要 benchmark 的方法，然后根据模式对其采样，计算吞吐量，计算平均执行时间等。

#### Warmup
Warmup 是指在实际进行 benchmark 前先进行预热的行为。  

为什么需要预热？  
因为 JVM 的 JIT 机制的存在，如果某个函数被调用多次之后，JVM 会尝试将其编译成为机器码从而提高执行速度。  
所以为了让 benchmark 的结果更加接近真实情况就需要进行预热。

### 注解
**@Benchmark**  
表示该方法是需要进行 benchmark 的对象，用法和 JUnit 的 @Test 类似。

**@Mode**  
Mode 如之前所说，表示 JMH 进行 Benchmark 时所使用的模式。

**@State**  
State 用于声明某个类是一个“状态”，然后接受一个 Scope 参数用来表示该状态的共享范围。  
因为很多 benchmark 会需要一些表示状态的类，JMH 允许你把这些类以依赖注入的方式注入到 benchmark 函数里。  
Scope 主要分为两种：  
-	Thread: 该状态为每个线程独享。
-	Benchmark: 该状态在所有线程间共享。

**@OutputTimeUnit**  
benchmark 结果所使用的时间单位。

**@Param**  
@Param 可以用来指定某项参数的多种情况。特别适合用来测试一个函数在不同的参数输入的情况下的性能。

**@Setup**  
@Setup 会在执行 benchmark 之前被执行，正如其名，主要用于初始化。

**@TearDown**  
@TearDown 和 @Setup 相对的，会在所有 benchmark 执行结束以后执行，主要用于资源的回收等。

### 启动选项
```java
Options opt = new OptionsBuilder()
        .include(FirstBenchmark.class.getSimpleName())
        .forks(1)
        .warmupIterations(5)
        .measurementIterations(5)
        .build();
 
new Runner(opt).run();
```
-	**include**  
benchmark 所在的类的名字，注意这里是使用正则表达式对所有类进行匹配的。

-	**fork**  
进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试。
-	**warmupIterations**  
预热的迭代次数。
-	**measurementIterations**  
实际测量的迭代次数。

### 常用选项
-	**CompilerControl**  
控制 compiler 的行为，例如强制 inline，不允许编译等。

-	**Group**  
可以把多个 benchmark 定义为同一个 group，则它们会被同时执行，主要用于测试多个相互之间存在影响的方法。
-	**Level**  
用于控制 @Setup，@TearDown 的调用时机，默认是 Level.Trial，即benchmark开始前和结束后。
-	**Profiler**
JMH 支持一些 profiler，可以显示等待时间和运行时间比，热点函数等。

### 引入 JMH
```xml
<properties>
    <jmh.version>1.14.1</jmh.version>
</properties>
 
<dependencies>
    <dependency>
        <groupId>org.openjdk.jmh</groupId>
        <artifactId>jmh-core</artifactId>
        <version>${jmh.version}</version>
    </dependency>
    <dependency>
        <groupId>org.openjdk.jmh</groupId>
        <artifactId>jmh-generator-annprocess</artifactId>
        <version>${jmh.version}</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```