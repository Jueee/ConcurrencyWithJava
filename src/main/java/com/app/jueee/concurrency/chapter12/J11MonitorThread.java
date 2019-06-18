package com.app.jueee.concurrency.chapter12;

public class J11MonitorThread {

    public static void main(String[] args) {

        Thread thread = new Thread(new CommonThreadTask());
        thread.start();

        for (int i = 0; i < 10; i++) {
            System.out.println("**********************");
            System.out.println("Id: " + thread.getId());
            System.out.println("Name: " + thread.getName());
            System.out.println("Priority: " + thread.getPriority());
            System.out.println("Status: " + thread.getState());
            System.out.println("Stack Trace");
            for(StackTraceElement ste : thread.getStackTrace()) {
                System.out.println(ste);
            }
            System.out.println("**********************\n");
        }
    }
}



