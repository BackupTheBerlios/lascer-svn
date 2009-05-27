/*
 * Dateiname      : IntKonstZwei.java
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


package lascer.intfunktionen.konkrete;

import lascer.intfunktionen.arten.AbstNullStellIntFunk;

/**
 * Eine Funktion, die den konstanten Wert Zwei liefert.
 *
 * @author  Dietmar Lippold
 */
public class IntKonstZwei extends AbstNullStellIntFunk {

    /**
     * Erzeugt eine neue Instanz, die die Konstante Zwei als Wert liefert.
     *
     * @param nummer  Die Nummer dieser Funktion.
     */
    public IntKonstZwei(int nummer) {
        super(nummer);
    }

    /**
     * Liefert den Wert der Funktion für ein beliebiges Beispiel.
     *
     * @return  Der Wert der Funktion für ein beliebiges Beispiel.
     */
    protected int funkWert() {
        return 2;
    }
}

