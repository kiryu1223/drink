package io.github.kiryu1223.drink.pojos;


import io.github.kiryu1223.drink.base.annotation.Column;

public class Top
{
    @Column("stars")
    private Integer stars;
    private String title;

    public Integer getStars()
    {
        return stars;
    }

    public void setStars(Integer stars)
    {
        this.stars = stars;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
