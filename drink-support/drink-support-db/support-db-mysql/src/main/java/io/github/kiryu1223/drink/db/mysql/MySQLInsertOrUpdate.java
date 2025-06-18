package io.github.kiryu1223.drink.db.mysql;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.IInsertOrUpdate;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;
import io.github.kiryu1223.drink.base.metaData.LogicColumn;

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
    public String insertOrUpdate(MetaData metaData, List<FieldMetaData> onInsertOrUpdateFields, List<ISqlColumnExpression> conflictColumns, List<ISqlColumnExpression> updateColumns) {
        IDialect dialect = config.getDisambiguation();
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        String tableName = dialect.disambiguationTableName(metaData.getTableName());
        builder.append(tableName);
        builder.append(" (");
        List<String> columnNames = onInsertOrUpdateFields
                .stream()
                .map(fm -> dialect.disambiguation(fm.getColumn()))
                .collect(Collectors.toList());
        builder.append(String.join(",", columnNames));
        builder.append(") VALUES (");
        builder.append(onInsertOrUpdateFields.stream().map(f -> {
            if (f.hasLogicColumn()) {
                LogicColumn logicColumn = f.getLogicColumn();
                return logicColumn.onWrite(config);
            }
            else {
                return "?";
            }
        }).collect(Collectors.joining(",")));
        builder.append(") AS ");
        String asNew = dialect.disambiguationTableName("new");
        builder.append(asNew);
        builder.append(" ON DUPLICATE KEY UPDATE ");
        // 如果重复时需要更新的字段为空说明就是忽略更新
        if (updateColumns.isEmpty())
        {
            FieldMetaData primary = metaData.getPrimary();
            String primaryKeyName = dialect.disambiguation(primary.getColumn());
            String set = tableName + "." + primaryKeyName;
            builder.append(set).append(" = ").append(set);
        }
        else
        {
            List<String> us = updateColumns.stream()
                    .map(u -> dialect.disambiguation(u.getFieldMetaData().getColumn()))
                    .collect(Collectors.toList());
            builder.append(us.stream()
                    .map(e -> e + " = " + asNew + "." + e)
                    .collect(Collectors.joining(","))
            );
        }

        return builder.toString();
    }
}
