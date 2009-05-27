/*
 * Dateiname      : BibDateiErzeugung.java
 * Letzte Änderung: 05. Juli 2006
 * Autoren        : Dietmar Lippold
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


package mengenUeberdeckung.tests.zufall;

import java.util.Random;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Klasse erzeugt ein oder mehrere Überdeckungsprobleme und schreibt diese in
 * einem Bibliotheks-Format in Dateien.
 *
 * @author  Dietmar Lippold
 */
public class BibDateiErzeugung {

    /**
     * Liefert einen Dateinamen, der aus dem Problemnamen als Verzeichnis und
     * als Anfang des relativen Dateinamens sowie einer Nummer als Ende des
     * relativen Dateinamens besteht.
     *
     * @param problemname  Der Name des Problems, der als Name des
     *                     Verzeichnisses und als Anfang des relativen
     *                     Dateinamens benutzt wird.
     * @param gesAnz       Die Gesamtanzahl der Dateien.
     * @param nummer       Die Nummer der aktuellen Datei.
     *
     * @return  Den erzeugten Dateinamen.
     */
    private static String dateiname(String problemname, int gesAnz, int nummer) {
        String gesAnzText;
        String nummerText;
        String erweitertText;
        int    nullAnz;

        gesAnzText = String.valueOf(gesAnz);
        nummerText = String.valueOf(nummer);

        nullAnz = gesAnzText.length() - nummerText.length();
        erweitertText = nummerText;
        for (int i = 0; i < nullAnz; i++) {
            erweitertText = "0" + erweitertText;
        }

        return (problemname + "/" + problemname + "-" + erweitertText);
    }

    /**
     * Schreibt den übergebenen Text in eine Datei mit dem angegebenen Namen.
     *
     * @param daten      Der zu speichernde Text.
     * @param dateiname  Name der Datei, in die der Text geschrieben werden
     *                   soll.
     *
     * @throws IOException  Es ist ein Fehler beim Schreiben der Datei
     *                      aufgetreten.
     */
    private static void writeToFile(String daten, String dateiname)
        throws IOException {

        OutputStreamWriter outputStream;
        PrintWriter printWriter;
        File file;

        file = new File(dateiname);
        outputStream = new OutputStreamWriter(new FileOutputStream(file));
        printWriter = new PrintWriter(outputStream);
        printWriter.print(daten);
        if (printWriter.checkError()) {
            throw new IOException("Fehler beim Schreiben der Datei.");
        }
        printWriter.close();
    }

    /**
     * Ruft die einzelnen Methoden der Klasse <code>TestRahmen</code> und
     * anschließend die Methode zum Speichern der Datei auf.
     *
     * @param args  Befehlszeilenparameter (keiner).
     */
    public static void main(String[] args) {
        BufferedReader eingabe;
        ItmFamilie     problemFamilie;
        Random  rand;
        String  problemname = "";
        String  dateiname = "";
        int     anzahlElemente = 0;
        int     anzahlTeilmengen = 0;
        float   minAbdeckung = 0;
        float   maxAbdeckung = 0;
        float   minKosten = 0;
        float   maxKosten = 0;
        long    initRandom = 0;
        long    zufallszahl = 0;
        int     problemAnzahl = 0;

        System.out.println();
        System.out.println("Programm zur Erzeugung und Speicherung von"
                           + " SCP-Problemen");
        System.out.println();
        System.out.println();

        // Einlesen der Parameter.
        try {
            anzahlElemente = TestRahmen.getAnzahlElemente(args);
            anzahlTeilmengen = TestRahmen.getAnzahlTeilmengen(args);
            minAbdeckung = TestRahmen.getMinAbdeckung(args);
            maxAbdeckung = TestRahmen.getMaxAbdeckung(args);
            minKosten = TestRahmen.getMinKosten(args);
            maxKosten = TestRahmen.getMaxKosten(args);
            initRandom = TestRahmen.getInitRandom(args);
        } catch (IOException e) {
            System.err.println("Es ist ein Fehler beim Einlesen der Daten"
                               + " aufgetreten.");
            e.printStackTrace();
            System.exit(-1);
        }

        // Anzahl der zu erzeugenden Probleme abfragen.
        System.out.println("Anzahl der zu erzeugenden Probleme: ");
        try {
            problemAnzahl = TestRahmen.eingabeInt();
        } catch (IOException e) {
            System.err.println("Es ist ein Fehler beim Einlesen der Anzahl"
                               + " der Probleme aufgetreten.");
            e.printStackTrace();
            System.exit(-1);
        }

        // Dateiname abfragen.
        if (problemAnzahl == 1) {
            System.out.println("Name der Ausgabedatei: ");
        } else {
            System.out.println("Name vom Ausgabeverzeichnis: ");
        }
        try {
            eingabe = new BufferedReader(new InputStreamReader(System.in));
            problemname = eingabe.readLine();
        } catch (IOException e) {
            System.err.println("Es ist ein Fehler beim Einlesen des Namens"
                               + " aufgetreten.");
            e.printStackTrace();
            System.exit(-1);
        }

        rand = new Random(initRandom);
        for (int problemNr = 1; problemNr <= problemAnzahl; problemNr++) {

            // Erzeugung der Teilmengen-Familie.
            zufallszahl = rand.nextLong();
            problemFamilie = TestRahmen.aufbau(anzahlElemente, anzahlTeilmengen,
                                               minAbdeckung, maxAbdeckung,
                                               minKosten, maxKosten, zufallszahl);

            if (problemAnzahl == 1) {
                dateiname = problemname;
            } else {
                dateiname = dateiname(problemname, problemAnzahl, problemNr);
            }

            try {
                writeToFile(problemFamilie.toLibFormat(), dateiname);
            } catch (IOException e) {
                System.err.println("Es ist ein Fehler beim Schreiben der Datei"
                                   + " aufgetreten.");
                e.printStackTrace();
                System.exit(-1);
            }

            System.out.println();
            System.out.println("Datei " + dateiname + " erfolgreich geschrieben");
        }
    }
}

