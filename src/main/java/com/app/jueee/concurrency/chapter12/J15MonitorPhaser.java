package com.app.jueee.concurrency.chapter12;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class J15MonitorPhaser {

    public static void main(String[] args) {

        Phaser phaser=new Phaser(10);
        
        ThreadPoolExecutor executor=(ThreadPoolExecutor)Executors.newCachedThreadPool();
        
        for (int i=0; i<10; i++) {
            executor.execute(new CommonPhaserTask(phaser));
        }

        for (int i=0; i<20; i++) {
            System.out.println("*******************************************");
            System.out.println("Arrived Parties: "+phaser.getArrivedParties());
            System.out.println("Unarrived Parties: "+phaser.getUnarrivedParties());
            System.out.println("Phase: "+phaser.getPhase());
            System.out.println("Registered Parties: "+phaser.getRegisteredParties());
            System.out.println("Terminated: "+phaser.isTerminated());
            System.out.println("*******************************************");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        executor.shutdown();
        
        System.out.println("*******************************************");
        System.out.println("Arrived Parties: "+phaser.getArrivedParties());
        System.out.println("Unarrived Parties: "+phaser.getUnarrivedParties());
        System.out.println("Phase: "+phaser.getPhase());
        System.out.println("Registered Parties: "+phaser.getRegisteredParties());
        System.out.println("Terminated: "+phaser.isTerminated());
        System.out.println("*******************************************");

    }
}


class CommonPhaserTask implements Runnable {
    
    private Phaser phaser;
    
    public CommonPhaserTask (Phaser phaser) {
        this.phaser=phaser;
    }
    
    @Override
    public void run() {
        
        long duration=(long)(Math.random()*10);
        System.out.printf("%s-%s: Working %d seconds\n",new Date(),Thread.currentThread().getName(),duration);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        phaser.arriveAndAwaitAdvance();
        
        duration=(long)(Math.random()*10);
        System.out.printf("%s-%s: Working %d seconds\n",new Date(),Thread.currentThread().getName(),duration);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        phaser.arriveAndDeregister();
    }

}
