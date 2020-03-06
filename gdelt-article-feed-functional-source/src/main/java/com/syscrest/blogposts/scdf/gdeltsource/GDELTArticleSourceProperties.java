
package com.syscrest.blogposts.scdf.gdeltsource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("gdelt")
@Validated
public class GDELTArticleSourceProperties {

	/**
	 * The query to use to select data.
	 * 
	 * Example: ("climate change" or "global warming")
	 * 
	 */
	private String query = "global";

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
