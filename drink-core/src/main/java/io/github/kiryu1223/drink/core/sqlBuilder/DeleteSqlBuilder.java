package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.dialect.IDialect;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeleteSqlBuilder implements ISqlBuilder
{
    private final Config config;
    private final SqlJoinsExpression joins;
    private final SqlWhereExpression wheres;
    private final Class<?> target;
    private Set<Integer> excludes = new HashSet<>();
    private final SqlExpressionFactory factory;
    private final List<Class<?>> orderedClasses = new ArrayList<>();

    public DeleteSqlBuilder(Config config, Class<?> target)
    {
        this.config = config;
        this.target = target;
        factory = config.getSqlExpressionFactory();
        joins = factory.Joins();
        wheres = factory.where();
        orderedClasses.add(target);
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
        orderedClasses.add(table.getTableClass());
    }

    public void addExclude(Class<?> c)
    {
        excludes.add(orderedClasses.indexOf(c));
    }

    public void addWhere(SqlExpression where)
    {
        wheres.addCond(where);
    }

    @Override
    public Config getConfig()
    {
        return config;
    }

    public boolean hasWhere()
    {
        return !wheres.isEmpty();
    }

    @Override
    public String getSql()
    {
        List<String> strings = new ArrayList<>(3);
        String sql = makeDelete();
        strings.add(sql);
        String joinsSql = joins.getSql(config);
        if (!joinsSql.isEmpty())
        {
            strings.add(joinsSql);
        }
        String wheresSql = wheres.getSql(config);
        if (!wheresSql.isEmpty())
        {
            strings.add(wheresSql);
        }
        return String.join(" ", strings);
    }

    @Override
    public String getSqlAndValue(List<Object> values)
    {
        List<String> strings = new ArrayList<>(3);
        String sql = makeDelete();
        strings.add(sql);
        String joinsSql = joins.getSqlAndValue(config, values);
        if (!joinsSql.isEmpty())
        {
            strings.add(joinsSql);
        }
        String wheresSql = wheres.getSqlAndValue(config, values);
        if (!wheresSql.isEmpty())
        {
            strings.add(wheresSql);
        }
        return String.join(" ", strings);
    }

    private String makeDelete()
    {
        StringBuilder builder = new StringBuilder("DELETE");
        if (!excludes.isEmpty())
        {
            builder.append(" ");
            List<String> strings = new ArrayList<>(excludes.size());
            for (int index : excludes)
            {
                if (index != -1)
                {
                    strings.add("t" + index);
                }
            }
            builder.append(String.join(",", strings));
        }
        IDialect dbConfig = config.getDisambiguation();
        MetaData metaData = MetaDataCache.getMetaData(target);
        return builder.append(" FROM ").append(dbConfig.disambiguation(metaData.getTableName())).append(" AS t0").toString();
    }
}
