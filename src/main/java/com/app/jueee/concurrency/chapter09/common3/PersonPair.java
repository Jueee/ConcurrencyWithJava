package com.app.jueee.concurrency.chapter09.common3;


public class PersonPair extends Person {
	
    // 存放第二个用户标识符
	private String otherId;
	
	public String getOtherId() {
		return otherId;
	}

	public void setOtherId(String otherId) {
		this.otherId = otherId.intern();
	}
	
	public String getFullId() {
		return getId()+","+otherId;
	}

}
