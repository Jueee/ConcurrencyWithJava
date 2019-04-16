package com.app.jueee.concurrency.chapter07.common3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmazonMetaDataLoader {

    public static AmazonMetaData[] load(Path path) {
        List<AmazonMetaData> list = new ArrayList<AmazonMetaData>();
        List<String> lines = new ArrayList<>();
        try (InputStream in = Files.newInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    processItem(lines, list);
                    lines.clear();
                    break;
                }
                if (line.equals("")) {
                    processItem(lines, list);
                    lines.clear();
                }
                lines.add(line);
            }
        } catch (IOException x) {
            x.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AmazonMetaData ret[] = new AmazonMetaData[list.size()];
        return list.toArray(ret);
    }
    
    public static Pattern info_pattern = Pattern.compile("^(.*?)\\:(.*?)$");
    public static Pattern number_pattern = Pattern.compile("^(.*?)\\:(.*?)$");
    
    public static String getInfo(String string) {
        Matcher matcher = info_pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }

    private static void processItem(List<String> lineList, List<AmazonMetaData> list) {
        AmazonMetaData item = new AmazonMetaData();
        for (String line: lineList) {
            if (line.startsWith("Id")) {
                item.setId(Integer.valueOf(getInfo(line)));
            }
            if (line.startsWith("ASIN")) {
                item.setASIN(getInfo(line));
            }
            if (line.trim().startsWith("title")) {
                item.setTitle(getInfo(line));
            }
            if (line.trim().startsWith("group")) {
                item.setGroup(getInfo(line));
            }
            if (line.trim().startsWith("salesrank")) {
                item.setSalesrank(Long.valueOf(getInfo(line)));
            }
            if (line.trim().startsWith("categories")) {
                item.setCategories(Integer.valueOf(getInfo(line)));
            }
        }
        if (item.getId() != 0) {
            list.add(item);
        }
    }

    private static AmazonMetaData processItem(String line) {
        AmazonMetaData item = new AmazonMetaData();
        String tokens[] = line.split(";;");
        if (tokens.length != 8) {
            System.err.println("Error: " + line);
            System.err.println("Tokens: " + tokens.length);
            System.exit(-1);
        }

        item.setId(Integer.valueOf(tokens[0]));
        item.setASIN(tokens[1]);
        item.setTitle(tokens[2]);
        item.setGroup(tokens[3]);
        item.setSalesrank(Long.valueOf(tokens[4]));
        item.setReviews(Integer.valueOf(tokens[5]));
        item.setSimilar(Integer.valueOf(tokens[6]));
        item.setCategories(Integer.valueOf(tokens[7]));

        return item;
    }
    
    public static void main(String[] args) {
        Path path = Paths.get("data//chapter07","test-amazon.txt");
        AmazonMetaData[] data = AmazonMetaDataLoader.load(path);
        System.out.println(data.length);
        for (int i = 0; i < 5; i++) {
            System.out.println(data[i]);
        }
    }
}
