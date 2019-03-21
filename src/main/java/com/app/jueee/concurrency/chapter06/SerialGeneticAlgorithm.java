package com.app.jueee.concurrency.chapter06;

import com.app.jueee.concurrency.chapter06.common.GeneticOperators;
import com.app.jueee.concurrency.chapter06.common.Individual;

public class SerialGeneticAlgorithm {
    
    // 含有所有城市之间距离的距离矩阵
    private int[][] distanceMatrix;
    // 代的数目
    private int numberOfGenerations;
    // 种群中的个体数
    private int numberOfIndividuals;
    // 每个个体中的染色体数目
    private int size;

    public SerialGeneticAlgorithm(int[][] distanceMatrix, int numberOfGenerations, int numberOfIndividuals) {
        this.distanceMatrix = distanceMatrix;
        this.numberOfGenerations = numberOfGenerations;
        this.numberOfIndividuals = numberOfIndividuals;
        size = distanceMatrix.length;
    }
    
    public Individual calculate() {
        Individual[] population = GeneticOperators.initialize(numberOfIndividuals, size);
        GeneticOperators.evaluate(population, distanceMatrix);
        Individual best = population[0];
        
        for (int i = 1; i <= numberOfGenerations; i++) {
            // 获取选定的个体
            Individual[] selected = GeneticOperators.selection(population);
            // 计算下一代、评估新一代，而且如果新一代的最优解优于到目前为止最好的个体，那么替换该个体。
            population = GeneticOperators.crossover(selected, numberOfIndividuals, size);
            GeneticOperators.evaluate(population, distanceMatrix);
            if (population[0].getValue() < best.getValue()) {
                best = population[0];
            }
        }
        
        return best;
    }
}
