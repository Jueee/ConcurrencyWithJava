package com.app.jueee.concurrency.jmh;

import java.util.concurrent.atomic.AtomicBoolean;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Control;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


/**
 *  通过Control对象获取测试状态
 *	
 *	@author hzweiyongqiang
 */
@State(Scope.Group)
public class JMH_18_Control {

    public final AtomicBoolean flag = new AtomicBoolean();

    @Benchmark
    @Group("pingpong")
    public void ping(Control cnt) {
        while (!cnt.stopMeasurement && !flag.compareAndSet(false, true)) {
            // this body is intentionally left blank
        }
    }

    @Benchmark
    @Group("pingpong")
    public void pong(Control cnt) {
        while (!cnt.stopMeasurement && !flag.compareAndSet(true, false)) {
            // this body is intentionally left blank
        }
    }
    

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_18_Control.class.getSimpleName())
                .threads(2)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:00:40

Benchmark                      Mode  Cnt         Score        Error  Units
JMH_18_Control.pingpong       thrpt   20  44167258.722 ± 663040.555  ops/s
JMH_18_Control.pingpong:ping  thrpt   20  22082606.725 ± 331856.954  ops/s
JMH_18_Control.pingpong:pong  thrpt   20  22084651.997 ± 331230.361  ops/s
     * 
     */
}
