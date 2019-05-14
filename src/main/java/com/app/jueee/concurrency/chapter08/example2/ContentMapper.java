package com.app.jueee.concurrency.chapter08.example2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import com.app.jueee.concurrency.chapter08.common2.Document;

public class ContentMapper implements Function<Document, String> {

	private String query[];

	public ContentMapper(String query[]) {
		this.query = query;
	}

	@Override
	public String apply(Document d) {
		String result = "";
		
		try (Stream<String> content = Files.lines(Paths.get("docs",d.getDocumentName()))) {
			result = "<h2>" + d.getDocumentName() + ": "
					+ content.findFirst().get()
					+ ": " + d.getTfxidf() + "</h2>";
		} catch (IOException e) {
			e.printStackTrace();
			throw new UncheckedIOException(e);
		}
		
		try (Stream<String> content = Files.lines(Paths.get("docs",d.getDocumentName()))) {
			result += content
					.filter(l -> Arrays.stream(query).anyMatch(l.toLowerCase()::contains))
					.limit(3)
					.map(l -> "<p>"+l+"</p>")
					.reduce("",String::concat);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw new UncheckedIOException(e);
		}
	}

}
