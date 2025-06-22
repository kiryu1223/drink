package io.github.kiryu1223.project.pojos;

import io.github.kiryu1223.drink.base.annotation.IJsonObject;
import io.github.kiryu1223.drink.base.annotation.JsonObject;
import lombok.Data;

@Data
public class Json implements IJsonObject
{
    private int aaa;
    private int bbb;
    private int ccc;
}
