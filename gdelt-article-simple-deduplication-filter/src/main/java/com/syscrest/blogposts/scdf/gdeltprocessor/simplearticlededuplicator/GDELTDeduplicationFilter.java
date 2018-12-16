package com.syscrest.blogposts.scdf.gdeltprocessor.simplearticlededuplicator;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;

import org.springframework.integration.annotation.Filter;
import org.springframework.messaging.Message;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;


/**
 * Very simple implementation of a deduplication filter utilizing an in-memory
 * cache. Duplicates may still occur after restarts or if the ttl is too low.
 * Long TTLs may cause memory problems.
 * 
 * @author Thomas Memenga
 *
 */
@EnableConfigurationProperties(GDELTDeduplicationFilterProperties.class)
@EnableBinding(Processor.class)
@SpringBootApplication
public class GDELTDeduplicationFilter {

	private static final Log logger = LogFactory.getLog(GDELTDeduplicationFilter.class);

	public static void main(String[] args) {
		SpringApplication.run(GDELTDeduplicationFilter.class, args);
	}

	@Autowired
	private GDELTDeduplicationFilterProperties configuation;

	private Cache<String, Long> cache;

	@PostConstruct
	private void postConstruct() {
		cache = CacheBuilder.newBuilder().expireAfterWrite(configuation.getTimeToLive(), TimeUnit.MINUTES).build();
	}

	@Filter(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public boolean filter(Message<GDELTArticle> message) {

		GDELTArticle article = message.getPayload();
		Long currentCount = cache.getIfPresent(article.getUrl());
		if (currentCount != null) {
			cache.put(article.getUrl(), ++currentCount);
			logger.info("already seen " + (currentCount) + " times, filtering out article = " + article);
			return false;
		} else {
			cache.put(article.getUrl(), 1L);
			logger.info("seen for first time, passing thru = " + article);
			return true;
		}

	}

}
