/*
 * Dateiname      : ResultStatisticCreation.java
 * Letzte �nderung: 261. August 2006
 * Autoren        : Dietmar Lippold, Michael Wohlfart
 * Copyright (C)  : Institut f�r Intelligente Systeme Universit�t Stuttgart,
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


package mengenUeberdeckung.tests.bib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.util.Arrays;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import mengenUeberdeckung.tests.bib.parser.IParser;
import mengenUeberdeckung.tests.bib.parser.OrParser;
import mengenUeberdeckung.tests.bib.parser.ParseException;
import mengenUeberdeckung.tests.bib.parser.RailParser;
import mengenUeberdeckung.tests.bib.parser.TripleParser;
import mengenUeberdeckung.tests.bib.parser.XuBenchParser;
import mengenUeberdeckung.tests.bib.parser.SimpleParser;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;

/**
 * Diese Klasse ist ein Batch-Programm zur Erzeugung einer Statistik eines
 * Verfahrens zur Mengen�berdeckung zu eine Menge von Problemdateien. Der
 * Name des Verzeichnisses, in dem die Problemdateien liegen, wird zusammen
 * mit dem Typ der Dateien auf der Komandozeile �bergeben.
 *
 * @author  Dietmar Lippold, Michael Wohlfart
 */
public class ResultStatisticCreation {

    /**
     * Der Text, der als �berschrift in der Tabelle vor dem Namen des
     * Verzeichnisses mit den Testdateien ausgegeben wird.
     */
    private static final String IN_HEADER = "Ergebnisse zu den Dateien aus ";

    /**
     * Der Text, der als �berschrift in der Tabelle vor dem Namen der
     * Ausgabedatei ausgegeben wird.
     */
    private static final String OUT_HEADER = "Name der Ausgabedatei: ";

    /**
     * Die Endung, die an den Dateinamen angeh�ngt wird, wenn er noch keine
     * Endung hat.
     */
    private static final String FILE_SUFFIX = ".sxc";

