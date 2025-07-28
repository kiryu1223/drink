package io.github.kiryu1223.drink.db.tidb;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.IInsertOrUpdate;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.LogicColumn;
import io.github.kiryu1223.drink.base.metaData.MetaData;

import java.util.List;
import java.util.stream.Collectors;

public class TiDBInsertOrUpdate implements IInsertOrUpdate {

    private final IConfig config;

    public TiDBInsertOrUpdate(IConfig config) {
        this.config = config;
    }

    @Override
    public boolean apply() {
        return true;
    }

    @Override
    public String insertOrUpdate(MetaData metaData, List<FieldMetaData> insertColumns, List<ISqlColumnExpression> conflictColumns, List<ISqlColumnExpression> updateColumns) {
        IDialect dialect = config.getDisambiguation();
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        String tableName = dialect.disambiguationTableName(metaData.getTableName());
        builder.append(tableName);
        builder.append(" (");
        List<String> columnNames = insertColumns
                .stream()
                .map(fm -> dialect.disambiguation(fm.getColumnName()))
                .collect(Collectors.toList());
        builder.append(String.join(",", columnNames));
        builder.append(") VALUES (");
        builder.append(insertColumns.stream().map(f -> {
            if (f.hasLogicColumn()) {
                LogicColumn logicColumn = f.getLogicColumn();
                return logicColumn.onWrite(config);
            }
            else {
                return "?";
            }
        }).collect(Collectors.joining(",")));
        builder.append(")");
        String asNew = dialect.disambiguationTableName("new");
        builder.append(" AS ");
        builder.append(asNew);
        builder.append(" ON DUPLICATE KEY UPDATE ");
        // 如果重复时需要更新的字段为空说明就是忽略更新
        if (updateColumns.isEmpty()) {
            FieldMetaData primary = metaData.getPrimary();
            String primaryKeyName = dialect.disambiguation(primary.getColumnName());
            String set = tableName + "." + primaryKeyName;
            builder.append(set).append(" = ").append(set);
        }
        else {
            List<String> us = updateColumns.stream()
                    .map(u -> dialect.disambiguation(u.getFieldMetaData().getColumnName()))
                    .collect(Collectors.toList());
            builder.append(us.stream()
                    .map(e -> e + " = " + asNew + "." + e)
                    .collect(Collectors.joining(","))
            );
        }

        return builder.toString();
    }
}
