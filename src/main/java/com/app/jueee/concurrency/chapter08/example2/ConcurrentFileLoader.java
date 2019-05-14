package com.app.jueee.concurrency.chapter08.example2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Stream;

import com.app.jueee.concurrency.chapter08.common2.ConcurrentInvertedIndex;
import com.app.jueee.concurrency.chapter08.common2.Token;

public class ConcurrentFileLoader {

	public ConcurrentInvertedIndex load(Path path) throws IOException {
		ConcurrentInvertedIndex invertedIndex = new ConcurrentInvertedIndex();
		ConcurrentLinkedDeque<Token> results=new ConcurrentLinkedDeque<>();
		
		try (Stream<String> fileStream = Files.lines(path)) {
			fileStream
			.parallel()
			.flatMap(ConcurrentSearch2::limitedMapper)
			.forEach(results::add);
		}
		
		invertedIndex.setIndex(new ArrayList<>(results));
		return invertedIndex;
	}
}
