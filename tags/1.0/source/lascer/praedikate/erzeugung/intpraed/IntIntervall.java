/*
 * Dateiname      : IntIntervall.java
 * Letzte Änderung: 03. Mai 2006
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


package lascer.praedikate.erzeugung.intpraed;

import java.io.Serializable;

/**
 * Repräsentiert ein Intervall aus ganzzahligen Werten.
 *
 * @author  Dietmar Lippold
 */
public class IntIntervall implements Serializable {

    /**
     * Die Untergrenze des Intervalls (inklusive).
     */
    private int untergrenze;

    /**
     * Die Obergrenze des Intervalls (inklusive).
     */
    private int obergrenze;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param untergrenze  Die Untergrenze des Intervalls (inklusive).
     * @param obergrenze   Die Obergrenze des Intervalls (inklusive).
     */
    public IntIntervall(int untergrenze, int obergrenze) {
        this.untergrenze = untergrenze;
        this.obergrenze = obergrenze;
    }

    /**
     * Liefert die Untergrenze des Intervalls.
     *
     * @return  Die Untergrenze des Intervalls.
     */
    public int untergrenze() {
        return untergrenze;
    }

    /**
     * Liefert die Obergrenze des Intervalls.
     *
     * @return  Die Obergrenze des Intervalls.
     */
    public int obergrenze() {
        return obergrenze;
    }

    /**
     * Liefert eine textuelle Darstellung des Intervalls.
     *
     * @return  Eine textuelle Darstellung des Intervalls.
     */
    public String toString() {
        return "[" + untergrenze + ", " + obergrenze + "]";
    }
}

