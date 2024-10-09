package io.github.kiryu1223.drink.ext;

import io.github.kiryu1223.drink.config.dialect.*;
import io.github.kiryu1223.drink.core.builder.IncludeFactory;
import io.github.kiryu1223.drink.core.builder.h2.H2IncludeFactory;
import io.github.kiryu1223.drink.core.builder.mysql.MySqlIncludeFactory;
import io.github.kiryu1223.drink.core.builder.oracle.OracleIncludeFactory;
import io.github.kiryu1223.drink.core.builder.sqlserver.SqlServerIncludeFactory;
import io.github.kiryu1223.drink.core.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.h2.H2ExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.mysql.MySqlExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.oracle.OracleExpressionFactory;
import io.github.kiryu1223.drink.core.expression.ext.sqlserver.SqlServerExpressionFactory;

public enum DbType
{
    Any(new DefaultDialect(), new MySqlExpressionFactory(), new MySqlIncludeFactory()),
    MySQL(new MySQLDialect(), new MySqlExpressionFactory(), new MySqlIncludeFactory()),
    SqlServer(new SqlServerDialect(), new SqlServerExpressionFactory(), new SqlServerIncludeFactory()),
    H2(new H2Dialect(), new H2ExpressionFactory(), new H2IncludeFactory()),
    Oracle(new OracleDialect(), new OracleExpressionFactory(), new OracleIncludeFactory()),
    SQLite(new SQLiteDialect(), new MySqlExpressionFactory(), new MySqlIncludeFactory()),
    ;
    private final IDialect dialect;
    private final SqlExpressionFactory sqlExpressionFactory;
    private final IncludeFactory includeFactory;

    DbType(IDialect dialect, SqlExpressionFactory sqlExpressionFactory, IncludeFactory includeFactory)
    {
        this.dialect = dialect;
        this.sqlExpressionFactory = sqlExpressionFactory;
        this.includeFactory = includeFactory;
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
}
