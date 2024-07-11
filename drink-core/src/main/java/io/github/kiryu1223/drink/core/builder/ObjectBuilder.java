package io.github.kiryu1223.drink.core.builder;

import io.github.kiryu1223.drink.config.Config;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
        return new ObjectBuilder<>(resultSet, config, target, propertyMetaDataList, isSingle);
    }

    private ObjectBuilder(ResultSet resultSet, Config config, Class<T> target, List<PropertyMetaData> propertyMetaDataList, boolean isSingle)
    {
        this.resultSet = resultSet;
        this.config = config;
        this.target = target;
        this.propertyMetaDataList = propertyMetaDataList;
        this.isSingle = isSingle;
    }

    public List<T> createList() throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException
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

    private List<T> getSingleList() throws SQLException, NoSuchFieldException, IllegalAccessException
    {
        List<T> list = new ArrayList<>();
        while (resultSet.next())
        {
            T t = defConvertValue();
            list.add(t);
        }
        return list;
    }

    private List<T> getClassList() throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException
    {
        FastCreator<T> fastCreator = new FastCreator<>(target);
        Supplier<T> creator = fastCreator.getCreator();
//        Map<String, FastCreator.Setter<T>> setterMap = new HashMap<>(propertyMetaDataList.size());
//        for (PropertyMetaData metaData : propertyMetaDataList)
//        {
//            setterMap.put(metaData.getColumn(), fastCreator.getSetter(metaData.getType(), metaData.getSetter(), config.getLookup()));
//        }
        List<T> list = new ArrayList<>();
        while (resultSet.next())
        {
            T t = creator.get();
            for (PropertyMetaData metaData : propertyMetaDataList)
            {
                Object value = convertValue(metaData);
                metaData.getSetter().invoke(t, value);
                //setterMap.get(metaData.getColumn()).set(t,value);
            }
            list.add(t);
        }
        return list;
    }

    private Object convertValue(PropertyMetaData metaData) throws NoSuchFieldException, SQLException, IllegalAccessException
    {
        if (!metaData.isHasConverter())
        {
            Class<?> type = metaData.getType();
            if (type.isEnum())
            {
                String Enum = resultSet.getString(metaData.getColumn());
                return type.getField(Enum).get(null);
            }
            else
            {
                return resultSet.getObject(metaData.getColumn(), type);
            }
        }
        else
        {
            Object temp = resultSet.getObject(metaData.getColumn());
            return metaData.getConverter().toJava(cast(temp), metaData);
        }
    }

    private T defConvertValue() throws SQLException, NoSuchFieldException, IllegalAccessException
    {
        if (target.isEnum())
        {
            String Enum = resultSet.getString(1);
            return (T) target.getField(Enum).get(null);
        }
        else
        {
            return resultSet.getObject(1, target);
        }
    }

    private <R> R cast(Object o)
    {
        return (R) o;
    }

//    private MethodHandles.Lookup tryGetLookUp(Object o)
//    {
//        if (o instanceof ILookUp)
//        {
//            ILookUp iLookUp = (ILookUp) o;
//            return iLookUp.lookup();
//        }
//        else
//        {
//            return MethodHandles.lookup();
//        }
//    }
}
