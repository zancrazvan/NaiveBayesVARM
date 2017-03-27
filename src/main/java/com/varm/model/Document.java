package com.varm.model;

import java.util.Map;

/**
 * 
 * @author RazvanZanc
 *
 *         Represents the text used for training and prediction.
 */
public class Document {

	private Map<String, Integer> tokens;

	private String category;

	public Map<String, Integer> getTokens() {
		return tokens;
	}

	public void setTokens(Map<String, Integer> tokens) {
		this.tokens = tokens;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Document [tokens=" + tokens + ", category=" + category + "]";
	}

}
