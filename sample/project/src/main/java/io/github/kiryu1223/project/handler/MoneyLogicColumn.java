package io.github.kiryu1223.project.handler;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.metaData.LogicColumn;
import io.github.kiryu1223.drink.base.transform.Transformer;

public class MoneyLogicColumn extends LogicColumn {
    @Override
    public ISqlExpression onRead(IConfig config, ISqlColumnExpression column) {
        Transformer transformer = config.getTransformer();
        return transformer.abs(column);
    }

    @Override
    public String onWrite(IConfig config) {
        Transformer transformer = config.getTransformer();
        return transformer.count().getSql(config);
    }
}
