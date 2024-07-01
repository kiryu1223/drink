package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.*;

public abstract class SqlVisitor extends ResultThrowVisitor<SqlContext>
{
    protected List<ParameterExpression> parameters;


    @Override
    public SqlContext visit(LambdaExpression<?> lambda)
    {
        if (parameters == null)
        {
            parameters = lambda.getParameters();
            return visit(lambda.getBody());
        }
        else
        {
            SqlVisitor self = getSelf();
            return self.visit(lambda);
        }
    }

    @Override
    public SqlContext visit(FieldSelectExpression fieldSelect)
    {
        if (isProperty(parameters, fieldSelect))
        {
            ParameterExpression parameter = (ParameterExpression) fieldSelect.getExpr();
            int index = parameters.indexOf(parameter);
            return new SqlPropertyContext(propertyName(fieldSelect.getField()), index);
        }
        else
        {
            if (hasParameter(fieldSelect)) throw new RuntimeException();
            return new SqlValueContext(fieldSelect.getValue());
        }
    }

    @Override
    public SqlContext visit(MethodCallExpression methodCall)
    {
        if (isProperty(parameters, methodCall) && isGetter(methodCall.getMethod()))
        {
            ParameterExpression parameter = (ParameterExpression) methodCall.getExpr();
            int index = parameters.indexOf(parameter);
            return new SqlPropertyContext(propertyName(methodCall.getMethod()), index);
        }
        else
        {
            if (hasParameter(methodCall)) throw new RuntimeException();
            return new SqlValueContext(methodCall.getValue());
        }
    }

    @Override
    public SqlContext visit(BinaryExpression binary)
    {
        Expression left = binary.getLeft();
        Expression right = binary.getRight();
        return new SqlBinaryContext(
                SqlOperator.valueOf(binary.getOperatorType().name()),
                visit(left),
                visit(right)
        );
    }

    @Override
    public SqlContext visit(UnaryExpression unary)
    {
        return new SqlUnaryContext(
                SqlOperator.valueOf(unary.getOperatorType().name()),
                visit(unary.getOperand())
        );
    }

    @Override
    public SqlContext visit(ParensExpression parens)
    {
        return new SqlParensContext(visit(parens.getExpr()));
    }

    @Override
    public SqlContext visit(StaticClassExpression staticClass)
    {
        return new SqlValueContext(staticClass.getType());
    }

    @Override
    public SqlContext visit(ConstantExpression constant)
    {
        return new SqlValueContext(constant.getValue());
    }

    @Override
    public SqlContext visit(ReferenceExpression reference)
    {
        return new SqlValueContext(reference.getValue());
    }

    protected abstract SqlVisitor getSelf();

    protected boolean hasParameter(Expression expression)
    {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        expression.accept(new DeepFindVisitor()
        {
            @Override
            public void visit(ParameterExpression parameterExpression)
            {
                atomicBoolean.set(true);
            }
        });
        return atomicBoolean.get();
    }
}
