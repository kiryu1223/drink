package io.github.kiryu1223.project;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.converter.SnakeNameConverter;
import io.github.kiryu1223.drink.base.toBean.handler.JsonTypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;
import io.github.kiryu1223.drink.core.SqlBuilder;
import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.project.pojos.Course;
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
//        String sql = client.query(MkUserChatMessage.class)
//                .innerJoin(client.query(MkUserChatMessage.class)
//                                .where(c -> c.getFromUserId() == 1 || c.getToUserId() == 1)
//                                .withTemp(c -> new Result() {
//                                    String chatKey = c.getFromUserId() < c.getToUserId()
//                                            ? concat(c.getFromUserId().toString(), "_", c.getToUserId().toString())
//                                            : c.getToUserId() + "_" + c.getFromUserId();
//                                    Date createTime = c.getCreateTime();
//                                })
//                                .groupBy(c -> new Grouper() {
//                                    String chatKey = c.chatKey;
//                                })
//                                .select(g -> new Result() {
//                                    String chatKey = g.key.chatKey;
//                                    Date maxCreateTime = g.max(g.value1.createTime);
//                                })
//                        , (c1, c2) -> c1.getCreateTime() == c2.maxCreateTime
//                                      && c2.chatKey == (c1.getFromUserId() < c1.getToUserId()
//                                ? c1.getFromUserId() + "_" + c1.getToUserId()
//                                : c1.getToUserId() + "_" + c1.getFromUserId()))
//                .as("latest")
//                .leftJoin(MkUser.class, (a, b, u1) -> u1.getId() == a.getFromUserId())
//                .leftJoin(MkUser.class, (a, b, u1, u2) -> u2.getId() == a.getToUserId())
//                .orderByDesc((a, b, u1, u2) -> (a.getToUserId() == 1 && a.getReadFlag() == 1) ? 1 : 0)
//                .orderByDesc((a, b, u1, u2) -> a.getCreateTime())
//                .select((a, b, u1, u2) -> new MkUserChatMessage() {
//                    {
//                        setId(a.getId());
//                        setFromUserId(a.getFromUserId());
//                        setToUserId(a.getToUserId());
//                        setType(a.getType());
//                        setContent(a.getContent());
//                        setCreateTime(a.getCreateTime());
//                        setReadFlag(a.getReadFlag());
//                        setReadTime(a.getReadTime());
//                    }
//                    String fromUsername = u1.getNickname();
//                    String toUsername = u2.getNickname();
//                    int hasUnread = (a.getToUserId() == 1 && a.getReadFlag() == 1) ? 1 : 0;
//                })
//                .toSql();
//
//
//        System.out.println(sql);
//
//        try (JdbcQueryResultSet jdbcResultSet = client.query(MkUser.class).toJdbcResultSet()) {
//            ResultSet resultSet = jdbcResultSet.getResultSet();
//
//            while (resultSet.next()) {
//                // ...
//            }
//        }


//        List<Long> targetUserId = Arrays.asList(1L);
////        System.out.println(targetUserId);
//        String sql2 = client.query(MkUserChatMessage.class)
//                .where(u -> u.getToUserId() == 123)
//                .where(a -> targetUserId.contains(a.getFromUserId()))
//                .groupBy(c -> new Grouper() {
//                    Long fromUserId = c.getFromUserId();
//                })
//                .select(c -> new Result() {
//                    Long fromUserId = c.key.fromUserId;
//                    Long count = c.count();
//                }).toSql();
//
//        System.out.println(sql2);


        String sql = client.query(Course.class)
                .where(c -> c.getJson().get(2).getBbb() != 0)
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
