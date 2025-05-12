package io.github.kiryu1223.drink.base.metaData;


import io.github.kiryu1223.drink.base.annotation.Navigate;
import io.github.kiryu1223.drink.base.annotation.RelationType;

import java.util.Collection;

public class NavigateData
{
    private final Navigate navigate;
    private final Class<?> navigateTargetType;
    private final Class<? extends Collection<?>> navigateCollectionType;

    public NavigateData(Navigate navigate, Class<?> navigateTargetType, Class<? extends Collection<?>> navigateCollectionType)
    {
        this.navigate = navigate;
        this.navigateTargetType = navigateTargetType;
        this.navigateCollectionType = navigateCollectionType;
    }

    public String getSelfFieldName()
    {
        return navigate.self();
    }

    public String getTargetFieldName()
    {
        return navigate.target();
    }

    public RelationType getRelationType()
    {
        return navigate.value();
    }

    public Class<?> getNavigateTargetType()
    {
        return navigateTargetType;
    }

    public boolean isCollectionWrapper()
    {
        return navigateCollectionType != null;
    }

    public Class<? extends Collection<?>> getCollectionWrapperType()
    {
        return navigateCollectionType;
    }

    public Class<? extends IMappingTable> getMappingTableType()
    {
        return navigate.mappingTable();
    }

    public String getSelfMappingFieldName()
    {
        return navigate.selfMapping();
    }

    public String getTargetMappingFieldName()
    {
        return navigate.targetMapping();
    }

    public FieldMetaData getSelfMappingFieldMetaData()
    {
        return MetaDataCache.getMetaData(getMappingTableType()).getFieldMetaDataByFieldName(getSelfMappingFieldName());
    }

    public FieldMetaData getTargetMappingFieldMetaData()
    {
        return MetaDataCache.getMetaData(getMappingTableType()).getFieldMetaDataByFieldName(getTargetMappingFieldName());
    }
}
