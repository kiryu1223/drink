package io.github.kiryu1223.drink.test.sqlite;

import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.ext.SqlFunctions;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static io.github.kiryu1223.drink.ext.SqlFunctions.*;

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
                    LocalDateTime systemNow = systemNow();
                })
                .first();

        log.info(res.toString());

        LocalDateTime now = client.queryEmptyTable().endSelect(() -> now()).first();
        log.info(now.toString());

        LocalDateTime utcNow = client.queryEmptyTable().endSelect(() -> utcNow()).first();
        log.info(utcNow.toString());

        LocalDate nowDate = client.queryEmptyTable().endSelect(() -> nowDate()).first();
        log.info(nowDate.toString());

        LocalTime nowTime = client.queryEmptyTable().endSelect(() -> nowTime()).first();
        log.info(nowTime.toString());

        LocalDate utcNowDate = client.queryEmptyTable().endSelect(() -> utcNowDate()).first();
        log.info(utcNowDate.toString());

        LocalTime utcNowTime = client.queryEmptyTable().endSelect(() -> utcNowTime()).first();
        log.info(utcNowTime.toString());

        LocalDateTime systemNow = client.queryEmptyTable().endSelect(() -> systemNow()).first();
        log.info(systemNow.toString());
    }

    @Test
    public void addDateTest()
    {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime nextDay = client.queryEmptyTable()
                .endSelect(() -> addDate(now, 1))
                .first();

        Assert.assertEquals(now.plusDays(1), nextDay);

        LocalDateTime nextHour = client.queryEmptyTable()
                .endSelect(() -> addDate(now, SqlTimeUnit.HOUR, 1))
                .first();

        Assert.assertEquals(now.plusHours(1), nextHour);
    }

    @Test
    public void subDateTest()
    {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime subDay = client.queryEmptyTable()
                .endSelect(() -> subDate(now, 1))
                .first();

        Assert.assertEquals(now.minusDays(1), subDay);

        LocalDateTime subHour = client.queryEmptyTable()
                .endSelect(() -> subDate(now, SqlTimeUnit.HOUR, 1))
                .first();

        Assert.assertEquals(now.minusHours(1), subHour);
    }

    @Test
    public void dateTimeDiffTest()
    {
        long one = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.DAY, "1996-10-27", "2000-01-01"))
                .first();

        Assert.assertEquals(1161, one);


        long one1 = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.DAY, "1996-10-27", LocalDate.of(2020, 5, 5)))
                .first();

        Assert.assertEquals(8591, one1);

        LocalDateTime start = LocalDateTime.of(1996, 10, 27, 0, 0);
        LocalDateTime end = LocalDateTime.of(2000, 1, 1, 0, 0);
        long second = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.SECOND, start, end))
                .first();

        Assert.assertEquals(100310400, second);
    }

    @Test
    public void dateTimeDiff2Test()
    {
        LocalDateTime brithDay = LocalDateTime.of(1996, 10, 27, 7, 30, 45);
        LocalDateTime future = LocalDateTime.of(1997, 3, 7, 16, 30, 15);

        long yearDiff = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.YEAR, brithDay, future))
                .first();
        log.info(String.valueOf(yearDiff));
        long monthDiff = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.MONTH, brithDay, future))
                .first();
        log.info(String.valueOf(monthDiff));
        long weekDiff = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.WEEK, brithDay, future))
                .first();
        log.info(String.valueOf(weekDiff));
        long dayDiff = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.DAY, brithDay, future))
                .first();
        log.info(String.valueOf(dayDiff));
        long hourDiff = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.HOUR, brithDay, future))
                .first();
        log.info(String.valueOf(hourDiff));
        long minuteDiff = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.MINUTE, brithDay, future))
                .first();
        log.info(String.valueOf(minuteDiff));
        long secondDiff = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.SECOND, brithDay, future))
                .first();
        log.info(String.valueOf(secondDiff));
        long milliDiff = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.MILLISECOND, brithDay, future))
                .first();
        log.info(String.valueOf(milliDiff));
    }

    @Test
    public void getYearTest()
    {
        int year = client.queryEmptyTable()
                .endSelect(() -> getYear("2020-10-27"))
                .first();

        Assert.assertEquals(2020, year);
    }

    @Test
    public void getMonthTest()
    {
        int month = client.queryEmptyTable()
                .endSelect(() -> getMonth("2020-10-27"))
                .first();

        Assert.assertEquals(10, month);
    }

    @Test
    public void getDayTest()
    {
        int strDay = client.queryEmptyTable()
                .endSelect(() -> getDay("2020-10-27"))
                .first();

        Assert.assertEquals(27, strDay);

        int timeDay = client.queryEmptyTable()
                .endSelect(() -> getDay(LocalDate.of(2020, 10, 27)))
                .first();

        Assert.assertEquals(27, timeDay);
    }

    @Test
    public void getDayNameTest()
    {
        String dayName = client.queryEmptyTable()
                .endSelect(() -> getDayName("2020-10-27"))
                .first();

        log.info(dayName);
    }

    @Test
    public void getMonthNameTest()
    {
        String mouthName = client.queryEmptyTable()
                .endSelect(() -> getMonthName("2020-10-27"))
                .first();

        log.info(mouthName);
    }

    @Test
    public void getDayOfWeekTest()
    {
        int dayOfWeek = client.queryEmptyTable()
                .endSelect(() -> getDayOfWeek("2020-10-27"))
                .first();

        Assert.assertEquals(3, dayOfWeek);
    }

    @Test
    public void getDayOfYearTest()
    {
        int dayOfYear = client.queryEmptyTable()
                .endSelect(() -> getDayOfYear("2020-10-27"))
                .first();

        Assert.assertEquals(301, dayOfYear);
    }

    @Test
    public void toDaysTest()
    {
        int days = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.dateToDays("2020-10-27"))
                .first();

        Assert.assertEquals(738090, days);
    }

    @Test
    public void getHourTest()
    {
        int hour = client.queryEmptyTable()
                .endSelect(() -> getHour("2020-10-27 16:30:21"))
                .first();

        Assert.assertEquals(16, hour);
    }

    @Test
    public void getMinuteTest()
    {
        int minute = client.queryEmptyTable()
                .endSelect(() -> getMinute("2020-10-27 16:30:21"))
                .first();

        Assert.assertEquals(30, minute);
    }

    @Test
    public void getSecondTest()
    {
        int second = client.queryEmptyTable()
                .endSelect(() -> getSecond("2020-10-27 16:30:21"))
                .first();

        Assert.assertEquals(21, second);
    }

    @Test
    public void getMilliSecondTest()
    {
        int MilliSecond = client.queryEmptyTable()
                .endSelect(() -> getMilliSecond(now(3)))
                .first();

        log.info(String.valueOf(MilliSecond));
    }

