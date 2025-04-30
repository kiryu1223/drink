package io.github.kiryu1223.drink.base;

import io.github.kiryu1223.drink.base.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.base.expression.ISqlFromExpression;
import io.github.kiryu1223.drink.base.expression.ISqlJoinsExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.session.SqlSessionFactory;
import io.github.kiryu1223.drink.base.toBean.Include.IncludeFactory;
import io.github.kiryu1223.drink.base.toBean.beancreator.BeanCreatorFactory;
import io.github.kiryu1223.drink.base.transaction.TransactionManager;
import io.github.kiryu1223.drink.base.transform.Transformer;
import io.github.kiryu1223.drink.base.visitor.ISqlVisitor;

public interface IConfig
{
    SqlExpressionFactory getSqlExpressionFactory();

    IDialect getDisambiguation();

    DataSourceManager getDataSourceManager();

    TransactionManager getTransactionManager();

    SqlSessionFactory getSqlSessionFactory();

    IncludeFactory getIncludeFactory();

    BeanCreatorFactory getBeanCreatorFactory();

    Transformer getTransformer();

    Filter getFilter();

    ISqlVisitor getSqlVisitor(ISqlFromExpression fromExpression, ISqlJoinsExpression joins, int index);

    DbType getDbType();

    boolean isPrintSql();

    boolean isPrintBatch();

    boolean isIgnoreDeleteNoWhere();

    boolean isIgnoreUpdateNoWhere();
}
