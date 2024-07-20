## 如何引入

### 从零开始构建的场合

1. 引入maven并且进行配置

   ```xml
   <dependencies>
           <!--需要引入的依赖-->
           <dependency>
               <groupId>io.github.kiryu1223</groupId>
               <artifactId>drink-core</artifactId>
               <version>${project.version}</version>
           </dependency>

           <!--需要用户自己提供一个日志实现-->
           <dependency>
               <groupId>ch.qos.logback</groupId>
               <artifactId>logback-classic</artifactId>
               <version>1.4.12</version>
           </dependency>

           <!--数据库-->
           <dependency>
               <groupId>com.mysql</groupId>
               <artifactId>mysql-connector-j</artifactId>
               <version>9.0.0</version>
           </dependency>

           <!--数据源-->
           <dependency>
               <groupId>com.zaxxer</groupId>
               <artifactId>HikariCP</artifactId>
               <version>4.0.3</version>
           </dependency>
       </dependencies>

       <build>
           <plugins>
               <plugin>
                   <groupId>org.apache.maven.plugins</groupId>
                   <artifactId>maven-compiler-plugin</artifactId>
                   <version>3.8.1</version>
                   <configuration>
                       <!--开启apt的指令-->
                       <compilerArgs>
                           <arg>-Xplugin:ExpressionTree</arg>
                       </compilerArgs>
                       <annotationProcessorPaths>
                           <!--apt路径配置-->
                           <path>
                               <groupId>io.github.kiryu1223</groupId>
                               <artifactId>drink-core</artifactId>
                               <version>${project.version}</version>
                           </path>
                           <!--你的剩余apt路径配置，假设你的项目中还依赖了lombok等apt的话-->
                           <!--<path>-->
                           <!--    <groupId>org.projectlombok</groupId>-->
                           <!--    <artifactId>lombok</artifactId>-->
                           <!--    <version>1.18.34</version>-->
                           <!--</path>-->
                       </annotationProcessorPaths>
                   </configuration>
               </plugin>
           </plugins>
       </build>
   ```
2. 配置完成后进入main

   ```java
   package io.github.kiryu1223;

   import com.zaxxer.hikari.HikariDataSource;
   import io.github.kiryu1223.drink.Drink;
   import io.github.kiryu1223.drink.api.client.DrinkClient;
   import io.github.kiryu1223.drink.api.crud.transaction.DefaultTransactionManager;
   import io.github.kiryu1223.drink.api.crud.transaction.TransactionManager;
   import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
   import io.github.kiryu1223.drink.core.dataSource.DefaultDataSourceManager;
   import io.github.kiryu1223.drink.core.session.DefaultSqlSessionFactory;
   import io.github.kiryu1223.drink.core.session.SqlSessionFactory;

   public class Main
   {
       public static void main(String[] args)
       {
           // 配置一个数据源
           HikariDataSource dataSource = new HikariDataSource();
           dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true");
           dataSource.setUsername("root");
           dataSource.setPassword("root");
           dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

           // 获取一个DrinkClient对象，所有的CRUD都通过他完成
           DataSourceManager dataSourceManager = new DefaultDataSourceManager(dataSource);
           TransactionManager transactionManager = new DefaultTransactionManager(dataSourceManager);
           SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(dataSourceManager, transactionManager);

           Option option = new Option();
           option.setPrintSql(true);

           DrinkClient client = Drink.bootStrap()
                   .setDbType(DbType.MySQL)
                   .setOption(option)
                   .setDataSourceManager(dataSourceManager)
                   .setTransactionManager(transactionManager)
                   .setSqlSessionFactory(sqlSessionFactory)
                   .build();
       }
   }
   ```

3. 启动！

### 使用SpringBoot

1. 引入starter
   ```xm
           <dependency>
               <groupId>io.github.kiryu1223</groupId>
               <artifactId>drink-spring-boot-starter</artifactId>
               <version>${project.version}</version>
           </dependency>
   ```
2. 配置yml
   ```ya
   spring:
     output:
       ansi:
         enabled: always
     profiles:
       active: dev
     # 最低程度配置下只需要提供一个数据源
     datasource:
       type: com.zaxxer.hikari.HikariDataSource
       url: jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true
       username: root
       password: root
       driverClassName: com.mysql.cj.jdbc.Driver

   server:
     port: 8080

   # 不配置的情况下默认以database: mysql和print-sql: true模式运行
   #drink:
   #  database: mysql
   #  print-sql: true
   ```

3. 启动！

### 使用Solon

1. 引入插件

   ```xml
           <dependency>
               <groupId>io.github.kiryu1223</groupId>
               <artifactId>drink-solon-plugin</artifactId>
               <version>${project.version}</version>
           </dependency>
   ```
