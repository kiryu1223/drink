package io.github.kiryu1223.drink.db.mysql;

import io.github.kiryu1223.drink.base.DataBaseMetaData;
import io.github.kiryu1223.drink.base.IConfig;

public class MySQLUtil {

    /**
     * 判断是否是mysql8以上版本
     */
    public static boolean isAfter8(IConfig config) {
        DataBaseMetaData dataBaseMetaData = config.getDataBaseMetaData();
        String productVersion = dataBaseMetaData.getProductVersion();
        int start = safeToInt(productVersion.substring(0, 1));
        return start >= 8;
    }

    private static int safeToInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
