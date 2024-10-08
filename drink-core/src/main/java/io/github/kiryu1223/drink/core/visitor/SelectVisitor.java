package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlColumnExpression;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlQueryableExpression;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.core.visitor.methods.LogicExpression;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isBool;
import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isGroupKey;

public class SelectVisitor extends SqlVisitor
{
    private final SqlQueryableExpression queryable;
    private boolean useUnNameClass = false;

    public SelectVisitor(Config config, SqlQueryableExpression queryable)
    {
        super(config);
        this.queryable = queryable;
    }

    @Override
    public SqlExpression visit(NewExpression newExpression)
    {
        BlockExpression classBody = newExpression.getClassBody();
        if (classBody == null)
        {
            return super.visit(newExpression);
        }
        else
        {
            useUnNameClass = true;
            List<SqlExpression> expressions = new ArrayList<>();
            for (Expression expression : classBody.getExpressions())
            {
                if (expression.getKind() == Kind.Variable)
                {
                    VariableExpression variable = (VariableExpression) expression;
                    String name = variable.getName();
                    MetaData metaData = MetaDataCache.getMetaData(newExpression.getType());
                    Expression init = variable.getInit();
                    if (init != null)
                    {
                        SqlExpression context = visit(variable.getInit());
                        context = boxTheBool(variable.getInit(), context);
                        setAs(expressions, context, name);
                    }
                }
            }
            return factory.select(expressions, newExpression.getType());
        }
    }

    @Override
    public SqlExpression visit(MethodCallExpression methodCall)
    {
        SqlExpression visit = super.visit(methodCall);
        if (useUnNameClass)
        {
            return visit;
        }
        else
        {
            return boxTheBool(methodCall, visit);
        }
    }

    //    @Override
//    public SqlExpression visit(BlockExpression blockExpression)
//    {
//        List<SqlExpression> expressions = new ArrayList<>();
//        for (Expression expression : blockExpression.getExpressions())
//        {
//            if (expression instanceof VariableExpression)
//            {
//                VariableExpression variableExpression = (VariableExpression) expression;
//                if (cur == null)
//                {
//                    cur = variableExpression.getParameter();
//                }
//                else
//                {
//                    throw new IllegalExpressionException(blockExpression);
//                }
//            }
//            else if (expression instanceof MethodCallExpression)
//            {
//                MethodCallExpression methodCall = (MethodCallExpression) expression;
//                Method method = methodCall.getMethod();
//                if (isSetter(method) && methodCall.getExpr() == cur)
//                {
//                    MetaData metaData = MetaDataCache.getMetaData(method.getDeclaringClass());
//                    String name = metaData.getColumnNameBySetter(method);
//                    //propertyMetaData.add(metaData.getPropertyMetaData(name));
//                    SqlExpression expr = visit(methodCall.getArgs().get(0));
//                    setAs(expressions, expr, name);
//                }
//            }
//        }
//        return factory.select(expressions,cur.getType());
//    }

    @Override
    public SqlExpression visit(FieldSelectExpression fieldSelect)
    {
        if (isGroupKey(parameters, fieldSelect)) // g.key
        {
            return queryable.getGroupBy().getColumns().get("key");
        }
        else if (isGroupKey(parameters, fieldSelect.getExpr())) // g.key.xxx
        {
            Map<String, SqlExpression> columns = queryable.getGroupBy().getColumns();
            return columns.get(fieldSelect.getField().getName());
        }
        else
        {
            return super.visit(fieldSelect);
        }
    }

    @Override
    protected SqlVisitor getSelf()
    {
        return new SelectVisitor(config, queryable);
    }

    @Override
    public SqlExpression visit(ParameterExpression parameter)
    {
        if (parameters.contains(parameter))
        {
            Class<?> type = parameter.getType();
            int index = parameters.indexOf(parameter);
            MetaData metaData = MetaDataCache.getMetaData(type);
            //propertyMetaData.addAll(metaData.getColumns().values());
            List<SqlExpression> expressions = new ArrayList<>();
            for (PropertyMetaData pm : metaData.getNotIgnorePropertys())
            {
                expressions.add(factory.column(pm, index));
            }
            return factory.select(expressions, parameter.getType());
        }
        else
        {
            return super.visit(parameter);
        }
    }

    protected void setAs(List<SqlExpression> contexts, SqlExpression expression, String name)
    {
        if (expression instanceof SqlColumnExpression)
        {
            SqlColumnExpression sqlColumn = (SqlColumnExpression) expression;
            if (!sqlColumn.getPropertyMetaData().getColumn().equals(name))
            {
                contexts.add(factory.as(expression, name));
            }
            else
            {
                contexts.add(expression);
            }
        }
        else
        {
            contexts.add(factory.as(expression, name));
        }
    }

    protected SqlExpression boxTheBool(Expression init, SqlExpression result)
    {
        if (init instanceof MethodCallExpression)
        {
            MethodCallExpression methodCall = (MethodCallExpression) init;
            return boxTheBool(methodCall, result);
        }
        return result;
    }

    // 用于包装某些数据库不支持直接返回bool
    protected SqlExpression boxTheBool(MethodCallExpression methodCall, SqlExpression result)
    {
        if (isBool(methodCall.getMethod().getReturnType()))
        {
            switch (config.getDbType())
            {
                case SqlServer:
                case Oracle:
                    return LogicExpression.IfExpression(config, result, factory.constString("1"), factory.constString("0"));
            }
        }
        return result;
    }
}
