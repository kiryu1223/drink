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
package io.github.kiryu1223.drink.func;

import io.github.kiryu1223.drink.base.exception.Winner;
import io.github.kiryu1223.drink.base.sqlExt.GroupJoinExtension;
import io.github.kiryu1223.drink.base.sqlExt.Over;
import io.github.kiryu1223.drink.base.sqlExt.SqlExtensionExpression;
import io.github.kiryu1223.drink.base.sqlExt.SqlTimeUnit;
import io.github.kiryu1223.drink.func.mysql.MySqlDateTimeDiffExtension;
import io.github.kiryu1223.drink.func.oracle.OracleAddOrSubDateExtension;
import io.github.kiryu1223.drink.func.oracle.OracleDateTimeDiffExtension;
import io.github.kiryu1223.drink.func.oracle.OracleJoinExtension;
import io.github.kiryu1223.drink.func.pgsql.PostgreSQLAddOrSubDateExtension;
import io.github.kiryu1223.drink.func.pgsql.PostgreSQLDateTimeDiffExtension;
import io.github.kiryu1223.drink.func.sqlite.SqliteAddOrSubDateExtension;
import io.github.kiryu1223.drink.func.sqlite.SqliteDateTimeDiffExtension;
import io.github.kiryu1223.drink.func.sqlite.SqliteJoinExtension;
import io.github.kiryu1223.drink.func.types.SqlTypes;
import io.github.kiryu1223.drink.func.types.TypeCastExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.github.kiryu1223.drink.base.DbType.*;


/**
 * Sql函数
 *
 * @author kiryu1223
 * @since 3.0
 */
public final class SqlFunctions {

    private SqlFunctions() {
    }

    private static <Error> Error error() {
        return Winner.error();
    }

    // region [原始sql]

    @SqlExtensionExpression(template = "{&sql}")
    public static <T> T rawSql(Object sql) {
        return error();
    }

    // endregion

    // region [聚合函数]

    /**
     * 聚合函数COUNT(*)
     */
    @SqlExtensionExpression(template = "COUNT(*)")
    public static long count() {
        return error();
    }

    /**
     * 聚合函数COUNT(t)
     */
    @SqlExtensionExpression(template = "COUNT({t})")
    public static <T> long count(T t) {
        return error();
    }

    /**
     * 聚合函数SUM(t)
     */
    @SqlExtensionExpression(template = "SUM({t})")
    public static <T> BigDecimal sum(T t) {
        return error();
    }

    /**
     * 聚合函数AVG(t)
     */
    @SqlExtensionExpression(template = "AVG({t})")
    public static <T extends Number> BigDecimal avg(T t) {
        return error();
    }

    /**
     * 聚合函数MIN(t)
     */
    @SqlExtensionExpression(template = "MIN({t})")
    public static <T> T min(T t) {
        return error();
    }

    /**
     * 聚合函数MAX(t)
     */
    @SqlExtensionExpression(template = "MAX({t})")
    public static <T> T max(T t) {
        return error();
    }

    @SqlExtensionExpression(template = "", extension = GroupJoinExtension.class)
    public static String groupJoin(String property) {
        return error();
    }

    @SqlExtensionExpression(template = "", extension = GroupJoinExtension.class)
    public static <T> String groupJoin(String delimiter, T property) {
        return error();
    }

    // endregion

    // region [窗口函数]

    @SqlExtensionExpression(template = "OVER ()")
    public static Over over() {
        return Winner.error();
    }

    @SqlExtensionExpression(template = "OVER ({params})", separator = " ")
    public static Over over(Over.Param... params) {
        return Winner.error();
    }

    //endregion

    // region [时间]

