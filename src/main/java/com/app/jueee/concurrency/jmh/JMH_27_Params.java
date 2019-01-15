package com.app.jueee.concurrency.jmh;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


/**
 *  使用@Param注解指定配置参数
 *	
 *	@author hzweiyongqiang
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class JMH_27_Params {

    @Param({"1", "31", "65", "101", "103"})
    public int arg;

    @Param({"0", "1", "2", "4", "8", "16", "32"})
    public int certainty;

    @Benchmark
    public boolean bench() {
        return BigInteger.valueOf(arg).isProbablePrime(certainty);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_27_Params.class.getSimpleName())
//                .param("arg", "41", "42") // 使用此选项可以有选择地约束/覆盖参数
                .build();

        new Runner(opt).run();
    }
    
    /**
     * 
Benchmark            (arg)  (certainty)  Mode  Cnt     Score     Error  Units
JMH_27_Params.bench      1            0  avgt    5     3.359 ±   0.005  ns/op
JMH_27_Params.bench      1            1  avgt    5     5.028 ±   0.624  ns/op
JMH_27_Params.bench      1            2  avgt    5     4.935 ±   0.244  ns/op
JMH_27_Params.bench      1            4  avgt    5     4.925 ±   0.015  ns/op
JMH_27_Params.bench      1            8  avgt    5     4.967 ±   0.283  ns/op
JMH_27_Params.bench      1           16  avgt    5     4.916 ±   0.034  ns/op
JMH_27_Params.bench      1           32  avgt    5     4.927 ±   0.070  ns/op
JMH_27_Params.bench     31            0  avgt    5     4.549 ±   0.184  ns/op
JMH_27_Params.bench     31            1  avgt    5   412.167 ±  29.064  ns/op
JMH_27_Params.bench     31            2  avgt    5   403.824 ±  11.260  ns/op
JMH_27_Params.bench     31            4  avgt    5   779.863 ±  32.965  ns/op
JMH_27_Params.bench     31            8  avgt    5  1540.967 ± 158.689  ns/op
JMH_27_Params.bench     31           16  avgt    5  2929.196 ± 136.345  ns/op
JMH_27_Params.bench     31           32  avgt    5  5861.520 ± 294.863  ns/op
JMH_27_Params.bench     65            0  avgt    5     4.647 ±   0.408  ns/op
JMH_27_Params.bench     65            1  avgt    5   868.868 ±  19.529  ns/op
JMH_27_Params.bench     65            2  avgt    5   883.440 ± 117.616  ns/op
JMH_27_Params.bench     65            4  avgt    5   966.336 ± 103.454  ns/op
JMH_27_Params.bench     65            8  avgt    5   946.511 ± 148.354  ns/op
JMH_27_Params.bench     65           16  avgt    5   948.322 ± 116.389  ns/op
JMH_27_Params.bench     65           32  avgt    5   972.510 ±  96.631  ns/op
JMH_27_Params.bench    101            0  avgt    5     4.587 ±   0.371  ns/op
JMH_27_Params.bench    101            1  avgt    5   522.748 ±  22.971  ns/op
JMH_27_Params.bench    101            2  avgt    5   525.493 ±  12.721  ns/op
JMH_27_Params.bench    101            4  avgt    5  1014.906 ±  18.048  ns/op
JMH_27_Params.bench    101            8  avgt    5  2007.209 ± 204.189  ns/op
JMH_27_Params.bench    101           16  avgt    5  3907.753 ± 102.693  ns/op
JMH_27_Params.bench    101           32  avgt    5  7668.036 ±  90.330  ns/op
JMH_27_Params.bench    103            0  avgt    5     4.547 ±   0.502  ns/op
JMH_27_Params.bench    103            1  avgt    5   480.826 ±   8.881  ns/op
JMH_27_Params.bench    103            2  avgt    5   483.328 ±  11.553  ns/op
JMH_27_Params.bench    103            4  avgt    5   926.902 ±  19.072  ns/op
JMH_27_Params.bench    103            8  avgt    5  1808.312 ±  36.797  ns/op
JMH_27_Params.bench    103           16  avgt    5  3582.709 ± 173.850  ns/op
JMH_27_Params.bench    103           32  avgt    5  7072.571 ± 121.259  ns/op
     * 
     */
}
