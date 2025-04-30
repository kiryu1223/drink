package io.github.kiryu1223.drink.base.session;


import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Collection;
import java.util.List;

public interface SqlSession
{
    interface Function<T, R>
    {
        R invoke(T t) throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException;
    }

    /**
     * 执行查询，并返回结果
     *
     * @param func      对ResultSet进行操作并且返回结果
     * @param sql       sql语句
     * @param sqlValues 参数
     */
    <R> R executeQuery(Function<ResultSet, R> func, String sql, Collection<SqlValue> sqlValues);

    /**
     * 执行插入，并返回影响行数
     *
     * @param sql       sql语句
     * @param sqlValues 参数
     * @param length    批量下每轮的长度
     */
    long executeInsert(String sql, Collection<SqlValue> sqlValues, int length);

    /**
     * 执行更新，并返回影响行数
     *
     * @param sql       sql语句
     * @param sqlValues 参数
     */
    long executeUpdate(String sql, Collection<SqlValue> sqlValues);

    /**
     * 执行删除，并返回影响行数
     *
     * @param sql       sql语句
     * @param sqlValues 参数
     */
    long executeDelete(String sql, Collection<SqlValue> sqlValues);
}
