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
 *  k-最近邻算法：粗粒度并发版本
 *	
 *	@author hzweiyongqiang
 */
public class J2ConcurrentVersionKNearestNeighborsByCoarseGrained {

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
    public J2ConcurrentVersionKNearestNeighborsByCoarseGrained(List<? extends Sample> dataSet, int k, int factor, boolean parallelSort) {
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
     *  @param example
     *  @return
     * @throws InterruptedException 
     */
    public String classify(Sample example) throws Exception {
        Distance[] distances = new Distance[dataSet.size()];
        CountDownLatch endController = new CountDownLatch(numThreads);
        
        int length = dataSet.size() / numThreads;
        int startIndex = 0, endIndex = length;
        
        for (int i = 0; i <numThreads; i++) {
            GroupDistanceTask task = new GroupDistanceTask(distances, startIndex, endIndex, dataSet, example, endController);
            startIndex = endIndex;
            if (i <numThreads - 2) {
                endIndex = endIndex + length;
            } else {
                endIndex = dataSet.size();
            }
            executor.execute(task);
        }
        endController.await();

        // 根据 parallelSort 属性的值，调用排序
        if (parallelSort) {
            Arrays.parallelSort(distances);
        } else {
            Arrays.sort(distances);
        }

        // 在 k 个最邻近的范例中统计实例最多的标签
        Map<String, Integer> results = new HashMap<String, Integer>();
        for (int i = 0; i < k; i++) {
            Sample localSample = dataSet.get(distances[i].getIndex());
            String tag = localSample.getTag();
            results.merge(tag, 1, (a,b) -> a+b);
        }
        return Collections.max(results.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
    
    public static void main(String[] args) {
        
    }
}

/**
 *  处理的是训练数据集的一个子集
 *  因此它存放的是整个训练数据集及其要处理的这部分数据集的起始位置和终止位置。
 *	
 *	@author hzweiyongqiang
 */
class GroupDistanceTask implements Runnable {
    private final Distance[] distances;
    private final int startIndex,endIndex;
    private final Sample example;
    private final List<? extends Sample>dataSet;

    private final CountDownLatch endController;
    
    public GroupDistanceTask(Distance[] distances, int startIndex, int endIndex, List<? extends Sample> dataSet, Sample example, CountDownLatch endController) {
        this.distances = distances;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.example = example;
        this.dataSet = dataSet;
        this.endController = endController;
    }

    @Override
    public void run() {
        for (int index = startIndex; index <endIndex; index++) {
            Sample localSample=dataSet.get(index);
            distances[index] = new Distance();
            distances[index].setIndex(index);
            distances[index].setDistance(EuclideanDistanceCalculator.calculate(localSample, example));
        }
        endController.countDown();
    }
}
