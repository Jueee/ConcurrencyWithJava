package com.app.jueee.concurrency.chapter07.example1;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
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

import com.app.jueee.concurrency.chapter07.common1.Document;
import com.app.jueee.concurrency.chapter07.common1.DocumentLoader;
import com.app.jueee.concurrency.chapter07.common1.VocabularyLoader;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class J2Test_KMeans {
    
    // 要生成的簇数
    @Param({"5","10","15","20"})
    private int K;
    
    // 随机数生成器的“种子”。该“种子”确定了初始质心的位置。
    @Param({"1","13"})
    private int SEED;
    
    // 一个任务在不分割的前提下所能处理的最大项
    @Param({"1","20","400"})
    private int MAX_SIZE;
    
    // 存储所有的文档
    private Map<String, Integer> vocIndex;
    private Document[] documents;
    
    @Setup
    public void prepare() {
        try {
            // 从文件加载数据--单词
            Path pathVoc = Paths.get("data//chapter07", "movies.words");
            vocIndex = VocabularyLoader.load(pathVoc);

            // 从文件加载数据--文档
            Path pathDocs = Paths.get("data//chapter07", "movies.data");
            documents = DocumentLoader.load(pathDocs, vocIndex);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }
    
    @Benchmark
    public void J2KMeansSerialMain(){
        J2KMeansSerialMain.calculate(documents, K, vocIndex.size(), SEED);
    }
    
    @Benchmark
    public void J2KMeansConcurrentMain(){
        J2KMeansConcurrentMain.calculate(documents, K, vocIndex.size(), SEED, MAX_SIZE);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(J2Test_KMeans.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(1)
                .build();

        new Runner(opt).run();
    }
}
