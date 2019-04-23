package com.app.jueee.concurrency.chapter08.example1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ConcurrentDataLoader {

    public static List<Record> load(Path path) throws IOException {
        System.out.println("Loading data by:" + path.toFile().getAbsolutePath());

        // 使用 Files 类的 readAllLines() 方法加载该文件
        List<String> lines = Files.readAllLines(path);

        List<Record> records = lines
                .parallelStream()               // 创建一个并行流来处理该文件的所有行
                .skip(1)                        // 忽略该流的第一项；在本例中，即文件的第一行，其中包含了文件的头信息
                .map(l -> l.split(";"))         // 对 String[] 数组中的各个字符串进行转换，用 ; 字符分割各行。
                .map(t -> new Record(t))        // 使用 Record 类的构造函数将每个字符串数组转换成一个  Record 对象。
                .collect(Collectors.toList());

        return records;
    }
    
    public static void main(String[] args) {
        try {
            List<Record> records = load(Paths.get("data//chapter08//Online Retail.csv"));
            System.out.println(records.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
