package io.github.kiryu1223.drink.core.sqlBuilder;

import io.github.kiryu1223.drink.base.annotation.RelationType;
import io.github.kiryu1223.drink.base.expression.ISqlQueryableExpression;
import io.github.kiryu1223.drink.base.expression.ISqlTableRefExpression;
import io.github.kiryu1223.drink.base.expression.SqlExpressionFactory;
import io.github.kiryu1223.drink.base.expression.SqlOperator;
import io.github.kiryu1223.drink.base.metaData.FieldMetaData;
import io.github.kiryu1223.drink.base.metaData.IMappingTable;
import javafx.util.Pair;

import java.util.*;

public class SubQueryBuilder {

    private String fieldName;
    private ISqlQueryableExpression subQuery;
    private boolean toMany;

    private List<SubQueryBuilder> subQueryList = new ArrayList<>();
    private FieldMetaData include;
    private ISqlQueryableExpression query;


    public void setSubQuery(ISqlQueryableExpression subQuery) {
        this.subQuery = subQuery;
    }

    public void setInclude(FieldMetaData include) {
        this.include = include;
    }

    public List<SubQueryBuilder> getSubQueryList() {
        return subQueryList;
    }

    public void setQuery(ISqlQueryableExpression queryableExpression) {
        this.query = queryableExpression;
    }

    public void includeToSubQuery() {
        SqlExpressionFactory factory = getConfig().getSqlExpressionFactory();
        String selfFieldName = navigateData.getSelfFieldName();
        String targetFieldName = navigateData.getTargetFieldName();
        Class<?> selfType = fieldMetaData.getParentType();
        Class<?> targetType = navigateData.getNavigateTargetType();
        FieldMetaData selfField = getConfig().getMetaData(selfType).getFieldMetaDataByFieldName(selfFieldName);
        FieldMetaData targetField = getConfig().getMetaData(targetType).getFieldMetaDataByFieldName(targetFieldName);
        RelationType relationType = navigateData.getRelationType();

        ISqlQueryableExpression subQuery = factory.queryable(targetType);
        // A.B
        // 一对一，多对一的场合
        // SELECT B.* FROM B WHERE B.targetField IN A.selfField(?)
        if (relationType == RelationType.ManyToOne || relationType == RelationType.OneToOne) {
            subQuery.addWhere(factory.binary(SqlOperator.IN, factory.column(targetField, subQuery.getFrom().getTableRefExpression()), factory.column(selfField, selfISqlTableRefExpression)));
        }
        // many to many的场合
        // SELECT B.* FROM B WHERE B.targetField IN (SELECT M.targetMapping FROM M WHERE M.selfMapping = A.selfField)
        // TODO:优化成以下
        // SELECT B.* FROM B INNER JOIN M ON B.targetField = M.targetMapping WHERE M.selfMapping = A.selfField
        // one to many的场合 [A IN {B1,B2,B3,...}]
        // SELECT B.* FROM B WHERE B.targetField = A.selfField
        else {
            FieldMetaData selfMappingField = navigateData.getSelfMappingFieldMetaData(config);
            FieldMetaData targetMappingField = navigateData.getTargetMappingFieldMetaData(config);
            Class<? extends IMappingTable> mappingType = navigateData.getMappingTableType();
            ISqlQueryableExpression mappingQuery = factory.queryable(mappingType);
            ISqlTableRefExpression mappingTableRef = mappingQuery.getFrom().getTableRefExpression();
            mappingQuery.addWhere(factory.binary(SqlOperator.EQ, factory.column(selfMappingField, mappingTableRef), factory.column(selfField, selfISqlTableRefExpression)));
            mappingQuery.setSelect(factory.select(new ArrayList<>(Collections.singletonList(factory.column(targetMappingField, mappingTableRef))), targetMappingField.getType()));
            subQuery.addWhere(factory.binary(SqlOperator.IN, factory.column(targetField, subQuery.getFrom().getTableRefExpression()), mappingQuery));
        }
    }
}
