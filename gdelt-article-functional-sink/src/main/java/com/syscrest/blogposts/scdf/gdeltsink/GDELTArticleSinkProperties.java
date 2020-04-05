
package com.syscrest.blogposts.scdf.gdeltsink;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("gdelt")
@Validated
public class GDELTArticleSinkProperties {

	private boolean printStatistics = true;

	public boolean isPrintStatistics() {
		return printStatistics;
	}

	public void setPrintStatistics(boolean printStatistics) {
		this.printStatistics = printStatistics;
	}

}
