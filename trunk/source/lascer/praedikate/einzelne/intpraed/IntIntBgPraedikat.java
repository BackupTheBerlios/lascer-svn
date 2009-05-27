/*
 * Dateiname      : IntIntBgPraedikat.java
 * Letzte Änderung: 29. August 2006
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


package lascer.praedikate.einzelne.intpraed;

import lascer.problemdaten.attribute.IntAttribut;

/**
 * Definiert ein Prädikat, das die ganzzahligen Werte zweier Attribute
 * daraufhin überprüft, ob der eine Werte den anderen Wert beschränkt oder
 * gleich zu diesem ist, also kleiner-gleich oder größer-gleich ist.
 *
 * @author  Dietmar Lippold
 */
public class IntIntBgPraedikat extends IntIntPraedikat {

    /**
     * Gibt an, ob verlichen wird, ob der Wert des ersten Attributs kleiner
     * oder gleich dem Wert des zweiten Attributs ist. Falls nicht, wird ein
     * größer-gleich Vergleich durchgeführt.
     */
    private final boolean kleinerGleich;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param attribut1      Das erste Attribut, dessen Wert verglichen werden
     *                       soll.
     * @param attribut2      Das zweite Attribut, dessen Wert verglichen
     *                       werden soll.
     * @param kleinerGleich  Gibt an, ob verlichen wird, ob der Wert des
     *                       ersten Attributs kleiner oder gleich dem Wert des
     *                       zweiten Attributs ist. Falls nicht, wird ein
     *                       größer-gleich Vergleich durchgeführt.
     */
    public IntIntBgPraedikat(IntAttribut attribut1, IntAttribut attribut2,
                             boolean kleinerGleich) {

        super(attribut1, attribut2);
        this.kleinerGleich = kleinerGleich;
    }

    /**
     * Liefert die Komplexität des Prädikats.
     *
     * @return  Die Komplexität des Prädikats.
     */
    public float komplexitaet() {
        return Konstanten.INT_INT_BG_PRAED_KOMPLEX;
    }

    /**
     * Ermittelt, ob das Prädikat für die beiden übergebenen Werte erfüllt
     * ist.
     *
     * @param wert1  Der erste Wert, der verglichen werden soll.
     * @param wert2  Der zweite Wert, der verglichen werden soll.
     *
     * @return  Die Angabe, ob das Prädikat für die beiden übergebenen Werte
     *          erfüllt ist.
     */
    protected boolean erfuellt(int wert1, int wert2) {

        if (kleinerGleich) {
            return (wert1 <= wert2);
        } else {
            return (wert1 >= wert2);
        }
    }

    /**
     * Liefert eine Beschreibung der zum Vergleich benutzten Relation des
     * Prädikats.
     *
     * @return  Eine Beschreibung der zum Vergleich benutzten Relation des
     *          Prädikats.
     */
    protected String relation() {

        if (kleinerGleich) {
            return "<=";
        } else {
            return ">=";
        }
    }
}

