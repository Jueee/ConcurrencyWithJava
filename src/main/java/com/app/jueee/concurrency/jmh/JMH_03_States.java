package com.app.jueee.concurrency.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


/**
 *  State 用于声明某个类是一个“状态”，然后接受一个 Scope 参数用来表示该状态的共享范围。  
 *  因为很多 benchmark 会需要一些表示状态的类，JMH 允许你把这些类以依赖注入的方式注入到 benchmark 函数里。  
 *  
 *  Scope 主要分为两种：  
 *  -   Thread: 该状态为每个线程独享。
 *  -   Benchmark: 该状态在所有线程间共享。
 *	
 *	@author hzweiyongqiang
 */
public class JMH_03_States {
    
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        volatile double x = Math.PI;
    }

    @State(Scope.Thread)
    public static class ThreadState {
        volatile double x = Math.PI;
    }

    @Benchmark
    public void measureUnshared(ThreadState state) {
        state.x++;
    }

    @Benchmark
    public void measureShared(BenchmarkState state) {
        state.x++;
    }
    
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_03_States.class.getName())
            .threads(4)
            .forks(1)
            .build();
        
        new Runner(options).run();
    }
    
    /**
# Run complete. Total time: 00:01:21

Benchmark                       Mode  Cnt          Score         Error  Units
JMH_03_States.measureShared    thrpt   20   39831217.341 ±  365267.915  ops/s
JMH_03_States.measureUnshared  thrpt   20  603819942.207 ± 3556426.710  ops/s
     * 
     */
}
