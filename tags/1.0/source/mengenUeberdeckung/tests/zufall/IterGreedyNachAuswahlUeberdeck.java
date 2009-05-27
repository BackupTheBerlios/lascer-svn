/*
 * Dateiname      : IterGreedyNachAuswahlUeberdeck.java
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

import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.reduktion.UeberdeckNachSofortAuswahl;
import mengenUeberdeckung.heuristiken.utility.FrequencyUtility;
import mengenUeberdeckung.iteratedGreedyCovering.IterEnhancedGreedyHeuristic;

/**
 * Implementiert einen Test für den SCP-Algorithums, bei dem die Familie der
 * Teilmengen, die zur Überdeckung verwendet werden können, zuerst reduziert
 * wird.
 *
 * @author  Natalia Sevcenko, Rene Berleong, Wolfgang Tischer, Dietmar Lippold
 */
public class IterGreedyNachAuswahlUeberdeck {

    /**
     * Führt den Test aus.
     *
     * @param args  Ein Array der Befehlszeilenparameter. Dieses kann einige
     *              der einzulesenden Parameter enthalten oder leer sein.
     *
     * @exception IOException  Exception bei einem Ein-/Ausgabefehler.
     */
    public static void main (String[] args) throws IOException {
        UeberdeckungsOptimierung ueberdeckVerfahren;
        UeberdeckungsOptimierung auswahlVerfahren;
        String                   verfahrensName;

        verfahrensName = "IterEnhancedGreedyHeuristic nach SofortigeAuswahl";
        ueberdeckVerfahren = new IterEnhancedGreedyHeuristic();
        auswahlVerfahren = new UeberdeckNachSofortAuswahl(Konstanten.MIN_ITM_ANZ,
                                                          Konstanten.MIN_UEBERDECK_ANZ,
                                                          Konstanten.FAKTOR_ERG_ANZ,
                                                          new FrequencyUtility(false),
                                                          ueberdeckVerfahren);

        ZufaelligeUeberdeckung.ausfuehrung(args, verfahrensName,
                                           auswahlVerfahren);
    }
}

