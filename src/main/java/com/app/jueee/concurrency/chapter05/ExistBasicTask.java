package com.app.jueee.concurrency.chapter05;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import com.app.jueee.concurrency.chapter05.common.LevenshteinDistance;

public class ExistBasicTask implements Callable<Boolean> {

    // 任务将在列表中处理的第一个单词（包括）
    private int startIndex;
    // 任务将在列表中处理的最后一个单词（不包括）
    private int endIndex;
    // 完整的单词列表
    private List<String> dictionary;
    // 任务要查找的单词
    private String word;

    public ExistBasicTask(int startIndex, int endIndex, List<String> dictionary, String word) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.dictionary = dictionary;
        this.word = word;
    }

    @Override
    public Boolean call() throws Exception {
        
        // 将遍历分配给该任务的那部分列表，计算输入单词和这部分列表中各单词之间的 Levenshtein 距离。
        for (int i = startIndex; i < endIndex; i++) {
            int distance = LevenshteinDistance.calculate(word, dictionary.get(i));
            // 如果找到了该单词，那么它将返回 true 值。
            if (distance == 0) {
                return true;
            }
        }
        
        
        if (Thread.interrupted()) {
            return false;
        }
        
        // 如果任务处理完分配给它的所有单词之后并没有发现要找的单词，那么它将抛出一个异常以适应  invokeAny() 方法的行为。
        // 在这种情况下，如果该任务返回了 false 值， invokeAny() 方法将返回 false 值而无须等待剩下的任务。
        throw new NoSuchElementException("The word " + word + "doesn't exists.");
    }
}
