package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.core.expression.SqlExpression;


import java.lang.reflect.Method;
import java.util.List;

public abstract class BaseSqlExtension
{
    public abstract FunctionBox parse(Method sqlFunc,List<SqlExpression> args);
}