//    @Test
//    public void getMicroSecondTest()
//    {
//        int microSecond = client.queryEmptyTable()
//                .endSelect(() -> getMicroSecond(now()))
//                .first();
//
//        log.info(String.valueOf(microSecond));
//    }

    @Test
    public void getLastDayTest()
    {
        LocalDate lastDay = client.queryEmptyTable()
                .endSelect(() -> getLastDay("2020-10-27"))
                .first();

        Assert.assertEquals(LocalDate.of(2020, 10, 31), lastDay);
    }

    @Test
    public void getQuarterTest()
    {
        int quarter = client.queryEmptyTable()
                .endSelect(() -> getQuarter("2020-10-27"))
                .first();

        Assert.assertEquals(4, quarter);
    }

    @Test
    public void getWeekTest()
    {
        int week = client.queryEmptyTable()
                .endSelect(() -> getWeek("2020-10-27"))
                .first();

        Assert.assertEquals(44, week);
    }

    @Test
    public void getWeekDayTest()
    {
        int weekDay = client.queryEmptyTable()
                .endSelect(() -> getWeekDay("2020-10-27"))
                .first();

        Assert.assertEquals(1, weekDay);
    }

    @Test
    public void getWeekOfYearTest()
    {
        int weekOfYear = client.queryEmptyTable()
                .endSelect(() -> getWeekOfYear("2020-10-27"))
                .first();

        Assert.assertEquals(44, weekOfYear);
    }
}
