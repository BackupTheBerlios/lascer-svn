/*
 * Dateiname      : SimpleParser.java
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * implementation of a simple parser for the SCP, the parser
 * can read the format used in the SaveProblemAction
 *
 * @author  Michael Wohlfart
 */
public class SimpleParser implements IParser {

    /**
     * number of identical sets
     */
    private int doubleSets = 0;

    /**
     * the only constructor
     */
    public SimpleParser() {
    }

    /**
     * This method is actually doing the whole work of parsing the input
     * stream.
     *
     * @param input  a input stream to be parsed
     *
     * @return  an instance of a SCP
     *
     * @throws ParseException  thrown if some IO errors or some parsing
     *                         problems occure
     */
    public ItmFamilie doParse(InputStream input) throws ParseException {

        // the result (will be initialized as soon as we know the size)
        ItmFamilie itmFamilie;
        doubleSets = 0;

        int lineCount = 0;
        //TextOutput out = Core.getInstance().getTextOutput();

        try {
            BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(input));
            String line;
            StringTokenizer values;

            // first line is the size
            lineCount++;
            line = bufferedReader.readLine();

            int size = Integer.parseInt(line);

            itmFamilie = new ItmFamilie(size);

            // remember the weight
            float weight = 1f;
            // vector to store element
            Vector vector;

            line = bufferedReader.readLine();
            while (line != null) {
                //System.out.println("dimensions: " + line);
                values = new StringTokenizer(line, ",{} ");
                vector = new Vector();

                while (values.hasMoreTokens()) {
                    String element = values.nextToken();
                    // the weight starts with "[":
                    if (element.startsWith(":")) {
                        element = element.substring(1, element.length());
                        //System.out.println(" element: " + element);
                        weight = Float.parseFloat(element);
                    } else {
                        // we have an element:
                        // assert (element != null);
                        vector.add(element);
                    }
                }

                // skip empty lines:
                if (vector.size() > 0) {

                    // create the set:
                    IndexTeilmenge set = new IndexTeilmenge(size, weight);
                    for (int i = 0; i < vector.size(); i++) {
                        set.indexAufnehmen(
                                   Integer.parseInt((String) vector.get(i)));
                    }
                    if (itmFamilie.enthaelt(set)) {
                        doubleSets++;
                    } else {
                        itmFamilie.teilmengeHinzufuegen(set);
                    }
                }

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (Exception ex) {
            ParseException pex = new ParseException();
            pex.initCause(ex);
            throw pex;
        }
       return itmFamilie;
    }

    /**
     * @return the number of identical sets
     */
    public int getDoubleSets() {
        return doubleSets;
    }
}

