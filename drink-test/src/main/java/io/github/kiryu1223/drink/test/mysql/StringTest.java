package io.github.kiryu1223.drink.test.mysql;

import io.github.kiryu1223.drink.ext.SqlFunctions;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StringTest extends MysqlTest
{
    private static final Logger log = LoggerFactory.getLogger(StringTest.class);

    @Test
    public void concat()
    {
        List<String> c1 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.concat("aa", "bb"))
                .toList();
        Assert.assertEquals(c1.get(0),"aabb");
        List<String> c2 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.concat("aa", "bb", "cc", "dd", "ee", "ff", "gg"))
                .toList();
        Assert.assertEquals(c2.get(0),"aabbccddeeffgg");
    }

    @Test
    public void join()
    {
        List<String> j1 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.join("+", "aa", "bb"))
                .toList();
        Assert.assertEquals(j1.get(0),"aa+bb");
        List<String> j2 = client.queryEmptyTable()
                .endSelect(() ->
                        SqlFunctions.join("|", "aa", "bb", "cc", "dd", "ee", "ff", "gg")
                ).toList();
        Assert.assertEquals(j2.get(0),"aa|bb|cc|dd|ee|ff|gg");
    }
}
