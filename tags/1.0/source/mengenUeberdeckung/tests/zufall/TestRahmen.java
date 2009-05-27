/*
 * Dateiname      : TestRahmen.java
 * Letzte �nderung: 23. Juli 2006
 * Autoren        : Natalia Sevcenko, Rene Berleong, Wolfgang Tischer, Dietmar Lippold
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


package mengenUeberdeckung.tests.zufall;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Date;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;

/**
 * Implementiert einen Test f�r den SCP-Algorithums.
 *
 * @author  Natalia Sevcenko, Rene Berleong, Wolfgang Tischer, Dietmar Lippold
 */
public class TestRahmen {

    /**
     * Gibt eine Eingabe als Character-Zeichen zur�ck.
     *
     * @return  Eingegebenes Zeichen.
     *
     * @exception  IOException  Exception bei Ein-/Ausgabefehler.
     */
    public static char eingabeChar() throws IOException {
        BufferedReader din;

        din = new BufferedReader(new InputStreamReader(System.in));
        return din.readLine().charAt(0);
    }

    /**
     * Gibt eine Eingabe als Integer-Wert zur�ck.
     *
     * @return  Eingegebene Integer-Zahl.
     *
     * @exception  IOException  Exception bei Ein-/Ausgabefehler.
     */
    public static int eingabeInt() throws IOException {
        BufferedReader din;

        din = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(din.readLine());
    }

    /**
     * Gibt eine Eingabe als Float-Wert zur�ck.
     *
     * @return  Eingegebene Float-Zahl.
     *
     * @exception  IOException  Exception bei Ein-/Ausgabefehler.
     */
    public static float eingabeFloat() throws IOException {
        BufferedReader din;

        din = new BufferedReader(new InputStreamReader(System.in));
        return Float.parseFloat(din.readLine());
    }

    /**
     * Gibt die Anzahl der Elemente zur�ck.
     *
     * @param args  Befehlszeilenparameter.
     *
     * @return  Anzahl der Elemente.
     *
     * @exception IOException  Exception bei Ein-/Ausgabefehler.
     */
    public static int getAnzahlElemente(String[] args) throws IOException {
        if (args.length > 0) {
            return Integer.parseInt(args[0]);
        } else {
            System.out.println("Bitte geben Sie die Anzahl der Elemente ein");
            return eingabeInt();
        }
    }

    /**
     * Gibt die Anzahl der Teilmengen zur�ck.
     *
     * @param args  Befehlszeilenparameter.
     *
     * @return  Anzahl der Teilmengen.
     *
     * @exception IOException  Exception bei Ein-/Ausgabefehler.
     */
    public static int getAnzahlTeilmengen(String[] args) throws IOException {
        if (args.length > 1) {
            return Integer.parseInt(args[1]);
        } else {
            System.out.println("Bitte geben Sie die Anzahl der Teilmengen ein");
            return eingabeInt();
        }
    }

    /**
     * Gibt die minimale Abdeckung in Prozent zur�ck.
     *
     * @param args  Befehlszeilenparameter.
     *
     * @return  Minimale Abdeckung in Prozent.
     *
     * @exception IOException  Exception bei Ein-/Ausgabefehler.
     */
    public static float getMinAbdeckung(String[] args) throws IOException {
        if (args.length > 2) {
            return Float.parseFloat(args[2]);
        } else {
            System.out.println("Bitte geben Sie die min. Abdeckung in % ein");
            return eingabeFloat();
        }
    }

    /**
     * Gibt die minimale Abdeckung in Prozent zur�ck.
     *
     * @param args  Befehlszeilenparameter.
     *
     * @return  Maximale Abdeckung in Prozent.
     *
     * @exception IOException  Exception bei Ein-/Ausgabefehler.
     */
    public static float getMaxAbdeckung(String[] args) throws IOException {
        if (args.length > 3) {
            return Float.parseFloat(args[3]);
        } else {
            System.out.println("Bitte geben Sie die max. Abdeckung in % ein");
            return eingabeFloat();
        }
    }

