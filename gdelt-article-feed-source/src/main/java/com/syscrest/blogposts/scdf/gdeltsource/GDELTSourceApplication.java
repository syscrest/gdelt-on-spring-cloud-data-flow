package com.syscrest.blogposts.scdf.gdeltsource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

/**
 * Example of a source implementation utilizing the Spring Integration Java DSL.
 * 
 * 
 * @author Thomas Memenga <t.memenga@syscrest.com>
 *
 */
@EnableConfigurationProperties(GDELTSourceProperties.class)
@EnableBinding(Source.class)
@SpringBootApplication
public class GDELTSourceApplication {

	private static final Log logger = LogFactory.getLog(GDELTSourceApplication.class);

	public static void main(String[] args) {
		logger.info("going to run GDELTSourceApplication ..");
		SpringApplication.run(GDELTSourceApplication.class, args);
	}

	@Autowired
	private GDELTSourceProperties properties;

	@StreamEmitter
	@Output(Source.OUTPUT)
	@Bean
	public Publisher<Message<List<GDELTArticle>>> emit() {
		return IntegrationFlows.from(() -> {
			try {
				URL feedUrl = new URL("https://api.gdeltproject.org/api/v2/doc/doc?query="
						+ URLEncoder.encode(properties.getQuery(), "UTF-8")
						+ "&mode=artlist&maxrecords=250&timespan=1h&sort=datedesc&format=json");
				logger.info("going to fetch data from gdeltproject.org using url = " + feedUrl);
				InputStream inputStreamObject = feedUrl.openStream();
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
				StringBuilder responseStrBuilder = new StringBuilder();
				String inputStr;
				while ((inputStr = streamReader.readLine()) != null) {
					responseStrBuilder.append(inputStr);
				}
				JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
				JSONArray articles = jsonObject.getJSONArray("articles");
				List<GDELTArticle> response = new ArrayList<>();
				for (int i = 0; i < articles.length(); i++) {
					JSONObject article = articles.getJSONObject(i);
					GDELTArticle a = new GDELTArticle();
					a.setUrl(article.getString("url"));
					a.setTitle(article.getString("title"));
					a.setDomain(article.getString("domain"));
					a.setSourcecountry(article.getString("sourcecountry"));
					a.setLanguage(article.getString("language"));
					a.setSeendate(article.getString("seendate"));
					response.add(a);
				}
				return new GenericMessage<>(response);
			} catch (Exception e) {
				logger.error("", e);
				return new GenericMessage<>(null);
			}
		}, e -> e.poller(p -> p.fixedDelay(this.properties.getTriggerDelay(), TimeUnit.SECONDS))).toReactivePublisher();
	}

}