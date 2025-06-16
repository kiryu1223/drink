package io.github.kiryu1223.drink.db.pg;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.IInsertOrUpdate;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostgreInsertOrUpdate implements IInsertOrUpdate {
    private final IConfig config;

    public PostgreInsertOrUpdate(IConfig config) {
        this.config = config;
    }

    @Override
    public boolean apply() {
        return true;
    }

    @Override
    public String insertOrUpdate(MetaData metaData, List<FieldMetaData> onInsertOrUpdateFields, List<ISqlColumnExpression> conflictColumns, List<ISqlColumnExpression> updateColumns) {
        IDialect dialect = config.getDisambiguation();

        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(dialect.disambiguationTableName(metaData.getTableName()));
        builder.append(" (");
        List<String> columnNames = onInsertOrUpdateFields
                .stream()
                .map(fm -> dialect.disambiguation(fm.getColumn()))
                .collect(Collectors.toList());
        builder.append(String.join(",", columnNames));
        builder.append(") VALUES (");
        builder.append(columnNames.stream().map(e -> "?").collect(Collectors.joining(",")));
        builder.append(") ON CONFLICT (");
        List<String> cs = new ArrayList<>();
        for (ISqlColumnExpression conflictColumn : conflictColumns) {
            cs.add(dialect.disambiguation(conflictColumn.getFieldMetaData().getColumn()));
        }
        builder.append(String.join(",", cs));
        builder.append(") ");
        if (updateColumns.isEmpty())
        {
            builder.append("DO NOTHING");
        }
        else
        {
            builder.append("DO UPDATE SET ");
            List<String> us = new ArrayList<>();
            for (ISqlColumnExpression updateColumn : updateColumns) {
                FieldMetaData field = updateColumn.getFieldMetaData();
                String column = dialect.disambiguation(field.getColumn());
                us.add(column + " = EXCLUDED." + column);
            }
            builder.append(String.join(",", us));
        }
        return builder.toString();
    }
}
