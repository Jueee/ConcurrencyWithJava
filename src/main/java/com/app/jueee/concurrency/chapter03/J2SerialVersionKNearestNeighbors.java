package com.app.jueee.concurrency.chapter03;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.jueee.concurrency.chapter03.data.Distance;
import com.app.jueee.concurrency.chapter03.data.Sample;

/**
 * k-最近邻算法：串行版本
 * 
 * @author hzweiyongqiang
 */
public class J2SerialVersionKNearestNeighbors {
    
    // 训练数据集
    private final List<? extends Sample> dataSet;
    // 数值 k（用于确定某个实例标签的范例数量）
    private int k;

    public J2SerialVersionKNearestNeighbors(List<? extends Sample> dataSet, int k) {
        this.dataSet = dataSet;
        this.k = k;
    }
    
    /**
     *  接收一个范例作为参数并且返回一个字符串
     *	@param example
     *	@return
     */
    public String classify(Sample example) {
        // 计算范例和训练集所有范例之间的距离
        Distance[] distances = new Distance[dataSet.size()];
        int index = 0;
        for (Sample localExample : dataSet) {
            distances[index] = new Distance();
            distances[index].setIndex(index);
            distances[index].setDistance(EuclideanDistanceCalculator.calculate(localExample, example));
            index ++;
        }
        
        // 使用 Arrays.sort() 方法按照距离从低到高的顺序排列范例
        Arrays.sort(distances);
        
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

/**
 *  计算两个范例之间的距离
 *	
 *	@author hzweiyongqiang
 */
class EuclideanDistanceCalculator {
    
    public static double calculate (Sample example1, Sample example2) {
        double ret=0.0d;
        
        double[] data1=example1.getExample();
        double[] data2=example2.getExample();
        
        if (data1.length!=data2.length) {
            throw new IllegalArgumentException ("Vector doesn't have the same length");
        }
        
        for (int i=0; i<data1.length; i++) {
            ret+=Math.pow(data1[i]-data2[i], 2);
        }
        return Math.sqrt(ret);

    }

}