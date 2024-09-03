package io.github.kiryu1223.drink.core.visitor;

import java.util.ArrayList;
import java.util.List;

public class ParamMatcher
{
    public List<String> bracesContent = new ArrayList<>();
    public List<String> remainder = new ArrayList<>();

    @Override
    public String toString()
    {
        return bracesContent + "\n" + remainder;
    }
}
