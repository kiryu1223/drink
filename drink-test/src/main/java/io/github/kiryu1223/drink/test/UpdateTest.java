package io.github.kiryu1223.drink.test;

import io.github.kiryu1223.drink.api.transaction.Transaction;
import io.github.kiryu1223.drink.ext.SqlCalculates;
import io.github.kiryu1223.drink.pojos.Department;
import io.github.kiryu1223.drink.pojos.DeptEmp;
import io.github.kiryu1223.drink.pojos.School;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.github.kiryu1223.drink.ext.SqlCalculates.in;

@SuppressWarnings("all")
public class UpdateTest extends BaseTest
{
    @Test
    public void u1()
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
                    .set(s ->
                    {
                        s.setName("newName");
                    })
                    .where(w -> SqlCalculates.eq(w.getNumber(), "100"))
                    .executeRows();

            System.out.println("更新影响条数:" + l);


            List<Department> list2 = client.query(Department.class).limit(1).toList();
            System.out.println(list2);

            long l3 = client.delete(Department.class)
                    .where(w -> SqlCalculates.eq(w.getNumber(), "100"))
                    .executeRows();

            System.out.println("删除影响条数:" + l);

            List<Department> list3 = client.query(Department.class).limit(1).toList();
            System.out.println(list3);

            transaction.rollback();
        }

    }

    @Test
    public void u2()
    {
        String sql = client.update(Department.class)
                .leftJoin(DeptEmp.class, (a, b) -> a.getNumber() == b.getDeptNumber())
                .set((a, b) -> a.setName("111"))
                .where((a, b) -> b.getEmpNumber() == 1)
                .toSql();

        System.out.println(sql);
    }

    public void display()
    {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        String sql = client.query(School.class)
                .where(w ->
                        in(w.id, client.query(School.class)
                                .where(h -> in(h.areaId, list)
                                                && h.areaId == w.areaId
                                                && h.hot == 1
                                                && h.status == 1
                                                && in(h.classIfyId, client.query(School.class)
                                                .where(e -> e.id == 975 && e.del == 0)
                                                .select(s -> s.id)
                                        ) && w.del == 0
                                )
                                .orderBy(o -> o.plat_sort)
                                .limit(2)
                                .select(s -> s.id)
                        ) && w.del == 0
                ).toSql();

        System.out.println(sql);
    }

    @Test
    public void display0()
    {
        String sql = client.update(Department.class)
                .set(s ->
                {
                    s.setName("newName");
                })
                .where(w -> w.getNumber() == "100")
                .toSql();
        System.out.println(sql);
    }

    @Test
    public void display1()
    {
        long l = client.update(Department.class)
                .leftJoin(DeptEmp.class, (a, b) -> a.getNumber() == b.getDeptNumber())
                .set((a, b) -> a.setName(b.getDeptNumber()))
                .where((a, b) -> 1 == 1)
                .executeRows();
    }
}
