package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.annotation.Column;
import io.github.kiryu1223.drink.annotation.Table;
import io.github.kiryu1223.drink.api.crud.read.group.IGroup;
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

    public static String propertyName(Method method)
    {
        try
        {
            String fieldName = method.getName();
            if (fieldName.startsWith("get") || fieldName.startsWith("set"))
            {
                fieldName = fieldName.substring(3);
            }
            else // is
            {
                fieldName = fieldName.substring(2);
            }
            Field field = method.getDeclaringClass().getDeclaredField(toLowerCaseFirstOne(fieldName));
            return propertyName(field);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String propertyName(Field field)
    {
        Column column = field.getAnnotation(Column.class);
        if (column == null || column.value().isEmpty())
        {
            return field.getName();
        }
        else
        {
            return column.value();
        }
    }

    public static String toLowerCaseFirstOne(String s)
    {
        if (Character.isLowerCase(s.charAt(0)))
        {
            return s;
        }
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    public static String getTableName(Class<?> target)
    {
        Table table = target.getAnnotation(Table.class);
        if (table == null || table.value().isEmpty())
        {
            return target.getSimpleName();
        }
        else
        {
            return table.value();
        }
    }
}
