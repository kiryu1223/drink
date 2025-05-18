package io.github.kiryu1223.drink.base.expression.impl;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.expression.*;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.table.TreeCte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlRecursionExpression implements ISqlRecursionExpression {
    protected final ISqlQueryableExpression queryable;
    protected final FieldMetaData parentField;
    protected final FieldMetaData childField;
    protected final int level;

    protected SqlRecursionExpression(ISqlQueryableExpression queryable, FieldMetaData parentField, FieldMetaData childField, int level) {
        this.queryable = queryable;
        this.parentField = parentField;
        this.childField = childField;
        this.level = level;
    }

    @Override
    public ISqlQueryableExpression getQueryable() {
        return queryable;
    }

    @Override
    public String withTableName() {
        return "as_tree_cte";
    }

    @Override
    public FieldMetaData parentId() {
        return parentField;
    }

    @Override
    public FieldMetaData childId() {
        return childField;
    }

    @Override
    public int level() {
        return level;
    }

    // WITH "as_tree_cte" as (
    // SELECT 0 as cte_level, a."Code", a."Name", a."ParentCode"
    // FROM "Area" a
    // WHERE (a."Name" = '中国')

    // union all

    // SELECT wct1.cte_level + 1 as cte_level, wct2."Code", wct2."Name", wct2."ParentCode"
    // FROM "as_tree_cte" wct1
    // INNER JOIN "Area" wct2 ON wct2."ParentCode" = wct1."Code"
    // )

    // SELECT a."Code", a."Name", a."ParentCode"
    // FROM "as_tree_cte" a
    // ORDER BY a."Code"

    protected String getStart(IConfig config) {
        IDialect dialect = config.getDisambiguation();
        return dialect.disambiguationTableName(withTableName())+" AS";
    }

    @Override
    public String getSqlAndValue(IConfig config, List<SqlValue> values) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlQueryableExpression queryCopy = queryable.copy(config);
        ISqlSelectExpression selectCopy = queryCopy.getSelect().copy(config);
        Class<?> fromTableType = queryCopy.getFrom().getType();
        // SELECT ..., 0 as cte_level
        ISqlTableRefExpression AsWct1 = factory.tableRef("wct1");
        ISqlTableRefExpression AsWct2 = factory.tableRef("wct2");
        selectCopy.accept(new SqlTreeVisitor(){
            @Override
            public void visit(ISqlColumnExpression column) {
                column.setTableRefExpression(AsWct2);
            }
        });
        // SELECT ..., wct1.cte_level + 1 as cte_level
        ISqlQueryableExpression wct = factory.queryable(selectCopy, factory.from(factory.table(TreeCte.class), AsWct1));
        ISqlConditionsExpression on = factory.condition(Collections.singletonList(factory.binary(SqlOperator.EQ, factory.column(parentField, AsWct2), factory.column(childField, AsWct1))));
        wct.addJoin(factory.join(JoinType.INNER, factory.table(fromTableType), on, AsWct2));
        tryLevel(config, queryCopy.getSelect(), wct.getSelect(), wct.getWhere(),AsWct1);
        List<String> templates = new ArrayList<>();
        templates.add(getStart(config));
        templates.add("(" + queryCopy.getSqlAndValue(config, values));
        templates.add("UNION ALL");
        templates.add(wct.getSqlAndValue(config, values) + ")");
        return String.join(" ", templates);
    }

    protected void tryLevel(IConfig config, ISqlSelectExpression s1, ISqlSelectExpression s2, ISqlWhereExpression where,ISqlTableRefExpression wct1) {
        if (level > 0) {
            SqlExpressionFactory factory = config.getSqlExpressionFactory();
            String levelColumnName = "cte_level";
            s1.addColumn(factory.as(factory.constString("0"), levelColumnName));
//            ISqlConstStringExpression wct1_cte_level = factory.constString(dialect.disambiguation("wct1") + "." + dialect.disambiguation("cte_level"));
            ISqlDynamicColumnExpression wct1_cte_level = factory.dynamicColumn(levelColumnName,int.class,wct1);
            s2.addColumn(factory.as(factory.binary(SqlOperator.PLUS, wct1_cte_level, factory.value(1)), levelColumnName));
            where.addCondition(factory.binary(SqlOperator.LT, wct1_cte_level, factory.value(level)));
        }
    }
}
