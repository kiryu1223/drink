package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.transform.expression.DefaultExpressionTransformer;
import io.github.kiryu1223.drink.base.transform.expression.ExpressionTransformer;
import io.github.kiryu1223.drink.base.transform.method.DefaultMethodTransform;
import io.github.kiryu1223.drink.base.transform.method.MethodTransformer;

public class DefaultTransformer extends Transform implements Transformer {

    public DefaultTransformer(IConfig config) {
        super(config);
    }

    @Override
    public ExpressionTransformer getExpressionTransformer() {
        return new DefaultExpressionTransformer(config);
    }

    @Override
    public MethodTransformer getMethodTransformer() {
        return new DefaultMethodTransform(config);
    }
}
