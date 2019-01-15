package com.app.jueee.concurrency.jmh;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 *  Fixtures 有不同的级别来控制它们即将运行的时间。
 *  Level.Invocation有时用于执行一些每次调用工作不应该算作有效载荷。
 *  
 *  
 *	
 *	@author hzweiyongqiang
 */
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class JMH_07_FixtureLevelInvocation {


    /**
     *  此状态处理执行程序。
     *	我们使用Level.Trial创建和关闭执行程序，所以它在所有迭代中保持不变。
     *	@author hzweiyongqiang
     */
    @State(Scope.Benchmark)
    public static class NormalState {
        ExecutorService service;

        @Setup(Level.Trial)
        public void up() {
            service = Executors.newCachedThreadPool();
        }

        @TearDown(Level.Trial)
        public void down() {
            service.shutdown();
        }

    }
    
    /**
     *  这是基本状态的*扩展*，也是有Level.Invocation Fixtures方法
     *	
     *	@author hzweiyongqiang
     */
    public static class LaggingState extends NormalState {
        public static final int SLEEP_TIME = Integer.getInteger("sleepTime", 10);

        @Setup(Level.Invocation)
        public void lag() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
        }
    }

    @State(Scope.Thread)
    public static class Scratch {
        private double p;
        public double doWork() {
            p = Math.log(p);
            return p;
        }
    }

    public static class Task implements Callable<Double> {
        private Scratch s;

        public Task(Scratch s) {
            this.s = s;
        }

        @Override
        public Double call() {
            return s.doWork();
        }
    }

    // 热模式，不会进行睡眠
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public double measureHot(NormalState e, final Scratch s) throws ExecutionException, InterruptedException {
        return e.service.submit(new Task(s)).get();
    }

    // 冷模式，因为LaggingState的lag方法为Level.Invocation，每次迭代都会执行
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public double measureCold(LaggingState e, final Scratch s) throws ExecutionException, InterruptedException {
        return e.service.submit(new Task(s)).get();
    }
    

    /**
     *  可以看到 measureCold 运行时间更长，因为我们负责了线程唤醒。
     *	@param args
     *	@throws RunnerException
     */
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_07_FixtureLevelInvocation.class.getName())
            .forks(1)
            .build();
        
        new Runner(options).run();
    }
    
    /**
# Run complete. Total time: 00:01:21

Benchmark                                  Mode  Cnt   Score   Error  Units
JMH_07_FixtureLevelInvocation.measureCold  avgt   20  70.666 ± 3.449  us/op
JMH_07_FixtureLevelInvocation.measureHot   avgt   20   2.371 ± 0.065  us/op
     * 
     */
}
