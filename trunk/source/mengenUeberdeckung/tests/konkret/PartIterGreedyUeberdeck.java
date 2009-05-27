/*
 * Dateiname      : PartIterGreedyUeberdeck.java
 * Letzte Änderung: 26. Juli 2006
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
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.konvertierung.PartielleUeberdeckung;
import mengenUeberdeckung.iteratedGreedyCovering.IterEnhancedGreedyHeuristic;

/**
 * Implementiert einen Test mit dem Iterated-Greedy-Verfahren zur nur
 * partiellen Überdeckung einer Menge, wobei die Teilmengen konkret vorgegeben
 * sind.
 *
 * @author  Dietmar Lippold
 */
public class PartIterGreedyUeberdeck {

    /**
     * Führt das Testprogramm aus.
     *
     * @param args  Es muß genau eine ganze Zahl auf der Befehlszeile
     *              übergeben werden. Das ist die Nummer des zu behandelnden
     *              Problems.
     */
    public static void main(String[] args) {
        UeberdeckungsOptimierung  ueberdeckVerfahren;
        PartielleUeberdeckung     partVerfahren;
        ItmFamilie                problem, partUeberdeckung;

        if (args.length != 1) {
            System.err.println("Fehler: Es wurde keine Problemnummer auf"
                               + " der Komandozeile angegeben");
            System.exit(-1);
        }

        int problemNr = Integer.parseInt(args[0]);
        problem = PartielleProbleme.gibProblem(problemNr);

        KonkretesProblemAusgabe.ausgabeProblem(problem);

        ueberdeckVerfahren = new IterEnhancedGreedyHeuristic();
        partVerfahren = new PartielleUeberdeckung(ueberdeckVerfahren);
        partUeberdeckung = partVerfahren.ueberdeckung(problem);

        KonkretesProblemAusgabe.ausgabeUeberdeckung(partUeberdeckung);
    }
}

