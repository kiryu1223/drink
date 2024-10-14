package io.github.kiryu1223.drink.ext;


import io.github.kiryu1223.drink.annotation.SqlExtensionExpression;
import io.github.kiryu1223.drink.exception.SqlFunctionInvokeException;
import io.github.kiryu1223.drink.ext.mysql.MySqlDateTimeDiffExtension;
import io.github.kiryu1223.drink.ext.oracle.OracleAddOrSubDateExtension;
import io.github.kiryu1223.drink.ext.oracle.OracleCastExtension;
import io.github.kiryu1223.drink.ext.oracle.OracleDateTimeDiffExtension;
import io.github.kiryu1223.drink.ext.oracle.OracleJoinExtension;
import io.github.kiryu1223.drink.ext.pgsql.PostgreSQLAddOrSubDateExtension;
import io.github.kiryu1223.drink.ext.pgsql.PostgreSQLDateTimeDiffExtension;
import io.github.kiryu1223.drink.ext.sqlite.SqliteAddOrSubDateExtension;
import io.github.kiryu1223.drink.ext.sqlite.SqliteDateTimeDiffExtension;
import io.github.kiryu1223.drink.ext.sqlite.SqliteJoinExtension;
import io.github.kiryu1223.drink.ext.types.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SqlFunctions
{

    // region [聚合函数]

    @SqlExtensionExpression(function = "COUNT(*)")
    public static long count()
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(function = "COUNT({t})")
    public static <T> long count(T t)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(function = "SUM({t})")
    public static <T> T sum(T t)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(function = "AVG({t})")
    public static <T extends Number> BigDecimal avg(T t)
    {
        boom();
        return BigDecimal.ZERO;
    }

    @SqlExtensionExpression(function = "MIN({t})")
    public static <T> T min(T t)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(function = "MAX({t})")
    public static <T> T max(T t)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "GROUP_CONCAT({property})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GROUP_CONCAT({property})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LISTAGG({property}) WITHIN GROUP (ORDER BY {property})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "STRING_AGG({property},'')")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "GROUP_CONCAT({property})")
    public static String groupJoin(String property)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "GROUP_CONCAT({property} SEPARATOR {delimiter})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GROUP_CONCAT({property} SEPARATOR {delimiter})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LISTAGG({property},{delimiter}) WITHIN GROUP (ORDER BY {property})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "STRING_AGG({property},{delimiter})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "GROUP_CONCAT({property},{delimiter})")
    public static <T> String groupJoin(String delimiter, T property)
    {
        boom();
        return "";
    }

    // endregion

    // region [时间]

    @SqlExtensionExpression(dbType = DbType.H2, function = "NOW()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "NOW()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST(CURRENT_TIMESTAMP AS TIMESTAMP)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "GETDATE()")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "DATETIME('now','localtime')")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "NOW()")
    public static LocalDateTime now()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "NOW({precision})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "NOW({precision})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST(CURRENT_TIMESTAMP AS TIMESTAMP)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "GETDATE()")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "DATETIME('now','localtime')")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "NOW()")
    public static LocalDateTime now(int precision)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UTC_TIMESTAMP()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UTC_TIMESTAMP()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "GETUTCDATE()")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "DATETIME('now')")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')")
    public static LocalDateTime utcNow()
    {
        boom();
        return LocalDateTime.now();
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "LOCALTIME()")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOCALTIME()")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOCALTIMESTAMP")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CURRENT_DATE")
//    public static LocalDateTime localNow()
//    {
//        boom();
//        return LocalDateTime.now();
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SYSDATE()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SYSDATE()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SYSDATE")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "SYSDATETIME()")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "DATETIME('now','localtime')")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "NOW()")
    public static LocalDateTime systemNow()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CURDATE()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CURDATE()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CURRENT_DATE")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CAST(GETDATE() AS DATE)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "DATE('now','localtime')")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "CURRENT_DATE")
    public static LocalDate nowDate()
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CURTIME()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CURTIME()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CURRENT_DATE")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CAST(GETDATE() AS TIME)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "TIME('now','localtime')")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "CURRENT_TIME")
    public static LocalTime nowTime()
    {
        boom();
        return LocalTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UTC_DATE()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UTC_DATE()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST(SYS_EXTRACT_UTC(CURRENT_TIMESTAMP) AS DATE)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CAST(GETUTCDATE() AS DATE)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "DATE('now')")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')::DATE")
    public static LocalDate utcNowDate()
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UTC_TIME()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UTC_TIME()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST(SYS_EXTRACT_UTC(CURRENT_TIMESTAMP) AS DATE)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CAST(GETUTCDATE() AS TIME)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "TIME('now')")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')::TIME")
    public static LocalTime utcNowTime()
    {
        boom();
        return LocalTime.now();
    }

    //    public static LocalDateTime addDays(LocalDateTime time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addDays(LocalDate time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addHours(LocalDateTime time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addHours(LocalDate time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addMinutes(LocalDateTime time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addMinutes(LocalDate time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addSeconds(LocalDateTime time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addSeconds(LocalDate time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addMicroSeconds(LocalDateTime time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addMicroSeconds(LocalDate time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addMilliSeconds(LocalDateTime time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addMilliSeconds(LocalDate time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addNanoSeconds(LocalDateTime time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addNanoSeconds(LocalDate time, long duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addMonths(LocalDateTime time, int duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addMonths(LocalDate time, int duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addYears(LocalDateTime time, int duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    public static LocalDateTime addYears(LocalDate time, int duration)
