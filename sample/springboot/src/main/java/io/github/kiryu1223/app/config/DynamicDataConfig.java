package io.github.kiryu1223.app.config;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DynamicDataConfig {

    @Autowired
    public DynamicDataConfig(DrinkClient client) {
        DataSourceManager dataSourceManager = client.getConfig().getDataSourceManager();
        dataSourceManager.addDataSource("ds1", ds1());
        dataSourceManager.addDataSource("ds2", ds2());
        dataSourceManager.addDataSource("ds3", ds3());
    }

    public DataSource ds1() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    public DataSource ds2() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    public DataSource ds3() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }
}
