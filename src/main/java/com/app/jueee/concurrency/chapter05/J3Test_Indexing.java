package com.app.jueee.concurrency.chapter05;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class J3Test_Indexing {
    
    private File source = new File("data//chapter05//data");
    // 存储所有的文档
    private File[] files = source.listFiles();
    
    @Benchmark
    public void J3SerialIndexing(){
        J3SerialIndexing.test(files);
    }
    
    @Benchmark
    public void J3ConcurrentIndexing(){
        J3ConcurrentIndexing.test(files);
    }
    
    @Benchmark
    public void J3MultipleConcurrentIndexing(){
        J3MultipleConcurrentIndexing.test(files);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(J3Test_Indexing.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
}
