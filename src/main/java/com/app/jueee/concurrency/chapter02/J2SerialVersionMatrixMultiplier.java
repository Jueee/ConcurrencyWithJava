package com.app.jueee.concurrency.chapter02;

import java.util.Date;

/**
 *  串行版本
 *	
 *	@author hzweiyongqiang
 */
public class J2SerialVersionMatrixMultiplier {

    public static void multiply(double[][] matrix1, double[][] matrix2, double[][] result) {
        int row1 = matrix1.length;
        int columns1 = matrix1[0].length;
        int columns2 = matrix2[0].length;
        
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < columns2; j++) {
                result[i][j] = 0;
                for (int k = 0; k < columns1; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
    }

    
    public static int MATRIX_SIZE = 1000;
    /**
     *  测试串行版矩阵乘法算法
     *	@param args
     */
    public static void main(String[] args) {
        // 生成两个 2000 行 2000 列的随机矩阵
        double matrix1[][] = MatrixGenerator.generate(MATRIX_SIZE, MATRIX_SIZE);
        double matrix2[][] = MatrixGenerator.generate(MATRIX_SIZE, MATRIX_SIZE);
        double resultSerial[][]= new double[matrix1.length][matrix2[0].length];
        
        Date start=new Date();
        J2SerialVersionMatrixMultiplier.multiply(matrix1, matrix2, resultSerial);
        Date end=new Date();
        System.out.printf("Serial: %d%n",end.getTime()-start.getTime());
    }
    
}
