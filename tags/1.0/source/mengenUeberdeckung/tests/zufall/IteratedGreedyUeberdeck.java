/*
 * Dateiname      : IteratedGreedyUeberdeck.java
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

import java.io.IOException;

import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.iteratedGreedyCovering.IterEnhancedGreedyHeuristic;

/**
 * Implementiert einen Test f�r den SCP-Algorithums.
 *
 * @author  Natalia Sevcenko, Rene Berleong, Wolfgang Tischer, Dietmar Lippold
 */
public class IteratedGreedyUeberdeck {

    /**
     * F�hrt den Test aus.
     *
     * @param args  Ein Array der Befehlszeilenparameter. Dieses kann einige
     *              der einzulesenden Parameter enthalten oder leer sein.
     *
     * @exception IOException  Exception bei einem Ein-/Ausgabefehler.
     */
    public static void main(String[] args) throws IOException {
        UeberdeckungsOptimierung ueberdeckVerfahren;
        String                   verfahrensName;

        verfahrensName = "IterEnhancedGreedyHeuristic";
        ueberdeckVerfahren = new IterEnhancedGreedyHeuristic();

        ZufaelligeUeberdeckung.ausfuehrung(args, verfahrensName,
                                           ueberdeckVerfahren);
    }
}

