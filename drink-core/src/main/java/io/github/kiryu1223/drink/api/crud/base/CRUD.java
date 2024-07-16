package io.github.kiryu1223.drink.api.crud.base;


import io.github.kiryu1223.drink.config.Config;
import org.slf4j.Logger;

public abstract class CRUD
{
    protected abstract Config getConfig();

    protected void tryPrintSql(Logger log, String sql)
    {
        if (getConfig().isPrintSql())
        {
            log.info("==> {}", sql);
        }
    }

    protected void tryPrintUseDs(Logger log, String ds)
    {
        if (getConfig().isPrintUseDs())
        {
            log.info("current use datasource:{}", ds == null ? "default" : ds);
        }
    }

    protected void tryPrintBatch(Logger log, long count)
    {
        if (getConfig().isPrintBatch())
        {
            log.info("dataSize:{} use batch execute",count);
        }
    }

    protected void tryPrintNoBatch(Logger log, long count)
    {
        if (getConfig().isPrintBatch())
        {
            log.info("dataSize:{} use normal execute",count);
        }
    }
}
