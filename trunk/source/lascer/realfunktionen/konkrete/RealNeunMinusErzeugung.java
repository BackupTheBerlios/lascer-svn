/*
 * Dateiname      : RealNeunMinusErzeugung.java
 * Letzte Änderung: 26. Februar 2006
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


package lascer.realfunktionen.konkrete;

import lascer.realfunktionen.ErzeugbareRealFunk;
import lascer.realfunktionen.arten.EinStellRealFunkErz;
import lascer.realfunktionen.arten.EinStellRealFunk;

/**
 * Erzeugt eine Funktion, die die Differenz zur Zahl Neun berechnet.
 *
 * @author  Dietmar Lippold
 */
public class RealNeunMinusErzeugung implements EinStellRealFunkErz {

    /**
     * Erzeugt eine einstellige Funktion zur Berechnung der Differenz des
     * Funktionswertes der Argumentfunktion zur Zahl Neun.
     *
     * @param nummer       Die Nummer der einstelligen Funktion, die erzeugt
     *                     werden soll.
     * @param argFunktion  Die Argumentfunktion der zu erzeugenden Funktion.
     *
     * @return  Die erzeugte einstellige Funktion zur Berechnung der Differenz
     *          des Funktionswertes der Argumentfunktion zur Zahl Neun.
     */
    public EinStellRealFunk einStelligeFunktion(int nummer,
                                                ErzeugbareRealFunk argFunktion) {
        return new RealNeunMinus(nummer, argFunktion);
    }
}

