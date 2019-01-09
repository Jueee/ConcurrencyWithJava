package com.app.jueee.concurrency.chapter01;

/**
 *      会合模式
 *	
 *	@author hzweiyongqiang
 */
public class J5Rendezvous {

    public static void main(String[] args) {
        new Thread(new ThreadRendezvous1()).start();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(new ThreadRendezvous2()).start();
    }

    
    public static Object commonObject1 = "";
    public static Object commonObject2 = "";
    
    private static class ThreadRendezvous1 extends Thread{
        @Override
        public void run(){
            synchronized (commonObject1) {
                System.out.println("enter thread1...");
                try {
                    section1_1();
                    commonObject1.notify();
                    commonObject2.wait();
                    section1_2();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                System.out.println("over thread1...");
            }
        }
        public void section1_1(){
            System.out.println("section1_1");
        }
        public void section1_2(){
            System.out.println("section1_2");
        }
    }

    private static class ThreadRendezvous2 extends Thread{
        @Override
        public void run(){
            synchronized (commonObject1) {
                System.out.println("enter thread2....");
                try {
                    section2_1();
                    commonObject2.notify();
//                    commonObject1.wait();
                    section2_2();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("over thread2...");
            }
        }

        public void section2_1(){
            System.out.println("section2_1");
        }
        public void section2_2(){
            System.out.println("section2_2");
        }
    }
}
