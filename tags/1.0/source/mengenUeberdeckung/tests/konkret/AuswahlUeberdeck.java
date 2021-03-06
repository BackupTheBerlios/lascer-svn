/*
 * Dateiname      : AuswahlUeberdeck.java
 * Letzte �nderung: 26. Juli 2006
 * Autoren        : Dietmar Lippold
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


package mengenUeberdeckung.tests.konkret;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.heuristiken.utility.FrequencyUtility;
import mengenUeberdeckung.reduktion.UeberdeckDurchSofortAuswahl;

/**
 * Implementiert einen Test mit dem Auswahl-�berdeckungs-Verfahren zur
 * �berdeckung einer Menge, wobei die Teilmengen konkret vorgegeben sind.
 *
 * @author  Dietmar Lippold
 */
public class AuswahlUeberdeck {

    /**
     * F�hrt das Testprogramm aus.
     *
     * @param args  Es mu� genau eine ganze Zahl auf der Befehlszeile
     *              �bergeben werden. Das ist die Nummer des zu behandelnden
     *              Problems.
     */
    public static void main(String[] args) {
        UeberdeckungsOptimierung  ueberdeckVerfahren;
        FrequencyUtility          freqUtil;
        ItmFamilie                problem, ueberdeckung;

        if (args.length != 1) {
            System.err.println("Fehler: Es wurde keine Problemnummer auf"
                               + " der Komandozeile angegeben");
            System.exit(-1);
        }

        int problemNr = Integer.parseInt(args[0]);
        problem = UeberdeckProbleme.gibProblem(problemNr);

        KonkretesProblemAusgabe.ausgabeProblem(problem);

        freqUtil = new FrequencyUtility(false);
        ueberdeckVerfahren = new UeberdeckDurchSofortAuswahl(freqUtil);
        ueberdeckung = ueberdeckVerfahren.ueberdeckung(problem);

        KonkretesProblemAusgabe.ausgabeUeberdeckung(ueberdeckung);
    }
}

