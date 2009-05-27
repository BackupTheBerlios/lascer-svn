/*
 * Dateiname      : SimpleParserTest.java
 * Letzte Änderung: 1. Januar 2005
 * Autoren        : Michael Wohlfart
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2005
 *
 * This file is part of Lascer (http://lascer.berlios.de/).
 *
 * Lascer is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Lascer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Lascer; if not, see <http://www.gnu.org/licenses/>.
 */


package mengenUeberdeckung.tests.bib.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import mengenUeberdeckung.allgemein.ItmFamilie;

import junit.framework.TestCase;

/**
 * parser testcase used to test the parser implementation
 *
 * @author  Michael Wohlfart
 */
public class SimpleParserTest extends TestCase {

    /**
     * platform dependant newline
     */
    private static final String NEWLINE = System.getProperty("line.separator");

    /**
     * simple test with a 50x23 matrix
     */
    public void testSimpleParse1() {

        String testFile =
         "50" + NEWLINE
         + "{4, 6, 12, 24, 28, 29, 31, 32, 33, 35, 37, 39, 40, 45, 48}:1.0" + NEWLINE
         + "{0, 6, 10, 11, 12, 13, 16, 20, 27, 36, 37, 44, 45, 46}:1.0" + NEWLINE
         + "{1, 3, 5, 8, 15, 18, 22, 27, 32, 33, 36, 37, 39, 40, 41, 43, 45, 47, 49}:1.0" + NEWLINE
         + "{0, 8, 10, 12, 14, 15, 16, 20, 21, 27, 38, 40, 42, 45, 48, 49}:1.0" + NEWLINE
         + "{0, 4, 9, 11, 13, 19, 20, 21, 22, 23, 28, 29, 38, 47, 48, 49}:1.0" + NEWLINE
         + "{5, 6, 8, 14, 18, 22, 26, 27, 28, 29, 31, 32, 38, 44, 46, 49}:1.0" + NEWLINE
         + "{3, 4, 6, 7, 9, 21, 27, 28, 29, 33, 37, 39, 44, 45, 46, 47, 49}:1.0" + NEWLINE
         + "{1, 2, 3, 4, 5, 7, 8, 13, 14, 19, 22, 25, 26, 28, 33, 35, 40, 41, 47}:1.0" + NEWLINE
         + "{0, 2, 12, 13, 14, 15, 16, 17, 24, 26, 32, 43, 44}:1.0" + NEWLINE
         + "{3, 10, 11, 15, 16, 17, 18, 22, 23, 25, 31, 36, 37, 42}:1.0" + NEWLINE
         + "{9, 14, 15, 16, 21, 23, 29, 32, 34, 37, 40, 41, 44, 46, 47}:1.0" + NEWLINE
         + "{2, 7, 9, 11, 13, 19, 20, 21, 28, 30, 31, 34, 35, 38, 39, 40, 42, 46, 47}:1.0" + NEWLINE
         + "{0, 1, 2, 5, 8, 10, 18, 19, 22, 27, 32, 36, 37, 38, 42, 45, 49}:1.0" + NEWLINE
         + "{2, 4, 15, 16, 17, 25, 26, 30, 33, 35, 38, 40, 43, 44}:1.0" + NEWLINE
         + "{0, 2, 3, 6, 8, 10, 13, 19, 24, 26, 34, 37, 38, 41, 42, 43, 44}:1.0" + NEWLINE
         + "{1, 4, 6, 8, 10, 11, 12, 15, 18, 19, 23, 24, 34, 37, 45}:1.0" + NEWLINE
         + "{0, 7, 9, 12, 14, 16, 20, 22, 23, 32, 33, 36, 37, 38, 39}:1.0" + NEWLINE
         + "{0, 1, 7, 9, 10, 14, 16, 20, 23, 24, 26, 30, 32, 47}:1.0" + NEWLINE
         + "{2, 4, 5, 15, 18, 20, 21, 24, 25, 32, 33, 34, 35, 38, 42, 43, 48}:1.0" + NEWLINE
         + "{1, 4, 9, 11, 12, 14, 15, 18, 25, 26, 28, 31, 32, 38, 40, 41, 46}:1.0" + NEWLINE
         + "{1, 4, 5, 6, 8, 12, 17, 20, 23, 28, 34, 35, 36, 39, 42, 49}:1.0" + NEWLINE
         + "{3, 5, 9, 14, 16, 17, 20, 21, 22, 25, 28, 32, 34, 45, 48}:1.0" + NEWLINE
         + "{0, 2, 4, 6, 9, 12, 18, 20, 21, 23, 25, 35, 40, 44, 47, 49}:1.0";

        // this whole section is just for putting a simple string into an
        // InputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStreamWriter ow;
        try {
            ow = new OutputStreamWriter(os, "utf-8");
            ow.write(testFile, 0, testFile.length());
            ow.flush();
        } catch (UnsupportedEncodingException e1) {
            fail();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = new ByteArrayInputStream(os.toByteArray());

        // init the parser
        IParser parser = new SimpleParser();
        try {
            ItmFamilie scp = parser.doParse(inputStream);
            assertEquals(23, scp.groesseFamilie());
            assertEquals(50, scp.groesseGesamtmenge());
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        }
    }
}

