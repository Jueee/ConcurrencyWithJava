package com.app.jueee.concurrency.chapter07.common1;

/**
 *  存放了有关文档的所有信息。
 *	
 *	@author hzweiyongqiang
 */
public class Document {

    // 存放文档中单词
    private Word[] data;
    // 存放文档的文件名
    private String name;
    // 表示与该文档相关的簇
    private DocumentCluster cluster;

    public Document(String name, int size) {
        this.name = name;
        data = new Word[size];
        cluster = null;
    }

    public Word[] getData() {
        return data;
    }

    public void setData(Word[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentCluster getCluster() {
        return cluster;
    }

    // 返回一个布尔值，这个值表明属性的新值是与旧值相同，还是一个新值。我们将用该值判定是否应该停止算法。
    public boolean setCluster(DocumentCluster cluster) {
        if (this.cluster == cluster) {
            return false;
        } else {
            this.cluster = cluster;
            return true;
        }
    }

}
