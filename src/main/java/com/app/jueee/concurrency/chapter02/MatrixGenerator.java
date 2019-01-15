package com.app.jueee.concurrency.chapter02;

import java.util.Random;

/**
 * 随机生成将进行乘法操作的矩阵。
 *	
 *	@author hzweiyongqiang
 */
public class MatrixGenerator {

    /**
     * 接收矩阵中所需的行数和列数作为参数，并基于这两个维数生成一个带有随机 double 值的矩阵。
     *	@param rows
     *	@param columns
     *	@return
     */
    public static double[][] generate(int rows,int columns){
        double[][] ret = new double[rows][columns];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                ret[i][j] = random.nextDouble() * 10;
            }
        }
        return ret;
    }
}
