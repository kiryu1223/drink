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
    private final List<PropertyMetaData> propertyMetaDataList;
    private final boolean isSingle;

    public static <T> ObjectBuilder<T> start(ResultSet resultSet, Config config, Class<T> target, List<PropertyMetaData> propertyMetaDataList, boolean isSingle)
    {
        return new ObjectBuilder<>(resultSet,config,target,propertyMetaDataList,isSingle);
    }

    private ObjectBuilder(ResultSet resultSet, Config config, Class<T> target, List<PropertyMetaData> propertyMetaDataList, boolean isSingle)
    {
        this.resultSet = resultSet;
        this.config = config;
        this.target = target;
        this.propertyMetaDataList = propertyMetaDataList;
        this.isSingle = isSingle;
    }

    public List<T> createList() throws SQLException
    {
        if (isSingle)
        {
            return getSingleList();
        }
        else
        {
            return getClassList();
        }
    }

    private List<T> getSingleList() throws SQLException
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
        Map<String, FastCreator.Setter<Object>> setterMap = new HashMap<>(propertyMetaDataList.size());
        for (PropertyMetaData metaData : propertyMetaDataList)
        {
            setterMap.put(metaData.getColumn(), fastCreator.getSetter(metaData.getSetter()));
        }
        List<T> list = new ArrayList<>();
        while (resultSet.next())
        {
            T t = creator.get();
            for (PropertyMetaData metaData : propertyMetaDataList)
            {
                Object value = resultSet.getObject(metaData.getColumn());
                setterMap.get(metaData.getColumn()).set(t,value);
            }
            list.add(t);
        }
        return list;
    }
}
