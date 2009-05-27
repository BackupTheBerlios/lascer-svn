/*
 * Dateiname      : KonzeptComparator.java
 * Letzte Änderung: 05. Mai 2006
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


package lascer.konzepte.einzelne;

import java.util.Comparator;
import java.io.Serializable;

import lascer.konzepte.Konzept;

/**
 * Dient dem Vergleich zweier Konzepte.
 *
 * @author  Dietmar Lippold
 */
public class KonzeptComparator implements Comparator, Serializable {

    /**
     * Vergleicht die beiden übergebenen Konzepte.
     *
     * @param konzObjekt1  Das erste der beiden zu vergleichenden Konzepte.
     * @param konzObjekt2  Das zweite der beiden zu vergleichenden Konzepte.
     *
     * @return  Den Wert -1, wenn das erste Konzept kleiner ist als das
     *          zweite, den Wert Null, wenn sie gleich sind und den Wert 1,
     *          wenn das erste größer ist.
     *
     * @throws ClassCastException  Wenn eines der beiden Objekte kein Konzept
     *                             ist.
     */
    public int compare(Object konzObjekt1, Object konzObjekt2) {
        Konzept konzept1, konzept2;

        if (!(konzObjekt1 instanceof Konzept) || !(konzObjekt2 instanceof Konzept)) {
            throw new ClassCastException();
        }

        if (konzObjekt1.equals(konzObjekt2)) {
            return 0;
        }

        konzept1 = (Konzept) konzObjekt1;
        konzept2 = (Konzept) konzObjekt2;

        if (konzept1.istBesser(konzept2)
            || (konzept1.istGleichGut(konzept2)
                && (konzept1.toString().compareTo(konzept2.toString()) < 0))) {
            return -1;
        } else {
            return 1;
        }
    }
}

