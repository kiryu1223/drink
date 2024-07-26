package io.github.kiryu1223.drink.core.metaData;

import io.github.kiryu1223.drink.annotation.Navigate;
import io.github.kiryu1223.drink.annotation.RelationType;

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

    public String getSelfPropertyName()
    {
        return navigate.self();
    }

    public String getTargetPropertyName()
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
}
