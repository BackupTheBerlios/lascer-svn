/*
 * Dateiname      : IterGreedyNachAuswahlUeberdeck.java
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
import mengenUeberdeckung.reduktion.UeberdeckNachSofortAuswahl;
import mengenUeberdeckung.iteratedGreedyCovering.IterEnhancedGreedyHeuristic;

/**
 * Implementiert einen Test mit dem Iterated-Greedy-Verfahren nach der
 * Reduktion eines Problems zur �berdeckung einer Menge, wobei die Teilmengen
 * konkret vorgegeben sind.
 *
 * @author  Dietmar Lippold
 */
public class IterGreedyNachAuswahlUeberdeck {

    /**
     * F�hrt das Testprogramm aus.
     *
     * @param args  Es m�ssen genau drei Zahlen auf der Befehlszeile �bergeben
     *              werden. Die erste ist die Nummer des zu behandelnden
     *              Problems, die zweite ist die minimale H�ufigkeit, mit der
     *              jedes Element �berdeckt werden soll, die dritte ist der
     *              Faktor, um den die Anzahl der Teilmengen der sekund�ren
     *              Auswahl gr��er ist als die Anzahl der Teilmengen der
     *              prim�ren Auswahl. Wenn dieser Wert negativ ist, findet
     *              keine Auswahl statt.
     */
    public static void main(String[] args) {
        UeberdeckungsOptimierung  ueberdeckVerfahren;
        UeberdeckungsOptimierung  auswahlUeberdeckVerf;
        FrequencyUtility          freqUtil;
        ItmFamilie                problem, ueberdeckung;
        float                     faktorErgAnz;
        int                       minUeberdeckAnz;

        if (args.length != 3) {
            System.err.println("Fehler: Es wurde keine Problemnummer, keine"
                               + " minimale H�ufigkeit der �berdeckung oder"
                               + " kein Faktor auf der Komandozeile angegeben");
            System.exit(-1);
        }

        int problemNr = Integer.parseInt(args[0]);
        problem = UeberdeckProbleme.gibProblem(problemNr);
        minUeberdeckAnz = Integer.parseInt(args[1]);
        faktorErgAnz = Float.parseFloat(args[2]);

        KonkretesProblemAusgabe.ausgabeProblem(problem);

        ueberdeckVerfahren = new IterEnhancedGreedyHeuristic();
        freqUtil = new FrequencyUtility(false);
        if (faktorErgAnz < 0) {
            auswahlUeberdeckVerf = ueberdeckVerfahren;
        } else {
            auswahlUeberdeckVerf =
                new UeberdeckNachSofortAuswahl(0, minUeberdeckAnz, faktorErgAnz,
                                               freqUtil, ueberdeckVerfahren);
        }

        ueberdeckung = auswahlUeberdeckVerf.ueberdeckung(problem);

        KonkretesProblemAusgabe.ausgabeUeberdeckung(ueberdeckung);
    }
}

