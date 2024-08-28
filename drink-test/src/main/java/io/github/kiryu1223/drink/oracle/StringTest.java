package io.github.kiryu1223.drink.oracle;

import io.github.kiryu1223.drink.ext.SqlFunctions;
import io.github.kiryu1223.drink.pojos.Dual;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringTest extends OracleTest
{
    private static final Logger log = LoggerFactory.getLogger(StringTest.class);

    @Test
    public void concat()
    {
        String c1 = client.query(Dual.class)
                .endSelect(d -> SqlFunctions.concat("aa", "bb"))
                .toSql();
        log.debug(c1);
        String c2 = client.query(Dual.class)
                .endSelect(d -> SqlFunctions.concat("aa", "bb", "cc", "dd", "ee", "ff", "gg"))
                .toSql();
        log.debug(c2);
    }

    @Test
    public void join()
    {
        String j1 = client.query(Dual.class)
                .endSelect(d -> SqlFunctions.join("+", "aa", "bb"))
                .toSql();
        log.debug(j1);
        String j2 = client.query(Dual.class)
                .endSelect(d ->
                        SqlFunctions.join("|", "aa", "bb", "cc", "dd", "ee", "ff", "gg")
                ).toSql();
        log.debug(j2);
    }
}
