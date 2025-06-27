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

    default void printSql(IConfig config,String sql)
    {
        if (config.isPrintSql())
        {
            Logger logger = getLogger();
            logger.info("==>  Preparing: {}",sql);
        }
    }

    default void printValues(IConfig config,List<SqlValue> values)
    {
        if (config.isPrintValues())
        {
            Logger logger = getLogger();
            String collect = values.stream().map(v -> DrinkUtil.showValueAndType(v.getValue())).collect(Collectors.joining(", "));
            logger.info("==> Parameters: {}",collect);
        }
    }

    default void printTotal(IConfig config,long total)
    {
        if(config.isPrintTotal())
        {
            Logger logger = getLogger();
            logger.info("<==      Total: {}",total);
        }
    }
}
