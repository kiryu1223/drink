package io.github.kiryu1223.drink.base.transform.expression;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.transform.Transform;
import io.github.kiryu1223.drink.base.transform.expression.impl.Logic;

public class DefaultExpressionTransformer extends Transform implements ExpressionTransformer {

    public DefaultExpressionTransformer(IConfig config) {
        super(config);
    }

    @Override
    public ILogic getLogic() {
        return new Logic(config);
    }
}
