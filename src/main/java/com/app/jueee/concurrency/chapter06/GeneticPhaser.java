package com.app.jueee.concurrency.chapter06;

import java.util.Arrays;
import java.util.concurrent.Phaser;

/**
 *  我们需要在任务的阶段变化时执行代码
 *  因此必须实现自己的分段器并且重载 onAdvance() 方法
 *  在所有的参与方都完成某个阶段且即将开始执行下一阶段时执行该方法。
 *	
 *  它存储了需要用到的 SharedData 对象，并且将其作为构造函数的参数之一。
 *  
 *	@author hzweiyongqiang
 */
public class GeneticPhaser extends Phaser{

    private SharedData data;
    
    public GeneticPhaser(int parties, SharedData data) {
        super(parties);
        this.data = data;
    }
    
    public SharedData getData() {
        return data;
    }

    public void setData(SharedData data) {
        this.data = data;
    }

    /**
     *  如果余数为 0 ，任务完成了选择阶段并且准备执行交叉阶段。使用 0 值对该索引对象进行初始化。
     *  如果余数为 1 ，任务完成交叉阶段并且准备执行评估阶段。使用 0 值来初始化该索引对象。
     *  如果余数为 2 ，任务已经完成了评估阶段且准备再次开始选择阶段。我们基于适应度函数对种群进行排序，并且如果必要，还要更新最优个体。
     *  @param phase 分段器的阶段编号
     *  @param registeredParties 已注册参与方的编码
     *  @return
     */
    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        int realPhase = phase % 3;
        if (registeredParties > 0) {
            switch (realPhase) {
                case 0:
                case 1:
                    data.getIndex().set(0);
                    break;
                case 2:
                    Arrays.sort(data.getPopulation());
                    if (data.getPopulation()[0].getValue() < data.getBest().getValue()) {
                        data.setBest(data.getPopulation()[0]);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
        return true;
    }
}
