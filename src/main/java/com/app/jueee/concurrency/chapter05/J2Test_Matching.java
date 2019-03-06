package com.app.jueee.concurrency.chapter05;

import java.util.List;
import java.util.concurrent.ExecutionException;
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

import com.app.jueee.concurrency.chapter05.common.WordsLoader;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class J2Test_Matching {

    @Param({"java"})
    private String word;
    
    private List<String> dictionary;
    
    @Setup
    public void prepare() {
        dictionary = WordsLoader.load("data/chapter05/UK Advanced Cryptics Dictionary.txt");
    }
    
    @Benchmark
    public void advancedConcurrentCalculation() throws InterruptedException, ExecutionException {
        J2BestMatchingAdvancedConcurrentCalculation.getBestMatchingWords(word, dictionary);
    }
    
    @Benchmark
    public void basicConcurrentCalculation() throws InterruptedException, ExecutionException {
        J2BestMatchingBasicConcurrentCalculation.getBestMatchingWords(word, dictionary);
    }
    
    @Benchmark
    public void serialCalculation() {
        J2BestMatchingSerialCalculation.getBestMatchingWords(word, dictionary);
    }
    
    @Benchmark
    public void existBasicConcurrent() throws Exception {
        J2ExistBasicConcurrentMain.existWord(word, dictionary);
    }
    
    @Benchmark
    public void existSerial() throws Exception {
        J2ExistSerialMain.existWord(word, dictionary);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(J2Test_Matching.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
}
