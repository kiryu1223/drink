package io.github.kiryu1223.drink.core.expression.ext;

import io.github.kiryu1223.drink.core.expression.SqlFromExpression;
import io.github.kiryu1223.drink.core.expression.SqlTableExpression;
import io.github.kiryu1223.drink.core.expression.factory.SqlExpressionFactory;

public class OracleSqlExpressionFactory extends SqlExpressionFactory
{
    @Override
    public SqlFromExpression from(SqlTableExpression sqlTable, int index)
    {
        return new OracleSqlFromExpression(sqlTable, index);
    }
}