/*
 * Dateiname      : RealAdditionErzeugung.java
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
import lascer.realfunktionen.arten.ZweiStellRealFunkErz;
import lascer.realfunktionen.arten.ZweiStellRealFunk;

/**
 * Erzeugt eine Funktion, die eine Summe berechnet.
 *
 * @author  Dietmar Lippold
 */
public class RealAdditionErzeugung implements ZweiStellRealFunkErz {

    /**
     * Erzeugt eine zweistellige Funktion zur Berechnung des Summe der
     * Funktionswerte der beiden Argumentfunktionen.
     *
     * @param nummer        Die Nummer der zweistelligen Funktion, die erzeugt
     *                      werden soll.
     * @param argFunktion1  Die erste Argumentfunktion der zu erzeugenden
     *                      Funktion.
     * @param argFunktion2  Die zweite Argumentfunktion der zu erzeugenden
     *                      Funktion.
     *
     * @return  Die erzeugte zweistellige Funktion zur Berechnung der Summe
     *          der Funktionswerte der beiden Argumentfunktionen.
     */
    public ZweiStellRealFunk zweiStelligeFunktion(int nummer,
                                                  ErzeugbareRealFunk argFunktion1,
                                                  ErzeugbareRealFunk argFunktion2) {
        return new RealAddition(nummer, argFunktion1, argFunktion2);
    }
}

