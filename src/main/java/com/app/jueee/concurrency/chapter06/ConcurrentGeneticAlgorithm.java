package com.app.jueee.concurrency.chapter06;

import com.app.jueee.concurrency.chapter06.common.GeneticOperators;
import com.app.jueee.concurrency.chapter06.common.Individual;

public class ConcurrentGeneticAlgorithm {
    
    // 每个个体的染色体数目
    private int numberOfGenerations;
    // 每一代的个体数目
    private int numberOfIndividuals;
    // 距离矩阵
    private int[][] distanceMatrix;
    // 代的数目
    private int size;

    public ConcurrentGeneticAlgorithm(int[][] distanceMatrix, int numberOfGenerations, int numberOfIndividuals) {
        this.distanceMatrix = distanceMatrix;
        this.numberOfGenerations = numberOfGenerations;
        this.numberOfIndividuals = numberOfIndividuals;
        size = distanceMatrix.length;
    }
    
    // 执行遗传算法并且返回最优个体
    public Individual calculate() {
        Individual[] population = GeneticOperators.initialize(numberOfIndividuals, size);
        GeneticOperators.evaluate(population, distanceMatrix);
        
        SharedData data = new SharedData();
        data.setPopulation(population);
        data.setDistanceMatrix(distanceMatrix);
        data.setBest(population[0]);
        
        int numTasks = Runtime.getRuntime().availableProcessors();
        GeneticPhaser phaser = new GeneticPhaser(numTasks, data);

        ConcurrentGeneticTask[] tasks = new ConcurrentGeneticTask[numTasks];
        Thread[] threads = new Thread[numTasks];
        tasks[0] = new ConcurrentGeneticTask(phaser, numberOfGenerations, true);
        for (int i = 1; i < numTasks; i++) {
            tasks[i] = new ConcurrentGeneticTask(phaser, numberOfGenerations, false);
        }
        for (int i = 0; i < numTasks; i++) {
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }
        for (int i = 0; i < numTasks; i++) {
            try {
                threads[i].join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data.getBest();
    }
}
