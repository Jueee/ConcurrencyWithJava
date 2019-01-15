package com.app.jueee.concurrency.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class JMH_01_HelloWorld {

    @Benchmark
    public void wellHelloThere() {
        // 这个方法故意留空。
    }
    
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(JMH_01_HelloWorld.class.getName())
            .forks(1)
            .build();
        
        new Runner(options).run();
    }
    
    /**
# Run complete. Total time: 00:00:40

Benchmark                          Mode  Cnt           Score         Error  Units
JMH_01_HelloWorld.wellHelloThere  thrpt   20  1927812440.905 ± 4787542.683  ops/s

     */
}
