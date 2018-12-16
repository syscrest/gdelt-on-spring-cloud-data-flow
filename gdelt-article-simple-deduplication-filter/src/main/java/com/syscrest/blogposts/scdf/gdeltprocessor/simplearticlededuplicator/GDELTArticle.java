package com.syscrest.blogposts.scdf.gdeltprocessor.simplearticlededuplicator;

public class GDELTArticle {

	private String url;
	private String title;
	private String language;
	private String sourcecountry;
	private String domain;
	private String seendate;

	public GDELTArticle() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSourcecountry() {
		return sourcecountry;
	}

	public void setSourcecountry(String sourcecountry) {
		this.sourcecountry = sourcecountry;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSeendate() {
		return seendate;
	}

	public void setSeendate(String seendate) {
		this.seendate = seendate;
	}

	@Override
	public String toString() {
		return "GDELTArticle [url=" + url + ", title=" + title + ", language=" + language + ", sourcecountry="
				+ sourcecountry + ", domain=" + domain + ", seendate=" + seendate + "]";
	}

}