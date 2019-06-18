## JVM 中的并发处理：Clojure、带有 GPars 库的 Groovy 以及 Scala
Java 是最受欢迎的编程语言，但并不是实现 Java 虚拟机（JVM）程序的唯一编程语言。  

本章将对其中三种语言提供的并发元素进行简要介绍。
-	Clojure：  
提供 Atom、Agent 等引用类型，以及 Future 和 Promise 等其他元素。

-	Groovy：  
通过 GPars 库提供面向数据并行化处理的元素，它拥有自己的 Actor 模型、Agent 和 Dataflow。
-	Scala：  
提供 Future 和 Promise 两个元素。

### 基本内容
1.	[Clojure 的并发处理](C1Clojure的并发处理.md)  
1.1	使用 Java 元素  
1.2	引用类型  
1.3	Ref 对象  
1.4	Delay  
1.5	Future  
1.6	Promise  
2.	[Groovy 及其 GPars 库的并发处理](C2Groovy 及其 GPars 库的并发处理.md)  
2.1	使用 Java 元素  
2.2	数据并行处理  
2.3	Fork/Join 处理  
2.4	Actor  
2.5	Agent  
2.6	Dataflow  
3.	[Scala 的并发处理](C3Scala 的并发处理.md)  
3.1	Scala 中的 Future 对象  
3.2	Promise  

### 小结
-	了解到如何使用面向 JVM 的三种语言来实现并发应用程序。
