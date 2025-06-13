package io.github.kiryu1223.drink.db.mysql;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.IInsertOrUpdate;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;

import java.util.List;
import java.util.stream.Collectors;

public class MySQLInsertOrUpdate implements IInsertOrUpdate {

    private final IConfig config;

    public MySQLInsertOrUpdate(IConfig config) {
        this.config = config;
    }

    @Override
    public boolean apply() {
        return true;
    }

    @Override
    public String insertOrUpdate(MetaData metaData, List<FieldMetaData> notIgnoreAndNavigateFields, List<ISqlColumnExpression> conflictColumns, List<ISqlColumnExpression> updateColumns) {
        IDialect dialect = config.getDisambiguation();
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(dialect.disambiguationTableName(metaData.getTableName()));
        builder.append(" (");
        List<String> columnNames = notIgnoreAndNavigateFields
                .stream()
                .map(fm -> dialect.disambiguation(fm.getColumn()))
                .collect(Collectors.toList());
        builder.append(String.join(",", columnNames));
        builder.append(") VALUES (");
        builder.append(columnNames.stream().map(e -> "?").collect(Collectors.joining(",")));
        builder.append(") AS ");
        String asNew = dialect.disambiguationTableName("new");
        builder.append(asNew);
        builder.append(" ON DUPLICATE KEY UPDATE ");
        List<String> us = updateColumns.stream()
                .map(u -> dialect.disambiguation(u.getFieldMetaData().getColumn()))
                .collect(Collectors.toList());
        builder.append(us.stream()
                .map(e -> e + " = " + asNew + "." + e)
                .collect(Collectors.joining(","))
        );
        return builder.toString();
    }
}
