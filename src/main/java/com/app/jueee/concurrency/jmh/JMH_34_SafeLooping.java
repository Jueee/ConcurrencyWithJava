package com.app.jueee.concurrency.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_34_SafeLooping {

    static final int BASE = 42;

    static int work(int x) {
        return BASE + x;
    }

    @Param({"1", "10", "100", "1000"})
    int size;

    int[] xs;

    @Setup
    public void setup() {
        xs = new int[size];
        for (int c = 0; c < size; c++) {
            xs[c] = c;
        }
    }

    @Benchmark
    public int measureWrong_1() {
        int acc = 0;
        for (int x : xs) {
            acc = work(x);
        }
        return acc;
    }

    @Benchmark
    public int measureWrong_2() {
        int acc = 0;
        for (int x : xs) {
            acc += work(x);
        }
        return acc;
    }

    @Benchmark
    public void measureRight_1(Blackhole bh) {
        for (int x : xs) {
            bh.consume(work(x));
        }
    }

    @Benchmark
    public void measureRight_2() {
        for (int x : xs) {
            sink(work(x));
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public static void sink(int v) {
        // The method intentionally does nothing.
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_34_SafeLooping.class.getSimpleName())
                .forks(3)
                .build();

        new Runner(opt).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:08:16

Benchmark                          (size)  Mode  Cnt     Score    Error  Units
JMH_34_SafeLooping.measureRight_1       1  avgt   15     4.871 ±  1.877  ns/op
JMH_34_SafeLooping.measureRight_1      10  avgt   15    21.767 ±  0.319  ns/op
JMH_34_SafeLooping.measureRight_1     100  avgt   15   196.916 ±  0.931  ns/op
JMH_34_SafeLooping.measureRight_1    1000  avgt   15  1883.451 ±  7.883  ns/op
JMH_34_SafeLooping.measureRight_2       1  avgt   15     3.103 ±  0.030  ns/op
JMH_34_SafeLooping.measureRight_2      10  avgt   15    18.395 ±  0.793  ns/op
JMH_34_SafeLooping.measureRight_2     100  avgt   15   167.098 ±  1.382  ns/op
JMH_34_SafeLooping.measureRight_2    1000  avgt   15  1592.958 ± 22.228  ns/op
JMH_34_SafeLooping.measureWrong_1       1  avgt   15     3.031 ±  0.153  ns/op
JMH_34_SafeLooping.measureWrong_1      10  avgt   15     3.887 ±  0.009  ns/op
JMH_34_SafeLooping.measureWrong_1     100  avgt   15     5.834 ±  0.021  ns/op
JMH_34_SafeLooping.measureWrong_1    1000  avgt   15    30.406 ±  0.479  ns/op
JMH_34_SafeLooping.measureWrong_2       1  avgt   15     3.103 ±  0.007  ns/op
JMH_34_SafeLooping.measureWrong_2      10  avgt   15     5.153 ±  0.008  ns/op
JMH_34_SafeLooping.measureWrong_2     100  avgt   15    25.975 ±  0.118  ns/op
JMH_34_SafeLooping.measureWrong_2    1000  avgt   15   290.488 ±  0.463  ns/op
     * 
     */
}
