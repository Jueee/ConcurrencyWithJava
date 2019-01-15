package com.app.jueee.concurrency.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
public class JMH_04_DefaultState {


    double x = Math.PI;

    @Benchmark
    public void measure() {
        x++;
    }
    
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_04_DefaultState.class.getName())
            .forks(1)
            .build();
        
        new Runner(options).run();
    }
    
    /**
# Run complete. Total time: 00:00:40

Benchmark                     Mode  Cnt          Score        Error  Units
JMH_04_DefaultState.measure  thrpt   20  429225766.575 ± 592930.674  ops/s
     * 
     */
}
