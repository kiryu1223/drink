package io.github.kiryu1223.drink.base.expression;

import java.util.Map;

public class SqlTreeVisitor {
    public void visit(ISqlExpression expr) {
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
    }

    public void visit(ISqlUnaryExpression expr) {
        visit(expr.getExpression());
    }

    public void visit(ISqlAsExpression expr) {
        visit(expr.getExpression());
    }

    public void visit(ISqlBinaryExpression expr) {
        visit(expr.getLeft());
        visit(expr.getRight());
    }

    public void visit(ISqlColumnExpression expr) {
        // do nothing
    }

    public void visit(ISqlCollectedValueExpression expr) {
        // do nothing
    }

    public void visit(ISqlConstStringExpression expr) {
        // do nothing
    }

    public void visit(ISqlConditionsExpression expr) {
        for (ISqlExpression condition : expr.getConditions()) {
            visit(condition);
        }
    }

    public void visit(ISqlDynamicColumnExpression expr) {
        // do nothing
    }

    public void visit(ISqlFromExpression expr) {
        visit(expr.getSqlTableExpression());
    }

    public void visit(ISqlGroupByExpression expr) {
        for (Map.Entry<String, ISqlExpression> stringISqlExpressionEntry : expr.getColumns().entrySet()) {
            visit(stringISqlExpressionEntry.getValue());
        }
    }

    public void visit(ISqlHavingExpression expr) {
        visit(expr.getConditions());
    }

    public void visit(ISqlJoinExpression expr) {
        visit(expr.getJoinTable());
        visit(expr.getConditions());
    }

    public void visit(ISqlJoinsExpression expr) {
        for (ISqlJoinExpression sqlJoinExpression : expr.getJoins()) {
            visit(sqlJoinExpression);
        }
    }

    public void visit(ISqlLimitExpression expr) {
        // do nothing
    }

    public void visit(ISqlOrderByExpression expr) {
        for (ISqlOrderExpression sqlOrderExpression : expr.getSqlOrders()) {
            visit(sqlOrderExpression);
        }
    }

    public void visit(ISqlOrderExpression expr) {
        visit(expr.getExpression());
    }

    public void visit(ISqlParensExpression expr) {
        visit(expr.getExpression());
    }

    public void visit(ISqlQueryableExpression expr) {
        visit(expr.getSelect());
        visit(expr.getFrom());
        visit(expr.getJoins());
        visit(expr.getWhere());
        visit(expr.getGroupBy());
        visit(expr.getHaving());
        visit(expr.getOrderBy());
        visit(expr.getLimit());
    }

    public void visit(ISqlRealTableExpression expr) {
        // do nothing
    }

    public void visit(ISqlRecursionExpression expr) {
        visit(expr.getQueryable());
    }

    public void visit(ISqlSelectExpression expr) {
        for (ISqlExpression column : expr.getColumns()) {
            visit(column);
        }
    }

    public void visit(ISqlSetExpression expr) {
        visit(expr.getColumn());
        visit(expr.getValue());
    }

    public void visit(ISqlSetsExpression expr) {
        for (ISqlSetExpression sqlSetExpression : expr.getSets()) {
            visit(sqlSetExpression);
        }
    }

    public void visit(ISqlSingleValueExpression expr) {
        // do nothing
    }

    public void visit(ISqlTemplateExpression expr) {
        for (ISqlExpression sqlExpression : expr.getExpressions()) {
            visit(sqlExpression);
        }
    }

    public void visit(ISqlWithExpression expr) {
        visit(expr.getQueryable());
    }

    public void visit(ISqlWithsExpression expr) {
        for (ISqlWithExpression sqlWithExpression : expr.getWiths()) {
            visit(sqlWithExpression);
        }
    }

    public void visit(ISqlWhereExpression expr) {
        visit(expr.getConditions());
    }

    public void visit(ISqlUnionQueryableExpression expr) {
        for (ISqlQueryableExpression queryable : expr.getQueryable()) {
            visit(queryable);
        }
    }

    public void visit(ISqlTypeCastExpression expr) {
        // do nothing
    }

    public void visit(ISqlUpdateExpression expr) {
        visit(expr.getFrom());
        visit(expr.getJoins());
        visit(expr.getSets());
        visit(expr.getWhere());
    }

    public void visit(ISqlDeleteExpression expr) {
        visit(expr.getFrom());
        visit(expr.getJoins());
        visit(expr.getWhere());
    }

    public void visit(ISqlTableRefExpression expr) {

    }
}
