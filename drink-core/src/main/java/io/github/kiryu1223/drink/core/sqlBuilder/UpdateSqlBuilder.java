package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.dialect.IDialect;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.visitor.NormalVisitor;
import io.github.kiryu1223.drink.core.visitor.SetVisitor;
import io.github.kiryu1223.expressionTree.expressions.LambdaExpression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdateSqlBuilder implements ISqlBuilder
{
    private final Config config;
    private final SqlJoinsExpression joins;
    private final SqlSetsExpression sets;
    private final SqlWhereExpression wheres;
    private final Class<?> target;
    private final SqlExpressionFactory factory;

    public UpdateSqlBuilder(Config config, Class<?> target)
    {
        this.config = config;
        this.target = target;
        factory = config.getSqlExpressionFactory();
        joins = factory.Joins();
        sets = factory.sets();
        wheres = factory.where();
    }

    public void addJoin(Class<?> target, JoinType joinType, SqlTableExpression table, SqlExpression on)
    {
        SqlJoinExpression join = factory.join(
                joinType,
                table,
                on,
                1 + joins.getJoins().size()
        );
        joins.addJoin(join);
    }

    public void addSet(SqlSetsExpression set)
    {
        sets.addSet(set.getSets());
    }

    public void addSet(SqlSetExpression set)
    {
        sets.addSet(set);
    }

    public void addWhere(SqlExpression where)
    {
        wheres.addCond(where);
    }

    public boolean hasWhere()
    {
        return !wheres.isEmpty();
    }

    @Override
    public String getSql()
    {
        return makeUpdate();
    }

    @Override
    public String getSqlAndValue(List<Object> values)
    {
        return makeUpdate();
    }

    public Config getConfig()
    {
        return config;
    }

    private String makeUpdate()
    {
        MetaData metaData = MetaDataCache.getMetaData(target);
        IDialect dbConfig = config.getDisambiguation();
        String sql = "UPDATE " + dbConfig.disambiguationTableName(metaData.getTableName()) + " AS t0";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        String joinsSqlAndValue = joins.getSql(config);
        if (!joinsSqlAndValue.isEmpty())
        {
            sb.append(" ").append(joinsSqlAndValue);
        }
        String setsSqlAndValue = sets.getSql(config);
        sb.append(" ").append(setsSqlAndValue);
        String wheresSqlAndValue = wheres.getSql(config);
        if (!wheresSqlAndValue.isEmpty())
        {
            sb.append(" ").append(wheresSqlAndValue);
        }
        return sb.toString();
    }

    private String makeUpdate(List<Object> values)
    {
        MetaData metaData = MetaDataCache.getMetaData(target);
        IDialect dbConfig = config.getDisambiguation();
        String sql = "UPDATE " + dbConfig.disambiguationTableName(metaData.getTableName()) + " AS t0";
        List<String> sb = new ArrayList<>();
        sb.add(sql);
        String joinsSqlAndValue = joins.getSqlAndValue(config, values);
        if (!joinsSqlAndValue.isEmpty())
        {
            sb.add(joinsSqlAndValue);
        }
        String setsSqlAndValue = sets.getSqlAndValue(config, values);
        sb.add(setsSqlAndValue);
        String wheresSqlAndValue = wheres.getSqlAndValue(config, values);
        if (!wheresSqlAndValue.isEmpty())
        {
            sb.add(wheresSqlAndValue);
        }
        return String.join(" ", sb);
    }
}
