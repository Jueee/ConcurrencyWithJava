package com.app.jueee.concurrency.chapter12.MultithreadedTC;

import edu.umd.cs.mtc.MultithreadedTestCase;

public class TestClassKo extends MultithreadedTestCase{
    
    private Data data;
    private int amount;
    private int initialData;
    
    public TestClassKo(Data data, int amount) {
        this.amount = amount;
        this.data = data;
        this.initialData = data.getData();
    }
    
    @Override
    public void initialize() {
        super.initialize();
    }
    
    // 读取数据的值，增加其值，并且再次输出数据的值。
    public void threadAdd() {
        System.out.println("Add: Getting the data");
        int value = data.getData();
        waitForTick(2);
        System.out.println("Add: Increment the data");
        value += amount;
        System.out.println("Add: Set the data");
        data.setData(value);
    }
    
    // 首先，等待时刻 1 。然后，获取该数据的值，减少其值，并且重新输出该数据的值。
    public void threadSub() {
        waitForTick(1);
        System.out.println("Sub: Getting the data");
        int value = data.getData();
        waitForTick(3);
        System.out.println("Sub: Decrement the data");
        value -= amount;
        System.out.println("Sub: Set the data");
        data.setData(value);
    }
    
    @Override
    public void finish() {
        super.finish();
        assertEquals(initialData, data.getData());
    }
    
}
