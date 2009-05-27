/*
 * Dateiname      : RealNeunMinus.java
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
import lascer.realfunktionen.arten.AbstEinStellRealFunk;

/**
 * Eine Funktion, die die Differenz zur Zahl Neun berechnet.
 *
 * @author  Dietmar Lippold
 */
public class RealNeunMinus extends AbstEinStellRealFunk {

    /**
     * Erzeugt eine neue Instanz, die die Differenz des Funktionswertes der
     * Argumentfunktion zur Zahl Neun.
     *
     * @param nummer       Die Nummer dieser Funktion.
     * @param argFunktion  Die Argumentfunktion dieser Funktion.
     */
    public RealNeunMinus(int nummer, ErzeugbareRealFunk argFunktion) {
        super(nummer, argFunktion);
    }

    /**
     * Gibt die maximale Anzahl an elementaren Funktionen an, die diese
     * Funktion als Gesamtfunktion maximal haben darf.
     *
     * @return  Die maximale Anzahl an elementaren Funktionen dieser Funktion
     *          als Gesamtfunktion.
     */
    public int maxElementFunkAnz() {
        return Konstanten.MAX_ELEMENT_ANZ_NEUN_MINUS;
    }

    /**
     * Liefert die eigene Komplexität der elementaren Funktion.
     *
     * @return  Die eigene Komplexität der elementaren Funktion.
     */
    protected float eigeneKomplexitaet() {
        return Konstanten.KOMPLEX_NEUN_MINUS;
    }

    /**
     * Liefert den Wert der Funktion für den übergebenen Wert der
     * Argumentfunktion.
     *
     * @param funkWert  Der Wert der Argumentfunktion.
     *
     * @return  Den Wert der Funktion für den übergebenen Wert der
     *          Argumentfunktion.
     */
    protected float funkWert(float funkWert) {
        return (9.0f - funkWert);
    }

    /**
     * Liefert ein Array mit den Werten der Funktion für die übergebenen
     * Werte der Argumentfunktion.
     *
     * @param argFunkWerte  Die Werte der Argumentfunktion.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          der Funktion auf die Werte der Argumentfunktionen ergeben.
     */
    protected float[] funkWerte(float[] argFunkWerte) {
        float[] funkWerte;
        int     funkWertAnz;

        funkWertAnz = argFunkWerte.length;
        funkWerte = new float[funkWertAnz];
        for (int i = 0; i < funkWertAnz; i++) {
            funkWerte[i] = 9.0f - argFunkWerte[i];
        }
        return funkWerte;
    }

    /**
     * Liefert eine Beschreibung der Funktion.
     *
     * @return  Eine Beschreibung der Funktion.
     */
    public String toString() {
        return ("(9.0 - " + argumentFunktion().toString() + ")");
    }
}

