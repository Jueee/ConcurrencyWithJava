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
            List<Product> productList = Files.walk(file, FileVisitOption.FOLLOW_LINKS)
                    .parallel()
                    .filter(f -> f.toString().endsWith(".txt"))
                    .collect(ArrayList<Product>::new, new ConcurrentLoaderAccumulator(), List::addAll);
            System.out.println("Results:"+productList.size());
            Map<String, List<ProductReview>> productsByBuyer = productList.parallelStream()
                    .<ProductReview>flatMap(p -> p.getReviews().stream().map(r -> new ProductReview(p, r.getUser(), r.getValue())))
                    .collect(Collectors.groupingByConcurrent(p -> p.getBuyer()));
            Map<String, List<ProductReview>> recommendedProducts = productsByBuyer.get(user)
                    .parallelStream()
                    .map(p -> p.getReviews())
                    .flatMap(Collection::stream)
                    .map(r -> r.getUser())
                    .distinct()
                    .map(productsByBuyer::get)
                    .flatMap(Collection::stream)
                    .collect(Collectors.groupingByConcurrent(p -> p.getTitle()));
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
