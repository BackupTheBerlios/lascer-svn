/*
 * Dateiname      : AbstNullStellIntFunk.java
 * Letzte �nderung: 09. Juni 2006
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


package lascer.intfunktionen.arten;

import java.util.List;
import java.util.LinkedList;

import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;

/**
 * Abstrakte nullstellige int-Funktion.
 *
 * @author  Dietmar Lippold
 */
public abstract class AbstNullStellIntFunk implements NullStellIntFunk {

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
    public AbstNullStellIntFunk(int nummer) {
        this.nummer = nummer;
    }

    /**
     * Gibt an, da� die Funktion immer sinnvoll ist.
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
     * Liefert eine neu erzeugte leere Liste f�r die von dieser Funktion
     * verwendeten Attribute.
     *
     * @return  Eine eine neu erzeugte leere Liste f�r die von dieser Funktion
     *          verwendeten Attribute.
     */
    public List verwendeteAttribute() {
        return new LinkedList();
    }

    /**
     * Liefert die Komplexit�t der Funktion.
     *
     * @return  Die Komplexit�t der Funktion.
     */
    public float komplexitaet() {
        return Konstanten.KOMPLEX_NULL_STELL;
    }

    /**
     * Liefert den Wert der Funktion f�r ein beliebiges Beispiel.
     *
     * @return  Der Wert der Funktion f�r ein beliebiges Beispiel.
     */
    protected abstract int funkWert();

    /**
     * Liefert den Wert der Funktion f�r das �bergebene Beispiel.
     *
     * @param beispiel  Ein Beispiel, f�r das der Wert der Funktion ermittelt
     *                  werden soll.
     *
     * @return  Den Wert der Funktion f�r das �bergebene Beispiel.
     */
    public int wert(Beispiel beispiel) {
        return funkWert();
    }

    /**
     * Liefert ein Array mit den Werten der Funktion f�r die positiven
     * Beispiele der �bergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, f�r deren positive Beispiele
     *                       die Werte der Funktion ermittelt werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten der Funktion f�r die
     *          positiven Beispiele.
     */
    public int[] posBspWerte(Beispieldaten beispieldaten) {
        int[] funkWerte;
        int   funkWert;
        int   funkWertAnz;

        funkWert = funkWert();
        funkWertAnz = beispieldaten.posBspAnz();
        funkWerte = new int[funkWertAnz];
        for (int i = 0; i < funkWertAnz; i++) {
            funkWerte[i] = funkWert;
        }
        return funkWerte;
    }

    /**
     * Liefert ein Array mit den Werten der Funktion f�r die negativen
     * Beispiele der �bergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, f�r deren negative Beispiele
     *                       die Werte der Funktion ermittelt werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten der Funktion f�r die
     *          negativen Beispiele.
     */
    public int[] negBspWerte(Beispieldaten beispieldaten) {
        int[] funkWerte;
        int   funkWert;
        int   funkWertAnz;

        funkWert = funkWert();
        funkWertAnz = beispieldaten.negBspAnz();
        funkWerte = new int[funkWertAnz];
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

