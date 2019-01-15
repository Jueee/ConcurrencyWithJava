package com.app.jueee.concurrency.jmh;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Group)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_15_Asymmetric {

    private AtomicInteger counter;

    @Setup
    public void up() {
        counter = new AtomicInteger();
    }

    /**
     *  3个线程同步执行inc()
     *	@return
     */
    @Benchmark
    @Group("g")
    @GroupThreads(3)
    public int inc() {
        return counter.incrementAndGet();
    }

    /**
     *  1个线程执行get()
     *	@return
     */
    @Benchmark
    @Group("g")
    @GroupThreads(1)
    public int get() {
        return counter.get();
    }

    
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_15_Asymmetric.class.getName())
            .forks(1)
            .build();
        
        new Runner(options).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:00:40

Benchmark                Mode  Cnt   Score   Error  Units
JMH_15_Asymmetric.g      avgt   20  56.032 ± 0.766  ns/op
JMH_15_Asymmetric.g:get  avgt   20  31.071 ± 1.553  ns/op
JMH_15_Asymmetric.g:inc  avgt   20  64.353 ± 0.804  ns/op
     * 
     */
}
