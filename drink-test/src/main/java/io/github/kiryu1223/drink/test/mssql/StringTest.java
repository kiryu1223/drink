package io.github.kiryu1223.drink.test.mssql;

import io.github.kiryu1223.drink.ext.SqlFunctions;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringTest extends BaseTest
{
    private static final Logger log = LoggerFactory.getLogger(StringTest.class);
    private static final String atoz = "abcdefghijklmnopqrstuvwxyz";

    @Test
    public void isEmpty()
    {
        boolean isEmpty = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.isEmpty(""))
                .first();

        Assert.assertTrue(isEmpty);

        boolean isNotEmpty = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.isEmpty(" "))
                .first();

        Assert.assertFalse(isNotEmpty);
    }

    @Test
    public void strToAsciiTest()
    {
        int res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.strToAscii("abc"))
                .first();

        Assert.assertEquals(97, res);
    }

    @Test
    public void asciiToStrTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.asciiToStr(97))
                .first();

        Assert.assertEquals("a", res);
    }

    @Test
    public void lengthTest()
    {
        int res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.length(atoz))
                .first();

        Assert.assertEquals(26, res);
    }


    @Test
    public void concatTest()
    {
        String c1 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.concat("aa", "bb"))
                .first();
        Assert.assertEquals("aabb", c1);
        String c2 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.concat("aa", "bb", "cc", "dd", "ee", "ff", "gg"))
                .first();
        Assert.assertEquals("aabbccddeeffgg", c2);
    }

    @Test
    public void joinTest()
    {
        String j1 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.join("+", "aa", "bb"))
                .first();
        Assert.assertEquals("aa+bb", j1);
        String j2 = client.queryEmptyTable()
                .endSelect(() ->
                        SqlFunctions.join("|", "aa", "bb", "cc", "dd", "ee", "ff", "gg")
                ).first();
        Assert.assertEquals("aa|bb|cc|dd|ee|ff|gg", j2);
    }

    @Test
    public void indexOfTest()
    {
        int res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.indexOf(atoz, "h"))
                .first();

        Assert.assertEquals(8, res);

        int res2 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.indexOf("aabbaabbaabb", "bb", 6))
                .first();

        Assert.assertEquals(7, res2);
    }

    @Test
    public void toLowerCaseTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.toLowerCase("UP"))
                .first();

        Assert.assertEquals("up", res);
    }

    @Test
    public void toUpperCaseTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.toUpperCase("down"))
                .first();

        Assert.assertEquals("DOWN", res);
    }

    @Test
    public void leftTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.left("HelloWorld", 5))
                .first();

        Assert.assertEquals("Hello", res);
    }

    @Test
    public void rightTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.right("HelloWorld", 5))
                .first();

        Assert.assertEquals("World", res);
    }

    @Test
    public void byteLengthTest()
    {
        int res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.byteLength("哒哒哒"))
                .first();

        System.out.println(res);
    }

    @Test
    public void leftPadTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.leftPad("|", 5, "-"))
                .first();

        Assert.assertEquals("----|", res);
    }

    @Test
    public void rightPadTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.rightPad("|", 5, "-"))
                .first();

        Assert.assertEquals("|----", res);
    }

    @Test
    public void trimTest()
    {
        String h = " 呵呵 ";

        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.trim(h))
                .first();

        Assert.assertEquals("呵呵", res);

        String res2 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.trimStart(h))
                .first();

        Assert.assertEquals("呵呵 ", res2);

        String res3 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.trimEnd(h))
                .first();

        Assert.assertEquals(" 呵呵", res3);
    }

    @Test
    public void replaceTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.replace("宝宝你是一个一个sb", "sb", "傻宝"))
                .first();

        Assert.assertEquals("宝宝你是一个一个傻宝", res);
    }

    @Test
    public void reverseTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.reverse("abc"))
                .first();

        Assert.assertEquals("cba", res);
    }

    @Test
    public void compareTest()
    {
        int res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.compare("A", "B"))
                .first();

        Assert.assertEquals(-1, res);
    }

    @Test
    public void subStringTest()
    {
        String res = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.subString(atoz, 6))
                .first();

        Assert.assertEquals("fghijklmnopqrstuvwxyz", res);

        String res2 = client.queryEmptyTable()
                .endSelect(() -> SqlFunctions.subString(atoz, 6, 6))
                .first();

        Assert.assertEquals("fghijk", res2);
    }
}
