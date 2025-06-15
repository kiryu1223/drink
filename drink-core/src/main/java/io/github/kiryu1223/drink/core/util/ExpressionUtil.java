/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.kiryu1223.drink.core.util;

import io.github.kiryu1223.drink.base.sqlExt.SqlExtensionExpression;
import io.github.kiryu1223.drink.base.sqlExt.SqlOperatorMethod;
import io.github.kiryu1223.drink.base.toBean.beancreator.IGetterCaller;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import io.github.kiryu1223.drink.core.api.ITable;
import io.github.kiryu1223.drink.core.api.crud.read.IDynamicTable;
import io.github.kiryu1223.drink.core.api.crud.read.pivot.Pivoted;
import io.github.kiryu1223.drink.core.api.crud.read.pivot.UnPivoted;
import io.github.kiryu1223.expressionTree.expressions.MethodCallExpression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * 表达式工具
 *
 * @author kiryu1223
 * @since 3.0
 */
public class ExpressionUtil {

    public static boolean isDrinkList(Class<?> c) {
        return io.github.kiryu1223.drink.core.util.List.class.isAssignableFrom(c);
    }

    public static boolean isDynamicColumn(Method method) {
        String name = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        return IDynamicTable.class.isAssignableFrom(method.getDeclaringClass())
               && name.equals("column") && parameterTypes.length == 2 && isString(parameterTypes[0])
               && parameterTypes[1] == Class.class;
    }

    public static boolean isEquals(MethodCallExpression methodCall) {
        Method method = methodCall.getMethod();
        if (method.getParameterCount() != 1) return false;
        if (method.getParameterTypes()[0] != Object.class) return false;
        if (method.getReturnType() != boolean.class) return false;
        return method.getName().equals("equals");
    }

    /**
     * 判断是否为属性表达式
     */
//    public static boolean isProperty(Map<ParameterExpression, ISqlTableRefExpression> asNameMap, MethodCallExpression methodCall) {
//        if (methodCall.getExpr().getKind() != Kind.Parameter) return false;
//        ParameterExpression parameter = (ParameterExpression) methodCall.getExpr();
//        return asNameMap.containsKey(parameter);
//    }
//
//    /**
//     * 判断是否为属性表达式
//     */
//    public static boolean isProperty(Map<ParameterExpression, ISqlTableRefExpression> asNameMap, FieldSelectExpression fieldSelect) {
//        if (fieldSelect.getExpr().getKind() != Kind.Parameter) return false;
//        ParameterExpression parameter = (ParameterExpression) fieldSelect.getExpr();
//        return asNameMap.containsKey(parameter);
//    }

    /**
     * 判断是否为分组键
     */
//    public static boolean isGroupKey(Map<ParameterExpression, ISqlTableRefExpression> parameters, Expression expression) {
//        if (expression.getKind() != Kind.FieldSelect) return false;
//        FieldSelectExpression fieldSelect = (FieldSelectExpression) expression;
//        if (fieldSelect.getExpr().getKind() != Kind.Parameter) return false;
//        ParameterExpression parameter = (ParameterExpression) fieldSelect.getExpr();
//        Field field = fieldSelect.getField();
//        return parameters.containsKey(parameter)
//                && IGroup.class.isAssignableFrom(field.getDeclaringClass())
//                && field.getName().equals("key");
//    }
//
//    public static boolean isGroupValue(Map<ParameterExpression, ISqlTableRefExpression> parameters, Expression expression) {
//        if (expression.getKind() != Kind.FieldSelect) return false;
//        FieldSelectExpression fieldSelect = (FieldSelectExpression) expression;
//        if (fieldSelect.getExpr().getKind() != Kind.Parameter) return false;
//        ParameterExpression parameter = (ParameterExpression) fieldSelect.getExpr();
//        Field field = fieldSelect.getField();
//        String fieldName = field.getName();
//        return parameters.containsKey(parameter)
//                && IGroup.class.isAssignableFrom(field.getDeclaringClass())
//                && fieldName.startsWith("value");
//    }

    /**
     * 判断是否为getter方法
     */
    public static boolean isGetter(Method method) {
        if (isVoid(method.getReturnType())) return false;
        if (method.getParameterCount() != 0) return false;
        String name = method.getName();
        return name.startsWith("get") || name.startsWith("is");
    }

