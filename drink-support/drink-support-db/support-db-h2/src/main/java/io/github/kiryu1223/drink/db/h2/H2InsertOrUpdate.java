package io.github.kiryu1223.drink.db.h2;

import io.github.kiryu1223.drink.base.IInsertOrUpdate;

public class H2InsertOrUpdate implements IInsertOrUpdate {
    @Override
    public boolean apply() {
        return false;
    }
}
