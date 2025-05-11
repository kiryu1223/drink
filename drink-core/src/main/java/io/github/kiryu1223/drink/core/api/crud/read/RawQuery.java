package io.github.kiryu1223.drink.core.api.crud.read;

import io.github.kiryu1223.expressionTree.delegate.Action1;
import io.github.kiryu1223.expressionTree.delegate.Func1;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RawQuery
{

    public interface Func<R>
    {
        R invoke(ResultSet rs) throws SQLException;
    }
}
