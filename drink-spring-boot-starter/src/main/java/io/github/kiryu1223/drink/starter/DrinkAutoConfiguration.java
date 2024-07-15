package io.github.kiryu1223.drink.starter;

import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.starter.dataSource.SpringDataSourceManager;
import io.github.kiryu1223.drink.starter.transaction.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DrinkProperties.class)
public class DrinkAutoConfiguration
{
    private final Config config;

    @Autowired
    public DrinkAutoConfiguration(DataSource dataSource, DrinkProperties properties)
    {
        SpringDataSourceManager dataSourceManager = new SpringDataSourceManager(dataSource);
        SpringTransactionManager transactionManager = new SpringTransactionManager(dataSourceManager);
        config = new Config(properties.getDataBase(), transactionManager, dataSourceManager, new DefaultSqlSessionFactory(dataSourceManager, transactionManager));
        config.setPrintSql(properties.isPrintSql());
    }

    @Bean
    public DrinkClient drinkClient()
    {
        return new DrinkClient(config);
    }
}
