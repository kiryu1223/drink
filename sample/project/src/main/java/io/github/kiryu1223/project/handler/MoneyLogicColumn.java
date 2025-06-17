package io.github.kiryu1223.project.handler;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.metaData.SqlLogicColumn;
import io.github.kiryu1223.drink.base.transform.Transformer;

public class MoneyLogicColumn extends SqlLogicColumn {
    @Override
    public ISqlExpression onRead(IConfig config, ISqlColumnExpression column) {
        Transformer transformer = config.getTransformer();
        return transformer.abs(column);
    }

    @Override
    public ISqlExpression onWrite(IConfig config, ISqlExpression value) {
        Transformer transformer = config.getTransformer();
        return transformer.abs(value);
    }
}
