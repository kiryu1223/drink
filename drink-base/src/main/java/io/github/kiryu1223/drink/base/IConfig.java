package io.github.kiryu1223.drink.base;

import io.github.kiryu1223.drink.base.converter.NameConverter;
import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.page.Pager;
import io.github.kiryu1223.drink.base.session.SqlSessionFactory;
import io.github.kiryu1223.drink.base.toBean.beancreator.BeanCreatorFactory;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;
import io.github.kiryu1223.drink.base.transform.Transformer;

public interface IConfig
{
    SqlExpressionFactory getSqlExpressionFactory();

    IDialect getDisambiguation();

    DataSourceManager getDataSourceManager();

    TransactionManager getTransactionManager();

    SqlSessionFactory getSqlSessionFactory();

    BeanCreatorFactory getBeanCreatorFactory();

    Transformer getTransformer();

    Pager getPager();

    IInsertOrUpdate getInsertOrUpdate();

    NameConverter getNameConverter();

    MetaData getMetaData(Class<?> c);

    Filter getFilter();

    Aop getAop();

    DbType getDbType();

    boolean isPrintSql();

    boolean isPrintBatch();

    boolean isIgnoreDeleteNoWhere();

    boolean isIgnoreUpdateNoWhere();
}
