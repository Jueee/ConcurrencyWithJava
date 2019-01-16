package com.app.jueee.concurrency.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.WarmupMode;


/**
 *  使用WarmupMode，指定预热操作模式：
 * 
 *  1.  WarmupMode.INDI：在每个测试方法运行前作预热；
 *  2.  WarmupMode.BULK：在所有测试方法运行前作统一的预热；
 *  3.  WarmupMode.BULK_INDI：在所有测试方法运行前作统一的预热，且在每个测试方法运行前也作预热。
 *	
 *	@author hzweiyongqiang
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_32_BulkWarmup {

    public interface Counter {
        int inc();
    }

    public class Counter1 implements Counter {
        private int x;

        @Override
        public int inc() {
            return x++;
        }
    }

    public class Counter2 implements Counter {
        private int x;

        @Override
        public int inc() {
            return x++;
        }
    }

    Counter c1 = new Counter1();
    Counter c2 = new Counter2();

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int measure(Counter c) {
        int s = 0;
        for (int i = 0; i < 10; i++) {
            s += c.inc();
        }
        return s;
    }

    @Benchmark
    public int measure_c1() {
        return measure(c1);
    }

    @Benchmark
    public int measure_c2() {
        return measure(c2);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_32_BulkWarmup.class.getSimpleName())
                // .includeWarmup(...) <-- this may include other benchmarks into warmup
                .warmupMode(WarmupMode.BULK) // see other WarmupMode.* as well
                .forks(1)
                .build();

        new Runner(opt).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:05:00

Benchmark                     Mode  Cnt  Score   Error  Units
JMH_32_BulkWarmup.measure_c1  avgt    5  5.479 ± 0.068  ns/op
JMH_32_BulkWarmup.measure_c2  avgt    5  6.106 ± 0.046  ns/op
     * 
     */
}
