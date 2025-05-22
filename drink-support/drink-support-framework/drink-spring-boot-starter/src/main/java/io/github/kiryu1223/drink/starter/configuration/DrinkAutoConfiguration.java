package io.github.kiryu1223.drink.starter.configuration;

import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;
import io.github.kiryu1223.drink.core.SqlBuilder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.starter.dataSource.SpringDataSourceManager;
import io.github.kiryu1223.drink.starter.transaction.SpringTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DrinkAutoConfiguration.class)
@ConditionalOnBean(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(
        prefix = "drink",
        name = "enable",
        havingValue = "true"
)
public class DrinkAutoConfiguration
{
    private static final Logger log = LoggerFactory.getLogger(DrinkAutoConfiguration.class);

    private SqlClient init(DataSource dataSource, DrinkProperties properties) throws InstantiationException, IllegalAccessException
    {
        DataSourceManager dataSourceManager = new SpringDataSourceManager(dataSource);
        TransactionManager transactionManager = new SpringTransactionManager(dataSourceManager);
        return SqlBuilder.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setTransactionManager(transactionManager)
                .setDbType(properties.getDatabase())
                .setOption(properties.bulidOption())
                .setNameConverter(properties.getNameConversion().getNameConverter())
                .setPager(properties.getPager().newInstance())
                .build();
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean(SqlClient.class)
    public SqlClient build(DataSource dataSource, DrinkProperties properties) throws InstantiationException, IllegalAccessException
    {
        return init(dataSource, properties);
    }
}
