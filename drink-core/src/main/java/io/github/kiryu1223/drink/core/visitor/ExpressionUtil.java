package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.annotation.SqlExtensionExpression;
import io.github.kiryu1223.drink.annotation.SqlOperatorMethod;
import io.github.kiryu1223.drink.api.crud.read.group.IGroup;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static boolean isSqlExtensionExpressionMethod(Method method)
    {
        SqlExtensionExpression[] annotationsByType = method.getAnnotationsByType(SqlExtensionExpression.class);
        return annotationsByType != null && annotationsByType.length > 0;
    }

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

//    public static SqlContext unBox(SqlContext context)
//    {
//        if (context instanceof SqlAsNameContext)
//        {
//            SqlAsNameContext sqlAsNameContext = (SqlAsNameContext) context;
//            return unBox(sqlAsNameContext.getContext());
//        }
//        else if (context instanceof SqlAsTableNameContext)
//        {
//            SqlAsTableNameContext sqlAsTableNameContext = (SqlAsTableNameContext) context;
//            return unBox(sqlAsTableNameContext.getContext());
//        }
//        else if (context instanceof SqlParensContext)
//        {
//            SqlParensContext sqlParensContext = (SqlParensContext) context;
//            return unBox(sqlParensContext.getContext());
//        }
//        return context;
//    }

    public static <R> R cast(Object o)
    {
        return (R) o;
    }

    public static <T> Class<T> getType(Type type, int index)
    {
        if (type instanceof ParameterizedType)
        {
            ParameterizedType pType = (ParameterizedType) type;
            Type dbType = pType.getActualTypeArguments()[index];
            return (Class<T>) dbType;
        }
        throw new RuntimeException(String.format("无法找到%s的第%s个泛型类型", type, index));
    }

    public static boolean isBool(Class<?> type)
    {
        return type == boolean.class || type == Boolean.class;
    }

    public static boolean isChar(Class<?> type)
    {
        return type == char.class || type == Character.class;
    }

    public static boolean isString(Class<?> type)
    {
        return type == String.class;
    }

    public static boolean isInt(Class<?> type)
    {
        return type == int.class || type == Integer.class;
    }

    public static boolean isByte(Class<?> type)
    {
        return type == byte.class || type == Byte.class;
    }

    public static boolean isDateTime(Class<?> type)
    {
        return type == java.util.Date.class || type == LocalDateTime.class || type == Timestamp.class;
    }

    public static boolean isDate(Class<?> type)
    {
        return type == java.sql.Date.class || type == LocalDate.class;
    }

    public static boolean isShort(Class<?> type)
    {
        return type == short.class || type == Short.class;
    }

    public static Class<?> upperClass(Class<?> c)
    {
        if (c.isPrimitive())
        {
            if (c == Character.TYPE)
            {
                return Character.class;
            }
            if (c == Byte.TYPE)
            {
                return Byte.class;
            }
            else if (c == Short.TYPE)
            {
                return Short.class;
            }
            else if (c == Integer.TYPE)
            {
                return Integer.class;
            }
            else if (c == Long.TYPE)
            {
                return Long.class;
            }
            else if (c == Float.TYPE)
            {
                return Float.class;
            }
            else if (c == Double.TYPE)
            {
                return Double.class;
            }
            else if (c == Boolean.TYPE)
            {
                return Boolean.class;
            }
            else
            {
                return Void.class;
            }
        }
        else
        {
            return c;
        }
    }
}
