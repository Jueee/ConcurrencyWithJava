package com.app.jueee.concurrency.chapter07.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * 存储有关簇的信息。
 * 
 * @author hzweiyongqiang
 */
public class DocumentCluster {

    private double centroid[];

    private Collection<Document> documents;

    public DocumentCluster(int size, Collection<Document> documents) {
        this.documents = documents;
        centroid = new double[size];
    }

    public void addDocument(Document document) {
        documents.add(document);
    }

    public void clearClusters() {
        documents.clear();
    }

    // 计算簇的质心作为向量的平均值，而这些向量代表所有与该簇相关的文档。
    public void calculateCentroid() {

        Arrays.fill(centroid, 0);

        for (Document document : documents) {
            Word vector[] = document.getData();

            for (Word word : vector) {
                centroid[word.getIndex()] += word.getTfidf();
            }
        }

        for (int i = 0; i < centroid.length; i++) {
            centroid[i] /= documents.size();
        }
    }

    public double[] getCentroid() {
        return centroid;
    }

    public Collection<Document> getDocuments() {
        return documents;
    }

    // 采用随机数来初始化簇向量的质心。
    public void initialize(Random random) {
        for (int i = 0; i < centroid.length; i++) {
            centroid[i] = random.nextDouble();
        }
    }

    public int getDocumentCount() {
        return documents.size();
    }
}
