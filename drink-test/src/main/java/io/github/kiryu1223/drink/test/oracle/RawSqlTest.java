package io.github.kiryu1223.drink.test.oracle;

import org.junit.Test;

import java.sql.*;
import java.time.LocalDateTime;

public class RawSqlTest extends BaseTest
{
//    @Test
//    public void r1() throws SQLException
//    {
//        try (Connection connection = oracleDataSource.getConnection())
//        {
//            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT (CURRENT_TIMESTAMP + INTERVAL '?' DAY) FROM DUAL"))
//            {
//                preparedStatement.setInt(1,1);
//                try (ResultSet resultSet = preparedStatement.executeQuery())
//                {
//                    resultSet.next();
//                    Object object = resultSet.getObject(1);
//                    System.out.println(object);
//                    System.out.println(object.getClass());
////                    LocalDateTime timestamp = resultSet.getObject(1, LocalDateTime.class);
////                    System.out.println(timestamp);
//                }
//            }
//        }
//    }
}
