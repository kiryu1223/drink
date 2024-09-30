package io.github.kiryu1223.plugin.service;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TaskEvent;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Options;
import io.github.kiryu1223.drink.annotation.Recode;
import io.github.kiryu1223.expressionTree.expressions.annos.Getter;
import io.github.kiryu1223.expressionTree.expressions.annos.Setter;
import io.github.kiryu1223.expressionTree.ext.IExtensionService;
import org.noear.snack.ONode;
import org.noear.snack.core.Feature;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DrinkExtensionService implements IExtensionService
{
    private static final String projectVersion = "1.0.5";
    private FileObject aotConfig;
    //private boolean aotTime = NativeDetector.inNativeImage();
    private boolean aotTime;
    private List<ClassData> classData = new ArrayList<>();
    private Set<String> classes = new HashSet<>();

    @Override
    public void init(Context context)
    {
        checkAot(context);
        if (!aotTime) return;
        try
        {
            createAotFile(context);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void checkAot(Context context)
    {
        Options options = Options.instance(context);
        String aot = options.get("-Aot");
        aotTime = aot != null;
    }

//    private void testWrite() throws IOException
//    {
//        String data = "[\n" +
//                "  {\n" +
//                "    \"name\": \"com.github.dudiao.solon.nativex.example.service.impl.EmployeeService$1\",\n" +
//                "    \"unsafeAllocated\": true,\n" +
//                "    \"allDeclaredFields\": true,\n" +
//                "    \"allPublicMethods\": true\n" +
//                "  }\n" +
//                "]";
//        Writer writer = aotConfig.openWriter();
//        writer.write(data);
//        writer.flush();
//        writer.close();
//    }

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
        if (!aotTime) return;
    }

    @Override
    public void finished(TaskEvent event) throws Throwable
    {
        if (!aotTime) return;
        recodeAnonymousClasses(event);
        writeData(event);
    }

    private void recodeAnonymousClasses(TaskEvent event)
    {
        if (event.getKind() == TaskEvent.Kind.ANALYZE)
        {
            CompilationUnitTree compilationUnit = event.getCompilationUnit();
            for (Tree tree : compilationUnit.getTypeDecls())
            {
                if (!(tree instanceof JCTree.JCClassDecl)) continue;
                JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) tree;
                classDecl.accept(new TreeScanner()
                {
                    @Override
                    public void visitNewClass(JCTree.JCNewClass newClass)
                    {
                        // 匿名类
                        if (newClass.getClassBody() != null)
                        {
                            JCTree.JCClassDecl classBody = newClass.getClassBody();
                            //获取父类符号
                            Symbol.ClassSymbol classSymbol = classBody.getExtendsClause().type.asElement().enclClass();
                            Getter getter = classSymbol.getAnnotation(Getter.class);
                            Setter setter = classSymbol.getAnnotation(Setter.class);
                            //检查是否有标记
                            if (getter != null || setter != null)
                            {
                                classData.add(new AnonymousClassData(classBody.sym.flatName().toString()));
                            }
                        }
                        else
                        {
                            super.visitNewClass(newClass);
                        }
                    }

                    @Override
                    public void visitApply(JCTree.JCMethodInvocation methodInvocation)
                    {
                        JCTree.JCExpression methodSelect = methodInvocation.getMethodSelect();
                        Symbol.MethodSymbol methodSymbol;
                        if (methodSelect instanceof JCTree.JCFieldAccess)
                        {
                            JCTree.JCFieldAccess select = (JCTree.JCFieldAccess) methodSelect;
                            methodSymbol = (Symbol.MethodSymbol) select.sym;
                        }
                        else
                        {
                            JCTree.JCIdent select = (JCTree.JCIdent) methodSelect;
                            methodSymbol = (Symbol.MethodSymbol) select.sym;
                        }
                        if (methodSymbol.getAnnotation(Recode.class) != null)
                        {
                            JCTree.JCExpression arg = methodInvocation.getArguments().get(0);
                            Type.ClassType classType = (Type.ClassType) arg.type;
                            Type type = classType.getTypeArguments().get(0);
                            classes.add(type.toString());
//                            if (arg instanceof JCTree.JCFieldAccess)
//                            {
//                                JCTree.JCFieldAccess _class = (JCTree.JCFieldAccess) arg;
//                                Symbol.VarSymbol varSymbol = (Symbol.VarSymbol) _class.sym;
//                                System.out.println(varSymbol.type);
//                                System.out.println(varSymbol.type.getClass());
////                                System.out.println(_class);
////                                System.out.println(_class.sym);
////                                System.out.println(_class.sym.getClass());
////                                JCTree.JCExpression expression = _class.getExpression();
////                                System.out.println(expression);
////                                System.out.println(expression.getClass());
//                            }
                        }
//                        Type.MethodType methodType =  methodSelect.type.asMethodType();
//                        Symbol.TypeSymbol tsym = methodType.tsym;
//                        Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol) tsym;
//                        System.out.println(tsym.getClass());
                        super.visitApply(methodInvocation);
                    }
                });
            }
        }
    }

    private boolean isWrite = false;

    private void writeData(TaskEvent event) throws IOException
    {
        if (event.getKind().name().equals("COMPILATION") && !isWrite)
        {
            isWrite = true;
            for (String name : classes)
            {
                classData.add(new ClassData(name));
            }
            String stringify = ONode.stringify(classData, org.noear.snack.core.Options.def().add(Feature.PrettyFormat));
            Writer writer = aotConfig.openWriter();
            writer.write(stringify);
            writer.flush();
            writer.close();
        }
    }
}
