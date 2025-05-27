package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.*;

public class SqlTreeTransformer {
    protected final IConfig config;
    protected final SqlExpressionFactory factory;

    public SqlTreeTransformer(IConfig config)
    {
        this.config = config;
        factory=config.getSqlExpressionFactory();
    }

    public ISqlExpression visit(ISqlExpression expr) {
        if (expr instanceof ISqlAsExpression) {
            visit((ISqlAsExpression) expr);
        }
        else if (expr instanceof ISqlUnaryExpression) {
            visit((ISqlUnaryExpression) expr);
        }
        else if (expr instanceof ISqlBinaryExpression) {
            visit((ISqlBinaryExpression) expr);
        }
        else if (expr instanceof ISqlColumnExpression) {
            visit((ISqlColumnExpression) expr);
        }
        else if (expr instanceof ISqlCollectedValueExpression) {
            visit((ISqlCollectedValueExpression) expr);
        }
        else if (expr instanceof ISqlConstStringExpression) {
            visit((ISqlConstStringExpression) expr);
        }
        else if (expr instanceof ISqlConditionsExpression) {
            visit((ISqlConditionsExpression) expr);
        }
        else if (expr instanceof ISqlDynamicColumnExpression) {
            visit((ISqlDynamicColumnExpression) expr);
        }
        else if (expr instanceof ISqlFromExpression) {
            visit((ISqlFromExpression) expr);
        }
        else if (expr instanceof ISqlGroupByExpression) {
            visit((ISqlGroupByExpression) expr);
        }
        else if (expr instanceof ISqlHavingExpression) {
            visit((ISqlHavingExpression) expr);
        }
        else if (expr instanceof ISqlJoinExpression) {
            visit((ISqlJoinExpression) expr);
        }
        else if (expr instanceof ISqlJoinsExpression) {
            visit((ISqlJoinsExpression) expr);
        }
        else if (expr instanceof ISqlLimitExpression) {
            visit((ISqlLimitExpression) expr);
        }
        else if (expr instanceof ISqlOrderByExpression) {
            visit((ISqlOrderByExpression) expr);
        }
        else if (expr instanceof ISqlOrderExpression) {
            visit((ISqlOrderExpression) expr);
        }
        else if (expr instanceof ISqlParensExpression) {
            visit((ISqlParensExpression) expr);
        }
        else if (expr instanceof ISqlQueryableExpression) {
            visit((ISqlQueryableExpression) expr);
        }
        else if (expr instanceof ISqlRealTableExpression) {
            visit((ISqlRealTableExpression) expr);
        }
        else if (expr instanceof ISqlRecursionExpression) {
            visit((ISqlRecursionExpression) expr);
        }
        else if (expr instanceof ISqlUnionQueryableExpression) {
            visit((ISqlUnionQueryableExpression) expr);
        }
        else if (expr instanceof ISqlSelectExpression) {
            visit((ISqlSelectExpression) expr);
        }
        else if (expr instanceof ISqlSetExpression) {
            visit((ISqlSetExpression) expr);
        }
        else if (expr instanceof ISqlSetsExpression) {
            visit((ISqlSetsExpression) expr);
        }
        else if (expr instanceof ISqlSingleValueExpression) {
            visit((ISqlSingleValueExpression) expr);
        }
        else if (expr instanceof ISqlTemplateExpression) {
            visit((ISqlTemplateExpression) expr);
        }
        else if (expr instanceof ISqlWithExpression) {
            visit((ISqlWithExpression) expr);
        }
        else if (expr instanceof ISqlWithsExpression) {
            visit((ISqlWithsExpression) expr);
        }
        else if (expr instanceof ISqlTypeCastExpression) {
            visit((ISqlTypeCastExpression) expr);
        }
        else if (expr instanceof ISqlUpdateExpression) {
            visit((ISqlUpdateExpression) expr);
        }
        else if (expr instanceof ISqlWhereExpression) {
            visit((ISqlWhereExpression) expr);
        }
        else if (expr instanceof ISqlDeleteExpression) {
            visit((ISqlDeleteExpression) expr);
        }
        else if (expr instanceof ISqlTableRefExpression) {
            visit((ISqlTableRefExpression) expr);
        }
        else if (expr instanceof ISqlStarExpression) {
            visit((ISqlStarExpression) expr);
        }
    }

