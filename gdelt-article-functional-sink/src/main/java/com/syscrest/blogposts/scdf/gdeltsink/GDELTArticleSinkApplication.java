package com.syscrest.blogposts.scdf.gdeltsink;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@SpringBootApplication
public class GDELTArticleSinkApplication {

	Logger logger = org.slf4j.LoggerFactory.getLogger(GDELTArticleSinkApplication.class);

	@Autowired
	private GDELTArticleSinkProperties configuration;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(GDELTArticleSinkApplication.class, args);
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {

		EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
		factory.setDatabaseConfigurer(new MySqlCompatibleH2DatabaseConfigurer());
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource("schema.sql"));
		factory.setDatabasePopulator(resourceDatabasePopulator);
		return new JdbcTemplate(factory.getDatabase());
	}

	@Bean
	public Consumer<List<GDELTArticle>> persistArticles() {
		return (List<GDELTArticle> articles) -> {
			logger.info(String.format("going to add %d articles to h2 database ", articles.size()));

			jdbcTemplate.batchUpdate(
					"INSERT IGNORE INTO articles "
							+ "(language, seendate , sourcecountry , title , url , domain, content) "
							+ " VALUES (?, ?, ?, ?, ?, ? ,?) on duplicate key update url = url",

					new BatchPreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {

							ps.setString(1, articles.get(i).getLanguage());
							ps.setString(2, articles.get(i).getSeendate());
							ps.setString(3, articles.get(i).getSourcecountry());
							ps.setString(4, articles.get(i).getTitle());
							ps.setString(5, articles.get(i).getUrl());
							ps.setString(6, articles.get(i).getDomain());
							ps.setString(7, articles.get(i).getContent());

						}

						@Override
						public int getBatchSize() {
							return articles.size();
						}
					});

			if (configuration.isPrintStatistics()) {

				jdbcTemplate
						.queryForList("select sourcecountry , count(*) as total_count "
								+ " from articles group by sourcecountry order by total_count desc, sourcecountry desc")
						.forEach((rowMap) -> {
							logger.info(String.format("country '%s' - current article count = %d",
									rowMap.get("sourcecountry"), rowMap.get("total_count")));
						});
			}
		};

	}

}
