/*
 * Dateiname      : IntAbsSubst.java
 * Letzte Änderung: 12. Juni 2006
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

import lascer.intfunktionen.ErzeugbareIntFunk;
import lascer.intfunktionen.arten.AbstZweiStellIntFunk;

/**
 * Eine Funktion, die den Absolutwert einer Differenz berechnet.
 *
 * @author  Dietmar Lippold
 */
public class IntAbsSubst extends AbstZweiStellIntFunk {

    /**
     * Erzeugt eine neue Instanz, die den Absolutwert der Differenz der
     * Funktionswerte der beiden Argumentfunktionen berechnet.
     *
     * @param nummer        Die Nummer dieser Funktion.
     * @param argFunktion1  Die erste Argumentfunktion dieser Funktion.
     * @param argFunktion2  Die zweite Argumentfunktion dieser Funktion.
     */
    public IntAbsSubst(int nummer, ErzeugbareIntFunk argFunktion1,
                       ErzeugbareIntFunk argFunktion2) {
        super(nummer, argFunktion1, argFunktion2);
    }

    /**
     * Liefert die Angabe, daß die Funktion kommutativ ist.
     *
     * @return  Die Angabe, daß die Funktion kommutativ ist.
     */
    public boolean istKommutativ() {
        return true;
    }

    /**
     * Liefert die eigene Komplexität der elementaren Funktion.
     *
     * @return  Die eigene Komplexität der elementaren Funktion.
     */
    protected float eigeneKomplexitaet() {
        return Konstanten.KOMPLEX_ABS_SUBST;
    }

    /**
     * Liefert den Wert der Funktion für die übergebenen Werte der
     * Argumentfunktionen.
     *
     * @param funkWert1  Der Wert der ersten Argumentfunktion.
     * @param funkWert2  Der Wert der zweiten Argumentfunktion.
     *
     * @return  Den Wert der Funktion für die übergebenen Werte der
     *          Argumentfunktionen.
     */
    protected int funkWert(int funkWert1, int funkWert2) {
        return Math.abs(funkWert1 - funkWert2);
    }

    /**
     * Liefert ein Array mit den Werten der Funktion für die übergebenen
     * Werte der Argumentfunktionen.
     *
     * @param funkWerte1  Die Werte der ersten Argumentfunktion.
     * @param funkWerte2  Die Werte der zweiten Argumentfunktion.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          der Funktion auf die Werte der Argumentfunktionen ergeben.
     */
    protected int[] funkWerte(int[] funkWerte1, int[] funkWerte2) {
        int[] funkWerte;
        int   funkWertAnz;

        funkWertAnz = funkWerte1.length;
        funkWerte = new int[funkWertAnz];
        for (int i = 0; i < funkWertAnz; i++) {
            funkWerte[i] = Math.abs(funkWerte1[i] - funkWerte2[i]);
        }
        return funkWerte;
    }

    /**
     * Liefert eine Beschreibung der Funktion.
     *
     * @return  Eine Beschreibung der Funktion.
     */
    public String toString() {
        return ("abs(" + argumentFunktion1().toString() + " - "
                + argumentFunktion2().toString() + ")");
    }
}

