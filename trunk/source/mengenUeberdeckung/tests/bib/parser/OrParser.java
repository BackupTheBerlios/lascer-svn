/*
 * Dateiname      : OrParser.java
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * This class implements the algorithms to parse an Set Covering Problem
 * description obtained from an input stream. The data files are from
 * http://mscmga.ms.ic.ac.uk/info.html. The syntax is described at
 * http://mscmga.ms.ic.ac.uk/jeb/orlib/scpinfo.html.  Note that  there
 * is a second set of data files using a different syntax also described in
 * that file.
 *
 * @author Michael Wohlfart
 */
public class OrParser implements IParser {

    /**
     * number of identical sets
     */
    private int doubleSets = 0;

    /**
     * The only constructor.
     */
    public OrParser() {
    }

    /**
     * This method is actually doing the whole work of parsing the input
     * stream.
     *
     * @param input input stream to be parsed
     *
     * @throws ParseException throws if some IO errors or some parsing problems
     *                        occure
     *
     * @return an instance of a SCP problem
     *
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

            // total number of partial sets
            int matrixColumns = Integer.parseInt(values.nextToken());

            // create the SCP
            itmFamilie = new ItmFamilie(matrixRows);

            // first we parse the column costs:
            int columnCount = 0;
            ArrayList columns = new ArrayList(matrixColumns);
            //out.println(("columns: " + matrixColumns));
            //out.println(("rows: " + matrixRows));

            // each column is a set, with a weight and a number of elements
            while (columnCount < matrixColumns) {
                lineCount++;
                values = new StringTokenizer(bufferedReader.readLine());
                float costs;
                while (values.hasMoreTokens()) {
                    costs = Float.parseFloat(values.nextToken());
                    IndexTeilmenge column =
                        new IndexTeilmenge(matrixRows, costs);
                    columns.add(columnCount, column);
                    columnCount++;
                }
            }

            // each loop parses the data for one row
            // which contains the numbers of the covering columns
            for (int i = 0; i < matrixRows; i++) {
                // we parse a row:
                //  - first element is the number of covering columns
                //  - next elements are the covering columns

                // there should be only one integer in this line:
                lineCount++;
                values = new StringTokenizer(bufferedReader.readLine());
                int coveringColumns = Integer.parseInt(values.nextToken());
                // start counting
                columnCount = 0;
                while (columnCount < coveringColumns) {
                    lineCount++;
                    values = new StringTokenizer(bufferedReader.readLine());
                    while (values.hasMoreTokens()) {
                        int columnCoveredByRowI =
                            (Integer.parseInt(values.nextToken()) - 1);
                        IndexTeilmenge column =
                            (IndexTeilmenge) columns.get(columnCoveredByRowI);
                        column.indexAufnehmen(i);
                        columnCount++;
                    }
                }
            }

            // move the sets and count the double sets
            for (int i = 0; i < matrixColumns; i++) {
                IndexTeilmenge newSet = (IndexTeilmenge) columns.get(i);
                if (itmFamilie.enthaelt(newSet)) {
                    doubleSets++;
                }
                itmFamilie.teilmengeHinzufuegen(newSet);
            }

            // there should be no more data in the stream now
            lineCount++;
            line = bufferedReader.readLine();
            while (line != null) {
                if (line.trim().length() > 0) {
                    // exception if there is anything else but whitespaces left
                    bufferedReader.close();
                    throw new IOException("unparsed Data found");
                }
                line = bufferedReader.readLine();
            }

            // some final checks (disabled by now)
            /*
             if ( matrixColumns != itmFamilie.groesseFamilie() ) {
             throw
             new IOException(
             "number of sets doesn't match with column number");
             }

             if ( matrixRows != itmFamilie.groesseGesamtmenge() ) {
             throw
             new IOException(
             "number of rows doesn't match with element count");
             }
             */

            bufferedReader.close();
        } catch (Exception ex) {
            ParseException pex =
                new ParseException("Exception at line " + lineCount);
            // convert exeption to parse exception
            pex.initCause(ex);
            throw pex;
        }

        return itmFamilie;
    }

    /**
     * There may be identical sets in the input stream, the underlaying
     * implementation of the SCP may ignore those identical sets, so this
     * method gives a hint about how many sets may be removed from the
     * original problem.
     *
     * @return the number of double sets contained in the parsed data
     */
    public int getDoubleSets() {
        return doubleSets;
    }
}