    /**
     * Gibt die minimalen Kosten zur�ck.
     *
     * @param args  Befehlszeilenparameter.
     *
     * @return  Minimale Kosten.
     *
     * @exception IOException  Exception bei Ein-/Ausgabefehler.
     */
    public static float getMinKosten(String[] args) throws IOException {
        if (args.length > 4) {
            return Float.parseFloat(args[4]);
        } else {
            System.out.println("Bitte geben Sie die minimalen Kosten"
                               + " einer Teilmenge ein!");
            return eingabeFloat();
        }
    }

    /**
     * Gibt die maximalen Kosten zur�ck.
     *
     * @param args  Befehlszeilenparameter.
     *
     * @return  Maximale Kosten.
     *
     * @exception IOException  Exception bei Ein-/Ausgabefehler.
     */
    public static float getMaxKosten(String[] args) throws IOException {
        if (args.length > 5) {
            return Float.parseFloat(args[5]);
        } else {
            System.out.println("Bitte geben Sie die maximalen Kosten"
                               + " einer Teilmenge ein!");
            return eingabeFloat();
        }
    }

    /**
     * Gibt den Initialisierungswert f�r den Zufallsgenerator zur�ck.
     *
     * @param args  Befehlszeilenparameter.
     *
     * @return  Initialisierungswert f�r den Zufallsgenerator.
     *
     * @exception IOException  Exception bei Ein-/Ausgabefehler.
     */
    public static int getInitRandom(String[] args) throws IOException {
        if (args.length > 6) {
            return Integer.parseInt(args[6]);
        } else {
            System.out.println("Bitte geben Sie die Initialsierung"
                               + " des Zufallsgenerators an!");
            return eingabeInt();
        }
    }

    /**
     * Gibt zur�ck, ob die erzeugten Teilmengen ausgegeben werden sollen.
     *
     * @param args  Befehlszeilenparameter.
     *
     * @return  Ausgabe der erzeugten Mengen (j/n).
     *
     * @exception IOException  Exception bei Ein-/Ausgabefehler.
     */
    public static boolean getAusgabeMengenYN(String[] args) throws IOException {
        char ausgabeMengenYN;
        if (args.length > 7) {
            ausgabeMengenYN = args[7].charAt(0);
        } else {
            System.out.println("W�nschen Sie eine Ausgabe der"
                               + " erzeugten Mengen? (j/n)");
            ausgabeMengenYN = eingabeChar();
        }

        if (ausgabeMengenYN == 'j') {
            return true;
        } else if (ausgabeMengenYN == 'n') {
            return false;
        } else {
            throw new IOException("nur j/n als Argument f�r die Ausgabe erlaubt");
        }
    }

    /**
     * Gibt zur�ck, ob die L�sung ausgegeben werden soll.
     *
     * @param args  Befehlszeilenparameter.
     *
     * @return  Ausgabe der L�sung (j/n).
     *
     * @exception IOException  Exception bei Ein-/Ausgabefehler
     */
    public static boolean getAusgabeLoesungYN(String[] args) throws IOException {
        char ausgabeLoesungYN;
        if (args.length > 8) {
            ausgabeLoesungYN = args[8].charAt(0);
        } else {
            System.out.println("W�nschen Sie eine Ausgabe der L�sung? (j/n)");
            ausgabeLoesungYN = eingabeChar();
        }

        if (ausgabeLoesungYN == 'j') {
            return true;
        } else if (ausgabeLoesungYN == 'n') {
            return false;
        } else {
            throw new IOException("nur j/n als Argument f�r die Ausgabe erlaubt");
        }
    }

