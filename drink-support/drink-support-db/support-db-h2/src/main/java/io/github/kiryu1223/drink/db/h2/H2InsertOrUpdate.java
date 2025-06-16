package io.github.kiryu1223.drink.db.h2;

import io.github.kiryu1223.drink.base.IConfig;
import io.github.kiryu1223.drink.base.IDialect;
import io.github.kiryu1223.drink.base.IInsertOrUpdate;
import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.MetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class H2InsertOrUpdate implements IInsertOrUpdate
{
    private final IConfig config;

    public H2InsertOrUpdate(IConfig config)
    {
        this.config = config;
    }

    @Override
    public boolean apply()
    {
        return true;
    }

    @Override
    public String insertOrUpdate(MetaData metaData, List<FieldMetaData> onInsertOrUpdateFields, List<ISqlColumnExpression> conflictColumns, List<ISqlColumnExpression> updateColumns)
    {
        IDialect dialect = config.getDisambiguation();

        String tableName = dialect.disambiguationTableName(metaData.getTableName());
        String target = dialect.disambiguation("target");
        String source = dialect.disambiguation("source");

        StringBuilder builder = new StringBuilder();
        builder.append("MERGE INTO ");
        builder.append(tableName);
        builder.append(" AS ");
        builder.append(target);
        builder.append(" USING ( VALUES (");
        builder.append(onInsertOrUpdateFields
                .stream()
                .map(f -> "?")
                .collect(Collectors.joining(","))
        );
        builder.append(")) AS ");
        builder.append(source);
        builder.append(" (");
        builder.append(onInsertOrUpdateFields
                .stream()
                .map(f -> dialect.disambiguation(f.getColumn()))
                .collect(Collectors.joining(","))
        );
        builder.append(") ON ");
        List<String> cs = new ArrayList<>();
        for (ISqlColumnExpression c : conflictColumns)
        {
            String column = dialect.disambiguation(c.getFieldMetaData().getColumn());
            cs.add(target + "." + column + " = " + source + "." + column);
        }
        builder.append(String.join(" AND ", cs));
        builder.append(" ");
        if (!updateColumns.isEmpty())
        {
            builder.append("WHEN MATCHED THEN ");
            builder.append("UPDATE SET ");
            List<String> uc = new ArrayList<>();
            for (ISqlColumnExpression u : updateColumns)
            {
                String column = dialect.disambiguation(u.getFieldMetaData().getColumn());
                uc.add(target + "." + column + " = " + source + "." + column);
            }
            builder.append(String.join(",", uc));
            builder.append(" ");
        }
        builder.append("WHEN NOT MATCHED THEN ");
        builder.append("INSERT (");
        builder.append(onInsertOrUpdateFields
                .stream()
                .map(f -> dialect.disambiguation(f.getColumn()))
                .collect(Collectors.joining(","))
        );
        builder.append(") VALUES (");
        builder.append(onInsertOrUpdateFields
                .stream()
                .map(f -> source + "." + dialect.disambiguation(f.getColumn()))
                .collect(Collectors.joining(","))
        );
        builder.append(");");
        return builder.toString();
    }
}
