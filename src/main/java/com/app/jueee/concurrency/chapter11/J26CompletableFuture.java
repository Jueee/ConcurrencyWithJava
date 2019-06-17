package com.app.jueee.concurrency.chapter11;

import java.io.BufferedWriter;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.app.jueee.concurrency.chapter09.common1.Product;
import com.app.jueee.concurrency.chapter09.common1.ProductLoader;


public class J26CompletableFuture {

    
    public static void main(String[] args) {
        Path file = Paths.get("data//chapter07","test-amazon.txt");
        System.out.println(new Date() + ": Main: Loading products after three seconds....");
        
        // 使用 CompletableFuture 类的 supplyAsync() 方法执行 LoadTask 。
        // 在 LoadTask 开始之前将等待 3 秒，以展示 delayExecutor() 方法如何工作。
        LoadTask loadTask = new LoadTask(file);
        CompletableFuture<List<Product>> loadFuture = CompletableFuture.supplyAsync(loadTask, 
            CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS));
        
        // 在加载任务完成之后使用 thenApplyAsync() 执行搜索任务。
        System.out.println(new Date() + ": Main: Then apply for search");
        CompletableFuture<List<Product>> completableSearch = loadFuture.thenApplyAsync(new SearchTask("of"));
        
        // 一旦搜索任务完成，就要将执行结果输出到一个文件。
        CompletableFuture<Void> completableWrite = completableSearch.thenAcceptAsync(new WriteTask());
        completableWrite.exceptionally(ex -> {  // 如果写入任务抛出异常，那么使用 exceptionally() 方法指定要做的事项
            System.out.println(new Date() + ": Main: Exception " + ex.getMessage());
            return null;
        });
        
        // 使用 thenApplyAsync() 方法执行该任务，以便获取购买某一商品的用户列表。
        // 请注意，该任务并不会与搜索任务并行执行。
        System.out.println(new Date() + ": Main: Then apply for users");
        CompletableFuture<List<String>> completableUsers = loadFuture.thenApplyAsync(resultList -> {
            System.out.println(new Date()+ ": Main: Completable users: start");
            List<String> users = resultList.stream()
                .flatMap(p -> p.getReviews().stream())
                .map(review -> review.getUser())
                .distinct()
                .collect(Collectors.toList());
            System.out.println(new Date() + ": Main: Completable users: end");
            return users;
        });
        
        // 使用 thenApplyAsync() 方法执行了该任务，以便查找评价最高的商品和销量最佳的商品。
        System.out.println(new Date() + ": Main: Then apply for best rated product....");
        CompletableFuture<Product> completableProduct = loadFuture.thenApplyAsync(resultList -> {
            Product maxProduct = null;
            double maxScore = 0.0;
            System.out.println(new Date() + ": Main: Completable product: start");
            for (Product product : resultList) {
                if (!product.getReviews().isEmpty()) {
                    double score = product.getReviews().stream()
                    .mapToDouble(review -> review.getValue())
                    .average().getAsDouble();
                    if (score > maxScore) {
                        maxProduct = product;
                        maxScore = score;
                    }
                }
            }
            System.out.println(new Date() + ": Main: Completable product : end");
            return maxProduct;
        });
        
        System.out.println(new Date() + ": Main: Then apply for best selling product....");
        CompletableFuture<Product> completableBestSellingProduct = loadFuture.thenApplyAsync(resultList -> {
            System.out.println(new Date() + ": Main: Completable best selling: start");
            Product bestProduct = resultList.stream()
                .min(Comparator.comparingLong(Product::getSalesrank))
                .orElse(null);
            System.out.println(new Date() + ": Main: Completable best selling: end");
            return bestProduct;
        });
        
        // 将前两个任务的结果连到一起。可以使用 thenCombineAsync() 方法完成这一工作，用它指定一个将在这两个任务完成之后执行的任务。
        CompletableFuture<String> completableProductResult = completableBestSellingProduct.thenCombineAsync(
            completableProduct, (bestSellingProduct, bestRatedProduct) -> {
            System.out.println(new Date() + ": Main: Completable product result: start");
            String ret = "The best selling product is " + bestSellingProduct.getTitle() + "\n";
            ret += "The best rated product is " + bestRatedProduct.getTitle();
            System.out.println(new Date() + ": Main: Completable product result: end");
            return ret;
        });
        
        // 使用 completeOnTimeout() 方法预留 1 秒钟，以等待 completableProductResult 任务完成。
        // 如果它在 1 秒之内没有完成，那么完成 CompletableFuture ，并得出结果 TimeOut 。
        // 然后，使用 allOf() 方法和 join() 方法等待最终任务结束，并且输出使用 get() 方法获得的结果。
        System.out.println(new Date() + ": Main: Waiting for results");
        completableProductResult.completeOnTimeout("TimeOut", 1, TimeUnit.SECONDS);
        CompletableFuture<Void> finalCompletableFuture = CompletableFuture.allOf(completableProductResult, completableUsers, completableWrite);
        finalCompletableFuture.join();
        try {
            System.out.println("Number of loaded products: " + loadFuture.get().size());
            System.out.println("Number of found products: " + completableSearch.get().size());
            System.out.println("Number of users: " + completableUsers.get().size());
            System.out.println("Best rated product: " + completableProduct.get().getTitle());
            System.out.println("Best selling product: " + completableBestSellingProduct.get().getTitle());
            System.out.println("Product result:" + completableProductResult.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// 用于从磁盘加载商品信息并且返回一个 Product 对象列表。
class LoadTask implements Supplier<List<Product>>{
    private Path path;
    public LoadTask(Path path) {
        this.path = path;
    }
    @Override
    public List<Product> get() {
        List<Product> products = new ArrayList<Product>();
        try {
            List<List<Product>> productList = Files.walk(path, FileVisitOption.FOLLOW_LINKS)
                .parallel()
                .filter(f -> f.toString().endsWith(".txt"))
                .map(ProductLoader::loadList)
                .collect(Collectors.toList());
            productList.forEach(p -> products.addAll(p));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
}

// 实现对 Product 对象列表的搜索，查找在名称中含有某一单词的对象。
class SearchTask implements Function<List<Product>, List<Product>> {
    private String query;
    public SearchTask(String query) {
        this.query = query;
    }
    @Override
    public List<Product> apply(List<Product> t) {
        System.out.println(new Date() + ": CompletableTask:start");
        List<Product> ret = t.stream()
            .filter(product -> product.getTitle()!=null && product.getTitle().toLowerCase().contains(query))
            .collect(Collectors.toList());
        System.out.println(new Date() + ": CompletableTask:end:"+ret.size());
        return ret;
    }
    
}

// 将搜索任务中获得的商品写入一个 File 对象。
class WriteTask implements Consumer<List<Product>> {

    @Override
    public void accept(List<Product> products) {
        Path path = Paths.get("output\\results.html");
        System.out.println(new Date()+": WriteTask: start");
        try (BufferedWriter fileWriter = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
            fileWriter.write("<HTML>");
            fileWriter.write("<HEAD>");
            fileWriter.write("<TITLE>");
            fileWriter.write("Search Results");
            fileWriter.write("</TITLE>");
            fileWriter.write("</HEAD>");
            fileWriter.write("<BODY>");
            fileWriter.newLine();
            fileWriter.write("<UL>");
            for (Product product : products) {
                fileWriter.write("<LI>"+product.getTitle()+"</LI>");
            }
            fileWriter.write("</UL>");
            fileWriter.write("</BODY>");
            fileWriter.write("</HTML>");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Testing error management");
        }
        System.out.println(new Date()+": WriteTask: end");
    }

}