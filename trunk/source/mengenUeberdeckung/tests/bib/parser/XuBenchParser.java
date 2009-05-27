/*
 * Dateiname      : XuBenchParser.java
 * Letzte Änderung: 5. April 2006
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Parser for the benchmark files of Xu.
 *
 * Implementation according to the description on
 * <KDB>http://www.nlsde.buaa.edu.cn/~kexu/benchmarks/set-benchmarks.htm</KBD>.
 *
 * @author  Michael Wohlfart, Dietmar Lippold
 */
public class XuBenchParser implements IParser {

    /**
     * Number of identical sets.
     */
    private int doubleSets = 0;

    /**
     * Parse the input stream and return an instance of a SCP.
     *
     * @param input  the input stream to be parsed.
     *
     * @return  an instance of a SCP.
     *
     * @throws ParseException  if anything goes wrong here
     */
    public ItmFamilie doParse(InputStream input) throws ParseException {
        // the result (will be initialized as soon as we know the size)
        ItmFamilie     itmFamilie;
        IndexTeilmenge set;
        int            lineCount;

        doubleSets = 0;
        lineCount = 0;

        try {
            BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(input));
            StringTokenizer values;
            String          line;
            String          type, dateFormat;
            int             matrixColumns, matrixRows;
            int             element;
            int             valueNumer;

            // the first lines are comment lines or should look like this:
            // p set n m
            // n is the number of rows, i.e. the size of the basic set.
            // m is the number of columns, i.e. the number of partial sets.
            do {
                lineCount++;

                line = bufferedReader.readLine();
                values = new StringTokenizer(line);
                valueNumer = values.countTokens();
                type = values.nextToken();

                // check the type of the line.
                if (!type.equals("c") && !type.equals("p")) {
                    bufferedReader.close();
                    throw new ParseException("Undefined type of line at line "
                                             + lineCount + " : " + type);
                }

                if (valueNumer != 4) {
                    bufferedReader.close();
                    throw new ParseException("Invalid number of tokens at line "
                                             + lineCount + " : " + valueNumer);
                }

                dateFormat = values.nextToken();
                if (!dateFormat.equals("set")) {
                    bufferedReader.close();
                    throw new ParseException("Undefined format at line "
                                             + lineCount + " : " + dateFormat);
                }
            } while (!type.equals("p"));

            // read total elements in the target set.
            matrixRows = Integer.parseInt(values.nextToken());
            //System.out.println("elements: " + matrixRows);

            // read total number of partial sets.
            matrixColumns = Integer.parseInt(values.nextToken());
            //System.out.println("columns: " + matrixColumns);

            itmFamilie = new ItmFamilie(matrixRows);

            // read the numbers from the file.
            for (int setCount = 0; setCount < matrixColumns; setCount++) {
                lineCount++;

                // one line for each column, consists of
                // setNumber1, setNumber2, setNumber3
                line = bufferedReader.readLine();
                values = new StringTokenizer(line.trim());

                type = values.nextToken();

                // the first token must be "s".
                if (!type.equals("s")) {
                    bufferedReader.close();
                    throw new ParseException("Invalid typ of line at line "
                                             + lineCount + " : " + type);
                }

                // the weight of all sets is one.
                set = new IndexTeilmenge(matrixRows);

                // read the elements of the set.
                while (values.hasMoreTokens()) {
                    String next = values.nextToken();
                    //System.out.println("next: " + next);

                    // the couting in the file starts at 1.
                    element = Integer.parseInt(next) - 1;
                    set.indexAufnehmen(element);
                }

                if (itmFamilie.enthaelt(set)) {
                    doubleSets++;
                } else {
                    itmFamilie.teilmengeHinzufuegen(set);
                }
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

