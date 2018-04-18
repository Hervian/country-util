package com.github.hervian.country.util;

import java.io.File;
import java.util.Locale;

import org.junit.Test;

import junit.framework.TestCase;

public class GenerateCountryEnumProcessorTest extends TestCase {

    @Test
    public void testCountry(){
        Country denmark = Country.DENMARK;
        for (Locale locale: denmark.getLocales()) {
            if (!locale.getCountry().equals("DK")) {
                fail();
            }
        }
    }

    
}
