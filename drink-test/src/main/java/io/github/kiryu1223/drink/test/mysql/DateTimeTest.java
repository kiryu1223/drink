package io.github.kiryu1223.drink.test.mysql;

import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.github.kiryu1223.drink.ext.SqlFunctions.*;

public class DateTimeTest extends BaseTest
{
    private static final Logger log = LoggerFactory.getLogger(DateTimeTest.class);

    @Test
    public void nowTest()
    {
        Result one = client.queryEmptyTable()
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

        log.info(one.toString());
    }

    @Test
    public void dateTimeDiffTest()
    {
        int one = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.DAY, "1996-10-27", "2000-01-01"))
                .first();

        Assert.assertEquals(1161, one);

        LocalDateTime now = LocalDateTime.now();

        Integer one1 = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.DAY, "1996-10-27", now))
                .first();

        log.info(one1.toString());
    }

    @Test
    public void secondDiffTest()
    {
        LocalDateTime start = LocalDateTime.of(1996, 10, 27, 0, 0);
        LocalDateTime end = LocalDateTime.of(2000, 1, 1, 0, 0);
        int one = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.SECOND, start, end))
                .first();

        Assert.assertEquals(100310400,one);
    }
}