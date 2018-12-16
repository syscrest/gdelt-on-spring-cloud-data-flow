package com.syscrest.blogposts.scdf.gdeltprocessor.simplearticlededuplicator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("gdelt")
@Validated
public class GDELTDeduplicationFilterProperties {

	/**
	 * time in minutes to remember already seen article urls
	 */
	private long timeToLive = 24 * 60l;

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

}
