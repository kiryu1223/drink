# Drink ORM 使用文档

qq群：257911716

**最新最热版本:**![Maven Central Version](https://img.shields.io/maven-central/v/io.github.kiryu1223/drink-all)

## 目录

- [项目简介](#项目简介)
- [快速开始](#快速开始)
- [环境配置](#环境配置)
- [实体映射](#实体映射)
- [基础操作](#基础操作)
- [高级查询](#高级查询)
- [事务管理](#事务管理)
- [配置选项](#配置选项)
- [最佳实践](#最佳实践)
- [常见问题](#常见问题)

## 项目简介

Drink 是一个现代化的 Java ORM 框架，提供类型安全的 Lambda 表达式查询，支持多种数据库和框架集成。

### 核心特性

- 🚀 **类型安全**: 基于 Lambda 表达式的类型安全查询
- 🔧 **多数据库支持**: MySQL、PostgreSQL、SQLServer、Oracle、H2、SQLite
- 🌐 **框架集成**: 支持 Spring Boot、Solon 等主流框架
- ⚡ **高性能**: 自动批量操作、连接池优化
- 🎯 **简洁API**: 直观的链式调用，减少样板代码
- 🔄 **关联查询**: 强大的 Include 机制处理复杂关联关系

## 快速开始

## 环境配置

### 方式一：独立项目配置

适用于从零开始的新项目或需要完全控制配置的场景。

#### 1. Maven 依赖配置

```xml
<dependencies>
    <!-- Drink 核心依赖 -->
    <dependency>
        <groupId>io.github.kiryu1223</groupId>
        <artifactId>drink-core</artifactId>
        <version>${project.version}</version>
    </dependency>

    <!-- 日志实现（必需） -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.12</version>
    </dependency>

    <!-- 数据库驱动（根据实际数据库选择） -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>9.0.0</version>
    </dependency>

    <!-- 连接池 -->
    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
        <version>4.0.3</version>
    </dependency>

    <!-- Lombok（可选，用于简化实体类） -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.34</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <!-- 启用 ExpressionTree 插件 -->
                <compilerArgs>
                    <arg>-Xplugin:ExpressionTree</arg>
                </compilerArgs>
                <annotationProcessorPaths>
                    <path>
                        <groupId>io.github.kiryu1223</groupId>
                        <artifactId>drink-core</artifactId>
                        <version>${project.version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>1.18.34</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

```java
package io.github.kiryu1223.example;

import com.zaxxer.hikari.HikariDataSource;
import io.github.kiryu1223.drink.base.DbType;
import io.github.kiryu1223.drink.base.converter.SnakeNameConverter;
import io.github.kiryu1223.drink.core.SqlBuilder;
import io.github.kiryu1223.drink.core.SqlClient;

public class DrinkExample {
    public static void main(String[] args) {
        // 1. 配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test_db?rewriteBatchedStatements=true");
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // 2. 创建 SqlClient
        SqlClient client = SqlBuilder.bootStrap()
                .setDbType(DbType.MySQL)                    // 数据库类型
                .setNameConverter(new SnakeNameConverter()) // 命名转换策略
                .setDataSource(dataSource)                  // 数据源
                .build();

        // 3. 开始使用
        // 查询示例
        List<User> users = client.query(User.class)
                .where(u -> u.getAge() > 18)
                .toList();

        System.out.println("查询到 " + users.size() + " 个用户");
    }
}
```

#### 3. 基本使用示例

```java
// 查询
List<User> users = client.query(User.class)
        .where(u -> u.getName().contains("张"))
        .orderBy(u -> u.getAge())
        .limit(10)
        .toList();

// 插入
User newUser = new User();
newUser.setName("张三");
newUser.setAge(25);
long insertCount = client.insert(newUser).executeRows();

// 更新
long updateCount = client.update(User.class)
        .set(u -> u.setAge(26))
        .where(u -> u.getName().equals("张三"))
        .executeRows();

// 删除
long deleteCount = client.delete(User.class)
        .where(u -> u.getAge() < 18)
        .executeRows();
```

### 方式二：Spring Boot 集成

适用于 Spring Boot 项目，提供自动配置和依赖注入支持。

#### 1. 添加依赖

```xml
<dependencies>
    <!-- Drink Spring Boot Starter -->
    <dependency>
        <groupId>io.github.kiryu1223</groupId>
        <artifactId>drink-spring-boot-starter</artifactId>
        <version>${project.version}</version>
    </dependency>

    <!-- Spring Boot Web Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- 数据库驱动 -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <compilerArgs>
                    <arg>-Xplugin:ExpressionTree</arg>
                </compilerArgs>
                <annotationProcessorPaths>
                    <path>
                        <groupId>io.github.kiryu1223</groupId>
                        <artifactId>drink-core</artifactId>
                        <version>${project.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

**application.yml**

```yaml
spring:
  # 数据源配置
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:mysql://127.0.0.1:3306/test_db?rewriteBatchedStatements=true
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5

# Drink 配置
drink:
  database: mysql        # 数据库类型
  print-sql: true       # 是否打印 SQL
  print-batch: false    # 是否打印批量操作信息
  name-conversion: snake_case  # 命名转换策略

server:
  port: 8080
```

#### 3. 使用示例

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private DrinkClient drinkClient;

    @GetMapping
    public List<User> getUsers(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        return drinkClient.query(User.class)
                .orderBy(u -> u.getId())
                .limit(page * size, size)
                .toList();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return drinkClient.query(User.class)
                .where(u -> u.getId().equals(id))
                .first();
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        long count = drinkClient.insert(user).executeRows();
        return ResponseEntity.ok("创建成功，影响行数：" + count);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                           @RequestBody User user) {
        long count = drinkClient.update(User.class)
                .set(u -> {
                    u.setName(user.getName());
                    u.setAge(user.getAge());
                })
                .where(u -> u.getId().equals(id))
                .executeRows();
        return ResponseEntity.ok("更新成功，影响行数：" + count);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        long count = drinkClient.delete(User.class)
                .where(u -> u.getId().equals(id))
                .executeRows();
        return ResponseEntity.ok("删除成功，影响行数：" + count);
    }
}

### 方式三：Solon 框架集成

适用于 Solon 框架项目，支持多数据源配置。

#### 1. 添加依赖

```xml
<dependency>
    <groupId>io.github.kiryu1223</groupId>
    <artifactId>drink-solon-plugin</artifactId>
    <version>${project.version}</version>
</dependency>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <compilerArgs>
                    <arg>-Xplugin:ExpressionTree</arg>
                </compilerArgs>
                <annotationProcessorPaths>
                    <path>
                        <groupId>io.github.kiryu1223</groupId>
                        <artifactId>drink-core</artifactId>
                        <version>${project.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 实体映射

### 基础注解

#### @Table - 表映射

```java
@Table("user_info")  // 指定表名
@Table(schema = "test", value = "users")  // 指定 schema 和表名
public class User {
    // ...
}
```

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| value | String | 类名转换 | 数据库表名 |
| schema | String | 空 | 数据库 schema |

#### @Column - 列映射

```java
public class User {
    @Column(value = "user_id", primaryKey = true)
    private Long id;

    @Column("user_name")
    private String name;

    @Column(value = "age", notNull = true)
    private Integer age;

    @Column(value = "created_at", generatedKey = true)
    private LocalDateTime createTime;
}
```

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| value | String | 字段名转换 | 数据库列名 |
| primaryKey | boolean | false | 是否为主键 |
| notNull | boolean | false | 是否非空 |
| generatedKey | boolean | false | 是否为生成列（自增/默认值） |

#### @Navigate - 关联关系

```java
public class User {
    @Column(value = "user_id", primaryKey = true)
    private Long id;

    // 一对多：一个用户有多个订单
    @Navigate(value = RelationType.OneToMany,
              self = "id",
              target = "userId")
    private List<Order> orders;
}

public class Order {
    @Column(value = "order_id", primaryKey = true)
    private Long id;

    @Column("user_id")
    private Long userId;

    // 多对一：多个订单属于一个用户
    @Navigate(value = RelationType.ManyToOne,
              self = "userId",
              target = "id")
    private User user;

    // 多对多：订单和商品的关系（通过中间表）
    @Navigate(value = RelationType.ManyToMany,
              self = "id",
              selfMapping = "orderId",
              mappingTable = OrderProduct.class,
              targetMapping = "productId",
              target = "id")
    private List<Product> products;
}
```

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| value | RelationType | 必填 | 关联关系类型 |
| self | String | 必填 | 当前实体的关联字段 |
| target | String | 必填 | 目标实体的关联字段 |
| mappingTable | Class | 空 | 中间表类（多对多时必填） |
| selfMapping | String | 空 | 中间表中当前实体的字段 |
| targetMapping | String | 空 | 中间表中目标实体的字段 |

#### @IgnoreColumn - 忽略字段

```java
public class User {
    private Long id;
    private String name;

    @IgnoreColumn  // 该字段不参与数据库映射
    private String tempField;
}
```

### 完整实体示例

```java
@Data
@Table("users")
public class User {
    @Column(value = "user_id", primaryKey = true)
    private Long id;

    @Column("user_name")
    private String name;

    @Column(value = "age", notNull = true)
    private Integer age;

    @Column("email")
    private String email;

    @Column(value = "created_at", generatedKey = true)
    private LocalDateTime createTime;

    @Navigate(value = RelationType.OneToMany, self = "id", target = "userId")
    private List<Order> orders;

    @IgnoreColumn
    private String displayName;  // 计算字段，不存储到数据库
}
```

## 基础操作

所有的增删查改操作都通过 `DrinkClient` 对象完成。

### 查询操作

#### 基本查询

```java
// 查询所有用户
List<User> allUsers = client.query(User.class).toList();

// 条件查询
List<User> adults = client.query(User.class)
        .where(u -> u.getAge() >= 18)
        .toList();

// 多条件查询
List<User> result = client.query(User.class)
        .where(u -> u.getAge() >= 18)
        .where(u -> u.getName().contains("张"))
        .toList();

// OR 条件
List<User> result = client.query(User.class)
        .where(u -> u.getAge() >= 18)
        .orWhere(u -> u.getName().contains("管理员"))
        .toList();
```

#### 排序和分页

```java
// 排序
List<User> users = client.query(User.class)
        .orderBy(u -> u.getAge())           // 升序
        .orderBy(u -> u.getName(), false)   // 降序
        .toList();

// 分页
List<User> users = client.query(User.class)
        .orderBy(u -> u.getId())
        .limit(10)          // 限制 10 条
        .toList();

List<User> users = client.query(User.class)
        .orderBy(u -> u.getId())
        .limit(20, 10)      // 跳过 20 条，取 10 条
        .toList();
```

#### 字段选择

```java
// 选择特定字段
List<? extends Result> result = client.query(User.class)
        .select(u -> new Result() {
            String name = u.getName();
            Integer age = u.getAge();
        })
        .toList();

// 选择单个字段
List<String> names = client.query(User.class)
        .endSelect(u -> u.getName())
        .toList();

// 聚合查询
long count = client.query(User.class)
        .where(u -> u.getAge() >= 18)
        .count();

Integer maxAge = client.query(User.class).max(u -> u.getAge());
Integer minAge = client.query(User.class).min(u -> u.getAge());
BigDecimal avgAge = client.query(User.class).avg(u -> u.getAge());
```

#### 连表查询

```java
// 内连接
List<? extends Result> result = client.query(User.class)
        .innerJoin(Order.class, (u, o) -> u.getId().equals(o.getUserId()))
        .select((u, o) -> new Result() {
            String userName = u.getName();
            String orderNo = o.getOrderNo();
            BigDecimal amount = o.getAmount();
        })
        .toList();

// 左连接
List<? extends Result> result = client.query(User.class)
        .leftJoin(Order.class, (u, o) -> u.getId().equals(o.getUserId()))
        .where((u, o) -> u.getAge() >= 18)
        .select((u, o) -> new Result() {
            String userName = u.getName();
            Long orderCount = SqlFunctions.count(o.getId());
        })
        .toList();
```

### 插入操作

#### 单条插入

```java
User user = new User();
user.setName("张三");
user.setAge(25);
user.setEmail("zhangsan@example.com");

long insertCount = client.insert(user).executeRows();
System.out.println("插入成功，影响行数：" + insertCount);
```

#### 批量插入

```java
List<User> users = Arrays.asList(
    new User("张三", 25, "zhangsan@example.com"),
    new User("李四", 30, "lisi@example.com"),
    new User("王五", 28, "wangwu@example.com")
);

// 自动批量插入（当数量 >= 2 时）
long insertCount = client.insert(users).executeRows();
System.out.println("批量插入成功，影响行数：" + insertCount);
```

#### 链式插入

```java
User user1 = new User("张三", 25, "zhangsan@example.com");
User user2 = new User("李四", 30, "lisi@example.com");

long insertCount = client.insert(user1)
        .insert(user2)
        .executeRows();
```

### 更新操作

#### 基本更新

```java
// 更新单个字段
long updateCount = client.update(User.class)
        .set(u -> u.setAge(26))
        .where(u -> u.getId().equals(1L))
        .executeRows();

// 更新多个字段
long updateCount = client.update(User.class)
        .set(u -> {
            u.setName("新名字");
            u.setAge(30);
            u.setEmail("newemail@example.com");
        })
        .where(u -> u.getId().equals(1L))
        .executeRows();
```

#### 条件更新

```java
// 批量更新
long updateCount = client.update(User.class)
        .set(u -> u.setAge(u.getAge() + 1))  // 年龄加1
        .where(u -> u.getAge() < 30)
        .executeRows();

// 连表更新
long updateCount = client.update(User.class)
        .leftJoin(Order.class, (u, o) -> u.getId().equals(o.getUserId()))
        .set((u, o) -> u.setLastOrderTime(o.getCreateTime()))
        .where((u, o) -> o.getStatus().equals("COMPLETED"))
        .executeRows();
```

### 删除操作

#### 基本删除

```java
// 根据ID删除
long deleteCount = client.delete(User.class)
        .where(u -> u.getId().equals(1L))
        .executeRows();

// 条件删除
long deleteCount = client.delete(User.class)
        .where(u -> u.getAge() < 18)
        .executeRows();
```

#### 连表删除

```java
// 删除没有订单的用户
long deleteCount = client.delete(User.class)
        .leftJoin(Order.class, (u, o) -> u.getId().equals(o.getUserId()))
        .where((u, o) -> o.getId() == null)
        .executeRows();

// 指定删除的表
long deleteCount = client.delete(User.class)
        .leftJoin(Order.class, (u, o) -> u.getId().equals(o.getUserId()))
        .selectDelete((u, o) -> o)  // 删除订单表的记录
        .where((u, o) -> u.getStatus().equals("INACTIVE"))
        .executeRows();
```

## 高级查询

### 分组查询

```java
// 简单分组
List<? extends Result> result = client.query(Order.class)
        .groupBy(o -> o.getUserId())
        .select(g -> new Result() {
            Long userId = g.key;
            Long orderCount = g.count();
            BigDecimal totalAmount = g.sum(o -> o.getAmount());
        })
        .toList();

// 多字段分组
List<? extends Result> result = client.query(Order.class)
        .groupBy(o -> new Grouper() {
            Long userId = o.getUserId();
            String status = o.getStatus();
        })
        .select(g -> new Result() {
            Long userId = g.key.userId;
            String status = g.key.status;
            Long count = g.count();
            BigDecimal avgAmount = g.avg(o -> o.getAmount());
        })
        .toList();
```

### Having 条件

```java
List<? extends Result> result = client.query(Order.class)
        .groupBy(o -> o.getUserId())
        .having(g -> g.count() > 5)  // 订单数量大于5的用户
        .select(g -> new Result() {
            Long userId = g.key;
            Long orderCount = g.count();
        })
        .toList();
```

### Include 关联查询

Include 是 Drink 的强大特性，可以自动处理实体间的关联关系。

#### 基本 Include

```java
// 查询用户及其订单
List<User> users = client.query(User.class)
        .includes(u -> u.getOrders())  // 自动加载订单
        .toList();

// 用户的订单列表会被自动填充
for (User user : users) {
    System.out.println("用户: " + user.getName());
    System.out.println("订单数量: " + user.getOrders().size());
}
```

#### 条件 Include

```java
// 只加载已完成的订单
List<User> users = client.query(User.class)
        .includes(u -> u.getOrders(), o -> o.getStatus().equals("COMPLETED"))
        .toList();

// 复杂条件 Include
List<User> users = client.query(User.class)
        .includesByCond(u -> u.getOrders(), query -> query
                .where(o -> o.getAmount().compareTo(new BigDecimal("100")) > 0)
                .orderBy(o -> o.getCreateTime(), false)
                .limit(5)
        )
        .toList();
```

#### 多层 Include

```java
// 查询用户 -> 订单 -> 订单项
List<User> users = client.query(User.class)
        .includes(u -> u.getOrders())
        .includes(u -> u.getOrders(), o -> o.getOrderItems())
        .toList();
```

### 子查询

```java
// 查询有订单的用户
List<User> users = client.query(User.class)
        .where(u -> client.query(Order.class)
                .where(o -> o.getUserId().equals(u.getId()))
                .exists())
        .toList();

// 查询订单数量最多的用户
List<User> users = client.query(User.class)
        .where(u -> client.query(Order.class)
                .where(o -> o.getUserId().equals(u.getId()))
                .count() == client.query(Order.class)
                        .groupBy(o -> o.getUserId())
                        .max(g -> g.count()))
        .toList();
```

### Union 查询

```java
// Union 查询
LQuery<User> query1 = client.query(User.class)
        .where(u -> u.getAge() > 30);

LQuery<User> query2 = client.query(User.class)
        .where(u -> u.getName().contains("管理员"));

List<User> users = client.union(query1, query2).toList();

// Union All
List<User> users = client.unionAll(query1, query2).toList();
```

## 事务管理

### 手动事务

```java
// 基本事务使用
try (Transaction transaction = client.beginTransaction()) {
    // 执行多个操作
    client.insert(user).executeRows();
    client.update(Order.class)
            .set(o -> o.setStatus("PROCESSING"))
            .where(o -> o.getUserId().equals(user.getId()))
            .executeRows();

    // 提交事务
    transaction.commit();
} catch (Exception e) {
    // 异常时自动回滚
    e.printStackTrace();
}
```

### 指定事务隔离级别

```java
import java.sql.Connection;

try (Transaction transaction = client.beginTransaction(Connection.TRANSACTION_READ_COMMITTED)) {
    // 在指定隔离级别下执行操作
    // ...
    transaction.commit();
}
```

### Spring 事务集成

在 Spring Boot 项目中，可以使用 `@Transactional` 注解：

```java
@Service
public class UserService {

    @Autowired
    private DrinkClient client;

    @Transactional
    public void createUserWithOrder(User user, Order order) {
        // 插入用户
        client.insert(user).executeRows();

        // 设置订单的用户ID
        order.setUserId(user.getId());

        // 插入订单
        client.insert(order).executeRows();

        // 如果发生异常，整个事务会自动回滚
    }

    @Transactional(readOnly = true)
    public List<User> getActiveUsers() {
        return client.query(User.class)
                .where(u -> u.getStatus().equals("ACTIVE"))
                .toList();
    }
}
```

## 配置选项

### 数据库支持

Drink 支持以下数据库：

- **MySQL** - 完全支持
- **PostgreSQL** - 完全支持
- **SQL Server** - 完全支持
- **Oracle** - 完全支持
- **H2** - 完全支持
- **SQLite** - 完全支持

### 配置参数

#### Spring Boot 配置

```yaml
drink:
  database: mysql              # 数据库类型
  print-sql: true             # 是否打印SQL语句
  print-batch: false          # 是否打印批量操作信息
  name-conversion: snake_case # 命名转换策略
  ignore-update-no-where: false # 是否忽略无WHERE条件的UPDATE
  ignore-delete-no-where: false # 是否忽略无WHERE条件的DELETE
```

#### 命名转换策略

```java
// 驼峰命名转下划线
.setNameConverter(new SnakeNameConverter())

// 保持原样
.setNameConverter(new NoConverter())

// 自定义转换器
.setNameConverter(new NameConverter() {
    @Override
    public String convertTableName(String entityName) {
        return "t_" + entityName.toLowerCase();
    }

    @Override
    public String convertColumnName(String fieldName) {
        return fieldName.toUpperCase();
    }
})
```

### SQL 函数支持

Drink 内置了丰富的 SQL 函数支持：

#### 时间函数

```java
// 当前时间
LocalDateTime now = SqlFunctions.now();
LocalDate today = SqlFunctions.nowDate();

// 日期计算
LocalDate futureDate = SqlFunctions.addDate(LocalDate.now(), 30, DateUnit.DAY);
long daysDiff = SqlFunctions.dateTimeDiff(date1, date2, DateUnit.DAY);

// 日期格式化
String formatted = SqlFunctions.dateFormat(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss");
```

#### 字符串函数

```java
// 字符串操作
String concat = SqlFunctions.concat("Hello", " ", "World");
String upper = SqlFunctions.toUpperCase("hello");
String sub = SqlFunctions.subString("Hello World", 0, 5);
int length = SqlFunctions.length("Hello");
```

#### 数学函数

```java
// 数学计算
double abs = SqlFunctions.abs(-10.5);
double round = SqlFunctions.round(3.14159, 2);
double max = SqlFunctions.max(value1, value2, value3);
```

#### 条件函数

```java
// 条件判断
String result = SqlFunctions.If(condition, "真值", "假值");
String notNull = SqlFunctions.ifNull(nullableValue, "默认值");
```

## 最佳实践

### 1. 实体设计

```java
@Data
@Table("users")
public class User {
    // 主键使用 Long 类型
    @Column(value = "user_id", primaryKey = true)
    private Long id;

    // 必填字段标记 notNull
    @Column(value = "user_name", notNull = true)
    private String name;

    // 自增字段标记 generatedKey
    @Column(value = "created_at", generatedKey = true)
    private LocalDateTime createTime;

    // 使用合适的数据类型
    private BigDecimal balance;  // 金额使用 BigDecimal
    private LocalDate birthDate; // 日期使用 LocalDate
}
```

### 2. 查询优化

```java
// ✅ 好的做法：使用索引字段查询
List<User> users = client.query(User.class)
        .where(u -> u.getId().equals(userId))  // 主键查询
        .toList();

// ✅ 好的做法：限制查询结果数量
List<User> users = client.query(User.class)
        .orderBy(u -> u.getId())
        .limit(100)  // 限制结果集大小
        .toList();

// ❌ 避免：全表扫描
List<User> users = client.query(User.class)
        .where(u -> u.getName().contains("张"))  // 可能导致全表扫描
        .toList();
```

### 3. 批量操作

```java
// ✅ 好的做法：使用批量插入
List<User> users = Arrays.asList(user1, user2, user3);
client.insert(users).executeRows();  // 自动批量执行

// ✅ 好的做法：批量更新
client.update(User.class)
        .set(u -> u.setStatus("ACTIVE"))
        .where(u -> u.getCreateTime().isAfter(yesterday))
        .executeRows();
```

### 4. 事务管理

```java
// ✅ 好的做法：使用 try-with-resources
try (Transaction transaction = client.beginTransaction()) {
    // 执行多个相关操作
    client.insert(user).executeRows();
    client.insert(order).executeRows();

    transaction.commit();
} catch (Exception e) {
    // 异常自动回滚
    log.error("事务执行失败", e);
}
```

### 5. Include 使用

```java
// ✅ 好的做法：按需加载关联数据
List<User> users = client.query(User.class)
        .includes(u -> u.getOrders(), o -> o.getStatus().equals("ACTIVE"))
        .limit(10)
        .toList();

// ❌ 避免：无条件加载大量关联数据
List<User> users = client.query(User.class)
        .includes(u -> u.getOrders())  // 可能加载大量数据
        .toList();
```

## 常见问题

### Q1: 如何处理枚举类型？

```java
// 方式1：使用类型处理器
@UseTypeHandler(GenderHandler.class)
private Gender gender;

// 方式2：使用转换器
@Column(converter = GenderConverter.class)
private Gender gender;

// 自定义转换器
public class GenderConverter implements IConverter<Gender, String> {
    @Override
    public String toDb(Gender gender) {
        return gender == null ? null : gender.name();
    }

    @Override
    public Gender fromDb(String value) {
        return value == null ? null : Gender.valueOf(value);
    }
}
```

### Q2: 如何处理 JSON 字段？

```java
@Column("extra_info")
@UseTypeHandler(JsonTypeHandler.class)
private Map<String, Object> extraInfo;

// 自定义 JSON 处理器
public class JsonTypeHandler implements ITypeHandler<Map<String, Object>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setParameter(PreparedStatement ps, int i, Map<String, Object> parameter) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, Types.VARCHAR);
        } else {
            try {
                ps.setString(i, objectMapper.writeValueAsString(parameter));
            } catch (JsonProcessingException e) {
                throw new SQLException(e);
            }
        }
    }

    @Override
    public Map<String, Object> getResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new SQLException(e);
        }
    }
}
```

### Q3: 如何实现软删除？

```java
@Data
@Table("users")
public class User {
    @Column(value = "user_id", primaryKey = true)
    private Long id;

    private String name;

    @Column("is_deleted")
    private Boolean deleted = false;

    @Column("delete_time")
    private LocalDateTime deleteTime;
}

// 查询时自动过滤已删除记录
List<User> users = client.query(User.class)
        .where(u -> u.getDeleted() == false)
        .toList();

// 软删除
client.update(User.class)
        .set(u -> {
            u.setDeleted(true);
            u.setDeleteTime(LocalDateTime.now());
        })
        .where(u -> u.getId().equals(userId))
        .executeRows();
```

### Q4: 如何处理分页查询？

```java
// 基本分页
public PageResult<User> getUsers(int page, int size) {
    // 查询总数
    long total = client.query(User.class).count();

    // 查询数据
    List<User> users = client.query(User.class)
            .orderBy(u -> u.getId())
            .limit(page * size, size)
            .toList();

    return new PageResult<>(users, total, page, size);
}

// 分页结果类
@Data
public class PageResult<T> {
    private List<T> data;
    private long total;
    private int page;
    private int size;
    private int totalPages;

    public PageResult(List<T> data, long total, int page, int size) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.size = size;
        this.totalPages = (int) Math.ceil((double) total / size);
    }
}
```

### Q5: 如何处理动态查询条件？

```java
public List<User> searchUsers(UserSearchCriteria criteria) {
    LQuery<User> query = client.query(User.class);

    // 动态添加条件
    if (criteria.getName() != null) {
        query = query.where(u -> u.getName().contains(criteria.getName()));
    }

    if (criteria.getMinAge() != null) {
        query = query.where(u -> u.getAge() >= criteria.getMinAge());
    }

    if (criteria.getMaxAge() != null) {
        query = query.where(u -> u.getAge() <= criteria.getMaxAge());
    }

    if (criteria.getStatus() != null) {
        query = query.where(u -> u.getStatus().equals(criteria.getStatus()));
    }

    return query.orderBy(u -> u.getId()).toList();
}
```

### Q6: 如何处理复杂的统计查询？

```java
// 用户订单统计
List<? extends Result> stats = client.query(User.class)
        .leftJoin(Order.class, (u, o) -> u.getId().equals(o.getUserId()))
        .groupBy((u, o) -> new Grouper() {
            Long userId = u.getId();
            String userName = u.getName();
        })
        .select(g -> new Result() {
            Long userId = g.key.userId;
            String userName = g.key.userName;
            Long orderCount = g.count((u, o) -> o.getId());
            BigDecimal totalAmount = g.sum((u, o) -> o.getAmount());
            BigDecimal avgAmount = g.avg((u, o) -> o.getAmount());
            LocalDateTime lastOrderTime = g.max((u, o) -> o.getCreateTime());
        })
        .toList();
```

### Q7: 如何优化性能？

1. **使用索引字段查询**
```java
// ✅ 使用主键或索引字段
.where(u -> u.getId().equals(userId))

// ❌ 避免在非索引字段上使用 LIKE
.where(u -> u.getDescription().contains("关键词"))
```

2. **限制结果集大小**
```java
// 总是使用 limit 限制结果数量
.limit(100)
```

3. **按需加载关联数据**
```java
// 只加载需要的关联数据
.includes(u -> u.getOrders(), o -> o.getStatus().equals("ACTIVE"))
```

4. **使用批量操作**
```java
// 批量插入而不是循环单条插入
client.insert(userList).executeRows();
```

### Q8: 如何处理数据库连接池配置？

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20        # 最大连接数
      minimum-idle: 5              # 最小空闲连接数
      connection-timeout: 30000    # 连接超时时间(毫秒)
      idle-timeout: 600000         # 空闲超时时间(毫秒)
      max-lifetime: 1800000        # 连接最大生存时间(毫秒)
      leak-detection-threshold: 60000 # 连接泄漏检测阈值(毫秒)
```

---

## 总结

Drink ORM 提供了强大而灵活的数据库操作能力，通过类型安全的 Lambda 表达式查询，让数据库操作变得更加直观和安全。无论是简单的 CRUD 操作，还是复杂的关联查询和事务管理，Drink 都能提供优雅的解决方案。

希望这份文档能帮助您快速上手并充分利用 Drink ORM 的强大功能。如有问题，欢迎加入 QQ 群：257911716 进行交流讨论。

```java

@Data
@Table("employees")
public class Employee
{
    //员工id
    @Column("emp_no")
    private int number;
    //生日
    @Column("birth_date")
    private LocalDate birthDay;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    //性别
    @Column(converter = GenderConverter.class)
    private Gender gender;
    //入职日期
    @Column("hire_date")
    private LocalDate hireDay;
}
```

根据id获得员工对象

```java
public class DisplayTest extends BaseTest
{
    public void d1()
    {
        int id = 10001;
        List<Employee> list = client.query(Employee.class) // FROM `employees` AS t0
                .where(e -> e.getNumber() == id) // WHERE t0.`emp_no` = ?
                // 因为没有select，默认选择了全字段
                // SELECT t0.`birth_date`,t0.`first_name`,t0.`last_name`,t0.`emp_no`,t0.`hire_date`,t0.`gender`
                .toList();
    }
}
```

对应的sql

```mysql
SELECT t0.`birth_date`, t0.`first_name`, t0.`last_name`, t0.`emp_no`, t0.`hire_date`, t0.`gender`
FROM `employees` AS t0
WHERE t0.`emp_no` = ?
```

根据firstName和性别获得员工对象

```java
public class DisplayTest extends BaseTest
{
    public void d2()
    {
        List<Employee> list = client.query(Employee.class) // FROM `employees` AS t0
                .where(e -> e.getGender() == Gender.F && e.getFirstName() == "lady") // WHERE t0.`gender` = ? AND t0.`first_name` = ?
                // 因为没有select，默认选择了全字段
                // SELECT t0.`birth_date`, t0.`first_name`, t0.`last_name`, t0.`emp_no`, t0.`hire_date`, t0.`gender`
                .toList();
    }
}
```

对应的sql

```mysql
SELECT t0.`birth_date`, t0.`first_name`, t0.`last_name`, t0.`emp_no`, t0.`hire_date`, t0.`gender`
FROM `employees` AS t0
WHERE t0.`gender` = ?
  AND t0.`first_name` = ?
```

假设我们还有一张员工薪资历史表

```java

@Data
@Table("salaries")
public class Salary
{
    //员工id
    @Column("emp_no")
    private int empNumber;
    //薪资
    private int salary;
    //何时开始的
    @Column("from_date")
    private LocalDate from;
    //何时为止的
    @Column("to_date")
    private LocalDate to;
}
```

查询一个员工的姓名和历史最高薪资和平均薪资

```java
public class DisplayTest extends BaseTest
{
    public void d3()
    {
        int id = 10001;
        List<? extends Result> list = client.query(Employee.class) // FROM `employees` AS t0
                .leftJoin(Salary.class, (e, s) -> e.getNumber() == s.getEmpNumber()) // LEFT JOIN `salaries` AS t1 ON t0.`emp_no` = t1.`emp_no`
                .where((e, s) -> e.getNumber() == id) // WHERE t0.`emp_no` = ?
                // SELECT
                .select((e, s) -> new Result()
                {
                    // CONCAT(t0.`first_name`, ?, t0.`last_name`) AS `name`
                    String name = SqlFunctions.concat(e.getFirstName(), " ", e.getLastName());
                    // MAX(t1.`salary`)                           AS `maxSalary`,
                    int maxSalary = SqlFunctions.max(s.getSalary());
                    // AVG(t1.`salary`)                           AS `avgSalary`
                    BigDecimal avgSalary = SqlFunctions.avg(s.getSalary());
                })
                .toList();
    }
}
```

对应的sql

```mysql
SELECT CONCAT(t0.`first_name`, ?, t0.`last_name`) AS `name`,
       MAX(t1.`salary`)                           AS `maxSalary`,
       AVG(t1.`salary`)                           AS `avgSalary`
FROM `employees` AS t0
         LEFT JOIN `salaries` AS t1 ON t0.`emp_no` = t1.`emp_no`
WHERE t0.`emp_no` = ?
```

假设我们还有部门表和员工部门中间表

```java

@Table("departments")
@Data
public class Department
{
    // 部门编号
    @Column("dept_no")
    private String number;
    // 部门名称
    @Column("dept_name")
    private String name;
}
```

```java

@Data
@Table("dept_emp")
public class DeptEmp
{
    // 员工编号
    @Column("emp_no")
    private int empNumber;
    // 部门编号
    @Column("dept_no")
    private String deptNumber;
    // 什么时候加入的
    @Column("from_date")
    private LocalDate from;
    // 什么时候离开的
    @Column("to_date")
    private LocalDate to;
}
```

查询某部门的员工的平均薪水

```java
public class DisplayTest extends BaseTest
{
    public void d4()
    {
        String departmentId = "d009";

        List<? extends Result> list = client.query(DeptEmp.class) // FROM `dept_emp` AS t0
                .innerJoin(Salary.class, (de, s) -> de.getEmpNumber() == s.getEmpNumber()) // INNER JOIN `salaries` AS t1 ON t0.`emp_no` = t1.`emp_no`
                .innerJoin(Department.class, (de, s, d) -> de.getDeptNumber() == d.getNumber()) // INNER JOIN `departments` AS t2 ON t0.`dept_no` = t2.`dept_no`
                .where((de, s, d) -> de.getDeptNumber() == departmentId && s.getTo() == LocalDate.of(9999, 1, 1)) // WHERE t0.`dept_no` = ? AND t1.`to_date` = ?
                // GROUP BY
                .groupBy((de, s, d) -> new Grouper()
                {
                    // t0.`dept_no`,
                    String id = de.getDeptNumber();
                    // t2.`dept_name`
                    String name = d.getName();
                })
                // SELECT
                .select(g -> new Result()
                {
                    // t0.`dept_no` AS `deptId`
                    String deptId = g.key.id;
                    // t2.`dept_name` AS `deptName`
                    String deptName = g.key.name;
                    // AVG(t1.`salary`) AS `avgSalary`
                    BigDecimal avgSalary = g.avg((de, s, d) -> s.getSalary());
                })
                .toList();
    }
}
```

对应的sql

```mysql
SELECT t0.`dept_no` AS `deptId`, t2.`dept_name` AS `deptName`, AVG(t1.`salary`) AS `avgSalary`
FROM `dept_emp` AS t0
         INNER JOIN `salaries` AS t1 ON t0.`emp_no` = t1.`emp_no`
         INNER JOIN `departments` AS t2 ON t0.`dept_no` = t2.`dept_no`
WHERE t0.`dept_no` = ?
  AND t1.`to_date` = ?
GROUP BY t0.`dept_no`, t2.`dept_name`
```

### 新增

新增由client对象的insert方法发起，insert方法接收一个或多个数据库表对应的对象，
返回一个新增过程对象,可以对这个新增过程对象后续进行insert方法添加更多数据

| 方法          | 参数           | 返回      | 说明             |
|-------------|--------------|---------|----------------|
| insert      | 同类型单个对象或对象集合 | this    | 添加更多需要传入数据库的对象 |
| executeRows |              | 执行成功的数量 | 执行insert       |

> 注意：insert根据数量自动选择批量执行（数量>=2）

Department表新增一个数据

```java
public class DisplayTest extends BaseTest
{
    public void i1()
    {
        Department department = new Department();
        department.setNumber("101");
        department.setName("ddd");

        client.insert(department).executeRows();
    }
}
```

新增多个数据

```java
public class DisplayTest extends BaseTest
{
    public void i2()
    {
        Department department1 = new Department();
        department1.setNumber("101");
        department1.setName("ddd");
        Department department2 = new Department();
        department2.setNumber("102");
        department2.setName("eee");
        Department department3 = new Department();
        department3.setNumber("103");
        department3.setName("fff");

        List<Department> list = Arrays.asList(department1, department2, department3);

        client.insert(list).executeRows();
    }
}
```

新增任意数据

```java
public class DisplayTest extends BaseTest
{
    public void i3()
    {
        Department d = new Department();

        Department department1 = new Department();
        department1.setNumber("101");
        department1.setName("ddd");
        Department department2 = new Department();
        department2.setNumber("102");
        department2.setName("eee");
        Department department3 = new Department();
        department3.setNumber("103");
        department3.setName("fff");

        List<Department> ds = Arrays.asList(department1, department2, department3);

        client.insert(d).insert(ds).executeRows();
    }
}
```

### 更新

更新由client对象的update方法发起，update方法接收一个class对象，返回一个更新过程对象，
可以对这个对象后续进行`set`设置数据和`where`限制更新范围等操作

| 方法                   | 参数               | 返回       | 说明                       |
|----------------------|------------------|----------|--------------------------|
| left/right/innerJoin | 同查询过程对象的leftJoin | 新的更新过程对象 | 用于连表更新，操作方式与查询时的join方法一致 |
| set                  | lambda表达式        | this     | 设置更新数据的lambda表达式         |
| where                | 同查询过程对象的where    | this     | 同查询过程对象的where            |

> 警告：进行无where限制下的update时默认会报错，需要手开启无视无where限制

为Department表更新数据

```java
public class UpdateTest extends BaseTest
{
    public void display0()
    {
        long l2 = client.update(Department.class)
                .set(s -> s.setName("newName"))
                .where(w -> w.getNumber() == "100")
                .executeRows();
    }
}
```

对应sql

```mysql
UPDATE `departments` AS t0
SET t0.`dept_name` = ?
WHERE t0.`dept_no` = ?
```

连表更新

```java

@SuppressWarnings("all")
public class UpdateTest extends BaseTest
{
    long l = client.update(Department.class)
            .leftJoin(DeptEmp.class, (a, b) -> a.getNumber() == b.getDeptNumber())
            .set((a, b) -> a.setName(b.getDeptNumber()))
            .where((a, b) -> 1 == 1)
            .executeRows();
}
```

对应sql

```mysql
UPDATE `departments` AS t0 LEFT JOIN `dept_emp` AS t1 ON t0.`dept_no` = t1.`dept_no`
SET t0.`dept_name` = t1.`dept_no`
WHERE ? = ?
```

### 删除

删除由client对象的delete方法发起，delete方法接收一个class对象，返回一个删除过程对象，
可以对这个对象后续进行`where`限制更新范围等操作

| 方法                   | 参数                      | 返回       | 说明                                                |
|----------------------|-------------------------|----------|---------------------------------------------------|
| left/right/innerJoin | 同查询过程对象的leftJoin        | 新的删除过程对象 | 用于连表删除，操作方式与查询时的join方法一致                          |
| where                | 同查询过程对象的where           | this     | 同查询过程对象的where                                     |
| selectDelete         | 返回需要删除的目标表的对象的lambda表达式 | this     | join后连表删除时可以使用，用于指定需要删除的表，可以通过多次调用增加目标（无调用默认选择全部） |

> 警告：进行无where限制下的delete时默认会报错，需要手开启无视无where限制

为Department表删除数据

```java
public class DeleteTest extends BaseTest
{
    @Test
    public void d1()
    {
        long executeRows = client.delete(Department.class)
                .where(w -> w.getNumber() == "10009")
                .executeRows();
    }
}
```

对应sql

```mysql
DELETE
FROM `departments` AS t0
WHERE t0.`dept_no` = ?
```

连表删除

```java
public class DeleteTest extends BaseTest
{
    @Test
    public void d2()
    {
        String sql = client.delete(Department.class)
                .leftJoin(DeptEmp.class, (d, dm) -> d.getNumber() == dm.getDeptNumber())
                .where((d, dm) -> d.getNumber() == "d009")
//                .selectDeleteTable((d, dm) -> d)
                .selectDelete((d, dm) -> dm)
                .toSql();
        System.out.println(sql);
    }
}
```

对应sql

```mysql
DELETE t1
FROM `departments` AS t0
         LEFT JOIN `dept_emp` AS t1 ON t0.`dept_no` = t1.`dept_no`
WHERE t0.`dept_no` = ?
```

## 关联查询 INCLUDE

假设我们有一个工资类和一个员工类，员工类配置了对工资类的关联信息，员工与工资是一对多关系（一个员工有多个工资信息）

```java

@Data
@Table(value = "salaries")
public class Salary
{
    @Column(value = "emp_no", primaryKey = true)
    private int empNumber;
    private int salary;
    @Column("from_date")
    private LocalDate from;
    @Column("to_date")
    private LocalDate to;
}

@Data
@Table("employees")
public class Employee
{
    @Column(value = "emp_no", primaryKey = true)
    private int number;
    @Column("birth_date")
    private LocalDate birthDay;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column(converter = GenderConverter.class)
    private Gender gender;
    @Column("hire_date")
    private LocalDate hireDay;
    // 一对多，self为自身的number字段，target为Salary的empNumber字段
    @Navigate(value = RelationType.OneToMany, self = "number", target = "empNumber")
    private List<Salary> salaries;
}
```

现在我们就可以填充指定的员工的工资信息

```java
public class IncludeTest extends BaseTest
{
    @Test
    public void oneManyTest()
    {
        //获取编号为10001的员工并且查询出该员工的所有工资信息
        Employee employee = client.query(Employee.class)
                .where(e -> e.getNumber() == 10001)
                .includes(e -> e.getSalaries())
                .first();

        Assert.assertEquals(17, employee.getSalaries().size());
    }
}
```

我们也可以对这个查询做出限制

```java
public class IncludeTest extends BaseTest
{
    @Test
    public void oneManyCondTest()
    {
        //获取编号为10001的员工并且查询出该员工最后一次调整工资（9999-01-01）以外的历史工资
        Employee employee = client.query(Employee.class)
                .where(e -> e.getNumber() == 10001)
                .includes(e -> e.getSalaries(), s -> s.getTo().isBefore(LocalDate.of(9999, 1, 1)))
                .first();

        Assert.assertEquals(16, employee.getSalaries().size());
    }
}
```

也支持更复杂的限制条件，比方说限制关联查询获取的条目数

```java
public class IncludeTest extends BaseTest
{
    @Test
    public void oneManyCond2Test()
    {
        //获取编号为10001的员工并且查询出该员工最后一次调整工资（9999-01-01）以外的历史工资
        //同时只获取前10条
        //并且按工资排序
        Employee employee = client.query(Employee.class)
                .includesByCond(e -> e.getSalaries(), query -> query
                        .orderBy(s -> s.getSalary())
                        .where(s -> s.getTo().isBefore(LocalDate.of(9999, 1, 1)))
                        .limit(10)
                )
                .first();

        Assert.assertEquals(10, employee.getSalaries().size());
    }
}
```

## 支持的sql函数

框架内部支持了绝大多数的常用sql函数，具体逻辑可以在SqlFunctions类中查看

`时间相关`

| 函数名            | 参数 | 返回类型                    | 功能                   |
|----------------|----|-------------------------|----------------------|
| now            |    | LocalDateTime           | 获取当前的日期时间            |
| utcNow         |    | LocalDateTime           | 获取当前的utc日期时间         |
| systemNow      |    | LocalDateTime           | 获取当前的系统日期时间          |
| nowDate        |    | LocalDate               | 获取当前的日期              |
| nowTime        |    | LocalTime               | 获取当前的时间              |
| utcNowDate     |    | LocalDate               | 获取当前的utc日期           |
| utcNowTime     |    | LocalTime               | 获取当前的utc时间           |
| addData        |    | LocalDate/LocalDateTime | 日期或日期时间增加指定的单位长度     |
| subDate        |    | LocalDate/LocalDateTime | 日期或日期时间减去指定的单位长度     |
| dateTimeDiff   |    | long                    | 获取两个日期或日期时间相差的指定单位的值 |
| dateFormat     |    | String                  | 格式化日期或日期时间           |
| getYear        |    | int                     | 提取年份                 |
| getMonth       |    | int                     | 提取月份                 |
| getWeek        |    | int                     | 提取周                  |
| getDay         |    | int                     | 提取日                  |
| getHour        |    | int                     | 提取小时                 |
| getMinute      |    | int                     | 提取日                  |
| getSecond      |    | int                     | 提取秒                  |
| getMilliSecond |    | int                     | 提取毫秒                 |
| getDayName     |    | String                  | 获取指定日期在本周周几的全名       |
| getDayOfWeek   |    | int                     | 获取指定日期在本周周几          |
| getDayOfYear   |    | int                     | 获取指定日期是当年的第几天        |
| dateToDays     |    | int                     | 从0000-01-01到指定日期的天数  |
| getLastDay     |    | LocalDate               | 获取本月最后一天的日期          |
| getMonthName   |    | String                  | 获取指定日期的月份名称          |
| getQuarter     |    | int                     | 获取指定日期在第几季度          |
| getWeekDay     |    | int                     | 获取指定日期在本周周几的索引       |
| getWeekOfYear  |    | int                     | 获取本周是今年的第几周          |

`数值相关`

| 函数名      | 参数           | 返回类型   | 功能                        |
|----------|--------------|--------|---------------------------|
| abs      |              | 同入参    |                           |
| cos      |              | double |                           |
| acos     |              | double |                           |
| sin      |              | double |                           |
| asin     |              | double |                           |
| tan      |              | double |                           |
| atan     |              | double |                           |
| atan2    |              | double |                           |
| ceil     |              | int    | 向上取整到最近的整数                |
| floor    |              | int    | 向下取整到最近的整数                |
| cot      |              | double | 余切函数                      |
| degrees  |              | double | 弧度转角度                     |
| radians  |              | double | 角度转弧度                     |
| exp      |              | double |                           |
| big      |              | 同入参    | 获取所有数值中最大的数值              |
| small    |              | 同入参    | 获取所有数值中最小的数值              |
| ln       |              | double |                           |
| log      |              | double |                           |
| log2     |              | double |                           |
| log10    |              | double |                           |
| mod      |              | 同入参    | 取模                        |
| pi       |              | double | 获取PI                      |
| pow      |              | double |                           |
| random   |              | double | 获取0-1的随机小数                |
| round    | (T a)        | int    | 四舍五入取整                    |
| round    | (T a, int b) | 同入参    | 指定截取多少位小数四舍五入取整           |
| sign     |              | int    | 参数为正数、负数和零时分别返回 1, -1 和 0 |
| sqrt     |              | double | 获取平方根                     |
| truncate | (T a)        | int    | 截断所有小数位                   |
| truncate | (T a, int b) | double | 截断指定位数的小数位                |

`字符串相关`

| 函数名          | 参数                                         | 返回类型   | 功能                            |
|--------------|--------------------------------------------|--------|-------------------------------|
| strToAscii   |                                            | int    | 第一个字符的ASCII码                  |
| asciiToStr   |                                            | String | ASCII码转字符串                    |
| length       |                                            | int    | 字符串的长度                        |
| byteLength   |                                            | int    | 字符串的字节长度                      |
| concat       |                                            | String | 拼接字符串                         |
| join         |                                            | String | 根据插值拼接字符串                     |
| numberFormat |                                            | String | 格式化数值                         |
| indexOf      | (String str, String subStr)                | int    | 返回一个字符串中指定子字符串的位置             |
| indexOf      | (String str, String subStr, int offset)    | int    | 返回一个字符串中指定子字符串的位置,并且指定起始搜索的位置 |
| toLowerCase  |                                            | String | 转小写                           |
| toUpperCase  |                                            | String | 转大写                           |
| left         |                                            | String | 返回具有指定长度的字符串的左边部分             |
| right        |                                            | String | 返回具有指定长度的字符串的右边部分             |
| leftPad      |                                            | String | 从左边开始对字符串进行重复填充，直到满足指定的长度     |
| rightPad     |                                            | String | 从右边开始对字符串进行重复填充，直到满足指定的长度     |
| trimStart    |                                            | String | 去除字符串左侧的空格                    |
| trimEnd      |                                            | String | 去除字符串右侧的空格                    |
| trim         |                                            | String | 去除字符串左侧和右侧的空格                 |
| replace      |                                            | String | 替换字符串中指定的字符为新字符               |
| reverse      |                                            | String | 反转字符串                         |
| compare      |                                            | int    | 比较字符串                         |
| subString    | (String str, int beginIndex)               | String | 获取子字符串                        |
| subString    | (String str, int beginIndex, int endIndex) | String | 获取子字符串                        |

`其他`

| 函数名       | 参数                                           | 返回类型 | 功能                                                                      |
|-----------|----------------------------------------------|------|-------------------------------------------------------------------------|
| If        | (boolean condition, T truePart, T falsePart) | T    | 如果condition为true则返回truePart，否则返回falsePart                               |
| ifNull    | (T valueNotNull, T valueIsNull)              | T    | 如果valueNotNull为null则返回valueIsNull，否则返回如果valueNotNull为null则返回valueIsNull |
| nullIf    | (T t1, T t2)                                 | T    | 如果t1 = t2则返回null，否则返回t1                                                 |
| cast      | (Object value, Class\<T> targetType)         | T    | 转换到指定类型                                                                 |
| cast      | (Object value, SqlTypes\<T> targetType)      | T    | 转换到指定类型                                                                 |
| isNull    | (T t)                                        | T    | isNull的快捷方法                                                             |
| isNotNull | (T t)                                        | T    | isNotNull的快捷方法                                                          |****

## java函数到sql表达式的映射

> 以下仅列举映射到mysql的情况，实际会根据数据库类型来决定策略

`String类`

| java                                | sql                              |
|-------------------------------------|----------------------------------|
| this.contains(arg)                  | this LIKE CONCAT('%',arg,'%')    |
| this.startsWith(arg)                | this LIKE CONCAT(arg,'%')        |
| this.endsWith(arg)                  | this LIKE CONCAT('%',arg)        |
| this.length()                       | CHAR_LENGTH(this)                |
| this.toUpperCase()                  | UPPER(this)                      |
| this.toLowerCase()                  | LOWER(this)                      |
| this.concat(arg)                    | CONCAT(this,arg)                 |
| this.trim()                         | TRIM(this)                       |
| this.isEmpty()                      | (CHAR_LENGTH(this) = 0)          |
| this.indexOf(subStr)                | INSTR(this,subStr)               |
| this.indexOf(subStr,fromIndex)      | LOCATE(subStr,this,fromIndex)    |
| this.replace(oldStr,newStr)         | REPLACE(this,oldStr,newStr)      |
| this.substring(beginIndex)          | SUBSTR(this,beginIndex)          |
| this.substring(beginIndex,endIndex) | SUBSTR(this,beginIndex,endIndex) |
| String.join(delimiter,elements...)  | CONCAT_WS(delimiter,elements...) |

`Math类`

| java                | sql              |
|---------------------|------------------|
| Math.abs(arg)       | ABS(arg)         |
| Math.cos(arg)       | COS(arg)         |
| Math.acos(arg)      | ACOS(arg)        |
| Math.sin(arg)       | SIN(arg)         |
| Math.asin(arg)      | ASIN(arg)        |
| Math.tab(arg)       | TAN(arg)         |
| Math.atan(arg)      | ATAN(arg)        |
| Math.atan2(arg)     | ATAN2(arg)       |
| Math.toDegrees(arg) | DEGREES(arg)     |
| Math.toRadians(arg) | RADIANS(arg)     |
| Math.exp(arg)       | EXP(arg)         |
| Math.floor(arg)     | FLOOR(arg)       |
| Math.log(arg)       | LN(arg)          |
| Math.log10(arg)     | LOG10(arg)       |
| Math.random()       | RAND()           |
| Math.round(arg)     | ROUND(arg)       |
| Math.pow(arg1,arg2) | POWER(arg1,arg2) |
| Math.signum(arg)    | SIGN(arg)        |
| Math.sqrt(arg)      | SQRT(arg)        |

`List接口`

| java               | sql             |
|--------------------|-----------------|
| this.contains(arg) | arg IN (this,,) |

`BigDecimal类`

| java                | sql        |
|---------------------|------------|
| this.add(arg)       | this + arg |
| this.subtract(arg)  | this - arg |
| this.multiply(arg)  | this * arg |
| this.divide(arg)    | this / arg |
| this.remainder(arg) | this % arg |

`Temporal接口`

> LocalDate,LocalDateTime,LocalTime

| java               | sql        |
|--------------------|------------|
| this.isAfter(arg)  | this > arg |
| this.isBefore(arg) | this < arg |
| this.isEqual(arg)  | this = arg |
