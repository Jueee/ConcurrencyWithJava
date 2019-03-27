package com.app.jueee.concurrency.chapter07.common;

/**
 *  用于计算两个向量之间的欧氏距离
 *	
 *	@author hzweiyongqiang
 */
public class DistanceMeasurer {

    public static double euclideanDistance(Word[] words, double[] centroid) {
        double distance = 0;
        int wordIndex = 0;
        for (int i = 0; i < centroid.length; i++) {
            if ((wordIndex < words.length) && (words[wordIndex].getIndex()==i)) {
                distance += Math.pow((words[wordIndex].getTfidf() - centroid[i]), 2);
                wordIndex ++;
            } else {
                distance += centroid[i] * centroid[i];
            }
        }
        return Math.sqrt(distance);
    }
}
