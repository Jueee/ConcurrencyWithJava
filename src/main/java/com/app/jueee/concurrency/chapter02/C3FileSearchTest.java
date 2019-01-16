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
    
    @Param({"C:", "D:"})
    private String fileSearch;
    
    @Param({"hosts"})
    private String fileName;
    
    private File fileSerial;
    private File fileParallel;
    
    private Result resultSerial;
    private Result resultParallel;
    
    @Setup
    public void prepare() {
        fileSerial = new File(fileSearch);
        fileParallel = new File(fileSearch);

        resultSerial  = new Result();
        resultParallel  = new Result();
    }
    
    @Benchmark
    public void serialVersion() {
        C3SerialVersionFileSearch.searchFiles(fileSerial, fileName, resultSerial);
    }
    
    @Benchmark
    public void parallelVersion() {
        C3ParallelVersionFileSearch.searchFiles(fileParallel, fileName, resultParallel);
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
