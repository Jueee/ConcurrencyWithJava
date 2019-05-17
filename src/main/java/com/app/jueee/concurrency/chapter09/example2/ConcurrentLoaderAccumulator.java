package com.app.jueee.concurrency.chapter09.example2;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;

import com.app.jueee.concurrency.chapter09.common2.Product;
import com.app.jueee.concurrency.chapter09.common2.ProductLoader;


public class ConcurrentLoaderAccumulator implements BiConsumer<List<Product>, Path>{

    @Override
    public void accept(List<Product> list, Path path) {
        try {
            Product[] products = ProductLoader.load(path);
            System.out.println(path.toString()+" products length:" + products.length);
            
            for (Product product: products) {
                list.add(product);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
