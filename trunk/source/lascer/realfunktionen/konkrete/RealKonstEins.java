/*
 * Dateiname      : RealKonstEins.java
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

import lascer.realfunktionen.arten.AbstNullStellRealFunk;

/**
 * Eine Funktion, die den konstanten Wert Eins liefert.
 *
 * @author  Dietmar Lippold
 */
public class RealKonstEins extends AbstNullStellRealFunk {

    /**
     * Erzeugt eine neue Instanz, die die Konstante Eins als Wert liefert.
     *
     * @param nummer  Die Nummer dieser Funktion.
     */
    public RealKonstEins(int nummer) {
        super(nummer);
    }

    /**
     * Liefert den Wert der Funktion für ein beliebiges Beispiel.
     *
     * @return  Den Wert der Funktion für ein beliebiges Beispiel.
     */
    protected float funkWert() {
        return 1.0f;
    }
}

