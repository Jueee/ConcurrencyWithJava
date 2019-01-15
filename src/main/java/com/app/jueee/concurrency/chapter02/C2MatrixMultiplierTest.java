package com.app.jueee.concurrency.chapter02;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class C2MatrixMultiplierTest {
    
    @Param({"100", "500", "1000"})
    private int length;
    
    private double[][] matrix1;
    private double[][] matrix2;
    private double[][] resultSerial;
    
    @Setup
    public void prepare() {
        // 生成两个 2000 行 2000 列的随机矩阵
        matrix1 = MatrixGenerator.generate(length, length);
        matrix2 = MatrixGenerator.generate(length, length);
        resultSerial = new double[matrix1.length][matrix2[0].length];
    }
    
    @Benchmark
    public void serialVersion() {
        C2SerialVersionMatrixMultiplier.multiply(matrix1, matrix2, resultSerial);
    }
    
    @Benchmark
    public void parallelVersion1() {
        C2ParallelVersionMatrixMultiplier1.multiply(matrix1, matrix2, resultSerial);
    }
    
    @Benchmark
    public void parallelVersion2() {
        C2ParallelVersionMatrixMultiplier2.multiply(matrix1, matrix2, resultSerial);
    }
    
    @Benchmark
    public void parallelVersion3() {
        C2ParallelVersionMatrixMultiplier3.multiply(matrix1, matrix2, resultSerial);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(C2MatrixMultiplierTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
}
