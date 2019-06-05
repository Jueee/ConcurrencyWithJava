package com.app.jueee.concurrency.chapter11;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class J15VariableHandles {

    public static void main(String[] args) {
        VarHandleData data = new VarHandleData();
        for (int i = 0; i < 10; i++) {
            VarHandleTask task = new VarHandleTask(data);
            ForkJoinPool.commonPool().execute(task);
        }
        ForkJoinPool.commonPool().shutdown();
        try {
            ForkJoinPool.commonPool().awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Safe Value: "+data.safeValue);
        System.out.println("Unsafe Value: "+data.unsafeValue);
    }
}


class VarHandleData{
    public double safeValue;
    public double unsafeValue;
}

class VarHandleTask implements Runnable{
    private VarHandleData data;
    public VarHandleTask(VarHandleData data) {
        this.data = data;
    }
    @Override
    public void run() {
        VarHandle handle;
        try {
            handle = MethodHandles.lookup().in(VarHandleData.class)
                .findVarHandle(VarHandleData.class, "safeValue", double.class);
            for (int i = 0; i < 10000; i++) {
                handle.getAndAdd(data, +100);
                data.unsafeValue += 100;
                handle.getAndAdd(data, -100);
                data.unsafeValue -= 100;
            }
        } catch (Exception e) {
            System.out.println("[NoSuchFieldException]");
        }
        
    }
}