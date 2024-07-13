package io.github.kiryu1223.app.config;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.ext.DbType;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

//@Configuration
public class drinkConfig
{
    @Bean
    public DrinkClient drinkClient()
    {
        return new DrinkClient(new Config(DbType.MySQL,dataSource()));
    }

    public DataSource dataSource()
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true&transactionIsolation=TRANSACTION_READ_COMMITTED");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }
}
