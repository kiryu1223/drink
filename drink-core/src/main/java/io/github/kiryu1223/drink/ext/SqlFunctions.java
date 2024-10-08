package io.github.kiryu1223.drink.ext;


import io.github.kiryu1223.drink.annotation.SqlExtensionExpression;
import io.github.kiryu1223.drink.exception.SqlFunctionInvokeException;
import io.github.kiryu1223.drink.ext.mysql.MySqlDateTimeDiffExtension;
import io.github.kiryu1223.drink.ext.oracle.OracleAddOrSubDateExtension;
import io.github.kiryu1223.drink.ext.oracle.OracleCastExtension;
import io.github.kiryu1223.drink.ext.oracle.OracleDateTimeDiffExtension;
import io.github.kiryu1223.drink.ext.oracle.OracleJoinExtension;
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
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "STRING_AGG({property},'')")
    public static String groupJoin(String property)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "GROUP_CONCAT({property} SEPARATOR {delimiter})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GROUP_CONCAT({property} SEPARATOR {delimiter})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LISTAGG({property},{delimiter}) WITHIN GROUP (ORDER BY {property})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "STRING_AGG({property},{delimiter})")
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
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "GETDATE()")
    public static LocalDateTime now()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "NOW({precision})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "NOW({precision})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST(CURRENT_TIMESTAMP AS TIMESTAMP)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "GETDATE()")
    public static LocalDateTime now(int precision)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UTC_TIMESTAMP()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UTC_TIMESTAMP()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST(SYS_EXTRACT_UTC(CURRENT_TIMESTAMP) AS DATE)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "GETUTCDATE()")
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
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "SYSDATETIME()")
    public static LocalDateTime systemNow()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CURDATE()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CURDATE()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CURRENT_DATE")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CAST(GETDATE() AS DATE)")
    public static LocalDate nowDate()
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CURTIME()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CURTIME()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CURRENT_DATE")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CAST(GETDATE() AS TIME)")
    public static LocalTime nowTime()
    {
        boom();
        return LocalTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UTC_DATE()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UTC_DATE()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST(SYS_EXTRACT_UTC(CURRENT_TIMESTAMP) AS DATE)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CAST(GETUTCDATE() AS DATE)")
    public static LocalDate utcNowDate()
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UTC_TIME()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UTC_TIME()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST(SYS_EXTRACT_UTC(CURRENT_TIMESTAMP) AS DATE)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CAST(GETUTCDATE() AS TIME)")
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
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEADD({unit},{num},{time})")
    public static LocalDateTime addDate(LocalDateTime time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEADD({unit},{num},{time})")
    public static LocalDate addDate(LocalDate time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEADD(DAY,{days},{time})")
    public static LocalDateTime addDate(LocalDateTime time, int days)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEADD(DAY,{days},{time})")
    public static LocalDate addDate(LocalDate time, int days)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEADD({unit},-({num}),{time})")
    public static LocalDateTime subDate(LocalDateTime time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - INTERVAL '{num}' {unit})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEADD({unit},-({num}),{time})")
    public static LocalDate subDate(LocalDate time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEADD(DAY,-({days}),{time})")
    public static LocalDateTime subDate(LocalDateTime time, int days)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleAddOrSubDateExtension.class)
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEADD(DAY,-({days}),{time})")
    public static LocalDate subDate(LocalDate time, int days)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDateTime t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDateTime t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDateTime t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDate t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEDIFF({unit},{t1},{t2})")
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDate t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    public static long dateTimeDiff(SqlTimeUnit unit, LocalDate t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    public static long dateTimeDiff(SqlTimeUnit unit, String t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    public static long dateTimeDiff(SqlTimeUnit unit, String t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "", extension = MySqlDateTimeDiffExtension.class)
    @SqlExtensionExpression(dbType = DbType.Oracle, extension = OracleDateTimeDiffExtension.class, function = "")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEDIFF_BIG({unit},{t1},{t2})")
    public static long dateTimeDiff(SqlTimeUnit unit, String t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({} + {})")
//    public static LocalDateTime addDate(LocalDateTime time, LocalDateTime addtime)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({} + {})")
//    public static LocalDate addDate(LocalDate time, LocalDate addtime)
//    {
//        boom();
//        return LocalDate.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({} + {})")
//    public static LocalDateTime addTime(LocalDateTime time, LocalTime addtime)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({} + {})")
//    public static LocalDateTime addTime(LocalDateTime time, String addtime)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({} + {})")
//    public static LocalTime addTime(LocalTime time, LocalTime addtime)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({} + {})")
//    public static LocalDateTime addTime(String time, LocalTime addtime)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({} + {})")
//    public static LocalDateTime addTime(String time, String addtime)
//    {
//        boom();
//        return LocalDateTime.now();
//    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATE({})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATE({})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRUNC({})")
//    public static LocalDate getDate(LocalDateTime time)
//    {
//        boom();
//        return LocalDate.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATE({})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATE({})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRUNC({})")
//    public static LocalDate getDate(String time)
//    {
//        boom();
//        return LocalDate.now();
//    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NUMTODSINTERVAL(({} - {}), 'DAY')")
//    public static int daysDiff(LocalDateTime t1, LocalDateTime t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NUMTODSINTERVAL(({} - {}), 'DAY')")
//    public static int daysDiff(LocalDateTime t1, LocalDate t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NUMTODSINTERVAL(({} - {}), 'DAY')")
//    public static int daysDiff(LocalDateTime t1, String t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NUMTODSINTERVAL(({} - {}), 'DAY')")
//    public static int daysDiff(LocalDate t1, LocalDate t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NUMTODSINTERVAL(({} - {}), 'DAY')")
//    public static int daysDiff(LocalDate t1, LocalDateTime t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NUMTODSINTERVAL(({} - {}), 'DAY')")
//    public static int daysDiff(LocalDate t1, String t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NUMTODSINTERVAL(({} - {}), 'DAY')")
//    public static int daysDiff(String t1, String t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int hoursDiff(LocalDateTime t1, LocalDateTime t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int hoursDiff(LocalDateTime t1, LocalDate t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int hoursDiff(LocalDateTime t1, String t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int hoursDiff(LocalDate t1, LocalDate t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int hoursDiff(LocalDate t1, LocalDateTime t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int hoursDiff(LocalDate t1, String t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int hoursDiff(String t1, String t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int minutesDiff(LocalDateTime t1, LocalDateTime t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int minutesDiff(LocalDateTime t1, LocalDate t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int minutesDiff(LocalDateTime t1, String t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int minutesDiff(LocalDate t1, LocalDate t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int minutesDiff(LocalDate t1, LocalDateTime t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int minutesDiff(LocalDate t1, String t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int minutesDiff(String t1, String t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int secondsDiff(LocalDateTime t1, LocalDateTime t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int secondsDiff(LocalDateTime t1, LocalDate t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int secondsDiff(LocalDateTime t1, String t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int secondsDiff(LocalDate t1, LocalDate t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int secondsDiff(LocalDate t1, LocalDateTime t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int secondsDiff(LocalDate t1, String t2)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "DATEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
//    public static int secondsDiff(String t1, String t2)
//    {
//        boom();
//        return 0;
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},{format})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "FORMAT({time},{format})")
    public static String dateFormat(LocalDateTime time, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},{format})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "FORMAT({time},{format})")
    public static String dateFormat(LocalDate time, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATE_FORMAT({time},{format})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},{format})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "FORMAT({time},{format})")
    public static String dateFormat(String time, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(DAY FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(DAY,{time})")
    public static int getDay(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(DAY FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(DAY,{time})")
    public static int getDay(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(DAY FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(DAY,{time})")
    public static int getDay(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'DAY')")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATENAME(WEEKDAY,{time})")
    public static String getDayName(LocalDateTime time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'DAY')")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATENAME(WEEKDAY,{time})")
    public static String getDayName(LocalDate time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'DAY')")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATENAME(WEEKDAY,{time})")
    public static String getDayName(String time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'D'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "(DATEPART(WEEKDAY,{time}))")
    public static int getDayOfWeek(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'D'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "(DATEPART(WEEKDAY,{time}))")
    public static int getDayOfWeek(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFWEEK({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'D'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(WEEKDAY,{time})")
    public static int getDayOfWeek(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'DDD'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(DAYOFYEAR,{time})")
    public static int getDayOfYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'DDD'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(DAYOFYEAR,{time})")
    public static int getDayOfYear(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'DDD'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(DAYOFYEAR,{time})")
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
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CONVERT(DATE,{time},23)")
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
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "(DATEDIFF(DAY,'0001-01-01',{time}) + 366)")
    public static int dateToDays(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRUNC({time} - (TO_DATE('0001-01-01', 'YYYY-MM-DD') - INTERVAL '1' YEAR) - 2)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "(DATEDIFF(DAY,'0001-01-01',{time}) + 366)")
    public static int dateToDays(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TO_DAYS({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(DAY FROM (TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff') - (TO_TIMESTAMP('0001-01-01', 'YYYY-MM-DD') - INTERVAL '1' YEAR) - INTERVAL '2' DAY))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "(DATEDIFF(DAY,'0001-01-01',{time}) + 366)")
    public static int dateToDays(String time)
    {
        boom();
        return 0;
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "((EXTRACT(HOUR FROM {time}) * 3600) + (EXTRACT(MINUTE FROM {time}) * 60) + EXTRACT(SECOND FROM {time}))")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(HOUR,{time}) * 3600 + DATEPART(MINUTE,{time}) * 60 + DATEPART(SECOND,{time})")
//    public static int toSecond(LocalDateTime time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "((EXTRACT(HOUR FROM {time}) * 3600) + (EXTRACT(MINUTE FROM {time}) * 60) + EXTRACT(SECOND FROM {time}))")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(HOUR,{time}) * 3600 + DATEPART(MINUTE,{time}) * 60 + DATEPART(SECOND,{time})")
//    public static int toSecond(LocalTime time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_TO_SEC({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "((EXTRACT(HOUR FROM TO_DATE({time})) * 3600) + (EXTRACT(MINUTE FROM TO_DATE({time})) * 60) + EXTRACT(SECOND FROM TO_DATE({time})))")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(HOUR,{time}) * 3600 + DATEPART(MINUTE,{time}) * 60 + DATEPART(SECOND,{time})")
//    public static int toSecond(String time)
//    {
//        boom();
//        return 0;
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(HOUR FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(HOUR,{time})")
    public static int getHour(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(HOUR FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(HOUR,{time})")
    public static int getHour(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HOUR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(HOUR FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(HOUR,{time})")
    public static int getHour(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "EOMONTH({time})")
    public static LocalDate getLastDay(LocalDateTime time)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "EOMONTH({time})")
    public static LocalDate getLastDay(LocalDate time)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LAST_DAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LAST_DAY(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "EOMONTH({time})")
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
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MINUTE,{time})")
    public static int getMinute(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MINUTE({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MINUTE({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MINUTE FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MINUTE,{time})")
    public static int getMinute(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MINUTE({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MINUTE({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MINUTE FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MINUTE,{time})")
    public static int getMinute(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MONTH FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MINUTE,{time})")
    public static int getMonth(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MONTH FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MONTH,{time})")
    public static int getMonth(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTH({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MONTH FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MONTH,{time})")
    public static int getMonth(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'FMMONTH')")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "FORMAT({time}, 'MMMM')")
    public static String getMonthName(LocalDate time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'FMMONTH')")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "FORMAT({time}, 'MMMM')")
    public static String getMonthName(LocalDateTime time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTHNAME({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'FMMONTH')")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "FORMAT(CONVERT(DATE,{time}), 'MMMM')")
    public static String getMonthName(String time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL(EXTRACT(MONTH FROM {time}) / 3)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(QUARTER,{time})")
    public static int getQuarter(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL(EXTRACT(MONTH FROM {time}) / 3)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(QUARTER,{time})")
    public static int getQuarter(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "QUARTER({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL(EXTRACT(MONTH FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff')) / 3)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(QUARTER,{time})")
    public static int getQuarter(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(SECOND FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(SECOND,{time})")
    public static int getSecond(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(SECOND FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(SECOND,{time})")
    public static int getSecond(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SECOND({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(SECOND FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(SECOND,{time})")
    public static int getSecond(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(EXTRACT(SECOND FROM {time}) * 1000)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MS,{time})")
    public static int getMilliSecond(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(EXTRACT(SECOND FROM {time}) * 1000)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MS,{time})")
    public static int getMilliSecond(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "(MICROSECOND({time}) / 1000)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(EXTRACT(SECOND FROM {time}) * 1000)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MS,{time})")
    public static int getMilliSecond(String time)
    {
        boom();
        return 0;
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "MICROSECOND({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MICROSECOND({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MICROSECOND FROM {time})")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MCS,{time})")
//    public static int getMicroSecond(LocalTime time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "MICROSECOND({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MICROSECOND({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MICROSECOND FROM {time})")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MCS,{time})")
//    public static int getMicroSecond(LocalDateTime time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "MICROSECOND({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MICROSECOND({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MICROSECOND FROM TO_DATE({time}))")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(MCS,{time})")
//    public static int getMicroSecond(String time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(NS,{time})")
//    public static int getNanoSecond(LocalTime time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(NS,{time})")
//    public static int getNanoSecond(LocalDateTime time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(NS,{time})")
//    public static int getNanoSecond(String time)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - {subtime})")
//    public static LocalDateTime subDate(LocalDateTime time, LocalDateTime subtime)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - {subtime})")
//    public static LocalDate subDate(LocalDate time, LocalDate subtime)
//    {
//        boom();
//        return LocalDate.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - {subtime})")
//    public static LocalDateTime subTime(LocalDateTime time, LocalTime subtime)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - {subtime})")
//    public static LocalDateTime subTime(LocalDateTime time, String subtime)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - {subtime})")
//    public static LocalTime subTime(LocalTime time, LocalTime subtime)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - {subtime})")
//    public static LocalDateTime subTime(String time, LocalTime subtime)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBTIME({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - {subtime})")
//    public static LocalDateTime subTime(String time, String subtime)
//    {
//        boom();
//        return LocalDateTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE(TO_CHAR({time},'HH24:MI:SS'))")
//    public static LocalTime getTime(LocalDateTime time)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME({time})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME({time})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE(TO_CHAR({time},'HH24:MI:SS'))")
//    public static LocalTime getTime(LocalTime time)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME({})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME({})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE(TO_CHAR(TO_DATE({time}),'HH24:MI:SS'))")
//    public static LocalTime getTime(String time)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_FORMAT({time},{format})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_FORMAT({time},{format})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time}, {format})")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "FORMAT({time}, {format})")
//    public static String timeFormat(LocalTime time, String format)
//    {
//        boom();
//        return "";
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_FORMAT({time},{format})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_FORMAT({time},{format})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time}, {format})")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "FORMAT({time}, {format})")
//    public static String timeFormat(LocalDateTime time, String format)
//    {
//        boom();
//        return "";
//    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({t1} - {t2})")
//    public static LocalTime timeDiff(LocalDateTime t1, LocalDateTime t2)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({t1} - {t2})")
//    public static LocalTime timeDiff(LocalDateTime t1, LocalTime t2)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({t1} - {t2})")
//    public static LocalTime timeDiff(LocalDateTime t1, String t2)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({t1} - {t2})")
//    public static LocalTime timeDiff(LocalTime t1, LocalTime t2)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({t1} - {t2})")
//    public static LocalTime timeDiff(LocalTime t1, LocalDateTime t2)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({t1} - {t2})")
//    public static LocalTime timeDiff(LocalTime t1, String t2)
//    {
//        boom();
//        return LocalTime.now();
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({t1} - {t2})")
//    public static LocalTime timeDiff(String t1, String t2)
//    {
//        boom();
//        return LocalTime.now();
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(WEEK,{time})")
    public static int getWeek(LocalDate time)
    {
        boom();
        return 0;
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({time},{firstDayofweek})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({time},{firstDayofweek})")
//    public static int getWeek(LocalDate time, int firstDayofweek)
//    {
//        boom();
//        return 0;
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(WEEK,{time})")
    public static int getWeek(LocalDateTime time)
    {
        boom();
        return 0;
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({time},{firstDayofweek})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({time},{firstDayofweek})")
//    public static int getWeek(LocalDateTime time, int firstDayofweek)
//    {
//        boom();
//        return 0;
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({time},1)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'IW'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(WEEK,{time})")
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
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "(CASE DATEPART(WEEKDAY,{time}) WHEN 1 THEN 6 ELSE DATEPART(WEEKDAY,{time}) - 2 END)")
    public static int getWeekDay(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CASE TO_NUMBER(TO_CHAR({time},'D')) WHEN 1 THEN 6 ELSE TO_NUMBER(TO_CHAR({time},'D')) - 2 END)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "(CASE DATEPART(WEEKDAY,{time}) WHEN 1 THEN 6 ELSE DATEPART(WEEKDAY,{time}) - 2 END)")
    public static int getWeekDay(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKDAY({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CASE TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'D')) WHEN 1 THEN 6 ELSE TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'D')) - 2 END)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "(CASE DATEPART(WEEKDAY,{time}) WHEN 1 THEN 6 ELSE DATEPART(WEEKDAY,{time}) - 2 END)")
    public static int getWeekDay(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(ISO_WEEK,{time})")
    public static int getWeekOfYear(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({time},'IW'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(ISO_WEEK,{time})")
    public static int getWeekOfYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKOFYEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR(TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'),'IW'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(ISO_WEEK,{time})")
    public static int getWeekOfYear(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(YEAR FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(YEAR,{time})")
    public static int getYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(YEAR FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(YEAR,{time})")
    public static int getYear(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "YEAR({time})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(YEAR FROM TO_TIMESTAMP({time},'YYYY-MM-DD hh24:mi:ss:ff'))")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(YEAR,{time})")
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
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "ABS({a})")
    public static <T extends Number> T abs(T a)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "COS({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "COS({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "COS({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "COS({a})")
    public static <T extends Number> double cos(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SIN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SIN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SIN({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "SIN({a})")
    public static <T extends Number> double sin(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TAN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TAN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TAN({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "TAN({a})")
    public static <T extends Number> double tan(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ACOS({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ACOS({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ACOS({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "ACOS({a})")
    public static <T extends Number> double acos(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ASIN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ASIN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ASIN({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "ASIN({a})")
    public static <T extends Number> double asin(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ATAN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ATAN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ATAN({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "ATAN({a})")
    public static <T extends Number> double atan(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "ATN2({a},{b})")
    public static <T extends Number> double atan2(T a, T b)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CEIL({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CEIL({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CEILING({a})")
    public static <T extends Number> int ceil(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "COT({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "COT({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CASE SIN({a}) WHEN 0 THEN 0 ELSE COS({a}) / SIN({a}) END)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "COT({a})")
    public static <T extends Number> double cot(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DEGREES({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DEGREES({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({a} * 180 / " + Math.PI + ")")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DEGREES({a})")
    public static <T extends Number> double degrees(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "EXP({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "EXP({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXP({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "EXP({a})")
    public static <T extends Number> double exp(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "FLOOR({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "FLOOR({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "FLOOR({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "FLOOR({a})")
    public static <T extends Number> int floor(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "GREATEST({a},{b})")
    public static <T extends Number> T big(T a, T b)
    {
        boom();
        return (T) new Num();
    }

    @SafeVarargs
    @SqlExtensionExpression(dbType = DbType.H2, function = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "GREATEST({a},{b},{cs})")
    public static <T extends Number> T big(T a, T b, T... cs)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "LEAST({a},{b})")
    public static <T extends Number> T small(T a, T b)
    {
        boom();
        return (T) new Num();
    }

    @SafeVarargs
    @SqlExtensionExpression(dbType = DbType.H2, function = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "LEAST({a},{b},{cs})")
    public static <T extends Number> T small(T a, T b, T... cs)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LN({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "LOG({a})")
    public static <T extends Number> double ln(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOG({base},{a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOG({base},{a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOG({base},{a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "LOG({a},{base})")
    public static <T extends Number, B extends Number> double log(T a, B base)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOG2({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOG2({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOG(2,{a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "LOG({a},2)")
    public static <T extends Number> double log2(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOG10({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOG10({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOG(10,{a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "LOG({a},10)")
    public static <T extends Number> double log10(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "({a} % {b})")
    public static <T extends Number> T mod(T a, T b)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "PI()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "PI()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(" + Math.PI + ")")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "PI()")
    public static double pi()
    {
        boom();
        return 3.14159;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "POWER({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "POWER({a},{b})")
    public static <T extends Number> double pow(T a, T b)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RADIANS({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RADIANS({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({a} * " + Math.PI + " / 180)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "RADIANS({a})")
    public static <T extends Number> double radians(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RAND()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RAND()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "DBMS_RANDOM.VALUE")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "RAND()")
    public static double random()
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RAND({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RAND({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "DBMS_RANDOM.VALUE")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "RAND({a})")
    public static double random(int a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ROUND({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ROUND({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ROUND({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "ROUND({a},0)")
    public static <T extends Number> int round(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "ROUND({a},{b})")
    public static <T extends Number> T round(T a, int b)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SIGN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SIGN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SIGN({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "SIGN({a})")
    public static <T extends Number> int sign(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SQRT({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SQRT({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SQRT({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "SQRT({a})")
    public static <T extends Number> double sqrt(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TRUNCATE({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TRUNCATE({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRUNC({a},{b})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "ROUND({a},{b},1)")
    public static <T extends Number> double truncate(T a, int b)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TRUNCATE({a},0)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TRUNCATE({a},0)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRUNC({a})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "ROUND({a},0,1)")
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
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "(DATALENGTH({str}) = 0)")
    public static boolean isEmpty(String str)
    {
        boom();
        return true;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ASCII({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ASCII({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ASCII({str})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "ASCII({str})")
    public static int strToAscii(String str)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CHAR_LENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CHAR_LENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NVL(LENGTH({str}),0)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "LEN({str})")
    public static int length(String str)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CONCAT({s1},{s2})")
    public static String concat(String s1, String s2)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT({s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT({s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({s1}||{s2}||{ss})", separator = "||")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CONCAT({s1},{s2},{ss})")
    public static String concat(String s1, String s2, String... ss)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT_WS({separator},{s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT_WS({separator},{s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({s1}||{separator}||{s2})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CONCAT_WS({separator},{s1},{s2})")
    public static String join(String separator, String s1, String s2)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT_WS({separator},{s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT_WS({separator},{s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleJoinExtension.class)
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CONCAT_WS({separator},{s1},{s2},{ss})")
    public static String join(String separator, String s1, String s2, String... ss)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CHAR({t})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CHAR({t})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CHR({t})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CHAR({t})")
    public static String asciiToStr(int t)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "FORMAT({t},{format})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "FORMAT({t},{format})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({t},{format})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "FORMAT({t},{format})")
    public static <T extends Number> String numberFormat(T t, String format)
    {
        boom();
        return "";
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "HEX({t})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HEX({t})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({t},'XXXXXXXXXXXXXXXX')")
//    public static <T extends Number> String hex(T t)
//    {
//        boom();
//        return "";
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "HEX({str})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HEX({str})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "RAWTOHEX({str})")
//    public static String hex(String str)
//    {
//        boom();
//        return "";
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "INSERT({str},{pos},{length},{newStr})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "INSERT({str},{pos},{length},{newStr})")
//    public static String insert(String str, int pos, int length, String newStr)
//    {
//        boom();
//        return "";
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "INSTR({str},{subStr})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "INSTR({str},{subStr})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "INSTR({str},{subStr})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CHARINDEX({subStr},{str})")
    public static int indexOf(String str, String subStr)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOCATE({subStr},{str},{offset})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOCATE({subStr},{str},{offset})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "INSTR({str},{subStr},{offset})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CHARINDEX({subStr},{str},{offset})")
    public static int indexOf(String str, String subStr, int offset)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOWER({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOWER({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOWER({str})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "LOWER({str})")
    public static String toLowerCase(String str)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LEFT({str},{length})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LEFT({str},{length})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SUBSTR({str},1,{length})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "LEFT({str},{length})")
    public static String left(String str, int length)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LENGTHB({str})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATALENGTH({str})")
    public static int byteLength(String str)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LPAD({str},{length},{lpadStr})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LPAD({str},{length},{lpadStr})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LPAD({str},{length},{lpadStr})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "IIF({length} - LEN({str}) <= 0,{str},CONCAT(REPLICATE({lpadStr},{length} - LEN({str})),{str}))")
    public static String leftPad(String str, int length, String lpadStr)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "LTRIM({str})")
    public static String trimStart(String str)
    {
        boom();
        return "";
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "LOCATE({subStr},{str})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOCATE({subStr},{str})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "INSTR({str},{subStr})")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CHARINDEX({subStr},{str})")
//    public static int locate(String subStr, String str)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "LOCATE({subStr},{str},{offset})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOCATE({subStr},{str},{offset})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "INSTR({str},{subStr},{offset})")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CHARINDEX({subStr},{str},{offset})")
//    public static int locate(String subStr, String str, int offset)
//    {
//        boom();
//        return 0;
//    }
//
//    @SqlExtensionExpression(dbType = DbType.H2, function = "REPEAT({},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "REPEAT({},{})")
//    public static String repeat(String string, int number)
//    {
//        boom();
//        return "";
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "REPLACE({cur},{subs},{news})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "REPLACE({cur},{subs},{news})")
    public static String replace(String cur, String subs, String news)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "REVERSE({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "REVERSE({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "REVERSE({str})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "REVERSE({str})")
    public static String reverse(String str)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RIGHT({str},{length})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RIGHT({str},{length})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SUBSTR({str},LENGTH({str}) - ({length} - 1),{length})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "RIGHT({str},{length})")
    public static String right(String str, int length)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RPAD({str},{length},{rpadStr})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RPAD({str},{length},{rpadStr})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "RPAD({str},{length},{rpadStr})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "IIF({length} - LEN({str}) <= 0,{str},CONCAT({str},REPLICATE({rpadStr},{length} - LEN({str}))))")
    public static String rightPad(String str, int length, String rpadStr)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "RTRIM({str})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "RTRIM({str})")
    public static String trimEnd(String str)
    {
        boom();
        return "";
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "SPACE({})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SPACE({})")
//    public static String space(int number)
//    {
//        boom();
//        return "";
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "STRCMP({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "STRCMP({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CASE WHEN {s1} < {s2} THEN -1 WHEN {s1} = {s2} THEN 0 ELSE 1 END)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "(CASE WHEN {s1} < {s2} THEN -1 WHEN {s1} = {s2} THEN 0 ELSE 1 END)")
    public static int compare(String s1, String s2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBSTR({str},{beginIndex})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBSTR({str},{beginIndex})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SUBSTR({str},{beginIndex})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "SUBSTRING({str},{beginIndex},LEN({str}) - ({beginIndex} - 1))")
    public static String subString(String str, int beginIndex)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBSTR({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBSTR({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SUBSTR({str},{beginIndex},{endIndex})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "SUBSTRING({str},{beginIndex},{endIndex})")
    public static String subString(String str, int beginIndex, int endIndex)
    {
        boom();
        return "";
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBSTRING_INDEX({},{},{})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBSTRING_INDEX({},{},{})")
//    public static String subStringIndex(String str, String delimiter, int length)
//    {
//        boom();
//        return "";
//    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TRIM({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TRIM({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRIM({str})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "TRIM({str})")
    public static String trim(String str)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UPPER({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UPPER({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "UPPER({str})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "UPPER({str})")
    public static String toUpperCase(String str)
    {
        boom();
        return "";
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "UNHEX({})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UNHEX({})")
//    public static byte[] unHex(String string)
//    {
//        boom();
//        return new byte[]{};
//    }
//    @SafeVarargs
//    @SqlExtensionExpression(dbType = DbType.H2, function = "GROUP_CONCAT({properties} SEPARATOR {delimiter})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GROUP_CONCAT({properties} SEPARATOR {delimiter})")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "STRING_AGG({properties},{delimiter})")
//    public static <T> String groupJoin(String delimiter, T... properties)
//    {
//        boom();
//        return "";
//    }

    // endregion

    // region [控制流程]

    @SqlExtensionExpression(dbType = DbType.H2, function = "IF({condition},{truePart},{falsePart})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "IF({condition},{truePart},{falsePart})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(CASE WHEN {condition} THEN {truePart} ELSE {falsePart} END)")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "IIF({condition},{truePart},{falsePart})")
    public static <T> T If(boolean condition, T truePart, T falsePart)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "IFNULL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "IFNULL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NVL({valueNotNull},{valueIsNull})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "ISNULL({valueNotNull},{valueIsNull})")
    public static <T> T ifNull(T valueNotNull, T valueIsNull)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "NULLIF({t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "NULLIF({t1},{t2})")
    public static <T> T nullIf(T t1, T t2)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "", extension = OracleCastExtension.class)
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CAST({value} AS {targetType})")
    public static <T> T cast(Object value, Class<T> targetType)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST({value} AS {targetType})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CAST({value} AS {targetType})")
    public static <T> T cast(Object value, SqlTypes<T> targetType)
    {
        boom();
        return (T) new Object();
    }

//    @SqlExtensionExpression(dbType = DbType.H2, function = "CAST({value} AS {targetType})")
//    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONVERT({targetType},{value})")
//    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST({value} AS {targetType})")
//    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "CONVERT({targetType},{value})")
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
}
