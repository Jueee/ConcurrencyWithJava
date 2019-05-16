package com.app.jueee.concurrency.chapter09.example1;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class J2ConcurrentMainBasicSearch {

    public static void main(String[] args) {
        String query = "Book";
        Path file = Paths.get("data//chapter07","test-amazon.txt");
        try {
            Date start, end;
            start = new Date();
            List<String> results = Files
                    .walk(file, FileVisitOption.FOLLOW_LINKS)
                    .parallel()
                    .filter(f -> f.toString().endsWith(".txt"))
                    .collect(ArrayList<String>::new,
                            new ConcurrentStringAccumulator(query),
                            List::addAll);
            end = new Date();
            System.out.println("Resultados: "+results.size());
            System.out.println("*************");
            results.forEach(System.out::println);
            System.out.println("Execution Time: "+(end.getTime()-start.getTime()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
