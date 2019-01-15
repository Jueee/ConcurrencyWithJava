package com.app.jueee.concurrency.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 *  使用Blackhole消耗CPU
 *	
 *	@author hzweiyongqiang
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_21_ConsumeCPU {

    @Benchmark
    public void consume_0000() {
        Blackhole.consumeCPU(0);
    }

    @Benchmark
    public void consume_0001() {
        Blackhole.consumeCPU(1);
    }

    @Benchmark
    public void consume_0002() {
        Blackhole.consumeCPU(2);
    }

    @Benchmark
    public void consume_0004() {
        Blackhole.consumeCPU(4);
    }

    @Benchmark
    public void consume_0008() {
        Blackhole.consumeCPU(8);
    }

    @Benchmark
    public void consume_0016() {
        Blackhole.consumeCPU(16);
    }

    @Benchmark
    public void consume_0032() {
        Blackhole.consumeCPU(32);
    }

    @Benchmark
    public void consume_0064() {
        Blackhole.consumeCPU(64);
    }

    @Benchmark
    public void consume_0128() {
        Blackhole.consumeCPU(128);
    }

    @Benchmark
    public void consume_0256() {
        Blackhole.consumeCPU(256);
    }

    @Benchmark
    public void consume_0512() {
        Blackhole.consumeCPU(512);
    }

    @Benchmark
    public void consume_1024() {
        Blackhole.consumeCPU(1024);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_21_ConsumeCPU.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:08:05

Benchmark                       Mode  Cnt     Score    Error  Units
JMH_21_ConsumeCPU.consume_0000  avgt   20     1.811 ±  0.002  ns/op
JMH_21_ConsumeCPU.consume_0001  avgt   20     2.608 ±  0.024  ns/op
JMH_21_ConsumeCPU.consume_0002  avgt   20     3.369 ±  0.026  ns/op
JMH_21_ConsumeCPU.consume_0004  avgt   20     4.669 ±  0.041  ns/op
JMH_21_ConsumeCPU.consume_0008  avgt   20     8.632 ±  0.027  ns/op
JMH_21_ConsumeCPU.consume_0016  avgt   20    19.872 ±  0.048  ns/op
JMH_21_ConsumeCPU.consume_0032  avgt   20    46.656 ±  0.889  ns/op
JMH_21_ConsumeCPU.consume_0064  avgt   20   104.777 ±  0.683  ns/op
JMH_21_ConsumeCPU.consume_0128  avgt   20   221.775 ±  0.217  ns/op
JMH_21_ConsumeCPU.consume_0256  avgt   20   453.531 ±  0.360  ns/op
JMH_21_ConsumeCPU.consume_0512  avgt   20   917.897 ±  2.608  ns/op
JMH_21_ConsumeCPU.consume_1024  avgt   20  1854.734 ± 15.592  ns/op
     * 
     */
}
