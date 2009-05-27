/*
 * Dateiname      : IntFunktion.java
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


package lascer.intfunktionen;

import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;

/**
 * Definiert ein Funktion, die zu einem Beispiel einen ganzzahligen Wert
 * liefert.
 *
 * @author  Dietmar Lippold
 */
public interface IntFunktion {

    /**
     * Liefert die Komplexit�t der Gesamtfunktion.
     *
     * @return  Die Komplexit�t der Gesamtfunktion.
     */
    public float komplexitaet();

    /**
     * Liefert den Wert der Funktion f�r das �bergebene Beispiel.
     *
     * @param beispiel  Ein Beispiel, f�r das der Wert der Funktion ermittelt
     *                  werden soll.
     *
     * @return  Den Wert der Funktion f�r das �bergebene Beispiel.
     */
    public int wert(Beispiel beispiel);

    /**
     * Liefert ein Array mit den Werten der Funktion f�r die positiven
     * Beispiele der �bergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, f�r deren positive Beispiele
     *                       die Werte der Funktion ermittelt werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          der Funktion auf die positiven Beispiele ergeben.
     */
    public int[] posBspWerte(Beispieldaten beispieldaten);

    /**
     * Liefert ein Array mit den Werten der Funktion f�r die negativen
     * Beispiele der �bergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, f�r deren negativen Beispiele
     *                       die Werte der Funktion ermittelt werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          der Funktion auf die negativen Beispiele ergeben.
     */
    public int[] negBspWerte(Beispieldaten beispieldaten);

    /**
     * Liefert eine Beschreibung der Funktion.
     *
     * @return  Eine Beschreibung der Funktion.
     */
    public String toString();
}

