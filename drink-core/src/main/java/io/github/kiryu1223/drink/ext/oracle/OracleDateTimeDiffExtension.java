package io.github.kiryu1223.drink.ext.oracle;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.sqlext.BaseSqlExtension;
import io.github.kiryu1223.drink.base.sqlext.SqlTimeUnit;
import io.github.kiryu1223.drink.nnnn.expression.SqlSingleValueExpression;
import io.github.kiryu1223.drink.exception.DrinkException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OracleDateTimeDiffExtension extends BaseSqlExtension
{
    @Override
    public ISqlExpression parse(IConfig config, Method sqlFunc, List<ISqlExpression> args)
    {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        ISqlExpression unit = args.get(0);
        ISqlExpression t1 = args.get(1);
        ISqlExpression t2 = args.get(2);
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
                    templates.add("FLOOR(MONTHS_BETWEEN(" + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + "," + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ") / 12)");
                    break;
                case MONTH:
                    templates.add("MONTHS_BETWEEN(" + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + "," + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ")");
                    break;
                case WEEK:
                    templates.add("TRUNC(EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ") / 7)");
                    break;
                case DAY:
                    templates.add("EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ")");
                    break;
                case HOUR:
                    templates.add("(EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ") * 24 + EXTRACT(HOUR FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + "))");
                    break;
                case MINUTE:
                    templates.add("(EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ") * 1440 + EXTRACT(HOUR FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ") * 60 + EXTRACT(MINUTE FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + "))");
                    break;
                case SECOND:
                    templates.add("(EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ") * 86400 + EXTRACT(HOUR FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ") * 3600 + EXTRACT(MINUTE FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ") * 60 + TRUNC(EXTRACT(SECOND FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ")))");
                    break;
                case MILLISECOND:
                    templates.add("(EXTRACT(DAY FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ") * 86400000 + EXTRACT(HOUR FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ") * 3600000 + EXTRACT(MINUTE FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + ") * 60000 + TRUNC(EXTRACT(SECOND FROM " + t2TO_TIMESTAMPLeft);
                    sqlExpressions.add(t2);
                    templates.add(t2TO_TIMESTAMPRight + " - " + t1TO_TIMESTAMPLeft);
                    sqlExpressions.add(t1);
                    templates.add(t1TO_TIMESTAMPRight + "),3) * 1000)");
                    break;
                default:
                    templates.add("(0)");
            }
        }
        else
        {
            throw new DrinkException("SqlTimeUnit必须为可求值的");
        }
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}
