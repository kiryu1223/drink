package io.github.kiryu1223.drink.test.mysql;

import io.github.kiryu1223.drink.core.api.Result;
import io.github.kiryu1223.drink.base.sqlExt.SqlTimeUnit;
import io.github.kiryu1223.drink.func.SqlFunctions;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static io.github.kiryu1223.drink.func.SqlFunctions.*;

public class DateTimeTest extends BaseTest
{
    private static final Logger log = LoggerFactory.getLogger(DateTimeTest.class);

    @Test
    public void nowTest()
    {
        Result res = client.queryEmptyTable()
                .select(() -> new Result()
                {
                    LocalDateTime now = now();
                    LocalDateTime utcNow = utcNow();
                    LocalDate nowDate = nowDate();
                    LocalTime nowTime = nowTime();
                    LocalDate utcNowDate = utcNowDate();
                    LocalTime utcNowTime = utcNowTime();
//                    LocalDateTime systemNow = systemNow();
                })
                .first();

        log.info(res.toString());

        LocalDateTime now = client.queryEmptyTable().select(() -> now()).first();
        log.info(now.toString());

        LocalDateTime utcNow = client.queryEmptyTable().select(() -> utcNow()).first();
        log.info(utcNow.toString());

        LocalDate nowDate = client.queryEmptyTable().select(() -> nowDate()).first();
        log.info(nowDate.toString());

        LocalTime nowTime = client.queryEmptyTable().select(() -> nowTime()).first();
        log.info(nowTime.toString());

        LocalDate utcNowDate = client.queryEmptyTable().select(() -> utcNowDate()).first();
        log.info(utcNowDate.toString());

        LocalTime utcNowTime = client.queryEmptyTable().select(() -> utcNowTime()).first();
        log.info(utcNowTime.toString());

//        LocalDateTime systemNow = client.queryEmptyTable().select(() -> systemNow()).first();
//        log.info(systemNow.toString());
    }

    @Test
    public void addDateTest()
    {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime nextDay = client.queryEmptyTable()
                .select(() -> addDate(now, 1))
                .first();

        Assert.assertEquals(now.plusDays(1), nextDay);

        LocalDateTime nextHour = client.queryEmptyTable()
                .select(() -> addDate(now, SqlTimeUnit.HOUR, 1))
                .first();

        Assert.assertEquals(now.plusHours(1), nextHour);
    }

    @Test
    public void subDateTest()
    {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime subDay = client.queryEmptyTable()
                .select(() -> subDate(now, 1))
                .first();

        Assert.assertEquals(now.minusDays(1), subDay);

        LocalDateTime subHour = client.queryEmptyTable()
                .select(() -> subDate(now, SqlTimeUnit.HOUR, 1))
                .first();

        Assert.assertEquals(now.minusHours(1), subHour);
    }

    @Test
    public void dateTimeDiffTest()
    {
        long one = client.queryEmptyTable()
                .select(() -> dateTimeDiff(SqlTimeUnit.DAY, "1996-10-27", "2000-01-01"))
                .first();

        Assert.assertEquals(1161, one);


        long one1 = client.queryEmptyTable()
                .select(() -> dateTimeDiff(SqlTimeUnit.DAY, "1996-10-27", LocalDate.of(2020, 5, 5)))
                .first();

        Assert.assertEquals(8591, one1);

        LocalDateTime start = LocalDateTime.of(1996, 10, 27, 0, 0);
        LocalDateTime end = LocalDateTime.of(2000, 1, 1, 0, 0);
        long second = client.queryEmptyTable()
                .select(() -> dateTimeDiff(SqlTimeUnit.SECOND, start, end))
                .first();

        Assert.assertEquals(100310400, second);
    }

    @Test
    public void dateTimeDiff2Test()
    {
        LocalDateTime brithDay = LocalDateTime.of(1996, 10, 27, 7, 30, 45);
        LocalDateTime future = LocalDateTime.of(2085, 3, 7, 16, 30, 15);

        long yearDiff = client.queryEmptyTable()
                .select(() -> dateTimeDiff(SqlTimeUnit.YEAR, brithDay, future))
                .first();
        log.info(String.valueOf(yearDiff));
        long monthDiff = client.queryEmptyTable()
                .select(() -> dateTimeDiff(SqlTimeUnit.MONTH, brithDay, future))
                .first();
        log.info(String.valueOf(monthDiff));
        long weekDiff = client.queryEmptyTable()
                .select(() -> dateTimeDiff(SqlTimeUnit.WEEK, brithDay, future))
                .first();
        log.info(String.valueOf(weekDiff));
        long dayDiff = client.queryEmptyTable()
                .select(() -> dateTimeDiff(SqlTimeUnit.DAY, brithDay, future))
                .first();
        log.info(String.valueOf(dayDiff));
        long hourDiff = client.queryEmptyTable()
                .select(() -> dateTimeDiff(SqlTimeUnit.HOUR, brithDay, future))
                .first();
        log.info(String.valueOf(hourDiff));
        long minuteDiff = client.queryEmptyTable()
                .select(() -> dateTimeDiff(SqlTimeUnit.MINUTE, brithDay, future))
                .first();
        log.info(String.valueOf(minuteDiff));
        long secondDiff = client.queryEmptyTable()
                .select(() -> dateTimeDiff(SqlTimeUnit.SECOND, brithDay, future))
                .first();
        log.info(String.valueOf(secondDiff));
        long milliDiff = client.queryEmptyTable()
                .select(() -> dateTimeDiff(SqlTimeUnit.MILLISECOND, brithDay, future))
                .first();
        log.info(String.valueOf(milliDiff));
    }

