package io.github.kiryu1223.drink.test.mssql;

import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.test.BaseTest;

public abstract class SqlServerTest extends BaseTest
{
    protected final DrinkClient client;

    public SqlServerTest()
    {
        this.client = sqlserverClient;
    }
}
