package io.github.kiryu1223.drink.test;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.Drink;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.api.transaction.DefaultTransactionManager;
import io.github.kiryu1223.drink.api.transaction.TransactionManager;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.core.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.core.session.SqlSessionFactory;

import javax.sql.DataSource;

public class BaseTest
{
    public final DrinkClient client;
    public final DataSource dataSource;

    BaseTest()
    {
        HikariDataSource dataSource = new HikariDataSource();
        this.dataSource = dataSource;
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(dataSource);
        TransactionManager transactionManager = new DefaultTransactionManager(dataSourceManager);
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);
        client = Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setTransactionManager(transactionManager)
                .setSqlSessionFactory(sqlSessionFactory)
                .build();
    }
}
