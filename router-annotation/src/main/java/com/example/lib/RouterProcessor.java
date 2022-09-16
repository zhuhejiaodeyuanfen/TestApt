package com.example.lib;

import com.example.routerlib.Router;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        String pkgName = null;
        //首先获取注解元素
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Router.class);

        if (elements.isEmpty())
            return false;
        //定义一个public static 类型的 map方法
        MethodSpec.Builder mapMethod = MethodSpec
                .methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        //遍历注解元素
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                pkgName = String.valueOf(processingEnv.getElementUtils()
                        .getPackageOf(element)
                        .getQualifiedName());
                Router router = element.getAnnotation(Router.class);
                //获取activity的 class name
                ClassName className = ClassName.get((TypeElement) element);
                //获取uri地址
                String path = router.value();
                //生成代码Routers.map(uri,xxx.Class);
                ClassName router1 = ClassName.get("com.example.mylibrary", "MyRouter");
                mapMethod.addStatement("$T.add($S,$T.class)", router1, path, className);
            }
        }
        mapMethod.addCode("\n");

        //生成RouterMapping文件
        TypeSpec helloWorldClass = TypeSpec.classBuilder("RouterMapping")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(mapMethod.build())
                .build();

        assert pkgName != null;
        JavaFile javaFile = JavaFile.builder(pkgName, helloWorldClass).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Router.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}