    /**
     * Parst die die �bergebenen Probleme, berechnet die L�sungen und
     * ermittelt dabei die Laufzeiten.
     *
     * @param problems      Ein Array mit den Problem-Dateien.
     * @param parser        Der zum Einlesen zu verwendende Parser.
     * @param scpAlgorithm  Der Algorithmus, der zur Erzeugung der L�sungen
     *                      verwendet werden soll.
     *
     * @return  Eine Liste von Elementen vom Typ <CODE>ResultSet3</CODE>.
     *
     * @throws ParseException  Eine der Dateien konnte vom �bergebenen Parser
     *                         nicht  verarbeitet werden.
     */
    private static List calculatedResults(File[] problems, IParser parser,
                                          UeberdeckungsOptimierung scpAlgorithm)
        throws ParseException {

        ArrayList results = new ArrayList();

        for (int i = 0; i < problems.length; i++) {
            try {
                FileInputStream stream;
                stream = new FileInputStream(problems[i]);
                ItmFamilie problem = parser.doParse(stream);

                long startTime = (new Date()).getTime();
                ItmFamilie solution = scpAlgorithm.ueberdeckung(problem);
                long runtime = (new Date()).getTime() - startTime;

                results.add(new ResultSet3(problems[i].getName(),
                                           solution.kostenFamilie(), runtime));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    /**
     * Gibt die �bergebene Ergebnisse einzeln und insgesamt auf der
     * Standard-Ausgabe aus.
     *
     * @param results  Eine Liste mit den Ergebnissen zu den einzelnen
     *                 Problemen.
     */
    private static void printResults(List results) {
        ResultSet3 result;
        Iterator   resultIter;
        float      totalCosts;
        long       totalRuntime;

        totalCosts = 0;
        totalRuntime = 0;
        resultIter = results.iterator();
        while (resultIter.hasNext()) {
            result = (ResultSet3) resultIter.next();
            System.out.print("Problem: " + result.getProblemName());
            System.out.print("; Kosten = " + result.getSolutionCost());
            System.out.println(", Zeit = " + result.getRuntime() + " ms");

            totalCosts += result.getSolutionCost();
            totalRuntime += result.getRuntime();
        }

        System.out.println();
        System.out.println("Gesamtkosten = " + totalCosts);
        System.out.println("Gesamtzeit   = " + totalRuntime / 1000.0 + " s");
    }

    /**
     * Schreibt das �bergebene workbook in eine Datei.
     *
     * @param workbook  Das in die Datei zu schreibende workbook.
     * @param filename  Name der Datei, in die das workbook geschrieben werden
     *                  soll.
     *
     * @throws IOException  Es ist ein Fehler beim Schreiben der Datei
     *                      aufgetreten.
     */
    private static void writeWorkbook(HSSFWorkbook workbook, String filename)
        throws IOException {

        FileOutputStream outputStream;
        File file;

        file = new File(filename);
        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();

        System.out.println("Datei gespeichert unter: " + filename);
    }

    /**
     * Erzeugt eine Excel-Datei mit den Ergebnissen zu den L�sungen einiger
     * Problemdateien.<P>
     *
     * Als Typangabe der Problemdateien sind folgende Angaben zul�ssig:
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
     * @param dir           Der Namen des Verzeichnisses, in dem die
     *                      Problemdateien enthalten sind.
     * @param typ           Der Typ der Problemdateien.
     * @param filename      Der Name der Ausgabedatei oder "stdout".
     * @param scpAlgorithm  Der Algorithmus, der zur Erzeugung der L�sungen
     *                      verwendet werden soll.
     *
     * @throws IllegalArgumentException  Einer der Komandozeilenparameter war
     *                                   nicht korrekt.
     * @throws ParseException            Eine der Dateien hatte nicht das
     *                                   angegebene Format.
     * @throws IOException               Es ist ein Fehler beim Schreiben der
     *                                   Datei aufgetreten.
     */
    public static void createStatistic(String dir, String typ, String filename,
                                       UeberdeckungsOptimierung scpAlgorithm)
        throws IllegalArgumentException, ParseException, IOException {

        File[]  problems;
        List    results;
        IParser parser;
        String  shortFilename, fullFilename;

        // Die Dateinamen der zu l�senden Probleme einlesen.
        problems = (new File(dir)).listFiles();
        if (problems == null) {
            throw new IllegalArgumentException("Verzeichnis kann nicht"
                                               + " gelesen werden");
        }

        // Die Problemdateien nach ihren Namen sortieren.
        Arrays.sort(problems);

        // Den zu verwendenden Parser erzeugen.
        if (typ.equals("or-bib")) {
            parser = new OrParser();
        } else if (typ.equals("rail-bib")) {
            parser = new RailParser();
        } else if (typ.equals("triple")) {
            parser = new TripleParser();
        } else if (typ.equals("xu-bench")) {
            parser = new XuBenchParser();
        } else if (typ.equals("setOfSets")) {
            parser = new SimpleParser();
        } else {
            throw new IllegalArgumentException("Keine zul�ssige Typangabe f�r"
                                               + " die Problemdateien");
        }

        // Alle Probleme parsen, l�sen und die Messergebnisse merken.
        results = calculatedResults(problems, parser, scpAlgorithm);

        if (filename.equalsIgnoreCase("stdout")) {

            // Die Ergebnisse auf der Standard-Ausgabe ausgeben.
            printResults(results);

        } else {

            // Den Namen der Datei ohne Verzeichnisangabe ermitteln.
            int pos = filename.lastIndexOf(File.separatorChar);
            if (pos == -1) {
                // Der Dateiname enth�lt keine Verzeichnisangabe.
                shortFilename = filename;
            } else {
                // Der Dateiname beginnt nach dem Verzeichnis-Trenner.
                shortFilename = filename.substring(pos + 1);
            }

            // Den tats�chlichen Dateinamen ermitteln. Wenn der �bergebene
            // Dateiname kein Endung hat, die Standard-Endung diesen anh�ngen.
            fullFilename = filename;
            if (shortFilename.indexOf('.') == -1) {
                fullFilename += FILE_SUFFIX;
            }

            // Das Excel-Sheet anlegen und ausgeben.
            ExcelSheet3 sheet = new ExcelSheet3(shortFilename, IN_HEADER + dir,
                                                OUT_HEADER + shortFilename);
            writeWorkbook(sheet.generatedSheet(results), fullFilename);
        }
    }
}

