package io.github.kiryu1223.drink.base.annotation;

public enum RelationType
{
    OneToOne,
    OneToMany,
    ManyToOne,
    ManyToMany;

    public boolean toMany()
    {
        return this == OneToMany || this == ManyToMany;
    }
}
