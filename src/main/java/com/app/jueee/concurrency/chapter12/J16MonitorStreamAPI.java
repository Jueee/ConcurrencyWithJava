package com.app.jueee.concurrency.chapter12;

import java.util.stream.IntStream;

public class J16MonitorStreamAPI {

    // 计算了前 999 个数的平方的平均值
    public static void main(String[] args) {

        double result=IntStream.range(0,1000)
                        .parallel()
                        .peek(n -> System.out.println(Thread.currentThread().getName()+": Number "+n))  // 输出该流处理的数
                        .map(n -> n*n)
                        .peek(n -> System.out.println(Thread.currentThread().getName()+": Transformer "+n)) // 输出这些数的平方
                        .average()
                        .getAsDouble();
        
        System.out.println("Result: "+result);

    }
}
