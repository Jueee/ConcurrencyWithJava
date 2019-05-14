package com.app.jueee.concurrency.chapter08.common2;

public class Utils {

	public static String getWord(String w) {
		int pos = w.indexOf(":");
		String word = w.substring(0, pos);
		return word;
	}

}
