package io.github.kiryu1223.drink.core.visitor.methods;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.exception.DrinkException;
import io.github.kiryu1223.drink.ext.DbType;

import java.util.*;

public class StringMethods
{
    public static SqlBinaryExpression contains(Config config, SqlExpression left, SqlExpression right)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        if (config.getDbType() == DbType.Oracle)
        {
            functions = Arrays.asList("('%'||", "||'%')");
        }
        else
        {
            functions = Arrays.asList("CONCAT('%',", ",'%')");
        }
        SqlFunctionExpression function = factory.function(functions, Collections.singletonList(right));
        return factory.binary(SqlOperator.LIKE, left, function);
    }

    public static SqlBinaryExpression startsWith(Config config, SqlExpression left, SqlExpression right)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        SqlFunctionExpression function = factory.function(Arrays.asList("CONCAT(", ",'%')"), Collections.singletonList(right));
        return factory.binary(SqlOperator.LIKE, left, function);
    }

    public static SqlBinaryExpression endsWith(Config config, SqlExpression left, SqlExpression right)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        SqlFunctionExpression function = factory.function(Arrays.asList("CONCAT('%',", ")"), Collections.singletonList(right));
        return factory.binary(SqlOperator.LIKE, left, function);
    }

    public static SqlFunctionExpression length(Config config, SqlExpression thiz)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        switch (config.getDbType())
        {
            case SqlServer:
                functions = Arrays.asList("LEN(", ")");
                break;
            case Oracle:
                functions = Arrays.asList("LENGTH(", ")");
                break;
            case MySQL:
            case H2:
            default:
                functions = Arrays.asList("CHAR_LENGTH(", ")");
        }
        return factory.function(functions, Collections.singletonList(thiz));
    }

    public static SqlFunctionExpression toUpperCase(Config config, SqlExpression thiz)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions = Arrays.asList("UPPER(", ")");
        return factory.function(functions, Collections.singletonList(thiz));
    }

    public static SqlFunctionExpression toLowerCase(Config config, SqlExpression thiz)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions = Arrays.asList("LOWER(", ")");
        return factory.function(functions, Collections.singletonList(thiz));
    }

    public static SqlFunctionExpression concat(Config config, SqlExpression left, SqlExpression right)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions = Arrays.asList("CONCAT(", ",", ")");
        return factory.function(functions, Arrays.asList(left, right));
    }

    public static SqlFunctionExpression trim(Config config, SqlExpression thiz)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions = Arrays.asList("TRIM(", ")");
        return factory.function(functions, Collections.singletonList(thiz));
    }

    public static SqlExpression isEmpty(Config config, SqlExpression thiz)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        SqlParensExpression parens = factory.parens(factory.binary(SqlOperator.EQ, length(config, thiz), factory.constString("0")));
        if (config.getDbType() == DbType.SqlServer)
        {
            return factory.function(Arrays.asList("IIF(", ",1,0)"), Collections.singletonList(parens));
        }
        else
        {
            return parens;
        }
    }

    public static SqlFunctionExpression indexOf(Config config, SqlExpression thisStr, SqlExpression subStr)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<SqlExpression> sqlExpressions;
        switch (config.getDbType())
        {
            case SqlServer:
                functions = Arrays.asList("CHARINDEX(", ",", ")");
                sqlExpressions = Arrays.asList(subStr, thisStr);
                break;
            case Oracle:
            case MySQL:
            case H2:
            default:
                functions = Arrays.asList("INSTR(", ",", ")");
                sqlExpressions = Arrays.asList(thisStr, subStr);
        }
        return factory.function(functions, sqlExpressions);
    }

    public static SqlFunctionExpression indexOf(Config config, SqlExpression thisStr, SqlExpression subStr, SqlExpression fromIndex)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<SqlExpression> sqlExpressions;
        switch (config.getDbType())
        {
            case SqlServer:
                functions = Arrays.asList("CHARINDEX(", ",", ",", ")");
                sqlExpressions = Arrays.asList(subStr, thisStr, fromIndex);
                break;
            case Oracle:
                functions = Arrays.asList("INSTR(", ",", ",", ")");
                sqlExpressions = Arrays.asList(thisStr, subStr, fromIndex);
                break;
            case MySQL:
            case H2:
            default:
                functions = Arrays.asList("LOCATE(", ",", ",", ")");
                sqlExpressions = Arrays.asList(subStr, thisStr, fromIndex);
        }
        return factory.function(functions, sqlExpressions);
    }

    public static SqlFunctionExpression replace(Config config, SqlExpression thisStr, SqlExpression oldStr, SqlExpression newStr)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions = Arrays.asList("REPLACE(", ",", ",", ")");
        List<SqlExpression> sqlExpressions = Arrays.asList(thisStr, oldStr, newStr);
        return factory.function(functions, sqlExpressions);
    }

    public static SqlFunctionExpression substring(Config config, SqlExpression thisStr, SqlExpression beginIndex)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<SqlExpression> sqlExpressions;
        switch (config.getDbType())
        {
            case SqlServer:
                functions = Arrays.asList("SUBSTRING(", ",", ",LEN(", ") - (", " - 1))");
                sqlExpressions = Arrays.asList(thisStr, beginIndex, thisStr, beginIndex);
                break;
            case Oracle:
            case MySQL:
            case H2:
            default:
                functions = Arrays.asList("SUBSTR(", ",", ")");
                sqlExpressions = Arrays.asList(thisStr, beginIndex);
                break;
        }
        return factory.function(functions, sqlExpressions);
    }

    public static SqlFunctionExpression substring(Config config, SqlExpression thisStr, SqlExpression beginIndex, SqlExpression endIndex)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<SqlExpression> sqlExpressions = Arrays.asList(thisStr, beginIndex, endIndex);
        switch (config.getDbType())
        {
            case SqlServer:
                functions = Arrays.asList("SUBSTRING(", ",", ",", ")");
                break;
            case Oracle:
            case MySQL:
            case H2:
            default:
                functions = Arrays.asList("SUBSTR(", ",", ",", ")");
        }
        return factory.function(functions, sqlExpressions);
    }

    public static SqlFunctionExpression joinArray(Config config, SqlExpression delimiter, List<SqlExpression> elements)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions = new ArrayList<>();
        List<SqlExpression> sqlExpressions = new ArrayList<>(1 + elements.size());
        sqlExpressions.add(delimiter);
        sqlExpressions.addAll(elements);
        switch (config.getDbType())
        {
            case Oracle:
                functions.add("(");
                functions.add("||");
                for (int i = 0; i < sqlExpressions.size(); i++)
                {
                    if (i < sqlExpressions.size() - 2) functions.add("||");
                }
                functions.add(")");
                break;
            case MySQL:
            case SqlServer:
            case H2:
            default:
                functions.add("CONCAT_WS(");
                functions.add(",");
                for (int i = 0; i < sqlExpressions.size(); i++)
                {
                    if (i < sqlExpressions.size() - 2) functions.add(",");
                }
                functions.add(")");
        }
        return factory.function(functions, sqlExpressions);
    }

    public static SqlFunctionExpression joinList(Config config, SqlExpression delimiter, SqlExpression elements)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        if (elements instanceof SqlCollectedValueExpression)
        {
            if (config.getDbType() == DbType.Oracle)
            {
                SqlCollectedValueExpression expression = (SqlCollectedValueExpression) elements;
                expression.setDelimiter("||");
            }
        }
        else
        {
            throw new DrinkException("String.join()的第二个参数必须是java中能获取到的");
        }
        List<String> functions = new ArrayList<>();
        List<SqlExpression> sqlExpressions = Arrays.asList(delimiter, elements);
        switch (config.getDbType())
        {
            case Oracle:
                functions.add("(");
                functions.add("||");
                functions.add(")");
                break;
            case MySQL:
            case SqlServer:
            case H2:
            default:
                functions.add("CONCAT_WS(");
                functions.add(",");
                functions.add(")");
        }
        return factory.function(functions, sqlExpressions);
    }
}
