package io.github.kiryu1223.drink.oldtest;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.core.Drink;
import io.github.kiryu1223.drink.core.api.client.DrinkClient;
import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.base.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.base.session.SqlSessionFactory;
import io.github.kiryu1223.drink.base.transaction.DefaultTransactionManager;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;

public class BaseTest
{
    protected final DrinkClient mysql;
//    protected final DrinkClient oracle;
//    protected final DrinkClient sqlserver;

    BaseTest()
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(dataSource);
        TransactionManager transactionManager = new DefaultTransactionManager(dataSourceManager);
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);
        mysql = Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setTransactionManager(transactionManager)
                .setSqlSessionFactory(sqlSessionFactory)
                .build();
    }
}
