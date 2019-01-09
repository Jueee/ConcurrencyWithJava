package com.app.jueee.concurrency.chapter01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 *  双重检查锁定模式
 *  
 *  当你获得某个锁之后要检查某项条件时，这种设计模式可以为解决该问题提供方案。  
 *	
 *	@author hzweiyongqiang
 */
public class J5DoubleCheckedLocking {

    private static J5DoubleCheckedLocking reference;
    private static final Lock lock = new ReentrantLock();


    /**
     *  方法一：单例设计模式。
     *  存在问题：如果该条件为假，你实际上也已经花费了获取到理想的锁所需的开销。
     *  @return
     */
    public static J5DoubleCheckedLocking getReference1() {
        try {
            lock.lock();
            if (reference == null) {
                reference = new J5DoubleCheckedLocking();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            lock.unlock();
        }
        return reference;
    }

    /**
     *  方法二：在条件之中包含锁。
     *  存在问题：如果两个任务同时检查条件，你将要创建两个对象。  
     *	@return
     */
    public static J5DoubleCheckedLocking getReference2() {
        if (reference == null) {
            lock.lock();
            if (reference == null) {
                reference = new J5DoubleCheckedLocking();
            }
            lock.unlock();
        }
        return reference;
    }

    
    /**
     *  最佳方案就是不使用任何显式的同步机制
     */
    private static class LazySingleton {
        private static final J5DoubleCheckedLocking INSTANCE = new J5DoubleCheckedLocking();
    }
    public static J5DoubleCheckedLocking getSingleton() {
        return LazySingleton.INSTANCE;
    }
    
    public static void main(String[] args) {

    }
}
