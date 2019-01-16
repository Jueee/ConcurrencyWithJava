package com.app.jueee.concurrency.jmh;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class JMH_38_PerInvokeSetup {

    private void bubbleSort(byte[] b) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int c = 0; c < b.length - 1; c++) {
                if (b[c] > b[c + 1]) {
                    byte t = b[c];
                    b[c] = b[c + 1];
                    b[c + 1] = t;
                    changed = true;
                }
            }
        }
    }

    @State(Scope.Benchmark)
    public static class Data {

        @Param({"1", "16", "256"})
        int count;

        byte[] arr;

        @Setup
        public void setup() {
            arr = new byte[count];
            Random random = new Random(1234);
            random.nextBytes(arr);
        }
    }

    @Benchmark
    public byte[] measureWrong(Data d) {
        // 只有第一次迭代排序正确，后续迭代时，数组已经有序
        bubbleSort(d.arr);
        return d.arr;
    }

    @State(Scope.Thread)
    public static class DataCopy {
        byte[] copy;

        @Setup(Level.Invocation)
        public void setup2(Data d) {
           copy = Arrays.copyOf(d.arr, d.arr.length);
        }
    }

    @Benchmark
    public byte[] measureNeutral(DataCopy d) {
        bubbleSort(d.copy);
        return d.copy;
    }

    @Benchmark
    public byte[] measureRight(Data d) {
        byte[] c = Arrays.copyOf(d.arr, d.arr.length);
        bubbleSort(c);
        return c;
    }
    

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + JMH_38_PerInvokeSetup.class.getSimpleName() + ".*")
                .build();

        new Runner(opt).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:07:45

Benchmark                             (count)  Mode  Cnt      Score      Error  Units
JMH_38_PerInvokeSetup.measureNeutral        1  avgt   25     23.595 ±    0.341  ns/op
JMH_38_PerInvokeSetup.measureNeutral       16  avgt   25     90.994 ±    1.958  ns/op
JMH_38_PerInvokeSetup.measureNeutral      256  avgt   25  53189.792 ± 2356.391  ns/op
JMH_38_PerInvokeSetup.measureRight          1  avgt   25      7.371 ±    0.221  ns/op
JMH_38_PerInvokeSetup.measureRight         16  avgt   25     81.985 ±    0.767  ns/op
JMH_38_PerInvokeSetup.measureRight        256  avgt   25  58261.581 ± 4280.846  ns/op
JMH_38_PerInvokeSetup.measureWrong          1  avgt   25      2.603 ±    0.135  ns/op
JMH_38_PerInvokeSetup.measureWrong         16  avgt   25      7.415 ±    0.087  ns/op
JMH_38_PerInvokeSetup.measureWrong        256  avgt   25     55.517 ±    0.318  ns/op
     * 
     */
}
