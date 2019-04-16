package com.app.jueee.concurrency.chapter07.example3;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.app.jueee.concurrency.chapter07.common3.AmazonMetaData;
import com.app.jueee.concurrency.chapter07.common3.AmazonMetaDataLoader;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class J3Test_MergeSort {
    
    private AmazonMetaData[] data;
    
    @Setup
    public void prepare() {
        Path path = Paths.get("data//chapter07","amazon-meta.txt");
        data = AmazonMetaDataLoader.load(path);
    }
    
    @Benchmark
    public void serialMain(){
        J3MergeSortSerialMain.mergeSort(data);
    }
    
    @Benchmark
    public void concurrentMain(){
        J3MergeSortConcurrentMain.mergeSort(data);
    }
    
    @Benchmark
    public void sort(){
        Arrays.sort(data);
    }
    
    @Benchmark
    public void parallelSort(){
        Arrays.parallelSort(data);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(J3Test_MergeSort.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(1)
                .build();

        new Runner(opt).run();
    }
}
