package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.config.Config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ObjectBuilder<T>
{
    private final ResultSet resultSet;
    private final Config config;
    private final Class<T> target;
    private final List<ColumnMetaData> columnMetaDataList;

    public ObjectBuilder(ResultSet resultSet, Config config, Class<T> target, List<ColumnMetaData> columnMetaDataList)
    {
        this.resultSet = resultSet;
        this.config = config;
        this.target = target;
        this.columnMetaDataList = columnMetaDataList;
    }

    public List<T> createList() throws SQLException
    {
        if (target.isPrimitive())
        {
            return getSignlList();
        }
        else
        {
            return getClassList();
        }
    }

    private List<T> getSignlList() throws SQLException
    {
        List<T> list = new ArrayList<>();
        while (resultSet.next())
        {
            T t = (T) resultSet.getObject(1, target);
            list.add(t);
        }
        return list;
    }

    private List<T> getClassList() throws SQLException
    {
        FastCreator<T> fastCreator = new FastCreator<>(target);
        Supplier<T> creator = fastCreator.getCreator();
        Map<String, FastCreator.Setter<Object>> setterMap = new HashMap<>(columnMetaDataList.size());
        for (ColumnMetaData column : columnMetaDataList)
        {
            setterMap.put(column.getColumnName(), fastCreator.getSetter(column.getColumnSetter()));
        }
        List<T> list = new ArrayList<>();
        while (resultSet.next())
        {
            T t = creator.get();
            for (ColumnMetaData column : columnMetaDataList)
            {
                Object value = resultSet.getObject(column.getColumnName());
                setterMap.get(column.getColumnName()).set(t,value);
            }
            list.add(t);
        }
        return list;
    }
}
