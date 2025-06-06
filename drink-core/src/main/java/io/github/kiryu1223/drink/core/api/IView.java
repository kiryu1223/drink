package io.github.kiryu1223.drink.core.api;

import io.github.kiryu1223.drink.core.SqlClient;
import io.github.kiryu1223.drink.core.api.crud.read.QueryBase;

public interface IView<T> {
   QueryBase<?, T> createView(SqlClient client);
}
