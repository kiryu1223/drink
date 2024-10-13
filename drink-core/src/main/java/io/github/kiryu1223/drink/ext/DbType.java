package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.config.dialect.*;
import io.github.kiryu1223.drink.core.builder.DefaultResultSetValueGetter;
import io.github.kiryu1223.drink.core.builder.IResultSetValueGetter;
import io.github.kiryu1223.drink.core.builder.IncludeFactory;
import io.github.kiryu1223.drink.core.builder.h2.H2IncludeFactory;
import io.github.kiryu1223.drink.core.builder.mysql.MySqlIncludeFactory;
import io.github.kiryu1223.drink.core.builder.oracle.OracleIncludeFactory;
import io.github.kiryu1223.drink.core.builder.sqlite.SqliteResultSetValueGetter;
import io.github.kiryu1223.drink.core.builder.sqlserver.SqlServerIncludeFactory;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.h2.H2ExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.mysql.MySqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.oracle.OracleExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.sqlite.SqliteExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.sqlserver.SqlServerExpressionFactory;

public enum DbType
{
    Any(new DefaultDialect(), new MySqlExpressionFactory(), new MySqlIncludeFactory(), new DefaultResultSetValueGetter()),
    MySQL(new MySQLDialect(), new MySqlExpressionFactory(), new MySqlIncludeFactory(), new DefaultResultSetValueGetter()),
    SQLServer(new SqlServerDialect(), new SqlServerExpressionFactory(), new SqlServerIncludeFactory(), new DefaultResultSetValueGetter()),
    H2(new H2Dialect(), new H2ExpressionFactory(), new H2IncludeFactory(), new DefaultResultSetValueGetter()),
    Oracle(new OracleDialect(), new OracleExpressionFactory(), new OracleIncludeFactory(), new DefaultResultSetValueGetter()),
    SQLite(new SQLiteDialect(), new SqliteExpressionFactory(), new MySqlIncludeFactory(), new SqliteResultSetValueGetter()),
    PostgreSQL(new PostgreSQLDialect(),new MySqlExpressionFactory(), new MySqlIncludeFactory(), new DefaultResultSetValueGetter()),
    ;
    private final IDialect dialect;
    private final SqlExpressionFactory sqlExpressionFactory;
    private final IncludeFactory includeFactory;
    private final IResultSetValueGetter valueGetter;

    DbType(IDialect dialect, SqlExpressionFactory sqlExpressionFactory, IncludeFactory includeFactory, IResultSetValueGetter valueGetter)
    {
        this.dialect = dialect;
        this.sqlExpressionFactory = sqlExpressionFactory;
        this.includeFactory = includeFactory;
        this.valueGetter = valueGetter;
    }

    public IDialect getDialect()
    {
        return dialect;
    }

    public SqlExpressionFactory getSqlExpressionFactory()
    {
        return sqlExpressionFactory;
    }

    public IncludeFactory getIncludeFactory()
    {
        return includeFactory;
    }

    public IResultSetValueGetter getValueGetter()
    {
        return valueGetter;
    }
}
