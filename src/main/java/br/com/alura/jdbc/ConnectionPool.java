package br.com.alura.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

public class ConnectionPool {

    private DataSource dataSource;

    public ConnectionPool() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/jdbcDB");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("root");
        basicDataSource.setInitialSize(1);
        this.dataSource = basicDataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
