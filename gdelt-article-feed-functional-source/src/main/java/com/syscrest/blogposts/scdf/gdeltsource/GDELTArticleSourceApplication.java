package com.syscrest.blogposts.scdf.gdeltsource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.PollableBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootApplication
public class GDELTArticleSourceApplication {

	Logger logger = org.slf4j.LoggerFactory.getLogger(GDELTArticleSourceApplication.class);

	private static DateTimeFormatter START_DATETIME_FORMATTER = DateTimeFormat.forPattern("yyyyMMddHHmmss")
			.withZone(DateTimeZone.UTC);

	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private GDELTArticleSourceProperties configuration;

	public static void main(String[] args) {
		SpringApplication.run(GDELTArticleSourceApplication.class, args);
	}

	@PollableBean(splittable = true)
	public Supplier<List<GDELTArticle>> pullArticles() {
		return () -> {
			try {

				UriComponentsBuilder builder = UriComponentsBuilder
						.fromHttpUrl("https://api.gdeltproject.org/api/v2/doc/doc")
						.queryParam("query", configuration.getQuery()).queryParam("mode", "artlist")
						.queryParam("maxrecords", 10)
						.queryParam("startdatetime",
								START_DATETIME_FORMATTER
										.print(new DateTime().withZone(DateTimeZone.UTC).minusMinutes(31)))
						.queryParam("sort", "HybridRel").queryParam("format", "json");

				GDELTResponse data = restTemplate.getForObject(builder.toUriString(), GDELTResponse.class);
				logger.info("data =  " + data);

				if (data != null && data.getArticles() != null && data.getArticles().length > 0) {
					return Arrays.asList(data.getArticles());
				} else {
					return null;
				}
			} catch (Exception e) {
				logger.error("", e);
				throw new RuntimeException("", e);
			}
		};
	}

}
