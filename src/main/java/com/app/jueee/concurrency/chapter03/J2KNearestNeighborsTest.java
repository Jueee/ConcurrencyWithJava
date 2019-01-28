package com.app.jueee.concurrency.chapter03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.runner.RunnerException;

import com.app.jueee.concurrency.chapter03.data.BankMarketing;

public class J2KNearestNeighborsTest {

    private int k = 10;
    private int success = 0;
    private int mistakes = 0;
    
    /**
     *  k-最近邻算法：串行版本
     */
    public void serialVersion() {
        List<BankMarketing> train = load("data\\chapter03\\bank.data");
        System.out.println("Train: " + train.size());
        List<BankMarketing> test = load("data\\chapter03\\bank.test");
        System.out.println("Test: " + test.size());
        J2SerialVersionKNearestNeighbors classifier = new J2SerialVersionKNearestNeighbors(train, k);
        long startTime = System.currentTimeMillis();
        for (BankMarketing example : test) {
            String tag = classifier.classify(example);
            if (tag.equals(example.getTag())) {
                success++;
            } else {
                mistakes++;
            }
        }
        System.out.println("k-最近邻算法：串行版本 - \nK: " + k + "    Success: " + success + "  Mistakes: " + mistakes + ",useTime: " + (System.currentTimeMillis()-startTime));
    }
    
    /**
     *  k-最近邻算法：粗粒度并发版本
     *	@throws Exception
     */
    public void concurrentVersionByCoarseGrained() throws Exception {
        List<BankMarketing> train = load("data\\chapter03\\bank.data");
        System.out.println("Train: " + train.size());
        List<BankMarketing> test = load("data\\chapter03\\bank.test");
        System.out.println("Test: " + test.size());
        J2ConcurrentVersionKNearestNeighborsByCoarseGrained classifier = new J2ConcurrentVersionKNearestNeighborsByCoarseGrained(train, k, 1, false);
        long startTime = System.currentTimeMillis();
        for (BankMarketing example : test) {
            String tag = classifier.classify(example);
            if (tag.equals(example.getTag())) {
                success++;
            } else {
                mistakes++;
            }
        }
        System.out.println("k-最近邻算法：粗粒度并发版本-不排序 - \nK: " + k + "    Success: " + success + "  Mistakes: " + mistakes + ",useTime: " + (System.currentTimeMillis()-startTime));
    }
    
    /**
     *  k-最近邻算法：粗粒度并发版本
     *	@throws Exception
     */
    public void concurrentVersionByCoarseGrainedSort() throws Exception {
        List<BankMarketing> train = load("data\\chapter03\\bank.data");
        System.out.println("Train: " + train.size());
        List<BankMarketing> test = load("data\\chapter03\\bank.test");
        System.out.println("Test: " + test.size());
        J2ConcurrentVersionKNearestNeighborsByCoarseGrained classifier = new J2ConcurrentVersionKNearestNeighborsByCoarseGrained(train, k, 1, true);
        long startTime = System.currentTimeMillis();
        for (BankMarketing example : test) {
            String tag = classifier.classify(example);
            if (tag.equals(example.getTag())) {
                success++;
            } else {
                mistakes++;
            }
        }
        System.out.println("k-最近邻算法：粗粒度并发版本-排序 - \nK: " + k + "    Success: " + success + "  Mistakes: " + mistakes + ",useTime: " + (System.currentTimeMillis()-startTime));
    }
    
    /**
     *  k-最近邻算法：细粒度并发版本-排序
     *	@throws Exception
     */
    public void concurrentVersionByFineGrained() throws Exception {
        List<BankMarketing> train = load("data\\chapter03\\bank.data");
        System.out.println("Train: " + train.size());
        List<BankMarketing> test = load("data\\chapter03\\bank.test");
        System.out.println("Test: " + test.size());
        J2ConcurrentVersionKNearestNeighborsByFineGrained classifier = new J2ConcurrentVersionKNearestNeighborsByFineGrained(train, k, 1, false);
        long startTime = System.currentTimeMillis();
        for (BankMarketing example : test) {
            String tag = classifier.classify(example);
            if (tag.equals(example.getTag())) {
                success++;
            } else {
                mistakes++;
            }
        }
        System.out.println("k-最近邻算法：细粒度并发版本-不排序 - \nK: " + k + "    Success: " + success + "  Mistakes: " + mistakes + ",useTime: " + (System.currentTimeMillis()-startTime));
    }
    
    /**
     *  k-最近邻算法：细粒度并发版本-排序
     *	@throws Exception
     */
    public void concurrentVersionByFineGrainedSort() throws Exception {
        List<BankMarketing> train = load("data\\chapter03\\bank.data");
        System.out.println("Train: " + train.size());
        List<BankMarketing> test = load("data\\chapter03\\bank.test");
        System.out.println("Test: " + test.size());
        J2ConcurrentVersionKNearestNeighborsByFineGrained classifier = new J2ConcurrentVersionKNearestNeighborsByFineGrained(train, k, 1, true);
        long startTime = System.currentTimeMillis();
        for (BankMarketing example : test) {
            String tag = classifier.classify(example);
            if (tag.equals(example.getTag())) {
                success++;
            } else {
                mistakes++;
            }
        }
        System.out.println("k-最近邻算法：细粒度并发版本-排序 - \nK: " + k + "    Success: " + success + "  Mistakes: " + mistakes + ",useTime: " + (System.currentTimeMillis()-startTime));
    }
    
    public void test(int num) throws Exception {
        switch (num) {
            case 1:
                serialVersion();
                break;
            case 2:
                concurrentVersionByCoarseGrained();
                break;
            case 3:
                concurrentVersionByCoarseGrainedSort();
                break;
            case 4:
                concurrentVersionByFineGrained();
                break;
            case 5:
                concurrentVersionByFineGrainedSort();
                break;
            default:
                break;
        }
    }
    
    public static void main(String[] args) throws RunnerException {
        J2KNearestNeighborsTest test = new J2KNearestNeighborsTest();
        try {
            test.test(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BankMarketing> load(String dataPath) {
        Path file = Paths.get(dataPath);
//        System.out.println("父路径：" + file.toFile().getAbsolutePath());
        List<BankMarketing> dataSet = new ArrayList<>();
        try (InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String data[] = line.split(";");
                BankMarketing dataObject = new BankMarketing();
                dataObject.setData(data);
                dataSet.add(dataObject);
            }
        } catch (IOException x) {
            x.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSet;
    }
}
