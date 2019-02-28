package com.app.jueee.concurrency.chapter05.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于将单词列表加载到字符串对象列表中。
 * 
 * @author hzweiyongqiang
 */
public class WordsLoader {

    public static List<String> load(String path) {
        Path file = Paths.get(path);
        List<String> data = new ArrayList<String>();
        try (InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException x) {
            x.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}
