package io.github.kiryu1223.drink.base.util;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTemplateExpression;
import io.github.kiryu1223.drink.base.metaData.MetaData;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class DrinkUtil {
    public static String getFirst(IConfig config, Class<?> c) {
        MetaData metaData = config.getMetaData(c);
        return metaData.getTableName().substring(0, 1).toLowerCase();
    }

    public static <T> T cast(Object o) {
        return (T) o;
    }

    public static Field findField(Class<?> clazz, String fieldName) {
        Optional<Field> any = Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.getName().equals(fieldName)).findAny();
        return any.orElseGet(() -> findField(clazz.getSuperclass(), fieldName));
    }

    public static ISqlTemplateExpression mergeTemplates(IConfig config, ISqlTemplateExpression left, ISqlTemplateExpression right) {
        List<String> strings = new ArrayList<>(left.getTemplateStrings().size() + right.getTemplateStrings().size());
        strings.addAll(left.getTemplateStrings());
        strings.addAll(right.getTemplateStrings());
        List<ISqlExpression> expressions = new ArrayList<>(left.getExpressions().size() + right.getExpressions().size());
        expressions.addAll(left.getExpressions());
        expressions.addAll(right.getExpressions());
        return config.getSqlExpressionFactory().template(strings, expressions);
    }

    /**
     * 是否为void类型
     */
    public static boolean isVoid(Class<?> c) {
        return c == Void.class || c == void.class;
    }

    /**
     * 是否为bool类型
     */
    public static boolean isBool(Class<?> type) {
        return type == boolean.class || type == Boolean.class;
    }

    /**
     * 是否为char类型
     */
    public static boolean isChar(Class<?> type) {
        return type == char.class || type == Character.class;
    }

    /**
     * 是否为string类型
     */
    public static boolean isString(Class<?> type) {
        return type == String.class;
    }

    /**
     * 是否为int类型
     */
    public static boolean isInt(Class<?> type) {
        return type == int.class || type == Integer.class;
    }

    /**
     * 是否为long类型
     */
    public static boolean isLong(Class<?> type) {
        return type == long.class || type == Long.class;
    }

    /**
     * 是否为byte类型
     */
    public static boolean isByte(Class<?> type) {
        return type == byte.class || type == Byte.class;
    }

    /**
     * 是否为datetime类型
     */
    public static boolean isDateTime(Class<?> type) {
        return type == Date.class || type == LocalDateTime.class || type == Timestamp.class;
    }

    /**
     * 是否为time类型
     */
    public static boolean isTime(Class<?> type) {
        return type == Time.class || type == LocalTime.class;
    }

    /**
     * 是否为date类型
     */
    public static boolean isDate(Class<?> type) {
        return type == java.sql.Date.class || type == LocalDate.class;
    }

    /**
     * 是否为short类型
     */
    public static boolean isShort(Class<?> type) {
        return type == short.class || type == Short.class;
    }

    /**
     * 是否为float类型
     */
    public static boolean isFloat(Class<?> type) {
        return type == float.class || type == Float.class;
    }

    /**
     * 是否为double类型
     */
    public static boolean isDouble(Class<?> type) {
        return type == double.class || type == Double.class;
    }

    /**
     * 是否为decimal类型
     */
    public static boolean isDecimal(Class<?> type) {
        return type == BigDecimal.class;
    }

    public static Class<?> getTargetType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        else if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type dbType = pType.getActualTypeArguments()[0];
            return (Class<?>) dbType;
        }
        throw new DrinkException("无法获取集合的目标类型:" + type);
    }
}
