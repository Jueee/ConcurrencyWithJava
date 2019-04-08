package com.app.jueee.concurrency.chapter07;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.app.jueee.concurrency.chapter07.common.CensusData;
import com.app.jueee.concurrency.chapter07.common.CensusDataLoader;
import com.app.jueee.concurrency.chapter07.common.Filter;
import com.app.jueee.concurrency.chapter07.common.FilterData;

public class J3DataFilterSerialMain {

    /**
     * 用于查找满足筛选器条件的第一个数据对象
     * 
     * @param data
     * @param filters
     * @return
     */
    public static CensusData findAny(CensusData[] data, List<FilterData> filters) {
        int index = 0;

        for (CensusData censusData : data) {
            if (Filter.filter(censusData, filters)) {
                System.out.println("Found:" + index);
                return censusData;
            }
            index++;
        }

        return null;
    }

    /**
     * 用于查找满足筛选器条件的所有数据对象
     * 
     * @param data
     * @param filters
     * @return
     */
    public static List<CensusData> findAll(CensusData[] data, List<FilterData> filters) {
        List<CensusData> results = new ArrayList<CensusData>();

        for (CensusData censusData : data) {
            if (Filter.filter(censusData, filters)) {
                results.add(censusData);
            }
        }

        return results;
    }

    public static void main(String[] args) {
        // 从文件加载数据
        Path path = Paths.get("data", "chapter07//census-income.data");
        CensusData data[] = CensusDataLoader.load(path);
        System.out.println("Number of items: " + data.length);
        Date start, end;

        // 一、使用 findAny() 方法查找出现在数组中第一个位置的对象
        List<FilterData> filters = new ArrayList<>();
        FilterData filter = new FilterData();
        filter.setIdField(32);
        filter.setValue("Dominican-Republic");
        filters.add(filter);
        filter = new FilterData();
        filter.setIdField(31);
        filter.setValue("Dominican-Republic");
        filters.add(filter);
        filter = new FilterData();
        filter.setIdField(1);
        filter.setValue("Not in universe");
        filters.add(filter);
        filter = new FilterData();
        filter.setIdField(14);
        filter.setValue("Not in universe");
        filters.add(filter);
        start = new Date();
        CensusData result = findAny(data, filters);
        System.out.println("Test 1 - Result: " + result.getReasonForUnemployment());
        end = new Date();
        System.out.println("Test 1- Execution Time: " + (end.getTime() - start.getTime()));

        // 二、使用 findAny() 方法查找出现在数组中最后一个位置的对象
        filters = new ArrayList<>();
        filter = new FilterData();
        filter.setIdField(32);
        filter.setValue("United-States");
        filters.add(filter);
        filter = new FilterData();
        filter.setIdField(31);
        filter.setValue("Greece");
        filters.add(filter);
        filter = new FilterData();
        filter.setIdField(1);
        filter.setValue("Private");
        filters.add(filter);
        filter = new FilterData();
        filter.setIdField(14);
        filter.setValue("Not in universe");
        filters.add(filter);
        filter = new FilterData();
        filter.setIdField(0);
        filter.setValue("62");
        filters.add(filter);

        start = new Date();
        result = findAny(data, filters);
        System.out.println("Test 2 - Result: " + result.getReasonForUnemployment());
        end = new Date();
        System.out.println("Test 2- Execution Time: " + (end.getTime() - start.getTime()));

        // 三、使用 findAny() 方法尝试查找某个并不存在的对象
        filters = new ArrayList<>();
        filter = new FilterData();
        filter.setIdField(32);
        filter.setValue("XXXX");
        filters.add(filter);

        start = new Date();
        result = findAny(data, filters);
        if (result == null) {
            System.out.println("Test 3 - Result: " + result);
        } else {
            System.out.println("Test 3 - Result: " + result.getReasonForUnemployment());
        }
        end = new Date();
        System.out.println("Test 3 - Execution Time: " + (end.getTime() - start.getTime()));

        // 四、在错误情境中使用 findAny() 方法
        filters = new ArrayList<>();
        filter = new FilterData();
        filter.setIdField(0);
        filter.setValue("Dominican-Republic");
        filters.add(filter);

        start = new Date();
        try {
            result = findAny(data, filters);
            System.out.println("Test 4 - Results: " + result.getCitizenship());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        end = new Date();
        System.out.println("Test 4 - Execution Time: " + (end.getTime() - start.getTime()));

        // 五、使用 findAll() 方法获取满足筛选器列表条件的所有对象
        filters = new ArrayList<>();
        filter = new FilterData();
        filter.setIdField(32);
        filter.setValue("Dominican-Republic");
        filters.add(filter);
        filter = new FilterData();
        filter.setIdField(31);
        filter.setValue("Dominican-Republic");
        filters.add(filter);
        filter = new FilterData();
        filter.setIdField(1);
        filter.setValue("Not in universe");
        filters.add(filter);
        filter = new FilterData();
        filter.setIdField(14);
        filter.setValue("Not in universe");
        filters.add(filter);

        start = new Date();
        List<CensusData> results = findAll(data, filters);
        System.out.println("Test 5 - Results: " + results.size());
        end = new Date();
        System.out.println("Test 5 - Execution Time: " + (end.getTime() - start.getTime()));

        // 六、在错误情境中使用 findAll() 方法
        filters = new ArrayList<FilterData>();
        filter = new FilterData();
        filter.setIdField(0);
        filter.setValue("Dominican-Republic");
        filters.add(filter);

        start = new Date();
        try {
            results = findAll(data, filters);
            System.out.println("Test 6 - Results: " + results.size());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        end = new Date();
        System.out.println("Test 6 - Execution Time: " + (end.getTime() - start.getTime()));
    }
}
