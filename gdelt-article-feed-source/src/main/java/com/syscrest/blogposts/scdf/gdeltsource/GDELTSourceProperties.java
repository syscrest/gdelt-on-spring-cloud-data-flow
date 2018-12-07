package com.syscrest.blogposts.scdf.gdeltsource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("gdelt")
@Validated
public class GDELTSourceProperties {

	/**
	 * The query to use to select data.
	 * 
	 * Example: ("climate change" or "global warming")
	 * 
	 */
	private String query = "climate change";

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * The delay between pulling data from gdelt (in seconds).
	 */
	private long triggerDelay = 300L;

	public long getTriggerDelay() {
		return triggerDelay;
	}

	public void setTriggerDelay(long triggerDelay) {
		this.triggerDelay = triggerDelay;
	}

}
