package io.github.kiryu1223.drink.test.sqlite;

import io.github.kiryu1223.drink.pojos.Employee;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("all")
public class JavaMethodsTest extends BaseTest
{
    // region [String]
    @Test
    public void containsTest()
    {
        List<Employee> gao = client.query(Employee.class)
                .where(e -> e.getFirstName().contains("Gao"))
                .toList();

        Assert.assertEquals(244, gao.size());
    }

    @Test
    public void startsWithTest()
    {
        List<Employee> bor = client.query(Employee.class)
                .where(e -> e.getLastName().startsWith("Bor"))
                .toList();

        Assert.assertEquals(570, bor.size());
    }

    @Test
    public void endsWithTest()
    {
        List<Employee> user = client.query(Employee.class)
                .where(e -> e.getLastName().endsWith("user"))
                .toList();

        Assert.assertEquals(370, user.size());
    }

    @Test
    public void lengthTest()
    {
        String str = "abcdefg";
        int length = client.queryEmptyTable()
                .endSelect(() -> str.length()).first();

        Assert.assertEquals(7, length);
    }

    @Test
    public void toUpperCaseTest()
    {
        String str = "abcdefg";
        String upper = client.queryEmptyTable()
                .endSelect(() -> str.toUpperCase()).first();

        Assert.assertEquals("ABCDEFG", upper);
    }

    @Test
    public void toLowerCaseTest()
    {
        String str = "ABCDEFG";
        String upper = client.queryEmptyTable()
                .endSelect(() -> str.toLowerCase()).first();

        Assert.assertEquals("abcdefg", upper);
    }

    @Test
    public void concatTest()
    {
        String s1 = "abc";
        String s2 = "xyz";
        String AtoZ = client.queryEmptyTable()
                .endSelect(() -> s1.concat(s2)).first();

        Assert.assertEquals("abcxyz", AtoZ);
    }

    @Test
    public void trimTest()
    {
        String str = "  blankBlank  ";
        String blank = client.queryEmptyTable()
                .endSelect(() -> str.trim()).first();

        Assert.assertEquals("blankBlank", blank);
    }

    @Test
    public void isEmptyTest()
    {
        String str = "";
        boolean isEmpty = client.queryEmptyTable()
                .endSelect(() -> str.isEmpty()).first();

        Assert.assertTrue(isEmpty);
    }

    @Test
    public void indexOfTest()
    {
        String str = "abc|def|ghi";
        int first = client.queryEmptyTable()
                .endSelect(() -> str.indexOf("|")).first();

        Assert.assertEquals(4, first);
    }

    @Test
    public void indexOf2Test()
    {
        String str = "abc|def|ghi";
        int first = client.queryEmptyTable()
                .endSelect(() -> str.indexOf("|", 5)).first();

        Assert.assertEquals(8, first);
    }

    @Test
    public void replaceTest()
    {
        String str = "abc|def|ghi";
        String replace = client.queryEmptyTable()
                .endSelect(() -> str.replace("|", "-")).first();

        Assert.assertEquals("abc-def-ghi", replace);
    }

    @Test
    public void substringTest()
    {
        String str = "abcdefghijklmn";
        String replace = client.queryEmptyTable()
                .endSelect(() -> str.substring(3)).first();

        Assert.assertEquals("cdefghijklmn", replace);
    }

    @Test
    public void substring2Test()
    {
        String str = "abcdefghijklmn";
        String replace = client.queryEmptyTable()
                .endSelect(() -> str.substring(3, 3)).first();

        Assert.assertEquals("cde", replace);
    }

    @Test
    public void joinTest()
    {
        String replace = client.queryEmptyTable()
                .endSelect(() -> String.join("/", "a", "b", "c", "d", "e")).first();

        Assert.assertEquals("a/b/c/d/e", replace);
    }

    @Test
    public void join2Test()
    {
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        String replace = client.queryEmptyTable()
                .endSelect(() -> String.join("/", list)).first();

        Assert.assertEquals("a/b/c/d/e", replace);
    }

    //endregion

    //region [Math]

    @Test
    public void absTest()
    {
        int abs = client.queryEmptyTable()
                .endSelect(() -> Math.abs(-999))
                .first();

        Assert.assertEquals(999, abs);
    }

    //endregion

    //region [BigDecimal]

    @Test
    public void addTest()
    {
        BigDecimal b1 = BigDecimal.valueOf(9999);
        BigDecimal add = client.queryEmptyTable()
                .endSelect(() -> b1.add(BigDecimal.ONE))
                .first();

        Assert.assertEquals(BigDecimal.valueOf(10000), add);
    }

    @Test
    public void subtractTest()
    {
        BigDecimal b1 = BigDecimal.valueOf(9999);
        BigDecimal subtract = client.queryEmptyTable()
                .endSelect(() -> b1.subtract(BigDecimal.ONE))
                .first();

        Assert.assertEquals(BigDecimal.valueOf(9998), subtract);
    }

    @Test
    public void multiplyTest()
    {
        BigDecimal b1 = BigDecimal.valueOf(9999);
        BigDecimal multiply = client.queryEmptyTable()
                .endSelect(() -> b1.multiply(BigDecimal.TEN))
                .first();

        Assert.assertEquals(BigDecimal.valueOf(99990), multiply);
    }

    @Test
    public void divideTest()
    {
        BigDecimal b1 = BigDecimal.valueOf(9999);
        BigDecimal b2 = BigDecimal.valueOf(9.9);
        BigDecimal divide = client.queryEmptyTable()
                .endSelect(() -> b1.divide(b2))
                .first();

        Assert.assertEquals(1010.0, divide.doubleValue(), 0);
    }

    @Test
    public void remainderTest()
    {
        BigDecimal b1 = BigDecimal.valueOf(9999);
        BigDecimal remainder = client.queryEmptyTable()
                .endSelect(() -> b1.remainder(BigDecimal.TEN))
                .first();

        Assert.assertEquals(BigDecimal.valueOf(9), remainder);
    }

    //endregion

    //region [TIME]

    @Test
    public void isAfterTest()
    {
        boolean isAfter = client.queryEmptyTable()
                .endSelect(() -> LocalDate.of(2000, 7, 7).isAfter(LocalDate.of(1997, 7, 7)))
                .first();

        Assert.assertTrue(isAfter);
    }

    @Test
    public void isBeforeTest()
    {
        boolean isBefore = client.queryEmptyTable()
                .endSelect(() -> LocalDate.of(1997, 7, 7).isBefore(LocalDate.of(2000, 7, 7)))
                .first();

        Assert.assertTrue(isBefore);
    }

    @Test
    public void isEqualTest()
    {
        boolean isEqual = client.queryEmptyTable()
                .endSelect(() -> LocalDate.of(1997, 7, 7).isEqual(LocalDate.of(1997, 7, 7)))
                .first();

        Assert.assertTrue(isEqual);
    }

    //endregion
}
