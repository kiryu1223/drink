qq群：257911716

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
               <version>1.2.12</version>
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
2. 配置完成后进入main

   ```java
   package io.github.kiryu1223;

   import com.zaxxer.hikari.HikariDataSource;
   import io.github.kiryu1223.drink.Drink;
   import io.github.kiryu1223.drink.api.client.DrinkClient;
   import io.github.kiryu1223.drink.transaction.DefaultTransactionManager;
   import io.github.kiryu1223.drink.transaction.TransactionManager;
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

1. 引入starter并且开启apt

   ```xml
           <dependency>
               <groupId>io.github.kiryu1223</groupId>
               <artifactId>drink-spring-boot-starter</artifactId>
               <version>${project.version}</version>
           </dependency>
   ```
   ```xml
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
2. 配置yml
   ```yaml
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

1. 引入插件并且开启apt

   ```xml
           <dependency>
               <groupId>io.github.kiryu1223</groupId>
               <artifactId>drink-solon-plugin</artifactId>
               <version>${project.version}</version>
           </dependency>
   ```
   ```xml
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

## 常用的注解

| 名称       | 参数                    | 说明                                                          |
|----------|-----------------------|-------------------------------------------------------------|
| `Column` | 参数1：数据库列名<br/>参数2：转换器 | Column注解用于类型的字段与数据库字段不一致的场合，或者是类型不一致需要转换的场合（java枚举<->数据库枚举） |
| `Table`  | 参数1：表名                | Table注解用于类名与表名不一致的场合                                        |

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

以下是常用的查询过程的api

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
| `select `      | 无参select()或select(Vo.class)或select(lambda)    | 新查询过程对象                   | select代表一次查询过程的终结，在select之后调用任意条件api（例如where）都将把上一个查询过程视为中间表然后对中间表进行的查询                                                                                                       |
| `selectSingle` | 需要返回的字段与返回类型                                  | 终结查询过程对象                  | 同select,区别是当需要返回单一的元素时（比如说`select(s -> s.getId())`），出于安全考虑强制要求使用selectSingle（`selectSingle(s -> s.getId())`）而非select                                                          |
| `toList`       |                                               | 查询返回的结果集                  | 多表查询时必须进行一次select之后才能进行返回结果集操作（因为多表情况下不知道到底要返回什么）                                                                                                                             |

假设我们有一个员工表

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
                .set(s ->
                {
                    s.setName("newName");
                })
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

