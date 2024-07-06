package io.github.kiryu1223.drink.core.context;

import io.github.kiryu1223.drink.config.Config;

import java.util.List;

public class SqlFunctionsContext extends SqlContext
{
    private final List<String> functions;
    private final List<SqlContext> contexts;

    public SqlFunctionsContext(List<String> functions, List<SqlContext> contexts)
    {
        this.functions = functions;
        this.contexts = contexts;
    }

    @Override
    public String getSqlAndValue(Config config, List<Object> values)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < functions.size(); i++)
        {
            String function = functions.get(i);
            sb.append(function);
            if (i < contexts.size())
            {
                SqlContext context = contexts.get(i);
                sb.append(context.getSqlAndValue(config, values));
            }
        }
        return sb.toString();
    }

    @Override
    public String getSql(Config config)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < functions.size(); i++)
        {
            String function = functions.get(i);
            sb.append(function);
            if (i < contexts.size())
            {
                SqlContext context = contexts.get(i);
                sb.append(context.getSql(config));
            }
        }
        return sb.toString();
    }
}
