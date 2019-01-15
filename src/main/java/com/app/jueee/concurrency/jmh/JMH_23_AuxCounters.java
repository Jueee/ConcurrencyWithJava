package com.app.jueee.concurrency.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.AuxCounters;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


/**
 *  使用@AuxCounters针对状态类中的多个字段或公共方法作统计
 *	
 *	@author hzweiyongqiang
 */
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class JMH_23_AuxCounters {

    @State(Scope.Thread)
    @AuxCounters(AuxCounters.Type.OPERATIONS)
    public static class OpCounters {
        // 公共字段均会作为指标
        public int case1;
        public int case2;

        // 有返回结果的公共
        public int total() {
            return case1 + case2;
        }
    }

    @State(Scope.Thread)
    @AuxCounters(AuxCounters.Type.EVENTS)
    public static class EventCounters {
        // 该字段会作为指标，但不会量化
        public int wows;
    }

    /**
     *  测试代理里不同分支的性能
     *	@param counters
     */
    @Benchmark
    public void splitBranch(OpCounters counters) {
        if (Math.random() < 0.1) {
            counters.case1++;
        } else {
            counters.case2++;
        }
    }

    @Benchmark
    public void runSETI(EventCounters counters) {
        float random = (float) Math.random();
        float wowSignal = (float) Math.PI / 4;
        if (random == wowSignal) {
            // WOW, that's unusual.
            counters.wows++;
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_23_AuxCounters.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
    
    /**
     * 
Benchmark                              Mode  Cnt         Score        Error  Units
JMH_23_AuxCounters.runSETI            thrpt    5  55293441.745 ±  53744.420  ops/s
JMH_23_AuxCounters.runSETI:wows       thrpt    5        22.000                   #
JMH_23_AuxCounters.splitBranch        thrpt    5  53771742.172 ± 158902.077  ops/s
JMH_23_AuxCounters.splitBranch:case1  thrpt    5   5377967.311 ±  16944.380  ops/s
JMH_23_AuxCounters.splitBranch:case2  thrpt    5  48393774.861 ± 142478.389  ops/s
JMH_23_AuxCounters.splitBranch:total  thrpt    5  53771742.172 ± 158902.077  ops/s
     * 
     */
}
