package io.github.kiryu1223.drink.oracle;

import io.github.kiryu1223.drink.ext.SqlFunctions;
import io.github.kiryu1223.drink.ext.SqlTimeUnit;
import io.github.kiryu1223.drink.pojos.Dual;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class DateTimeTest extends OracleTest
{
    private static final Logger log = LoggerFactory.getLogger(DateTimeTest.class);

    @Test
    public void now() throws SQLException
    {
        List<LocalDateTime> list = client.query(Dual.class)
                .endSelect(d -> SqlFunctions.now())
                .toList();
        log.debug(list.toString());
    }

    @Test
    public void dateTimeDiff()
    {
        String d1 = client.query(Dual.class)
                .endSelect(d -> SqlFunctions.dateTimeDiff(SqlTimeUnit.DAY,SqlFunctions.now(),SqlFunctions.now()))
                .toSql();
        log.debug(d1);
    }
}
