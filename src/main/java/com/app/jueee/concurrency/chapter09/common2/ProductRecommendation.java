package com.app.jueee.concurrency.chapter09.common2;

public class ProductRecommendation implements Comparable<ProductRecommendation>{

    // 要推荐的商品名称
    private String title;
    // 推荐的分值，这是通过计算商品所有评论的平均分值得到的
    private double value;
    
    public ProductRecommendation(String title, double value) {
        this.title=title;
        this.value=value;
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public double getValue() {
        return value;
    }
    
    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int compareTo(ProductRecommendation o) {
        if (this.getValue() > o.getValue()) { 
            return -1;
        }
        
        if (this.getValue() < o.getValue()) {
            return 1;
        }
        return 0;
    }
    
}
