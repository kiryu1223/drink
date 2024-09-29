package io.github.kiryu1223.plugin.service;

import com.sun.source.util.TaskEvent;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Context;
import io.github.kiryu1223.expressionTree.ext.IExtensionService;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.io.*;

public class DrinkExtensionService implements IExtensionService
{
    Filer filer;
    FileObject myConfig;
    @Override
    public void init(Context context)
    {
        JavacProcessingEnvironment processingEnvironment = JavacProcessingEnvironment.instance(context);
        filer = processingEnvironment.getFiler();
        JavaFileManager fileManager = context.get(JavaFileManager.class);

        try
        {
            myConfig = filer.getResource(StandardLocation.CLASS_PATH, "", "META-INF/native-image/resource-config.json");
            System.out.println(myConfig.getCharContent(false));
            myConfig = fileManager.getFileForOutput(StandardLocation.CLASS_OUTPUT,
                    "",
                    "META-INF/native-image/resource-config.json",
                    null);

            try (Writer writer = myConfig.openWriter())
            {
                writer.append("11111");
                writer.flush();
                System.out.println("写入成功");
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void started(TaskEvent event) throws Throwable
    {
//        try
//        {
//            System.out.println(event.getKind());
//            BufferedWriter bufferedWriter = new BufferedWriter(myConfig.openWriter());
//            bufferedWriter.newLine();
//            bufferedWriter.append("11111");
//            System.out.println("写入成功");
//        }
//        catch (Throwable e)
//        {
//            System.out.println("写入失败");
//        }
    }

    @Override
    public void finished(TaskEvent event) throws Throwable
    {
//        try
//        {
//            System.out.println(event.getKind());
//            BufferedWriter bufferedWriter = new BufferedWriter(myConfig.openWriter());
//            bufferedWriter.newLine();
//            bufferedWriter.append("11111");
//            System.out.println("写入成功");
//        }
//        catch (Throwable e)
//        {
//            System.out.println("写入失败");
//        }
    }
}
