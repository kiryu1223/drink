package io.github.kiryu1223.drink.ext;


import io.github.kiryu1223.drink.annotation.SqlExtensionExpression;
import io.github.kiryu1223.drink.exception.SqlFunctionInvokeException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SqlFunctions
{

    // region [聚合函数]

    @SqlExtensionExpression(dbType = DbType.H2, function = "COUNT(*)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "COUNT(*)")
    public static long count()
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "COUNT({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "COUNT({})")
    public static <T> long count(T t)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUM({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUM({})")
    public static <T> T sum(T t)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "AVG({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "AVG({})")
    public static <T extends Number> BigDecimal avg(T t)
    {
        boom();
        return BigDecimal.ZERO;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MIN({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MIN({})")
    public static <T> T min(T t)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MAX({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MAX({})")
    public static <T> T max(T t)
    {
        boom();
        return (T) new Object();
    }

    // endregion

    // region [时间]

    @SqlExtensionExpression(dbType = DbType.H2, function = "NOW()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "NOW()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CURRENT_TIMESTAMP")
    public static LocalDateTime now()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UTC_TIMESTAMP()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UTC_TIMESTAMP()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(SYSTIMESTAMP AT TIME ZONE 'UTC')")
    public static LocalDateTime utcNow()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOCALTIME()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOCALTIME()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOCALTIMESTAMP")
    public static LocalDateTime localNow()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SYSDATE()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SYSDATE()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SYSDATE")
    public static LocalDateTime systemNow()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CURDATE()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CURDATE()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CURRENT_DATE")
    public static LocalDate nowDate()
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CURTIME()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CURTIME()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE(TO_CHAR(SYSDATE,'HH24:MI:SS'))")
    public static LocalTime nowTime()
    {
        boom();
        return LocalTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UTC_DATE()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UTC_DATE()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CAST(FROM_TZ(CAST(SYSTIMESTAMP AS TIMESTAMP),'UTC') AS DATE)")
    public static LocalDate utcNowDate()
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UTC_TIME()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UTC_TIME()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR(SYSTIMESTAMP AT TIME ZONE 'UTC','HH24:MI:SS')")
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
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} + INTERVAL {num} {unit})")
    public static LocalDateTime addDate(LocalDateTime time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} + INTERVAL {num} {unit})")
    public static LocalDate addDate(LocalDate time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({} + {})")
    public static LocalDateTime addDate(LocalDateTime time, int days)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ADDDATE({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ADDDATE({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({} + {})")
    public static LocalDate addDate(LocalDate time, int days)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - INTERVAL {num} {unit})")
    public static LocalDateTime subDate(LocalDateTime time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},INTERVAL {num} {unit})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - INTERVAL {num} {unit})")
    public static LocalDate subDate(LocalDate time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - {days})")
    public static LocalDateTime subDate(LocalDateTime time, int days)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBDATE({time},{days})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "({time} - {days})")
    public static LocalDate subDate(LocalDate time, int days)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT({unit} FROM ({t1} - {t2}))")
    public static int dateTimeDiff(SqlTimeUnit unit, LocalDateTime t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT({unit} FROM ({t1} - {t2}))")
    public static int dateTimeDiff(SqlTimeUnit unit, LocalDateTime t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT({unit} FROM ({t1} - {t2}))")
    public static int dateTimeDiff(SqlTimeUnit unit, LocalDateTime t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT({unit} FROM ({t1} - {t2}))")
    public static int dateTimeDiff(SqlTimeUnit unit, LocalDate t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT({unit} FROM ({t1} - {t2}))")
    public static int dateTimeDiff(SqlTimeUnit unit, LocalDate t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT({unit} FROM ({t1} - {t2}))")
    public static int dateTimeDiff(SqlTimeUnit unit, LocalDate t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIMESTAMPDIFF({unit},{t1},{t2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT({unit} FROM ({t1} - {t2}))")
    public static int dateTimeDiff(SqlTimeUnit unit, String t1, String t2)
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

    @SqlExtensionExpression(dbType = DbType.H2, function = "DATE_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATE_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({},{})")
    public static String dateFormat(LocalDateTime time, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DATE_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATE_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({},{})")
    public static String dateFormat(LocalDate time, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DATE_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DATE_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({},{})")
    public static String dateFormat(String time, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAY({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAY({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(DAY FROM {})")
    public static int getDay(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAY({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAY({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(DAY FROM {})")
    public static int getDay(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAY({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAY({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(DAY FROM {})")
    public static int getDay(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYNAME({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYNAME({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({},'DAY')")
    public static String getDayName(LocalDateTime time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYNAME({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYNAME({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({},'DAY')")
    public static String getDayName(LocalDate time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYNAME({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYNAME({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({},'DAY')")
    public static String getDayName(String time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFWEEK({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFWEEK({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({},'D'))")
    public static int getDayOfWeek(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFWEEK({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFWEEK({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({},'D'))")
    public static int getDayOfWeek(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFWEEK({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFWEEK({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({},'D'))")
    public static int getDayOfWeek(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({}, 'DDD'))")
    public static int getDayOfYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({}, 'DDD'))")
    public static int getDayOfYear(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DAYOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DAYOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_NUMBER(TO_CHAR({}, 'DDD'))")
    public static int getDayOfYear(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SEC_TO_TIME({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SEC_TO_TIME({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE(TO_CHAR(FLOOR({second} / 3600), 'FM00')||':'||TO_CHAR(FLOOR(MOD({second}, 3600) / 60), 'FM00')||':'||TO_CHAR(MOD({second}, 60), 'FM00'))")
    public static LocalTime toTime(int second)
    {
        boom();
        return LocalTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "STR_TO_DATE({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "STR_TO_DATE({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE({time})")
    public static LocalDate toDate(String time)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "STR_TO_DATE({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "STR_TO_DATE({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE({time},{format})")
    public static LocalDate toDate(String time, String format)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "STR_TO_DATE({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "STR_TO_DATE({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE({time},{format})")
    public static LocalDateTime toDateTime(String time, String format)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "FROM_DAYS({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "FROM_DAYS({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(TO_DATE('1970-01-01', 'YYYY-MM-DD') + ({days} - 719163))")
    public static LocalDate toDate(long days)
    {
        boom();
        return LocalDate.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TO_DAYS({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TO_DAYS({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "FLOOR(({time} - TO_DATE('1970-01-01', 'YYYY-MM-DD')))")
    public static long toDays(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TO_DAYS({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TO_DAYS({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "FLOOR(({time} - TO_DATE('1970-01-01', 'YYYY-MM-DD')))")
    public static long toDays(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TO_DAYS({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TO_DAYS({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "FLOOR(({time} - TO_DATE('1970-01-01', 'YYYY-MM-DD')))")
    public static long toDays(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_TO_SEC({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_TO_SEC({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "((EXTRACT(HOUR FROM {time}) * 3600) + (EXTRACT(MINUTE FROM {time}) * 60) + EXTRACT(SECOND FROM {time}))")
    public static int toSecond(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_TO_SEC({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_TO_SEC({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "((EXTRACT(HOUR FROM {time}) * 3600) + (EXTRACT(MINUTE FROM {time}) * 60) + EXTRACT(SECOND FROM {time}))")
    public static int toSecond(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_TO_SEC({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_TO_SEC({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "((EXTRACT(HOUR FROM {time}) * 3600) + (EXTRACT(MINUTE FROM {time}) * 60) + EXTRACT(SECOND FROM {time}))")
    public static int toSecond(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "HOUR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HOUR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(HOUR FROM {})")
    public static int getHour(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "HOUR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HOUR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(HOUR FROM {})")
    public static int getHour(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "HOUR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HOUR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(HOUR FROM {})")
    public static int getHour(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LAST_DAY({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LAST_DAY({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LAST_DAY({})")
    public static int getLastDay(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LAST_DAY({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LAST_DAY({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LAST_DAY({})")
    public static int getLastDay(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LAST_DAY({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LAST_DAY({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LAST_DAY({})")
    public static int getLastDay(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MAKEDATE({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MAKEDATE({},{})")
    public static LocalDate createDate(int year, int days)
    {
        throw new SqlFunctionInvokeException();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MAKETIME({},{},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MAKETIME({},{},{})")
    public static LocalTime createTime(int hour, int minute, int second)
    {
        throw new SqlFunctionInvokeException();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MICROSECOND({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MICROSECOND({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MICROSECOND FROM {})")
    public static int getMicroSecond(LocalTime time)
    {
        throw new SqlFunctionInvokeException();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MICROSECOND({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MICROSECOND({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MICROSECOND FROM {})")
    public static int getMicroSecond(LocalDateTime time)
    {
        throw new SqlFunctionInvokeException();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MICROSECOND({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MICROSECOND({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MICROSECOND FROM {})")
    public static int getMicroSecond(String time)
    {
        throw new SqlFunctionInvokeException();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MINUTE({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MINUTE({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MINUTE FROM {})")
    public static int getMinute(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MINUTE({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MINUTE({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MINUTE FROM {})")
    public static int getMinute(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MINUTE({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MINUTE({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MINUTE FROM {})")
    public static int getMinute(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTH({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTH({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MONTH FROM {})")
    public static int getMonth(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTH({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTH({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MONTH FROM {})")
    public static int getMonth(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTH({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTH({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(MONTH FROM {})")
    public static int getMonth(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTHNAME({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTHNAME({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'FMMONTH')")
    public static String getMonthName(LocalDate time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTHNAME({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTHNAME({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'FMMONTH')")
    public static String getMonthName(LocalDateTime time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MONTHNAME({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MONTHNAME({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'FMMONTH')")
    public static String getMonthName(String time)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "QUARTER({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "QUARTER({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL(EXTRACT(MONTH FROM {time}) / 3)")
    public static int getQuarter(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "QUARTER({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "QUARTER({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL(EXTRACT(MONTH FROM {time}) / 3)")
    public static int getQuarter(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "QUARTER({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "QUARTER({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL(EXTRACT(MONTH FROM {time}) / 3)")
    public static int getQuarter(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SECOND({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SECOND({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(SECOND FROM {time})")
    public static int getSecond(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SECOND({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SECOND({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(SECOND FROM {time})")
    public static int getSecond(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SECOND({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SECOND({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(SECOND FROM {time})")
    public static int getSecond(String time)
    {
        boom();
        return 0;
    }

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

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE(TO_CHAR({time},'HH24:MI:SS'))")
    public static LocalTime getTime(LocalDateTime time)
    {
        boom();
        return LocalTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE(TO_CHAR({time},'HH24:MI:SS'))")
    public static LocalTime getTime(LocalTime time)
    {
        boom();
        return LocalTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_DATE({time})")
    public static LocalTime getTime(String time)
    {
        boom();
        return LocalTime.now();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time}, {format})")
    public static String timeFormat(LocalTime time, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time}, {format})")
    public static String timeFormat(LocalDateTime time, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TIME_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TIME_FORMAT({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time}, {format})")
    public static String timeFormat(String time, String format)
    {
        boom();
        return "";
    }

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

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'IW')")
    public static int getWeek(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'IW')")
    public static int getWeek(LocalDate time, int firstDayofweek)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'IW')")
    public static int getWeek(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'IW')")
    public static int getWeek(LocalDateTime time, int firstDayofweek)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'IW')")
    public static int getWeek(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEK({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEK({},{})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'IW')")
    public static int getWeek(String time, int firstDayofweek)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKDAY({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKDAY({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(TO_CHAR({time},'D') - 1)")
    public static int getWeekDay(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKDAY({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKDAY({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(TO_CHAR({time},'D') - 1)")
    public static int getWeekDay(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKDAY({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKDAY({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "(TO_CHAR({time},'D') - 1)")
    public static int getWeekDay(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'IW')")
    public static int getWeekOfYear(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'IW')")
    public static int getWeekOfYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "WEEKOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "WEEKOFYEAR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({time},'IW')")
    public static int getWeekOfYear(String time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "YEAR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "YEAR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(YEAR FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(YEAR,{})")
    public static int getYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "YEAR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "YEAR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(YEAR FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(YEAR,{})")
    public static int getYear(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "YEAR({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "YEAR({})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXTRACT(YEAR FROM {time})")
    @SqlExtensionExpression(dbType = DbType.SqlServer, function = "DATEPART(YEAR,{})")
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
    public static <T extends Number> T abs(T a)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "COS({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "COS({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "COS({a})")
    public static <T extends Number> double cos(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SIN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SIN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SIN({a})")
    public static <T extends Number> double sin(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TAN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TAN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TAN({a})")
    public static <T extends Number> double tan(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ACOS({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ACOS({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ACOS({a})")
    public static <T extends Number> double acos(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ASIN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ASIN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ASIN({a})")
    public static <T extends Number> double asin(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ATAN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ATAN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ATAN({a})")
    public static <T extends Number> double atan(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ATAN2({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ATAN2({a},{b})")
    public static <T extends Number> double atan2(T a, T b)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CEIL({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CEIL({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CEIL({a})")
    public static <T extends Number> long ceil(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "COT({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "COT({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "COT({a})")
    public static <T extends Number> double cot(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "DEGREES({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "DEGREES({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "DEGREES({a})")
    public static <T extends Number> double degrees(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "EXP({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "EXP({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "EXP({a})")
    public static <T extends Number> double exp(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "FLOOR({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "FLOOR({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "FLOOR({a})")
    public static <T extends Number> T floor(T a)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GREATEST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "GREATEST({a},{b})")
    public static <T extends Number> T big(T a, T b)
    {
        boom();
        return (T) new Num();
    }

    @SafeVarargs
    @SqlExtensionExpression(dbType = DbType.H2, function = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GREATEST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "GREATEST({a},{b},{cs})")
    public static <T extends Number> T big(T a, T b, T... cs)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LEAST({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LEAST({a},{b})")
    public static <T extends Number> T small(T a, T b)
    {
        boom();
        return (T) new Num();
    }

    @SafeVarargs
    @SqlExtensionExpression(dbType = DbType.H2, function = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LEAST({a},{b},{cs})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LEAST({a},{b},{cs})")
    public static <T extends Number> T small(T a, T b, T... cs)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LN({a})")
    public static <T extends Number> double ln(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOG({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOG({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOG({a})")
    public static <T extends Number> double log(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOG2({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOG2({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOG2({a})")
    public static <T extends Number> double log2(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOG10({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOG10({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LOG10({a})")
    public static <T extends Number> double log10(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "MOD({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "MOD({a},{b})")
    public static <T extends Number> T mod(T a, T b)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "PI()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "PI()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "PI()")
    public static double pi()
    {
        boom();
        return 3.14159;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "POW({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "POW({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "POW({a},{b})")
    public static <T extends Number> double pow(T a, T b)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RADIANS({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RADIANS({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "RADIANS({a})")
    public static <T extends Number> double radians(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RAND()")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RAND()")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "RAND()")
    public static double random()
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RAND({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RAND({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "RAND({a})")
    public static double random(int a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ROUND({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ROUND({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ROUND({a})")
    public static <T extends Number> T round(T a)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ROUND({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ROUND({a},{b})")
    public static <T extends Number> T round(T a, int b)
    {
        boom();
        return (T) new Num();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SIGN({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SIGN({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SIGN({a})")
    public static <T extends Number> int sign(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SQRT({a})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SQRT({a})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "SQRT({a})")
    public static <T extends Number> double sqrt(T a)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TRUNCATE({a},{b})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TRUNCATE({a},{b})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRUNCATE({a},{b})")
    public static <T extends Number> long truncate(T a, int b)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TRUNCATE({a},0)")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TRUNCATE({a},0)")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TRUNCATE({a},0)")
    public static <T extends Number> long truncate(T a)
    {
        boom();
        return 0;
    }

    // endregion

    // region [字符串]

    @SqlExtensionExpression(dbType = DbType.H2, function = "ASCII({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "ASCII({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "ASCII({str})")
    public static int strToAscii(String str)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CHAR_LENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CHAR_LENGTH({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "LENGTH({str})")
    public static int length(String str)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CONCAT({s1},{s2})")
    public static String concat(String s1, String s2)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT({s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT({s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "{s1}||{s2}||{ss}", separator = "||")
    public static String concat(String s1, String s2, String... ss)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT_WS({separator},{s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT_WS({separator},{s1},{s2})")
    public static String join(String separator, String s1, String s2)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CONCAT_WS({separator},{s1},{s2},{ss})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONCAT_WS({separator},{s1},{s2},{ss})")
    public static String join(String separator, String s1, String s2, String... ss)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CHAR({t})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CHAR({t})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "CHR({t})")
    public static <T extends Number> String asciiToStr(T t)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "FORMAT({t},{format})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "FORMAT({t},{format})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({t},{format})")
    public static <T extends Number> String numberFormat(T t, String format)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "HEX({t})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HEX({t})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "TO_CHAR({t}, 'XXXXXXXXXXXXXXXX')")
    public static <T extends Number> String hex(T t)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "HEX({str})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "HEX({str})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "RAWTOHEX({str})")
    public static String hex(String str)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "INSERT({str},{pos},{length},{newStr})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "INSERT({str},{pos},{length},{newStr})")
    public static String insert(String str, int pos, int length, String newStr)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "INSTR({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "INSTR({s1},{s2})")
    @SqlExtensionExpression(dbType = DbType.Oracle, function = "INSTR({s1},{s2})")
    public static int instr(String s1, String s2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOWER({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOWER({})")
    public static String toLower(String string)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LEFT({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LEFT({},{})")
    public static String left(String string, int length)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LENGTH({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LENGTH({})")
    public static int byteLength(String string)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LPAD({},{},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LPAD({},{},{})")
    public static String leftPad(String string, int length, String lpadString)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LTRIM({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LTRIM({})")
    public static String trimStart(String string)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOCATE({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOCATE({},{})")
    public static int locate(String subString, String string)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "LOCATE({},{},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "LOCATE({},{},{})")
    public static int locate(String subString, String string, int offset)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "REPEAT({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "REPEAT({},{})")
    public static String repeat(String string, int number)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "REPLACE({},{},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "REPLACE({},{},{})")
    public static String replace(String cur, String subs, String news)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "REVERSE({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "REVERSE({})")
    public static String reverse(String string)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RIGHT({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RIGHT({},{})")
    public static String right(String string, int length)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RPAD({},{},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RPAD({},{},{})")
    public static String rightPad(String string, int length, String rpadString)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "RTRIM({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "RTRIM({})")
    public static String trimEnd(String string)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SPACE({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SPACE({})")
    public static String space(int number)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "STRCMP({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "STRCMP({},{})")
    public static int compare(String s1, String s2)
    {
        boom();
        return 0;
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBSTR({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBSTR({},{})")
    public static String subString(String string, int pos)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBSTR({},{},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBSTR({},{},{})")
    public static String subString(String string, int pos, int length)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "SUBSTRING_INDEX({},{},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "SUBSTRING_INDEX({},{},{})")
    public static String subStringIndex(String string, String delimiter, int length)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "TRIM({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "TRIM({})")
    public static String trim(String string)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UPPER({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UPPER({})")
    public static String toUpper(String string)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "UNHEX({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "UNHEX({})")
    public static byte[] unHex(String string)
    {
        boom();
        return new byte[]{};
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "GROUP_CONCAT({})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GROUP_CONCAT({})")
    public static String groupJoin(String property)
    {
        boom();
        return "";
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "GROUP_CONCAT({property} SEPARATOR {delimiter})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GROUP_CONCAT({property} SEPARATOR {delimiter})")
    public static <T> String groupJoin(String delimiter, T property)
    {
        boom();
        return "";
    }

    @SafeVarargs
    @SqlExtensionExpression(dbType = DbType.H2, function = "GROUP_CONCAT({properties} SEPARATOR {delimiter})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "GROUP_CONCAT({properties} SEPARATOR {delimiter})")
    public static <T> String groupJoin(String delimiter, T... properties)
    {
        boom();
        return "";
    }

    // endregion

    // region [控制流程]

    @SqlExtensionExpression(dbType = DbType.H2, function = "IF({},{},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "IF({},{},{})")
    public static <T> T If(boolean condition, T truePart, T falsePart)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "IFNULL({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "IFNULL({},{})")
    public static <T> T ifNull(T ifNotNull, T ifNull)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "NULLIF({},{})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "NULLIF({},{})")
    public static <T> T nullIf(T ifNotEq, T t2)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CAST({} AS {})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CAST({} AS {})")
    public static <T> T cast(Object value, Class<T> targetType)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(dbType = DbType.H2, function = "CAST({} AS {})")
    @SqlExtensionExpression(dbType = DbType.MySQL, function = "CONVERT({targetType},{value})")
    public static <T> T convert(Object value, Class<T> targetType)
    {
        boom();
        return (T) new Object();
    }

    @SqlExtensionExpression(function = "{} IS NULL")
    public static <T> boolean isNull(T t)
    {
        boom();
        return false;
    }

    @SqlExtensionExpression(function = "{} IS NOT NULL")
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
