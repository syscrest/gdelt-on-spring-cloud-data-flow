package com.syscrest.blogposts.scdf.gdeltprocessor.kstreamsdeduplicator;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GDELTArticleDeserializer implements Deserializer<GDELTArticle> {

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Default constructor needed by Kafka
	 */
	public GDELTArticleDeserializer() {
	}

	@Override
	public GDELTArticle deserialize(String topic, byte[] bytes) {

		if (bytes == null) {
			return null;
		}

		GDELTArticle data;
		try {
			data = objectMapper.readValue(bytes, GDELTArticle.class);
		} catch (Exception e) {
			throw new SerializationException(e);
		}

		return data;
	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub

	}
}