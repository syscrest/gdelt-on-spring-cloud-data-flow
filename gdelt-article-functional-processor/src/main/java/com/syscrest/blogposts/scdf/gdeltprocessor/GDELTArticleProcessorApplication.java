package com.syscrest.blogposts.scdf.gdeltprocessor;

import java.util.function.Function;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class GDELTArticleProcessorApplication {

	Logger logger = org.slf4j.LoggerFactory.getLogger(GDELTArticleProcessorApplication.class);

	@Autowired
	private GDELTArticleProcessorProperties configuration;

	public static void main(String[] args) {
		SpringApplication.run(GDELTArticleProcessorApplication.class, args);
	}

	private RestTemplate restTemplate = new RestTemplate();

	@Bean
	public Function<GDELTArticle, GDELTArticleWithContent> downloadContent() {

		return (article) -> {

			GDELTArticleWithContent articleWithContent = new GDELTArticleWithContent(article);

			try {
				logger.info(String.format("fetching url = %s", article.getUrl()));
				ResponseEntity<String> response = restTemplate.getForEntity(article.getUrl(), String.class);

				if (response.getStatusCode().is2xxSuccessful()) {
					if (configuration.isTransformHtmlToPlainText()) {
						articleWithContent.setContent(Jsoup.parse(response.getBody()).text());
					} else {
						articleWithContent.setContent(response.getBody());
					}
					return articleWithContent;
				} else {
					logger.info(String.format("bad status code (%d, dropping message", response.getStatusCodeValue()));
					return null;
				}

			} catch (Exception e) {
				logger.error("while fetching url = " + article.getUrl(), e);
				return null; // filter entity
			}
		};

	}

}
