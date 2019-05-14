package com.app.jueee.concurrency.chapter08.common2;

import java.util.List;

import com.app.jueee.concurrency.chapter08.common2.Token;

public class ConcurrentInvertedIndex {

	private List<Token> index;

	public void setIndex(List<Token> index) {
		this.index = index;
	}

	public List<Token> getIndex() {
		return index;
	}
	
}
