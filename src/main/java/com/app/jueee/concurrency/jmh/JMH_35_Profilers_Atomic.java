package com.app.jueee.concurrency.jmh;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.profile.WinPerfAsmProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


/**
 *  使用Profiler作测试
 *	
 *	@author hzweiyongqiang
 */
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_35_Profilers_Atomic {

    private AtomicLong n;

    @Setup
    public void setup() {
        n = new AtomicLong();
    }

    @Benchmark
    public long test() {
        return n.incrementAndGet();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_35_Profilers_Atomic.class.getSimpleName())
//                .addProfiler(LinuxPerfProfiler.class)
//                .addProfiler(LinuxPerfNormProfiler.class)
//                .addProfiler(LinuxPerfAsmProfiler.class)
                .addProfiler(WinPerfAsmProfiler.class)
//                .addProfiler(DTraceAsmProfiler.class)
                .build();

        new Runner(opt).run();
    }
}
