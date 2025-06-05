package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.IView;
import io.github.kiryu1223.drink.core.api.crud.read.EndQuery;
import lombok.Data;

@Data
public class Klass implements IView<Klass> {

    private int id;
    private String name;
    private int studentCount;

    @Override
    public EndQuery<Klass> createView(SqlClient client) {
        return null;
//        return client.query(Course.class)
//                .leftJoin(Student.class, (a, b) -> a.getStudents().contains(b))
//                .select((c, s) -> new Klass() {
//                    {
//                        setId(c.getCourseId());
//                        setName(s.getName());
////                        setStudentCount((int) c.getCount());
//                    }
//                });
    }
}
