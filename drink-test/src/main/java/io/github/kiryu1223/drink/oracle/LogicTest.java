package io.github.kiryu1223.drink.oracle;

import io.github.kiryu1223.drink.ext.SqlFunctions;
import io.github.kiryu1223.drink.pojos.Dual;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogicTest extends OracleTest
{
    private static final Logger log = LoggerFactory.getLogger(LogicTest.class);

    @Test
    public void isNullOrIsNotNull()
    {
        String i1 = client.query(Dual.class).where(d -> SqlFunctions.isNull(1)).toSql();
        log.debug(i1);
        String i2 = client.query(Dual.class).where(d -> SqlFunctions.isNotNull(1)).toSql();
        log.debug(i2);
    }
}
