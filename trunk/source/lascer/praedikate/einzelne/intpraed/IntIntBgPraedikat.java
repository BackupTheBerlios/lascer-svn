/*
 * Dateiname      : IntIntBgPraedikat.java
 * Letzte �nderung: 29. August 2006
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
 * Definiert ein Pr�dikat, das die ganzzahligen Werte zweier Attribute
 * daraufhin �berpr�ft, ob der eine Werte den anderen Wert beschr�nkt oder
 * gleich zu diesem ist, also kleiner-gleich oder gr��er-gleich ist.
 *
 * @author  Dietmar Lippold
 */
public class IntIntBgPraedikat extends IntIntPraedikat {

    /**
     * Gibt an, ob verlichen wird, ob der Wert des ersten Attributs kleiner
     * oder gleich dem Wert des zweiten Attributs ist. Falls nicht, wird ein
     * gr��er-gleich Vergleich durchgef�hrt.
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
     *                       gr��er-gleich Vergleich durchgef�hrt.
     */
    public IntIntBgPraedikat(IntAttribut attribut1, IntAttribut attribut2,
                             boolean kleinerGleich) {

        super(attribut1, attribut2);
        this.kleinerGleich = kleinerGleich;
    }

    /**
     * Liefert die Komplexit�t des Pr�dikats.
     *
     * @return  Die Komplexit�t des Pr�dikats.
     */
    public float komplexitaet() {
        return Konstanten.INT_INT_BG_PRAED_KOMPLEX;
    }

    /**
     * Ermittelt, ob das Pr�dikat f�r die beiden �bergebenen Werte erf�llt
     * ist.
     *
     * @param wert1  Der erste Wert, der verglichen werden soll.
     * @param wert2  Der zweite Wert, der verglichen werden soll.
     *
     * @return  Die Angabe, ob das Pr�dikat f�r die beiden �bergebenen Werte
     *          erf�llt ist.
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
     * Pr�dikats.
     *
     * @return  Eine Beschreibung der zum Vergleich benutzten Relation des
     *          Pr�dikats.
     */
    protected String relation() {

        if (kleinerGleich) {
            return "<=";
        } else {
            return ">=";
        }
    }
}

