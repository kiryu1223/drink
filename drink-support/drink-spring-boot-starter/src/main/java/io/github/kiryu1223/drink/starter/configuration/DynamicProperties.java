package io.github.kiryu1223.drink.starter.configuration;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class DynamicProperties
{
    private boolean enabled = true;
    private String primary = "";
    private boolean strict = true;
    private Map<String, DataSourceProperties> datasources = new LinkedHashMap<>();

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getPrimary()
    {
        return primary;
    }

    public void setPrimary(String primary)
    {
        this.primary = primary;
    }

    public boolean isStrict()
    {
        return strict;
    }

    public void setStrict(boolean strict)
    {
        this.strict = strict;
    }

    public Map<String, DataSourceProperties> getDatasources()
    {
        return datasources;
    }

    public void setDatasources(Map<String, DataSourceProperties> datasources)
    {
        this.datasources = datasources;
    }
}
