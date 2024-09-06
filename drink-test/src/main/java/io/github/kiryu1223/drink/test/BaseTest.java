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
import io.github.kiryu1223.drink.ext.DbType;

public abstract class BaseTest
{
    protected final DrinkClient mysqlClient;
    protected HikariDataSource mysqlDataSource;

    protected final DrinkClient oracleClient;
    protected HikariDataSource oracleDataSource;

    protected final DrinkClient sqlserverClient;
    protected HikariDataSource sqlserverDataSource;

    public BaseTest()
    {
        mysqlClient = initMysql();
        oracleClient = initOracle();
        sqlserverClient = initMSSql();
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

    private DrinkClient initOracle()
    {
        oracleDataSource = new HikariDataSource();
        oracleDataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521/XEPDB1");
        oracleDataSource.setUsername("TESTUSER");
        oracleDataSource.setPassword("root");
        oracleDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(oracleDataSource);
        return Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setDbType(DbType.Oracle)
                .build();
    }

    private DrinkClient initMSSql()
    {
        sqlserverDataSource = new HikariDataSource();
        sqlserverDataSource.setJdbcUrl("jdbc:sqlserver://localhost:1433;database=DrinkDb;encrypt=true;trustServerCertificate=true");
        sqlserverDataSource.setUsername("sa");
        sqlserverDataSource.setPassword("root");
        sqlserverDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        DataSourceManager dataSourceManager = new DefaultDataSourceManager(sqlserverDataSource);
        return Drink.bootStrap()
                .setDataSourceManager(dataSourceManager)
                .setDbType(DbType.SqlServer)
                .build();
    }
}
