package com.app.jueee.concurrency.chapter08.example2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

import com.app.jueee.concurrency.chapter08.common2.ConcurrentInvertedIndex;
import com.app.jueee.concurrency.chapter08.common2.QueryResult;

public class ConcurrentSearch5 {


    public static void executorSearch(String[] query, ConcurrentInvertedIndex invertedIndex, ForkJoinPool pool) {
        HashSet<String> set = new HashSet<>(Arrays.asList(query));
        QueryResult results = new QueryResult(new ConcurrentHashMap<>());

        pool.submit(() -> {
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
        }).join();

        System.out.println("Executor Search Ok.");
    
    }
}
