package io.github.kiryu1223.drink.api.crud.create;

import io.github.kiryu1223.drink.api.crud.CRUD;
import io.github.kiryu1223.drink.core.sqlBuilder.InsertSqlBuilder;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.config.dialect.IDialect;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.core.session.SqlSession;
import io.github.kiryu1223.drink.ext.IConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.cast;

public abstract class InsertBase extends CRUD
{
    public final static Logger log = LoggerFactory.getLogger(InsertBase.class);

    private final InsertSqlBuilder sqlBuilder;

    protected InsertSqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }

    protected Config getConfig()
    {
        return sqlBuilder.getConfig();
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
        Config config = getConfig();
        List<SqlValue> sqlValues = new ArrayList<>();
        String sql = makeByObjects(objects, sqlValues);
        tryPrintUseDs(log,config.getDataSourceManager().getDsKey());
        tryPrintSql(log, sql);
        SqlSession session = config.getSqlSessionFactory().getSession();
        if (objects.size() > 1)
        {
            tryPrintBatch(log,objects.size());
            return session.batchExecuteUpdate(sql, objects.size(), sqlValues);
        }
        else
        {
            tryPrintNoBatch(log,objects.size());
            return session.executeUpdate(sql, sqlValues);
        }
    }

    private String makeByObjects(List<Object> objects, List<SqlValue> sqlValues)
    {
        MetaData metaData = MetaDataCache.getMetaData(getTableType());
        List<String> tableFields = new ArrayList<>();
        List<String> tableValues = new ArrayList<>();
        for (PropertyMetaData pro : metaData.getNotIgnorePropertys())
        {
            IConverter<?, ?> converter = pro.getConverter();
            tableFields.add(pro.getColumn());
            tableValues.add("?");
            if (sqlValues == null) continue;
            List<Object> values = new ArrayList<>(objects.size());
            if (pro.hasConverter())
            {
                for (Object o : objects)
                {
                    try
                    {
                        Object obj = pro.getGetter().invoke(o);
                        if (obj != null)
                        {
                            values.add(converter.toDb(cast(obj), pro));
                        }
                        else
                        {
                            values.add(null);
                        }
                    }
                    catch (IllegalAccessException | InvocationTargetException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
                sqlValues.add(new SqlValue(converter.getDbType(), values));
            }
            else
            {
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
                sqlValues.add(new SqlValue(pro.getField().getType(), values));
            }

        }
        IDialect dbConfig = getSqlBuilder().getConfig().getDisambiguation();
        return "INSERT INTO " + dbConfig.disambiguation(metaData.getTableName()) + "(" + String.join(",", tableFields)
                + ") VALUES(" + String.join(",", tableValues) + ")";
    }
}
