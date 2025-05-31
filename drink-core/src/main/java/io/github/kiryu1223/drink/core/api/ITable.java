package io.github.kiryu1223.drink.core.api;

import io.github.kiryu1223.drink.core.api.crud.read.LQuery;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.expressionTree.expressions.annos.Getter;
import io.github.kiryu1223.expressionTree.expressions.annos.Setter;

import java.util.Collection;

@Getter
@Setter
public interface ITable
{
    default <T extends ITable> LQuery<T> query(Collection<T> t)
    {
       throw new NotCompiledException();
    }
}
