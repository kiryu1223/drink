package io.github.kiryu1223.drink.base.session;


import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface SqlSession {
    interface Function<R> {
        R invoke(ResultSet resultSet) throws SQLException, NoSuchFieldException, IllegalAccessException, InvocationTargetException;
    }

    interface Action {
        void invoke(ResultSet resultSet) throws SQLException, IllegalAccessException, InvocationTargetException;
    }

    /**
     * 执行查询，并返回结果
     *
     * @param func      对ResultSet进行操作并且返回结果
     * @param sql       sql语句
     * @param sqlValues 参数
     */
    <R> R executeQuery(Function<R> func, String sql, Collection<SqlValue> sqlValues);

    /**
     * 执行插入，并返回影响行数
     *
     * @param action        对ResultSet进行操作
     * @param sql           sql语句
     * @param sqlValues     参数
     * @param length        批量下每轮的长度
     * @param autoIncrement 是否回填id
     */
    long executeInsert(Action action, String sql, Collection<SqlValue> sqlValues, int length, boolean autoIncrement);

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
