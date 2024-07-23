package io.github.kiryu1223.drink.api;

import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.expressionTree.expressions.annos.Getter;
import io.github.kiryu1223.expressionTree.expressions.annos.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Result
{
    @Override
    public String toString()
    {
        try
        {
            List<String> strings = new ArrayList<>();
            MetaData metaData = MetaDataCache.getMetaData(this.getClass());
            for (Map.Entry<String, PropertyMetaData> entry : metaData.getColumns().entrySet())
            {
                strings.add(entry.getKey() + "=" + entry.getValue().getGetter().invoke(this));
            }
            return "(" + String.join(",", strings) + ")";
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }
}
