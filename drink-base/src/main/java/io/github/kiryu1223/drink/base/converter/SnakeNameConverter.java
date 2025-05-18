package io.github.kiryu1223.drink.base.converter;

public class SnakeNameConverter extends NameConverter
{
    @Override
    public String convertFieldName(String fieldName)
    {
        return camelToSnake(fieldName);
    }

    public String camelToSnake(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(str.charAt(0)));

        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append('_');
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }
}
