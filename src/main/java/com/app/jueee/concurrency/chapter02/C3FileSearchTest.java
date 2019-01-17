package com.app.jueee.concurrency.chapter02;

import java.io.File;
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


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class C3FileSearchTest {
    
    @Param({"C:\\Windows\\", "C:\\Users\\"})
    private String fileSearch;
    
    @Param({"hosts"})
    private String fileName;
    
    private File file;
    
    @Setup
    public void prepare() {
        file = new File(fileSearch);
    }
    
    /**
     *  串行版本
     */
    @Benchmark
    public void serialVersion() {
        Result result = new Result();
        C3SerialVersionFileSearch.searchFiles(file, fileName, result);
    }
    
    /**
     *  并发版本
     */
    @Benchmark
    public void parallelVersion() {
        Result result = new Result();
        C3ParallelVersionFileSearch.searchFiles(file, fileName, result);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(C3FileSearchTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
}
