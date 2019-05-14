package com.app.jueee.concurrency.chapter08.example2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Stream;

import com.app.jueee.concurrency.chapter08.common2.QueryResult;
import com.app.jueee.concurrency.chapter08.common2.Token;
import com.app.jueee.concurrency.chapter08.common2.Utils;

public class ConcurrentSearch2 {


    public static void reducedSearch(String query[]) throws IOException {
        Path path = Paths.get("data/chapter08", "salida.txt");
        HashSet<String> set = new HashSet<>(Arrays.asList(query));
        QueryResult results = new QueryResult(new ConcurrentHashMap<>());

        // Version 2
        try (Stream<String> invertedIndex = Files.lines(path)) {

            invertedIndex.parallel()
                .filter(line -> set.contains(Utils.getWord(line)))
                .flatMap(ConcurrentSearch2::limitedMapper)
                .forEach(results::append);

            results
                .getAsList()
                .stream()
                .sorted()
                .limit(100)
                .forEach(System.out::println);

            System.out.println("Reduced Search Ok ");
        }

    }

    public static Stream<Token> limitedMapper(String input) {
        ConcurrentLinkedDeque<Token> list = new ConcurrentLinkedDeque();
        String word = Utils.getWord(input);

        Arrays.stream(input.split(","))
            .skip(1)
            .limit(100)
            .parallel()
            .forEach(token -> {
                list.add(new Token(word, token));
            });

        return list.stream();
    }
}
