package com.app.jueee.concurrency.chapter02;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 第一个并发版本：每个元素一个线程
 * 
 * @author hzweiyongqiang
 */
public class C2ParallelVersionMatrixMultiplier1 {

    /**
     * 创建所有必要的执行线程计算结果矩阵
     */
    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
        List<Thread> threads = new ArrayList<>();
        int rows1 = matrix1.length;
        int columns1 = matrix1[0].length;
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < columns1; j++) {
                IndividualMultiplierTask task = new IndividualMultiplierTask(result, matrix1, matrix2, i, j);
                Thread thread = new Thread(task);
                thread.start();
                threads.add(thread);
                if (threads.size() % 10 == 0) {
                    waitForThreads(threads);
                }
            }
        }
    }

    private static void waitForThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads.clear();
    }


    
    public static int MATRIX_SIZE = 1000;
    /**
     *  测试并发版矩阵乘法算法
     *  @param args
     */
    public static void main(String[] args) {
        // 生成两个 2000 行 2000 列的随机矩阵
        double matrix1[][] = MatrixGenerator.generate(MATRIX_SIZE, MATRIX_SIZE);
        double matrix2[][] = MatrixGenerator.generate(MATRIX_SIZE, MATRIX_SIZE);
        double resultSerial[][]= new double[matrix1.length][matrix2[0].length];
        
        Date start=new Date();
        C2ParallelVersionMatrixMultiplier1.multiply(matrix1, matrix2, resultSerial);
        Date end=new Date();
        System.out.printf("Serial: %d%n",end.getTime()-start.getTime());
    }
}

/**
 * 实现每个 Thread 。 该类实现了 Runnable 接口，将使用五个内部属性：两个要相乘的矩阵、结果矩阵，以及要计算的元素的行和列。
 * 
 * @author hzweiyongqiang
 */
class IndividualMultiplierTask implements Runnable {
    private final double[][] result;
    private final double[][] matrix1;
    private final double[][] matrix2;
    private final int row;
    private final int column;

    public IndividualMultiplierTask(double[][] result, double[][] matrix1, double[][] matrix2, int i, int j) {
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.row = i;
        this.column = j;
    }

    @Override
    public void run() {
        result[row][column] = 0;
        for (int k = 0; k < matrix1[row].length; k++) {
            result[row][column] += matrix1[row][k] * matrix2[k][column];
        }
    }
}