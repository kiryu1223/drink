package io.github.kiryu1223.drink.ext;


import io.github.kiryu1223.drink.annotation.SqlFuncExt;
import io.github.kiryu1223.drink.exception.SqlFunctionInvokeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;

public class SqlFunctions
{

    // region [聚合函数]

    @SqlFuncExt(dbType = DbType.H2, function = "COUNT(*)")
    @SqlFuncExt(dbType = DbType.MySQL, function = "COUNT(*)")
    public static long count()
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "COUNT({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "COUNT({})")
    public static <T> long count(T t)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUM({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUM({})")
    public static <T> T sum(T t)
    {
        boom();
        return (T) new Object();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "AVG({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "AVG({})")
    public static <T extends Number> T avg(T t)
    {
        boom();
        return (T) new Num();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MIN({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MIN({})")
    public static <T> T min(T t)
    {
        boom();
        return (T) new Object();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MAX({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MAX({})")
    public static <T> T max(T t)
    {
        boom();
        return (T) new Object();
    }

    // endregion

    // region [时间]

    @SqlFuncExt(dbType = DbType.H2, function = "NOW()")
    @SqlFuncExt(dbType = DbType.MySQL, function = "NOW()")
    public static LocalDateTime now()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "UTC_TIMESTAMP()")
    @SqlFuncExt(dbType = DbType.MySQL, function = "UTC_TIMESTAMP()")
    public static LocalDateTime utcNow()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LOCALTIME()")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LOCALTIME()")
    public static LocalDateTime localNow()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SYSDATE()")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SYSDATE()")
    public static LocalDateTime sysNow()
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "CURDATE()")
    @SqlFuncExt(dbType = DbType.MySQL, function = "CURDATE()")
    public static LocalDate nowDate()
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "CURTIME()")
    @SqlFuncExt(dbType = DbType.MySQL, function = "CURTIME()")
    public static LocalTime nowTime()
    {
        boom();
        return LocalTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "UTC_DATE()")
    @SqlFuncExt(dbType = DbType.MySQL, function = "UTC_DATE()")
    public static LocalDate utcNowDate()
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "UTC_TIME()")
    @SqlFuncExt(dbType = DbType.MySQL, function = "UTC_TIME()")
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

    @SqlFuncExt(dbType = DbType.H2, function = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ADDDATE({time},INTERVAL {num} {unit})")
    public static LocalDateTime addDate(LocalDateTime time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ADDDATE({time},INTERVAL {num} {unit})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ADDDATE({time},INTERVAL {num} {unit})")
    public static LocalDate addDate(LocalDate time, SqlTimeUnit unit, int num)
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ADDDATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ADDDATE({},{})")
    public static LocalDateTime addDate(LocalDateTime time, int days)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ADDDATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ADDDATE({},{})")
    public static LocalDate addDate(LocalDate time, int days)
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ADDDATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ADDDATE({},{})")
    public static LocalDateTime addDate(LocalDateTime time, LocalDateTime addtime)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ADDDATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ADDDATE({},{})")
    public static LocalDate addDate(LocalDate time, LocalDate addtime)
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ADDTIME({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ADDTIME({},{})")
    public static LocalDateTime addTime(LocalDateTime time, LocalTime addtime)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ADDTIME({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ADDTIME({},{})")
    public static LocalDateTime addTime(LocalDateTime time, String addtime)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ADDTIME({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ADDTIME({},{})")
    public static LocalTime addTime(LocalTime time, LocalTime addtime)
    {
        boom();
        return LocalTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ADDTIME({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ADDTIME({},{})")
    public static LocalDateTime addTime(String time, LocalTime addtime)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ADDTIME({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ADDTIME({},{})")
    public static LocalDateTime addTime(String time, String addtime)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATE({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATE({})")
    public static LocalDate getDate(LocalDateTime time)
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATE({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATE({})")
    public static LocalDate getDate(String time)
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int daysDiff(LocalDateTime t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int daysDiff(LocalDateTime t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int daysDiff(LocalDateTime t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int daysDiff(LocalDate t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int daysDiff(LocalDate t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int daysDiff(LocalDate t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int daysDiff(String t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int hoursDiff(LocalDateTime t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int hoursDiff(LocalDateTime t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int hoursDiff(LocalDateTime t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int hoursDiff(LocalDate t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int hoursDiff(LocalDate t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int hoursDiff(LocalDate t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int hoursDiff(String t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int minutesDiff(LocalDateTime t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int minutesDiff(LocalDateTime t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int minutesDiff(LocalDateTime t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int minutesDiff(LocalDate t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int minutesDiff(LocalDate t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int minutesDiff(LocalDate t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int minutesDiff(String t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int secondsDiff(LocalDateTime t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int secondsDiff(LocalDateTime t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int secondsDiff(LocalDateTime t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int secondsDiff(LocalDate t1, LocalDate t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int secondsDiff(LocalDate t1, LocalDateTime t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int secondsDiff(LocalDate t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATEDIFF({},{})")
    public static int secondsDiff(String t1, String t2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATE_FORMAT({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATE_FORMAT({},{})")
    public static String dateFormat(LocalDateTime time, String format)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATE_FORMAT({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATE_FORMAT({},{})")
    public static String dateFormat(LocalDate time, String format)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DATE_FORMAT({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DATE_FORMAT({},{})")
    public static String dateFormat(String time, String format)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAY({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAY({})")
    public static int getDay(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAY({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAY({})")
    public static int getDay(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAY({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAY({})")
    public static int getDay(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAYNAME({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAYNAME({})")
    public static String getDayName(LocalDateTime time)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAYNAME({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAYNAME({})")
    public static String getDayName(LocalDate time)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAYNAME({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAYNAME({})")
    public static String getDayName(String time)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAYOFWEEK({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAYOFWEEK({})")
    public static int getDayOfWeek(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAYOFWEEK({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAYOFWEEK({})")
    public static int getDayOfWeek(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAYOFWEEK({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAYOFWEEK({})")
    public static int getDayOfWeek(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAYOFYEAR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAYOFYEAR({})")
    public static int getDayOfYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAYOFYEAR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAYOFYEAR({})")
    public static int getDayOfYear(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DAYOFYEAR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DAYOFYEAR({})")
    public static int getDayOfYear(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SEC_TO_TIME({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SEC_TO_TIME({})")
    public static LocalTime toTime(int second)
    {
        boom();
        return LocalTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "STR_TO_DATE({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "STR_TO_DATE({})")
    public static LocalDate toDate(String time)
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "STR_TO_DATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "STR_TO_DATE({},{})")
    public static LocalDate toDate(String time, String format)
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "STR_TO_DATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "STR_TO_DATE({},{})")
    public static LocalDateTime toDateTime(String time, String format)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "FROM_DAYS({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "FROM_DAYS({})")
    public static LocalDate toDate(long days)
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TO_DAYS({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TO_DAYS({})")
    public static long toDays(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TO_DAYS({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TO_DAYS({})")
    public static long toDays(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TO_DAYS({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TO_DAYS({})")
    public static long toDays(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIME_TO_SEC({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIME_TO_SEC({})")
    public static int toSecond(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIME_TO_SEC({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIME_TO_SEC({})")
    public static int toSecond(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIME_TO_SEC({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIME_TO_SEC({})")
    public static int toSecond(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "HOUR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "HOUR({})")
    public static int getHour(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "HOUR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "HOUR({})")
    public static int getHour(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "HOUR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "HOUR({})")
    public static int getHour(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LAST_DAY({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LAST_DAY({})")
    public static int getLastDay(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LAST_DAY({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LAST_DAY({})")
    public static int getLastDay(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LAST_DAY({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LAST_DAY({})")
    public static int getLastDay(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MAKEDATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MAKEDATE({},{})")
    public static LocalDate createDate(int year, int days)
    {
        throw new SqlFunctionInvokeException();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MAKETIME({},{},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MAKETIME({},{},{})")
    public static LocalTime createTime(int hour, int minute, int second)
    {
        throw new SqlFunctionInvokeException();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MICROSECOND({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MICROSECOND({})")
    public static int getMicroSecond(LocalTime time)
    {
        throw new SqlFunctionInvokeException();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MICROSECOND({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MICROSECOND({})")
    public static int getMicroSecond(LocalDateTime time)
    {
        throw new SqlFunctionInvokeException();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MICROSECOND({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MICROSECOND({})")
    public static int getMicroSecond(String time)
    {
        throw new SqlFunctionInvokeException();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MINUTE({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MINUTE({})")
    public static int getMinute(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MINUTE({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MINUTE({})")
    public static int getMinute(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MINUTE({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MINUTE({})")
    public static int getMinute(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MONTH({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MONTH({})")
    public static int getMonth(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MONTH({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MONTH({})")
    public static int getMonth(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MONTH({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MONTH({})")
    public static int getMonth(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MONTHNAME({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MONTHNAME({})")
    public static String getMonthName(LocalDate time)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MONTHNAME({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MONTHNAME({})")
    public static String getMonthName(LocalDateTime time)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MONTHNAME({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MONTHNAME({})")
    public static String getMonthName(String time)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "QUARTER({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "QUARTER({})")
    public static int getQuarter(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "QUARTER({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "QUARTER({})")
    public static int getQuarter(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "QUARTER({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "QUARTER({})")
    public static int getQuarter(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SECOND({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SECOND({})")
    public static int getSecond(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SECOND({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SECOND({})")
    public static int getSecond(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SECOND({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SECOND({})")
    public static int getSecond(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBDATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBDATE({},{})")
    public static <TimeLong extends TemporalAmount> LocalDateTime subDate(LocalDateTime time, TimeLong timeLong)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBDATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBDATE({},{})")
    public static <TimeLong extends TemporalAmount> LocalDate subDate(LocalDate time, TimeLong timeLong)
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBDATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBDATE({},{})")
    public static LocalDateTime subDate(LocalDateTime time, int days)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBDATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBDATE({},{})")
    public static LocalDate subDate(LocalDate time, int days)
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBDATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBDATE({},{})")
    public static LocalDateTime subDate(LocalDateTime time, LocalDateTime addtime)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBDATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBDATE({},{})")
    public static LocalDate subDate(LocalDate time, LocalDate addtime)
    {
        boom();
        return LocalDate.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBTIME({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBTIME({},{})")
    public static LocalDateTime subTime(LocalDateTime time, LocalTime addtime)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBTIME({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBTIME({},{})")
    public static LocalDateTime subTime(LocalDateTime time, String addtime)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBTIME({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBTIME({},{})")
    public static LocalTime subTime(LocalTime time, LocalTime addtime)
    {
        boom();
        return LocalTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBTIME({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBTIME({},{})")
    public static LocalDateTime subTime(String time, LocalTime addtime)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBTIME({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBTIME({},{})")
    public static LocalDateTime subTime(String time, String addtime)
    {
        boom();
        return LocalDateTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIME({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIME({})")
    public static int getTime(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIME({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIME({})")
    public static int getTime(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIME({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIME({})")
    public static int getTime(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIME_FORMAT({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIME_FORMAT({},{})")
    public static String timeFormat(LocalTime time, String format)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIME_FORMAT({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIME_FORMAT({},{})")
    public static String timeFormat(LocalDateTime time, String format)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIME_FORMAT({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIME_FORMAT({},{})")
    public static String timeFormat(String time, String format)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIMEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
    public static LocalTime timeDiff(LocalDateTime t1, LocalDateTime t2)
    {
        boom();
        return LocalTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIMEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
    public static LocalTime timeDiff(LocalDateTime t1, LocalTime t2)
    {
        boom();
        return LocalTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIMEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
    public static LocalTime timeDiff(LocalDateTime t1, String t2)
    {
        boom();
        return LocalTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIMEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
    public static LocalTime timeDiff(LocalTime t1, LocalTime t2)
    {
        boom();
        return LocalTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIMEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
    public static LocalTime timeDiff(LocalTime t1, LocalDateTime t2)
    {
        boom();
        return LocalTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIMEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
    public static LocalTime timeDiff(LocalTime t1, String t2)
    {
        boom();
        return LocalTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TIMEDIFF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TIMEDIFF({},{})")
    public static LocalTime timeDiff(String t1, String t2)
    {
        boom();
        return LocalTime.now();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEK({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEK({})")
    public static int getWeek(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEK({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEK({},{})")
    public static int getWeek(LocalDate time, int firstDayofweek)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEK({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEK({})")
    public static int getWeek(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEK({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEK({},{})")
    public static int getWeek(LocalDateTime time, int firstDayofweek)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEK({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEK({})")
    public static int getWeek(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEK({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEK({},{})")
    public static int getWeek(String time, int firstDayofweek)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEKDAY({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEKDAY({})")
    public static int getWeekDay(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEKDAY({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEKDAY({})")
    public static int getWeekDay(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEKDAY({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEKDAY({})")
    public static int getWeekDay(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEKOFYEAR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEKOFYEAR({})")
    public static int getWeekOfYear(LocalDate time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEKOFYEAR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEKOFYEAR({})")
    public static int getWeekOfYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "WEEKOFYEAR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "WEEKOFYEAR({})")
    public static int getWeekOfYear(String time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "YEAR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "YEAR({})")
    @SqlFuncExt(dbType = DbType.SqlServer, function = "DATEPART(YEAR,{})")
    public static int getYear(LocalDateTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "YEAR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "YEAR({})")
    @SqlFuncExt(dbType = DbType.SqlServer, function = "DATEPART(YEAR,{})")
    public static int getYear(LocalTime time)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "YEAR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "YEAR({})")
    @SqlFuncExt(dbType = DbType.SqlServer, function = "DATEPART(YEAR,{})")
    public static int getYear(String time)
    {
        boom();
        return 0;
    }

    // endregion

    // region [数值]

    @SqlFuncExt(dbType = DbType.H2, function = "ABS({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ABS({})")
    public static <T extends Number> T abs(T a)
    {
        boom();
        return (T) new Num();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "COS({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "COS({})")
    public static <T extends Number> double cos(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SIN({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SIN({})")
    public static <T extends Number> double sin(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TAN({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TAN({})")
    public static <T extends Number> double tan(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ACOS({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ACOS({})")
    public static <T extends Number> double acos(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ASIN({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ASIN({})")
    public static <T extends Number> double asin(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ATAN({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ATAN({})")
    public static <T extends Number> double atan(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ATAN2({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ATAN2({},{})")
    public static <T extends Number> double atan2(T a, T b)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "CEIL({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "CEIL({})")
    public static <T extends Number> long ceil(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "COT({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "COT({})")
    public static <T extends Number> double cot(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "DEGREES({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "DEGREES({})")
    public static <T extends Number> double degrees(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "EXP({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "EXP({})")
    public static <T extends Number> double exp(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "FLOOR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "FLOOR({})")
    public static <T extends Number> long floor(T a)
    {
        boom();
        return 0;
    }

    @SafeVarargs
    @SqlFuncExt(dbType = DbType.H2, function = "GREATEST({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "GREATEST({},{})")
    public static <T extends Number> T big(T a, T b, T... cs)
    {
        boom();
        return (T) new Num();
    }

    @SafeVarargs
    @SqlFuncExt(dbType = DbType.H2, function = "LEAST({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LEAST({},{})")
    public static <T extends Number> T small(T a, T b, T... cs)
    {
        boom();
        return (T) new Num();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LN({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LN({})")
    public static <T extends Number> double ln(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LOG({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LOG({})")
    public static <T extends Number> double log(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LOG2({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LOG2({})")
    public static <T extends Number> double log2(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LOG10({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LOG10({})")
    public static <T extends Number> double log10(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "MOD({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "MOD({},{})")
    public static <T extends Number> T mod(T a, T b)
    {
        boom();
        return (T) new Num();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "PI()")
    @SqlFuncExt(dbType = DbType.MySQL, function = "PI()")
    public static double pi()
    {
        boom();
        return 3.14159;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "POW({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "POW({},{})")
    public static <T extends Number> double pow(T a, T b)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "RADIANS({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "RADIANS({})")
    public static <T extends Number> double radians(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "RAND()")
    @SqlFuncExt(dbType = DbType.MySQL, function = "RAND()")
    public static double random()
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "RAND({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "RAND({})")
    public static double random(int a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ROUND({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ROUND({})")
    public static <T extends Number> T round(T a)
    {
        boom();
        return (T) new Num();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "ROUND({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ROUND({},{})")
    public static <T extends Number> T round(T a, int b)
    {
        boom();
        return (T) new Num();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SIGN({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SIGN({})")
    public static <T extends Number> int sign(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SQRT({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SQRT({})")
    public static <T extends Number> double sqrt(T a)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TRUNCATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TRUNCATE({},{})")
    public static <T extends Number> long truncate(T a, int b)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TRUNCATE({},0)")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TRUNCATE({},0)")
    public static <T extends Number> long truncate(T a)
    {
        boom();
        return 0;
    }

    // endregion

    // region [字符串]

    @SqlFuncExt(dbType = DbType.H2, function = "ASCII({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "ASCII({})")
    public static int ascii(String string)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "CHAR_LENGTH({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "CHAR_LENGTH({})")
    public static int length(String string)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "CONCAT({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "CONCAT({},{})")
    public static String concat(String s1, String s2, String... ss)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "CONCAT_WS({},{},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "CONCAT_WS({},{},{})")
    public static String join(String separator, String s1, String s2, String... ss)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "CHAR({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "CHAR({})")
    public static <T extends Number> String toStr(T t)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "FORMAT({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "FORMAT({},{})")
    public static <T extends Number> String numberFormat(T t, String format)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "HEX({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "HEX({})")
    public static <T extends Number> String hex(T t)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "HEX({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "HEX({})")
    public static String hex(String string)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "INSERT({},{},{},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "INSERT({},{},{},{})")
    public static String insert(String str, int pos, int length, String newStr)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "INSTR({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "INSTR({},{})")
    public static int instr(String s1, String s2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LOWER({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LOWER({})")
    public static String toLower(String string)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LEFT({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LEFT({},{})")
    public static String left(String string, int length)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LENGTH({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LENGTH({})")
    public static int byteLength(String string)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LPAD({},{},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LPAD({},{},{})")
    public static String leftPad(String string, int length, String lpadString)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LTRIM({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LTRIM({})")
    public static String trimStart(String string)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LOCATE({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LOCATE({},{})")
    public static int locate(String subString, String string)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "LOCATE({},{},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "LOCATE({},{},{})")
    public static int locate(String subString, String string, int offset)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "REPEAT({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "REPEAT({},{})")
    public static String repeat(String string, int number)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "REPLACE({},{},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "REPLACE({},{},{})")
    public static String replace(String cur, String subs, String news)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "REVERSE({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "REVERSE({})")
    public static String reverse(String string)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "RIGHT({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "RIGHT({},{})")
    public static String right(String string, int length)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "RPAD({},{},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "RPAD({},{},{})")
    public static String rightPad(String string, int length, String rpadString)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "RTRIM({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "RTRIM({})")
    public static String trimEnd(String string)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SPACE({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SPACE({})")
    public static String space(int number)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "STRCMP({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "STRCMP({},{})")
    public static int compare(String s1, String s2)
    {
        boom();
        return 0;
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBSTR({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBSTR({},{})")
    public static String subString(String string, int pos)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBSTR({},{},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBSTR({},{},{})")
    public static String subString(String string, int pos, int length)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "SUBSTRING_INDEX({},{},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "SUBSTRING_INDEX({},{},{})")
    public static String subStringIndex(String string, String delimiter, int length)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "TRIM({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "TRIM({})")
    public static String trim(String string)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "UPPER({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "UPPER({})")
    public static String toUpper(String string)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "UNHEX({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "UNHEX({})")
    public static byte[] unHex(String string)
    {
        boom();
        return new byte[]{};
    }

    @SqlFuncExt(dbType = DbType.H2, function = "GROUP_CONCAT({})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "GROUP_CONCAT({})")
    public static String groupJoin(String property)
    {
        boom();
        return "";
    }

    @SqlFuncExt(dbType = DbType.H2, function = "GROUP_CONCAT({property} SEPARATOR {delimiter})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "GROUP_CONCAT({property} SEPARATOR {delimiter})")
    public static <T> String groupJoin(String delimiter, T property)
    {
        boom();
        return "";
    }

    @SafeVarargs
    @SqlFuncExt(dbType = DbType.H2, function = "GROUP_CONCAT({properties} SEPARATOR {delimiter})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "GROUP_CONCAT({properties} SEPARATOR {delimiter})")
    public static <T> String groupJoin(String delimiter, T... properties)
    {
        boom();
        return "";
    }

    // endregion

    // region [控制流程]

    @SqlFuncExt(dbType = DbType.H2, function = "IF({},{},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "IF({},{},{})")
    public static <T> T If(boolean condition, T truePart, T falsePart)
    {
        boom();
        return (T) new Object();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "IFNULL({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "IFNULL({},{})")
    public static <T> T ifNull(T ifNotNull, T ifNull)
    {
        boom();
        return (T) new Object();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "NULLIF({},{})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "NULLIF({},{})")
    public static <T> T nullIf(T ifNotEq, T t2)
    {
        boom();
        return (T) new Object();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "CAST({} AS {})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "CAST({} AS {})")
    public static <T> T cast(Object value, Class<T> targetType)
    {
        boom();
        return (T) new Object();
    }

    @SqlFuncExt(dbType = DbType.H2, function = "CAST({} AS {})")
    @SqlFuncExt(dbType = DbType.MySQL, function = "CONVERT({targetType},{value})")
    public static <T> T convert(Object value, Class<T> targetType)
    {
        boom();
        return (T) new Object();
    }

    @SqlFuncExt(function = "{} IS NULL")
    public static <T> boolean isNull(T t)
    {
        boom();
        return false;
    }

    @SqlFuncExt(function = "{} IS NOT NULL")
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
        private Num() {}

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
