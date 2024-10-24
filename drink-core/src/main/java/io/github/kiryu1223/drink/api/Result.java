package io.github.kiryu1223.drink.api;

import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.MetaDataCache;
import io.github.kiryu1223.drink.base.metaData.PropertyMetaData;
import io.github.kiryu1223.expressionTree.expressions.annos.Getter;
import io.github.kiryu1223.expressionTree.expressions.annos.Setter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
            for (PropertyMetaData property : metaData.getPropertys())
            {
                strings.add(property.getProperty() + "=" + property.getGetter().invoke(this));
            }
            return "(" + String.join(",", strings) + ")";
        } catch (IllegalAccessException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }
}
