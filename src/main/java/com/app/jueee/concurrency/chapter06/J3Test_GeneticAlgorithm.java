package com.app.jueee.concurrency.chapter06;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.app.jueee.concurrency.chapter06.common.DataLoader;
import com.app.jueee.concurrency.chapter06.common.Individual;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class J3Test_GeneticAlgorithm {
    
    @Param({"lau15_dist","kn57_dist"})
    private String fileName;
    
    // 代的数目
    @Param({"10","100","1000"})
    private int generations;
    
    // 种群中的个体数
    @Param({"100","1000","10000"})
    private int individuals;
    // 存储所有的文档
    private int[][] distanceMatrix;
    
    @Setup
    public void prepare() {
        try {
            distanceMatrix = DataLoader.load(Paths.get("data//chapter06", fileName+".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        };
    }
    
    @Benchmark
    public void J3SerialIndexing(){
        SerialGeneticAlgorithm serialGeneticAlgorithm = new SerialGeneticAlgorithm(distanceMatrix, generations, individuals);
        Individual result = serialGeneticAlgorithm.calculate();
    }
    
    @Benchmark
    public void J3ConcurrentIndexing(){
        ConcurrentGeneticAlgorithm concurrentGeneticAlgorithm = new ConcurrentGeneticAlgorithm(distanceMatrix, generations, individuals);
        Individual result = concurrentGeneticAlgorithm.calculate();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(J3Test_GeneticAlgorithm.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(1)
                .build();

        new Runner(opt).run();
    }
}
