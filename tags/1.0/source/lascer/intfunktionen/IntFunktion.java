/*
 * Dateiname      : IntFunktion.java
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
     * Liefert die Komplexität der Gesamtfunktion.
     *
     * @return  Die Komplexität der Gesamtfunktion.
     */
    public float komplexitaet();

    /**
     * Liefert den Wert der Funktion für das übergebene Beispiel.
     *
     * @param beispiel  Ein Beispiel, für das der Wert der Funktion ermittelt
     *                  werden soll.
     *
     * @return  Den Wert der Funktion für das übergebene Beispiel.
     */
    public int wert(Beispiel beispiel);

    /**
     * Liefert ein Array mit den Werten der Funktion für die positiven
     * Beispiele der übergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, für deren positive Beispiele
     *                       die Werte der Funktion ermittelt werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          der Funktion auf die positiven Beispiele ergeben.
     */
    public int[] posBspWerte(Beispieldaten beispieldaten);

    /**
     * Liefert ein Array mit den Werten der Funktion für die negativen
     * Beispiele der übergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, für deren negativen Beispiele
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

