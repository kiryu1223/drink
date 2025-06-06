package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.IView;
import io.github.kiryu1223.drink.core.api.crud.read.EndQuery;
import io.github.kiryu1223.drink.core.api.crud.read.QueryBase;
import lombok.Data;

@Data
public class Klass implements IView<Klass> {

    private int id;
    private String name;
    private int studentCount;

    @Override
    public QueryBase<?, Klass> createView(SqlClient client) {
        return client.query(Course.class)
                .leftJoin(Student.class, (a, b) -> a.getStudents().contains(b))
                .select((c, s) -> new Klass() {
                    {
                        setId(c.getCourseId());
                        setName(s.getName());
                    }
                });
    }
}
