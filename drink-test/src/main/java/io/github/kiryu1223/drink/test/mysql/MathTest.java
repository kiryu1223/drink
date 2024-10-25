package io.github.kiryu1223.drink.test.mysql;

import io.github.kiryu1223.drink.core.sqlExt.SqlFunctions;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathTest extends BaseTest
{
    private static final Logger log = LoggerFactory.getLogger(MathTest.class);

//    //@Test
//    public void conn() throws SQLException
//    {
//        try (Connection connection = sqlserverDataSource.getConnection())
//        {
//            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT FLOOR(?)"))
//            {
//                preparedStatement.setDouble(1, 1.9);
//                try (ResultSet resultSet = preparedStatement.executeQuery())
//                {
//                    resultSet.next();
//                    Object o1 = resultSet.getObject(1);
//                    System.out.println(o1);
//                    System.out.println(o1.getClass());
//                }
//            }
//        }
//    }

    @Test
    public void absTest()
    {
        int res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.abs(-100))
                .first();

        Assert.assertEquals(100, res);
    }

    @Test
    public void cosTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.cos(1))
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void acosTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.acos(0.75))
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void sinTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.sin(60))
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void asinTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.asin(0.75))
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void tanTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.tan(90))
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void atanTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.atan(90))
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void atan2Test()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.atan2(1, 5))
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void ceilTest()
    {
        long res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.ceil(1.1))
                .first();

        Assert.assertEquals(2, res);
    }

    @Test
    public void cotTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.cot(180))
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void degreesTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.degrees(SqlFunctions.pi()))
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void expTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.exp(1))
                .first();

        Assert.assertEquals(Math.E, res, 0);
    }

    @Test
    public void floorTest()
    {
        int res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.floor(1.9))
                .first();

        Assert.assertEquals(1, res);
    }

    @Test
    public void bigTest()
    {
        int big = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.big(1, 2, 3, 4, 5, 6, 7))
                .first();

        Assert.assertEquals(7, big);

        double big2 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.big(1.1, 2.1, 3.1, 4.1, 5.1))
                .first();

        Assert.assertEquals(5.1, big2, 0);
    }

    @Test
    public void smallTest()
    {
        int big = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.small(1, 2, 3, 4, 5, 6, 7))
                .first();

        Assert.assertEquals(1, big);

        double big2 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.small(1.1, 2.1, 3.1, 4.1, 5.1))
                .first();

        Assert.assertEquals(1.1, big2, 0);
    }

    @Test
    public void lnTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.ln(10))
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void logTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.log(10, 2))
                .first();

        log.info(String.valueOf(res));

        double log2 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.log2(10))
                .first();

        log.info(String.valueOf(log2));

        double log10 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.log10(5))
                .first();

        log.info(String.valueOf(log10));
    }

    @Test
    public void modTest()
    {
        int res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.mod(10, 3))
                .first();

        Assert.assertEquals(1, res);
    }

    @Test
    public void piTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.pi())
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void powTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.pow(3, 3))
                .first();

        Assert.assertEquals(27, res, 0);
    }

    @Test
    public void radiansTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.radians(180f))
                .first();

        log.info(String.valueOf(res));
    }

    @Test
    public void randomTest()
    {
        double random = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.random())
                .first();

        log.info(String.valueOf(random));
    }


    @Test
    public void roundTest()
    {
        double r1 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.round(1.4))
                .first();

        Assert.assertEquals(1, r1, 0);

        double r2 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.round(1.5))
                .first();

        Assert.assertEquals(2, r2, 0);
    }

    @Test
    public void round2Test()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.round(1.88888, 2))
                .first();

        Assert.assertEquals(1.89, res, 0);
    }

    @Test
    public void signTest()
    {
        int zero = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.sign(0))
                .first();

        Assert.assertEquals(0, zero);

        int one = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.sign(0.1))
                .first();

        Assert.assertEquals(1, one);

        int minusOne = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.sign(-0.1))
                .first();

        Assert.assertEquals(-1, minusOne);
    }

    @Test
    public void sqrtTest()
    {
        double res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.sqrt(9))
                .first();

        Assert.assertEquals(3, res, 0);
    }

    @Test
    public void truncateTest()
    {
        int trunc = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.truncate(100.999999))
                .first();

        Assert.assertEquals(100, trunc);

        double trunc3 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.truncate(100.999999, 3))
                .first();

        Assert.assertEquals(100.999, trunc3, 0);
    }
}
