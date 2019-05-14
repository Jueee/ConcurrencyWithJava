package com.app.jueee.concurrency.chapter08.common2;

public class Token {
	
	private final String word;
	private final double tfxidf;
	private final String file;

	public Token(String word, String token) {
		this.word=word;
		String[] parts=token.split(":");
		this.file=parts[0];
		this.tfxidf=Double.parseDouble(parts[1]);
	}

	public String getWord() {
		return word;
	}

	public double getTfxidf() {
		return tfxidf;
	}

	public String getFile() {
		return file;
	}

	@Override
	public String toString() {
		return word+":"+file+":"+tfxidf;
	}

	
	
}
