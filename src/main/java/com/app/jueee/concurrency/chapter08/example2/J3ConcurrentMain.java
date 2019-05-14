package com.app.jueee.concurrency.chapter08.example2;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.ForkJoinPool;

import com.app.jueee.concurrency.chapter08.common2.ConcurrentData;
import com.app.jueee.concurrency.chapter08.common2.ConcurrentInvertedIndex;

public class J3ConcurrentMain {

    public static void main(String[] args) throws IOException {

        String[] query1 = {"james", "bond"};
        String[] query2 = {"gone", "with", "the", "wind"};
        String[] query3 = {"rocky"};
        StringBuffer bufferResults = new StringBuffer();

        Date start, end;

        bufferResults.append("Version 1, query 1, concurrent\n");
        start = new Date();
        ConcurrentSearch1.basicSearch(query1);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 1, query 2, concurrent\n");
        start = new Date();
        ConcurrentSearch1.basicSearch(query2);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 1, query 3, concurrent\n");
        start = new Date();
        ConcurrentSearch1.basicSearch(query3);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 2, query 1, concurrent\n");
        start = new Date();
        ConcurrentSearch2.reducedSearch(query1);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 2, query 2, concurrent\n");
        start = new Date();
        ConcurrentSearch2.reducedSearch(query2);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 2, query 3, concurrent\n");
        start = new Date();
        ConcurrentSearch2.reducedSearch(query3);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 3, query 1, concurrent\n");
        start = new Date();
        ConcurrentSearch3.htmlSearch(query1, "query1_concurrent");
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 3, query 2, concurrent\n");
        start = new Date();
        ConcurrentSearch3.htmlSearch(query2, "query2_concurrent");
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 3, query 3, concurrent\n");
        start = new Date();
        ConcurrentSearch3.htmlSearch(query3, "query3_concurrent");
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        ConcurrentInvertedIndex invertedIndex = new ConcurrentInvertedIndex();
        ConcurrentFileLoader loader = new ConcurrentFileLoader();
        invertedIndex = loader.load(Paths.get("index", "invertedIndex.txt"));

        bufferResults.append("Version 4, query 1, concurrent\n");
        start = new Date();
        ConcurrentSearch4.preloadSearch(query1, invertedIndex);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 4, query 2, concurrent\n");
        start = new Date();
        ConcurrentSearch4.preloadSearch(query2, invertedIndex);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 4, query 3, concurrent\n");
        start = new Date();
        ConcurrentSearch4.preloadSearch(query3, invertedIndex);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        ForkJoinPool pool = new ForkJoinPool();

        bufferResults.append("Version 5, query 1, concurrent\n");
        start = new Date();
        ConcurrentSearch5.executorSearch(query1, invertedIndex, pool);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 5, query 2, concurrent\n");
        start = new Date();
        ConcurrentSearch5.executorSearch(query2, invertedIndex, pool);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Version 5, query 3, concurrent\n");
        start = new Date();
        ConcurrentSearch5.executorSearch(query3, invertedIndex, pool);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Inverted Index Data: Words in File version 1\n");
        start = new Date();
        ConcurrentData.getWordsInFile1("27759897.txt", invertedIndex);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Inverted Index Data: Words in File version 2\n");
        start = new Date();
        ConcurrentData.getWordsInFile2("27759897.txt", invertedIndex);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Inverted Index Data: Average Tfxidf in File \n");
        start = new Date();
        ConcurrentData.getAverageTfxidf("27759897.txt", invertedIndex);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Inverted Index Data: Max Tfxidf\n");
        start = new Date();
        ConcurrentData.maxTfxidf(invertedIndex);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        bufferResults.append("Inverted Index Data: Min Tfxidf\n");
        start = new Date();
        ConcurrentData.minTfxidf(invertedIndex);
        end = new Date();
        bufferResults.append("Execution Time: " + (end.getTime() - start.getTime()) + "\n");

        System.out.println("***************************");
        System.out.println(bufferResults.toString());
        System.out.println("***************************");

    }
}
