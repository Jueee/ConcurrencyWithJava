package com.app.jueee.concurrency.chapter05.common;

/**
 * 用于计算两个字符串之间的 Levenshtein 距离。
 * 编辑距离：又称Levenshtein距离，是指两个字串之间，由一个转成另一个所需的最少的编辑操作次数。
 * 编辑操作包括将一个字符替换成另一个字符，插入一个字符，删除一个字符。
 * 
 * 一般来说，编辑距离越小，两个串的相似度越大。
 * @author hzweiyongqiang
 */
public class LevenshteinDistance {

    /**
     *  该方法接收两个字符串对象作为参数，并且返回一个 int 值来表示两个单词之间的距离。
     *	@param string1
     *	@param string2
     *	@return
     */
    public static int calculate(String string1, String string2) {
        int[][] distances = new int[string1.length() + 1][string2.length() + 1];

        for (int i = 1; i <= string1.length(); i++) {
            distances[i][0] = i;
        }
        for (int i = 1; i <= string2.length(); i++) {
            distances[0][i] = i;
        }

        for (int i = 1; i <= string1.length(); i++) {
            for (int j = 1; j <= string2.length(); j++) {
                if (string1.charAt(i - 1) == string2.charAt(j - 1)) {
                    distances[i][j] = distances[i - 1][j - 1];
                } else {
                    distances[i][j] = minimum(distances[i - 1][j], distances[i][j - 1], distances[i - 1][j - 1]) + 1;
                }
            }
        }
        return distances[string1.length()][string2.length()];
    }

    private static int minimum(int i, int j, int k) {
        return Math.min(i, Math.min(j, k));
    }

    public static void main(String[] args) {
        System.out.println(calculate("java", "jbca"));
    }
}
