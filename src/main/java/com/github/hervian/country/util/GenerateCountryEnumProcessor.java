package com.github.hervian.country.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.imageio.ImageIO;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * Annotation scanner that creates a new source file: Country, which is populated with country enums.
 * Each enum will have methods to access the Locales for that country and a method for retriving the country's flag.
 * Please note that the images are taken from: https://github.com/oxguy3/flags which also contains a reference to an API where one can query for flags.
 * @author Anders Granau Høfft
 *
 */
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
            
            javaFile.append("import java.util.Locale;").append(NEWLINE);
            javaFile.append("import java.awt.Image;");
            
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
            javaFile.append(getCountryEnumName(locale)).append("(GenerateCountryEnumProcessor.getFlagForCountry(\"").append(countryCode).append("\"), ").append("new Locale[]{").append(getLocalesAsArrayElements(countryCode)).append("})");
            if (i!=countryCodes.length-1){
                javaFile.append(",");
            }
            javaFile.append(NEWLINE_TAB); //DENMARK(new Locale("DK"));
        }
        javaFile.append(";");
        
        javaFile.append(NEWLINE).append(NEWLINE_TAB);
        javaFile.append("private Locale[] locales;"); //private Locale locale;
        javaFile.append("private Image flag;");
        javaFile.append(NEWLINE).append(NEWLINE_TAB);
        javaFile.append("private ").append(className).append("(Image flag, Locale[] locales){").append(NEWLINE_TAB); //private tmp(Locale locale){
        javaFile.append("\tthis.locales = locales;").append(NEWLINE_TAB);     //  this.locale = locale;
        javaFile.append("}").append(NEWLINE_TAB);                           //}       
        javaFile.append(NEWLINE).append(NEWLINE_TAB);

        javaFile.append("public Locale[] getLocales(){").append(NEWLINE_TAB);
        javaFile.append("\treturn locales;").append(NEWLINE_TAB);
        javaFile.append("}").append(NEWLINE_TAB);
        
        javaFile.append("public Image getFlag(){").append(NEWLINE_TAB);
        javaFile.append("\treturn flag;").append(NEWLINE_TAB);
        javaFile.append("}").append(NEWLINE_TAB);
    }

    private String getCountryEnumName(Locale locale) {
        String countryNameAsEnumName = locale.getDisplayCountry().toUpperCase()
                .replaceAll("[() '-]", "_")
                .replaceAll("\\.", "")
                .replaceAll(",", "")
                .replaceAll("&", "and")
                .replaceAll("’", "");
                
        return countryNameAsEnumName;
    }
    
    public String getLocalesAsArrayElements(String iso3166CountryCode) {
        StringBuilder sb = new StringBuilder();
        List<Locale> localesForCountry = getLocalesForCountryCode(iso3166CountryCode);
        for (Locale locale: localesForCountry) {
            if (sb.length()>0) {
                sb.append(",");
            }
            sb.append("new Locale(\"").append(locale.getISO3Language()).append("\",\"").append(locale.getCountry()).append("\"").append(")");
        }
        return sb.toString();
    }
    
    private List<Locale> getLocalesForCountryCode(String iso3166CountryCode) {
        List<Locale> localesForCountry = new ArrayList<>();
        for (Locale locale: Locale.getAvailableLocales()) {
            if (locale.getCountry().equals(iso3166CountryCode)) {
                localesForCountry.add(locale);
            }
        }
        return localesForCountry;
    }
    
    static BufferedImage getFlagForCountry(String iso3166CountryCode) {
        try {
            File flagFile = getFlagFile(iso3166CountryCode);
            if (flagFile==null) {
                flagFile = getFlagFile("unknown");
            }
            return ImageIO.read(flagFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    static File getFlagFile(String iso3166CountryCode) {
        URL urlToFile = GenerateCountryEnumProcessor.class.getClassLoader().getResource("svg/"+iso3166CountryCode.toLowerCase() + ".svg");
        String fileName = urlToFile==null ? "" : urlToFile.getFile();
        return fileName.isEmpty() ? null : new File(fileName);
    }

}
