package com.app.jueee.concurrency.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
public class JMH_05_StateFixtures {
    
    double x;
    
    // @Setup 初始化操作，默认在所有测试方法执行之前
    @Setup
    public void prepare() {
        x = Math.PI;
    }
    
    // @TearDown 销毁操作，默认在所有测试方法执行之后
    @TearDown
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
            .include(JMH_05_StateFixtures.class.getName())
            .forks(1)
            .jvmArgs("-ea")
            .build();
        
        new Runner(options).run();
    }
    
    /**
# Run complete. Total time: 00:01:21

Benchmark                           Mode  Cnt           Score         Error  Units
JMH_05_StateFixtures.measureRight  thrpt   20   428755352.974 ± 1803423.853  ops/s
JMH_05_StateFixtures.measureWrong  thrpt   19  1932288933.937 ± 3089937.678  ops/s
     */
}
