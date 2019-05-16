package com.app.jueee.concurrency.chapter09.common1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class ProductLoader {

    public static Product[] load(Path path) {
        List<Product> list = new ArrayList<Product>();
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

        Product ret[] = new Product[list.size()];
        return list.toArray(ret);
    }
    
    public static Pattern info_pattern = Pattern.compile("^(.*?)\\:(.*?)$");
    public static Pattern number_pattern = Pattern.compile("^(.*?)\\:(.*?)$");
    public static Pattern categories_pattern = Pattern.compile("\\|(.*?)\\[.*?\\]");
    public static Pattern review_pattern = Pattern.compile("cutomer:(.*?)rating:\\s(\\d+)");
    
    public static String getInfo(String string) {
        Matcher matcher = info_pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }
    
    public static Set<String> getCategoriesInfo(String string) {
        HashSet<String> set = new HashSet<>();
        Matcher matcher = categories_pattern.matcher(string);
        while (matcher.find()) {
            set.add(matcher.group(1).trim());
        }
        return set;
    }
    
    public static Review getReview(String string) {
        Matcher matcher = review_pattern.matcher(string);
        while (matcher.find()) {
            Review review = new Review();
            review.setUser(matcher.group(1).trim());
            review.setValue(Short.valueOf(matcher.group(2).trim()));
            return review;
        }
        return null;
    }

    private static void processItem(List<String> lineList, List<Product> list) {
        Product item = new Product();
        int categoriesNum = 0;
        boolean isCategories = false;
        HashSet<String> categoriesSet = new HashSet<>();
        int iCategories = 0;
        boolean isReviews = false;
        List<Review> reviewsList = new ArrayList<>();
        for (String line: lineList) {
            if (line.startsWith("Id")) {
                item.setId(getInfo(line));
            } else if (line.startsWith("ASIN")) {
                item.setAsin(getInfo(line));
            } else if (line.trim().startsWith("title")) {
                item.setTitle(getInfo(line));
            } else if (line.trim().startsWith("group")) {
                item.setGroup(getInfo(line));
            } else if (line.trim().startsWith("salesrank")) {
                item.setSalesrank(Long.valueOf(getInfo(line)));
            } else if (line.trim().startsWith("categories")) {
                categoriesNum = Integer.valueOf(getInfo(line));
                isCategories = true;
            } else if (line.trim().startsWith("reviews")) {
                isReviews = true;
            }
            if (isCategories) {
                if (iCategories > 0 && iCategories <= categoriesNum) {
                    categoriesSet.addAll(getCategoriesInfo(line));
                }
                iCategories++;
            }
            if (isReviews) {
                Review review = getReview(line);
                if (review != null) {
                    reviewsList.add(review);
                }
            }
        }
        if (StringUtils.isNotBlank(item.getId())) {
            item.setCategories(categoriesSet);
            item.setReviews(reviewsList);
            list.add(item);
        }
    }
    
    public static void main(String[] args) {
        Path path = Paths.get("data//chapter07","test-amazon.txt");
        Product[] data = ProductLoader.load(path);
        System.out.println(data.length);
        for (int i = 0; i < 6; i++) {
            System.out.println(data[i].getCategories());
            System.out.println(data[i].getReviews());
        }
    }
}
