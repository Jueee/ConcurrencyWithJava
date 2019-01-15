package com.app.jueee.concurrency.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class JMH_11_Loops {

    int x = 1;
    int y = 2;
    
    @Benchmark
    public int measureRight() {
        return (x + y);
    }
    
    private int reps(int reps) {
        int s = 0;
        for (int i = 0; i < reps; i++) {
            s += (x + y);
        }
        return s;
    }
    
    @Benchmark
    @OperationsPerInvocation(1)
    public int measureWrong_1() {
        return reps(1);
    }

    @Benchmark
    @OperationsPerInvocation(10)
    public int measureWrong_10() {
        return reps(10);
    }

    @Benchmark
    @OperationsPerInvocation(100)
    public int measureWrong_100() {
        return reps(100);
    }

    @Benchmark
    @OperationsPerInvocation(1000)
    public int measureWrong_1000() {
        return reps(1000);
    }

    @Benchmark
    @OperationsPerInvocation(10000)
    public int measureWrong_10000() {
        return reps(10000);
    }

    @Benchmark
    @OperationsPerInvocation(100000)
    public int measureWrong_100000() {
        return reps(100000);
    }
    
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_11_Loops.class.getName())
            .forks(1)
            .build();
        
        new Runner(options).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:04:43

Benchmark                         Mode  Cnt  Score    Error  Units
JMH_11_Loops.measureRight         avgt   20  2.165 ±  0.004  ns/op
JMH_11_Loops.measureWrong_1       avgt   20  2.169 ±  0.011  ns/op
JMH_11_Loops.measureWrong_10      avgt   20  0.236 ±  0.004  ns/op
JMH_11_Loops.measureWrong_100     avgt   20  0.026 ±  0.001  ns/op
JMH_11_Loops.measureWrong_1000    avgt   20  0.022 ±  0.001  ns/op
JMH_11_Loops.measureWrong_10000   avgt   20  0.018 ±  0.001  ns/op
JMH_11_Loops.measureWrong_100000  avgt   20  0.017 ±  0.001  ns/op
     * 
     */
}