2. 配置config和yml

   ```yml
   # 这个名称与config类中的@Inject("${ds1}")对应
   ds1:
     type: com.zaxxer.hikari.HikariDataSource
     jdbcUrl: jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true
     driverClassName: com.mysql.cj.jdbc.Driver
     username: root
     password: root
   # 这个名称与config类中的@Inject("${ds2}")对应
   ds2:
     type: com.zaxxer.hikari.HikariDataSource
     jdbcUrl: jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true
     driverClassName: com.mysql.cj.jdbc.Driver
     username: root
     password: root
   # 这个名称与config类中的@Inject("${ds3}")对应
   ds3:
     type: com.zaxxer.hikari.HikariDataSource
     jdbcUrl: jdbc:mysql://127.0.0.1:3306/employees?rewriteBatchedStatements=true
     driverClassName: com.mysql.cj.jdbc.Driver
     username: root
     password: root

   # 这个名称与config类中的@Inject("${dynamic}")对应
   # 多数据源
   dynamic:
     type: com.zaxxer.hikari.HikariDataSource
     strict: true #严格模式（指定的源不存时：严格模式会抛异常；非严格模式用默认源）
     default: db_user_1 #指定默认数据源
     db_user_1:
       schema: db_user
       jdbcUrl: jdbc:mysql://localhost:3306/db_user?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true
       driverClassName: com.mysql.cj.jdbc.Driver
       username: root
       password: 123456
     db_user_2:
       schema: db_user
       jdbcUrl: jdbc:mysql://localhost:3307/db_user?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true
       driverClassName: com.mysql.cj.jdbc.Driver
       username: root
       password: 123456

   drink:
     # 这个名称代表了ioc容器中Client对象bean的别名，通过@Inject("main")注入到你想要的地方，下同
     main:
       database: MySQL
       # 这里需要一个config类中定义的的数据源的bean的别名，下同
       datasource: normalDs1
     sub:
       database: SqlServer
       datasource: normalDs2
     readonly:
       database: H2
       datasource: normalDs3
     dynamic:
       database: H2
       datasource: dynamicDs
   ```

   ```java
   package io.github.kiryu1223.app.config;

   import com.zaxxer.hikari.HikariDataSource;
   import org.noear.solon.annotation.Bean;
   import org.noear.solon.annotation.Configuration;
   import org.noear.solon.annotation.Inject;
   import org.noear.solon.data.dynamicds.DynamicDataSource;

   import javax.sql.DataSource;

   @Configuration
   public class MyConfig
   {
       @Bean("normalDs1")
       public DataSource dataSource1(@Inject("${ds1}") HikariDataSource dataSource)
       {
           return dataSource;
       }

       @Bean("normalDs2")
       public DataSource dataSource2(@Inject("${ds2}") HikariDataSource dataSource)
       {
           return dataSource;
       }

       @Bean("normalDs3")
       public DataSource dataSource3(@Inject("${ds3}") HikariDataSource dataSource)
       {
           return dataSource;
       }

       @Bean("dynamicDs")
       public DataSource dataSource4(@Inject("${dynamic}") DynamicDataSource dataSource)
       {
           return dataSource;
       }
   }
   ```

   注意，在只配了一个client对象的情况下，使用`@inject`注解在service或者你想要的地方注入Client对象时，不需要填入别名（否则会找不到报错）

3. 启动！

## CRUD

所有的增删查改操作都由DrinkClient对象完成（以下简称为client）

以下是主要使用的api

| 方法     | 参数             | 返回     |
|--------|----------------|--------|
| query  | 数据库表对应对象的class | 查询过程对象 |
| insert | 一个或者多个相同的表对应对象 | 新增过程对象 |
| update | 数据库表对应对象的class | 更新过程对象 |
| delete | 数据库表对应对象的class | 删除过程对象 |

### 查询

查询由client对象的query方法发起，query方法接收一个class对象，返回一个查询过程对象，
可以在后续调用`where` `group by` `limit`等方法添加查询条件

以下是查询过程的api

| 方法             | 参数                                            | 返回                        | 说明                                                                                                                                                                            |
|----------------|-----------------------------------------------|---------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `leftJoin`     | 参数1：class对象或者LQuery对象<br/> 参数2：连接条件的lambda表达式 | 当前泛型数量+1的查询过程对象（因为连了一张新表） | 左连接                                                                                                                                                                           |
| `rightJoin`    | 同leftJoin                                     | 同leftJoin                 | 右连接                                                                                                                                                                           |
| `innerJoin`    | 同leftJoin                                     | 同leftJoin                 | 内连接                                                                                                                                                                           |
| `where`        | where条件的lambda表达式                             | this                      | where过滤条件，多个where默认使用and拼接                                                                                                                                                    |
| `orWhere`      | 同where                                        | this                      | 同where，区别是多个where使用or拼接                                                                                                                                                       |
| `groupBy`      | 返回单个元素或者包含多个元素的Grouper对象的lambda               | 组查询过程对象                   | 单个元素的group by时，可以直接类似于<br/>`a -> a.getId()`<br/>这样的lambda,多个元素时需要使用 <br/>a -> new Grouper()<br/>{ <br/>int id=a.getId();<br/>String name=a.getName();<br/>...<br/>} 这样的lambda |
| `having`       | having条件的lambda表达式                            | this                      | having过滤条件，多个having使用and连接                                                                                                                                                    |
| `orderBy`      | 参数1：需要排序的一个字段<br/>参数2：是否反向排序                  | this                      | 默认正序排序，有多个排序字段的需求时需要调用次orderBy方法                                                                                                                                              |
| `limit`        | rows或者offset和rows                             | this                      |                                                                                                                                                                               |
| `distinct`     | 无参或bool                                       | this                      | 无参调用时将distinct设置为true                                                                                                                                                         |
| `select `      | 无参select()或select(Type.class)或select(lambda)  | 新查询过程对象                   | select代表一次查询过程的终结，在select之后调用任意条件api（例如where）都将把上一个查询过程视为中间表然后对中间表进行的查询                                                                                                       |
| `selectSingle` | 需要返回的字段与返回类型                                  | 终结查询过程对象                  | 同select,区别是当需要返回单一的元素时（比如说`select(s -> s.getId())`），出于安全考虑强制要求使用selectSingle（`selectSingle(s -> s.getId())`）而非select                                                          |
| `toList`       |                                               | 查询返回的结果集                  | 多表查询时必须进行一次select之后才能进行返回结果集操作（因为多表情况下不知道到底要返回什么）                                                                                                                             |

### 新增

xxxx

### 更新

xxxx

### 删除

xxxx
