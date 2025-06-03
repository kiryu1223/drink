package io.github.kiryu1223.drink.core.api;

import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.crud.read.EndQuery;

public interface IView<T> {
    EndQuery<T> createView(SqlClient client);
}
