package io.github.kiryu1223.drink.starter;


import io.github.kiryu1223.drink.starter.dataSource.SpringDynamicDataSourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AutoConfigureBefore(DrinkAutoConfiguration.class)
@EnableConfigurationProperties(DynamicProperties.class)
@ConditionalOnProperty(prefix = "spring.datasource.dynamic", name = "enabled", havingValue = "true")
public class DynamicDataSourceAutoConfiguration
{
    private final DynamicProperties dynamicProperties;

    @Autowired
    public DynamicDataSourceAutoConfiguration(DynamicProperties dynamicProperties)
    {
        this.dynamicProperties = dynamicProperties;
    }

    @Bean
    public SpringDynamicDataSourceManager dynamicDataSource()
    {
        String primary = dynamicProperties.getPrimary();
        Map<String, DataSourceProperties> datasource = dynamicProperties.getDatasources();
        if (primary.isEmpty())
        {
            primary = datasource.keySet().iterator().next();
        }
        SpringDynamicDataSourceManager dataSourceManager = new SpringDynamicDataSourceManager(primary);
        Map<Object, Object> dataSourceMap = new HashMap<>();
        for (Map.Entry<String, DataSourceProperties> e : datasource.entrySet())
        {
            DataSource build = e.getValue().initializeDataSourceBuilder().build();
            dataSourceMap.put(e.getKey(), build);
        }
        dataSourceManager.setTargetDataSources(dataSourceMap);
        dataSourceManager.setLenientFallback(dynamicProperties.isStrict());
        return dataSourceManager;
    }
}
