package com.app.jueee.concurrency.chapter03.data;

/**
 *  存放 Sample 输入和训练数据集中某一实例之间的距离。
 *	
 *	@author hzweiyongqiang
 */
public class Distance implements Comparable<Distance> {

	/**
	 * 训练集范例的索引
	 */
	private int index;
	/**
	 * 训练集范例到输入范例的距离
	 */
	private double distance;
	
	
	/**
	 *  比较两个距离
	 */
	@Override
	public int compareTo(Distance other) {
		if (this.distance < other.getDistance()) {
			return -1;
		} else if (this.distance > other.getDistance()) {
			return 1;
		}
		return 0;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	
	
}
