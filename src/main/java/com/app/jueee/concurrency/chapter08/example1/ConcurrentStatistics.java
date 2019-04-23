package com.app.jueee.concurrency.chapter08.example1;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConcurrentStatistics {

    /**
     *  获得每位英国客户订购的产品数量
     *	@param records
     */
    public static void customersFromUnitedKingdom(List<Record> records) {
        System.out.println("**************************************");
        System.out.println("Customers from UnitedKingdom");
        ConcurrentMap<String, List<Record>> map = records.parallelStream()
            .filter(r -> r.getCountry().equals("the United Kingdom"))
            .collect(Collectors.groupingByConcurrent(Record::getCustomer));
        map.forEach((k, l) -> System.out.println(k + ": " + l.size()));
        System.out.println("**************************************");
    }
    
    /**
     *  获得来自英国的订单的产品数量的统计信息（最大值、最小值和平均值）
     *	@param records
     */
    public static void quantityFromUnitedKingdom(List<Record> records) {
        System.out.println("****************************************");
        System.out.println("Quantity from the United Kingdom");
        DoubleSummaryStatistics statistics = records.parallelStream()
            .filter(r -> r.getCountry().equals("the United Kingdom"))
            .collect(Collectors.summarizingDouble(Record::getQuantity));
        
        System.out.println("Min: " + statistics.getMin());
        System.out.println("Max: " + statistics.getMax());
        System.out.println("Average: " + statistics.getAverage());
        System.out.println("****************************************");
    }
    
    /**
     *  获取订购了 ID 为 85123A 的产品的国家列表
     *	@param records
     */
    public static void countriesForProduct(List<Record> records) {
        System.out.println("****************************************");
        System.out.println("Countries for product 85123A");
        records.parallelStream()
            .filter(r -> r.getStockCode().equals("85123A"))
            .map(r -> r.getCountry())
            .distinct()     // 仅选取唯一值
            .sorted()       // 按照字母顺序对这些值进行排序
            .forEachOrdered(System.out::println);
        System.out.println("****************************************");
    }

    /**
     *  获取 ID 为 85123A 的产品记录相关的最大和最小产品数目
     *	@param records
     */
    public static void quantityForProduct(List<Record> records) {
        System.out.println("****************************************");
        System.out.println("Quantity for Product");
        int value = records.parallelStream()
            .filter(r -> r.getStockCode().equals("85123A"))
            .mapToInt(r -> r.getQuantity())
            .max()
            .getAsInt();
        System.out.println("Max quantity: " + value);
        value = records.parallelStream()
            .filter(r -> r.getStockCode().equals("85123A"))
            .mapToInt(r -> r.getQuantity())
            .min()
            .getAsInt();
        System.out.println("Min quantity: " + value);
        System.out.println("****************************************");
    }

    /**
     *  多个数据筛选器
     *  该方法的主要目标是获取至少满足如下条件之一的记录数。
     *  1、quantity 属性值大于 50 的记录数。
     *  2、unitPrice 属性值大于 10 的记录数。
     *	@param records
     */
    public static void multipleFilterData(List<Record> records) {
        System.out.println("****************************************");
        System.out.println("Multiple Filter");
        Stream<Record> stream1 = records.parallelStream().filter(r -> r.getQuantity() > 50);
        Stream<Record> stream2 = records.parallelStream().filter(r -> r.getUnitPrice() > 10);
        Stream<Record> complete = Stream.concat(stream1, stream2);
        Long value = complete.parallel().unordered().map(r -> r.getStockCode()).distinct().count();
        System.out.println("Number of products: " + value);
        System.out.println("****************************************");
    }

    /**
     *  以更优的方式实现同样的结果
     *	@param records
     */
    public static void multipleFilterDataPredicate(List<Record> records) {
        System.out.println("****************************************");
        System.out.println("Multiple filter with Predicate");
        Predicate<Record> p1 = r -> r.getQuantity() > 50;
        Predicate<Record> p2 = r -> r.getUnitPrice() > 10;
        Predicate<Record> pred = Stream.of(p1, p2).reduce(Predicate::or).get();
        long value = records.parallelStream().filter(pred).count();
        System.out.println("Number of products: " + value);
        System.out.println("****************************************");
    }
    /**
     *  获取发货量最高的 10 张发货单
     *	@param records
     */
    public static void getBiggestInvoiceAmmounts(List<Record> records) {
        System.out.println("****************************************");
        System.out.println("Biggest Invoice Ammounts");
        ConcurrentMap<String, List<Record>> map = records.stream()
            .unordered()
            .parallel()
            .collect(Collectors.groupingByConcurrent(r -> r.getId()));
        ConcurrentLinkedDeque<Invoice> invoices = new ConcurrentLinkedDeque();
        map.values().parallelStream().forEach(list -> {
            Invoice invoice = new Invoice();
            invoice.setId(list.get(0).getId());
            double ammount = list.stream().mapToDouble(r -> r.getUnitPrice() * r.getQuantity()).sum();
            invoice.setAmmount(ammount);
            invoice.setCustomerId(list.get(0).getCustomer());
            invoices.add(invoice);
        });
        System.out.println("Invoices: "+invoices.size()+": "+map.getClass());
        invoices.stream()
            .sorted(Comparator.comparingDouble(Invoice::getAmmount).reversed())
            .limit(10)
            .forEach(i -> System.out.println("Customer:"+i.getCustomerId() + "; Ammount: "+ i.getAmmount()));
        System.out.println("****************************************");
    }
    
    /**
     *  获取文件中单价在 1 到 10 之间的产品数
     *	@param records
     */
    public static void productsBetween1and10(List<Record> records) {
        System.out.println("****************************************");
        System.out.println("Products between 1 and 10");
        int count = records.stream()
            .unordered()
            .parallel()
            .filter(r -> (r.getUnitPrice() >= 1) && (r.getUnitPrice() <= 10))
            .map(i -> i.getStockCode())
            .distinct()
            .mapToInt(a -> 1)
            .reduce(0, Integer::sum);
        System.out.println("Products between 1 and 10: " + count);
        System.out.println("****************************************");
    }
}
