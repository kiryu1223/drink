package io.github.kiryu1223.drink.base.expression;

import io.github.kiryu1223.drink.base.IConfig;

import java.util.*;

public abstract class SqlTreeTransformer {
    protected final IConfig config;
    protected final SqlExpressionFactory factory;

    public SqlTreeTransformer(IConfig config) {
        this.config = config;
        factory = config.getSqlExpressionFactory();
    }

    public ISqlExpression visit(ISqlExpression expr) {
        if (expr instanceof ISqlAsExpression) {
           return visit((ISqlAsExpression) expr);
        }
        else if (expr instanceof ISqlUnaryExpression) {
            return visit((ISqlUnaryExpression) expr);
        }
        else if (expr instanceof ISqlBinaryExpression) {
            return visit((ISqlBinaryExpression) expr);
        }
        else if (expr instanceof ISqlColumnExpression) {
            return  visit((ISqlColumnExpression) expr);
        }
        else if (expr instanceof ISqlCollectedValueExpression) {
            return visit((ISqlCollectedValueExpression) expr);
        }
        else if (expr instanceof ISqlConstStringExpression) {
            return  visit((ISqlConstStringExpression) expr);
        }
        else if (expr instanceof ISqlConditionsExpression) {
            return  visit((ISqlConditionsExpression) expr);
        }
        else if (expr instanceof ISqlDynamicColumnExpression) {
            return  visit((ISqlDynamicColumnExpression) expr);
        }
        else if (expr instanceof ISqlFromExpression) {
            return  visit((ISqlFromExpression) expr);
        }
        else if (expr instanceof ISqlGroupByExpression) {
            return  visit((ISqlGroupByExpression) expr);
        }
        else if (expr instanceof ISqlHavingExpression) {
            visit((ISqlHavingExpression) expr);
        }
        else if (expr instanceof ISqlJoinExpression) {
            return visit((ISqlJoinExpression) expr);
        }
        else if (expr instanceof ISqlJoinsExpression) {
            return  visit((ISqlJoinsExpression) expr);
        }
        else if (expr instanceof ISqlLimitExpression) {
            return   visit((ISqlLimitExpression) expr);
        }
        else if (expr instanceof ISqlOrderByExpression) {
            return   visit((ISqlOrderByExpression) expr);
        }
        else if (expr instanceof ISqlOrderExpression) {
            return  visit((ISqlOrderExpression) expr);
        }
        else if (expr instanceof ISqlParensExpression) {
            return   visit((ISqlParensExpression) expr);
        }
        else if (expr instanceof ISqlQueryableExpression) {
            return  visit((ISqlQueryableExpression) expr);
        }
        else if (expr instanceof ISqlRealTableExpression) {
            return  visit((ISqlRealTableExpression) expr);
        }
        else if (expr instanceof ISqlRecursionExpression) {
            return  visit((ISqlRecursionExpression) expr);
        }
        else if (expr instanceof ISqlUnionQueryableExpression) {
            return      visit((ISqlUnionQueryableExpression) expr);
        }
        else if (expr instanceof ISqlSelectExpression) {
            return  visit((ISqlSelectExpression) expr);
        }
        else if (expr instanceof ISqlSetExpression) {
            return  visit((ISqlSetExpression) expr);
        }
        else if (expr instanceof ISqlSetsExpression) {
            return   visit((ISqlSetsExpression) expr);
        }
        else if (expr instanceof ISqlSingleValueExpression) {
            return   visit((ISqlSingleValueExpression) expr);
        }
        else if (expr instanceof ISqlTemplateExpression) {
            return  visit((ISqlTemplateExpression) expr);
        }
        else if (expr instanceof ISqlWithExpression) {
            return   visit((ISqlWithExpression) expr);
        }
        else if (expr instanceof ISqlWithsExpression) {
            return   visit((ISqlWithsExpression) expr);
        }
        else if (expr instanceof ISqlTypeCastExpression) {
            return   visit((ISqlTypeCastExpression) expr);
        }
        else if (expr instanceof ISqlUpdateExpression) {
            return  visit((ISqlUpdateExpression) expr);
        }
        else if (expr instanceof ISqlWhereExpression) {
            return  visit((ISqlWhereExpression) expr);
        }
        else if (expr instanceof ISqlDeleteExpression) {
            return  visit((ISqlDeleteExpression) expr);
        }
        else if (expr instanceof ISqlTableRefExpression) {
            return  visit((ISqlTableRefExpression) expr);
        }
        else if (expr instanceof ISqlStarExpression) {
            return   visit((ISqlStarExpression) expr);
        }
        return null;
    }

