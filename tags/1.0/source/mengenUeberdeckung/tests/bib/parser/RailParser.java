/*
 * Dateiname      : RailParser.java
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

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * parser for the train files from the OR Library
 *
 * Implementation according to the second syntax described in
 * <KDB>http://mscmga.ms.ic.ac.uk/jeb/orlib/scpinfo.html</KBD>.
 *
 * @author  Michael Wohlfart
 */
public class RailParser implements IParser {

    /**
     * number of identical sets
     */
    private int doubleSets = 0;

    /**
     * parse the input stream and return an instance of a SCP
     *
     * @param input  the input stream to be parsed
     *
     * @return  an instance of a SCP
     *
     * @throws ParseException  if anything goes wrong here
     */
    public ItmFamilie doParse(InputStream input) throws ParseException {
        // the result (will be initialized as soon as we know the size)
        ItmFamilie itmFamilie;
        doubleSets = 0;

        int lineCount = 0;

        try {
            BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(input));
            String line;
            StringTokenizer values;

            // first line is matrix size and should look like this: n m
            lineCount++;
            line = bufferedReader.readLine();
            //System.out.println("dimensions: " + line);
            values = new StringTokenizer(line);

            // must be two tokens here:
            if (2 != values.countTokens()) {
                bufferedReader.close();
                throw new ParseException("undefined matrix dimension at line "
                        + lineCount);
            }

            // total elements in the target set:
            int matrixRows = Integer.parseInt(values.nextToken());
            //System.out.println("elements: " + matrixRows);

            // total number of partial sets
            int matrixColumns = Integer.parseInt(values.nextToken());
            //System.out.println("columns: " + matrixColumns);

            itmFamilie = new ItmFamilie(matrixRows);

            IndexTeilmenge set;

            float weight;
            int elements, element;

            for (int i = 0; i < matrixColumns; i++) {
                // one line for each column, consists of
                // cost number of element, element0.. elementn
                line = bufferedReader.readLine();
                values = new StringTokenizer(line.trim());

                weight = Float.parseFloat(values.nextToken());
                //System.out.println("weight: " + weight);

                elements = Integer.parseInt(values.nextToken());

                set = new IndexTeilmenge(matrixRows, weight);

                for (int j = 0; j < elements; j++) {
                    String next = values.nextToken();
                    //System.out.println("next: " + next);
                    element = Integer.parseInt(next) - 1;
                    // the coutning in the file starts at 1
                    set.indexAufnehmen(element);
                }

                itmFamilie.teilmengeHinzufuegen(set);
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
     * Return the number of identical sets in the input stream.
     *
     * @return  number of identical sets
     */
    public int getDoubleSets() {
        return doubleSets;
    }
}

