package com.app.jueee.concurrency.chapter02;

import java.io.File;
import java.util.Date;

/**
 *  串行版本
 *	
 *	@author hzweiyongqiang
 */
public class C3SerialVersionFileSearch {

    public static void searchFiles(File file, String fileName, Result result) {
        File[] contents = file.listFiles();
        if (contents == null || contents.length == 0) {
            return;
        }
        for(File content : contents) {
            if (content.isDirectory()) {
                searchFiles(content, fileName, result);
            } else {
                if (content.getName().equals(fileName)) {
                    result.setPath(content.getAbsolutePath());
                    result.setFound(true);
                    System.out.println(String.format("Serial Search: Path: %s%n", result.getPath()));
                    return;
                }
            }
            if (result.isFound()) {
                return;
            }
        }
    }

    
    /**
     *  测试串行版文件搜索
     *	@param args
     */
    public static void main(String[] args) {
        File file = new File("C:\\Windows\\");
        String regex = "hosts";
        Date start, end;

        Result result = new Result();
        start = new Date();
        C3SerialVersionFileSearch.searchFiles(file, regex, result);
        end = new Date();


        System.out.printf("Serial Search: Execution Time: %d%n", end.getTime() - start.getTime());
    }
    
}
