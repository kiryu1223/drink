package io.github.kiryu1223.plugin.service;

import com.sun.source.util.TaskEvent;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Context;
import io.github.kiryu1223.expressionTree.ext.IExtensionService;
import org.noear.solon.core.runtime.NativeDetector;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public class DrinkExtensionService implements IExtensionService
{
    private static final String projectVersion = "1.0.5";
    private FileObject aotConfig;
    private boolean nativeImageTime = NativeDetector.inNativeImage();

    @Override
    public void init(Context context)
    {
        System.out.println(NativeDetector.inNativeImage());
        System.out.println(NativeDetector.notInNativeImage());
        System.out.println(NativeDetector.isAotRuntime());
        System.out.println(NativeDetector.isNotAotRuntime());
        if (!nativeImageTime) return;
        try
        {
            createAotFile(context);
            testWrite();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void testWrite() throws IOException
    {
        String data = "[\n" +
                "  {\n" +
                "    \"name\": \"com.github.dudiao.solon.nativex.example.service.impl.EmployeeService$1\",\n" +
                "    \"unsafeAllocated\": true,\n" +
                "    \"allDeclaredFields\": true,\n" +
                "    \"allPublicMethods\": true\n" +
                "  }\n" +
                "]";
        Writer writer = aotConfig.openWriter();
        writer.write(data);
        writer.flush();
        writer.close();
    }

    private void createAotFile(Context context) throws IOException
    {
        Filer filer = JavacProcessingEnvironment.instance(context).getFiler();
        aotConfig = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/native-image/io/github/kiryu1223/drink/" + projectVersion + "/reflect-config.json");
    }

//    private void getOrCreateAotFile(Context context)
//    {
//        JavaFileManager fileManager = context.get(JavaFileManager.class);
//        try
//        {
//            //先尝试获取reflect-config.json
//            aotConfig = fileManager.getFileForOutput(StandardLocation.CLASS_OUTPUT, "", "META-INF/native-image/reflect-config.json", null);
//        }
//        catch (IOException e)
//        {
//            //没有的话再手动创建reflect-config.json
//            Filer filer = JavacProcessingEnvironment.instance(context).getFiler();
//            try
//            {
//                aotConfig = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/native-image/reflect-config.json");
//            }
//            catch (IOException ex)
//            {
//                throw new RuntimeException(ex);
//            }
//        }
//    }

    @Override
    public void started(TaskEvent event) throws Throwable
    {
        if (!nativeImageTime) return;
    }

    @Override
    public void finished(TaskEvent event) throws Throwable
    {
        if (!nativeImageTime) return;
//        if (event.getKind() != TaskEvent.Kind.ANALYZE) return;
//        System.out.println(event.getSourceFile().getName());
//        CompilationUnitTree compilationUnit = event.getCompilationUnit();
//        for (Tree tree : compilationUnit.getTypeDecls())
//        {
//            if (!(tree instanceof JCTree.JCClassDecl)) continue;
//            JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) tree;
//            classDecl.accept(new TreeScanner(){
//                @Override
//                public void visitNewClass(JCTree.JCNewClass newClass)
//                {
//                    if(newClass.getClassBody()!=null)
//                }
//            });
//        }
    }
}
