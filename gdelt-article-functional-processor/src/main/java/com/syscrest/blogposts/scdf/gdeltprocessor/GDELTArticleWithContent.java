package com.syscrest.blogposts.scdf.gdeltprocessor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GDELTArticleWithContent extends GDELTArticle {

	public GDELTArticleWithContent(GDELTArticle article) {
		this.setDomain(article.getDomain());
		this.setLanguage(article.getLanguage());
		this.setSeendate(article.getSeendate());
		this.setSourcecountry(article.getSourcecountry());
		this.setTitle(article.getTitle());
		this.setUrl(article.getUrl());

	}

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "GDELTArticleWithContent [content=" + content + ", getUrl()=" + getUrl() + ", getTitle()=" + getTitle()
				+ ", getLanguage()=" + getLanguage() + ", getSourcecountry()=" + getSourcecountry() + ", getDomain()="
				+ getDomain() + ", getSeendate()=" + getSeendate() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}

}
