package io.github.kiryu1223.drink.mysql;

import io.github.kiryu1223.drink.ext.SqlFunctions;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class DateTimeTest extends MySqlTest
{
    private static final Logger log = LoggerFactory.getLogger(DateTimeTest.class);

    @Test
    public void now() throws SQLException
    {
        List<LocalDateTime> list = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.now())
                .toList();

        for (LocalDateTime localDateTime : list)
        {
            log.debug(localDateTime.toString());
        }
    }

    @Test
    public void dateTimeDiff()
    {
        List<Integer> list = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.dateTimeDiff(SqlTimeUnit.DAY, "1996-10-27", SqlFunctions.now()))
                .toList();

        for (int localDateTime : list)
        {
            log.debug(String.valueOf(localDateTime));
        }
    }
}
