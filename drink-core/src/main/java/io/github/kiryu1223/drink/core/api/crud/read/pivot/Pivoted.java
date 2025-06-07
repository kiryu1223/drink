package io.github.kiryu1223.drink.core.api.crud.read.pivot;

import io.github.kiryu1223.drink.base.exception.Winner;
import io.github.kiryu1223.expressionTree.expressions.annos.Getter;
import io.github.kiryu1223.expressionTree.expressions.annos.Setter;

@Getter
@Setter
public abstract class Pivoted<TransColumn,AggColumn> {

    public final AggColumn column(TransColumn columnName) {
        return Winner.error();
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public final String toString() {
        return super.toString();
    }

    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
    }
}
