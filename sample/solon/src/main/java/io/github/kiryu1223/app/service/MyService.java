package io.github.kiryu1223.app.service;

import io.github.kiryu1223.app.pojos.Department;
import io.github.kiryu1223.app.pojos.Employee;
import io.github.kiryu1223.drink.core.api.Result;
import io.github.kiryu1223.drink.core.api.client.DrinkClient;
import io.github.kiryu1223.drink.core.api.transaction.Transaction;
import io.github.kiryu1223.drink.core.dataSource.DataSourceManager;
import io.github.kiryu1223.drink.core.sqlExt.SqlFunctions;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;


import java.time.LocalDate;
import java.util.List;

@Component
public class MyService
{
    @Inject
    DrinkClient client;

    public long showCount()
    {
        long aLong = client
                .query(Employee.class)
                .endSelect(s -> SqlFunctions.count())
                .toList()
                .get(0);

        System.out.println(aLong);
        return aLong;
    }

    public List<? extends Result> showList()
    {
        List<? extends Result> list = client
                .query(Employee.class)
                .limit(50)
                .select(s -> new Result()
                {
                    int a = s.getNumber();
                    String b = s.getFirstName();
                    LocalDate c = s.getHireDay();
                })
                .toList();

        System.out.println(list.size());
        return list;
    }

    @Tran
    public long insertTest()
    {
        Department department = new Department();
        department.setNumber("101");
        department.setName("ddd");
        long l = client.insert(department).executeRows();
        System.out.println(1 / 0);
        return l;
    }

    public long dataBaseTest()
    {
        try (Transaction transaction = client.beginTransaction())
        {
            Department department1 = new Department();
            department1.setNumber("100");
            long l = client.insert(department1).executeRows();
            System.out.println("插入条数:" + l);

            List<Department> list = client.query(Department.class).limit(1).toList();
            System.out.println(list);

            long l2 = client.update(Department.class)
                    .set(s -> s.setName("newName"))
                    .where(w -> w.getNumber() == "100")
                    .executeRows();

            System.out.println("更新影响条数:" + l);


            List<Department> list2 = client.query(Department.class).limit(1).toList();
            System.out.println(list2);

            long l3 = client.delete(Department.class)
                    .where(w -> w.getNumber() == "100")
                    .executeRows();

            System.out.println("删除影响条数:" + l);

            List<Department> list3 = client.query(Department.class).limit(1).toList();
            System.out.println(list3);

            transaction.rollback();

            return 0;
        }
    }

    public void dsTest()
    {
        DataSourceManager dataSourceManager = client.getConfig().getDataSourceManager();
        System.out.println(dataSourceManager);
        System.out.println(dataSourceManager.getDataSource());
    }
}