    public ISqlExpression visit(ISqlUnaryExpression expr) {
        ISqlExpression right = expr.getExpression();
        ISqlExpression visit = visit(right);
        if (visit != right) {
            return factory.unary(expr.getOperator(), visit);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlAsExpression expr) {
        ISqlExpression right = expr.getExpression();
        ISqlExpression visit = visit(right);
        if (visit != right) {
            return factory.as(visit, expr.getAsName());
        }
        return expr;
    }

    public ISqlExpression visit(ISqlBinaryExpression expr) {
        ISqlExpression left = expr.getLeft();
        ISqlExpression right = expr.getRight();
        ISqlExpression l = visit(left);
        ISqlExpression r = visit(right);
        if (left != l || right != r) {
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
        List<ISqlExpression> cs = new ArrayList<>();
        boolean change = false;
        for (ISqlExpression condition : expr.getConditions()) {
            ISqlExpression visit = visit(condition);
            cs.add(visit);
            if (visit != condition) change = true;
        }
        if (change) {
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
        if (visit != tableExpression) {
            return factory.from((ISqlTableExpression) visit, expr.getTableRefExpression());
        }
        return expr;
    }

    public ISqlExpression visit(ISqlGroupByExpression expr) {
        LinkedHashMap<String, ISqlExpression> temp = new LinkedHashMap<>();
        boolean change = false;
        for (Map.Entry<String, ISqlExpression> e : expr.getColumns().entrySet()) {
            ISqlExpression visit = visit(e.getValue());
            temp.put(e.getKey(), visit);
            if (visit != e.getValue()) change = true;
        }
        if (change) {
            return factory.groupBy(temp);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlHavingExpression expr) {
        ISqlExpression visit = visit(expr.getConditions());
        if (visit != expr.getConditions()) {
            return factory.having((ISqlConditionsExpression) visit);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlJoinExpression expr) {
        ISqlTableExpression table = expr.getJoinTable();
        ISqlConditionsExpression conditions = expr.getConditions();
        ISqlExpression t = visit(table);
        ISqlExpression c = visit(conditions);
        if (t != table || c != conditions) {
            return factory.join(expr.getJoinType(), (ISqlTableExpression) t, (ISqlConditionsExpression) c, expr.getTableRefExpression());
        }
        return expr;
    }

    public ISqlExpression visit(ISqlJoinsExpression expr) {
        List<ISqlJoinExpression> joinList = new ArrayList<>();
        boolean changed = false;
        for (ISqlJoinExpression join : expr.getJoins()) {
            ISqlExpression visit = visit(join);
            joinList.add((ISqlJoinExpression) visit);
            if (visit != join) {
                changed = true;
            }
        }
        if (changed) {
            expr.getJoins().clear();
            expr.getJoins().addAll(joinList);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlLimitExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlOrderByExpression expr) {
        List<ISqlOrderExpression> orderList = new ArrayList<>();
        boolean changed = false;
        for (ISqlOrderExpression order : expr.getSqlOrders()) {
            ISqlExpression visit = visit(order);
            orderList.add((ISqlOrderExpression) visit);
            if (visit != order) {
                changed = true;
            }
        }
        if (changed) {
            expr.getSqlOrders().clear();
            expr.getSqlOrders().addAll(orderList);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlOrderExpression expr) {
        ISqlExpression order = expr.getExpression();
        ISqlExpression visit = visit(order);
        if (visit != order) {
            return factory.order(visit, expr.isAsc());
        }
        return expr;
    }

    public ISqlExpression visit(ISqlParensExpression expr) {
        ISqlExpression expression = expr.getExpression();
        ISqlExpression visit = visit(expression);
        if (visit != expression) {
            return factory.parens(visit);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlQueryableExpression expr) {
        ISqlSelectExpression select = expr.getSelect();
        ISqlFromExpression from = expr.getFrom();
        ISqlJoinsExpression joins = expr.getJoins();
        ISqlWhereExpression where = expr.getWhere();
        ISqlGroupByExpression group = expr.getGroupBy();
        ISqlHavingExpression having = expr.getHaving();
        ISqlOrderByExpression order = expr.getOrderBy();
        ISqlLimitExpression limit = expr.getLimit();
        ISqlSelectExpression s = (ISqlSelectExpression) visit(select);
        ISqlFromExpression f = (ISqlFromExpression) visit(from);
        ISqlJoinsExpression j = (ISqlJoinsExpression) visit(joins);
        ISqlWhereExpression w = (ISqlWhereExpression) visit(where);
        ISqlGroupByExpression g = (ISqlGroupByExpression) visit(group);
        ISqlHavingExpression h = (ISqlHavingExpression) visit(having);
        ISqlOrderByExpression o = (ISqlOrderByExpression) visit(order);
        ISqlLimitExpression l = (ISqlLimitExpression) visit(limit);
        if (s != select || f != from || j != joins || w != where || g != group || h != having || o != order || l != limit) {
            return factory.queryable(s, f, j, w, g, h, o, l);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlRealTableExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlRecursionExpression expr) {
        ISqlQueryableExpression queryable = expr.getQueryable();
        ISqlExpression visit = visit(queryable);
        if (visit != queryable) {
            return factory.recursion((ISqlQueryableExpression) visit, expr.parentId(), expr.childId(), expr.level());
        }
        return expr;
    }

    public ISqlExpression visit(ISqlSelectExpression expr) {
        List<ISqlExpression> columns = new ArrayList<>();
        boolean changed = false;
        for (ISqlExpression column : expr.getColumns()) {
            ISqlExpression visit = visit(column);
            columns.add(visit);
            if (visit != column) {
                changed = true;
            }
        }
        if (changed) {
            expr.getColumns().clear();
            expr.getColumns().addAll(columns);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlSingleValueExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlTemplateExpression expr) {
        List<ISqlExpression> list = new ArrayList<>();
        boolean changed = false;
        for (ISqlExpression sqlExpression : expr.getExpressions()) {
            ISqlExpression visit = visit(sqlExpression);
            list.add(visit);
            if (visit != sqlExpression) {
                changed = true;
            }
        }
        if (changed) {
            return factory.template(expr.getTemplateStrings(), list);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlWithExpression expr) {
        ISqlQueryableExpression queryable = expr.getQueryable();
        ISqlQueryableExpression visit = (ISqlQueryableExpression) visit(queryable);
        if (visit != queryable) {
            return factory.with(visit, expr.withTableName());
        }
        return expr;
    }

    public ISqlExpression visit(ISqlWithsExpression expr) {
        List<ISqlWithExpression> withList = new ArrayList<>();
        boolean changed = false;
        for (ISqlWithExpression with : expr.getWiths()) {
            ISqlExpression visit = visit(with);
            withList.add((ISqlWithExpression) visit);
            if (visit != with) {
                changed = true;
            }
        }
        if (changed) {
            expr.getWiths().clear();
            expr.getWiths().addAll(withList);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlWhereExpression expr) {
        ISqlExpression visit = visit(expr.getConditions());
        if (visit != expr.getConditions()) {
            return factory.where((ISqlConditionsExpression) visit);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlUnionQueryableExpression expr) {
        List<ISqlQueryableExpression> list = new ArrayList<>();
        boolean changed = false;
        for (ISqlQueryableExpression queryable : expr.getQueryable()) {
            ISqlExpression visit = visit(queryable);
            list.add((ISqlQueryableExpression) visit);
            if (visit != queryable) {
                changed = true;
            }
        }
        if (changed) {
            expr.getQueryable().clear();
            expr.getQueryable().addAll(list);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlTypeCastExpression expr) {
        ISqlExpression expression = expr.getExpression();
        ISqlExpression visit = visit(expression);
        if (visit != expression) {
            return factory.typeCast(expr.getType(), visit);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlUpdateExpression expr) {
        ISqlFromExpression from = expr.getFrom();
        ISqlJoinsExpression joins = expr.getJoins();
        ISqlSetsExpression sets = expr.getSets();
        ISqlWhereExpression where = expr.getWhere();
        ISqlFromExpression f = (ISqlFromExpression) visit(from);
        ISqlJoinsExpression j = (ISqlJoinsExpression) visit(joins);
        ISqlSetsExpression s = (ISqlSetsExpression) visit(sets);
        ISqlWhereExpression w = (ISqlWhereExpression) visit(where);
        if (f != from || j != joins || s != sets || w != where) {
            return factory.update(f, j, s, w);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlDeleteExpression expr) {
        ISqlFromExpression from = expr.getFrom();
        ISqlJoinsExpression joins = expr.getJoins();
        ISqlWhereExpression where = expr.getWhere();
        ISqlFromExpression f = (ISqlFromExpression) visit(from);
        ISqlJoinsExpression j = (ISqlJoinsExpression) visit(joins);
        ISqlWhereExpression w = (ISqlWhereExpression) visit(where);
        if (f != from || j != joins || w != where) {
            return factory.delete(f, j, w);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlTableRefExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlStarExpression expr) {
        return expr;
    }

    public ISqlExpression visit(ISqlSetsExpression expr) {
        List<ISqlSetExpression> setList = new ArrayList<>();
        boolean changed = false;
        for (ISqlSetExpression set : expr.getSets()) {
            ISqlSetExpression visit = (ISqlSetExpression) visit(set);
            setList.add(visit);
            if (visit != set) {
                changed = true;
            }
        }
        if (changed) {
            expr.getSets().clear();
            expr.getSets().addAll(setList);
        }
        return expr;
    }

    public ISqlExpression visit(ISqlSetExpression expr) {
        ISqlColumnExpression column = expr.getColumn();
        ISqlExpression value = expr.getValue();
        ISqlColumnExpression c = (ISqlColumnExpression) visit(column);
        ISqlExpression v = visit(value);
        if (c != column || v != value) {
            return factory.set(c, v);
        }
        return expr;
    }
}