    public ISqlExpression visit(ISqlUnaryExpression expr) {
        ISqlExpression right = expr.getExpression();
        ISqlExpression visit = visit(right);
        if(visit!=right)
        {
            return factory.unary(expr.getOperator(), visit);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlAsExpression expr) {
        ISqlExpression right = expr.getExpression();
        ISqlExpression visit = visit(right);
        if(visit!=right)
        {
            return  factory.as(visit, expr.getAsName());
        }
        return expr;
    }

    public ISqlExpression visit(ISqlBinaryExpression expr) {
        ISqlExpression left = expr.getLeft();
        ISqlExpression right = expr.getRight();
        ISqlExpression l = visit(left);
        ISqlExpression r = visit(right);
        if (left != l || right != r)
        {
            return factory.binary(expr.getOperator(), l, r);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlColumnExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlCollectedValueExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlConstStringExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlConditionsExpression expr) {
        List<ISqlExpression> cs=new ArrayList<>();
        boolean change=false;
        for (ISqlExpression condition : expr.getConditions()) {
            ISqlExpression visit = visit(condition);
            cs.add(visit);
            if (visit != condition) change=true;
        }
        if(change)
        {
            expr.getConditions().clear();
            expr.getConditions().addAll(cs);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlDynamicColumnExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlFromExpression expr) {
        ISqlTableExpression tableExpression = expr.getSqlTableExpression();
        ISqlExpression visit = visit(tableExpression);
        if (visit != tableExpression)
        {
           return factory.from((ISqlTableExpression) visit,expr.getTableRefExpression());
        }
        return expr;
    }

    public ISqlExpression visit(ISqlGroupByExpression expr) {
        LinkedHashMap<String, ISqlExpression> temp=new LinkedHashMap<>();
        boolean change=false;
        for (Map.Entry<String, ISqlExpression> e : expr.getColumns().entrySet()) {
            ISqlExpression visit = visit(e.getValue());
            temp.put(e.getKey(),visit);
            if(visit!=e.getValue())change=true;
        }
        if (change)
        {
            return factory.groupBy(temp);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlHavingExpression expr) {
        ISqlExpression visit = visit(expr.getConditions());
        if(visit!=expr.getConditions())
        {
            return factory.having((ISqlConditionsExpression) visit);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlJoinExpression expr) {
        ISqlTableExpression table = expr.getJoinTable();
        ISqlConditionsExpression conditions = expr.getConditions();
        ISqlExpression t = visit(table);
        ISqlExpression c = visit(conditions);
        if (t != table || c != conditions)
        {
            return factory.join(expr.getJoinType(), (ISqlTableExpression) t, (ISqlConditionsExpression) c, expr.getTableRefExpression());
        }
        return expr;
    }

    public ISqlExpression visit(ISqlJoinsExpression expr) {
        List<ISqlJoinExpression> joinList = new ArrayList<>();
        boolean changed=false;
        for (ISqlJoinExpression join : expr.getJoins()) {
            ISqlExpression visit = visit(join);
            joinList.add((ISqlJoinExpression) visit);
            if(visit!=join)
            {
                changed=true;
            }
        }
        if (changed)
        {
            expr.getJoins().clear();
            expr.getJoins().addAll(joinList);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlLimitExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlOrderByExpression expr) {
        List<ISqlOrderExpression> orderList=new ArrayList<>();
        boolean changed=false;
        for (ISqlOrderExpression order : expr.getSqlOrders()) {
            ISqlExpression visit = visit(order);
            orderList.add((ISqlOrderExpression) visit);
            if (visit != order)
            {
                changed=true;
            }
        }
        if (changed)
        {
            expr.getSqlOrders().clear();
            expr.getSqlOrders().addAll(orderList);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlOrderExpression expr) {
        ISqlExpression order = expr.getExpression();
        ISqlExpression visit = visit(order);
        if (visit != order)
        {
            return factory.order(visit,expr.isAsc());
        }
        return expr;
    }

    public ISqlExpression visit(ISqlParensExpression expr) {
        ISqlExpression expression = expr.getExpression();
        ISqlExpression visit = visit(expression);
        if (visit != expression)
        {
            return factory.parens(visit);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlQueryableExpression expr) {
        visit(expr.getSelect());
        visit(expr.getFrom());
        visit(expr.getJoins());
        visit(expr.getWhere());
        visit(expr.getGroupBy());
        visit(expr.getHaving());
        visit(expr.getOrderBy());
        visit(expr.getLimit());
    }

    public ISqlExpression visit(ISqlRealTableExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlRecursionExpression expr) {
        ISqlQueryableExpression queryable = expr.getQueryable();
        ISqlExpression visit = visit(queryable);
        if(visit!=queryable)
        {
            return factory.recursion((ISqlQueryableExpression)visit,expr.parentId(),expr.childId(),expr.level());
        }
        return expr;
    }

    public ISqlExpression visit(ISqlSelectExpression expr) {
        List<ISqlExpression> columns=new ArrayList<>();
        boolean changed=false;
        for (ISqlExpression column : expr.getColumns()) {
            ISqlExpression visit = visit(column);
            columns.add(visit);
            if (visit != column)
            {
                changed=true;
            }
        }
        if (changed)
        {
            expr.getColumns().clear();;
            expr.getColumns().addAll(columns);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlSingleValueExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlTemplateExpression expr) {
        for (ISqlExpression sqlExpression : expr.getExpressions()) {
            visit(sqlExpression);
        }
    }

    public ISqlExpression visit(ISqlWithExpression expr) {
        visit(expr.getQueryable());
    }

    public ISqlExpression visit(ISqlWithsExpression expr) {
        for (ISqlWithExpression sqlWithExpression : expr.getWiths()) {
            visit(sqlWithExpression);
        }
    }

    public ISqlExpression visit(ISqlWhereExpression expr) {
        ISqlExpression visit = visit(expr.getConditions());
        if(visit!=expr.getConditions())
        {
            return factory.where((ISqlConditionsExpression) visit);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlUnionQueryableExpression expr) {
        for (ISqlQueryableExpression queryable : expr.getQueryable()) {
            visit(queryable);
        }
    }

    public ISqlExpression visit(ISqlTypeCastExpression expr) {
        ISqlExpression expression = expr.getExpression();
        ISqlExpression visit = visit(expression);
        if(visit!=expression)
        {
            return factory.typeCast(expr.getType(),visit);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlUpdateExpression expr) {
        visit(expr.getFrom());
        visit(expr.getJoins());
        visit(expr.getSets());
        visit(expr.getWhere());
    }

    public ISqlExpression visit(ISqlDeleteExpression expr) {
        visit(expr.getFrom());
        visit(expr.getJoins());
        visit(expr.getWhere());
    }

    public ISqlExpression visit(ISqlTableRefExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlStarExpression expr) {
        return expr;
    }
}