    public static boolean isPivoted(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (Pivoted.class.isAssignableFrom(declaringClass)) {
            String name = method.getName();
            if (name.equals("column")) {
                Type[] genericParameterTypes = method.getGenericParameterTypes();
                return method.getParameterCount() == 1 && genericParameterTypes.length == 1;
            }
        }
        return false;
    }

    public static boolean isUnPivotedName(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (UnPivoted.class.isAssignableFrom(declaringClass)) {
            String name = method.getName();
            if (name.equals("nameColumn")) {
                return method.getParameterCount() == 0;
            }
        }
        return false;
    }

    public static boolean isUnPivotedValue(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (UnPivoted.class.isAssignableFrom(declaringClass)) {
            String name = method.getName();
            if (name.equals("valueColumn")) {
                return method.getParameterCount() == 0;
            }
        }
        return false;
    }

    /**
     * 判断是否为setter方法
     */
    public static boolean isSetter(Method method) {
        if (method.getParameterCount() != 1) return false;
        String name = method.getName();
        return name.startsWith("set");
    }

    public static boolean isStartQuery(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        return ITable.class.isAssignableFrom(declaringClass) && method.getName().equals("query") && method.getParameterCount() == 1;
    }

    public static boolean isList(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        return io.github.kiryu1223.drink.core.util.List.class.isAssignableFrom(declaringClass);
    }

    /**
     * 判断是否为sql操作符方法
     */
    public static boolean isSqlOperatorMethod(Method method) {
        return method.isAnnotationPresent(SqlOperatorMethod.class);
    }

    /**
     * 判断是否为sql扩展方法
     */
    public static boolean isSqlExtensionExpressionMethod(Method method) {
        SqlExtensionExpression[] annotationsByType = method.getAnnotationsByType(SqlExtensionExpression.class);
        return annotationsByType.length > 0;
    }

//    public static String toLowerCaseFirstOne(String s) {
//        if (Character.isLowerCase(s.charAt(0))) {
//            return s;
//        }
//        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
//    }
//
//
//    public static String firstUpperCase(String original) {
//        if (!original.isEmpty()) {
//            return Character.toUpperCase(original.charAt(0)) + original.substring(1);
//        }
//        return original;
//    }

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

    /**
     * 转换为包装类型
     */
    public static Class<?> upperClass(Class<?> c) {
        if (c.isPrimitive()) {
            if (c == Character.TYPE) {
                return Character.class;
            }
            if (c == Byte.TYPE) {
                return Byte.class;
            }
            else if (c == Short.TYPE) {
                return Short.class;
            }
            else if (c == Integer.TYPE) {
                return Integer.class;
            }
            else if (c == Long.TYPE) {
                return Long.class;
            }
            else if (c == Float.TYPE) {
                return Float.class;
            }
            else if (c == Double.TYPE) {
                return Double.class;
            }
            else if (c == Boolean.TYPE) {
                return Boolean.class;
            }
            else {
                return Void.class;
            }
        }
        else {
            return c;
        }
    }

    public static <R> java.util.List<R> buildTree(java.util.List<R> flatList, IGetterCaller<R,?> selfFieldGetter, IGetterCaller<R,?> targetGetter, ISetterCaller<R> navigateSetter, IGetterCaller<R, Collection<R>> navigateGetter) throws InvocationTargetException, IllegalAccessException
    {
        // 用 Map 存储所有节点，以便快速查找
        Map<Object, R> nodeMap = new HashMap<>();
        java.util.List<R> rootNodes = new ArrayList<>();

        // 将所有节点加入 Map
        for (R node : flatList) {
            nodeMap.put(selfFieldGetter.apply(node), node);
        }

        // 构建树结构
        for (R node : flatList) {
            Object parentValue = targetGetter.apply(node);
            R parentNode = nodeMap.get(parentValue);
            // 如果没有父节点，则将当前节点作为根节点
            if (parentNode == null) {
                rootNodes.add(node);
            }
            else {
                Collection<R> collection = navigateGetter.apply(parentNode);
                if (collection == null) {
                    collection = new ArrayList<>();
                    navigateSetter.call(parentNode, collection);
                }
                collection.add(node);
            }
        }

        return rootNodes;
    }
}
