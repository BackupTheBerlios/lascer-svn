/*
 * Dateiname      : AbstNullStellRealFunk.java
 * Letzte Änderung: 09. Juni 2006
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


package lascer.realfunktionen.arten;

import java.util.List;
import java.util.LinkedList;

import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;

/**
 * Abstrakte nullstellige real-Funktion.
 *
 * @author  Dietmar Lippold
 */
public abstract class AbstNullStellRealFunk implements NullStellRealFunk {

    /**
     * Die Nummer dieser Funktion unter allen nullstelligen Funktionen.
     */
    private int nummer;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param nummer  Die Nummer der zu erzeugenden Funktion unter allen
     *                zweistelligen Funktionen.
     */
    public AbstNullStellRealFunk(int nummer) {
        this.nummer = nummer;
    }

    /**
     * Gibt an, daß die Funktion immer sinnvoll ist.
     *
     * @return  Den Wert <CODE>true</CODE>.
     */
    public boolean istSinnvoll() {
        return true;
    }

    /**
     * Liefert die Nummer der Funktion unter den Funktionen mit gleicher
     * Stelligkeit.
     *
     * @return  Die Nummer der Funktion.
     */
    public int nummer() {
        return nummer;
    }

    /**
     * Liefert die Anzahl der elementaren Funktionen in der Gesamtfunktion.
     *
     * @return  Die Anzahl der elementaren Funktionen.
     */
    public int elementFunkAnz() {
        return 1;
    }

    /**
     * Liefert eine neu erzeugte leere Liste für die von dieser Funktion
     * verwendeten Attribute.
     *
     * @return  Eine eine neu erzeugte leere Liste für die von dieser Funktion
     *          verwendeten Attribute.
     */
    public List verwendeteAttribute() {
        return new LinkedList();
    }

    /**
     * Liefert die Komplexität der Funktion.
     *
     * @return  Die Komplexität der Funktion.
     */
    public float komplexitaet() {
        return Konstanten.KOMPLEX_NULL_STELL;
    }

    /**
     * Liefert den Wert der Funktion für ein beliebiges Beispiel.
     *
     * @return  Der Wert der Funktion für ein beliebiges Beispiel.
     */
    protected abstract float funkWert();

    /**
     * Liefert den Wert der Funktion für das übergebene Beispiel.
     *
     * @param beispiel  Ein Beispiel, für das der Wert der Funktion ermittelt
     *                  werden soll.
     *
     * @return  Den Wert der Funktion für das übergebene Beispiel.
     */
    public float wert(Beispiel beispiel) {
        return funkWert();
    }

    /**
     * Liefert ein Array mit den Werten der Funktion für die positiven
     * Beispiele der übergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, für deren positive Beispiele
     *                       die Werte der Funktion ermittelt werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten der Funktion für die
     *          positiven Beispiele.
     */
    public float[] posBspWerte(Beispieldaten beispieldaten) {
        float[] funkWerte;
        float   funkWert;
        int     funkWertAnz;

        funkWert = funkWert();
        funkWertAnz = beispieldaten.posBspAnz();
        funkWerte = new float[funkWertAnz];
        for (int i = 0; i < funkWertAnz; i++) {
            funkWerte[i] = funkWert;
        }
        return funkWerte;
    }

    /**
     * Liefert ein Array mit den Werten der Funktion für die negativen
     * Beispiele der übergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, für deren negative Beispiele
     *                       die Werte der Funktion ermittelt werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten der Funktion für die
     *          negativen Beispiele.
     */
    public float[] negBspWerte(Beispieldaten beispieldaten) {
        float[] funkWerte;
        float   funkWert;
        int     funkWertAnz;

        funkWert = funkWert();
        funkWertAnz = beispieldaten.negBspAnz();
        funkWerte = new float[funkWertAnz];
        for (int i = 0; i < funkWertAnz; i++) {
            funkWerte[i] = funkWert;
        }
        return funkWerte;
    }

    /**
     * Liefert eine Beschreibung der Funktion.
     *
     * @return  Eine Beschreibung der Funktion.
     */
    public String toString() {
        return String.valueOf(funkWert());
    }
}

