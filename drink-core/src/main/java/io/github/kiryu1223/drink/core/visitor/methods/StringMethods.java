package io.github.kiryu1223.drink.core.visitor.methods;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.*;
import io.github.kiryu1223.drink.exception.DrinkException;
import io.github.kiryu1223.drink.ext.DbType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringMethods
{
    public static SqlBinaryExpression contains(Config config, SqlExpression left, SqlExpression right)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        switch (config.getDbType())
        {
            case Oracle:
            case SQLite:
                functions = Arrays.asList("('%'||", "||'%')");
                break;
            default:
                functions = Arrays.asList("CONCAT('%',", ",'%')");
                break;
        }
        SqlFunctionExpression function = factory.function(functions, Collections.singletonList(right));
        return factory.binary(SqlOperator.LIKE, left, function);
    }

    public static SqlBinaryExpression startsWith(Config config, SqlExpression left, SqlExpression right)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<SqlExpression> args = Collections.singletonList(right);
        switch (config.getDbType())
        {
            case SQLite:
                functions = Arrays.asList("(", "||'%')");
                break;
            default:
                functions = Arrays.asList("CONCAT(", ",'%')");
                break;
        }
        return factory.binary(SqlOperator.LIKE, left, factory.function(functions, args));
    }

    public static SqlBinaryExpression endsWith(Config config, SqlExpression left, SqlExpression right)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<SqlExpression> args = Collections.singletonList(right);
        switch (config.getDbType())
        {
            case SQLite:
                functions = Arrays.asList("('%'||", ")");
                break;
            default:
                functions = Arrays.asList("CONCAT('%',", ")");
                break;
        }
        return factory.binary(SqlOperator.LIKE, left, factory.function(functions, args));
    }

    public static SqlFunctionExpression length(Config config, SqlExpression thiz)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        switch (config.getDbType())
        {
            case SQLServer:
                functions = Arrays.asList("LEN(", ")");
                break;
            case Oracle:
                functions = Arrays.asList("NVL(LENGTH(", "),0)");
                break;
            case SQLite:
            case PostgreSQL:
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
        List<String> functions;
        switch (config.getDbType())
        {
            case SQLite:
                functions = Arrays.asList("(", "||", ")");
                break;
            default:
                functions = Arrays.asList("CONCAT(", ",", ")");
        }
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
        switch (config.getDbType())
        {
            case SQLServer:
                return factory.parens(factory.binary(SqlOperator.EQ, factory.function(Arrays.asList("DATALENGTH(", ")"), Collections.singletonList(thiz)), factory.constString("0")));
            default:
                return factory.parens(factory.binary(SqlOperator.EQ, length(config, thiz), factory.constString("0")));
        }
//        if (config.getDbType() == DbType.SQLServer)
//        {
//            return factory.function(Arrays.asList("IIF(", ",1,0)"), Collections.singletonList(parens));
//        }
//        else
//        {
//            return parens;
//        }
    }

    public static SqlFunctionExpression indexOf(Config config, SqlExpression thisStr, SqlExpression subStr)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<SqlExpression> sqlExpressions;
        switch (config.getDbType())
        {
            case SQLServer:
                functions = Arrays.asList("CHARINDEX(", ",", ")");
                sqlExpressions = Arrays.asList(subStr, thisStr);
                break;
            case PostgreSQL:
                functions = Arrays.asList("STRPOS(", ",", ")");
                sqlExpressions = Arrays.asList(thisStr, subStr);
                break;
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
            case SQLServer:
                functions = Arrays.asList("CHARINDEX(", ",", ",", ")");
                sqlExpressions = Arrays.asList(subStr, thisStr, fromIndex);
                break;
            case Oracle:
                functions = Arrays.asList("INSTR(", ",", ",", ")");
                sqlExpressions = Arrays.asList(thisStr, subStr, fromIndex);
                break;
            case SQLite:
                functions = Arrays.asList("(INSTR(SUBSTR(", ",", " + 1),", ") + ", ")");
                sqlExpressions = Arrays.asList(thisStr, fromIndex, subStr, fromIndex);
                break;
            case PostgreSQL:
                functions = Arrays.asList("(STRPOS(SUBSTR(", ",", " + 1),", ") + ", ")");
                sqlExpressions = Arrays.asList(thisStr, fromIndex, subStr, fromIndex);
                break;
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
            case SQLServer:
                functions = Arrays.asList("SUBSTRING(", ",", ",LEN(", ") - (", " - 1))");
                sqlExpressions = Arrays.asList(thisStr, beginIndex, thisStr, beginIndex);
                break;
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
            case SQLServer:
                functions = Arrays.asList("SUBSTRING(", ",", ",", ")");
                break;
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
        switch (config.getDbType())
        {
            case Oracle:
            case SQLite:
                functions.add("(");
                for (int i = 0; i < elements.size(); i++)
                {
                    sqlExpressions.add(elements.get(i));
                    if (i < elements.size() - 1)
                    {
                        functions.add("||");
                        sqlExpressions.add(delimiter);
                        functions.add("||");
                    }
                }
                functions.add(")");
                break;
            default:
                sqlExpressions.add(delimiter);
                sqlExpressions.addAll(elements);
                functions.add("CONCAT_WS(");
                functions.add(",");
                for (int i = 0; i < sqlExpressions.size(); i++)
                {
                    if (i < elements.size() - 1) functions.add(",");
                }
                functions.add(")");
        }
        return factory.function(functions, sqlExpressions);
    }

    public static SqlFunctionExpression joinList(Config config, SqlExpression delimiter, SqlExpression elements)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<String> functions;
        List<SqlExpression> sqlExpressions;
        if (elements instanceof SqlCollectedValueExpression)
        {
            if (config.getDbType() == DbType.Oracle || config.getDbType() == DbType.SQLite)
            {
                SqlCollectedValueExpression expression = (SqlCollectedValueExpression) elements;
                List<Object> collection = new ArrayList<>(expression.getCollection());
                functions = new ArrayList<>(collection.size() * 2);
                sqlExpressions = new ArrayList<>(collection.size() * 2);
                functions.add("(");
                for (int i = 0; i < collection.size(); i++)
                {
                    sqlExpressions.add(factory.value(collection.get(i)));
                    if (i < collection.size() - 1)
                    {
                        functions.add("||");
                        sqlExpressions.add(delimiter);
                        functions.add("||");
                    }
                }
                functions.add(")");
            }
            else
            {
                functions = new ArrayList<>();
                sqlExpressions = Arrays.asList(delimiter, elements);
            }
        }
        else
        {
            throw new DrinkException("String.join()的第二个参数必须是java中能获取到的");
        }
        switch (config.getDbType())
        {
            case Oracle:
            case SQLite:
                break;
            default:
                functions.add("CONCAT_WS(");
                functions.add(",");
                functions.add(")");
        }
        return factory.function(functions, sqlExpressions);
    }
}
