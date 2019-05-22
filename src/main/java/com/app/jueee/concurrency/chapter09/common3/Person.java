package com.app.jueee.concurrency.chapter09.common3;

import java.util.ArrayList;
import java.util.List;

public class Person {
	
    // 用户 ID
	private String id;
	// 该用户的联系人列表
	private List<String> contacts;
	
	public Person() {
		contacts=new ArrayList<String>();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id.intern();
	}
	
	public List<String> getContacts() {
		return contacts;
	}
	
	public void setContacts(List<String> contacts) {
		this.contacts = contacts;
	}
	
	// 用于将单个联系人添	加到联系人列表。
	public void addContact(String contact) {
		contacts.add(contact);
	}
}
