/*
 * Dateiname      : EinStellRealFunk.java
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


package lascer.realfunktionen.arten;

import lascer.realfunktionen.ErzeugbareRealFunk;

/**
 * Definiert eine einstellige real-Funktion.
 *
 * @author  Dietmar Lippold
 */
public interface EinStellRealFunk extends ErzeugbareRealFunk {

    /**
     * Gibt die maximale Anzahl an elementaren Funktionen an, die die
     * Funktion als Gesamtfunktion maximal haben darf. Diese muß gleich oder
     * größer Zwei sein. Der Wert Null steht für eine unbegrenzte Komplexität.
     *
     * @return  Die maximale Anzahl an elementaren Funktionen, die die
     *          Funktion als Gesamtfunktion maximal haben darf.
     */
    public int maxElementFunkAnz();

    /**
     * Liefert die Argument-Funktion.
     *
     * @return  Die Argument-Funktion.
     */
    public ErzeugbareRealFunk argumentFunktion();
}

