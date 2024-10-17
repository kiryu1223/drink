package io.github.kiryu1223.drink.test.sqlite;

import io.github.kiryu1223.drink.ext.SqlFunctions;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.kiryu1223.drink.ext.SqlFunctions.Case;
import static io.github.kiryu1223.drink.ext.SqlFunctions.when;

@SuppressWarnings("all")
public class ControlTest extends BaseTest
{
    private static final Logger log = LoggerFactory.getLogger(ControlTest.class);

    @Test
    public void ifTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.If(2 > 1, "二比一大", "一比二大"))
                .first();

        Assert.assertEquals("二比一大", res);
    }

    @Test
    public void ifNullTest()
    {
        String notnullres = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.ifNull("我不是空", null))
                .first();

        Assert.assertEquals("我不是空", notnullres);

        String nullres = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.ifNull(null, "我是空"))
                .first();

        Assert.assertEquals("我是空", nullres);
    }

    @Test
    public void nullIfTest()
    {
        String eq = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.nullIf("我们都一样", "我们都一样"))
                .first();

        Assert.assertEquals(null, eq);

        String notEq = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.nullIf("我们不一样", "我们都一样"))
                .first();

        Assert.assertEquals("我们不一样", notEq);
    }

    @Test
    public void caseTest()
    {
        char a = 'a';
        char b = 'b';
        int cmp = client.queryEmptyTable()
                .endSelect(() -> Case(
                        0,
                        when(a > b, 1),
                        when(a < b, -1)
                ))
                .first();

        log.info(String.valueOf(cmp));
    }
}
