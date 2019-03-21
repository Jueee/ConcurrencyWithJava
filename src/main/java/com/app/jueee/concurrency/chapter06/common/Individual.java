package com.app.jueee.concurrency.chapter06.common;

public class Individual implements Comparable<Individual>{
    
    // 该数组包含旅行商经过各个城市的顺序。
    private Integer[] chromosomes;
    // 存放适应度函数的结果
    private int value;
    
    // 接收必须访问的城市的数量作为参数，创建一个空数组。
    public Individual(int size) {
        chromosomes = new Integer[size];
    }
    
    // 接收 Individual 对象作为参数，并且复制其染色体
    public Individual(Individual other) {
        chromosomes = other.getChromosomes().clone();
    }

    // 使用适应度函数的结果比较两个个体
    @Override
    public int compareTo(Individual o) {
        return Integer.compare(this.getValue(), o.getValue());
    }

    public Integer[] getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(Integer[] chromosomes) {
        this.chromosomes = chromosomes;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String ret="";
        for (Integer number: chromosomes) {
            ret+=number+";";
        }
        return ret;
    }

}
