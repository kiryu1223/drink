package io.github.kiryu1223.drink.test.oracle;

import io.github.kiryu1223.drink.ext.SqlFunctions;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringTest extends BaseTest
{
    private static final Logger log = LoggerFactory.getLogger(StringTest.class);

    @Test
    public void concat()
    {
        String c1 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.concat("aa", "bb"))
                .first();
        Assert.assertEquals("aabb",c1);
        String c2 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.concat("aa", "bb", "cc", "dd", "ee", "ff", "gg"))
                .first();
        Assert.assertEquals("aabbccddeeffgg",c2);
    }

    @Test
    public void join()
    {
        String j1 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.join("+", "aa", "bb"))
                .first();
        Assert.assertEquals("aa+bb",j1);
        String j2 = client.queryEmptyTable()
                .endSelect(() ->
                        SqlFunctions.join("|", "aa", "bb", "cc", "dd", "ee", "ff", "gg")
                ).first();
        Assert.assertEquals("aa|bb|cc|dd|ee|ff|gg",j2);
    }
}
