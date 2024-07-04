package io.github.kiryu1223.drink.core.visitor;

import io.github.kiryu1223.drink.api.crud.read.group.IGroup;
import io.github.kiryu1223.drink.config.Config;
import io.github.kiryu1223.drink.core.builder.MetaData;
import io.github.kiryu1223.drink.core.builder.MetaDataCache;
import io.github.kiryu1223.drink.core.builder.PropertyMetaData;
import io.github.kiryu1223.drink.core.context.*;
import io.github.kiryu1223.expressionTree.expressions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isGroupKey;
import static io.github.kiryu1223.drink.core.visitor.ExpressionUtil.isSetter;

public class SelectVisitor extends SqlVisitor
{
    private ParameterExpression cur;
    private final SqlContext group;
    private final List<PropertyMetaData> propertyMetaData = new ArrayList<>();

    public List<PropertyMetaData> getPropertyMetaData()
    {
        return propertyMetaData;
    }

    public SelectVisitor(SqlContext group, Config config)
    {
        super(config);
        this.group = group;
    }

    @Override
    public SqlContext visit(NewExpression newExpression)
    {
        BlockExpression classBody = newExpression.getClassBody();
        if (classBody == null) throw new RuntimeException();
        List<SqlContext> contexts = new ArrayList<>();
        for (Expression expression : classBody.getExpressions())
        {
            if (expression.getKind() == Kind.Variable)
            {
                VariableExpression variable = (VariableExpression) expression;
                String name = variable.getName();
                MetaData metaData = MetaDataCache.getMetaData(newExpression.getType());
                propertyMetaData.add(metaData.getPropertyMetaData(name));
                SqlContext context = visit(variable.getInit());
                setAs(contexts, context, name);
            }
        }
        return new SqlSelectorContext(contexts);
    }

    @Override
    public SqlContext visit(BlockExpression blockExpression)
    {
        List<SqlContext> contexts = new ArrayList<>();
        for (Expression expression : blockExpression.getExpressions())
        {
            if (expression instanceof VariableExpression)
            {
                VariableExpression variableExpression = (VariableExpression) expression;
                if (cur == null)
                {
                    cur = variableExpression.getParameter();
                }
                else
                {
                    throw new RuntimeException();
                }
            }
            else if (expression instanceof MethodCallExpression)
            {
                MethodCallExpression methodCall = (MethodCallExpression) expression;
                Method method = methodCall.getMethod();
                if (isSetter(method) && methodCall.getExpr() == cur)
                {
                    MetaData metaData = MetaDataCache.getMetaData(method.getDeclaringClass());
                    String name = metaData.getColumnNameBySetter(method);
                    propertyMetaData.add(metaData.getPropertyMetaData(name));
                    SqlContext context = visit(methodCall.getArgs().get(0));
                    setAs(contexts, context, name);
                }
            }
        }
        return new SqlSelectorContext(contexts);
    }

    @Override
    public SqlContext visit(FieldSelectExpression fieldSelect)
    {
        if (isGroupKey(parameters, fieldSelect)) // g.key
        {
            return group;
        }
        else if (isGroupKey(parameters, fieldSelect.getExpr())) // g.key.xxx
        {
            SqlGroupContext groupContext = (SqlGroupContext) group;
            Map<String, SqlContext> contextMap = groupContext.getContextMap();
            return contextMap.get(fieldSelect.getField().getName());
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
    public SqlContext visit(ParameterExpression parameter)
    {
        if (parameters.contains(parameter))
        {
            Class<?> type = parameter.getType();
            if (IGroup.class.isAssignableFrom(type))
            {
                throw new RuntimeException("select中暂时不支持直接返回Group对象");
            }
            int index = parameters.indexOf(parameter);
            MetaData metaData = MetaDataCache.getMetaData(type);
            propertyMetaData.addAll(metaData.getColumns().values());
            List<SqlContext> contextList = new ArrayList<>();
            for (PropertyMetaData pm : metaData.getColumns().values())
            {
                contextList.add(new SqlPropertyContext(pm.getColumn(), index));
            }
            return new SqlSelectorContext(contextList);
        }
        else
        {
            return super.visit(parameter);
        }
    }

    private void setAs(List<SqlContext> contexts, SqlContext context, String name)
    {
        if (context instanceof SqlPropertyContext)
        {
            SqlPropertyContext propertyContext = (SqlPropertyContext) context;
            if (!propertyContext.getProperty().equals(name))
            {
                contexts.add(new SqlAsNameContext(name, context));
            }
            else
            {
                contexts.add(context);
            }
        }
        else
        {
            contexts.add(new SqlAsNameContext(name, context));
        }
    }
}
