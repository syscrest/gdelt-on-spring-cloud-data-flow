package com.syscrest.blogposts.scdf.gdeltprocessor.kstreamsdeduplicator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("gdelt")
@Validated
public class GDELTKStreamsDeduplicationProperties {

	/**
	 * time in seconds before a seen url (without any further occurrences) is
	 * considered final and can be emitted. For the deduplication to work this
	 * values must be greater than the poll interval of the source.
	 */
	private long inactivityGap = 180;

	public long getInactivityGap() {
		return inactivityGap;
	}

	public void setInactivityGap(long inactivityGap) {
		this.inactivityGap = inactivityGap;
	}

}
