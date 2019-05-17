package com.app.jueee.concurrency.chapter09.common2;

public class Review {
	
    // 用户的内部编码
	private String user;
	// 用户对商品的评分
	private short value;
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public short getValue() {
		return value;
	}
	
	public void setValue(short value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
	    return user+":"+value;
	}
	

}
