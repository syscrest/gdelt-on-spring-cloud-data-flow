package com.syscrest.blogposts.scdf.gdeltprocessor.kstreamsdeduplicator;

import java.time.Duration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.Suppressed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.annotations.KafkaStreamsProcessor;
import org.springframework.messaging.handler.annotation.SendTo;

/**
 * 
 * A more advanced deduplication filter leveraging
 * https://kafka.apache.org/20/documentation/streams/developer-guide/dsl-api.html#session-windows
 * Session windows. It will use the url of the GDELTArticle as the key and the
 * GDELTArticle itself as the value. If the article has not been seen for X
 * seconds, it is no longer part of the query response of the sink and can be
 * passed downstream. X must be greater than the poll intervall of the sink.
 * 
 * @author Thomas Memenga <t.memenga@syscrest.com>
 *
 */
@EnableConfigurationProperties(GDELTKStreamsDeduplicationProperties.class)
@EnableBinding(KafkaStreamsProcessor.class)
@SpringBootApplication
public class GDELTKStreamsDeduplicationApplication {

	private static final Log logger = LogFactory.getLog(GDELTKStreamsDeduplicationApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GDELTKStreamsDeduplicationApplication.class, args);
	}

	@Autowired
	private GDELTKStreamsDeduplicationProperties configuration;

	@StreamListener("input")
	@SendTo("output")
	public KStream<String, GDELTArticle> process(KStream<Object, GDELTArticle> input) {

		return input
				.groupBy((k, v) -> v.getUrl(), Grouped.with(Serdes.String(), CustomSerdes.GDELTArticleSerde()))
				.windowedBy(
						SessionWindows.with(
								Duration.ofSeconds(configuration.getInactivityGap())).grace(Duration.ZERO)
						)
				.reduce((a1, a2) -> a1)
				.suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
				.toStream((windowed, value) -> windowed.key())
				.peek((k, v) -> {
					logger.info("emitting article = " + v);
				});

	}

}
