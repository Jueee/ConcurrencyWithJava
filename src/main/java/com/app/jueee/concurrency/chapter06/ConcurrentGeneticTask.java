package com.app.jueee.concurrency.chapter06;

import java.util.Random;

import com.app.jueee.concurrency.chapter06.common.GeneticOperators;
import com.app.jueee.concurrency.chapter06.common.Individual;

/**
 * 实现了那些将用于执行遗传算法各个阶段的任务。
 * 
 * @author hzweiyongqiang
 */
public class ConcurrentGeneticTask implements Runnable {

    // 在每个阶段结束时进行任务同步
    private GeneticPhaser phaser;
    // 访问共享数据
    private SharedData data;
    // 必须计算的代的数目
    private int numberOfGenerations;
    // 是否为主任务的布尔标志
    private boolean main;

    public ConcurrentGeneticTask(GeneticPhaser phaser, int numberOfGenerations, boolean main) {
        this.phaser = phaser;
        this.numberOfGenerations = numberOfGenerations;
        this.main = main;
        this.data = phaser.getData();
    }

    @Override
    public void run() {
        Random random = new Random(System.nanoTime());
        
        for (int i = 0; i < numberOfGenerations; i++) {
            
            // 选择阶段
            // 只有主任务会执行选择阶段。
            if (main) {
                data.setSelected(GeneticOperators.selection(data.getPopulation()));
            }
            // 其他任务将使用 arriveAndAwaitAdvance() 方法等待该阶段结束
            phaser.arriveAndAwaitAdvance();

            // 交叉阶段
            int individualIndex;
            do {
                individualIndex = data.getIndex().getAndAdd(2);
                if (individualIndex < data.getPopulation().length) {
                    int secondIndividual = individualIndex++ ;
                    
                    int p1Index = random.nextInt(data.getSelected().length);
                    int p2Index;
                    do {
                        p2Index = random.nextInt(data.getSelected().length);
                    } while (p1Index == p2Index);
                    
                    Individual parent1 = data.getSelected()[p1Index];
                    Individual parent2 = data.getSelected()[p2Index];
                    Individual individual1 = data.getPopulation()[individualIndex];
                    Individual individual2 = data.getPopulation()[secondIndividual];
                    GeneticOperators.crossover(parent1, parent2, individual1,individual2);
                }
            } while (individualIndex < data.getPopulation().length);
            phaser.arriveAndAwaitAdvance();
            
            // 评估阶段
            do {
                individualIndex = data.getIndex().getAndIncrement();
                if (individualIndex < data.getPopulation().length) {
                    GeneticOperators.evaluate(data.getPopulation()[individualIndex], data.getDistanceMatrix());
                }
            } while (individualIndex < data.getPopulation().length);

        }
        // 当所有的代都计算完毕后，各任务使用 arriveAndDeregister() 方法表示执行完毕，这样分段器就进入终止状态。
        phaser.arriveAndDeregister();
        
    }
}
