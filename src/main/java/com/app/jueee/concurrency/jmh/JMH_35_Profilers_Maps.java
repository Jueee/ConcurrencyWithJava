package com.app.jueee.concurrency.jmh;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


/**
 *  使用Profiler作测试
 *	
 *	@author hzweiyongqiang
 */
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_35_Profilers_Maps {

    private Map<Integer, Integer> map;

    @Param({"hashmap", "treemap"})
    private String type;

    private int begin;
    private int end;

    @Setup
    public void setup() {
        switch (type) {
            case "hashmap":
                map = new HashMap<>();
                break;
            case "treemap":
                map = new TreeMap<>();
                break;
            default:
                throw new IllegalStateException("Unknown type: " + type);
        }

        begin = 1;
        end = 256;
        for (int i = begin; i < end; i++) {
            map.put(i, i);
        }
    }

    @Benchmark
    public void test(Blackhole bh) {
        for (int i = begin; i < end; i++) {
            bh.consume(map.get(i));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_35_Profilers_Maps.class.getSimpleName())
                .addProfiler(StackProfiler.class)
//              .addProfiler(GCProfiler.class)
                .build();

        new Runner(opt).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:01:02

Benchmark                           (type)  Mode  Cnt     Score     Error  Units
JMH_35_Profilers_Maps.test         hashmap  avgt   15  1354.707 ±  11.376  ns/op
JMH_35_Profilers_Maps.test:·stack  hashmap  avgt            NaN              ---
JMH_35_Profilers_Maps.test         treemap  avgt   15  5140.672 ± 277.571  ns/op
JMH_35_Profilers_Maps.test:·stack  treemap  avgt            NaN              ---
     * 
     */
}
