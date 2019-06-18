package com.app.jueee.concurrency.chapter12.MultithreadedTC;

import edu.umd.cs.mtc.TestFramework;

public class MainKo {

    public static void main(String[] args) {
        Data data = new Data();
        data.setData(10);
        TestClassKo ok = new TestClassKo(data, 10);
        try {
            TestFramework.runOnce(ok);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
