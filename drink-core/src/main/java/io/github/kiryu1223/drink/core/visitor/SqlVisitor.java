package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.api.crud.read.group.IAggregation;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.MetaData;
import io.github.kiryu1223.drink.core.builder.MetaDataCache;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isGetter;
import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isProperty;

public abstract class SqlVisitor extends ResultThrowVisitor<SqlContext>
{
    protected List<ParameterExpression> parameters;
    protected final Config config;

    protected SqlVisitor(Config config)
    {
        this.config = config;
    }

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
            Field field = fieldSelect.getField();
            MetaData metaData = MetaDataCache.getMetaData(field.getDeclaringClass());
            return new SqlPropertyContext(metaData.getColumnNameByPropertyName(field.getName()), index);
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
        if (IAggregation.class.isAssignableFrom(methodCall.getMethod().getDeclaringClass()))
        {
            String name = methodCall.getMethod().getName();
            switch (name)
            {
                case "count":
                    break;
                case "sum":
                    break;
                case "avg":
                    break;
                case "max":
                    break;
                case "min":
                    break;
            }
            List<SqlContext> args = new ArrayList<>(methodCall.getArgs().size());
            for (Expression arg : methodCall.getArgs())
            {
                args.add(visit(arg));
            }
            return new SqlFuncContext(name.toUpperCase(), args);
        }
        else if (isProperty(parameters, methodCall) && isGetter(methodCall.getMethod()))
        {
            ParameterExpression parameter = (ParameterExpression) methodCall.getExpr();
            int index = parameters.indexOf(parameter);
            Method method = methodCall.getMethod();
            MetaData metaData = MetaDataCache.getMetaData(method.getDeclaringClass());
            return new SqlPropertyContext(metaData.getColumnNameByGetter(method), index);
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
