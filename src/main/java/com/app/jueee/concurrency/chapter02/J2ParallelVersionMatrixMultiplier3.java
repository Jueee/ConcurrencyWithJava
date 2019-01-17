package com.app.jueee.concurrency.chapter02;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 第三个并发版本：线程的数量由处理器决定
 * 
 * @author hzweiyongqiang
 */
public class J2ParallelVersionMatrixMultiplier3 {

    /**
     * 创建所有必要的执行线程计算结果矩阵
     */
    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
        List<Thread> threads = new ArrayList<>();
        int rows1 = matrix1.length;
        int numThreads = Runtime.getRuntime().availableProcessors();
        int startIndex, endIndex, step;
        step = rows1 / numThreads;
        startIndex = 0;
        endIndex = step;
        for (int i = 0; i < numThreads; i++) {
            GroupMultiplierTask task = new GroupMultiplierTask(result, matrix1, matrix2, startIndex, endIndex);
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);
            startIndex = endIndex;
            endIndex = i == numThreads - 2 ? rows1 : endIndex + step;
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int MATRIX_SIZE = 1000;

    /**
     * 测试并发版矩阵乘法算法
     * 
     * @param args
     */
    public static void main(String[] args) {
        // 生成两个 2000 行 2000 列的随机矩阵
        double matrix1[][] = MatrixGenerator.generate(MATRIX_SIZE, MATRIX_SIZE);
        double matrix2[][] = MatrixGenerator.generate(MATRIX_SIZE, MATRIX_SIZE);
        double resultSerial[][] = new double[matrix1.length][matrix2[0].length];

        Date start = new Date();
        J2ParallelVersionMatrixMultiplier3.multiply(matrix1, matrix2, resultSerial);
        Date end = new Date();
        System.out.printf("Serial: %d%n", end.getTime() - start.getTime());
    }
}

/**
 * 实现每个 Thread 。 
 * 
 * @author hzweiyongqiang
 */
class GroupMultiplierTask implements Runnable {
    private final double[][] result;
    private final double[][] matrix1;
    private final double[][] matrix2;
    private final int startIndex;
    private final int endIndex;

    public GroupMultiplierTask(double[][] result, double[][] matrix1, double[][] matrix2, int startIndex,
        int endIndex) {
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void run() {
        for (int i = startIndex; i < endIndex; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                result[i][j] = 0;
                for (int k = 0; k < matrix1[i].length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
    }
}