package com.syscrest.blogposts.scdf.gdeltsource;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GDELTResponse {

	private GDELTArticle[] articles;

	@Override
	public String toString() {
		return "GDELTResponse [articles=" + Arrays.toString(articles) + "]";
	}

	public GDELTArticle[] getArticles() {
		return articles;
	}

	public void setArticles(GDELTArticle[] articles) {
		this.articles = articles;
	}

}
