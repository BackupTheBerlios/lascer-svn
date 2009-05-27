/*
 * Dateiname      : ZweiStellRealFunk.java
 * Letzte Änderung: 25. Februar 2006
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
 * Definiert eine zweistellige real-Funktion.
 *
 * @author  Dietmar Lippold
 */
public interface ZweiStellRealFunk extends ErzeugbareRealFunk {

    /**
     * Liefert die Angabe, ob die Funktion kommutativ ist.
     *
     * @return  Die Angabe, ob die Funktion kommutativ ist.
     */
    public boolean istKommutativ();

    /**
     * Liefert die erste Argument-Funktion.
     *
     * @return  Die erste Argument-Funktion.
     */
    public ErzeugbareRealFunk argumentFunktion1();

    /**
     * Liefert die zweite Argument-Funktion.
     *
     * @return  Die zweite Argument-Funktion.
     */
    public ErzeugbareRealFunk argumentFunktion2();
}

