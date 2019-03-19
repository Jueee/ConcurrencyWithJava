package com.app.jueee.concurrency.chapter06.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class DataLoader {

    private static final Pattern WHITESPACE = Pattern.compile(",");
    
    /**
     *  从某个文件加载距离矩阵
     *	@param path
     *	@return
     *	@throws IOException
     */
    public static int[][] load(Path path) throws IOException {
        return Files.readAllLines(path)
            .stream()
            .map(line -> WHITESPACE.splitAsStream(line.trim()).mapToInt(Integer::parseInt).toArray())
            .toArray(int[][]::new);
    }
}
