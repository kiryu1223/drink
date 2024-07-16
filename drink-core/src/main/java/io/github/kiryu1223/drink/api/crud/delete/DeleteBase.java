package io.github.kiryu1223.drink.api.crud.delete;

import io.github.kiryu1223.drink.api.crud.base.CRUD;
import io.github.kiryu1223.drink.api.crud.builder.DeleteSqlBuilder;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class DeleteBase extends CRUD
{
    public final static Logger log = LoggerFactory.getLogger(DeleteBase.class);

    private final DeleteSqlBuilder sqlBuilder;

    public DeleteBase(Config config)
    {
        this.sqlBuilder = new DeleteSqlBuilder(config);
    }

    protected DeleteSqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }

    public Config getConfig()
    {
        return sqlBuilder.getConfig();
    }

    protected void setDeleteTable(Class<?> c)
    {
        sqlBuilder.setDeleteTable(c);
    }

    public long executeRows()
    {
        Config config = getConfig();
        checkHasWhere();
        List<Object> values = new ArrayList<>();
        String sql = sqlBuilder.getSqlAndValue(values);
        tryPrintUseDs(log,config.getDataSourceManager().getDsKey());
        tryPrintSql(log, sql);
        SqlSession session = config.getSqlSessionFactory().getSession();
        return session.executeUpdate(sql, values);
    }

    public String toSql()
    {
        return sqlBuilder.getSql();
    }

    private void checkHasWhere()
    {
        if (!sqlBuilder.hasWhere())
        {
            throw new RuntimeException("DELETE没有条件");
        }
    }
}
