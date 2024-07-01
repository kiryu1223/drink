package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.util.List;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isProperty;
import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.propertyName;

public class WhereVisitor extends SqlVisitor
{
    @Override
    protected WhereVisitor getSelf()
    {
        return new WhereVisitor();
    }
}
