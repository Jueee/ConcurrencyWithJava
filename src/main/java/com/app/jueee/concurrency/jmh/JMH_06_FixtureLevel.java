package com.app.jueee.concurrency.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


/**
 *  Fixtures 是测试中非常重要的一部分。
 *  他们的主要目的是建立一个固定/已知的环境状态以确保 测试可重复并且按照预期方式运行。
 * 
 *  用户至少有三个级别。从上到下：
 *  Level.Trial：在整个基准测试运行之前或之后（迭代序列）
 *  Level.Iteration：基准迭代之前或之后（调用序列）
 *  Level.Invocation; 在基准方法调用之前或之后。
 *
 *  在 Fixtures 方法上花费的时间不计入性能
 *	
 *	@author hzweiyongqiang
 */
@State(Scope.Thread)
public class JMH_06_FixtureLevel {

    double x;
    
    @TearDown(Level.Iteration)
    public void check() {
        assert x > Math.PI : "Nothing changed?";
    }
    
    @Benchmark
    public void measureRight() {
        x++;
    }

    @Benchmark
    public void measureWrong() {
        double x = 0;
        x++;
    }
    
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_06_FixtureLevel.class.getName())
            .forks(1)
            .jvmArgs("-ea")
            .shouldFailOnError(false)
            .build();
        
        new Runner(options).run();
    }
    
    /**
# Run complete. Total time: 00:01:21

Benchmark                           Mode  Cnt           Score         Error  Units
JMH_05_StateFixtures.measureRight  thrpt   20   427796738.400 ±  816341.375  ops/s
JMH_05_StateFixtures.measureWrong  thrpt   19  1929305164.234 ± 6461929.594  ops/s
     * 
     */
}
