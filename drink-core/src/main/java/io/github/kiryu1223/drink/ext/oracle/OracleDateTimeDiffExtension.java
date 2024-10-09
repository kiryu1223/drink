package io.github.kiryu1223.drink.ext.oracle;

import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkException;
import io.github.kiryu1223.drink.ext.BaseSqlExtension;
import io.github.kiryu1223.drink.ext.FunctionBox;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;

import java.lang.reflect.Method;
import java.util.List;

public class OracleDateTimeDiffExtension extends BaseSqlExtension
{
//    static
//    {
//        new OracleDateTimeDiffExtension();
//    }

    @Override
    public FunctionBox parse(Method sqlFunc, List<SqlExpression> args)
    {
        FunctionBox box = new FunctionBox();
        List<String> functions = box.getFunctions();
        List<SqlExpression> sqlContexts = box.getSqlExpressions();
        SqlExpression unit = args.get(0);
        SqlExpression t1 = args.get(1);
        SqlExpression t2 = args.get(2);
        if (unit instanceof SqlSingleValueExpression)
        {
            SqlSingleValueExpression sqlSingleValueExpression = (SqlSingleValueExpression) unit;
            SqlTimeUnit timeUnit = (SqlTimeUnit) sqlSingleValueExpression.getValue();
            Class<?> t1Type = sqlFunc.getParameterTypes()[1];
            Class<?> t2Type = sqlFunc.getParameterTypes()[2];
            String t1TO_TIMESTAMPLeft = t1Type == String.class ? "TO_TIMESTAMP(" : "";
            String t1TO_TIMESTAMPRight = t1Type == String.class ? ",'YYYY-MM-DD hh24:mi:ss:ff')" : "";
            String t2TO_TIMESTAMPLeft = t2Type == String.class ? "TO_TIMESTAMP(" : "";
            String t2TO_TIMESTAMPRight = t2Type == String.class ? ",'YYYY-MM-DD hh24:mi:ss:ff')" : "";
            switch (timeUnit)
            {
                case YEAR:
                    functions.add("FLOOR(MONTHS_BETWEEN(" + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + "," + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ") / 12)");
                    break;
                case MONTH:
                    functions.add("MONTHS_BETWEEN(" + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + "," + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ")");
                    break;
                case WEEK:
                    functions.add("TRUNC(EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ") / 7)");
                    break;
                case DAY:
                    functions.add("EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ")");
                    break;
                case HOUR:
                    functions.add("(EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ") * 24 + EXTRACT(HOUR FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + "))");
                    break;
                case MINUTE:
                    functions.add("(EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ") * 1440 + EXTRACT(HOUR FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ") * 60 + EXTRACT(MINUTE FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + "))");
                    break;
                case SECOND:
                    functions.add("(EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ") * 86400 + EXTRACT(HOUR FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ") * 3600 + EXTRACT(MINUTE FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ") * 60 + TRUNC(EXTRACT(SECOND FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ")))");
                    break;
                case MILLISECOND:
                    functions.add("(EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ") * 86400000 + EXTRACT(HOUR FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ") * 3600000 + EXTRACT(MINUTE FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + ") * 60000 + TRUNC(EXTRACT(SECOND FROM " + t2TO_TIMESTAMPLeft);
                    sqlContexts.add(t2);
                    functions.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlContexts.add(t1);
                    functions.add(t1TO_TIMESTAMPRight + "),3) * 1000)");
                    break;
                default:
                    functions.add("(0)");
            }
        }
        else
        {
            throw new DrinkException("SqlTimeUnit必须为可求值的");
        }
        return box;
    }
}
