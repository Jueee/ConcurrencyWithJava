package com.app.jueee.concurrency.chapter09.example1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;

public class ConcurrentStringAccumulator implements BiConsumer<List<String>, Path> {

	private String word;

	public ConcurrentStringAccumulator(String word) {
		this.word = word.toLowerCase();
	}

	@Override
	public void accept(List<String> list, Path path) {
		long counter;
		try  {
			counter = Files
					.lines(path)
					.filter(l -> l.indexOf(":")!=-1)
					.map(l -> l.split(":")[1].toLowerCase())       // 使用 map() 方法获取每个属性的取值
					.filter(l -> l.contains(word.toLowerCase()))   // 使用 filter() 方法仅选取那些含有待搜索单词的行
					.count();      // 使用 count() 方法计算流中剩下的元素数
			System.out.println(counter);
			if (counter>0) {
				list.add(path.toString());
			}

		} catch (Exception e) {
			System.out.println(path);
			e.printStackTrace();
		}
	}

}
