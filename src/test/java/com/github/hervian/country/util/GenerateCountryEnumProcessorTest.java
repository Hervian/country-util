package com.github.hervian.country.util;

import org.junit.Test;

import junit.framework.TestCase;

public class GenerateCountryEnumProcessorTest extends TestCase {

    @Test
    public void testCountry(){
        Country denmark = Country.DENMARK;
        assertEquals("DK", denmark.getLocale().getCountry());
    }
    
}
