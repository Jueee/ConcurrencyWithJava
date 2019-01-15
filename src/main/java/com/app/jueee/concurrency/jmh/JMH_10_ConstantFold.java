package com.app.jueee.concurrency.jmh;

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


/**
 *  JVM自动优化可预测的计算结果
 *	
 *	@author hzweiyongqiang
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class JMH_10_ConstantFold {
    
    // 普通变量，值可能发生变化，不可预知
    private double x = Math.PI;

    // final变量，值不会发生变化，即可预知
    private final double wrongX = Math.PI;

    @Benchmark
    public double baseline() {
        return Math.PI;
    }

    @Benchmark
    public double measureWrong_1() {
        // Math.PI是常量，值可预知，计算会被合并
        return Math.log(Math.PI);
    }

    @Benchmark
    public double measureWrong_2() {
        // wrongX是常量，值可预知，计算会被合并
        return Math.log(wrongX);
    }

    @Benchmark
    public double measureRight() {
        // x是普通变量，值不可预知，计算不会被合并
        return Math.log(x);
    }
    
    /**
     *  使用measureWrong _ *()查看不切实际的快速计算，使用measureRight()进行实际测量。
     *	@param args
     *	@throws RunnerException
     */
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_10_ConstantFold.class.getName())
            .forks(1)
            .build();
        
        new Runner(options).run();
    }

    /**
     * 
# Run complete. Total time: 00:02:41

Benchmark                           Mode  Cnt  Score   Error  Units
JMH_10_ConstantFold.baseline        avgt   20  2.078 ± 0.015  ns/op
JMH_10_ConstantFold.measureRight    avgt   20  9.336 ± 0.246  ns/op
JMH_10_ConstantFold.measureWrong_1  avgt   20  8.657 ± 0.027  ns/op
JMH_10_ConstantFold.measureWrong_2  avgt   20  8.656 ± 0.022  ns/op
     *
     */
}
