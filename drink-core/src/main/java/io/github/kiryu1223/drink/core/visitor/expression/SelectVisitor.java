package io.github.kiryu1223.drink.core.visitor.expression;

import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.expression.SqlColumnExpression;
import io.github.kiryu1223.drink.core.expression.SqlExpression;
import io.github.kiryu1223.drink.core.expression.SqlGroupByExpression;
import io.github.kiryu1223.drink.core.metaData.MetaData;
import io.github.kiryu1223.drink.core.metaData.MetaDataCache;
import io.github.kiryu1223.drink.core.metaData.PropertyMetaData;
import io.github.kiryu1223.drink.exception.IllegalExpressionException;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.kiryu1223.drink.core.visitor.expression.ExpressionUtil.isGroupKey;

public class SelectVisitor extends SqlVisitor
{
    private ParameterExpression cur;
    private final SqlGroupByExpression group;

    public SelectVisitor(SqlGroupByExpression group, Config config)
    {
        super(config);
        this.group = group;
    }

    @Override
    public SqlExpression visit(NewExpression newExpression)
    {
        BlockExpression classBody = newExpression.getClassBody();
        if (classBody == null) throw new IllegalExpressionException(newExpression);
        List<SqlExpression> expressions = new ArrayList<>();
        for (Expression expression : classBody.getExpressions())
        {
            if (expression.getKind() == Kind.Variable)
            {
                VariableExpression variable = (VariableExpression) expression;
                String name = variable.getName();
                MetaData metaData = MetaDataCache.getMetaData(newExpression.getType());
                //propertyMetaData.add(metaData.getPropertyMetaData(name));
                SqlExpression context = visit(variable.getInit());
                setAs(expressions, context, name);
            }
        }
        return factory.select(expressions, newExpression.getType());
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
            return group;
        }
        else if (isGroupKey(parameters, fieldSelect.getExpr())) // g.key.xxx
        {
            Map<String, SqlExpression> columns = group.getColumns();
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
        return new SelectVisitor(group, config);
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

    protected void setAs(List<SqlExpression> contexts, SqlExpression context, String name)
    {
        if (context instanceof SqlColumnExpression)
        {
            SqlColumnExpression sqlColumn = (SqlColumnExpression) context;
            if (!sqlColumn.getPropertyMetaData().getProperty().equals(name))
            {
                contexts.add(factory.as(context, name));
            }
            else
            {
                contexts.add(context);
            }
        }
        else
        {
            contexts.add(factory.as(context, name));
        }
    }
}
