/*
 * Dateiname      : KonkretAlgVollTest2.java
 * Letzte Änderung: 09. Februar 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2006
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

import java.util.HashSet;

import lascer.konzepte.Konzept;
import lascer.konzepte.VollKonzeptVerwaltung;

/**
 * Stellt einen Test mit konkreten allgemeinen und korrekten Teilmengen zur
 * Erzeugung eines korrekten und vollständigen Konzepts dar.
 *
 * @author  Dietmar Lippold
 */
public class KonkretAlgVollTest2 {

    /**
     * Die Anzahl der positiven Beispiele.
     */
    private static final int POS_BSP_ANZ = 4;

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
        HashSet   erzeugteKonzepte;
        boolean[] posBspWerte1, posBspWerte2, posBspWerte3, posBspWerte4;
        boolean[] negBspWerte1, negBspWerte2, negBspWerte3, negBspWerte4;
        int       erzSpezScpIterAnz;
        boolean   veraendert;

        TestBeispieldaten beispieldaten = new TestBeispieldaten(POS_BSP_ANZ,
                                                                NEG_BSP_ANZ);

        posBspWerte1 = new boolean[] {false, false, false, true};
        posBspWerte2 = new boolean[] {false, true, true, false};
        posBspWerte3 = new boolean[] {true, false, false, true};
        posBspWerte4 = new boolean[] {false, false, true, true};

        negBspWerte1 = new boolean[] {true, true, true, false};
        negBspWerte2 = new boolean[] {false, true, false, false};
        negBspWerte3 = new boolean[] {false, false, false, false};
        negBspWerte4 = new boolean[] {false, false, false, true};

        TestPraedikat praedikat1 = new TestPraedikat(posBspWerte1, negBspWerte1,
                                                     "praedikat1");
        TestPraedikat praedikat2 = new TestPraedikat(posBspWerte2, negBspWerte2,
                                                     "praedikat2");
        TestPraedikat praedikat3 = new TestPraedikat(posBspWerte3, negBspWerte3,
                                                     "praedikat3");
        TestPraedikat praedikat4 = new TestPraedikat(posBspWerte4, negBspWerte4,
                                                     "praedikat4");

        konzeptVerwaltung = new VollKonzeptVerwaltung(beispieldaten, -1, -1);
        konzeptVerwaltung.erzAlgKonzeptMenge(true);
        konzeptVerwaltung.praedikatAufnehmen(praedikat1, true);
        konzeptVerwaltung.praedikatAufnehmen(praedikat2, false);
        konzeptVerwaltung.praedikatAufnehmen(praedikat3, false);
        konzeptVerwaltung.praedikatAufnehmen(praedikat4, false);
        erzSpezScpIterAnz = lascer.Konstanten.ERZ_SPEZ_SCP_ITER_ANZ;
        do {
            erzeugteKonzepte = konzeptVerwaltung.erzeugteKonzepte(erzSpezScpIterAnz);
            veraendert = konzeptVerwaltung.konzepteAufnehmen(erzeugteKonzepte);
        } while (veraendert);

        int finFormScpIterAnz = lascer.Konstanten.FIN_FORM_SCP_ITER_ANZ;
        Konzept ergebnis = konzeptVerwaltung.besteFormel(finFormScpIterAnz);
        System.out.println("Ergebnis : " + ergebnis.toString());
        System.out.println("Statistik:");
        System.out.println(ergebnis.statistik());
    }
}

