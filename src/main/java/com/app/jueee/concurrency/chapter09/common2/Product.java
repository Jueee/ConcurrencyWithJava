package com.app.jueee.concurrency.chapter09.common2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Product {

    // 商品的唯一标识符
    private String id;
    // 亚马逊的标准身份识别码
    private String asin;
    // 商品的名称
    private String title;
    // 商品的分组
    private String group;
    // 亚马逊公司的销售排名
    private long salesrank;
    // 文件中所包含的相似项的数目
    private String similar;
    // 指派给该商品的类别
    private Set<String> categories;
    // 该商品的评论（用户和评分）
    private List<Review> reviews;
    
    public Product() {
        categories=new HashSet<String>();
        reviews=new ArrayList<Review>();
    }
    
    public void addCategory(String category) {
        categories.add(category);
    }
    
    public void addReview(Review review) {
        reviews.add(review);
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getAsin() {
        return asin;
    }
    
    public void setAsin(String asin) {
        this.asin = asin;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getGroup() {
        return group;
    }
    
    public void setGroup(String group) {
        this.group = group;
    }
    
    public long getSalesrank() {
        return salesrank;
    }
    
    public void setSalesrank(long salesrank) {
        this.salesrank = salesrank;
    }
    
    public String getSimilar() {
        return similar;
    }
    
    public void setSimilar(String similar) {
        this.similar = similar;
    }
    
    public Set<String> getCategories() {
        return categories;
    }
    
    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }
    
    public List<Review> getReviews() {
        return reviews;
    }
    
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
