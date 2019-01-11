package com.app.jueee.concurrency.chapter01;

import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *  读-写锁模式
 *	
 *	@author hzweiyongqiang
 */
public class J5ReadWriteLock {
    
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    private static class WriteThread extends Thread{

        @Override
        public void run() {
            write();
        }

        public void write() {
            try {
                try {
                    lock.writeLock().lock();
                    System.out.println("[Thread]"+currentThread().getName()+"\t"+new Date()+"\tget write");
                    Thread.sleep(10000);
                } finally {
                    lock.writeLock().unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static class ReadThread extends Thread{

        @Override
        public void run() {
            read();
        }

        public void read() {
            try {
                try {
                    lock.readLock().lock();
                    System.out.println("[Thread]"+currentThread().getName()+"\t"+new Date()+"\tget read");
                    Thread.sleep(10000);
                } finally {
                    lock.readLock().unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void testOnlyRead() {
        for (int i = 0; i < 10; i++) {
            new Thread(new ReadThread()).start();
        }
    }

    public static void testOnlyWrite() {
        for (int i = 0; i < 10; i++) {
            new Thread(new WriteThread()).start();
        }
    }

    public static void testBothWriteRead() {
        for (int i = 0; i < 10; i++) {
            new Thread(new ReadThread()).start();
            new Thread(new WriteThread()).start();
        }
    }

    public static void testReadWriteLock(int num) {
        switch (num) {
            case 1:
                testOnlyRead();
                break;
            case 2:
                testOnlyWrite();
                break;
            case 3:
                testBothWriteRead();
                break;
            default:
                System.out.println("--error--");
        }
    }
    
    
    public static void main(String[] args) {
        testReadWriteLock(3);
    }
}
