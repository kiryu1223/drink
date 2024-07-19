package io.github.kiryu1223.drink.starter.configuration;


import io.github.kiryu1223.drink.starter.dataSource.SpringDynamicDataSourceManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore(DrinkAutoConfiguration.class)
@EnableConfigurationProperties(DynamicProperties.class)
@ConditionalOnProperty(prefix = "spring.datasource.dynamic", name = "enabled", havingValue = "true")
public class DynamicDataSourceAutoConfiguration
{
    private final DynamicProperties dynamicProperties;

    public DynamicDataSourceAutoConfiguration(DynamicProperties dynamicProperties)
    {
        this.dynamicProperties = dynamicProperties;
    }

    @Bean("SpringDynamicDataSourceManager")
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
