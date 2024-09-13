package io.github.kiryu1223.drink.test.oracle;

import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.github.kiryu1223.drink.ext.SqlFunctions.*;

public class DateTimeTest extends BaseTest
{
    private static final Logger log = LoggerFactory.getLogger(DateTimeTest.class);

    //@Test
    public void conn() throws SQLException
    {
        try (Connection connection = oracleDataSource.getConnection())
        {
            String s1 = "SELECT CAST(? AS DATE) - TO_DATE('1996-10-27' ,'YYYY-MM-DD hh:mi:ss') FROM DUAL";
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT ? - ? FROM DUAL"))
            {
                preparedStatement.setObject(1, LocalDateTime.now());
                preparedStatement.setObject(2, LocalDateTime.of(1996, 10, 27, 10, 10));
                try (ResultSet resultSet = preparedStatement.executeQuery())
                {
                    resultSet.next();
                    Object o1 = resultSet.getObject(1);
                    System.out.println(o1);
                    System.out.println(o1.getClass());
                }
            }
        }
    }

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

        int one1 = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.DAY, "1996-10-27", now))
                .first();

        log.info(String.valueOf(one1));
    }

    @Test
    public void secondDiffTest()
    {
        LocalDateTime start = LocalDateTime.of(1996, 10, 27, 0, 0);
        LocalDateTime end = LocalDateTime.of(2000, 1, 1, 0, 0);
        int one = client.queryEmptyTable()
                .endSelect(() -> dateTimeDiff(SqlTimeUnit.SECOND, start, end))
                .first();

        Assert.assertEquals(100310400, one);
    }
}