//    {
//        boom();
//        return LocalDateTime.now();
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEADD({unit},{num},{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDateTime addDate(LocalDateTime time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEADD({unit},{num},{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDate addDate(LocalDate time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEADD(DAY,{days},{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDateTime addDate(LocalDateTime time, int days)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEADD(DAY,{days},{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDate addDate(LocalDate time, int days)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEADD({unit},-({num}),{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDateTime subDate(LocalDateTime time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEADD({unit},-({num}),{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDate subDate(LocalDate time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEADD(DAY,-({days}),{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDateTime subDate(LocalDateTime time, int days)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEADD(DAY,-({days}),{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLAddOrSubDateExtension.class)
    public static LocalDate subDate(LocalDate time, int days)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDateTime t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDateTime t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDateTime t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDate t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDate t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDate t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, String t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, String t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "", extension = PostgreSQLDateTimeDiffExtension.class)
    public static long dateTimeDiff(SqlTimeUnit unit, String t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},{format})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "STRFTIME({format},{time})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TO_CHAR({time},{format})")
    public static String dateFormat(LocalDateTime time, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},{format})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "STRFTIME({format},{time})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TO_CHAR({time},{format})")
    public static String dateFormat(LocalDate time, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),{format})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "STRFTIME({format},{time})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TO_CHAR({time}::TIMESTAMP,{format})")
    public static String dateFormat(String time, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(DAY FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(DAY,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%d',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(DAY FROM {time})::INT")
    public static int getDay(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(DAY FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(DAY,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%d',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(DAY FROM {time})::INT")
    public static int getDay(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(DAY FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(DAY,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%d',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(DAY FROM {time}::TIMESTAMP)::INT")
    public static int getDay(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'DAY')")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATENAME(WEEKDAY,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 'Sunday' WHEN '1' THEN 'Monday' WHEN '2' THEN 'Tuesday' WHEN '3' THEN 'Wednesday' WHEN '4' THEN 'Thursday' WHEN '5' THEN 'Friday' WHEN '6' THEN 'Saturday' END)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TRIM(TO_CHAR({time},'Day'))")
    public static String getDayName(LocalDateTime time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'DAY')")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATENAME(WEEKDAY,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 'Sunday' WHEN '1' THEN 'Monday' WHEN '2' THEN 'Tuesday' WHEN '3' THEN 'Wednesday' WHEN '4' THEN 'Thursday' WHEN '5' THEN 'Friday' WHEN '6' THEN 'Saturday' END)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TRIM(TO_CHAR({time},'Day'))")
    public static String getDayName(LocalDate time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'DAY')")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATENAME(WEEKDAY,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 'Sunday' WHEN '1' THEN 'Monday' WHEN '2' THEN 'Tuesday' WHEN '3' THEN 'Wednesday' WHEN '4' THEN 'Thursday' WHEN '5' THEN 'Friday' WHEN '6' THEN 'Saturday' END)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TRIM(TO_CHAR({time}::TIMESTAMP,'Day'))")
    public static String getDayName(String time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'D'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "(DATEPART(WEEKDAY,{time}))")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%w',{time}) + 1)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(EXTRACT(DOW FROM {time}) + 1)::INT")
    public static int getDayOfWeek(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'D'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "(DATEPART(WEEKDAY,{time}))")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%w',{time}) + 1)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(EXTRACT(DOW FROM {time}) + 1)::INT")
    public static int getDayOfWeek(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'D'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(WEEKDAY,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%w',{time}) + 1)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(EXTRACT(DOW FROM {time}::TIMESTAMP) + 1)::INT")
    public static int getDayOfWeek(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'DDD'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(DAYOFYEAR,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%j',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(DOY FROM {time})::INT")
    public static int getDayOfYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'DDD'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(DAYOFYEAR,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%j',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(DOY FROM {time})::INT")
    public static int getDayOfYear(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'DDD'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(DAYOFYEAR,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%j',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(DOY FROM {time}::TIMESTAMP)::INT")
    public static int getDayOfYear(String time)
    {
        boom();
        return 0;
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "SEC_TO_TIME({second})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SEC_TO_TIME({second})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE(TO_CHAR(FLOOR({second} / 3600), 'FM00')||':'||TO_CHAR(FLOOR(MOD({second}, 3600) / 60), 'FM00')||':'||TO_CHAR(MOD({second}, 60), 'FM00'))")
//    public static LocalTime toTime(int second)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "STR_TO_DATE({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "STR_TO_DATE({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE({time}, 'YYYY-MM-DD')")
//    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CONVERT(DATE,{time},23)")
//    public static LocalDate toDate(String time)
//    {
//        boom();
//        return LocalDate.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "STR_TO_DATE({time},{format})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "STR_TO_DATE({time},{format})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE({time},{format})")
//    public static LocalDate toDate(String time, String format)
//    {
//        boom();
//        return LocalDate.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "FROM_DAYS({days})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "FROM_DAYS({days})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(TO_DATE('1970-01-01', 'YYYY-MM-DD') + ({days} - 719163))")
//    public static LocalDate toDate(long days)
//    {
//        boom();
//        return LocalDate.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "STR_TO_DATE({time},{format})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "STR_TO_DATE({time},{format})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE({time},{format})")
//    public static LocalDateTime toDateTime(String time, String format)
//    {
//        boom();
//        return LocalDateTime.now();
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRUNC({time} - (TO_DATE('0001-01-01', 'YYYY-MM-DD') - INTERVAL '1' YEAR) - 2)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "(DATEDIFF(DAY,'0001-01-01',{time}) + 366)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(JULIANDAY({time}) - JULIANDAY('0000-01-01'))")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(EXTRACT(EPOCH FROM {time})::INT / 86400 + 719528)")
    public static int dateToDays(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRUNC({time} - (TO_DATE('0001-01-01', 'YYYY-MM-DD') - INTERVAL '1' YEAR) - 2)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "(DATEDIFF(DAY,'0001-01-01',{time}) + 366)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(JULIANDAY({time}) - JULIANDAY('0000-01-01'))")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(EXTRACT(EPOCH FROM {time})::INT / 86400 + 719528)")
    public static int dateToDays(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(DAY FROM (TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff') - (TO_TIMESTAMP('0001-01-01', 'YYYY-MM-DD') - INTERVAL '1' YEAR) - INTERVAL '2' DAY))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "(DATEDIFF(DAY,'0001-01-01',{time}) + 366)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(JULIANDAY({time}) - JULIANDAY('0000-01-01'))")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(EXTRACT(EPOCH FROM {time}::TIMESTAMP)::INT / 86400 + 719528)")
    public static int dateToDays(String time)
    {
        boom();
        return 0;
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "((EXTRACT(HOUR FROM {time}) * 3600) + (EXTRACT(MINUTE FROM {time}) * 60) + EXTRACT(SECOND FROM {time}))")
//    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(HOUR,{time}) * 3600 + DATEPART(MINUTE,{time}) * 60 + DATEPART(SECOND,{time})")
//    public static int toSecond(LocalDateTime time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "((EXTRACT(HOUR FROM {time}) * 3600) + (EXTRACT(MINUTE FROM {time}) * 60) + EXTRACT(SECOND FROM {time}))")
//    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(HOUR,{time}) * 3600 + DATEPART(MINUTE,{time}) * 60 + DATEPART(SECOND,{time})")
//    public static int toSecond(LocalTime time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "((EXTRACT(HOUR FROM TO_DATE({time})) * 3600) + (EXTRACT(MINUTE FROM TO_DATE({time})) * 60) + EXTRACT(SECOND FROM TO_DATE({time})))")
//    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(HOUR,{time}) * 3600 + DATEPART(MINUTE,{time}) * 60 + DATEPART(SECOND,{time})")
//    public static int toSecond(String time)
//    {
//        boom();
//        return 0;
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(HOUR FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(HOUR,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%H',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(HOUR FROM {time})::INT")
    public static int getHour(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(HOUR FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(HOUR,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%H',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(HOUR FROM {time})::INT")
    public static int getHour(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(HOUR FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(HOUR,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%H',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(HOUR FROM {time}::TIMESTAMP)::INT")
    public static int getHour(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "EOMONTH({time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "DATE({time},'start of month','+1 month','-1 day')")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(DATE_TRUNC('MONTH',{time}) + INTERVAL '1' MONTH - INTERVAL '1' DAY)::DATE")
    public static LocalDate getLastDay(LocalDateTime time)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "EOMONTH({time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "DATE({time},'start of month','+1 month','-1 day')")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(DATE_TRUNC('MONTH',{time}) + INTERVAL '1' MONTH - INTERVAL '1' DAY)::DATE")
    public static LocalDate getLastDay(LocalDate time)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LAST_DAY(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "EOMONTH({time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "DATE({time},'start of month','+1 month','-1 day')")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(DATE_TRUNC('MONTH',{time}::TIMESTAMP) + INTERVAL '1' MONTH - INTERVAL '1' DAY)::DATE")
    public static LocalDate getLastDay(String time)
    {
        boom();
        return LocalDate.now();
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "MAKEDATE({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MAKEDATE({},{})")
//    public static LocalDate createDate(int year, int days)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "MAKETIME({},{},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MAKETIME({},{},{})")
//    public static LocalTime createTime(int hour, int minute, int second)
//    {
//        boom();
//        return 0;
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MINUTE({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MINUTE({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MINUTE FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(MINUTE,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%M',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(MINUTE FROM {time})::INT")
    public static int getMinute(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MINUTE({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MINUTE({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MINUTE FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(MINUTE,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%M',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(MINUTE FROM {time})::INT")
    public static int getMinute(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MINUTE({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MINUTE({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MINUTE FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(MINUTE,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%M',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(MINUTE FROM {time}::TIMESTAMP)::INT")
    public static int getMinute(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MONTH FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(MINUTE,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%m',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(MONTH FROM {time})::INT")
    public static int getMonth(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MONTH FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(MONTH,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%m',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(MONTH FROM {time})::INT")
    public static int getMonth(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MONTH FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(MONTH,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%m',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(MONTH FROM {time}::TIMESTAMP)::INT")
    public static int getMonth(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'FMMONTH')")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "FORMAT({time}, 'MMMM')")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(CASE STRFTIME('%m',{time}) WHEN '01' THEN 'January' WHEN '02' THEN 'February' WHEN '03' THEN 'March' WHEN '04' THEN 'April' WHEN '05' THEN 'May' WHEN '06' THEN 'June' WHEN '07' THEN 'July' WHEN '08' THEN 'August' WHEN '09' THEN 'September' WHEN '10' THEN 'October' WHEN '11' THEN 'November' WHEN '12' THEN 'December' END)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TRIM(TO_CHAR({time},'Month'))")
    public static String getMonthName(LocalDate time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'FMMONTH')")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "FORMAT({time}, 'MMMM')")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(CASE STRFTIME('%m',{time}) WHEN '01' THEN 'January' WHEN '02' THEN 'February' WHEN '03' THEN 'March' WHEN '04' THEN 'April' WHEN '05' THEN 'May' WHEN '06' THEN 'June' WHEN '07' THEN 'July' WHEN '08' THEN 'August' WHEN '09' THEN 'September' WHEN '10' THEN 'October' WHEN '11' THEN 'November' WHEN '12' THEN 'December' END)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TRIM(TO_CHAR({time},'Month'))")
    public static String getMonthName(LocalDateTime time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'FMMONTH')")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "FORMAT(CONVERT(DATE,{time}), 'MMMM')")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(CASE STRFTIME('%m',{time}) WHEN '01' THEN 'January' WHEN '02' THEN 'February' WHEN '03' THEN 'March' WHEN '04' THEN 'April' WHEN '05' THEN 'May' WHEN '06' THEN 'June' WHEN '07' THEN 'July' WHEN '08' THEN 'August' WHEN '09' THEN 'September' WHEN '10' THEN 'October' WHEN '11' THEN 'November' WHEN '12' THEN 'December' END)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TRIM(TO_CHAR({time}::TIMESTAMP,'Month'))")
    public static String getMonthName(String time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL(EXTRACT(MONTH FROM {time}) / 3)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(QUARTER,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "((strftime('%m',{time}) + 2) / 3)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(QUARTER FROM {time})::INT")
    public static int getQuarter(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL(EXTRACT(MONTH FROM {time}) / 3)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(QUARTER,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "((strftime('%m',{time}) + 2) / 3)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(QUARTER FROM {time})::INT")
    public static int getQuarter(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL(EXTRACT(MONTH FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff')) / 3)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(QUARTER,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "((strftime('%m',{time}) + 2) / 3)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(QUARTER FROM {time}::TIMESTAMP)::INT")
    public static int getQuarter(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(SECOND FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(SECOND,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%S',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(SECOND FROM {time})::INT")
    public static int getSecond(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(SECOND FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(SECOND,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%S',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(SECOND FROM {time})::INT")
    public static int getSecond(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(SECOND FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(SECOND,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%S',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(SECOND FROM {time}::TIMESTAMP)::INT")
    public static int getSecond(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(EXTRACT(SECOND FROM {time}) * 1000)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(MS,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%f',{time}) * 1000)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(MILLISECOND from {time})::INT")
    public static int getMilliSecond(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(EXTRACT(SECOND FROM {time}) * 1000)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(MS,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%f',{time}) * 1000)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(MILLISECOND from {time})::INT")
    public static int getMilliSecond(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(EXTRACT(SECOND FROM {time}) * 1000)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(MS,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%f',{time}) * 1000)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(MILLISECOND from {time}::TIMESTAMP)::INT")
    public static int getMilliSecond(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(WEEK,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(WEEK from {time})::INT")
    public static int getWeek(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(WEEK,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(WEEK from {time})::INT")
    public static int getWeek(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'IW'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(WEEK,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(WEEK from {time}::TIMESTAMP)::INT")
    public static int getWeek(String time)
    {
        boom();
        return 0;
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({time},{firstDayofweek})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({time},{firstDayofweek})")
//    public static int getWeek(String time, int firstDayofweek)
//    {
//        boom();
//        return 0;
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CASE TO_NUMBER(TO_CHAR({time},'D')) WHEN 1 THEN 6 ELSE TO_NUMBER(TO_CHAR({time},'D')) - 2 END)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "(CASE DATEPART(WEEKDAY,{time}) WHEN 1 THEN 6 ELSE DATEPART(WEEKDAY,{time}) - 2 END)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 6 ELSE STRFTIME('%w',{time}) - 1 END)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "((EXTRACT(DOW FROM {time}) + 6) % 7)::INT")
    public static int getWeekDay(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CASE TO_NUMBER(TO_CHAR({time},'D')) WHEN 1 THEN 6 ELSE TO_NUMBER(TO_CHAR({time},'D')) - 2 END)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "(CASE DATEPART(WEEKDAY,{time}) WHEN 1 THEN 6 ELSE DATEPART(WEEKDAY,{time}) - 2 END)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 6 ELSE STRFTIME('%w',{time}) - 1 END)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "((EXTRACT(DOW FROM {time}) + 6) % 7)::INT")
    public static int getWeekDay(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CASE TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'D')) WHEN 1 THEN 6 ELSE TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'D')) - 2 END)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "(CASE DATEPART(WEEKDAY,{time}) WHEN 1 THEN 6 ELSE DATEPART(WEEKDAY,{time}) - 2 END)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(CASE STRFTIME('%w',{time}) WHEN '0' THEN 6 ELSE STRFTIME('%w',{time}) - 1 END)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "((EXTRACT(DOW FROM {time}::TIMESTAMP) + 6) % 7)::INT")
    public static int getWeekDay(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(ISO_WEEK,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(WEEK FROM {time})::INT")
    public static int getWeekOfYear(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(ISO_WEEK,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(WEEK FROM {time})::INT")
    public static int getWeekOfYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'IW'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(ISO_WEEK,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(STRFTIME('%W',{time}) + 1)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(WEEK FROM {time}::TIMESTAMP)::INT")
    public static int getWeekOfYear(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(YEAR FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(YEAR,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%Y',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(YEAR FROM {time})::INT")
    public static int getYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(YEAR FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(YEAR,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%Y',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(YEAR FROM {time})::INT")
    public static int getYear(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(YEAR FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATEPART(YEAR,{time})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(STRFTIME('%Y',{time}) AS INTEGER)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXTRACT(YEAR FROM {time}::TIMESTAMP)::INT")
    public static int getYear(String time)
    {
        boom();
        return 0;
    }

    // endregion

    // region [数值]

    @SqlExtensionExpression(dbType = DbType.H2, function = "ABS({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ABS({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ABS({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "ABS({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "ABS({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "ABS({a})")
    public static <T extends Number> T abs(T a)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "COS({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "COS({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "COS({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "COS({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "COS({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "COS({a})")
    public static <T extends Number> double cos(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SIN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SIN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SIN({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "SIN({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "SIN({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "SIN({a})")
    public static <T extends Number> double sin(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TAN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TAN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TAN({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "TAN({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "TAN({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TAN({a})")
    public static <T extends Number> double tan(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ACOS({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ACOS({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ACOS({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "ACOS({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "ACOS({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "ACOS({a})")
    public static <T extends Number> double acos(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ASIN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ASIN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ASIN({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "ASIN({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "ASIN({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "ASIN({a})")
    public static <T extends Number> double asin(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ATAN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ATAN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ATAN({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "ATAN({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "ATAN({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "ATAN({a})")
    public static <T extends Number> double atan(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "ATN2({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "ATAN2({a},{b})")
    public static <T extends Number> double atan2(T a, T b)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CEIL({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CEIL({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CEILING({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CEIL({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "CEIL({a})::INT")
    public static <T extends Number> int ceil(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "FLOOR({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "FLOOR({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "FLOOR({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "FLOOR({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "FLOOR({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "FLOOR({a})::INT")
    public static <T extends Number> int floor(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "COT({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "COT({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CASE SIN({a}) WHEN 0 THEN 0 ELSE COS({a}) / SIN({a}) END)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "COT({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "COT({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "COT({a})")
    public static <T extends Number> double cot(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DEGREES({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DEGREES({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({a} * 180 / " + Math.PI + ")")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DEGREES({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "DEGREES({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "DEGREES({a})")
    public static <T extends Number> double degrees(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "EXP({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "EXP({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXP({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "EXP({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "EXP({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "EXP({a})")
    public static <T extends Number> double exp(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "MAX({a},{b})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "GREATEST({a},{b})")
    public static <T extends Number> T big(T a, T b)
    {
        boom();
        return (T) new Num();
    }

    @SafeVarargs
    @SqlExtensionExpression(dbType = DbType.H2, function = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "MAX({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "GREATEST({a},{b},{cs})")
    public static <T extends Number> T big(T a, T b, T... cs)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "MIN({a},{b})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "LEAST({a},{b})")
    public static <T extends Number> T small(T a, T b)
    {
        boom();
        return (T) new Num();
    }

    @SafeVarargs
    @SqlExtensionExpression(dbType = DbType.H2, function = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "MIN({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "LEAST({a},{b},{cs})")
    public static <T extends Number> T small(T a, T b, T... cs)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LN({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "LOG({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "LN({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "LN({a})")
    public static <T extends Number> double ln(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOG({base},{a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOG({base},{a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOG({base},{a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "LOG({a},{base})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "LOG({base},{a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "LOG({base},{a})::FLOAT8")
    public static <T extends Number, Base extends Number> double log(T a, Base base)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOG2({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOG2({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOG(2,{a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "LOG({a},2)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "LOG2({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "LOG(2,{a})::FLOAT8")
    public static <T extends Number> double log2(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOG10({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOG10({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOG(10,{a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "LOG({a},10)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "LOG10({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "LOG10({a})::FLOAT8")
    public static <T extends Number> double log10(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "({a} % {b})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "MOD({a},{b})")
    public static <T extends Number> T mod(T a, T b)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "PI()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "PI()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(" + Math.PI + ")")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "PI()")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "PI()")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "PI()")
    public static double pi()
    {
        boom();
        return 3.14159;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "POWER({a},{b})")
    public static <T extends Number> double pow(T a, T b)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RADIANS({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RADIANS({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({a} * " + Math.PI + " / 180)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "RADIANS({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "RADIANS({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "RADIANS({a})")
    public static <T extends Number> double radians(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RAND()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RAND()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "DBMS_RANDOM.VALUE")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "RAND()")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "ABS(RANDOM() / 10000000000000000000.0)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "RANDOM()")
    public static double random()
    {
        boom();
        return 0;
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "RAND({a})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RAND({a})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "DBMS_RANDOM.VALUE")
//    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "RAND({a})")
//    @SqlExtensionExpression(dbType = DbType.SQLite, function = "ABS(RANDOM() / 10000000000000000000.0)")
//    public static double random(int a)
//    {
//        boom();
//        return 0;
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ROUND({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ROUND({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ROUND({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "ROUND({a},0)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "ROUND({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "ROUND({a})::INT")
    public static <T extends Number> int round(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "ROUND({a}::NUMERIC,{b})::FLOAT8")
    public static <T extends Number> T round(T a, int b)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SIGN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SIGN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SIGN({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "SIGN({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "SIGN({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "SIGN({a})::INT")
    public static <T extends Number> int sign(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SQRT({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SQRT({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SQRT({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "SQRT({a})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "SQRT({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "SQRT({a})")
    public static <T extends Number> double sqrt(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TRUNCATE({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TRUNCATE({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRUNC({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "ROUND({a},{b},1)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST(SUBSTR({a} * 1.0,1,INSTR({a} * 1.0,'.') + {b}) AS REAL)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TRUNC({a}::NUMERIC,{b})::FLOAT8")
    public static <T extends Number> double truncate(T a, int b)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TRUNCATE({a},0)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TRUNCATE({a},0)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRUNC({a})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "ROUND({a},0,1)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "TRUNC({a})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TRUNC({a})::INT")
    public static <T extends Number> int truncate(T a)
    {
        boom();
        return 0;
    }

    // endregion

    // region [字符串]

    @SqlExtensionExpression(dbType = DbType.H2, function = "(CHAR_LENGTH({str}) = 0)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "(CHAR_LENGTH({str}) = 0)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(NVL(LENGTH({str}),0) = 0)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "(DATALENGTH({str}) = 0)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(LENGTH({str}) = 0)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(LENGTH({str}) = 0)")
    public static boolean isEmpty(String str)
    {
        boom();
        return true;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ASCII({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ASCII({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ASCII({str})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "ASCII({str})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "UNICODE({str})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "ASCII({str})")
    public static int strToAscii(String str)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CHAR({t})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CHAR({t})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CHR({t})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CHAR({t})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CHAR({t})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "CHR({t})")
    public static String asciiToStr(int t)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CHAR_LENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CHAR_LENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NVL(LENGTH({str}),0)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "LEN({str})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "LENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "LENGTH({str})")
    public static int length(String str)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LENGTHB({str})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "DATALENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "LENGTH(CAST({str} AS BLOB))")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "OCTET_LENGTH({str})")
    public static int byteLength(String str)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "({s1}||{s2})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "CONCAT({s1},{s2})")
    public static String concat(String s1, String s2)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT({s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT({s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({s1}||{s2}||{ss})", separator = "||")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CONCAT({s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "({s1}||{s2}||{ss})", separator = "||")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "CONCAT({s1},{s2},{ss})")
    public static String concat(String s1, String s2, String... ss)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT_WS({separator},{s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT_WS({separator},{s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({s1}||{separator}||{s2})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CONCAT_WS({separator},{s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "({s1}||{separator}||{s2})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "CONCAT_WS({separator},{s1},{s2})")
    public static String join(String separator, String s1, String s2)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT_WS({separator},{s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT_WS({separator},{s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleJoinExtension.class)
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CONCAT_WS({separator},{s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "", extension = SqliteJoinExtension.class)
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "CONCAT_WS({separator},{s1},{s2},{ss})")
    public static String join(String separator, String s1, String s2, String... ss)
    {
        boom();
        return "";
    }

//    todo
//    @SqlExtensionExpression(dbType = DbType.H2, function = "FORMAT({t},{format})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "FORMAT({t},{format})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({t},{format})")
//    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "FORMAT({t},{format})")
//    @SqlExtensionExpression(dbType = DbType.SQLite, function = "PRINTF({format},{t})")
//    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "FORMAT({format},{t})")
//    public static <T extends Number> String format(T t, String format)
//    {
//        boom();
//        return "";
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "INSTR({str},{subStr})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "INSTR({str},{subStr})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "INSTR({str},{subStr})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CHARINDEX({subStr},{str})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "INSTR({str},{subStr})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "STRPOS({str},{subStr})")
    public static int indexOf(String str, String subStr)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOCATE({subStr},{str},{offset})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOCATE({subStr},{str},{offset})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "INSTR({str},{subStr},{offset})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CHARINDEX({subStr},{str},{offset})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(INSTR(SUBSTR({str},{offset} + 1),{subStr}) + {offset})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(STRPOS(SUBSTR({str},{offset} + 1),{subStr}) + {offset})")
    public static int indexOf(String str, String subStr, int offset)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOWER({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOWER({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOWER({str})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "LOWER({str})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "LOWER({str})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "LOWER({str})")
    public static String toLowerCase(String str)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UPPER({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UPPER({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "UPPER({str})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "UPPER({str})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "UPPER({str})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "UPPER({str})")
    public static String toUpperCase(String str)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LEFT({str},{length})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LEFT({str},{length})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SUBSTR({str},1,{length})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "LEFT({str},{length})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "SUBSTR({str},1,{length})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "LEFT({str},{length})")
    public static String left(String str, int length)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RIGHT({str},{length})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RIGHT({str},{length})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SUBSTR({str},LENGTH({str}) - ({length} - 1),{length})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "RIGHT({str},{length})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "SUBSTR({str},LENGTH({str}) - ({length} - 1),{length})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "RIGHT({str},{length})")
    public static String right(String str, int length)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LPAD({str},{length},{lpadStr})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LPAD({str},{length},{lpadStr})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LPAD({str},{length},{lpadStr})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "IIF({length} - LEN({str}) <= 0,{str},CONCAT(REPLICATE({lpadStr},{length} - LEN({str})),{str}))")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "IIF({length} - LENGTH({str}) <= 0,{str},(REPLICATE({lpadStr},{length} - LENGTH({str}))||{str}))")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "LPAD({str},{length},{lpadStr})")
    public static String leftPad(String str, int length, String lpadStr)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RPAD({str},{length},{rpadStr})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RPAD({str},{length},{rpadStr})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "RPAD({str},{length},{rpadStr})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "IIF({length} - LEN({str}) <= 0,{str},CONCAT({str},REPLICATE({rpadStr},{length} - LEN({str}))))")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "IIF({length} - LENGTH({str}) <= 0,{str},({str}||REPLICATE({rpadStr},{length} - LENGTH({str}))))")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "RPAD({str},{length},{rpadStr})")
    public static String rightPad(String str, int length, String rpadStr)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TRIM({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TRIM({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRIM({str})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "TRIM({str})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "TRIM({str})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "TRIM({str})")
    public static String trim(String str)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "LTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "LTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "LTRIM({str})")
    public static String trimStart(String str)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "RTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "RTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "RTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "RTRIM({str})")
    public static String trimEnd(String str)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "REPLACE({cur},{subs},{news})")
    public static String replace(String cur, String subs, String news)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "REVERSE({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "REVERSE({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "REVERSE({str})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "REVERSE({str})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "REVERSE({str})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "REVERSE({str})")
    public static String reverse(String str)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "STRCMP({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "STRCMP({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CASE WHEN {s1} < {s2} THEN -1 WHEN {s1} = {s2} THEN 0 ELSE 1 END)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "(CASE WHEN {s1} < {s2} THEN -1 WHEN {s1} = {s2} THEN 0 ELSE 1 END)")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "(CASE WHEN {s1} < {s2} THEN -1 WHEN {s1} = {s2} THEN 0 ELSE 1 END)")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(CASE WHEN {s1} < {s2} THEN -1 WHEN {s1} = {s2} THEN 0 ELSE 1 END)")
    public static int compare(String s1, String s2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBSTR({str},{beginIndex})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBSTR({str},{beginIndex})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SUBSTR({str},{beginIndex})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "SUBSTRING({str},{beginIndex},LEN({str}) - ({beginIndex} - 1))")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "SUBSTR({str},{beginIndex})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "SUBSTR({str},{beginIndex})")
    public static String subString(String str, int beginIndex)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBSTR({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBSTR({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SUBSTR({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "SUBSTRING({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "SUBSTR({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "SUBSTR({str},{beginIndex},{endIndex})")
    public static String subString(String str, int beginIndex, int endIndex)
    {
        boom();
        return "";
    }

    // endregion

    // region [控制流程]

    @SqlExtensionExpression(function = "CASE {when} END")
    public static <R> R Case(When<R> when)
    {
        boom();
        return (R) new Object();
    }

    @SafeVarargs
    @SqlExtensionExpression(function = "CASE {when} {rs} END")
    public static <R> R Case(When<R> when, When<R>... rs)
    {
        boom();
        return (R) new Object();
    }

    @SqlExtensionExpression(function = "CASE {when} ELSE {elsePart} END")
    public static <R> R Case(R elsePart, When<R> when)
    {
        boom();
        return (R) new Object();
    }

    @SafeVarargs
    @SqlExtensionExpression(function = "CASE {when} {rs} ELSE {elsePart} END", separator = " ")
    public static <R> R Case(R elsePart, When<R> when, When<R>... rs)
    {
        boom();
        return (R) new Object();
    }

    @SqlExtensionExpression(function = "WHEN {condition} THEN {then}")
    public static <R> When<R> when(boolean condition, R then)
    {
        boom();
        return new When<>();
    }


    @SqlExtensionExpression(dbType = DbType.H2, function = "IF({condition},{truePart},{falsePart})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "IF({condition},{truePart},{falsePart})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CASE WHEN {condition} THEN {truePart} ELSE {falsePart} END)")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "IIF({condition},{truePart},{falsePart})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "IIF({condition},{truePart},{falsePart})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "(CASE WHEN {condition} THEN {truePart} ELSE {falsePart} END)")
    public static <T> T If(boolean condition, T truePart, T falsePart)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "IFNULL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "IFNULL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NVL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "ISNULL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "IFNULL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "COALESCE({valueNotNull},{valueIsNull})")
    public static <T> T ifNull(T valueNotNull, T valueIsNull)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "NULLIF({t1},{t2})")
    public static <T> T nullIf(T t1, T t2)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleCastExtension.class)
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "CAST({value} AS {targetType})")
    public static <T> T cast(Object value, Class<T> targetType)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.SQLite, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.PostgreSQL, function = "CAST({value} AS {targetType})")
    public static <T> T cast(Object value, SqlTypes<T> targetType)
    {
        boom();
        return (T) new Object();
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "CAST({value} AS {targetType})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONVERT({targetType},{value})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST({value} AS {targetType})")
//    @SqlExtensionExpression(dbType = DbType.SQLServer, function = "CONVERT({targetType},{value})")
//    public static <T> T convert(Object value, Class<T> targetType)
//    {
//        boom();
//        return (T) new Object();
//    }

    @SqlExtensionExpression(function = "{t} IS NULL")
    public static <T> boolean isNull(T t)
    {
        boom();
        return false;
    }

    @SqlExtensionExpression(function = "{t} IS NOT NULL")
    public static <T> boolean isNotNull(T t)
    {
        boom();
        return false;
    }

    // endregion

    private static void boom()
    {
        if (win) // if win we win always
        {
            throw new SqlFunctionInvokeException();
        }
    }

    private static final boolean win = true;

    private static class Num extends Number
    {
        private Num()
        {
        }

        @Override
        public int intValue()
        {
            return 0;
        }

        @Override
        public long longValue()
        {
            return 0;
        }

        @Override
        public float floatValue()
        {
            return 0;
        }

        @Override
        public double doubleValue()
        {
            return 0;
        }
    }

    public static class When<R>
    {
    }
}
