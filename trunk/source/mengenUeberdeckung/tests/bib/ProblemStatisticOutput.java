/*
 * Dateiname      : ProblemStatisticOutput.java
 * Letzte Änderung: 22. März 2007
 * Autoren        : Dietmar Lippold, Michael Wohlfart
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2007
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


package mengenUeberdeckung.tests.bib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import mengenUeberdeckung.tests.bib.parser.IParser;
import mengenUeberdeckung.tests.bib.parser.OrParser;
import mengenUeberdeckung.tests.bib.parser.ParseException;
import mengenUeberdeckung.tests.bib.parser.RailParser;
import mengenUeberdeckung.tests.bib.parser.TripleParser;
import mengenUeberdeckung.tests.bib.parser.XuBenchParser;
import mengenUeberdeckung.tests.bib.parser.SimpleParser;

import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Diese Klasse liest Problemdateien aus einem Verzeichnis ein und gibt deren
 * Statistik aus.
 *
 * @author  Dietmar Lippold, Michael Wohlfart
 */
public class ProblemStatisticOutput {

    /**
     * Liest Problemdateien aus einem Verzeichnis ein und gibt deren Statistik
     * aus.<P>
     *
     * Als Typangabe der Problemdateien sind folgende Angaben zulässig:
     *
     * <DL>
     * <DT>or-bib</DT>
     * <DD>Die Dateien haben das Format der OR-Bibliothek.</DD>
     * <DT>rail-bib</DT>
     * <DD>Die Dateien haben das Format des Wettbewerbs mit den Daten der
     *     Eisenbahngesellschaft.</DD>
     * <DT>triple</DT>
     * <DD>Die Dateien haben das Format eines Steiner triple systems.</DD>
     * <DT>xu-bench</DT>
     * <DD>Die Dateien haben das Format der Benchmarks von Xu.</DD>
     * <DT>setOfSets</DT>
     * <DD>Die Dateien habe das Format, in dem eine Familie von der Methode
     *     <CODE>ItmFamilie.toLibFormat()</CODE> ausgegeben wird.
     * </DL>
     *
     * @param args  Ein Array von Strings, das den Namen des Verzeichnisses
     *              mit den Problemdateien und den Typ der Problemdateien
     *              enthält.
     */
    public static void main(String[] args) {
        FileInputStream stream;
        File[]          problems;
        IParser         parser;
        ItmFamilie      problem;
        String          dirName;
        String          problemTyp;

        if (args.length != 2) {
            System.err.println("Keine zwei Parameter angegeben.");
            System.exit(-1);
        }

        dirName = args[0];
        problemTyp = args[1];

        // Die Dateinamen der Probleme einlesen.
        problems = (new File(dirName)).listFiles();
        if (problems == null) {
            System.err.println("Verzeichnis kann nicht gelesen werden.");
            System.exit(-1);
        }

        // Sortieren der Problemdateien nach ihren Dateinamen.
        Arrays.sort(problems);

        // Den zu verwendenden Parser erzeugen.
        parser = null;
        if (problemTyp.equals("or-bib")) {
            parser = new OrParser();
        } else if (problemTyp.equals("rail-bib")) {
            parser = new RailParser();
        } else if (problemTyp.equals("triple")) {
            parser = new TripleParser();
        } else if (problemTyp.equals("xu-bench")) {
            parser = new XuBenchParser();
        } else if (problemTyp.equals("setOfSets")) {
            parser = new SimpleParser();
        } else {
            System.err.println("Keine zulässige Typangabe für die"
                               + " Problemdateien");
            System.exit(-1);
        }

        // Dateien einlesen und deren Statistik ausgeben.
        for (int i = 0; i < problems.length; i++) {
            try {
                stream = new FileInputStream(problems[i]);
                problem = parser.doParse(stream);

                System.out.println();
                System.out.println("Problem: " + problems[i].getName());
                System.out.println();
                System.out.print("Statistik des Problems");
                System.out.println();
                System.out.print(problem.statistik(1));
                System.out.println();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(-1);

            } catch (ParseException e) {
                System.err.println("Fehler beim Parsen eines Problems.");
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}

