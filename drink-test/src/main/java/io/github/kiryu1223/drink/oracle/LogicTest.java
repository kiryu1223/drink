package io.github.kiryu1223.drink.oracle;

import io.github.kiryu1223.drink.ext.SqlFunctions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.github.kiryu1223.drink.ext.SqlFunctions.*;
import static io.github.kiryu1223.drink.ext.SqlFunctions.now;
import static io.github.kiryu1223.drink.ext.SqlTimeUnit.DAY;

public class LogicTest extends OracleTest
{
    private static final Logger log = LoggerFactory.getLogger(LogicTest.class);

    @Test
    public void l1()
    {
        List<String> sql = client.queryEmptyTable()
                .endSelect(() -> concat("没有女朋友的第", cast(dateTimeDiff(DAY, "1996-10-27", now()), String.class), "天"))
                .toList();

        log.info(sql.toString());
    }
}
