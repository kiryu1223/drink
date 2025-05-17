package io.github.kiryu1223.drink.base.toBean.Include;



import io.github.kiryu1223.drink.base.expression.ISqlColumnExpression;
import io.github.kiryu1223.drink.base.expression.ISqlExpression;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;

import java.util.ArrayList;
import java.util.List;

public class IncludeSet
{
    private final FieldMetaData columnMetaData;
    private final ISqlExpression cond;
    private final List<IncludeSet> includeSets = new ArrayList<>();

    public IncludeSet(FieldMetaData columnMetaData, ISqlExpression cond)
    {
        this.columnMetaData = columnMetaData;
        this.cond = cond;
    }

    public IncludeSet(FieldMetaData columnMetaData)
    {
        this(columnMetaData,null);
    }

    public FieldMetaData getColumnMetaData()
    {
        return columnMetaData;
    }

    public ISqlExpression getCond()
    {
        return cond;
    }

    public boolean hasCond()
    {
        return cond != null;
    }

    public List<IncludeSet> getIncludeSets()
    {
        return includeSets;
    }

    public IncludeSet getLastIncludeSet()
    {
        return includeSets.get(includeSets.size() - 1);
    }
}
