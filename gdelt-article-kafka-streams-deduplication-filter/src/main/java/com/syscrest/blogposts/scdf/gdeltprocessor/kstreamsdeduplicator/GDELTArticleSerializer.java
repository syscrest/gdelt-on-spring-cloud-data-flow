package com.syscrest.blogposts.scdf.gdeltprocessor.kstreamsdeduplicator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class GDELTArticleSerializer implements Serializer<GDELTArticle> {
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Default constructor needed by Kafka
	 */
	public GDELTArticleSerializer() {
	}

	@Override
	public void configure(Map<String, ?> props, boolean isKey) {
	}

	@Override
	public byte[] serialize(String topic, GDELTArticle data) {
		if (data == null)
			return null;

		try {
			return objectMapper.writeValueAsBytes(data);
		} catch (Exception e) {
			throw new SerializationException("Error serializing JSON message", e);
		}
	}

	@Override
	public void close() {
	}

}