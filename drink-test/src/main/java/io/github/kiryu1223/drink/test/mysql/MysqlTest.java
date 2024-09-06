package io.github.kiryu1223.drink.test.mysql;

import io.github.kiryu1223.drink.api.client.DrinkClient;
import io.github.kiryu1223.drink.test.BaseTest;

public abstract class MysqlTest extends BaseTest
{
    protected final DrinkClient client;

    public MysqlTest()
    {
        this.client = mysqlClient;
    }
}
