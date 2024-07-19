package io.github.kiryu1223.drink.starter;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.starter.dataSource.SpringDynamicDataSourceManager;
import io.github.kiryu1223.drink.starter.dataSource.SpringSingleDataSourceManager;
import io.github.kiryu1223.drink.starter.transaction.SpringTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({DrinkProperties.class, DynamicProperties.class})
public class DrinkAutoConfiguration
{
    private static final Logger log = LoggerFactory.getLogger(DrinkAutoConfiguration.class);
    private Config config;

    @Autowired(required = false)
    DataSource dataSource;

    @Autowired
    DrinkProperties properties;

    @Autowired(required = false)
    SpringDynamicDataSourceManager springDynamicDataSourceManager;

    private void init()
    {
        DataSourceManager dataSourceManager;
        boolean dynamicDataSource = false;
        if (springDynamicDataSourceManager != null)
        {
            dataSourceManager = springDynamicDataSourceManager;
            dynamicDataSource = true;
        }
        else if (dataSource != null)
        {
            dataSourceManager = new SpringSingleDataSourceManager(dataSource);
        }
        else
        {
            throw new RuntimeException("no datasource found");
        }
        SpringTransactionManager transactionManager = new SpringTransactionManager(dataSourceManager);
        config = new Config(properties.getDatabase(), transactionManager, dataSourceManager, new DefaultSqlSessionFactory(dataSourceManager, transactionManager));
        config.setPrintSql(properties.isPrintSql());
        config.setPrintBatch(properties.isPrintBatch());
        config.setPrintBatch(properties.isPrintUseDs());
        log.info("Client DataSource Mod: {}", dynamicDataSource ? "dynamic" : "single");
    }

    @Bean
    public DrinkClient drinkClient()
    {
        init();
        return new DrinkClient(config);
    }
}
