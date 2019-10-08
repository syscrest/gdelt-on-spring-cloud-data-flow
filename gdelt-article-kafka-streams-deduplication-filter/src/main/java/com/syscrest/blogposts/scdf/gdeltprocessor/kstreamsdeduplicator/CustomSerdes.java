package com.syscrest.blogposts.scdf.gdeltprocessor.kstreamsdeduplicator;

import org.apache.kafka.common.serialization.Serdes.WrapperSerde;

public class CustomSerdes {

	static public final class GDELTArticleSerde extends WrapperSerde<GDELTArticle> {

		public GDELTArticleSerde() {
			super(new GDELTArticleSerializer(), new GDELTArticleDeserializer());
		}

	}

	public static GDELTArticleSerde GDELTArticleSerde() {
		return new GDELTArticleSerde();
	}
}