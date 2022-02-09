package com.kxw959.programmingapplication.utils;

import com.kxw959.programmingapplication.user.User;
import org.codehaus.janino.Java;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class CompilerUtil {
    private File classesDir;
    private File sourceDir;
    private String error = "";

    public URLClassLoader loadClassesFromCompiledDirectory() throws Exception {
        return new URLClassLoader(new URL[]{classesDir.toURI().toURL()});
    }

    public void compile(boolean teacher) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), null);
        List<JavaFileObject> javaObjects = scanRecursivelyForJavaObjects(sourceDir, fileManager);
        if (javaObjects.size() == 0) {
            throw new Exception("There are no source files to compile in " + sourceDir.getAbsolutePath());
        }
        if(!teacher){
            javaObjects.removeIf(j -> !Objects.equals(j.getName(), User.selectedTask.start.getValue().replace("/", "\\"))
                    && !Objects.equals(j.getName(), User.selectedTask.test.getValue().replace("/", "\\")));

            for(JavaFileObject j : javaObjects){
                System.out.println(j.getName());
                System.out.println(User.selectedTask.test.getValue().replace("/", "\\"));
                System.out.println(User.selectedTask.start.getValue().replace("/", "\\"));
            }
        }

        System.out.println(classesDir.getAbsolutePath());
        System.out.println(sourceDir.getAbsolutePath());
        String[] compileOptions = new String[]{"-d", classesDir.getAbsolutePath()} ;
        Iterable<String> compilationOptions = Arrays.asList(compileOptions);

        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, diagnostics, compilationOptions, null, javaObjects) ;

        if (!compilerTask.call()) {
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                System.err.format("Error on line %d in %s", diagnostic.getLineNumber(), diagnostic);
                error += "Error on line "+diagnostic.getLineNumber()+" in "+diagnostic;
            }
            throw new Exception(error);
        }
    }

    private List<JavaFileObject> scanRecursivelyForJavaObjects(File dir, StandardJavaFileManager fileManager) {
        List<JavaFileObject> javaObjects = new LinkedList<JavaFileObject>();
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                javaObjects.addAll(scanRecursivelyForJavaObjects(file, fileManager));
            }
            else if (file.isFile() && file.getName().toLowerCase().endsWith(".java")) {
                javaObjects.add(readJavaObject(file, fileManager));
            }
        }
        return javaObjects;
    }


    private JavaFileObject readJavaObject(File file, StandardJavaFileManager fileManager) {
        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(file);
        Iterator<? extends JavaFileObject> it = javaFileObjects.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        throw new RuntimeException("Could not load " + file.getAbsolutePath() + " java file object");
    }

    public File getClassesDir() {
        return classesDir;
    }

    public void setClassesDir(File classesDir) {
        this.classesDir = classesDir;
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
    }
}