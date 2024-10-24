package io.github.kiryu1223.drink.test.sqlite;

import io.github.kiryu1223.drink.api.Result;
import io.github.kiryu1223.drink.ext.types.SqlTypes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;

import static io.github.kiryu1223.drink.base.sqlext.SqlTimeUnit.DAY;
import static io.github.kiryu1223.drink.ext.SqlFunctions.*;

public class LogicTest extends BaseTest
{
    private static final Logger log = LoggerFactory.getLogger(LogicTest.class);

    @Test
    public void noGirlFriendTest()
    {
        String day1 = client.queryEmptyTable()
                .endSelect(() -> concat(
                        "没有女朋友的第",
                        cast(dateTimeDiff(DAY, "1996-10-27", now()), String.class),
                        "天")
                )
                .first();

        log.info(day1);

        String day2 = client.queryEmptyTable()
                .endSelect(() -> concat(
                        "没有女朋友的第",
                        cast(dateTimeDiff(DAY, "1996-10-27", now()), SqlTypes.varchar2()),
                        "天")
                )
                .first();

        log.info(day2);
    }

    @Test
    public void castNumberTest() throws SQLException
    {
        Result result = client.queryEmptyTable()
                .select(() -> new Result()
                {
                    byte b1 = cast("100", byte.class);
                    short s1 = cast("100", short.class);
                    int i1 = cast("100", int.class);
                    long l1 = cast("100", long.class);
                    float f1 = cast("100", float.class);
                    double d1 = cast("100", double.class);
                    BigDecimal bd1 = cast("10000.999999999999", BigDecimal.class);
                }).first();

        log.info(result.toString());
    }

    @Test
    public void castStringTest()
    {
        Result one = client.queryEmptyTable()
                .select(() -> new Result()
                {
                    String s1 = cast(10000, String.class);
                    String s2 = cast(10000, SqlTypes.varchar2());
                    char c1 = cast("大大大", char.class);
                    char c2 = cast(2, char.class);
                    char c3 = cast("小小小", SqlTypes.Char());
                }).first();

        log.info(one.toString());
    }

    @Test
    public void singleCharTest()
    {
        char da = client.queryEmptyTable()
                .endSelect(() -> cast("哒哒哒", char.class))
                .first();

        log.info(String.valueOf(da));
    }
}
