package com.app.jueee.concurrency.chapter07.common3;

import net.sf.json.JSONObject;

public class AmazonMetaData implements Comparable<AmazonMetaData> {

    private int id;
    private String ASIN;
    private String title;
    private String group;
    private long salesrank;
    private int reviews;
    private int similar;
    private int categories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getASIN() {
        return ASIN;
    }

    public void setASIN(String aSIN) {
        ASIN = aSIN;
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

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public int getSimilar() {
        return similar;
    }

    public void setSimilar(int similar) {
        this.similar = similar;
    }

    public int getCategories() {
        return categories;
    }

    public void setCategories(int categories) {
        this.categories = categories;
    }

    // 比较两个对象的销售排名
    @Override
    public int compareTo(AmazonMetaData other) {
        return Long.compare(this.getSalesrank(), other.getSalesrank());
    }
    
    @Override
    public String toString() {
        return JSONObject.fromObject(this).toString();
    }
}
