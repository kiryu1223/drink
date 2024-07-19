package io.github.kiryu1223.app.config;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.dynamicds.DynamicDataSource;

import javax.sql.DataSource;

@Configuration
public class MyConfig
{
    @Bean("normalDs1")
    public DataSource dataSource1(@Inject("${ds1}") HikariDataSource dataSource)
    {
        return dataSource;
    }

//    @Bean("normalDs2")
//    public DataSource dataSource2(@Inject("${ds2}") HikariDataSource dataSource)
//    {
//        return dataSource;
//    }
//
//    @Bean("normalDs3")
//    public DataSource dataSource3(@Inject("${ds3}") HikariDataSource dataSource)
//    {
//        return dataSource;
//    }
//
//    @Bean("dynamicDs")
//    public DataSource dataSource4(@Inject("${dynamic}") DynamicDataSource dataSource)
//    {
//        return dataSource;
//    }
}
