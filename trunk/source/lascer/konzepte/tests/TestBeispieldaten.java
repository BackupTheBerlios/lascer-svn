/*
 * Dateiname      : TestBeispieldaten.java
 * Letzte Änderung: 31. Mai 2005
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2005
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


package lascer.konzepte.tests;

import lascer.problemdaten.Beispieldaten;

/**
 * Repräsentiert eine Menge von Beispielen und zugehörigen Attributen.
 *
 * @author  Dietmar Lippold
 */
public class TestBeispieldaten extends Beispieldaten {

    /**
     * Die Anzahl der positiven Beispiele.
     */
    private int posBspAnz;

    /**
     * Die Anzahl der negativen Beispiele.
     */
    private int negBspAnz;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param posBspAnz  Die Anzahl der positiven Beispiele.
     * @param negBspAnz  Die Anzahl der negativen Beispiele.
     */
    public TestBeispieldaten(int posBspAnz, int negBspAnz) {
        super("", null);
        this.posBspAnz = posBspAnz;
        this.negBspAnz = negBspAnz;
    }

    /**
     * Liefert die Anzahl der positiven Beispiele.
     *
     * @return  Die Anzahl der positiven Beispiele.
     */
    public int posBspAnz() {
        return posBspAnz;
    }

    /**
     * Liefert die Anzahl der negativen Beispiele.
     *
     * @return  Die Anzahl der negativen Beispiele.
     */
    public int negBspAnz() {
        return negBspAnz;
    }
}

