package io.github.kiryu1223.app.pojos;

import lombok.Data;

@Data
public class User
{
    private int id;
    private String username;
    private String info;
    private int balance;
}
