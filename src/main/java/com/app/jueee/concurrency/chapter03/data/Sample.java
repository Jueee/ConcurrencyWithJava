package com.app.jueee.concurrency.chapter03.data;

/**
 * Abstract class that defines the basic elements of an example to the knn algoritm
 * @author author
 *
 */
public abstract class Sample {

	/**
	 * 含有实例标签的字符串
	 */
	public abstract String getTag();
	
	public abstract double[] getExample();
}
