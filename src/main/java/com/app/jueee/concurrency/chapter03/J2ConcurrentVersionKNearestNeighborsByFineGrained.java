package com.app.jueee.concurrency.chapter03;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.app.jueee.concurrency.chapter03.data.Distance;
import com.app.jueee.concurrency.chapter03.data.Sample;

/**
 * k-最近邻算法：细粒度并发版本
 * 
 * @author hzweiyongqiang
 */
public class J2ConcurrentVersionKNearestNeighborsByFineGrained {
    
    // 训练数据集
    private final List<? extends Sample> dataSet;
    // 数值 k（用于确定某个实例标签的范例数量）
    private final int k;
    // 执行并行任务的 ThreadPoolExecutor 对象
    private final ThreadPoolExecutor executor;
    // 存放执行器中工作线程数
    private final int numThreads;
    // 指定是否要进行并行排序
    private final boolean parallelSort;

    /**
     * 构造函数
     * @param dataSet   训练数据集
     * @param k         数值 k
     * @param factor    从处理器获得的线程数
     * @param parallelSort
     */
    public J2ConcurrentVersionKNearestNeighborsByFineGrained(List<? extends Sample> dataSet, int k, int factor, boolean parallelSort) {
        this.dataSet = dataSet;
        this.k = k;
        numThreads = factor * (Runtime.getRuntime().availableProcessors());
        executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThreads);
        this.parallelSort = parallelSort;
    }

    /**
     *  用于关闭执行器
     */
    public void destroy() {
        executor.shutdown();
    }
    /**
     *  接收一个范例作为参数并且返回一个字符串
     *	@param example
     *	@return
     * @throws InterruptedException 
     */
    public String classify(Sample example) throws InterruptedException {
        Distance[] distances = new Distance[dataSet.size()];
        CountDownLatch endControler = new CountDownLatch(dataSet.size());
        int index = 0;
        for (Sample localExample:dataSet) {
            // 为每个需要计算的距离创建一个任务，并且将其发送给执行器。
            IndividualDistanceTask task = new IndividualDistanceTask(distances, index, localExample, example, endControler);
            executor.execute(task);
            index ++;
        }
        endControler.await();
        
        // 根据 parallelSort 属性的值，调用排序
        if (parallelSort) {
            Arrays.parallelSort(distances);
        } else {
            Arrays.sort(distances);
        }

        // 在 k 个最邻近的范例中统计实例最多的标签
        Map<String, Integer> results = new HashMap<String, Integer>();
        for (int i = 0; i < k; i++) {
            Sample localExample = dataSet.get(distances[i].getIndex());
            String tag = localExample.getTag();
            results.merge(tag, 1, (a,b) -> a+b);
        }
        return Collections.max(results.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}

class IndividualDistanceTask implements Runnable {

    private final Distance[] distances;

    private final int index;

    private final Sample localExample;

    private final Sample example;

    private final CountDownLatch endControler;

    public IndividualDistanceTask(Distance[] distances, int index, Sample localExample, Sample example,
            CountDownLatch endControler) {
        this.distances = distances;
        this.index = index;
        this.localExample = localExample;
        this.example = example;
        this.endControler = endControler;
    }

    @Override
    public void run() {
        distances[index] = new Distance();
        distances[index].setIndex(index);
        distances[index].setDistance(EuclideanDistanceCalculator.calculate(localExample, example));
        endControler.countDown();
    }

}