    /**
     * 获取当前日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "NOW()")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "NOW()")
    @SqlExtensionExpression(dbType = Oracle, template = "CAST(CURRENT_TIMESTAMP AS TIMESTAMP)")
    @SqlExtensionExpression(dbType = SQLServer, template = "GETDATE()")
    @SqlExtensionExpression(dbType = SQLite, template = "DATETIME('now','localtime')")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "NOW()")
    public static LocalDateTime now() {
        return error();
    }

    /**
     * 获取当前utc日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "UTC_TIMESTAMP()")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "UTC_TIMESTAMP()")
    @SqlExtensionExpression(dbType = Oracle, template = "(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')")
    @SqlExtensionExpression(dbType = SQLServer, template = "GETUTCDATE()")
    @SqlExtensionExpression(dbType = SQLite, template = "DATETIME('now')")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')")
    public static LocalDateTime utcNow() {
        return error();
    }

    /**
     * 获取当前日期
     */
    @SqlExtensionExpression(dbType = H2, template = "CURDATE()")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "CURDATE()")
    @SqlExtensionExpression(dbType = Oracle, template = "CURRENT_DATE")
    @SqlExtensionExpression(dbType = SQLServer, template = "CAST(GETDATE() AS DATE)")
    @SqlExtensionExpression(dbType = SQLite, template = "DATE('now','localtime')")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "CURRENT_DATE")
    public static LocalDate nowDate() {
        return error();
    }

    /**
     * 获取当前时间
     */
    @SqlExtensionExpression(dbType = H2, template = "CURTIME()")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "CURTIME()")
    @SqlExtensionExpression(dbType = Oracle, template = "CURRENT_DATE")
    @SqlExtensionExpression(dbType = SQLServer, template = "CAST(GETDATE() AS TIME)")
    @SqlExtensionExpression(dbType = SQLite, template = "TIME('now','localtime')")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "CURRENT_TIME")
    public static LocalTime nowTime() {
        return error();
    }

    /**
     * 获取当前utc日期
     */
    @SqlExtensionExpression(dbType = H2, template = "UTC_DATE()")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "UTC_DATE()")
    @SqlExtensionExpression(dbType = Oracle, template = "CAST(SYS_EXTRACT_UTC(CURRENT_TIMESTAMP) AS DATE)")
    @SqlExtensionExpression(dbType = SQLServer, template = "CAST(GETUTCDATE() AS DATE)")
    @SqlExtensionExpression(dbType = SQLite, template = "DATE('now')")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')::DATE")
    public static LocalDate utcNowDate() {
        return error();
    }

    /**
     * 获取当前utc时间
     */
    @SqlExtensionExpression(dbType = H2, template = "UTC_TIME()")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "UTC_TIME()")
    @SqlExtensionExpression(dbType = Oracle, template = "CAST(SYS_EXTRACT_UTC(CURRENT_TIMESTAMP) AS DATE)")
    @SqlExtensionExpression(dbType = SQLServer, template = "CAST(GETUTCDATE() AS TIME)")
    @SqlExtensionExpression(dbType = SQLite, template = "TIME('now')")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')::TIME")
    public static LocalTime utcNowTime() {
        return error();
    }

    /**
     * 指定日期或日期时间加上指定单位的时间
     *
     * @param time 指定的日期或日期时间
     * @param unit 时间单位
     * @param num  单位数量
     */
    @SqlExtensionExpression(dbType = H2, template = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = Oracle, template = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEADD({unit},{num},{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDateTime addDate(LocalDateTime time, SqlTimeUnit unit, int num) {
        return error();
    }

    /**
     * 指定日期或日期时间加上指定单位的时间
     *
     * @param time 指定的日期或日期时间
     * @param unit 时间单位
     * @param num  单位数量
     */
    @SqlExtensionExpression(dbType = H2, template = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = Oracle, template = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEADD({unit},{num},{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDate addDate(LocalDate time, SqlTimeUnit unit, int num) {
        return error();
    }

    /**
     * 指定日期或日期时间加上指定的天数
     *
     * @param time 指定的日期或日期时间
     * @param days 天数
     */
    @SqlExtensionExpression(dbType = H2, template = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = Oracle, template = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEADD(DAY,{days},{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDateTime addDate(LocalDateTime time, int days) {
        return error();
    }

    /**
     * 指定日期或日期时间加上指定的天数
     *
     * @param time 指定的日期或日期时间
     * @param days 天数
     */
    @SqlExtensionExpression(dbType = H2, template = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = Oracle, template = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEADD(DAY,{days},{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDate addDate(LocalDate time, int days) {
        return error();
    }

    /**
     * 指定日期或日期时间减去指定单位的时间
     *
     * @param time 指定的日期或日期时间
     * @param unit 时间单位
     * @param num  单位数量
     */
    @SqlExtensionExpression(dbType = H2, template = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = Oracle, template = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEADD({unit},-({num}),{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDateTime subDate(LocalDateTime time, SqlTimeUnit unit, int num) {
        return error();
    }

    /**
     * 指定日期或日期时间减去指定单位的时间
     *
     * @param time 指定的日期或日期时间
     * @param unit 时间单位
     * @param num  单位数量
     */
    @SqlExtensionExpression(dbType = H2, template = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = Oracle, template = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEADD({unit},-({num}),{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDate subDate(LocalDate time, SqlTimeUnit unit, int num) {
        return error();
    }

    /**
     * 指定日期或日期时间减去指定的天数
     *
     * @param time 指定的日期或日期时间
     * @param days 天数
     */
    @SqlExtensionExpression(dbType = H2, template = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = Oracle, template = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEADD(DAY,-({days}),{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDateTime subDate(LocalDateTime time, int days) {
        return error();
    }

    /**
     * 指定日期或日期时间减去指定的天数
     *
     * @param time 指定的日期或日期时间
     * @param days 天数
     */
    @SqlExtensionExpression(dbType = H2, template = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = Oracle, template = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEADD(DAY,-({days}),{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDate subDate(LocalDate time, int days) {
        return error();
    }

    /**
     * 计算两个日期或日期时间的指定的时间单位的差距
     *
     * @param unit 时间单位
     * @param from 过去时间
     * @param to   将来时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TIMESTAMPDIFF({unit},{from},{to})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = Oracle, template = "", extension = OracleDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEDIFF_BIG({unit},{from},{to})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDateTime from, LocalDateTime to) {
        return error();
    }

    /**
     * 计算两个日期或日期时间的指定的时间单位的差距
     *
     * @param unit 时间单位
     * @param from 过去时间
     * @param to   将来时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TIMESTAMPDIFF({unit},{from},{to})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = Oracle, extension = OracleDateTimeDiffExtension.class, template = "")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEDIFF_BIG({unit},{from},{to})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDateTime from, LocalDate to) {
        return error();
    }

    /**
     * 计算两个日期或日期时间的指定的时间单位的差距
     *
     * @param unit 时间单位
     * @param from 过去时间
     * @param to   将来时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TIMESTAMPDIFF({unit},{from},{to})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = Oracle, extension = OracleDateTimeDiffExtension.class, template = "")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEDIFF_BIG({unit},{from},{to})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDateTime from, String to) {
        return error();
    }

    /**
     * 计算两个日期或日期时间的指定的时间单位的差距
     *
     * @param unit 时间单位
     * @param from 过去时间
     * @param to   将来时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TIMESTAMPDIFF({unit},{from},{to})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = Oracle, extension = OracleDateTimeDiffExtension.class, template = "")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEDIFF_BIG({unit},{from},{to})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDate from, LocalDate to) {
        return error();
    }

    /**
     * 计算两个日期或日期时间的指定的时间单位的差距
     *
     * @param unit 时间单位
     * @param from 过去时间
     * @param to   将来时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TIMESTAMPDIFF({unit},{from},{to})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = Oracle, extension = OracleDateTimeDiffExtension.class, template = "")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEDIFF({unit},{from},{to})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDate from, LocalDateTime to) {
        return error();
    }

    /**
     * 计算两个日期或日期时间的指定的时间单位的差距
     *
     * @param unit 时间单位
     * @param from 过去时间
     * @param to   将来时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TIMESTAMPDIFF({unit},{from},{to})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = Oracle, extension = OracleDateTimeDiffExtension.class, template = "")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEDIFF_BIG({unit},{from},{to})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDate from, String to) {
        return error();
    }

    /**
     * 计算两个日期或日期时间的指定的时间单位的差距
     *
     * @param unit 时间单位
     * @param from 过去时间
     * @param to   将来时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TIMESTAMPDIFF({unit},{from},{to})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = Oracle, extension = OracleDateTimeDiffExtension.class, template = "")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEDIFF_BIG({unit},{from},{to})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, String from, String to) {
        return error();
    }

    /**
     * 计算两个日期或日期时间的指定的时间单位的差距
     *
     * @param unit 时间单位
     * @param from 过去时间
     * @param to   将来时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TIMESTAMPDIFF({unit},{from},{to})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = Oracle, extension = OracleDateTimeDiffExtension.class, template = "")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEDIFF_BIG({unit},{from},{to})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, String from, LocalDate to) {
        return error();
    }

    /**
     * 计算两个日期或日期时间的指定的时间单位的差距
     *
     * @param unit 时间单位
     * @param from 过去时间
     * @param to   将来时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TIMESTAMPDIFF({unit},{from},{to})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = Oracle, extension = OracleDateTimeDiffExtension.class, template = "")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEDIFF_BIG({unit},{from},{to})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, String from, LocalDateTime to) {
        return error();
    }

    /**
     * 格式化时间到字符串
     *
     * @param time   日期或日期时间
     * @param format 格式
     */
    @SqlExtensionExpression(dbType = H2, template = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_CHAR({time},{format})")
    @SqlExtensionExpression(dbType = SQLServer, template = "FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = SQLite, template = "STRFTIME({format},{time})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TO_CHAR({time},{format})")
    public static String dateFormat(LocalDateTime time, String format) {
        return error();
    }

    /**
     * 格式化时间到字符串
     *
     * @param time   日期或日期时间
     * @param format 格式
     */
    @SqlExtensionExpression(dbType = H2, template = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_CHAR({time},{format})")
    @SqlExtensionExpression(dbType = SQLServer, template = "FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = SQLite, template = "STRFTIME({format},{time})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TO_CHAR({time},{format})")
    public static String dateFormat(LocalDate time, String format) {
        return error();
    }

    /**
     * 格式化时间到字符串
     *
     * @param time   日期或日期时间
     * @param format 格式
     */
    @SqlExtensionExpression(dbType = H2, template = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),{format})")
    @SqlExtensionExpression(dbType = SQLServer, template = "FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = SQLite, template = "STRFTIME({format},{time})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TO_CHAR({time}::TIMESTAMP,{format})")
    public static String dateFormat(String time, String format) {
        return error();
    }

    /**
     * 提取日期或日期时间中的日期
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAY({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAY({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(DAY FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(DAY,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%d',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(DAY FROM {time})::INT4")
    public static int getDay(LocalDateTime time) {
        return error();
    }

    /**
     * 提取日期或日期时间中的日期
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAY({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAY({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(DAY FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(DAY,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%d',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(DAY FROM {time})::INT4")
    public static int getDay(LocalDate time) {
        return error();
    }

    /**
     * 提取日期或日期时间中的日期
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAY({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAY({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(DAY FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(DAY,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%d',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(DAY FROM {time}::TIMESTAMP)::INT4")
    public static int getDay(String time) {
        return error();
    }

    /**
     * 获取指定日期或日期时间为星期几
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_CHAR({time},'DAY')")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATENAME(WEEKDAY,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 'Sunday' WHEN '1' THEN 'Monday' WHEN '2' THEN 'Tuesday' WHEN '3' THEN 'Wednesday' WHEN '4' THEN 'Thursday' WHEN '5' THEN 'Friday' WHEN '6' THEN 'Saturday' END)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TRIM(TO_CHAR({time},'Day'))")
    public static String getDayName(LocalDateTime time) {
        return error();
    }

    /**
     * 获取指定日期或日期时间为星期几
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_CHAR({time},'DAY')")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATENAME(WEEKDAY,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 'Sunday' WHEN '1' THEN 'Monday' WHEN '2' THEN 'Tuesday' WHEN '3' THEN 'Wednesday' WHEN '4' THEN 'Thursday' WHEN '5' THEN 'Friday' WHEN '6' THEN 'Saturday' END)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TRIM(TO_CHAR({time},'Day'))")
    public static String getDayName(LocalDate time) {
        return error();
    }

    /**
     * 获取指定日期或日期时间为星期几
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'DAY')")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATENAME(WEEKDAY,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 'Sunday' WHEN '1' THEN 'Monday' WHEN '2' THEN 'Tuesday' WHEN '3' THEN 'Wednesday' WHEN '4' THEN 'Thursday' WHEN '5' THEN 'Friday' WHEN '6' THEN 'Saturday' END)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TRIM(TO_CHAR({time}::TIMESTAMP,'Day'))")
    public static String getDayName(String time) {
        return error();
    }

    /**
     * 从指定的日期或日期时间中取出星期几
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR({time},'D'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "(DATEPART(WEEKDAY,{time}))")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%w',{time}) + 1)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(EXTRACT(DOW FROM {time}) + 1)::INT4")
    public static int getDayOfWeek(LocalDateTime time) {
        return error();
    }

    /**
     * 从指定的日期或日期时间中取出星期几
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR({time},'D'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "(DATEPART(WEEKDAY,{time}))")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%w',{time}) + 1)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(EXTRACT(DOW FROM {time}) + 1)::INT4")
    public static int getDayOfWeek(LocalDate time) {
        return error();
    }

    /**
     * 从指定的日期或日期时间中取出星期几
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'D'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(WEEKDAY,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%w',{time}) + 1)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(EXTRACT(DOW FROM {time}::TIMESTAMP) + 1)::INT4")
    public static int getDayOfWeek(String time) {
        return error();
    }

    /**
     * 获取指定日期或日期时间为今年的多少天
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR({time},'DDD'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(DAYOFYEAR,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%j',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(DOY FROM {time})::INT4")
    public static int getDayOfYear(LocalDateTime time) {
        return error();
    }

    /**
     * 获取指定日期或日期时间为今年的多少天
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR({time},'DDD'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(DAYOFYEAR,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%j',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(DOY FROM {time})::INT4")
    public static int getDayOfYear(LocalDate time) {
        return error();
    }

    /**
     * 获取指定日期或日期时间为今年的多少天
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'DDD'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(DAYOFYEAR,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%j',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(DOY FROM {time}::TIMESTAMP)::INT4")
    public static int getDayOfYear(String time) {
        return error();
    }

//    @SqlExtensionExpression(dbType = H2, template = "SEC_TO_TIME({second})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "SEC_TO_TIME({second})")
//    @SqlExtensionExpression(dbType = Oracle, template = "TO_DATE(TO_CHAR(FLOOR({second} / 3600), 'FM00')||':'||TO_CHAR(FLOOR(MOD({second}, 3600) / 60), 'FM00')||':'||TO_CHAR(MOD({second}, 60), 'FM00'))")
//    public static LocalTime toTime(int second)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = H2, template = "STR_TO_DATE({time})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "STR_TO_DATE({time})")
//    @SqlExtensionExpression(dbType = Oracle, template = "TO_DATE({time}, 'YYYY-MM-DD')")
//    @SqlExtensionExpression(dbType = SQLServer, template = "CONVERT(DATE,{time},23)")
//    public static LocalDate toDate(String time)
//    {
//        boom();
//        return LocalDate.now();
//    }
//
//    @SqlExtensionExpression(dbType = H2, template = "STR_TO_DATE({time},{format})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "STR_TO_DATE({time},{format})")
//    @SqlExtensionExpression(dbType = Oracle, template = "TO_DATE({time},{format})")
//    public static LocalDate toDate(String time, String format)
//    {
//        boom();
//        return LocalDate.now();
//    }
//
//    @SqlExtensionExpression(dbType = H2, template = "FROM_DAYS({days})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "FROM_DAYS({days})")
//    @SqlExtensionExpression(dbType = Oracle, template = "(TO_DATE('1970-01-01', 'YYYY-MM-DD') + ({days} - 719163))")
//    public static LocalDate toDate(long days)
//    {
//        boom();
//        return LocalDate.now();
//    }
//
//    @SqlExtensionExpression(dbType = H2, template = "STR_TO_DATE({time},{format})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "STR_TO_DATE({time},{format})")
//    @SqlExtensionExpression(dbType = Oracle, template = "TO_DATE({time},{format})")
//    public static LocalDateTime toDateTime(String time, String format)
//    {
//        boom();
//        return LocalDateTime.now();
//    }

    /**
     * 获取指定日期或日期时间从公元到今天的天数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TRUNC({time} - (TO_DATE('0001-01-01', 'YYYY-MM-DD') - INTERVAL '1' YEAR) - 2)")
    @SqlExtensionExpression(dbType = SQLServer, template = "(DATEDIFF(DAY,'0001-01-01',{time}) + 366)")
    @SqlExtensionExpression(dbType = SQLite, template = "(JULIANDAY({time}) - JULIANDAY('0000-01-01'))")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(EXTRACT(EPOCH FROM {time})::INT4 / 86400 + 719528)")
    public static int dateToDays(LocalDate time) {
        return error();
    }

    /**
     * 获取指定日期或日期时间从公元到今天的天数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TRUNC({time} - (TO_DATE('0001-01-01', 'YYYY-MM-DD') - INTERVAL '1' YEAR) - 2)")
    @SqlExtensionExpression(dbType = SQLServer, template = "(DATEDIFF(DAY,'0001-01-01',{time}) + 366)")
    @SqlExtensionExpression(dbType = SQLite, template = "(JULIANDAY({time}) - JULIANDAY('0000-01-01'))")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(EXTRACT(EPOCH FROM {time})::INT4 / 86400 + 719528)")
    public static int dateToDays(LocalDateTime time) {
        return error();
    }

    /**
     * 获取指定日期或日期时间从公元到今天的天数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(DAY FROM (TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff') - (TO_TIMESTAMP('0001-01-01', 'YYYY-MM-DD') - INTERVAL '1' YEAR) - INTERVAL '2' DAY))")
    @SqlExtensionExpression(dbType = SQLServer, template = "(DATEDIFF(DAY,'0001-01-01',{time}) + 366)")
    @SqlExtensionExpression(dbType = SQLite, template = "(JULIANDAY({time}) - JULIANDAY('0000-01-01'))")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(EXTRACT(EPOCH FROM {time}::TIMESTAMP)::INT4 / 86400 + 719528)")
    public static int dateToDays(String time) {
        return error();
    }

//    @SqlExtensionExpression(dbType = H2, template = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = Oracle, template = "((EXTRACT(HOUR FROM {time}) * 3600) + (EXTRACT(MINUTE FROM {time}) * 60) + EXTRACT(SECOND FROM {time}))")
//    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(HOUR,{time}) * 3600 + DATEPART(MINUTE,{time}) * 60 + DATEPART(SECOND,{time})")
//    public static int toSecond(LocalDateTime time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = H2, template = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = Oracle, template = "((EXTRACT(HOUR FROM {time}) * 3600) + (EXTRACT(MINUTE FROM {time}) * 60) + EXTRACT(SECOND FROM {time}))")
//    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(HOUR,{time}) * 3600 + DATEPART(MINUTE,{time}) * 60 + DATEPART(SECOND,{time})")
//    public static int toSecond(LocalTime time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = H2, template = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = Oracle, template = "((EXTRACT(HOUR FROM TO_DATE({time})) * 3600) + (EXTRACT(MINUTE FROM TO_DATE({time})) * 60) + EXTRACT(SECOND FROM TO_DATE({time})))")
//    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(HOUR,{time}) * 3600 + DATEPART(MINUTE,{time}) * 60 + DATEPART(SECOND,{time})")
//    public static int toSecond(String time)
//    {
//        boom();
//        return 0;
//    }

    /**
     * 提取日期时间中的小时
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "HOUR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "HOUR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(HOUR FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(HOUR,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%H',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(HOUR FROM {time})::INT4")
    public static int getHour(LocalDateTime time) {
        return error();
    }

    /**
     * 提取日期时间中的小时
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "HOUR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "HOUR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(HOUR FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(HOUR,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%H',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(HOUR FROM {time})::INT4")
    public static int getHour(LocalDate time) {
        return error();
    }

    /**
     * 提取日期时间中的小时
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "HOUR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "HOUR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(HOUR FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(HOUR,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%H',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(HOUR FROM {time}::TIMESTAMP)::INT4")
    public static int getHour(String time) {
        return error();
    }

    /**
     * 获取指定的日期或日期时间当月的最后一天的日期
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "EOMONTH({time})")
    @SqlExtensionExpression(dbType = SQLite, template = "DATE({time},'start of month','+1 month','-1 day')")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(DATE_TRUNC('MONTH',{time}) + INTERVAL '1' MONTH - INTERVAL '1' DAY)::DATE")
    public static LocalDate getLastDay(LocalDateTime time) {
        return error();
    }

    /**
     * 获取指定的日期或日期时间当月的最后一天的日期
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "EOMONTH({time})")
    @SqlExtensionExpression(dbType = SQLite, template = "DATE({time},'start of month','+1 month','-1 day')")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(DATE_TRUNC('MONTH',{time}) + INTERVAL '1' MONTH - INTERVAL '1' DAY)::DATE")
    public static LocalDate getLastDay(LocalDate time) {
        return error();
    }

    /**
     * 获取指定的日期或日期时间当月的最后一天的日期
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "LAST_DAY(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "EOMONTH({time})")
    @SqlExtensionExpression(dbType = SQLite, template = "DATE({time},'start of month','+1 month','-1 day')")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(DATE_TRUNC('MONTH',{time}::TIMESTAMP) + INTERVAL '1' MONTH - INTERVAL '1' DAY)::DATE")
    public static LocalDate getLastDay(String time) {
        return error();
    }

//    @SqlExtensionExpression(dbType = H2, template = "MAKEDATE({},{})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "MAKEDATE({},{})")
//    public static LocalDate createDate(int year, int days)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = H2, template = "MAKETIME({},{},{})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "MAKETIME({},{},{})")
//    public static LocalTime createTime(int hour, int minute, int second)
//    {
//        boom();
//        return 0;
//    }

    /**
     * 提取日期时间中的分钟
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "MINUTE({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "MINUTE({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(MINUTE FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(MINUTE,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%M',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(MINUTE FROM {time})::INT4")
    public static int getMinute(LocalTime time) {
        return error();
    }

    /**
     * 提取日期时间中的分钟
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "MINUTE({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "MINUTE({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(MINUTE FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(MINUTE,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%M',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(MINUTE FROM {time})::INT4")
    public static int getMinute(LocalDateTime time) {
        return error();
    }

    /**
     * 提取日期时间中的分钟
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "MINUTE({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "MINUTE({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(MINUTE FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(MINUTE,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%M',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(MINUTE FROM {time}::TIMESTAMP)::INT4")
    public static int getMinute(String time) {
        return error();
    }

    /**
     * 提取日期或日期时间中的月份
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "MONTH({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "MONTH({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(MONTH FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(MINUTE,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%m',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(MONTH FROM {time})::INT4")
    public static int getMonth(LocalDate time) {
        return error();
    }

    /**
     * 提取日期或日期时间中的月份
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "MONTH({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "MONTH({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(MONTH FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(MONTH,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%m',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(MONTH FROM {time})::INT4")
    public static int getMonth(LocalDateTime time) {
        return error();
    }

    /**
     * 提取日期或日期时间中的月份
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "MONTH({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "MONTH({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(MONTH FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(MONTH,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%m',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(MONTH FROM {time}::TIMESTAMP)::INT4")
    public static int getMonth(String time) {
        return error();
    }

    /**
     * 获取指定日期或日期时间那个月的名字
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_CHAR({time},'FMMONTH')")
    @SqlExtensionExpression(dbType = SQLServer, template = "FORMAT({time}, 'MMMM')")
    @SqlExtensionExpression(dbType = SQLite, template = "(CASE STRFTIME('%m',{time}) WHEN '01' THEN 'January' WHEN '02' THEN 'February' WHEN '03' THEN 'March' WHEN '04' THEN 'April' WHEN '05' THEN 'May' WHEN '06' THEN 'June' WHEN '07' THEN 'July' WHEN '08' THEN 'August' WHEN '09' THEN 'September' WHEN '10' THEN 'October' WHEN '11' THEN 'November' WHEN '12' THEN 'December' END)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TRIM(TO_CHAR({time},'Month'))")
    public static String getMonthName(LocalDate time) {
        return error();
    }

    /**
     * 获取指定日期或日期时间那个月的名字
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_CHAR({time},'FMMONTH')")
    @SqlExtensionExpression(dbType = SQLServer, template = "FORMAT({time}, 'MMMM')")
    @SqlExtensionExpression(dbType = SQLite, template = "(CASE STRFTIME('%m',{time}) WHEN '01' THEN 'January' WHEN '02' THEN 'February' WHEN '03' THEN 'March' WHEN '04' THEN 'April' WHEN '05' THEN 'May' WHEN '06' THEN 'June' WHEN '07' THEN 'July' WHEN '08' THEN 'August' WHEN '09' THEN 'September' WHEN '10' THEN 'October' WHEN '11' THEN 'November' WHEN '12' THEN 'December' END)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TRIM(TO_CHAR({time},'Month'))")
    public static String getMonthName(LocalDateTime time) {
        return error();
    }

    /**
     * 获取指定日期或日期时间那个月的名字
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'FMMONTH')")
    @SqlExtensionExpression(dbType = SQLServer, template = "FORMAT(CONVERT(DATE,{time}), 'MMMM')")
    @SqlExtensionExpression(dbType = SQLite, template = "(CASE STRFTIME('%m',{time}) WHEN '01' THEN 'January' WHEN '02' THEN 'February' WHEN '03' THEN 'March' WHEN '04' THEN 'April' WHEN '05' THEN 'May' WHEN '06' THEN 'June' WHEN '07' THEN 'July' WHEN '08' THEN 'August' WHEN '09' THEN 'September' WHEN '10' THEN 'October' WHEN '11' THEN 'November' WHEN '12' THEN 'December' END)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TRIM(TO_CHAR({time}::TIMESTAMP,'Month'))")
    public static String getMonthName(String time) {
        return error();
    }

    /**
     * 提取日期或日期时间所在季度
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "QUARTER({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "QUARTER({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "CEIL(EXTRACT(MONTH FROM {time}) / 3)")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(QUARTER,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "((strftime('%m',{time}) + 2) / 3)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(QUARTER FROM {time})::INT4")
    public static int getQuarter(LocalDate time) {
        return error();
    }

    /**
     * 提取日期或日期时间所在季度
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "QUARTER({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "QUARTER({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "CEIL(EXTRACT(MONTH FROM {time}) / 3)")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(QUARTER,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "((strftime('%m',{time}) + 2) / 3)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(QUARTER FROM {time})::INT4")
    public static int getQuarter(LocalDateTime time) {
        return error();
    }

    /**
     * 提取日期或日期时间所在季度
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "QUARTER({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "QUARTER({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "CEIL(EXTRACT(MONTH FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff')) / 3)")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(QUARTER,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "((strftime('%m',{time}) + 2) / 3)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(QUARTER FROM {time}::TIMESTAMP)::INT4")
    public static int getQuarter(String time) {
        return error();
    }

    /**
     * 提取日期时间中的秒数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "SECOND({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SECOND({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(SECOND FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(SECOND,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%S',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(SECOND FROM {time})::INT4")
    public static int getSecond(LocalTime time) {
        return error();
    }

    /**
     * 提取日期时间中的秒数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "SECOND({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SECOND({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(SECOND FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(SECOND,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%S',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(SECOND FROM {time})::INT4")
    public static int getSecond(LocalDateTime time) {
        return error();
    }

    /**
     * 提取日期时间中的秒数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "SECOND({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SECOND({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(SECOND FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(SECOND,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%S',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(SECOND FROM {time}::TIMESTAMP)::INT4")
    public static int getSecond(String time) {
        return error();
    }

    /**
     * 提取日期时间中的毫秒
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = Oracle, template = "(EXTRACT(SECOND FROM {time}) * 1000)")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(MS,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%f',{time}) * 1000)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(MILLISECOND from {time})::INT4")
    public static int getMilliSecond(LocalTime time) {
        return error();
    }

    /**
     * 提取日期时间中的毫秒
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = Oracle, template = "(EXTRACT(SECOND FROM {time}) * 1000)")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(MS,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%f',{time}) * 1000)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(MILLISECOND from {time})::INT4")
    public static int getMilliSecond(LocalDateTime time) {
        return error();
    }

    /**
     * 提取日期时间中的毫秒
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = Oracle, template = "(EXTRACT(SECOND FROM {time}) * 1000)")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(MS,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%f',{time}) * 1000)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(MILLISECOND from {time}::TIMESTAMP)::INT4")
    public static int getMilliSecond(String time) {
        return error();
    }

    /**
     * 提取日期或日期时间中的周数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(WEEK,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(WEEK from {time})::INT4")
    public static int getWeek(LocalDate time) {
        return error();
    }

    /**
     * 提取日期或日期时间中的周数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(WEEK,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(WEEK from {time})::INT4")
    public static int getWeek(LocalDateTime time) {
        return error();
    }

    /**
     * 提取日期或日期时间中的周数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'IW'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(WEEK,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(WEEK from {time}::TIMESTAMP)::INT4")
    public static int getWeek(String time) {
        return error();
    }

//    @SqlExtensionExpression(dbType = H2, template = "WEEK({time},{firstDayofweek})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "WEEK({time},{firstDayofweek})")
//    public static int getWeek(String time, int firstDayofweek)
//    {
//        boom();
//        return 0;
//    }

    /**
     * 返回给定日期或日期时间的工作日编号
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "(CASE TO_NUMBER(TO_CHAR({time},'D')) WHEN 1 THEN 6 ELSE TO_NUMBER(TO_CHAR({time},'D')) - 2 END)")
    @SqlExtensionExpression(dbType = SQLServer, template = "(CASE DATEPART(WEEKDAY,{time}) WHEN 1 THEN 6 ELSE DATEPART(WEEKDAY,{time}) - 2 END)")
    @SqlExtensionExpression(dbType = SQLite, template = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 6 ELSE STRFTIME('%w',{time}) - 1 END)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "((EXTRACT(DOW FROM {time}) + 6) % 7)::INT4")
    public static int getWeekDay(LocalDate time) {
        return error();
    }

    /**
     * 返回给定日期或日期时间的工作日编号
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "(CASE TO_NUMBER(TO_CHAR({time},'D')) WHEN 1 THEN 6 ELSE TO_NUMBER(TO_CHAR({time},'D')) - 2 END)")
    @SqlExtensionExpression(dbType = SQLServer, template = "(CASE DATEPART(WEEKDAY,{time}) WHEN 1 THEN 6 ELSE DATEPART(WEEKDAY,{time}) - 2 END)")
    @SqlExtensionExpression(dbType = SQLite, template = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 6 ELSE STRFTIME('%w',{time}) - 1 END)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "((EXTRACT(DOW FROM {time}) + 6) % 7)::INT4")
    public static int getWeekDay(LocalDateTime time) {
        return error();
    }

    /**
     * 返回给定日期或日期时间的工作日编号
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "(CASE TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'D')) WHEN 1 THEN 6 ELSE TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'D')) - 2 END)")
    @SqlExtensionExpression(dbType = SQLServer, template = "(CASE DATEPART(WEEKDAY,{time}) WHEN 1 THEN 6 ELSE DATEPART(WEEKDAY,{time}) - 2 END)")
    @SqlExtensionExpression(dbType = SQLite, template = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 6 ELSE STRFTIME('%w',{time}) - 1 END)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "((EXTRACT(DOW FROM {time}::TIMESTAMP) + 6) % 7)::INT4")
    public static int getWeekDay(String time) {
        return error();
    }

    /**
     * 获取日期或日期时间中的周数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(ISO_WEEK,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(WEEK FROM {time})::INT4")
    public static int getWeekOfYear(LocalDate time) {
        return error();
    }

    /**
     * 获取日期或日期时间中的周数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(ISO_WEEK,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(WEEK FROM {time})::INT4")
    public static int getWeekOfYear(LocalDateTime time) {
        return error();
    }

    /**
     * 获取日期或日期时间中的周数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'IW'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(ISO_WEEK,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(WEEK FROM {time}::TIMESTAMP)::INT4")
    public static int getWeekOfYear(String time) {
        return error();
    }

    /**
     * 提取日期或日期时间中的年数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "YEAR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "YEAR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(YEAR FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(YEAR,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%Y',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(YEAR FROM {time})::INT4")
    public static int getYear(LocalDateTime time) {
        return error();
    }

    /**
     * 提取日期或日期时间中的年数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "YEAR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "YEAR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(YEAR FROM {time})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(YEAR,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%Y',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(YEAR FROM {time})::INT4")
    public static int getYear(LocalTime time) {
        return error();
    }

    /**
     * 提取日期或日期时间中的年数
     *
     * @param time 日期或日期时间
     */
    @SqlExtensionExpression(dbType = H2, template = "YEAR({time})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "YEAR({time})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXTRACT(YEAR FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATEPART(YEAR,{time})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(STRFTIME('%Y',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXTRACT(YEAR FROM {time}::TIMESTAMP)::INT4")
    public static int getYear(String time) {
        return error();
    }

    // endregion

    // region [数值]

    /**
     * 取绝对值
     */
    @SqlExtensionExpression(dbType = H2, template = "ABS({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ABS({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "ABS({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "ABS({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "ABS({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "ABS({a})")
    public static <T extends Number> T abs(T a) {
        return error();
    }

    /**
     * 计算cos
     */
    @SqlExtensionExpression(dbType = H2, template = "COS({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "COS({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "COS({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "COS({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "COS({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "COS({a})")
    public static <T extends Number> double cos(T a) {
        return error();
    }

    /**
     * 计算sin
     */
    @SqlExtensionExpression(dbType = H2, template = "SIN({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SIN({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "SIN({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "SIN({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "SIN({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "SIN({a})")
    public static <T extends Number> double sin(T a) {
        return error();
    }

    /**
     * 计算tan
     */
    @SqlExtensionExpression(dbType = H2, template = "TAN({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "TAN({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "TAN({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "TAN({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "TAN({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TAN({a})")
    public static <T extends Number> double tan(T a) {
        return error();
    }

    /**
     * 计算acos
     */
    @SqlExtensionExpression(dbType = H2, template = "ACOS({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ACOS({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "ACOS({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "ACOS({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "ACOS({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "ACOS({a})")
    public static <T extends Number> double acos(T a) {
        return error();
    }

    /**
     * 计算asin
     */
    @SqlExtensionExpression(dbType = H2, template = "ASIN({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ASIN({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "ASIN({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "ASIN({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "ASIN({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "ASIN({a})")
    public static <T extends Number> double asin(T a) {
        return error();
    }

    /**
     * 计算atan
     */
    @SqlExtensionExpression(dbType = H2, template = "ATAN({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ATAN({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "ATAN({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "ATAN({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "ATAN({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "ATAN({a})")
    public static <T extends Number> double atan(T a) {
        return error();
    }

    /**
     * 计算atan2
     */
    @SqlExtensionExpression(dbType = H2, template = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = Oracle, template = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = SQLServer, template = "ATN2({a},{b})")
    @SqlExtensionExpression(dbType = SQLite, template = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "ATAN2({a},{b})")
    public static <T extends Number> double atan2(T a, T b) {
        return error();
    }

    /**
     * 向上取整
     */
    @SqlExtensionExpression(dbType = H2, template = "CEIL({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "CEIL({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "CEIL({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "CEILING({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "CEIL({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "CEIL({a})::INT4")
    public static <T extends Number> int ceil(T a) {
        return error();
    }

    /**
     * 向下取整
     */
    @SqlExtensionExpression(dbType = H2, template = "FLOOR({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "FLOOR({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "FLOOR({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "FLOOR({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "FLOOR({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "FLOOR({a})::INT4")
    public static <T extends Number> int floor(T a) {
        return error();
    }

    /**
     * 余切函数
     */
    @SqlExtensionExpression(dbType = H2, template = "COT({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "COT({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "(CASE SIN({a}) WHEN 0 THEN 0 ELSE COS({a}) / SIN({a}) END)")
    @SqlExtensionExpression(dbType = SQLServer, template = "COT({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "COT({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "COT({a})")
    public static <T extends Number> double cot(T a) {
        return error();
    }

    /**
     * 将弧度转换为角度
     */
    @SqlExtensionExpression(dbType = H2, template = "DEGREES({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "DEGREES({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "({a} * 180 / " + Math.PI + ")")
    @SqlExtensionExpression(dbType = SQLServer, template = "DEGREES({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "DEGREES({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "DEGREES({a})")
    public static <T extends Number> double degrees(T a) {
        return error();
    }

    /**
     * 计算给定数值的指数函数值
     */
    @SqlExtensionExpression(dbType = H2, template = "EXP({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "EXP({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "EXP({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "EXP({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "EXP({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "EXP({a})")
    public static <T extends Number> double exp(T a) {
        return error();
    }

    /**
     * 获取最大值
     */
    @SqlExtensionExpression(dbType = H2, template = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = Oracle, template = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = SQLServer, template = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = SQLite, template = "MAX({a},{b})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "GREATEST({a},{b})")
    public static <T extends Number> T big(T a, T b) {
        return error();
    }

    /**
     * 获取最大值
     */
    @SafeVarargs
    @SqlExtensionExpression(dbType = H2, template = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = Oracle, template = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = SQLServer, template = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = SQLite, template = "MAX({a},{b},{cs})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "GREATEST({a},{b},{cs})")
    public static <T extends Number> T big(T a, T b, T... cs) {
        return error();
    }

    /**
     * 获取最小值
     */
    @SqlExtensionExpression(dbType = H2, template = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = Oracle, template = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = SQLServer, template = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = SQLite, template = "MIN({a},{b})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "LEAST({a},{b})")
    public static <T extends Number> T small(T a, T b) {
        return error();
    }

    /**
     * 获取最小值
     */
    @SafeVarargs
    @SqlExtensionExpression(dbType = H2, template = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = Oracle, template = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = SQLServer, template = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = SQLite, template = "MIN({a},{b},{cs})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "LEAST({a},{b},{cs})")
    public static <T extends Number> T small(T a, T b, T... cs) {
        return error();
    }


    /**
     * 计算指定基数的对数
     */
    @SqlExtensionExpression(dbType = H2, template = "LN({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LN({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "LN({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "LOG({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "LN({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "LN({a})")
    public static <T extends Number> double ln(T a) {
        return error();
    }

    /**
     * 计算指定基数的对数
     *
     * @param a    数值，用于计算其对数
     * @param base 对数的基数
     */
    @SqlExtensionExpression(dbType = H2, template = "LOG({base},{a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LOG({base},{a})")
    @SqlExtensionExpression(dbType = Oracle, template = "LOG({base},{a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "LOG({a},{base})")
    @SqlExtensionExpression(dbType = SQLite, template = "LOG({base},{a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "LOG({base},{a})::FLOAT8")
    public static <T extends Number, Base extends Number> double log(T a, Base base) {
        return error();
    }

    /**
     * 计算指定基数为2的对数
     */
    @SqlExtensionExpression(dbType = H2, template = "LOG2({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LOG2({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "LOG(2,{a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "LOG({a},2)")
    @SqlExtensionExpression(dbType = SQLite, template = "LOG2({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "LOG(2,{a})::FLOAT8")
    public static <T extends Number> double log2(T a) {
        return error();
    }

    /**
     * 计算指定基数为10的对数
     */
    @SqlExtensionExpression(dbType = H2, template = "LOG10({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LOG10({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "LOG(10,{a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "LOG({a},10)")
    @SqlExtensionExpression(dbType = SQLite, template = "LOG10({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "LOG10({a})::FLOAT8")
    public static <T extends Number> double log10(T a) {
        return error();
    }

    /**
     * 取模运算
     */
    @SqlExtensionExpression(dbType = H2, template = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = Oracle, template = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = SQLServer, template = "({a} % {b})")
    @SqlExtensionExpression(dbType = SQLite, template = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "MOD({a},{b})")
    public static <T extends Number> T mod(T a, T b) {
        return error();
    }

    /**
     * 获取π
     */
    @SqlExtensionExpression(dbType = H2, template = "PI()")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "PI()")
    @SqlExtensionExpression(dbType = Oracle, template = "(" + Math.PI + ")")
    @SqlExtensionExpression(dbType = SQLServer, template = "PI()")
    @SqlExtensionExpression(dbType = SQLite, template = "PI()")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "PI()")
    public static double pi() {
        return error();
    }

    /**
     * 幂运算
     */
    @SqlExtensionExpression(dbType = H2, template = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = Oracle, template = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = SQLServer, template = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = SQLite, template = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "POWER({a},{b})")
    public static <T extends Number> double pow(T a, T b) {
        return error();
    }

    /**
     * 将角度转换为弧度
     */
    @SqlExtensionExpression(dbType = H2, template = "RADIANS({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "RADIANS({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "({a} * " + Math.PI + " / 180)")
    @SqlExtensionExpression(dbType = SQLServer, template = "RADIANS({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "RADIANS({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "RADIANS({a})")
    public static <T extends Number> double radians(T a) {
        return error();
    }

    /**
     * 获取从0-1的随机数
     */
    @SqlExtensionExpression(dbType = H2, template = "RAND()")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "RAND()")
    @SqlExtensionExpression(dbType = Oracle, template = "DBMS_RANDOM.VALUE")
    @SqlExtensionExpression(dbType = SQLServer, template = "RAND()")
    @SqlExtensionExpression(dbType = SQLite, template = "ABS(RANDOM() / 10000000000000000000.0)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "RANDOM()")
    public static double random() {
        return error();
    }

//    @SqlExtensionExpression(dbType = H2, template = "RAND({a})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "RAND({a})")
//    @SqlExtensionExpression(dbType = Oracle, template = "DBMS_RANDOM.VALUE")
//    @SqlExtensionExpression(dbType = SQLServer, template = "RAND({a})")
//    @SqlExtensionExpression(dbType = SQLite, template = "ABS(RANDOM() / 10000000000000000000.0)")
//    public static double random(int a)
//    {
//        boom();
//        return 0;
//    }

    /**
     * 四舍五入
     */
    @SqlExtensionExpression(dbType = H2, template = "ROUND({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ROUND({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "ROUND({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "ROUND({a},0)")
    @SqlExtensionExpression(dbType = SQLite, template = "ROUND({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "ROUND({a})::INT4")
    public static <T extends Number> int round(T a) {
        return error();
    }

    /**
     * 指定到多少位的小数位为止，四舍五入
     */
    @SqlExtensionExpression(dbType = H2, template = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = Oracle, template = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = SQLServer, template = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = SQLite, template = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "ROUND({a}::NUMERIC,{b})::FLOAT8")
    public static <T extends Number> T round(T a, int b) {
        return error();
    }

    /**
     * 如果数字大于0，sign函数返回1；如果数字小于0，返回-1；如果数字等于0，返回0
     */
    @SqlExtensionExpression(dbType = H2, template = "SIGN({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SIGN({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "SIGN({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "SIGN({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "SIGN({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "SIGN({a})::INT4")
    public static <T extends Number> int sign(T a) {
        return error();
    }

    /**
     * 计算平方根
     */
    @SqlExtensionExpression(dbType = H2, template = "SQRT({a})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SQRT({a})")
    @SqlExtensionExpression(dbType = Oracle, template = "SQRT({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "SQRT({a})")
    @SqlExtensionExpression(dbType = SQLite, template = "SQRT({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "SQRT({a})")
    public static <T extends Number> double sqrt(T a) {
        return error();
    }

    /**
     * 截取到指定小数位的小数
     */
    @SqlExtensionExpression(dbType = H2, template = "TRUNCATE({a},{b})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "TRUNCATE({a},{b})")
    @SqlExtensionExpression(dbType = Oracle, template = "TRUNC({a},{b})")
    @SqlExtensionExpression(dbType = SQLServer, template = "ROUND({a},{b},1)")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST(SUBSTR({a} * 1.0,1,INSTR({a} * 1.0,'.') + {b}) AS REAL)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TRUNC({a}::NUMERIC,{b})::FLOAT8")
    public static <T extends Number> double truncate(T a, int b) {
        return error();
    }

    /**
     * 截取到整数
     */
    @SqlExtensionExpression(dbType = H2, template = "TRUNCATE({a},0)")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "TRUNCATE({a},0)")
    @SqlExtensionExpression(dbType = Oracle, template = "TRUNC({a})")
    @SqlExtensionExpression(dbType = SQLServer, template = "ROUND({a},0,1)")
    @SqlExtensionExpression(dbType = SQLite, template = "TRUNC({a})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TRUNC({a})::INT4")
    public static <T extends Number> int truncate(T a) {
        return error();
    }

    // endregion

    // region [字符串]

    /**
     * 判断字符串是否为空
     */
    @SqlExtensionExpression(dbType = H2, template = "(CHAR_LENGTH({str}) = 0)")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "(CHAR_LENGTH({str}) = 0)")
    @SqlExtensionExpression(dbType = Oracle, template = "(NVL(LENGTH({str}),0) = 0)")
    @SqlExtensionExpression(dbType = SQLServer, template = "(DATALENGTH({str}) = 0)")
    @SqlExtensionExpression(dbType = SQLite, template = "(LENGTH({str}) = 0)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(LENGTH({str}) = 0)")
    public static boolean isEmpty(String str) {
        return error();
    }

    /**
     * 字符串转ASCII码
     */
    @SqlExtensionExpression(dbType = H2, template = "ASCII({str})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "ASCII({str})")
    @SqlExtensionExpression(dbType = Oracle, template = "ASCII({str})")
    @SqlExtensionExpression(dbType = SQLServer, template = "ASCII({str})")
    @SqlExtensionExpression(dbType = SQLite, template = "UNICODE({str})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "ASCII({str})")
    public static int strToAscii(String str) {
        return error();
    }

    /**
     * ASCII码转字符串
     */
    @SqlExtensionExpression(dbType = H2, template = "CHAR({t})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "CHAR({t})")
    @SqlExtensionExpression(dbType = Oracle, template = "CHR({t})")
    @SqlExtensionExpression(dbType = SQLServer, template = "CHAR({t})")
    @SqlExtensionExpression(dbType = SQLite, template = "CHAR({t})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "CHR({t})")
    public static String asciiToStr(int t) {
        return error();
    }

    /**
     * 获取字符串长度
     */
    @SqlExtensionExpression(dbType = H2, template = "CHAR_LENGTH({str})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "CHAR_LENGTH({str})")
    @SqlExtensionExpression(dbType = Oracle, template = "NVL(LENGTH({str}),0)")
    @SqlExtensionExpression(dbType = SQLServer, template = "LEN({str})")
    @SqlExtensionExpression(dbType = SQLite, template = "LENGTH({str})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "LENGTH({str})")
    public static int length(String str) {
        return error();
    }

    /**
     * 获取字节长度
     * <p>
     * <b><span style='color:red;'>特殊说明</span>：各数据库的字节长度计算方式不同，请根据实际情况选择。</b>
     */
    @SqlExtensionExpression(dbType = H2, template = "LENGTH({str})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LENGTH({str})")
    @SqlExtensionExpression(dbType = Oracle, template = "LENGTHB({str})")
    @SqlExtensionExpression(dbType = SQLServer, template = "DATALENGTH({str})")
    @SqlExtensionExpression(dbType = SQLite, template = "LENGTH(CAST({str} AS BLOB))")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "OCTET_LENGTH({str})")
    public static int byteLength(String str) {
        return error();
    }

    /**
     * 字符串拼接
     */
    @SqlExtensionExpression(dbType = H2, template = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = Oracle, template = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = SQLServer, template = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = SQLite, template = "({s1}||{s2})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "CONCAT({s1},{s2})")
    public static String concat(String s1, String s2) {
        return error();
    }

    /**
     * 字符串拼接
     */
    @SqlExtensionExpression(dbType = H2, template = "CONCAT({s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "CONCAT({s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = Oracle, template = "({s1}||{s2}||{ss})", separator = "||")
    @SqlExtensionExpression(dbType = SQLServer, template = "CONCAT({s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = SQLite, template = "({s1}||{s2}||{ss})", separator = "||")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "CONCAT({s1},{s2},{ss})")
    public static String concat(String s1, String s2, String... ss) {
        return error();
    }

    /**
     * 以指定字符为间隔，拼接字符串
     *
     * @param separator 间隔
     */
    @SqlExtensionExpression(dbType = H2, template = "CONCAT_WS({separator},{s1},{s2})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "CONCAT_WS({separator},{s1},{s2})")
    @SqlExtensionExpression(dbType = Oracle, template = "({s1}||{separator}||{s2})")
    @SqlExtensionExpression(dbType = SQLServer, template = "CONCAT_WS({separator},{s1},{s2})")
    @SqlExtensionExpression(dbType = SQLite, template = "({s1}||{separator}||{s2})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "CONCAT_WS({separator},{s1},{s2})")
    public static String join(String separator, String s1, String s2) {
        return error();
    }

    /**
     * 以指定字符为间隔，拼接字符串
     *
     * @param separator 间隔
     */
    @SqlExtensionExpression(dbType = H2, template = "CONCAT_WS({separator},{s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "CONCAT_WS({separator},{s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = Oracle, template = "", extension = OracleJoinExtension.class)
    @SqlExtensionExpression(dbType = SQLServer, template = "CONCAT_WS({separator},{s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = SqliteJoinExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "CONCAT_WS({separator},{s1},{s2},{ss})")
    public static String join(String separator, String s1, String s2, String... ss) {
        return error();
    }

//    todo
//    @SqlExtensionExpression(dbType = H2, template = "FORMAT({t},{format})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "FORMAT({t},{format})")
//    @SqlExtensionExpression(dbType = Oracle, template = "TO_CHAR({t},{format})")
//    @SqlExtensionExpression(dbType = SQLServer, template = "FORMAT({t},{format})")
//    @SqlExtensionExpression(dbType = SQLite, template = "PRINTF({format},{t})")
//    @SqlExtensionExpression(dbType = PostgreSQL, template = "FORMAT({format},{t})")
//    public static <T extends Number> String format(T t, String format)
//    {
//        boom();
//        return "";
//    }

    /**
     * 获取子串在字符串中的位置
     */
    @SqlExtensionExpression(dbType = H2, template = "INSTR({str},{subStr})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "INSTR({str},{subStr})")
    @SqlExtensionExpression(dbType = Oracle, template = "INSTR({str},{subStr})")
    @SqlExtensionExpression(dbType = SQLServer, template = "CHARINDEX({subStr},{str})")
    @SqlExtensionExpression(dbType = SQLite, template = "INSTR({str},{subStr})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "STRPOS({str},{subStr})")
    public static int indexOf(String str, String subStr) {
        return error();
    }

    /**
     * 获取子串在字符串中的位置，并且设置起始偏移
     */
    @SqlExtensionExpression(dbType = H2, template = "LOCATE({subStr},{str},{offset})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LOCATE({subStr},{str},{offset})")
    @SqlExtensionExpression(dbType = Oracle, template = "INSTR({str},{subStr},{offset})")
    @SqlExtensionExpression(dbType = SQLServer, template = "CHARINDEX({subStr},{str},{offset})")
    @SqlExtensionExpression(dbType = SQLite, template = "(INSTR(SUBSTR({str},{offset} + 1),{subStr}) + {offset})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(STRPOS(SUBSTR({str},{offset} + 1),{subStr}) + {offset})")
    public static int indexOf(String str, String subStr, int offset) {
        return error();
    }

    /**
     * 将字符串转换为小写
     */
    @SqlExtensionExpression(dbType = H2, template = "LOWER({str})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LOWER({str})")
    @SqlExtensionExpression(dbType = Oracle, template = "LOWER({str})")
    @SqlExtensionExpression(dbType = SQLServer, template = "LOWER({str})")
    @SqlExtensionExpression(dbType = SQLite, template = "LOWER({str})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "LOWER({str})")
    public static String toLowerCase(String str) {
        return error();
    }

    /**
     * 将字符串转换为大写
     */
    @SqlExtensionExpression(dbType = H2, template = "UPPER({str})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "UPPER({str})")
    @SqlExtensionExpression(dbType = Oracle, template = "UPPER({str})")
    @SqlExtensionExpression(dbType = SQLServer, template = "UPPER({str})")
    @SqlExtensionExpression(dbType = SQLite, template = "UPPER({str})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "UPPER({str})")
    public static String toUpperCase(String str) {
        return error();
    }

    /**
     * 返回字符串中从左开始的指定数量字符
     *
     * @param length 指定的数量
     */
    @SqlExtensionExpression(dbType = H2, template = "LEFT({str},{length})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LEFT({str},{length})")
    @SqlExtensionExpression(dbType = Oracle, template = "SUBSTR({str},1,{length})")
    @SqlExtensionExpression(dbType = SQLServer, template = "LEFT({str},{length})")
    @SqlExtensionExpression(dbType = SQLite, template = "SUBSTR({str},1,{length})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "LEFT({str},{length})")
    public static String left(String str, int length) {
        return error();
    }

    /**
     * 返回字符串中从右开始的指定数量字符
     *
     * @param length 指定的数量
     */
    @SqlExtensionExpression(dbType = H2, template = "RIGHT({str},{length})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "RIGHT({str},{length})")
    @SqlExtensionExpression(dbType = Oracle, template = "SUBSTR({str},LENGTH({str}) - ({length} - 1),{length})")
    @SqlExtensionExpression(dbType = SQLServer, template = "RIGHT({str},{length})")
    @SqlExtensionExpression(dbType = SQLite, template = "SUBSTR({str},LENGTH({str}) - ({length} - 1),{length})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "RIGHT({str},{length})")
    public static String right(String str, int length) {
        return error();
    }

    /**
     * 将字符串左侧重复指定字符以填充到指定长度
     *
     * @param length  指定长度
     * @param lpadStr 指定字符
     */
    @SqlExtensionExpression(dbType = H2, template = "LPAD({str},{length},{lpadStr})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LPAD({str},{length},{lpadStr})")
    @SqlExtensionExpression(dbType = Oracle, template = "LPAD({str},{length},{lpadStr})")
    @SqlExtensionExpression(dbType = SQLServer, template = "IIF({length} - LEN({str}) <= 0,{str},CONCAT(REPLICATE({lpadStr},{length} - LEN({str})),{str}))")
    @SqlExtensionExpression(dbType = SQLite, template = "IIF({length} - LENGTH({str}) <= 0,{str},(REPLICATE({lpadStr},{length} - LENGTH({str}))||{str}))")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "LPAD({str},{length},{lpadStr})")
    public static String leftPad(String str, int length, String lpadStr) {
        return error();
    }

    /**
     * 将字符串右侧重复指定字符以填充到指定长度
     *
     * @param length  指定长度
     * @param rpadStr 指定字符
     */
    @SqlExtensionExpression(dbType = H2, template = "RPAD({str},{length},{rpadStr})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "RPAD({str},{length},{rpadStr})")
    @SqlExtensionExpression(dbType = Oracle, template = "RPAD({str},{length},{rpadStr})")
    @SqlExtensionExpression(dbType = SQLServer, template = "IIF({length} - LEN({str}) <= 0,{str},CONCAT({str},REPLICATE({rpadStr},{length} - LEN({str}))))")
    @SqlExtensionExpression(dbType = SQLite, template = "IIF({length} - LENGTH({str}) <= 0,{str},({str}||REPLICATE({rpadStr},{length} - LENGTH({str}))))")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "RPAD({str},{length},{rpadStr})")
    public static String rightPad(String str, int length, String rpadStr) {
        return error();
    }

    /**
     * 去除字符串两端空格
     */
    @SqlExtensionExpression(dbType = H2, template = "TRIM({str})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "TRIM({str})")
    @SqlExtensionExpression(dbType = Oracle, template = "TRIM({str})")
    @SqlExtensionExpression(dbType = SQLServer, template = "TRIM({str})")
    @SqlExtensionExpression(dbType = SQLite, template = "TRIM({str})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "TRIM({str})")
    public static String trim(String str) {
        return error();
    }

    /**
     * 去除字符串左侧的空格
     */
    @SqlExtensionExpression(dbType = H2, template = "LTRIM({str})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "LTRIM({str})")
    @SqlExtensionExpression(dbType = Oracle, template = "LTRIM({str})")
    @SqlExtensionExpression(dbType = SQLServer, template = "LTRIM({str})")
    @SqlExtensionExpression(dbType = SQLite, template = "LTRIM({str})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "LTRIM({str})")
    public static String trimStart(String str) {
        return error();
    }

    /**
     * 去除字符串右侧的空格
     */
    @SqlExtensionExpression(dbType = H2, template = "RTRIM({str})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "RTRIM({str})")
    @SqlExtensionExpression(dbType = Oracle, template = "RTRIM({str})")
    @SqlExtensionExpression(dbType = SQLServer, template = "RTRIM({str})")
    @SqlExtensionExpression(dbType = SQLite, template = "RTRIM({str})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "RTRIM({str})")
    public static String trimEnd(String str) {
        return error();
    }

    /**
     * 替换字符串
     */
    @SqlExtensionExpression(dbType = H2, template = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = Oracle, template = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = SQLServer, template = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = SQLite, template = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "REPLACE({cur},{subs},{news})")
    public static String replace(String cur, String subs, String news) {
        return error();
    }

    /**
     * 反转字符串
     * <p>
     * <b><span style='color:red;'>特殊说明</span>：oracle的REVERSE函数似乎只支持ASCII。</b>
     */
    @SqlExtensionExpression(dbType = H2, template = "REVERSE({str})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "REVERSE({str})")
    @SqlExtensionExpression(dbType = Oracle, template = "REVERSE({str})")
    @SqlExtensionExpression(dbType = SQLServer, template = "REVERSE({str})")
    @SqlExtensionExpression(dbType = SQLite, template = "REVERSE({str})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "REVERSE({str})")
    public static String reverse(String str) {
        return error();
    }

    /**
     * 比较两个字符串的大小
     */
    @SqlExtensionExpression(dbType = H2, template = "STRCMP({s1},{s2})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "STRCMP({s1},{s2})")
    @SqlExtensionExpression(dbType = Oracle, template = "(CASE WHEN {s1} < {s2} THEN -1 WHEN {s1} = {s2} THEN 0 ELSE 1 END)")
    @SqlExtensionExpression(dbType = SQLServer, template = "(CASE WHEN {s1} < {s2} THEN -1 WHEN {s1} = {s2} THEN 0 ELSE 1 END)")
    @SqlExtensionExpression(dbType = SQLite, template = "(CASE WHEN {s1} < {s2} THEN -1 WHEN {s1} = {s2} THEN 0 ELSE 1 END)")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(CASE WHEN {s1} < {s2} THEN -1 WHEN {s1} = {s2} THEN 0 ELSE 1 END)")
    public static int compare(String s1, String s2) {
        return error();
    }

    /**
     * 截取字符串
     */
    @SqlExtensionExpression(dbType = H2, template = "SUBSTR({str},{beginIndex})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SUBSTR({str},{beginIndex})")
    @SqlExtensionExpression(dbType = Oracle, template = "SUBSTR({str},{beginIndex})")
    @SqlExtensionExpression(dbType = SQLServer, template = "SUBSTRING({str},{beginIndex},LEN({str}) - ({beginIndex} - 1))")
    @SqlExtensionExpression(dbType = SQLite, template = "SUBSTR({str},{beginIndex})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "SUBSTR({str},{beginIndex})")
    public static String subString(String str, int beginIndex) {
        return error();
    }

    /**
     * 截取字符串
     */
    @SqlExtensionExpression(dbType = H2, template = "SUBSTR({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "SUBSTR({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = Oracle, template = "SUBSTR({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = SQLServer, template = "SUBSTRING({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = SQLite, template = "SUBSTR({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "SUBSTR({str},{beginIndex},{endIndex})")
    public static String subString(String str, int beginIndex, int endIndex) {
        return error();
    }

    // endregion

    // region [控制流程]

    /**
     * 发起一段CASE表达式
     */
    @SqlExtensionExpression(template = "CASE {when} END")
    public static <R> R Case(When<R> when) {
        return error();
    }

    /**
     * 发起一段CASE表达式
     */
    @SafeVarargs
    @SqlExtensionExpression(template = "CASE {when} {rs} END")
    public static <R> R Case(When<R> when, When<R>... rs) {
        return error();
    }

    /**
     * 发起一段CASE表达式，并设置ELSE
     */
    @SqlExtensionExpression(template = "CASE {when} ELSE {elsePart} END")
    public static <R> R Case(R elsePart, When<R> when) {
        return error();
    }

    /**
     * 发起一段CASE表达式，并设置ELSE
     */
    @SafeVarargs
    @SqlExtensionExpression(template = "CASE {when} {rs} ELSE {elsePart} END", separator = " ")
    public static <R> R Case(R elsePart, When<R> when, When<R>... rs) {
        return error();
    }

    /**
     * 编写CASE表达式的WHEN子句
     */
    @SqlExtensionExpression(template = "WHEN {condition} THEN {then}")
    public static <R> When<R> when(boolean condition, R then) {
        return error();
    }

    /**
     * @param condition 判断条件
     * @param truePart  如果条件成立，返回的值
     * @param falsePart 如果条件不成立，返回的值
     */
    @SqlExtensionExpression(dbType = H2, template = "IF({condition},{truePart},{falsePart})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "IF({condition},{truePart},{falsePart})")
    @SqlExtensionExpression(dbType = Oracle, template = "(CASE WHEN {condition} THEN {truePart} ELSE {falsePart} END)")
    @SqlExtensionExpression(dbType = SQLServer, template = "IIF({condition},{truePart},{falsePart})")
    @SqlExtensionExpression(dbType = SQLite, template = "IIF({condition},{truePart},{falsePart})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "(CASE WHEN {condition} THEN {truePart} ELSE {falsePart} END)")
    public static <T> T If(boolean condition, T truePart, T falsePart) {
        return error();
    }

    /**
     * 如果值不为NULL，则返回该值，否则返回另一个值
     *
     * @param valueNotNull 如果值不为NULL，则返回该值
     * @param valueIsNull  如果值为NULL，则返回该值
     */
    @SqlExtensionExpression(dbType = H2, template = "IFNULL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "IFNULL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = Oracle, template = "NVL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = SQLServer, template = "ISNULL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = SQLite, template = "IFNULL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "COALESCE({valueNotNull},{valueIsNull})")
    public static <T> T ifNull(T valueNotNull, T valueIsNull) {
        return error();
    }

    /**
     * 如果两个值相等，则返回NULL，否则返回第一个值
     */
    @SqlExtensionExpression(dbType = H2, template = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = Oracle, template = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = SQLServer, template = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = SQLite, template = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "NULLIF({t1},{t2})")
    public static <T> T nullIf(T t1, T t2) {
        return error();
    }

    /**
     * 类型转换
     */
    @SqlExtensionExpression(dbType = H2, template = "", extension = TypeCastExtension.class)
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "", extension = TypeCastExtension.class)
    @SqlExtensionExpression(dbType = Oracle, template = "", extension = TypeCastExtension.class)
    @SqlExtensionExpression(dbType = SQLServer, template = "", extension = TypeCastExtension.class)
    @SqlExtensionExpression(dbType = SQLite, template = "", extension = TypeCastExtension.class)
    @SqlExtensionExpression(dbType = PostgreSQL, template = "", extension = TypeCastExtension.class)
    public static <T> T cast(Object value, Class<T> targetType) {
        return error();
    }

    /**
     * 类型转换
     */
    @SqlExtensionExpression(dbType = H2, template = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = {MySQL, Doris}, template = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = Oracle, template = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = SQLServer, template = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = SQLite, template = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = PostgreSQL, template = "{value}::{targetType}")
    public static <T> T cast(Object value, SqlTypes<T> targetType) {
        return error();
    }

//    @SqlExtensionExpression(dbType = H2, template = "CAST({value} AS {targetType})")
//    @SqlExtensionExpression(dbType = {MySQL,Doris}, template = "CONVERT({targetType},{value})")
//    @SqlExtensionExpression(dbType = Oracle, template = "CAST({value} AS {targetType})")
//    @SqlExtensionExpression(dbType = SQLServer, template = "CONVERT({targetType},{value})")
//    public static <T> T convert(Object value, Class<T> targetType)
//    {
//        boom();
//        return (T) new Object();
//    }

    /**
     * 判断是否为NULL
     */
    @SqlExtensionExpression(template = "{t} IS NULL")
    public static <T> boolean isNull(T t) {
        return error();
    }

    /**
     * 判断是否不为NULL
     */
    @SqlExtensionExpression(template = "{t} IS NOT NULL")
    public static <T> boolean isNotNull(T t) {
        return error();
    }

    // endregion

    public final static class When<R> {
        private When() {
        }
    }
}
