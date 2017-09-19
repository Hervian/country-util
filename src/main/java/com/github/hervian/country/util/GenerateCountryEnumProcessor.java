package com.github.hervian.country.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

public class GenerateCountryEnumProcessor extends AbstractProcessor {

    private Filer filer;
    private static boolean fileCreated;
    
    private static final String PACKAGE = "com.github.hervian.country.util";
    private static final String NEWLINE = "\n";
    private static final String NEWLINE_TAB = "\n\t";
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(GenerateCountryEnum.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver() && !fileCreated) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(GenerateCountryEnum.class);
            for (Element element : elements) {
                if (element.getKind() == ElementKind.CLASS) {
                    GenerateCountryEnum generateSignatureContainerAnnotation = element.getAnnotation(GenerateCountryEnum.class);
                    generateCode(PACKAGE, "Country", generateSignatureContainerAnnotation);
                    return true;
                }
                break;
            }
        }
        return true;
    }
    
    private void generateCode(String packageOfMarkerClass, String className, GenerateCountryEnum generateSignatureContainerAnnotation) {
        String fqcn = packageOfMarkerClass + "." + className;
        try (Writer writer = filer.createSourceFile(fqcn).openWriter()) {
            StringBuilder javaFile = new StringBuilder();
            javaFile.append("package ").append(packageOfMarkerClass).append(";").append(NEWLINE).append(NEWLINE);
            
            javaFile.append("import java.util.Locale;");
            
            javaFile.append("\n\n/**\n * Copyright 2016 Anders Granau Høfft"
                    + "\n * The invocation methods throws an AbstractMethodError, if arguments provided does not match "
                    + "\n * the type defined by the Method over which the lambda was created."
                    + "\n * A typical example of this is that the caller forget to cast a primitive number to its proper type. "
                    + "\n * Fx. forgetting to explicitly cast a number as a short, byte etc. "
                    + "\n * The AbstractMethodException will also be thrown if the caller does not provide"
                    + "\n * an Object instance as the first argument to a non-static method, and vice versa."
                    + "\n * @author Anders Granau Høfft").append("\n */")
                            .append("\n@javax.annotation.Generated(value=\"com.hervian.lambda.GenerateLambdaProcessor\", date=\"").append(new Date()).append("\")")
                            .append("\npublic enum " + className + "{\n");
            generateEnums(javaFile, className, generateSignatureContainerAnnotation);
            javaFile.append("\n}");
            writer.write(javaFile.toString());
            fileCreated = true;
        } catch (IOException e) {
            throw new RuntimeException("An exception occurred while generating the source file " + className, e);
        }
    }

    private void generateEnums(StringBuilder javaFile, String className, GenerateCountryEnum generateSignatureContainerAnnotation) {
        javaFile.append(NEWLINE_TAB);
        String[] countryCodes = Locale.getISOCountries();
        for (int i=0; i<countryCodes.length; i++){
            String countryCode = countryCodes[i];
            Locale locale = new Locale("", countryCode);
            javaFile.append(getCountryEnumName(locale)).append("(new Locale(\"").append(countryCode).append("\"))");
            if (i!=countryCodes.length-1){
                javaFile.append(",");
            }
            javaFile.append(NEWLINE_TAB); //DENMARK(new Locale("DK"));
        }
        javaFile.append(";");
        
        javaFile.append(NEWLINE).append(NEWLINE_TAB);
        javaFile.append("private Locale locale;"); //private Locale locale;
        javaFile.append(NEWLINE).append(NEWLINE_TAB);
        javaFile.append("private ").append(className).append("(Locale locale){").append(NEWLINE_TAB); //private tmp(Locale locale){
        javaFile.append("\tthis.locale = locale;").append(NEWLINE_TAB);     //  this.locale = locale;
        javaFile.append("}").append(NEWLINE_TAB);                           //}       
        javaFile.append(NEWLINE);
//        for (String country : Locale.getISOCountries()){
//            javaFile.append(country).append(",\n");
//        }
//        javaFile.append(";");
    }

    private String getCountryEnumName(Locale locale) {
        String countryNameAsEnumName = locale.getDisplayCountry().toUpperCase()
                .replaceAll("[() '-]", "_")
                .replaceAll("\\.", "")
                .replaceAll(",", "");
                ;
        return countryNameAsEnumName;
    }

}
