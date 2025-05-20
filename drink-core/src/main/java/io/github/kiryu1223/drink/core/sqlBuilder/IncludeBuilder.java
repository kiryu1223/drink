package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlCollectedValueExpression;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.session.SqlSession;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.build.ObjectBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.github.kiryu1223.drink.base.util.DrinkUtil.cast;

public class IncludeBuilder
{

    private final IConfig config;
    private final ISqlQueryableExpression subQuery;
    private final ISqlCollectedValueExpression collectedValue;
    private final boolean toMany;
    private final List<IncludeBuilder> includes = new ArrayList<>();

//    public IncludeBuilder(ISqlQueryableExpression subQuery, boolean toMany)
//    {
//        this(subQuery,null,toMany);
//    }

    public IncludeBuilder(IConfig config, ISqlQueryableExpression subQuery, ISqlCollectedValueExpression collectedValue, boolean toMany)
    {
        this.config = config;
        this.subQuery = subQuery;
        this.collectedValue = collectedValue;
        this.toMany = toMany;
    }

    public List<IncludeBuilder> getIncludes()
    {
        return includes;
    }

    public void include(Collection<?> ids, SqlSession session)
    {
        Collection<?> collection = collectedValue.getCollection();
        collection.clear();
        collection.addAll(cast(ids));
        List<SqlValue> sqlValues = new ArrayList<>(ids.size());
        String sql = subQuery.getSqlAndValue(config, sqlValues);
        List<?> objects = session.executeQuery(rs -> ObjectBuilder.start(
                        rs,
                        subQuery.getMainTableClass(),
                        subQuery.getMappingData(config),
                        false,
                        config
                )
                .createList(),
                sql,
                sqlValues
        );
    }
}
