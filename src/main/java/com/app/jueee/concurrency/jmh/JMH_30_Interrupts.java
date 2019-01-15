package com.app.jueee.concurrency.jmh;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;


/**
 *  在测试中，JMH会检测卡住的线程，并且强制终端该测试线程
 *	
 *	@author hzweiyongqiang
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Group)
public class JMH_30_Interrupts {

    private BlockingQueue<Integer> q;

    @Setup
    public void setup() {
        q = new ArrayBlockingQueue<>(1);
    }

    @Group("Q")
    @Benchmark
    public Integer take() throws InterruptedException {
        return q.take();
    }

    @Group("Q")
    @Benchmark
    public void put() throws InterruptedException {
        q.put(42);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_30_Interrupts.class.getSimpleName())
                .threads(2)
                .forks(5)
                .timeout(TimeValue.seconds(10))
                .build();

        new Runner(opt).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:08:23

Benchmark                 Mode  Cnt     Score     Error  Units
JMH_30_Interrupts.Q       avgt   25  8486.411 ± 113.240  ns/op
JMH_30_Interrupts.Q:put   avgt   25  8486.410 ± 113.242  ns/op
JMH_30_Interrupts.Q:take  avgt   25  8486.412 ± 113.238  ns/op
     * 
     */
}
