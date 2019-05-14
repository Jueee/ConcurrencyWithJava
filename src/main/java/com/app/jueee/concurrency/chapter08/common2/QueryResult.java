package com.app.jueee.concurrency.chapter08.common2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryResult {
	// 存放相关文档信息.其键为文档的文件名，其值为一个 Document 对象
	private Map<String, Document> results;
	
	public QueryResult(Map<String, Document> results) {
		this.results=results;
	}

	public void append(Token token) {
		results.computeIfAbsent(token.getFile(), s -> new Document(s)).addTfxidf(token.getTfxidf());
	}

	public Map<String, Document> getResults() {
		return results;
	}

	public List<Document> getAsList() {
		return new ArrayList<>(results.values());
	}
	
	

}
