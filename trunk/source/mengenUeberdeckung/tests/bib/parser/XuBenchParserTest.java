/*
 * Dateiname      : XuBenchParserTest.java
 * Letzte Änderung: 23. Juli 2006
 * Autoren        : Michael Wohlfart, Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2006
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
 * Parser testcase used to test the parser implementation.
 *
 * @author  Michael Wohlfart, Dietmar Lippold
 */
public class XuBenchParserTest extends TestCase {

    /**
     * Platform dependant newline.
     */
    private static final String NEWLINE = System.getProperty("line.separator");

    /**
     * Simple test with a 9x12 matrix.
     */
    public void testSimpleParse1() {
        String testFile =
            // comment line
            "c comment" + NEWLINE

            // dimension:   #sets(columns)  #elements(rows)
            + "p set 12 9" + NEWLINE

            // elem1...elem4
            + "s 2 3 7 10" + NEWLINE
            + "s 1 3 8 11" + NEWLINE
            + "s 1 2 9 12" + NEWLINE
            + "s 1 5 6 10" + NEWLINE
            + "s 2 4 6 11" + NEWLINE
            + "s 3 4 5 12" + NEWLINE
            + "s 4 8 9 10" + NEWLINE
            + "s 5 7 9 11" + NEWLINE
            + "s 6 7 8 12" + NEWLINE;

        // This whole section is just for putting a simple string into an
        // InputStream.
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

        // Init the parser.
        IParser parser = new RailParser();
        try {
            ItmFamilie scp = parser.doParse(inputStream);
            assertEquals(12, scp.groesseFamilie());
            assertEquals(9, scp.groesseGesamtmenge());
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        }
    }
}

