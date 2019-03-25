package com.app.jueee.concurrency.chapter06;

import java.util.concurrent.atomic.AtomicInteger;

import com.app.jueee.concurrency.chapter06.common.Individual;

/**
 *  存放了所有将在任务之间共享的对象。
 *	
 *	@author hzweiyongqiang
 */
public class SharedData {

    // 种群数组，其中含有某一代的全部个体。
    private Individual[] population;
    // 精选数组，其中含有精选的个体。
    private Individual selected[];
    // 原子整型变量。这是唯一线程安全的对象，用于指明一个任务要生成或处理的个体的索引。
    private AtomicInteger index;
    // 所有各代中的最优个体，将作为算法的解返回。
    private Individual best;
    // 距离矩阵，其中含有城市之间的距离。
    private int[][] distanceMatrix;
    
    public SharedData() {
        index = new AtomicInteger();
    }
    
    public Individual[] getPopulation() {
        return population;
    }
    public void setPopulation(Individual[] population) {
        this.population = population;
    }
    public Individual[] getSelected() {
        return selected;
    }
    public void setSelected(Individual[] selected) {
        this.selected = selected;
    }
    public AtomicInteger getIndex() {
        return index;
    }
    public void setIndex(AtomicInteger index) {
        this.index = index;
    }
    public Individual getBest() {
        return best;
    }
    public void setBest(Individual best) {
        this.best = best;
    }
    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }
    public void setDistanceMatrix(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }
    
}
