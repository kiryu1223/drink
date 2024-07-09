package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.annotation.SqlOperatorMethod;
import io.github.kiryu1223.drink.api.crud.read.group.IGroup;
import io.github.kiryu1223.drink.core.context.SqlAsNameContext;
import io.github.kiryu1223.drink.core.context.SqlAsTableNameContext;
import io.github.kiryu1223.drink.core.context.SqlContext;
import io.github.kiryu1223.drink.core.context.SqlParensContext;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ExpressionUtil
{
    public static boolean isProperty(List<ParameterExpression> parameters, MethodCallExpression methodCall)
    {
        if (methodCall.getExpr().getKind() != Kind.Parameter) return false;
        ParameterExpression parameter = (ParameterExpression) methodCall.getExpr();
        return parameters.contains(parameter);
    }

    public static boolean isProperty(List<ParameterExpression> parameters, FieldSelectExpression fieldSelect)
    {
        if (fieldSelect.getExpr().getKind() != Kind.Parameter) return false;
        ParameterExpression parameter = (ParameterExpression) fieldSelect.getExpr();
        return parameters.contains(parameter);
    }

    public static boolean isGroupKey(List<ParameterExpression> parameters, Expression expression)
    {
        if (expression instanceof FieldSelectExpression)
        {
            FieldSelectExpression fieldSelect = (FieldSelectExpression) expression;
            Field field = fieldSelect.getField();
            return fieldSelect.inParameters(parameters)
                    && IGroup.class.isAssignableFrom(field.getDeclaringClass())
                    && field.getName().equals("key");
        }
        return false;
    }

    public static boolean isGetter(Method method)
    {
        if (isVoid(method.getReturnType())) return false;
        if (method.getParameterCount() != 0) return false;
        String name = method.getName();
        return name.startsWith("get") || name.startsWith("is");
    }

    public static boolean isSetter(Method method)
    {
        if (method.getParameterCount() != 1) return false;
        String name = method.getName();
        return name.startsWith("set");
    }

    public static boolean isVoid(Class<?> c)
    {
        return c == Void.class || c == void.class;
    }

    public static boolean isSqlOperatorMethod(Method method)
    {
        return method.isAnnotationPresent(SqlOperatorMethod.class);
    }
//    public static String propertyName(Method method)
//    {
//        try
//        {
//            String fieldName = method.getName();
//            if (fieldName.startsWith("get") || fieldName.startsWith("set"))
//            {
//                fieldName = fieldName.substring(3);
//            }
//            else // is
//            {
//                fieldName = fieldName.substring(2);
//            }
//            Field field = method.getDeclaringClass().getDeclaredField(toLowerCaseFirstOne(fieldName));
//            return propertyName(field);
//        }
//        catch (NoSuchFieldException e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static String propertyName(Field field)
//    {
//        Column column = field.getAnnotation(Column.class);
//        if (column == null || column.value().isEmpty())
//        {
//            return field.getName();
//        }
//        else
//        {
//            return column.value();
//        }
//    }

    public static String toLowerCaseFirstOne(String s)
    {
        if (Character.isLowerCase(s.charAt(0)))
        {
            return s;
        }
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }


    public static String firstUpperCase(String original)
    {
        if (!original.isEmpty())
        {
            return Character.toUpperCase(original.charAt(0)) + original.substring(1);
        }
        return original;
    }

    public static SqlContext unBox(SqlContext context)
    {
        if (context instanceof SqlAsNameContext)
        {
            SqlAsNameContext sqlAsNameContext = (SqlAsNameContext) context;
            return unBox(sqlAsNameContext.getContext());
        }
        else if (context instanceof SqlAsTableNameContext)
        {
            SqlAsTableNameContext sqlAsTableNameContext = (SqlAsTableNameContext) context;
            return unBox(sqlAsTableNameContext.getContext());
        }
        else if (context instanceof SqlParensContext)
        {
            SqlParensContext sqlParensContext = (SqlParensContext) context;
            return unBox(sqlParensContext.getContext());
        }
        return context;
    }

}
