package io.github.kiryu1223.drink.core.api.crud.create;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IInsertOrUpdate;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.expression.ISqlUpdateExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.drink.core.visitor.UpdateSqlVisitor;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;

import java.util.ArrayList;
import java.util.List;

public class ObjectInsertOrUpdate<T> extends InsertBase<ObjectInsertOrUpdate<T>, T> {
    private final List<T> ts;
    private final Class<T> tClass;
    private final ISqlUpdateExpression updateExpression;
    private final List<ISqlColumnExpression> conflictColumns = new ArrayList<>();
    private final List<ISqlColumnExpression> updateColumns = new ArrayList<>();

    public ObjectInsertOrUpdate(IConfig config, List<T> ts) {
        super(config);
        IInsertOrUpdate insertOrUpdate = config.getInsertOrUpdate();
        if (!insertOrUpdate.apply()) {
            throw new DrinkException(String.format("%s不支持插入或更新操作", config.getDbType()));
        }
        this.ts = ts;
        this.tClass = (Class<T>) ts.get(0).getClass();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        updateExpression = factory.update(tClass);
    }

    private List<ISqlColumnExpression> defaultConflictColumns() {
        MetaData metaData = config.getMetaData(tClass);
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlTableRefExpression ref = updateExpression.getFrom().getTableRefExpression();
        List<ISqlColumnExpression> defaultConflictColumns = new ArrayList<>();
        for (FieldMetaData primary : metaData.getPrimaryList()) {
            defaultConflictColumns.add(factory.column(primary, ref));
        }
        return defaultConflictColumns;
    }

    /**
     * 设置发生重复字段时的更新字段
     */
    public <R> ObjectInsertOrUpdate<T> updateColumn(@Expr(Expr.BodyType.Expr) Func1<T, R> expr) {
        throw new NotCompiledException();
    }

    public <R> ObjectInsertOrUpdate<T> updateColumn(ExprTree<Func1<T, R>> expr) {
        UpdateSqlVisitor visitor = new UpdateSqlVisitor(config, updateExpression);
        ISqlColumnExpression column = visitor.toColumn(expr.getTree());
        updateColumns.add(column);
        return this;
    }

    public ObjectInsertOrUpdate<T> updateAll() {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        MetaData metaData = config.getMetaData(tClass);
        ISqlTableRefExpression ref = updateExpression.getFrom().getTableRefExpression();
        for (FieldMetaData f : metaData.getNotIgnoreAndNavigateAndGeneratedKeyFields()) {
            updateColumns.add(factory.column(f, ref));
        }
        return this;
    }

    /**
     * 寻找保存时发生数据冲突条件的字段，mysql下无效
     */
    public <R> ObjectInsertOrUpdate<T> onConflict(@Expr(Expr.BodyType.Expr) Func1<T, R> expr) {
        throw new NotCompiledException();
    }

    public <R> ObjectInsertOrUpdate<T> onConflict(ExprTree<Func1<T, R>> expr) {
        UpdateSqlVisitor visitor = new UpdateSqlVisitor(config, updateExpression);
        ISqlColumnExpression column = visitor.toColumn(expr.getTree());
        conflictColumns.add(column);
        return this;
    }

    public String toSql() {
        MetaData metaData = config.getMetaData(tClass);
        List<FieldMetaData> onInsertOrUpdateFields = metaData.getNotIgnoreAndNavigateAndGeneratedKeyFields();
        IInsertOrUpdate ii = config.getInsertOrUpdate();
        List<ISqlColumnExpression> cc = conflictColumns.isEmpty() ? defaultConflictColumns() : conflictColumns;
//        List<ISqlColumnExpression> uc = updateColumns.isEmpty() ? defaultUpdateColumns() : updateColumns;
        return ii.insertOrUpdate(metaData, onInsertOrUpdateFields, cc, updateColumns);
    }

    @Override
    protected List<T> getObjects() {
        return ts;
    }

    @Override
    protected Class<T> getTableType() {
        return tClass;
    }

    @Override
    public long executeRows(boolean autoIncrement) {
        if (ts == null || ts.isEmpty()) {
            log.warn("insertOrUpdate列表为空");
            return 0;
        }
        List<ISqlColumnExpression> cc = conflictColumns.isEmpty() ? defaultConflictColumns() : conflictColumns;
        return executeInsertOrUpdate(ts, autoIncrement, cc, updateColumns);
    }
}
