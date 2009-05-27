/*
 * Dateiname      : RealAddition.java
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


package lascer.realfunktionen.konkrete;

import lascer.realfunktionen.ErzeugbareRealFunk;
import lascer.realfunktionen.arten.AbstZweiStellRealFunk;

/**
 * Eine Funktion, die eine Summe berechnet.
 *
 * @author  Dietmar Lippold
 */
public class RealAddition extends AbstZweiStellRealFunk {

    /**
     * Erzeugt eine neue Instanz, die die Summe der Funktionswerte der beiden
     * Argumentfunktionen berechnet.
     *
     * @param nummer        Die Nummer dieser Funktion.
     * @param argFunktion1  Die erste Argumentfunktion dieser Funktion.
     * @param argFunktion2  Die zweite Argumentfunktion dieser Funktion.
     */
    public RealAddition(int nummer, ErzeugbareRealFunk argFunktion1,
                        ErzeugbareRealFunk argFunktion2) {
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
        return Konstanten.KOMPLEX_ADDITION;
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
    protected float funkWert(float funkWert1, float funkWert2) {
        return (funkWert1 + funkWert2);
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
    protected float[] funkWerte(float[] funkWerte1, float[] funkWerte2) {
        float[] funkWerte;
        int     funkWertAnz;

        funkWertAnz = funkWerte1.length;
        funkWerte = new float[funkWertAnz];
        for (int i = 0; i < funkWertAnz; i++) {
            funkWerte[i] = funkWerte1[i] + funkWerte2[i];
        }
        return funkWerte;
    }

    /**
     * Liefert eine Beschreibung der Funktion.
     *
     * @return  Eine Beschreibung der Funktion.
     */
    public String toString() {
        return ("(" + argumentFunktion1().toString() + " + "
                + argumentFunktion2().toString() + ")");
    }
}

