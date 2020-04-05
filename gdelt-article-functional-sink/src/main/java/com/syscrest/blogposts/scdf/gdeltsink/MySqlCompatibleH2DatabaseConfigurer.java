package com.syscrest.blogposts.scdf.gdeltsink;

import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.h2.Driver;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer;

public class MySqlCompatibleH2DatabaseConfigurer implements EmbeddedDatabaseConfigurer {

	public void shutdown(DataSource dataSource, String databaseName) {
		try {
			java.sql.Connection connection = dataSource.getConnection();
			Statement stmt = connection.createStatement();
			stmt.execute("SHUTDOWN");
		} catch (SQLException ex) {
		}
	}

	public void configureConnectionProperties(ConnectionProperties properties, String databaseName) {
		properties.setDriverClass(Driver.class);
		properties.setUrl(String.format("jdbc:h2:mem:%s;MODE=MYSQL;DB_CLOSE_DELAY=-1", databaseName));
		properties.setUsername("sa");
		properties.setPassword("");
	}

}