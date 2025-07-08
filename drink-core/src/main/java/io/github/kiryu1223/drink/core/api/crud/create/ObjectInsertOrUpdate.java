package io.github.kiryu1223.drink.core.api.crud.create;

import io.github.kiryu1223.drink.base.Aop;
import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IInsertOrUpdate;
import io.github.kiryu1223.drink.base.exception.DrinkException;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.expression.ISqlUpdateExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.session.SqlValue;
import io.github.kiryu1223.drink.base.toBean.beancreator.AbsBeanCreator;
import io.github.kiryu1223.drink.base.toBean.beancreator.IGetterCaller;
import io.github.kiryu1223.drink.base.toBean.beancreator.ISetterCaller;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcExecutor;
import io.github.kiryu1223.drink.base.toBean.executor.JdbcInsertResultSet;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.core.exception.NotCompiledException;
import io.github.kiryu1223.drink.core.visitor.UpdateSqlVisitor;
import io.github.kiryu1223.expressionTree.delegate.Func1;
import io.github.kiryu1223.expressionTree.expressions.ExprTree;
import io.github.kiryu1223.expressionTree.expressions.annos.Expr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ObjectInsertOrUpdate<T> {
    private static final Logger log = LoggerFactory.getLogger(ObjectInsertOrUpdate.class);
    private final IConfig config;
    private final List<T> ts;
    private final Class<T> tClass;
    private final ISqlUpdateExpression updateExpression;
    private final List<ISqlColumnExpression> conflictColumns = new ArrayList<>();
    private final List<ISqlColumnExpression> updateColumns = new ArrayList<>();

    public ObjectInsertOrUpdate(IConfig config, List<T> ts) {
        IInsertOrUpdate insertOrUpdate = config.getInsertOrUpdate();
        if (!insertOrUpdate.apply()) {
            throw new DrinkException(String.format("%s不支持插入或更新操作", config.getDbType()));
        }
        this.config = config;
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

    private List<ISqlColumnExpression> defaultUpdateColumns() {
        MetaData metaData = config.getMetaData(tClass);
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        ISqlTableRefExpression ref = updateExpression.getFrom().getTableRefExpression();
        List<ISqlColumnExpression> defaultUpdateColumns = new ArrayList<>();
        for (FieldMetaData fieldMetaData : metaData.getFields()) {
            if (fieldMetaData.isGeneratedKey()) continue;
            defaultUpdateColumns.add(factory.column(fieldMetaData, ref));
        }
        return defaultUpdateColumns;
    }

    public long executeRows() {
        return executeRows(false);
    }

    public long executeRows(boolean autoIncrement) {
        MetaData metaData = config.getMetaData(tClass);

        List<FieldMetaData> onInsertOrUpdateFields = metaData.getOnInsertOrUpdateFields();
        IInsertOrUpdate ii = config.getInsertOrUpdate();
        List<ISqlColumnExpression> cc = conflictColumns.isEmpty() ? defaultConflictColumns() : conflictColumns;
//        List<ISqlColumnExpression> uc = updateColumns.isEmpty() ? defaultUpdateColumns() : updateColumns;
        String sql = ii.insertOrUpdate(metaData, onInsertOrUpdateFields, cc, updateColumns);

        AbsBeanCreator<T> beanCreator = config.getBeanCreatorFactory().get(tClass);
        List<List<SqlValue>> sqlValues;
        try {
            sqlValues = values(beanCreator, onInsertOrUpdateFields);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new DrinkException(e);
        }

        try (JdbcInsertResultSet jdbcInsertResultSet = JdbcExecutor.executeInsert(config, sql, sqlValues, autoIncrement))
        {
            if (autoIncrement)
            {
                ResultSet resultSet = jdbcInsertResultSet.getRs();
                FieldMetaData generatedPrimaryKey = metaData.getGeneratedPrimaryKey();
                int index = 0;
                while (resultSet.next()) {
                    T r = ts.get(index++);
                    ITypeHandler<?> typeHandler = generatedPrimaryKey.getTypeHandler();
                    Object value = typeHandler.getValue(resultSet, 1, generatedPrimaryKey.getGenericType());
                    if (value != null) {
                        ISetterCaller<T> beanSetter = beanCreator.getBeanSetter(generatedPrimaryKey.getFieldName());
                        beanSetter.call(r, value);
                    }
                }
            }
            return jdbcInsertResultSet.getRow();
        }
        catch (SQLException | InvocationTargetException | IllegalAccessException e)
        {
            throw new DrinkException(e);
        }
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

    public ObjectInsertOrUpdate<T> updateAll()
    {
        return updateAll(false);
    }

    public ObjectInsertOrUpdate<T> updateAll(boolean usePrimaryKey)
    {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        MetaData metaData = config.getMetaData(tClass);
        ISqlTableRefExpression ref = updateExpression.getFrom().getTableRefExpression();
        for (FieldMetaData f : metaData.getOnInsertOrUpdateFields())
        {
            if(f.isPrimaryKey()&&!usePrimaryKey)continue;
            updateColumns.add(factory.column(f,ref));
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
        List<FieldMetaData> onInsertOrUpdateFields = metaData.getOnInsertOrUpdateFields();
        IInsertOrUpdate ii = config.getInsertOrUpdate();
        List<ISqlColumnExpression> cc = conflictColumns.isEmpty() ? defaultConflictColumns() : conflictColumns;
//        List<ISqlColumnExpression> uc = updateColumns.isEmpty() ? defaultUpdateColumns() : updateColumns;
        return ii.insertOrUpdate(metaData, onInsertOrUpdateFields, cc, updateColumns);
    }

    private List<List<SqlValue>> values(AbsBeanCreator<T> beanCreator, List<FieldMetaData> onInsertOrUpdateFields) throws InvocationTargetException, IllegalAccessException {
        Aop aop = config.getAop();
        List<List<SqlValue>> sqlValues = new ArrayList<>(ts.size());
        for (T object : ts) {
            List<SqlValue> values = new ArrayList<>();
            aop.callOnInsert(object);
            for (FieldMetaData fieldMetaData : onInsertOrUpdateFields) {
                IGetterCaller<T, ?> beanGetter = beanCreator.getBeanGetter(fieldMetaData.getFieldName());
                Object value = beanGetter.apply(object);
                ITypeHandler<?> typeHandler = fieldMetaData.getTypeHandler();
                if (value == null && fieldMetaData.isNotNull()) {
                    throw new DrinkException(String.format("%s类的%s字段被设置为notnull，但是字段值为空且没有设置默认值注解", fieldMetaData.getParentType(), fieldMetaData.getFieldName()));
                }
                values.add(new SqlValue(value, typeHandler));
            }
            sqlValues.add(values);
        }
        return sqlValues;
    }
}
