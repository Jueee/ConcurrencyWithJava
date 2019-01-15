package com.app.jueee.concurrency.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


/**
 *  Blackhole
 *  Blackhole会消费传进来的值，不提供任何信息来确定这些值是否在之后被实际使用。 
 *  
 *  Blackhole处理的事情主要有以下几种：
 *  -   死代码消除：入参应该在每次都被用到，因此编译器就不会把这些参数优化为常量或者在计算的过程中对他们进行其他优化。
 *  -   处理内存壁：我们需要尽可能减少写的量，因为它会干扰缓存，污染写缓冲区等。 这很可能导致过早地撞到内存壁
 *	
 *	@author hzweiyongqiang
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class JMH_09_Blackholes {

    double x1 = Math.PI;
    double x2 = Math.PI * 2;

    // 基线测量：单个Math.log 耗时是多少。
    @Benchmark
    public double baseline() {
        return Math.log(x1);
    }

    // Math.log（x2）计算完好无损，Math.log（x1）冗余并优化。
    @Benchmark
    public double measureWrong() {
        Math.log(x1);
        return Math.log(x2);
    }

    // 将多个结果合并为一个并返回。
    @Benchmark
    public double measureRight_1() {
        return Math.log(x1) + Math.log(x2);
    }

    // 使用 Blackhole 消除死代码优化
    @Benchmark
    public void measureRight_2(Blackhole bh) {
        bh.consume(Math.log(x1));
        bh.consume(Math.log(x2));
    }
    
    /**
     *  您将看到measureWrong()与baseline()相同。
     *  measureRight()都测量基线的两倍，因此日志完好无损。
     *	@param args
     *	@throws RunnerException
     */
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_09_Blackholes.class.getName())
            .forks(1)
            .build();
        
        new Runner(options).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:02:43

Benchmark                         Mode  Cnt   Score   Error  Units
JMH_09_Blackholes.baseline        avgt   20   9.197 ± 0.007  ns/op
JMH_09_Blackholes.measureRight_1  avgt   20  16.311 ± 0.026  ns/op
JMH_09_Blackholes.measureRight_2  avgt   20  17.910 ± 0.112  ns/op
JMH_09_Blackholes.measureWrong    avgt   20  15.782 ± 0.243  ns/op
     * 
     */
}
