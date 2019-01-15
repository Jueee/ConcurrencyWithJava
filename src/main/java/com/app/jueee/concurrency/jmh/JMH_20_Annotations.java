package com.app.jueee.concurrency.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * 通过@Warmup和@Measurement配置测试方法运行环境
 * 
 * @author hzweiyongqiang
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
public class JMH_20_Annotations {

    double x1 = Math.PI;

    /**
          *   最接近范围内的注释优先：即基于方法的注释会覆盖基于类的注释
     * @return
     */
    @Benchmark
    @Warmup(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 100, timeUnit = TimeUnit.MILLISECONDS)
    public double measure() {
        return Math.log(x1);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(JMH_20_Annotations.class.getSimpleName()).build();

        new Runner(opt).run();
    }

    /**
     * 
     * # Run complete. Total time: 00:00:01
     * 
     * Benchmark Mode Cnt Score Error Units JMH_20_Annotations.measure thrpt 5 108.634 ± 1.260 ops/us
     * 
     */
}
