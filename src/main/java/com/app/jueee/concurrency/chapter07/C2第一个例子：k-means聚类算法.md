## 第一个例子：k-means 聚类算法
k-means 聚类算法将预先未分类的项集分组到预定的 K 个簇。  
它在数据挖掘和机器学习领域非常流行，并且在这些领域中用于以无监督方式组织和分类数据。 
 
每一项通常都由一个特征（或者说属性）向量定义。所有项都有相同数目的属性。  
每个簇也由一个含有同样属性数目的向量定义，这些属性描述了所有可分类到该簇的项。该向量叫作 `centroid` 。  
例如，如果这些项是用数值型向量定义的，那么簇就定义为划分到该簇的各项的平均值。

该算法基本上可以分为四个步骤。
-	**初始化**：  
在第一步中，要创建最初代表 K 个簇的向量，通常，你可以随机初始化这些向量。

-	**指派**：  
可以将每一项划分到一个簇中。  
为了选择该簇，可以计算项和每个簇之间的距离。  
可以使用欧氏距离作为距离度量方式，计算代表项的向量和代表簇的向量之间的距离。  
之后，你可以将该项分配到与其距离最短的簇中。
-	**更新**：  
一旦对所有项进行分类之后，必须重新计算定义每个簇的向量。  
如前所述，通常要计算划分到该簇所有项的向量的平均值。
-	**结束**：  
最后，检查是否有些项改变了为其指派的簇。  
如果存在变化，需要再次转入指派步骤。否则算法结束，所有项都已分类完毕。

该算法有如下两个主要局限。
-	如果随机初始化最初的簇向量，那么对同一项集执行两次分类的结果是不同的。

-	簇的数目是预先定义好的。从分类的视角来看，如果属性选择得不好将会导致糟糕的结果。

### 目标
实现一个应用程序来对某个文档集进行聚类。

### 公共类
-	[VocabularyLoader](common/VocabularyLoader.java) 类：  
用于加载文档集中构成词汇表的单词列表。

-	[Word](common/Word.java) 类：  
用于存放单词字符串和度量该单词的指标（ TF 、 DF 和 TF-IDF ）。
-	[Document](common/Document.java) 类：  
用于存放单词字符串以及将该单词作为关键字的文档数量。
-	[DocumentLoader](common/DocumentLoader.java) 类：  
用于加载有关文档的信息。  
-	[DistanceMeasurer](common/DistanceMeasurer.java) 类：  
用于计算两个向量之间的欧氏距离。 
-	[DocumentCluster](common/DocumentCluster.java) 类：  
用于存储有关簇的信息。

### 串行版本
-	[J2KMeansSerialMain](J2KMeansSerialMain.java) 类：  
实现了 k-means 聚类算法的串行版本。

### 并发版本
并发版本运用了 Fork/Join 框架。  
我们已经基于 RecursiveAction 类实现了两种任务。  
如前所述，当希望使用 Fork/Join框架处理不返回结果的任务时，可以使用 RecursiveAction 任务。  
将指派阶段和更新阶段的工作作为在 Fork/Join 框架中执行的任务来实现。

-	[AssignmentTask](AssignmentTask.java) 类：  

-	[UpdateTask](UpdateTask.java) 类：  
-	[J2KMeansConcurrentMain](J2KMeansConcurrentMain.java) 类：  
实现了算法的并发版本，测试算法的并发版本。




