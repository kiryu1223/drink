package io.github.kiryu1223.drink.base;

import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.session.SqlSessionFactory;
import io.github.kiryu1223.drink.base.tobean.Include.IncludeFactory;
import io.github.kiryu1223.drink.base.tobean.beancreator.BeanCreatorFactory;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;

public interface IConfig
{
    SqlExpressionFactory getSqlExpressionFactory();

    IDialect getDisambiguation();

    DataSourceManager getDataSourceManager();

    TransactionManager getTransactionManager();

    SqlSessionFactory getSqlSessionFactory();

    IncludeFactory getIncludeFactory();

    BeanCreatorFactory getFastCreatorFactory();

    DbType getDbType();

    boolean isPrintSql();

    boolean isPrintUseDs();

    boolean isPrintBatch();

    boolean isIgnoreDeleteNoWhere();

    boolean isIgnoreUpdateNoWhere();
}
