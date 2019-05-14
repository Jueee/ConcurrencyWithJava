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

public class ConcurrentSearch1 {

    public static void basicSearch(String[] query) throws IOException {
        Path path = Paths.get("data/chapter08", "salida.txt");
        HashSet<String> set = new HashSet<>(Arrays.asList(query));
        QueryResult results = new QueryResult(new ConcurrentHashMap<>());
        try (Stream<String> invertedIndex = Files.lines(path)) {
            invertedIndex.parallel()    // 获取一个并行流以提高搜索过程的性能
                .filter(line -> set.contains(Utils.getWord(line)))  // 选取将集合中单词与查询中单词相关联的行。 Utils.getWord() 方法将获取该                行的单词。
                .flatMap(ConcurrentSearch1::basicMapper)     // 将字符串流转换成一个 Token 对象流。
                .forEach(results::append);  // 使用该类的 add() 方法添加每个 Token 对象，进而生成 QueryResult 对象
            results.getAsList()     //  QueryResult 对象返回一个含有相关文档的列表
                .stream()       // 创建一个处理该列表的流
                .sorted()       // 按照文档的 tfxidf 值排列文档列表
                .limit(100)     // 获得前 100 个结果
                .forEach(System.out::println);  // 处理 100 个结果并且将信息输出到屏幕
            System.out.println("Basic Search Ok");
        }
    }
    
    /**
     *  将一个字符串流转换成一个 Token 对象流
     *	@param input
     *	@return
     */
    public static Stream<Token> basicMapper(String input) {
        ConcurrentLinkedDeque<Token> list = new ConcurrentLinkedDeque();
        String word = Utils.getWord(input);
        Arrays.stream(input.split(",")) // 使用 split() 方法分割字符串，并且使用 Arrays 类的 stream() 方法生成流
              .skip(1)  // 跳过第一个元素
              .parallel()
              .forEach(token -> list.add(new Token(word, token)));

        return list.stream();
    }
    
}
