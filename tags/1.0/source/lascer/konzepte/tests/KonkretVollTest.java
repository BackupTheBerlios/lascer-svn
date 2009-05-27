/*
 * Dateiname      : KonkretVollTest.java
 * Letzte Änderung: 13. November 2005
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2005
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


package lascer.konzepte.tests;

import lascer.konzepte.Konzept;
import lascer.konzepte.VollKonzeptVerwaltung;

/**
 * Stellt einen Test mit konkreten vollständigen Teilmengen zur Erzeugung
 * eines vollständigen und korrekten Konzepts dar.
 *
 * @author  Dietmar Lippold
 */
public class KonkretVollTest {

    /**
     * Die Anzahl der positiven Beispiele.
     */
    private static final int POS_BSP_ANZ = 3;

    /**
     * Die Anzahl der negativen Beispiele.
     */
    private static final int NEG_BSP_ANZ = 4;

    /**
     * Führt die Tests aus und gibt das beste Konzept (Formel) und dessen
     * Statistik aus.
     *
     * @param args  Ein Array mit den Argumente der Komandozeile. Dies sollte
     *              leer sein.
     */
    public static void main(String[] args) {
        VollKonzeptVerwaltung konzeptVerwaltung;
        boolean[] posBspWerte, negBspWerte1, negBspWerte2, negBspWerte3;

        TestBeispieldaten beispieldaten = new TestBeispieldaten(POS_BSP_ANZ,
                                                                NEG_BSP_ANZ);

        posBspWerte = new boolean[] {true, true, true};
        negBspWerte1 = new boolean[] {true, true, false, false};
        negBspWerte2 = new boolean[] {false, true, true, false};
        negBspWerte3 = new boolean[] {false, false, true, true};

        TestPraedikat praedikat1 = new TestPraedikat(posBspWerte, negBspWerte1,
                                                     "praedikat1");
        TestPraedikat praedikat2 = new TestPraedikat(posBspWerte, negBspWerte2,
                                                     "praedikat2");
        TestPraedikat praedikat3 = new TestPraedikat(posBspWerte, negBspWerte3,
                                                     "praedikat3");

        konzeptVerwaltung = new VollKonzeptVerwaltung(beispieldaten, -1, -1);
        konzeptVerwaltung.praedikatAufnehmen(praedikat1, false);
        konzeptVerwaltung.praedikatAufnehmen(praedikat2, false);
        konzeptVerwaltung.praedikatAufnehmen(praedikat3, false);

        int finFormScpIterAnz = lascer.Konstanten.FIN_FORM_SCP_ITER_ANZ;
        Konzept ergebnis = konzeptVerwaltung.besteFormel(finFormScpIterAnz);
        System.out.println("Ergebnis : " + ergebnis.toString());
        System.out.println("Statistik:");
        System.out.println(ergebnis.statistik());
    }
}