    /**
     * Baut eine Matrix mit zuf�lligen Werten auf.
     *
     * @param zeilenAnzahl  Anzahl der Elemente.
     * @param spaltenZahl   Anzahl der Teilmengen.
     * @param minAbdeckung  Minimale Abdeckung der Matrix mit Einsen.
     * @param maxAbdeckung  Maximale Abdeckung der Matrix mit Einsen.
     * @param minKosten     Minimale Kosten einer Teilmenge.
     * @param maxKosten     Maximale Kosten einer Teilmenge.
     * @param initRandom    Initialisierungszahl f�r Zufallsgenerator.
     *
     * @return  Aufgebaute Matrix.
     */
    public static ItmFamilie aufbau(int zeilenAnzahl, int spaltenZahl,
                                    float minAbdeckung, float maxAbdeckung,
                                    float minKosten, float maxKosten,
                                    long initRandom) {
        ItmFamilie itmFamilie    = new ItmFamilie(zeilenAnzahl);
        Random     zahlGenerator = new Random();
        int sollAufnahme;    // Anzahl Elemente die aufzunehmen sind
        int istAufnahme;     // Anzahl aufgenommener Elemente einer Teilmengen
        int randomElement;   // zuf�llig ermitteltes Element

        // Initialisiation der Zufallszahlen
        if (initRandom == 0) {
            zahlGenerator.setSeed((new Date()).getTime());
        } else {
            zahlGenerator.setSeed(initRandom);
        }

        // Matrix ausf�llen
        while (itmFamilie.groesseFamilie() < spaltenZahl) {
            float kosten = minKosten + (zahlGenerator.nextFloat()
                                        * (maxKosten - minKosten));
            IndexTeilmenge teilmenge = new IndexTeilmenge(zeilenAnzahl, kosten);

            // Berechnung sollAufnahme
            int minAnz = (int)Math.round(minAbdeckung * zeilenAnzahl / 100);
            int maxAnz = (int)Math.round(maxAbdeckung * zeilenAnzahl / 100);
            sollAufnahme = zahlGenerator.nextInt(maxAnz - minAnz + 1) + minAnz;
            // Abfangen eines eigentlich nie auftretenden
            // aber unerlaubten Ausnahmefalls
            if (sollAufnahme == 0) {
                sollAufnahme = 1;
            }

            istAufnahme = 0;
            while (istAufnahme < sollAufnahme) {
                randomElement = zahlGenerator.nextInt(zeilenAnzahl);
                if (!teilmenge.indexIstEnthalten(randomElement)) {
                    teilmenge.indexAufnehmen(randomElement);
                    istAufnahme = istAufnahme + 1;
                }
            }
            itmFamilie.teilmengeHinzufuegen(teilmenge);
        }
        return itmFamilie;
    }

    /**
     * Erzeugt eine Matrix und wendet den SCP Algorithmus darauf an.
     *
     * @param anzahlElemente     Anzahl der Elemente.
     * @param anzahlTeilmengen   Anzahl der Teilmengen.
     * @param minAbdeckung       Minimale Abdeckung der Matrix mit Einsen.
     * @param maxAbdeckung       Maximale Abdeckung der Matrix mit Einsen.
     * @param minKosten          Minimale Kosten einer Teilmenge.
     * @param maxKosten          Maximale Kosten einer Teilmenge.
     * @param initRandom         Initialisierungszahl f�r Zufallsgenerator.
     * @param ueberdeckErzeuger  Ein Verfahren, mit dem eine Ueberdeckung
     *                           erzeugt wird.
     *
     * @return  Erzeugte minimale �berdeckung.
     */
    public static ItmFamilie checkSCP(int anzahlElemente, int anzahlTeilmengen,
                                      float minAbdeckung, float maxAbdeckung,
                                      float minKosten, float maxKosten,
                                      long initRandom,
                                      UeberdeckungsOptimierung ueberdeckErzeuger) {

        ItmFamilie itmFamilie = aufbau(anzahlElemente, anzahlTeilmengen,
                                       minAbdeckung, maxAbdeckung, minKosten,
                                       maxKosten, initRandom);
        return ueberdeckErzeuger.ueberdeckung(itmFamilie);
    }

    /**
     * Gibt eine Familie von Teilmengen aus.
     *
     * @param itmFamilie  Auszugebende Familie von Teilmengen.
     */
    public static void teilmengenAusgabe(ItmFamilie itmFamilie) {
        Iterator itmIter = itmFamilie.iterator();
        int itmNr;

        System.out.println("Das sind die enthaltenen Teilmengen:");
        System.out.println();

        itmNr = 0;
        while (itmIter.hasNext()) {
            itmNr++;
            IndexTeilmenge teilmenge = (IndexTeilmenge)itmIter.next();
            System.out.print(itmNr + ". Teilmenge: ");
            System.out.println(teilmenge);
        }
    }
}

