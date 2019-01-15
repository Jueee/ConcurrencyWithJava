package com.app.jueee.concurrency.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 *  Mode 表示 JMH 进行 Benchmark 时所使用的模式。通常是测量的维度不同，或是测量的方式不同。  
 *  
 *  目前 JMH 共有四种模式：
 *  
 *  -   **Throughput**  
 *  整体吞吐量，例如“1秒内可以执行多少次调用”。
 *  
 *  -   **AverageTime**  
 *  调用的平均时间，例如“每次调用平均耗时xxx毫秒”。
 *  
 *  -   **SampleTime**  
 *  随机取样，最后输出取样结果的分布，例如“99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”
 *  
 *  -   **SingleShotTime**  
 *  以上模式都是默认一次 iteration 是 1s，唯有 SingleShotTime 是只运行一次。  
 *  往往同时把 warmup 次数设为0，用于测试冷启动时的性能。
 *	
 *	@author hzweiyongqiang
 */
public class JMH_02_BenchmarkModes {

    // Mode.Throughput，测量原始吞吐量
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureThroughput() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }
    
    // Mode.AverageTime测量平均执行时间
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureAvgTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }
    
    // Mode.SampleTime采样执行时间。
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureSamples() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }
    
    // Mode.SingleShotTime测量单个方法调用时间。
    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureSingleShot() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }
    
    // 同时要求多种基准模式。
    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureMultiple() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }
    
    // 同时要求所有基准模式。
    @Benchmark
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureAll() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }
    
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_02_BenchmarkModes.class.getName())
            .forks(1)
            .build();
        
        new Runner(options).run();
    }
    
    /**
Benchmark                                                        Mode  Cnt       Score     Error   Units
JMH_02_BenchmarkModes.measureAll                                thrpt   20      ≈ 10⁻⁵            ops/us
JMH_02_BenchmarkModes.measureMultiple                           thrpt   20      ≈ 10⁻⁵            ops/us
JMH_02_BenchmarkModes.measureThroughput                         thrpt   20       9.950 ±   0.013   ops/s
JMH_02_BenchmarkModes.measureAll                                 avgt   20  100557.158 ± 143.190   us/op
JMH_02_BenchmarkModes.measureAvgTime                             avgt   20  100356.214 ± 125.696   us/op
JMH_02_BenchmarkModes.measureMultiple                            avgt   20  100390.552 ± 165.055   us/op
JMH_02_BenchmarkModes.measureAll                               sample  200  100409.672 ± 104.833   us/op
JMH_02_BenchmarkModes.measureAll:measureAll·p0.00              sample        99352.576             us/op
JMH_02_BenchmarkModes.measureAll:measureAll·p0.50              sample       100401.152             us/op
JMH_02_BenchmarkModes.measureAll:measureAll·p0.90              sample       100794.368             us/op
JMH_02_BenchmarkModes.measureAll:measureAll·p0.95              sample       100925.440             us/op
JMH_02_BenchmarkModes.measureAll:measureAll·p0.99              sample       101056.512             us/op
JMH_02_BenchmarkModes.measureAll:measureAll·p0.999             sample       104464.384             us/op
JMH_02_BenchmarkModes.measureAll:measureAll·p0.9999            sample       104464.384             us/op
JMH_02_BenchmarkModes.measureAll:measureAll·p1.00              sample       104464.384             us/op
JMH_02_BenchmarkModes.measureMultiple                          sample  200  100374.282 ± 102.050   us/op
JMH_02_BenchmarkModes.measureMultiple:measureMultiple·p0.00    sample        99352.576             us/op
JMH_02_BenchmarkModes.measureMultiple:measureMultiple·p0.50    sample       100270.080             us/op
JMH_02_BenchmarkModes.measureMultiple:measureMultiple·p0.90    sample       100794.368             us/op
JMH_02_BenchmarkModes.measureMultiple:measureMultiple·p0.95    sample       100925.440             us/op
JMH_02_BenchmarkModes.measureMultiple:measureMultiple·p0.99    sample       101187.584             us/op
JMH_02_BenchmarkModes.measureMultiple:measureMultiple·p0.999   sample       103940.096             us/op
JMH_02_BenchmarkModes.measureMultiple:measureMultiple·p0.9999  sample       103940.096             us/op
JMH_02_BenchmarkModes.measureMultiple:measureMultiple·p1.00    sample       103940.096             us/op
JMH_02_BenchmarkModes.measureSamples                           sample  200  100344.136 ± 100.781   us/op
JMH_02_BenchmarkModes.measureSamples:measureSamples·p0.00      sample        99483.648             us/op
JMH_02_BenchmarkModes.measureSamples:measureSamples·p0.50      sample       100270.080             us/op
JMH_02_BenchmarkModes.measureSamples:measureSamples·p0.90      sample       100794.368             us/op
JMH_02_BenchmarkModes.measureSamples:measureSamples·p0.95      sample       100925.440             us/op
JMH_02_BenchmarkModes.measureSamples:measureSamples·p0.99      sample       101187.584             us/op
JMH_02_BenchmarkModes.measureSamples:measureSamples·p0.999     sample       104333.312             us/op
JMH_02_BenchmarkModes.measureSamples:measureSamples·p0.9999    sample       104333.312             us/op
JMH_02_BenchmarkModes.measureSamples:measureSamples·p1.00      sample       104333.312             us/op
JMH_02_BenchmarkModes.measureAll                                   ss       100189.815             us/op
JMH_02_BenchmarkModes.measureMultiple                              ss        99452.877             us/op
JMH_02_BenchmarkModes.measureSingleShot                            ss        99365.837             us/op
     */
}
