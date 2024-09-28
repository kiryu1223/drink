package io.github.kiryu1223.plugin.aot;


import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;

public class AotRegistrarPlugin implements Plugin
{
    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public void init(JavacTask task, String... args)
    {

    }
}
