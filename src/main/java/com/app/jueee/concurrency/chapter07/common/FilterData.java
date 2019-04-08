package com.app.jueee.concurrency.chapter07.common;

/**
 *  该类定义了一个数据筛选器。筛选器包括一个属性的编号和该属性的值。
 *	
 *	@author hzweiyongqiang
 */
public class FilterData {

    private int idField;
    private String value;
    
    public int getIdField() {
        return idField;
    }
    public void setIdField(int idField) {
        this.idField = idField;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
