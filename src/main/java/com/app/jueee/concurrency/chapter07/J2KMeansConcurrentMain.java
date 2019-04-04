package com.app.jueee.concurrency.chapter07;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.app.jueee.concurrency.chapter07.common.Document;
import com.app.jueee.concurrency.chapter07.common.DocumentCluster;
import com.app.jueee.concurrency.chapter07.common.DocumentLoader;
import com.app.jueee.concurrency.chapter07.common.VocabularyLoader;

public class J2KMeansConcurrentMain {

    /**
     * 
     * @param documents Document 对象数组，它存放了有关文档的信息
     * @param clusterCount 要生成的簇的数目
     * @param vocSize 词汇表的大小
     * @param seed 用于随机数生成器的“种子”
     * @return
     */
    public static DocumentCluster[] calculate(Document[] documents, int numberCluster, int vocSize, int seed, int maxSize) {
        // 创建一个由 clusterCount 参数确定的簇的数组，并且使用 initialize() 方法和 Random 对象对其初始化
        DocumentCluster[] clusters = new DocumentCluster[numberCluster];
        Random random = new Random(seed);
        for (int i = 0; i < numberCluster; i++) {
            clusters[i] = new DocumentCluster(vocSize, new ArrayList<>());
            clusters[i].initialize(random);
        }

        // 重复指派和更新阶段，直到所有文档对应的簇都不再变化为止。
        boolean change = true;
        ForkJoinPool pool = new ForkJoinPool();
        int numSteps = 0;
        while (change) {
            change = assignment(clusters, documents, maxSize, pool);
            update(clusters, maxSize, pool);
            numSteps++;
        }
        pool.shutdown();
        System.out.println("Number of steps: " + numSteps);

        // 返回描述了文档最终组织情况的簇数组
        return clusters;
    }

    /**
     * 指派阶段的工作 对于每个文档，该方法都计算其与所有簇之间的欧氏距离，并且将该文档指派到距离最短的簇。
     * 
     * @param clusters
     * @param documents
     * @return 该方法返回一个布尔值，该值表明从当前位置到下一位置是否有一个或多个文档改变了为其指派的簇。
     */
    private static boolean assignment(DocumentCluster[] clusters, Document[] documents, int maxSize, ForkJoinPool pool) {
        for (DocumentCluster cluster : clusters) {
            cluster.clearClusters();
        }

        AtomicInteger numChanges = new AtomicInteger(0);
        AssignmentTask task = new AssignmentTask(clusters, documents, 0, documents.length, numChanges, maxSize);
        pool.execute(task);
        task.join();
            
        System.out.println("Number of Changes: " + numChanges);
        return numChanges.get() > 0;
    }

    /**
     * 更新阶段，直接重新计算每个簇的质心
     * 
     * @param clusters 带有簇信息的 DocumentCluster 对象数组
     */
    private static void update(DocumentCluster[] clusters, int maxSize, ForkJoinPool pool) {
        UpdateTask task = new UpdateTask(clusters, 0, clusters.length, maxSize);
        pool.execute(task);
        task.join();
    }

    public static void main(String[] args) {
        try {
            // 从文件加载数据--单词
            Path pathVoc = Paths.get("data//chapter07", "movies.words");
            Map<String, Integer> vocIndex = VocabularyLoader.load(pathVoc);
            System.out.println("Voc Size:" + vocIndex.size());

            // 从文件加载数据--文档
            Path pathDocs = Paths.get("data//chapter07", "movies.data");
            Document[] documents = DocumentLoader.load(pathDocs, vocIndex);
            System.out.println("Document Size:" + documents.length);

            //
            int K = 10;
            int SEED = 100;
            int MAX_SIZE = 10;
            
            Date start, end;
            start = new Date();
            DocumentCluster[] clusters = calculate(documents, K, vocIndex.size(), SEED, MAX_SIZE);
            end = new Date();
            System.out.println("K: " + K + "; SEED: " + SEED);
            System.out.println("Execution Time: " + (end.getTime() - start.getTime()));
            System.out.println(
                Arrays.stream(clusters).map(DocumentCluster::getDocumentCount).sorted(Comparator.reverseOrder())
                    .map(Object::toString).collect(Collectors.joining(", ", "Cluster sizes: ", "")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
