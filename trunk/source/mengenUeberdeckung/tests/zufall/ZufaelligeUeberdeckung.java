/*
 * Dateiname      : ZufaelligeUeberdeckung.java
 * Letzte Änderung: 23. Juli 2006
 * Autoren        : Natalia Sevcenko, Rene Berleong, Wolfgang Tischer, Dietmar Lippold
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

import java.io.IOException;
import java.util.Date;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;

/**
 * Testet ein SCP-Verfahren mit einem zufällig erzeugten Problem.
 *
 * @author  Natalia Sevcenko, Rene Berleong, Wolfgang Tischer, Dietmar Lippold
 */
public class ZufaelligeUeberdeckung {

    /**
     * Liest Werte zur Erzeugung eines zufälligen Problems ein, erzeugt ein
     * Problem, berechnet eine Lösung und gibt Daten zum Prolem und zur Lösung
     * aus.
     *
     * @param parameter           Ein Array, das einige der einzulesenden
     *                            Parameter enthalten kann.
     * @param verfahrensName      Der Name des zu verwendenden Verfahrens.
     * @param ueberdeckErzeugung  Das Verfahren zur Erzeugung der Überdeckung.
     *
     * @exception IOException  Exception bei einem Ein-/Ausgabefehler.
     */
    public static void ausfuehrung(String[] parameter,
                                   String verfahrensName,
                                   UeberdeckungsOptimierung ueberdeckErzeugung)
        throws IOException {

        ItmFamilie problemFamilie, solutionFamilie;
        int        anzahlElemente, anzahlTeilmengen;
        float      minAbdeckung, maxAbdeckung, minKosten, maxKosten;
        long       initRandom, startZeit, endeZeit, rechenZeit;
        boolean    testfamilieAusgeben, loesungAusgeben;

        System.out.println();
        System.out.print("TESTPROGRAMM FÜR SCP-ALGORITHMUS");
        System.out.println(" " + verfahrensName);
        System.out.println();

        // Einlesen der Parameter.
        anzahlElemente = TestRahmen.getAnzahlElemente(parameter);
        anzahlTeilmengen = TestRahmen.getAnzahlTeilmengen(parameter);
        minAbdeckung = TestRahmen.getMinAbdeckung(parameter);
        maxAbdeckung = TestRahmen.getMaxAbdeckung(parameter);
        minKosten = TestRahmen.getMinKosten(parameter);
        maxKosten = TestRahmen.getMaxKosten(parameter);
        initRandom = TestRahmen.getInitRandom(parameter);
        testfamilieAusgeben = TestRahmen.getAusgabeMengenYN(parameter);
        loesungAusgeben = TestRahmen.getAusgabeLoesungYN(parameter);

        // Erzeugung der Test-Familie.
        problemFamilie = TestRahmen.aufbau(anzahlElemente, anzahlTeilmengen,
                                           minAbdeckung, maxAbdeckung,
                                           minKosten, maxKosten, initRandom);

        if (testfamilieAusgeben) {
            System.out.println();
            System.out.println();
            System.out.println("Statistik des gegebenen Problems:");
            System.out.println(problemFamilie.statistik(0));
            TestRahmen.teilmengenAusgabe(problemFamilie);
        }

        startZeit = (new Date()).getTime();
        solutionFamilie = ueberdeckErzeugung.ueberdeckung(problemFamilie);
        endeZeit = (new Date()).getTime();
        rechenZeit = (endeZeit - startZeit);

        if (solutionFamilie != null) {
            System.out.println();
            System.out.println();
            System.out.println("Statistik der Lösung: ");
            System.out.print(solutionFamilie.statistik(0));
        } else {
            System.out.println();
            System.out.println();
            System.out.println("Es wurde keine Lösung erzeugt.");
        }

        if (loesungAusgeben && (solutionFamilie != null)) {
            System.out.println();
            System.out.println();
            TestRahmen.teilmengenAusgabe(solutionFamilie);
        }

        System.out.println();
        System.out.println();
        System.out.println("Rechenzeit: "
                           + (rechenZeit / 1000.0) + " Sekunden");
        System.out.println();
    }
}

