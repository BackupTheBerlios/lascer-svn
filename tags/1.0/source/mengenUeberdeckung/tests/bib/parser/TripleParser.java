/*
 * Dateiname      : TripleParser.java
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
 * Parser for the Steiner triple system files from a OR Library.
 *
 * Implementation according to the file READ.ME out of
 * <KDB>http://www.research.att.com/~mgcr/data/steiner-triples.tar.gz</KBD>.
 *
 * @author  Michael Wohlfart, Dietmar Lippold
 */
public class TripleParser implements IParser {

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
        ItmFamilie       itmFamilie;
        IndexTeilmenge[] sets;
        int              lineCount;

        doubleSets = 0;
        lineCount = 0;

        try {
            BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(input));
            String          line;
            StringTokenizer values;
            int             setNumber;

            // first line is matrix size and should look like this: m n
            // m is the number of columns, i.e. the number of partial sets.
            // n is the number of rows, i.e. the size of the basic set.
            lineCount++;
            line = bufferedReader.readLine();
            values = new StringTokenizer(line);

            // must be two tokens here.
            if (values.countTokens() != 2) {
                bufferedReader.close();
                throw new ParseException("Undefined matrix dimension at line "
                                         + lineCount);
            }

            // total number of partial sets:
            int matrixColumns = Integer.parseInt(values.nextToken());
            //System.out.println("columns: " + matrixColumns);

            // total elements in the target set:
            int matrixRows = Integer.parseInt(values.nextToken());
            //System.out.println("elements: " + matrixRows);

            sets = new IndexTeilmenge[matrixColumns];
            itmFamilie = new ItmFamilie(matrixRows);

            // create the sets.
            for (int i = 0; i < matrixColumns; i++) {
                // the weight of all sets is one.
                sets[i] = new IndexTeilmenge(matrixRows);
            }

            // read the numbers from the file.
            for (int tripleCount = 0; tripleCount < matrixRows; tripleCount++) {
                lineCount++;

                // one line for each column, consists of
                // setNumber1, setNumber2, setNumber3
                line = bufferedReader.readLine();
                values = new StringTokenizer(line.trim());

                // must be three tokens here.
                if (values.countTokens() != 3) {
                    bufferedReader.close();
                    throw new ParseException("Invalid number of values at"
                                             + " line " + lineCount);
                }

                for (int j = 0; j < 3; j++) {
                    String next = values.nextToken();
                    //System.out.println("next: " + next);

                    // the couting in the file starts at 1.
                    setNumber = Integer.parseInt(next) - 1;
                    sets[setNumber].indexAufnehmen(tripleCount);
                }
            }

            // put the sets into the family.
            for (int i = 0; i < matrixColumns; i++) {
                if (itmFamilie.enthaelt(sets[i])) {
                    doubleSets++;
                } else {
                    itmFamilie.teilmengeHinzufuegen(sets[i]);
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

