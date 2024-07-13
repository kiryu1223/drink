package io.github.kiryu1223.drink.starter;

import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
        config = new Config(properties.getDataBase(), dataSource);
        config.setPrintSql(properties.isPrintSql());
    }

    @Bean
    public DrinkClient drinkClient()
    {
        return new DrinkClient(config);
    }
}
