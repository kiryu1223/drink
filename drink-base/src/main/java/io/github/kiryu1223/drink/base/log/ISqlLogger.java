package io.github.kiryu1223.drink.base.log;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.util.DrinkUtil;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public interface ISqlLogger
{
    Logger getLogger();

    IConfig getConfig();

    default void printSql(String sql)
    {
        if (getConfig().isPrintSql())
        {
            Logger logger = getLogger();
            logger.info("==>  Preparing: {}",sql);
        }
    }

    default void printValues(List<SqlValue> values)
    {
        if (getConfig().isPrintValues())
        {
            Logger logger = getLogger();
            String collect = values.stream().map(v -> DrinkUtil.showValueAndType(v.getValue())).collect(Collectors.joining(", "));
            logger.info("==> Parameters: {}",collect);
        }
    }

    default void printTotal(long total)
    {
        if(getConfig().isPrintTotal())
        {
            Logger logger = getLogger();
            logger.info("<==      Total: {}",total);
        }
    }

    default void printUpdate(long update)
    {
        if(getConfig().isPrintUpdate())
        {
            Logger logger = getLogger();
            logger.info("<==     Update: {}",update);
        }
    }

    default void printTime(long time)
    {
        if(getConfig().isPrintTime())
        {
            Logger logger = getLogger();
            logger.info("<==       Time: {}(ms)",time);
        }
    }
}
