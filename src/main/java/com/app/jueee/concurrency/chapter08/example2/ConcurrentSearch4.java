package com.app.jueee.concurrency.chapter08.example2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import com.app.jueee.concurrency.chapter08.common2.ConcurrentInvertedIndex;
import com.app.jueee.concurrency.chapter08.common2.QueryResult;

public class ConcurrentSearch4 {


    public static void preloadSearch(String[] query, ConcurrentInvertedIndex invertedIndex) {

        HashSet<String> set = new HashSet<>(Arrays.asList(query));
        QueryResult results = new QueryResult(new ConcurrentHashMap<>());

        // Version 4
        invertedIndex.getIndex()
            .parallelStream()
            .filter(token -> set.contains(token.getWord()))
            .forEach(results::append);

        results
            .getAsList()
            .stream()
            .sorted()
            .limit(100)
            .forEach(System.out::println);

        System.out.println("Preload Search Ok.");
    }
}
