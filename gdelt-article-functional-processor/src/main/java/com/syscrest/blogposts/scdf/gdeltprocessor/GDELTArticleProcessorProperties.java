
package com.syscrest.blogposts.scdf.gdeltprocessor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("gdelt")
@Validated
public class GDELTArticleProcessorProperties {

	/**
	 * Whether to save the original html body or transform the markup to plain text
	 */
	boolean transformHtmlToPlainText = true;

	public boolean isTransformHtmlToPlainText() {
		return transformHtmlToPlainText;
	}

	public void setTransformHtmlToPlainText(boolean transformHtmlToPlainText) {
		this.transformHtmlToPlainText = transformHtmlToPlainText;
	}

}