    @Test
    public void getYearTest()
    {
        int year = client.queryEmptyTable()
                .select(() -> getYear("2020-10-27"))
                .first();

        Assert.assertEquals(2020, year);
    }

    @Test
    public void getMonthTest()
    {
        int month = client.queryEmptyTable()
                .select(() -> getMonth("2020-10-27"))
                .first();

        Assert.assertEquals(10, month);
    }

    @Test
    public void getDayTest()
    {
        int strDay = client.queryEmptyTable()
                .select(() -> getDay("2020-10-27"))
                .first();

        Assert.assertEquals(27, strDay);

        int timeDay = client.queryEmptyTable()
                .select(() -> getDay(LocalDate.of(2020, 10, 27)))
                .first();

        Assert.assertEquals(27, timeDay);
    }

    @Test
    public void getDayNameTest()
    {
        String dayName = client.queryEmptyTable()
                .select(() -> getDayName("2020-10-27"))
                .first();

        log.info(dayName);
    }

    @Test
    public void getMonthNameTest()
    {
        String mouthName = client.queryEmptyTable()
                .select(() -> getMonthName("2020-10-27"))
                .first();

        log.info(mouthName);
    }

    @Test
    public void getDayOfWeekTest()
    {
        int dayOfWeek = client.queryEmptyTable()
                .select(() -> getDayOfWeek("2020-10-27"))
                .first();

        Assert.assertEquals(3, dayOfWeek);
    }

    @Test
    public void getDayOfYearTest()
    {
        int dayOfYear = client.queryEmptyTable()
                .select(() -> getDayOfYear("2020-10-27"))
                .first();

        Assert.assertEquals(301, dayOfYear);
    }

    @Test
    public void toDaysTest()
    {
        int days = client.queryEmptyTable()
                .select(() -> SqlFunctions.dateToDays("2020-10-27"))
                .first();

        Assert.assertEquals(738090, days);
    }

    @Test
    public void getHourTest()
    {
        int hour = client.queryEmptyTable()
                .select(() -> getHour("2020-10-27 16:30:21"))
                .first();

        Assert.assertEquals(16, hour);
    }

    @Test
    public void getMinuteTest()
    {
        int minute = client.queryEmptyTable()
                .select(() -> getMinute("2020-10-27 16:30:21"))
                .first();

        Assert.assertEquals(30, minute);
    }

    @Test
    public void getSecondTest()
    {
        int second = client.queryEmptyTable()
                .select(() -> getSecond("2020-10-27 16:30:21"))
                .first();

        Assert.assertEquals(21, second);
    }

    @Test
    public void getMilliSecondTest()
    {
        int MilliSecond = client.queryEmptyTable()
                .select(() -> getMilliSecond(now()))
                .first();

        log.info(String.valueOf(MilliSecond));
    }

//    @Test
//    public void getMicroSecondTest()
//    {
//        int microSecond = client.queryEmptyTable()
//                .select(() -> getMicroSecond(now()))
//                .first();
//
//        log.info(String.valueOf(microSecond));
//    }

    @Test
    public void getLastDayTest()
    {
        LocalDate lastDay = client.queryEmptyTable()
                .select(() -> getLastDay("2020-10-27"))
                .first();

        Assert.assertEquals(LocalDate.of(2020, 10, 31), lastDay);
    }

    @Test
    public void getQuarterTest()
    {
        int quarter = client.queryEmptyTable()
                .select(() -> getQuarter("2020-10-27"))
                .first();

        Assert.assertEquals(4, quarter);
    }

    @Test
    public void getWeekTest()
    {
        int week = client.queryEmptyTable()
                .select(() -> getWeek("2020-10-27"))
                .first();

        Assert.assertEquals(44, week);
    }

    @Test
    public void getWeekDayTest()
    {
        int weekDay = client.queryEmptyTable()
                .select(() -> getWeekDay("2020-10-27"))
                .first();

        Assert.assertEquals(1, weekDay);
    }

    @Test
    public void getWeekOfYearTest()
    {
        int weekOfYear = client.queryEmptyTable()
                .select(() -> getWeekOfYear("2020-10-27"))
                .first();

        Assert.assertEquals(44, weekOfYear);
    }
}
