package io.github.kiryu1223.drink.api.crud.create;

import io.github.kiryu1223.drink.api.crud.base.CRUD;
import io.github.kiryu1223.drink.api.crud.builder.InsertSqlBuilder;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.inter.IDBConfig;
import io.github.kiryu1223.drink.core.builder.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class InsertBase extends CRUD
{
    private final InsertSqlBuilder sqlBuilder;

    protected InsertSqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }

    public InsertBase(Config c)
    {
        this.sqlBuilder = new InsertSqlBuilder(c);
    }

    public long executeRows()
    {
        List<Object> objects = getObjects();
        if (!objects.isEmpty())
        {
            return objectsExecuteRows(objects);
        }
        else
        {
            return 0;
        }
    }

    public String toSql()
    {
        List<Object> objects = getObjects();
        if (!objects.isEmpty())
        {
            return makeByObjects(objects, null);
        }
        else
        {
            return sqlBuilder.getSql();
        }
    }

    protected <T> List<T> getObjects()
    {
        return Collections.emptyList();
    }

    protected abstract <T> Class<T> getTableType();

    private long objectsExecuteRows(List<Object> objects)
    {
        List<SqlValue> sqlValues = new ArrayList<>();
        String sql = makeByObjects(objects, sqlValues);
        System.out.println("===> " + sql);
        String key = sqlBuilder.getConfig().getDsKey();
        System.out.println("使用数据源: " + (key == null ? "默认" : key));
        SqlSession session = SqlSessionBuilder.getSession(sqlBuilder.getConfig().getDsKey());
        if (objects.size() > 1)
        {
            System.out.println("batchExecute");
            return session.batchExecuteUpdate(sql, objects.size(), sqlValues);
        }
        else
        {
            System.out.println("noBatchExecute");
            return session.executeUpdate(sql, sqlValues);
        }
    }

    private String makeByObjects(List<Object> objects, List<SqlValue> sqlValues)
    {
        MetaData metaData = MetaDataCache.getMetaData(getTableType());
        List<String> tableFields = new ArrayList<>();
        List<String> tableValues = new ArrayList<>();
        for (PropertyMetaData pro : metaData.getColumns().values())
        {
            tableFields.add(pro.getColumn());
            tableValues.add("?");
            if (sqlValues == null) continue;
            {
                List<Object> values = new ArrayList<>(objects.size());
                for (Object o : objects)
                {
                    try
                    {
                        values.add(pro.getGetter().invoke(o));
                    }
                    catch (IllegalAccessException | InvocationTargetException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
                sqlValues.add(new SqlValue(pro.getType(), values));
            }
        }
        IDBConfig dbConfig = getSqlBuilder().getConfig().getDbConfig();
        return "INSERT INTO " + dbConfig.propertyDisambiguation(metaData.getTableName()) + "(" + String.join(",", tableFields)
                + ") VALUES(" + String.join(",", tableValues) + ")";
    }
}
