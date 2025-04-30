package io.github.kiryu1223.drink.base.session;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.toBean.handler.ITypeHandler;
import io.github.kiryu1223.drink.base.toBean.handler.TypeHandlerManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.sun.jmx.mbeanserver.Util.cast;

public class SqlValue
{
    /**
     * 值
     */
    private final Object value;
    /**
     * 类型处理器
     */
    private final ITypeHandler<?> typeHandler;

    public SqlValue(Object value) {
        this(value, value == null ? TypeHandlerManager.getUnKnowTypeHandler() : TypeHandlerManager.get(value.getClass()));
    }

    public SqlValue(Object value, ITypeHandler<?> typeHandler) {
        this.value = value;
        this.typeHandler = typeHandler;
    }

    /**
     * 设置进sql
     */
    public void preparedStatementSetValue(PreparedStatement preparedStatement, int index) throws SQLException {
        typeHandler.setValue(preparedStatement, index, cast(value));
    }
}
