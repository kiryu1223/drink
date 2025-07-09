package io.github.kiryu1223.project;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.converter.SnakeNameConverter;
import io.github.kiryu1223.drink.base.toBean.handler.JsonTypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;
import io.github.kiryu1223.drink.core.SqlBuilder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.crud.read.EndQuery;
import io.github.kiryu1223.project.pojos.MkContent;
import io.github.kiryu1223.project.pojos.MkContentUserActLog;
import io.github.kiryu1223.project.pojos.MkUserActLog;
import lombok.Data;

import java.lang.reflect.Type;
import java.sql.SQLException;

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

    public static void main(String[] args) throws SQLException {
        SqlClient client = boot();

        EndQuery<? extends Union> q1 = client.query(MkUserActLog.class)
                .select(m -> new Union() {{
                    setFromUserId(m.getFromUserId());
                    setToUserId(m.getToUserId());
                    setType(点赞类型.主页);
                }});

        EndQuery<? extends Union> q2 = client.query(MkContentUserActLog.class)
                .leftJoin(MkContent.class, (a, b) -> a.getContentId().equals(b.getId()))
                .select((a, b) -> new Union() {{
                    setFromUserId(a.getUserId());
                    setToUserId(b.getUserId());
                    setType(点赞类型.作品);
                }});

        String sql = client.unionAll(q1, q2)
                .withTemp()
                .where(u -> u.getToUserId() == 100)
                .orderBy(u -> u.getFromUserId())
                .limit(10)
                .toSql();

        System.out.println(sql);
    }

    @Data
    static class Union {
        private Long fromUserId;
        private Long toUserId;
        private 点赞类型 type;
    }

    enum 点赞类型 {
        主页,
        作品
    }
}
