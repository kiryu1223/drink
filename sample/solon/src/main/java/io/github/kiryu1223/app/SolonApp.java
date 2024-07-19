package io.github.kiryu1223.app;

import org.noear.solon.Solon;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.handle.Context;

public class SolonApp
{
    // 这是程序入口
    public static void main(String[] args)
    {
        // 在main函数的入口处，通过 Solon.start(...) 启动Solon的容器服务，进而启动它的所有机能
        Solon.start(SolonApp.class, args);
    }
}
