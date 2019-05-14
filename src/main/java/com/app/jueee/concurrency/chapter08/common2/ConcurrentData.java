package com.app.jueee.concurrency.chapter08.common2;

public class ConcurrentData {
	
    /**
     *  计算文件中的单词数
     *	@param fileName
     *	@param index
     */
	public static void getWordsInFile1(String fileName, ConcurrentInvertedIndex index) {
		long value = index
				.getIndex()
				.parallelStream()
				.filter(token -> fileName.equals(token.getFile()))
				.count();
		System.out.println("Words in File "+fileName+": "+value);
	}

    
    /**
     *  计算文件中的单词数-使用 reduce() 方法替代 count() 方法
     *  @param fileName
     *  @param index
     */
	public static void getWordsInFile2(String fileName, ConcurrentInvertedIndex index) {

		long value = index
				.getIndex()
				.parallelStream()
				.filter(token -> fileName.equals(token.getFile()))
				.mapToLong(token -> 1)
				.reduce(0, Long::sum);
		System.out.println("Words in File "+fileName+": "+value);
	}
	
	/**
	 *  计算了文档集合中某个文件中单词的平均 tfxidf 值。
	 *	@param fileName
	 *	@param index
	 */
	public static void getAverageTfxidf(String fileName, ConcurrentInvertedIndex index) {

		long wordCounter = index
				.getIndex()
				.parallelStream()
				.filter(token -> fileName.equals(token.getFile()))
				.mapToLong(token -> 1)
				.reduce(0, Long::sum);
		
		double tfxidf = index
				.getIndex()
				.parallelStream()
				.filter(token -> fileName.equals(token.getFile()))
				.reduce(0d, (n,t) -> n+t.getTfxidf(), (n1,n2) -> n1+n2);
		
		System.out.println("Words in File "+fileName+": "+(tfxidf/wordCounter));
	}

	/**
	 *  获取索引中的最大 tfxidf 值
	 *	@param index
	 */
	public static void maxTfxidf(ConcurrentInvertedIndex index) {
		Token token = index
				.getIndex()
				.parallelStream()
				.reduce(new Token("", "xxx:0"), (t1, t2) -> {
					if (t1.getTfxidf()>t2.getTfxidf()) {
						return t1;
					} else {
						return t2;
					}
				});
		System.out.println(token.toString());
	}


    /**
     *  获取索引中的最小 tfxidf 值
     *  @param index
     */
	public static void minTfxidf(ConcurrentInvertedIndex index) {
		Token token = index
				.getIndex()
				.parallelStream()
				.reduce(new Token("", "xxx:1000000"), (t1, t2) -> {
					if (t1.getTfxidf()<t2.getTfxidf()) {
						return t1;
					} else {
						return t2;
					}
				});
		System.out.println(token.toString());
	}
	
	
}
