package io.github.kiryu1223.drink.test.mssql;

import io.github.kiryu1223.drink.ext.SqlFunctions;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class ControlTest extends BaseTest
{
    @Test
    public void ifTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.If(2 > 1, "二比一大", "一比二大"))
                .first();

        Assert.assertEquals("二比一大",res);
    }
}
