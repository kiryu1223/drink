package io.github.kiryu1223.drink.base.transform;

import io.github.kiryu1223.drink.base.transform.expression.ExpressionTransformer;
import io.github.kiryu1223.drink.base.transform.method.MethodTransformer;

public interface Transformer {
    ExpressionTransformer getExpressionTransformer();
    MethodTransformer getMethodTransformer();
}
