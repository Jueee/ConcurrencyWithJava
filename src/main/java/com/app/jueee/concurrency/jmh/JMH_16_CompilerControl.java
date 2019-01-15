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


/**
 *  使用@CompilerControl设置JVM的编译选型(HotSpot)
 *	
 *	@author hzweiyongqiang
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_16_CompilerControl {

   public void target_blank() {
       // this method was intentionally left blank
   }

   /**
        *   编译时，不内联
    */
   @CompilerControl(CompilerControl.Mode.DONT_INLINE)
   public void target_dontInline() {
       // this method was intentionally left blank
   }

   /**
        *   编译时，内联
    */
   @CompilerControl(CompilerControl.Mode.INLINE)
   public void target_inline() {
       // this method was intentionally left blank
   }

   /**
        *   编译时，排除该方法
    */
   @CompilerControl(CompilerControl.Mode.EXCLUDE)
   public void target_exclude() {
       // this method was intentionally left blank
   }


   @Benchmark
   public void baseline() {
       // this method was intentionally left blank
   }

   @Benchmark
   public void blank() {
       target_blank();
   }

   @Benchmark
   public void dontinline() {
       target_dontInline();
   }

   @Benchmark
   public void inline() {
       target_inline();
   }

   @Benchmark
   public void exclude() {
       target_exclude();
   }
   
   public static void main(String[] args) throws RunnerException {
       Options options = new OptionsBuilder()
           .include(JMH_16_CompilerControl.class.getName())
           .warmupIterations(0)
           .measurementIterations(3)
           .forks(1)
           .build();
       
       new Runner(options).run();
   }
   
   /**
    * 
# Run complete. Total time: 00:00:17

Benchmark                          Mode  Cnt   Score    Error  Units
JMH_16_CompilerControl.baseline    avgt    3   0.432 ±  1.342  ns/op
JMH_16_CompilerControl.blank       avgt    3   0.432 ±  1.359  ns/op
JMH_16_CompilerControl.dontinline  avgt    3   1.742 ±  2.759  ns/op
JMH_16_CompilerControl.exclude     avgt    3  12.683 ± 10.697  ns/op
JMH_16_CompilerControl.inline      avgt    3   0.433 ±  1.388  ns/op
    * 
    */
}
