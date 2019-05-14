package com.app.jueee.concurrency.chapter08.example2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Stream;

import com.app.jueee.concurrency.chapter08.common2.QueryResult;
import com.app.jueee.concurrency.chapter08.common2.Token;
import com.app.jueee.concurrency.chapter08.common2.Utils;

public class ConcurrentSearch3 {
    
    
    public static void htmlSearch(String query[], String fileName) throws IOException {
        Path path = Paths.get("data/chapter08", "salida.txt");
        HashSet<String> set = new HashSet<>(Arrays.asList(query));
        QueryResult results = new QueryResult(new ConcurrentHashMap<>());
        try (Stream<String> invertedIndex = Files.lines(path)) {
            invertedIndex.parallel()
                .filter(line -> set.contains(Utils.getWord(line)))
                .flatMap(ConcurrentSearch3::limitedMapper)
                .forEach(results::append);

            path = Paths.get("output", fileName + "_results.html");
            try (BufferedWriter fileWriter = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
                fileWriter.write("<HTML>");
                fileWriter.write("<HEAD>");
                fileWriter.write("<TITLE>");
                fileWriter.write("Search Results with Streams");
                fileWriter.write("</TITLE>");
                fileWriter.write("</HEAD>");
                fileWriter.write("<BODY>");
                fileWriter.newLine();
                results.getAsList().stream().sorted().limit(100).map(new ContentMapper(query)).forEach(l -> {
                    try {
                        fileWriter.write(l);
                        fileWriter.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                fileWriter.write("</BODY>");
                fileWriter.write("</HTML>");
            }
        }
    }

    public static Stream<Token> limitedMapper(String input) {
        ConcurrentLinkedDeque<Token> list = new ConcurrentLinkedDeque();
        String word = Utils.getWord(input);

        Arrays.stream(input.split(",")).skip(1).limit(100).parallel().forEach(token -> {
            list.add(new Token(word, token));
        });

        return list.stream();
    }
}
