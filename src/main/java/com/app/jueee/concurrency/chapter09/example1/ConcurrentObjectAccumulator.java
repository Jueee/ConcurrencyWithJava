package com.app.jueee.concurrency.chapter09.example1;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;

import org.apache.commons.lang.StringUtils;

import com.app.jueee.concurrency.chapter09.common1.Product;
import com.app.jueee.concurrency.chapter09.common1.ProductLoader;

public class ConcurrentObjectAccumulator implements BiConsumer<List<Product>, Path>{

    private String word;

    public ConcurrentObjectAccumulator(String word) {
        this.word = word.toLowerCase();
    }
    
    @Override
    public void accept(List<Product> list, Path path) {
        try {
            Product[] products = ProductLoader.load(path);
            System.out.println(path.toString()+" products length:" + products.length);
            
            for (Product product: products) {
                if (StringUtils.isNotBlank(product.getTitle())) {
                    if (product.getTitle().toLowerCase().contains(word.toLowerCase())) {
                        list.add(product);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
