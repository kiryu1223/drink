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

abstract class BaseTest
{
    protected static final DrinkClient client;
    protected static final HikariDataSource mysqlDataSource;

    static
    {
        mysqlDataSource = new HikariDataSource();
        mysqlDataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8");
        mysqlDataSource.setUsername("root");
        mysqlDataSource.setPassword("root");
        mysqlDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(mysqlDataSource);
        TransactionManager transactionManager = new DefaultTransactionManager(dataSourceManager);
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);
        client = Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setDbType(DbType.MySQL)
                .build();
    }

//    public BaseTest()
//    {
//        this.client = initMysql();
//    }
//
//    private DrinkClient initMysql()
//    {
//        mysqlDataSource = new HikariDataSource();
//        mysqlDataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8");
//        mysqlDataSource.setUsername("root");
//        mysqlDataSource.setPassword("root");
//        mysqlDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        DataSourceManager dataSourceManager = new DefaultDataSourceManager(mysqlDataSource);
//        TransactionManager transactionManager = new DefaultTransactionManager(dataSourceManager);
//        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);
//        return Drink.bootStrap()
//                .setDataSourceManager(dataSourceManager)
//                .setDbType(DbType.MySQL)
//                .build();
//    }
}
