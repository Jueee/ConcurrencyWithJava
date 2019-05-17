package com.app.jueee.concurrency.chapter09.example2;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import com.app.jueee.concurrency.chapter09.common2.Product;
import com.app.jueee.concurrency.chapter09.common2.ProductRecommendation;
import com.app.jueee.concurrency.chapter09.common2.ProductReview;

public class J3ConcurrentMainRecommendation {

    
    public static void main(String[] args) {
        String user = "A11NCO6YTE4BTJ";
        Path file = Paths.get("data//chapter07", "test-amazon.txt");

        try {
            Date start, end;
            start = new Date();
            
            
            // 1. 从其文件中加载整个 Product 对象列表。
            List<Product> productList = Files.walk(file, FileVisitOption.FOLLOW_LINKS)  // 启动流。该方法将创建一个流来处理 Data 目录下的所有文件和目录。
                    .parallel()     // 将该流转换成一个并发流
                    .filter(f -> f.toString().endsWith(".txt"))     // 仅获取扩展名为 .txt 的文件
                    .collect(ArrayList<Product>::new, new ConcurrentLoaderAccumulator(), List::addAll); // 使用 collect() 方法获取 Product 对象的 ConcurrentLinkedDeque 类
            System.out.println("Product Results:"+productList.size());

            
            // 2. 为每个商品评论创建一个 ProductReview 对象。
            Map<String, List<ProductReview>> productsByBuyer = productList.parallelStream()
                    .<ProductReview>flatMap(p -> p.getReviews().stream().map(r -> new ProductReview(p, r.getUser(), r.getValue()))) // 将现有的 Product 对象流转换成一个唯一的 ProductReview 对象流
                    .collect(Collectors.groupingByConcurrent(p -> p.getBuyer()));   // 生成最后的 Map
            System.out.println("ProductReview Results:"+productList.size());
            
            
            // 3. 选定某个客户购买的商品并且生成对该客户的推荐。
            // 第一个阶段，获取购买了原客户所购买商品的用户。
            // 第二个阶段，生成一个 Map ，其中含有这些客户所购买的商品，以及这些客户针对商品所做的评论。
            Map<String, List<ProductReview>> recommendedProducts = productsByBuyer.get(user)
                    .parallelStream()   // 获取用户所购买商品的列表，并且使用 parallelStream() 方法来生成一个并发流。
                    .map(p -> p.getReviews())   // 获取所有有关这些商品的评论
                    .flatMap(Collection::stream)    // 将该流转换成一个 Review 对象流
                    .map(r -> r.getUser())  // 将该流转换成一个 String 对象流，其中含有提交这些评论的用户的名称。
                    .distinct()     // 获取唯一的用户名称
                    .map(productsByBuyer::get)      // 将每个客户与其已购商品列表对应起来
                    .flatMap(Collection::stream)    // 此时，就有了一个 List<ProductReview> 对象流。
                    .collect(Collectors.groupingByConcurrent(p -> p.getTitle()));   // 生成一个商品 Map
            
            
            //  4. 对于每个商品，都希望计算其在评论中的平均分值，并且按照降序对该列表进行排序，以便将排在前面的商品放在首要位置显示。
            ConcurrentLinkedDeque<ProductRecommendation> recommendations = recommendedProducts.entrySet()
                    .parallelStream()
                    .map(entry -> new ProductRecommendation(entry.getKey(), entry.getValue().stream().mapToInt(p -> p.getValue()).average().getAsDouble()))
                    .sorted()
                    .collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
            
            
            end = new Date();
            System.out.println("*************");
            recommendations.forEach(pr -> System.out.println(pr.getTitle() + " - " + pr.getValue()));
            System.out.println("*************");
            System.out.println("Execution Time: " + (end.getTime() - start.getTime()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
