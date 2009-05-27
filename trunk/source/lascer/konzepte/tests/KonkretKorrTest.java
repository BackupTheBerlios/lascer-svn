/*
 * Dateiname      : KonkretKorrTest.java
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
import lascer.konzepte.KorrKonzeptVerwaltung;

/**
 * Stellt einen Test mit konkreten korrekten Teilmengen zur Erzeugung eines
 * korrekten und vollständigen Konzepts dar.
 *
 * @author  Dietmar Lippold
 */
public class KonkretKorrTest {

    /**
     * Die Anzahl der positiven Beispiele.
     */
    private static final int POS_BSP_ANZ = 4;

    /**
     * Die Anzahl der negativen Beispiele.
     */
    private static final int NEG_BSP_ANZ = 3;

    /**
     * Führt die Tests aus und gibt das beste Konzept (Formel) und dessen
     * Statistik aus.
     *
     * @param args  Ein Array mit den Argumente der Komandozeile. Dies sollte
     *              leer sein.
     */
    public static void main(String[] args) {
        KorrKonzeptVerwaltung konzeptVerwaltung;
        boolean[] posBspWerte1, posBspWerte2, posBspWerte3, negBspWerte;

        TestBeispieldaten beispieldaten = new TestBeispieldaten(POS_BSP_ANZ,
                                                                NEG_BSP_ANZ);

        posBspWerte1 = new boolean[] {true, true, false, false};
        posBspWerte2 = new boolean[] {false, true, true, false};
        posBspWerte3 = new boolean[] {false, false, true, true};
        negBspWerte = new boolean[] {false, false, false};

        TestPraedikat praedikat1 = new TestPraedikat(posBspWerte1, negBspWerte,
                                                     "praedikat1");
        TestPraedikat praedikat2 = new TestPraedikat(posBspWerte2, negBspWerte,
                                                     "praedikat2");
        TestPraedikat praedikat3 = new TestPraedikat(posBspWerte3, negBspWerte,
                                                     "praedikat3");

        konzeptVerwaltung = new KorrKonzeptVerwaltung(beispieldaten, -1, -1);
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

