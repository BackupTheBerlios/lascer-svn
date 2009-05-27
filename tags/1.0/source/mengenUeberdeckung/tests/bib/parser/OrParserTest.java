/*
 * Dateiname      : OrParserTest.java
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
 * @author Michael Wohlfart
 */
public class OrParserTest extends TestCase {
    /**
     * platform dependant newline
     */
    private static final String NEWLINE = System.getProperty("line.separator");

    /**
     * simple test with a 7x5 matrix
     */
    public void testSimpleParse1() {
        String testFile =
            // dimension:   #elements(rows)  #sets(columns)
            "7 5" + NEWLINE
            // weights
            + " 1 1 1 1 1 " + NEWLINE

            // # columns
            + " 1" + NEWLINE            // row 1:
            // covering columns
            + " 1 " + NEWLINE

            // # columns
            + " 1" + NEWLINE            // row 2
            // columns
            + " 3 " + NEWLINE

            // # columns
            + " 1" + NEWLINE            // row 3
            // columns
            + " 5 " + NEWLINE

            // # columns
            + " 1" + NEWLINE            // row 4
            // columns
            + " 4 " + NEWLINE

            // # columns
            + " 3" + NEWLINE            // row 5
            // columns
            + " 4 3 2" + NEWLINE

            // # columns
            + " 2" + NEWLINE            // row 6
            // columns
            + " 3 1" + NEWLINE

            // # columns
            + " 1" + NEWLINE            // row 7
            // columns
            + " 5 ";

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
        IParser parser = new OrParser();
        try {
            ItmFamilie scp = parser.doParse(inputStream);
            assertEquals(5, scp.groesseFamilie());
            assertEquals(7, scp.groesseGesamtmenge());
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * another soimple parse this time with a 5x7 Matrix
     */
    public void testSimpleParse2() {
        String testFile =
            // dimension:   #elements(rows)  #sets(columns)
            "5 7" + NEWLINE
            // weights for any set
            + " 1 1 1 1 1 1.2 3.4" + NEWLINE

            // # columns
            + " 2" + NEWLINE            // row 1
            // columns
            + " 1 2" + NEWLINE

            // # columns
            + " 2" + NEWLINE            // row 2
            // columns
            + " 2 3 " + NEWLINE

            // # columns
            + " 2" + NEWLINE            // row 3
            // columns
            + " 3 4 " + NEWLINE

            // # columns
            + " 2" + NEWLINE            // row 4
            // columns
            + " 4 5 " + NEWLINE

            // # columns
            + " 2" + NEWLINE            // row 5
            // columns
            + " 5 3 " + NEWLINE;

        // turn string into InputStream
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

        IParser parser = new OrParser();
        try {
            ItmFamilie scp = parser.doParse(inputStream);
            assertEquals(7, scp.groesseFamilie());
            assertEquals(5, scp.groesseGesamtmenge());
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        }
    }
}

