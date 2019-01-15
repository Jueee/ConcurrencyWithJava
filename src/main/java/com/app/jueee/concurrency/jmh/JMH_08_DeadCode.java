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
 *  许多基准测试的失败是死码删除（DCE）：
 *  编译器非常聪明，可以推断出一些冗余的计算并完全消除它们。 
 *  
 *  如果被淘汰的部分是我们的基准代码，我们就遇到了麻烦。
 *	
 *	@author hzweiyongqiang
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_08_DeadCode {

    private double x = Math.PI;

    @Benchmark
    public void baseline() {
        // 什么都不做，这是基线
    }

    @Benchmark
    public void measureWrong() {
        // 这是错误的：没有使用结果，整个计算被优化掉了。
        Math.log(x);
    }

    @Benchmark
    public double measureRight() {
        // 这是正确的：结果正在使用中。
        return Math.log(x);
    }
    
    /**
     *  可以使用measureWrong() 查看不切实际的快速计算，使用measureRight() 进行实际测量。
     *	@param args
     *	@throws RunnerException
     */
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_08_DeadCode.class.getName())
            .forks(1)
            .build();
        
        new Runner(options).run();
    }
    /**
     * 
# Run complete. Total time: 00:02:02

Benchmark                     Mode  Cnt  Score    Error  Units
JMH_08_DeadCode.baseline      avgt   20  0.517 ±  0.001  ns/op
JMH_08_DeadCode.measureRight  avgt   20  9.378 ±  0.231  ns/op
JMH_08_DeadCode.measureWrong  avgt   20  7.005 ±  0.015  ns/op
     * 
     */
}
