package com.app.jueee.concurrency.chapter01;

/** java中的sleep()和wait()的区别 */
public class J5Signaling {

    public static void main(String[] args) {
        new Thread(new Thread1()).start();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(new Thread2()).start();
    }

    private static class Thread1 extends Thread{
        @Override
        public void run(){
            synchronized (J5Signaling.class) {
                System.out.println("enter thread1...");
                System.out.println("thread1 is waiting...");
                try {
                    //调用wait()方法，线程会放弃对象锁，进入等待此对象的等待锁定池
                    J5Signaling.class.wait();
                    section2();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("thread1 is going on ....");
                System.out.println("thread1 is over!!!");
            }
        }

        public void section2(){
            System.out.println("section2");
        }
    }

    private static class Thread2 extends Thread{
        @Override
        public void run(){
            synchronized (J5Signaling.class) {
                System.out.println("enter thread2....");
                System.out.println("thread2 is sleep....");
                //只有针对此对象调用notify()方法后本线程才进入对象锁定池准备获取对象锁进入运行状态。
                section1();
                J5Signaling.class.notify();
                //==================
                //区别
                //如果我们把代码：J5Signaling.class.notify();给注释掉，即J5Signaling.class调用了wait()方法，但是没有调用notify()
                //方法，则线程永远处于挂起状态。
                try {
                    //sleep()方法导致了程序暂停执行指定的时间，让出cpu该其他线程，
                    //但是他的监控状态依然保持者，当指定的时间到了又会自动恢复运行状态。
                    //在调用sleep()方法的过程中，线程不会释放对象锁。
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("thread2 is going on....");
                System.out.println("thread2 is over!!!");
            }
        }

        public void section1(){
            System.out.println("section1");
        }
    }
}