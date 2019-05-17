package com.app.jueee.concurrency.chapter09.common2;

public class ProductReview extends Product{

    // 存放了商品客户的名称
    private String buyer;
    // 存放了该客户在其评论中对商品的评价
    private short value;

    public ProductReview(Product source, String buyer, short value) {
        this.setId(source.getId());
        this.setAsin(source.getAsin());
        this.setTitle(source.getTitle());
        this.setGroup(source.getGroup());
        this.setSalesrank(source.getSalesrank());
        this.setSimilar(source.getSimilar());
        this.setCategories(source.getCategories());
        this.setReviews(source.getReviews());
        this.buyer=buyer;
        this.value=value;
    }
    
    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }
    
}
