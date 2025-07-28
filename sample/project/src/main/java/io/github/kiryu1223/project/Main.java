package io.github.kiryu1223.project;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.Aop;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.converter.SnakeNameConverter;
import io.github.kiryu1223.drink.base.toBean.handler.JsonTypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;
import io.github.kiryu1223.drink.core.SqlBuilder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.project.pojos.Employee;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Objects;

public class Main {
    public static SqlClient boot() {
        TypeHandlerManager.setJsonTypeHandler(new JsonTypeHandler<Object>() {
            @Override
            public Object jsonToObject(String json, Type type) {
                return new Object();
            }

            @Override
            public String objectToJson(Object value) {
                return "{}";
            }
        });

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return SqlBuilder.bootStrap()
                // 数据库
                .setDbType(DbType.MySQL)
                // 名称转换风格
                .setNameConverter(new SnakeNameConverter())
                // 数据源
                .setDataSource(dataSource)
                .build();
    }

    public static HikariDataSource neo4jBoot() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("neo4j://m.hzwp.net:7687");
        dataSource.setUsername("neo4j");
        dataSource.setPassword("Yundi@7474");
        dataSource.setDriverClassName("org.neo4j.jdbc.Neo4jDriver");
        return dataSource;
    }

    public static void main(String[] args) throws SQLException {
        SqlClient client = boot();
        String sql = client.update(Employee.class)
                .where(e -> Objects.equals(e.getNumber(), 1000))
                .set(e -> e.getFirstName(), "")
                .toSql();
        System.out.println(sql);

//        IConfig config = client.getConfig();
//        Aop aop = config.getAop();
//        aop.onUpdate(Employee.class, e -> {
//            String firstName = e.getFirstName();
//            if (firstName == null) {
//                e.setFirstName("aaa");
//            }
//        });

//        try (HikariDataSource dataSource = neo4jBoot()) {
//            try (Connection connection = dataSource.getConnection()) {
//                DatabaseMetaData metaData = connection.getMetaData();
//                System.out.println(metaData.getDatabaseProductName());
//                System.out.println(metaData.getDatabaseProductVersion());
//                System.out.println(metaData.getDriverName());
//                System.out.println(metaData.getDriverVersion());
//                System.out.println(metaData.getURL());
//                System.out.println(metaData.getUserName());
//                System.out.println(metaData.getDriverMajorVersion());
//                System.out.println(metaData.getDriverMinorVersion());
//                System.out.println(metaData.getJDBCMajorVersion());
//                System.out.println(metaData.getJDBCMinorVersion());
//                System.out.println(metaData.getDatabaseMajorVersion());
//                System.out.println(metaData.getDatabaseMinorVersion());
//                System.out.println(metaData.getDefaultTransactionIsolation());
//                System.out.println(metaData.getExtraNameCharacters());
//                System.out.println(metaData.getIdentifierQuoteString());
//                System.out.println(metaData.getNumericFunctions());
//                System.out.println(metaData.getStringFunctions());
//                System.out.println(metaData.getSystemFunctions());
//                System.out.println(metaData.getTimeDateFunctions());
//                System.out.println(metaData.getURL());
//            }
//        }
    }
}
