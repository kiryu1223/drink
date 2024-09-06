package io.github.kiryu1223.drink.test.oracle;

import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.test.BaseTest;

public abstract class OracleTest extends BaseTest
{
    protected final DrinkClient client;

    public OracleTest()
    {
        this.client = oracleClient;
    }
}
