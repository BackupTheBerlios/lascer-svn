/*
 * Dateiname      : IteratedGreedyBeschUeberdeck.java
 * Letzte Änderung: 23. Juli 2006
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


package mengenUeberdeckung.tests.konkret;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.iteratedGreedyCovering.IterEnhancedGreedyHeuristic;

/**
 * Implementiert einen Test für den SCP-Algorithums, bei dem die maximale
 * Anzahl der Teilmengen in der Lösung und die konkreten Teilmengen vorgegeben
 * sind.
 *
 * @author  Dietmar Lippold
 */
public class IteratedGreedyBeschUeberdeck {

    /**
     * Führt das Testprogramm aus.
     *
     * @param args  Befehlszeilenparameter.
     */
    public static void main(String[] args) {
        UeberdeckungsOptimierung  iterGreedy;
        ItmFamilie                itmFamilie, ueberdeckung;
        IndexTeilmenge[]          teilmengen;
        int                       groesseGesamtmenge = 12;
        int                       itmAnz = 27;

        System.out.println("Vorhandene Teilmengen");

        teilmengen = new IndexTeilmenge[itmAnz];
        itmFamilie = new ItmFamilie(groesseGesamtmenge);
        itmFamilie = itmFamilie.clone(6, 12);

        // Erzeugung der Teilmengen mit einem Element und Kosten 1
        for (int itmNr = 0; itmNr < groesseGesamtmenge; itmNr++) {
            teilmengen[itmNr] = new IndexTeilmenge(groesseGesamtmenge, 1);
            teilmengen[itmNr].indexAufnehmen(itmNr);
            System.out.println(teilmengen[itmNr].toString());
            itmFamilie.teilmengeHinzufuegen(teilmengen[itmNr]);
        }

        // Erzeugung der Teilmengen mit zwei Elementen und Kosten 3
        for (int itmNr = 0; itmNr * 2 < groesseGesamtmenge; itmNr++) {
            teilmengen[12 + itmNr] = new IndexTeilmenge(groesseGesamtmenge, 3);
            teilmengen[12 + itmNr].indexAufnehmen(itmNr * 2);
            teilmengen[12 + itmNr].indexAufnehmen(itmNr * 2 + 1);
            System.out.println(teilmengen[12 + itmNr].toString());
            itmFamilie.teilmengeHinzufuegen(teilmengen[12 + itmNr]);
        }

        // Erzeugung der Teilmengen mit drei Elementen und Kosten 5
        for (int itmNr = 0; itmNr * 3 < groesseGesamtmenge; itmNr++) {
            teilmengen[18 + itmNr] = new IndexTeilmenge(groesseGesamtmenge, 5);
            teilmengen[18 + itmNr].indexAufnehmen(itmNr * 3);
            teilmengen[18 + itmNr].indexAufnehmen(itmNr * 3 + 1);
            teilmengen[18 + itmNr].indexAufnehmen(itmNr * 3 + 2);
            System.out.println(teilmengen[18 + itmNr].toString());
            itmFamilie.teilmengeHinzufuegen(teilmengen[18 + itmNr]);
        }

        // Erzeugung der Teilmengen mit vier Elementen und Kosten 7
        for (int itmNr = 0; itmNr * 4 < groesseGesamtmenge; itmNr++) {
            teilmengen[22 + itmNr] = new IndexTeilmenge(groesseGesamtmenge, 7);
            teilmengen[22 + itmNr].indexAufnehmen(itmNr * 4);
            teilmengen[22 + itmNr].indexAufnehmen(itmNr * 4 + 1);
            teilmengen[22 + itmNr].indexAufnehmen(itmNr * 4 + 2);
            teilmengen[22 + itmNr].indexAufnehmen(itmNr * 4 + 3);
            System.out.println(teilmengen[22 + itmNr].toString());
            itmFamilie.teilmengeHinzufuegen(teilmengen[22 + itmNr]);
        }

        // Erzeugung der Teilmengen mit sechs Elementen und Kosten 12
        for (int itmNr = 0; itmNr * 6 < groesseGesamtmenge; itmNr++) {
            teilmengen[25 + itmNr] = new IndexTeilmenge(groesseGesamtmenge, 12);
            teilmengen[25 + itmNr].indexAufnehmen(itmNr * 6);
            teilmengen[25 + itmNr].indexAufnehmen(itmNr * 6 + 1);
            teilmengen[25 + itmNr].indexAufnehmen(itmNr * 6 + 2);
            teilmengen[25 + itmNr].indexAufnehmen(itmNr * 6 + 3);
            teilmengen[25 + itmNr].indexAufnehmen(itmNr * 6 + 4);
            teilmengen[25 + itmNr].indexAufnehmen(itmNr * 6 + 5);
            System.out.println(teilmengen[25 + itmNr].toString());
            itmFamilie.teilmengeHinzufuegen(teilmengen[25 + itmNr]);
        }

        System.out.println();
        System.out.println("Statistik der resultierenden ItmFamilie:");
        System.out.print(itmFamilie.statistik(1));

        iterGreedy = new IterEnhancedGreedyHeuristic();
        ueberdeckung = iterGreedy.ueberdeckung(itmFamilie);

        System.out.println();
        System.out.println();
        System.out.println("Die erzeugte resultierenden Überdeckung:");
        System.out.println(ueberdeckung.toString());

        System.out.println();
        System.out.println();
        System.out.println("Statistik der erzeugten resultierenden Überdeckung:");
        System.out.print(ueberdeckung.statistik(1));
        System.out.println();
    }
}

