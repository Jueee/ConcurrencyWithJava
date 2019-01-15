package com.app.jueee.concurrency.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


/**
 *      使用@Fork在相同或不同进程中进行测试
 *	
 *	@author hzweiyongqiang
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class JMH_12_Forking {

    public interface Counter {
        int inc();
    }

    public class Counter1 implements Counter {
        private int x;

        @Override
        public int inc() {
            return x++;
        }
    }

    public class Counter2 implements Counter {
        private int x;

        @Override
        public int inc() {
            return x++;
        }
    }

    public int measure(Counter c) {
        int s = 0;
        for (int i = 0; i < 10; i++) {
            s += c.inc();
        }
        return s;
    }
    
    Counter c1 = new Counter1();
    Counter c2 = new Counter2();

    @Benchmark
    @Fork(0)
    public int measure_1_c1() {
        return measure(c1);
    }

    @Benchmark
    @Fork(0)
    public int measure_2_c2() {
        return measure(c2);
    }

    @Benchmark
    @Fork(0)
    public int measure_3_c1_again() {
        return measure(c1);
    }

    @Benchmark
    @Fork(1)
    public int measure_4_forked_c1() {
        return measure(c1);
    }

    @Benchmark
    @Fork(1)
    public int measure_5_forked_c2() {
        return measure(c2);
    }

    
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_12_Forking.class.getName())
            .build();
        
        new Runner(options).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:03:21

Benchmark                           Mode  Cnt  Score   Error  Units
JMH_12_Forking.measure_1_c1         avgt   20  1.992 ± 0.002  ns/op
JMH_12_Forking.measure_2_c2         avgt   20  4.068 ± 0.034  ns/op
JMH_12_Forking.measure_3_c1_again   avgt   20  4.116 ± 0.008  ns/op
JMH_12_Forking.measure_4_forked_c1  avgt   20  3.041 ± 0.015  ns/op
JMH_12_Forking.measure_5_forked_c2  avgt   20  3.548 ± 0.021  ns/op
     * 
     */
}
