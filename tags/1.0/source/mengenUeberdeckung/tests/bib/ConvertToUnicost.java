/*
 * Dateiname      : ConvertToUnicost.java
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
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Iterator;

import mengenUeberdeckung.tests.bib.parser.IParser;
import mengenUeberdeckung.tests.bib.parser.OrParser;
import mengenUeberdeckung.tests.bib.parser.ParseException;
import mengenUeberdeckung.tests.bib.parser.RailParser;
import mengenUeberdeckung.tests.bib.parser.TripleParser;
import mengenUeberdeckung.tests.bib.parser.XuBenchParser;
import mengenUeberdeckung.tests.bib.parser.SimpleParser;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Diese Klasse liest Problemdateien aus einem Verzeichnis ein, konvertiert
 * sie zu Unicost-Problemen und speichert diese in einem anderen Verzeichnis.
 *
 * @author  Dietmar Lippold, Michael Wohlfart
 */
public class ConvertToUnicost {

    /**
     * Liefert zum übergebenen Problem das entsprechende Unicost-Problem, das
     * also Teilmengen mit den gleichen Indices aber jeweils mit den Kosten
     * Eins hat.
     *
     * @param problem  Das zu konvertierende Problem
     *
     * @return  Das neu erzeugte Unicost-Problem.
     */
    private static ItmFamilie konvProblem(ItmFamilie problem) {
        Iterator       itmIter;
        ItmFamilie     konvProblem;
        IndexTeilmenge alteTeilmenge, neueTeilmenge;

        konvProblem = new ItmFamilie(problem.groesseGesamtmenge());

        itmIter = problem.iterator();
        while (itmIter.hasNext()) {
            alteTeilmenge = (IndexTeilmenge) itmIter.next();
            neueTeilmenge = new IndexTeilmenge(alteTeilmenge, 1, false);
            konvProblem.teilmengeHinzufuegen(neueTeilmenge);
        }

        return konvProblem;
    }

    /**
     * Liest Problemdateien aus einem Verzeichnis ein, konvertiert sie zu
     * Unicost-Problemen und speichert diese in einem anderen Verzeichnis.
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
     *              mit den Problemdateien, den Typ der Problemdateien und den
     *              Namen des Verzeichnisses für die konvertierten Dateien
     *              enthält.
     */
    public static void main(String[] args) {
        FileInputStream stream;
        FileWriter      fileWriter;
        File[]          problemFiles;
        File            konvProblemFile;
        IParser         parser;
        ItmFamilie      problem;
        String          inDirName, outDirName;
        String          problemTyp;

        if (args.length != 3) {
            System.err.println("Keine drei Parameter angegeben.");
            System.exit(-1);
        }

        inDirName = args[0];
        problemTyp = args[1];
        outDirName = args[2];

        // Die Dateinamen der Probleme einlesen.
        problemFiles = (new File(inDirName)).listFiles();
        if (problemFiles == null) {
            System.err.println("Verzeichnis kann nicht gelesen werden.");
            System.exit(-1);
        }

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

        // Dateien einlesen, Probleme konvertieren und speichern.
        for (int i = 0; i < problemFiles.length; i++) {
            try {
                stream = new FileInputStream(problemFiles[i]);
                problem = parser.doParse(stream);

                konvProblemFile = new File(outDirName,
                                           problemFiles[i].getName());
                fileWriter = new FileWriter(konvProblemFile);
                fileWriter.write(konvProblem(problem).toLibFormat());
                fileWriter.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(-1);

            } catch (ParseException e) {
                System.err.println("Fehler beim Parsen eines Problems.");
                e.printStackTrace();
                System.exit(-1);

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}

