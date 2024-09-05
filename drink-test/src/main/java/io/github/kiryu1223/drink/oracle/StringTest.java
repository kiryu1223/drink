package io.github.kiryu1223.drink.oracle;

import io.github.kiryu1223.drink.ext.SqlFunctions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StringTest extends OracleTest
{
    private static final Logger log = LoggerFactory.getLogger(StringTest.class);

    @Test
    public void concat()
    {
        List<String> c1 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.concat("aa", "bb"))
                .toList();
        log.info(c1.toString());
        List<String> c2 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.concat("aa", "bb", "cc", "dd", "ee", "ff", "gg"))
                .toList();
        log.info(c2.toString());
    }

    @Test
    public void join()
    {
        List<String> j1 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.join("+", "aa", "bb"))
                .toList();
        log.info(j1.toString());
        List<String> j2 = client.queryEmptyTable()
                .endSelect(() ->
                        SqlFunctions.join("|", "aa", "bb", "cc", "dd", "ee", "ff", "gg")
                ).toList();
        log.info(j2.toString());
    }
}
