package io.github.kiryu1223.drink.pojos;

import lombok.Data;

@Data
public class UserDo
{
    private int id;
    private String name;
    private int sex;
    private String headImg;

    private UserAddress address;
    private Area area;
}
