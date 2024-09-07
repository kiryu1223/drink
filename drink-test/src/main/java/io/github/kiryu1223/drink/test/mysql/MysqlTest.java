package io.github.kiryu1223.drink.test.mysql;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.Drink;
import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.api.transaction.DefaultTransactionManager;
import io.github.kiryu1223.drink.api.transaction.TransactionManager;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.dataSource.DefaultDataSourceManager;
import io.github.kiryu1223.drink.core.session.DefaultSqlSessionFactory;
import io.github.kiryu1223.drink.core.session.SqlSessionFactory;
import io.github.kiryu1223.drink.ext.DbType;
import io.github.kiryu1223.drink.test.BaseTest;

abstract class MysqlTest
{
    protected final DrinkClient client;
    protected HikariDataSource mysqlDataSource;

    public MysqlTest()
    {
        this.client = initMysql();
    }

    private DrinkClient initMysql()
    {
        mysqlDataSource = new HikariDataSource();
        mysqlDataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8");
        mysqlDataSource.setUsername("root");
        mysqlDataSource.setPassword("root");
        mysqlDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(mysqlDataSource);
        TransactionManager transactionManager = new DefaultTransactionManager(dataSourceManager);
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);
        return Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setDbType(DbType.MySQL)
                .build();
    }
}
