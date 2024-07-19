package io.github.kiryu1223.drink.starter.configuration;

import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.starter.dataSource.SpringDynamicDataSourceManager;
import io.github.kiryu1223.drink.starter.dataSource.SpringSingleDataSourceManager;
import io.github.kiryu1223.drink.starter.transaction.SpringTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DrinkProperties.class)
public class DrinkAutoConfiguration
{
    private static final Logger log = LoggerFactory.getLogger(DrinkAutoConfiguration.class);

    private Config config;

    private void init(DataSource dataSource, DrinkProperties properties)
    {
        DataSourceManager dataSourceManager = new SpringSingleDataSourceManager(dataSource);
        SpringTransactionManager transactionManager = new SpringTransactionManager(dataSourceManager);
        config = new Config(properties.getDatabase(), transactionManager, dataSourceManager, new DefaultSqlSessionFactory(dataSourceManager, transactionManager));
        config.setPrintSql(properties.isPrintSql());
        config.setPrintBatch(properties.isPrintBatch());
        config.setPrintBatch(properties.isPrintUseDs());
        log.info("Client DataSource Mod: [Single]");
    }

    private void init(SpringDynamicDataSourceManager dataSourceManager, DrinkProperties properties)
    {
        SpringTransactionManager transactionManager = new SpringTransactionManager(dataSourceManager);
        config = new Config(properties.getDatabase(), transactionManager, dataSourceManager, new DefaultSqlSessionFactory(dataSourceManager, transactionManager));
        config.setPrintSql(properties.isPrintSql());
        config.setPrintBatch(properties.isPrintBatch());
        config.setPrintBatch(properties.isPrintUseDs());
        log.info("Client DataSource Mod: [Dynamic]");
    }

    @Bean
    @ConditionalOnBean(SpringDynamicDataSourceManager.class)
    @ConditionalOnMissingBean(DrinkClient.class)
    public DrinkClient buildD(@Qualifier("SpringDynamicDataSourceManager") SpringDynamicDataSourceManager manager, DrinkProperties properties)
    {
        init(manager, properties);
        return new DrinkClient(config);
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean(DrinkClient.class)
    public DrinkClient buildS(DataSource dataSource, DrinkProperties properties)
    {
        init(dataSource, properties);
        return new DrinkClient(config);
    }
}
