package com.app.jueee.concurrency.chapter06;

import java.nio.file.Paths;
import java.util.Date;

import com.app.jueee.concurrency.chapter06.common.DataLoader;
import com.app.jueee.concurrency.chapter06.common.Individual;

public class J3GeneticAlgorithmConcurrentMain {

    
    public static void main(String[] args) {
        Date start, end;
        // 代的数目
        int generations = 10000;
        // 种群中的个体数
        int individuals = 1000;
        
        try {
            for(String name:new String[]{"lau15_dist","kn57_dist"}) {
                // 加载距离矩阵
                int[][] distanceMatrix = DataLoader.load(Paths.get("data//chapter06", name+".txt"));
                ConcurrentGeneticAlgorithm concurrentGeneticAlgorithm = new ConcurrentGeneticAlgorithm(distanceMatrix, generations, individuals);
                start = new Date();
                Individual result = concurrentGeneticAlgorithm.calculate();
                end = new Date();
                System.out.println ("=======================================");
                System.out.println("Example:"+name);
                System.out.println("Generations: " + generations);
                System.out.println("Population: " + individuals);
                System.out.println("Execution Time: " + (end.getTime() - start.getTime()));
                System.out.println("Best Individual: " + result);
                System.out.println("Total Distance: " + result.getValue());
                System.out.println ("=======================================");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
