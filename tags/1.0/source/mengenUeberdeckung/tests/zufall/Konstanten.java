/*
 * Dateiname      : Konstanten.java
 * Letzte �nderung: 13. M�rz 2004
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut f�r Intelligente Systeme Universit�t Stuttgart,
 *                  2004
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


package mengenUeberdeckung.tests.zufall;

/**
 * Enth�lt Konstanten f�r die Testprogramme.
 *
 * @author  Dietmar Lippold
 */
public final class Konstanten {

    /**
     * Minimale Anzahl der Teilmengen bei der Reduktion. Ein negativer Wert
     * bedeutet, da� keine Reduktion stattfindet. Bei einem Wert von Null
     * werden nur die notwendigen Teilmengen in die prim�re Auswahl
     * aufgenommen. Die Gr��e der erg�nzten Auswahl wird durch
     * <CODE>FAKTOR_ERG_ANZ</CODE> festgelegt.
     */
    public static final int MIN_ITM_ANZ = 0;

    /**
     * Die minimale H�ufigkeit, mit der jedes Element �berdeckt sein soll.
     * Ein Wert von Null bedeutet, da� keine minimale H�uigkeit gefordert
     * wird.
     */
    public static final int MIN_UEBERDECK_ANZ = 0;

    /**
     * Bei der Reduktion das Verh�ltnis der Anzahl der Teilmengen in der
     * erg�nzten Auswahl zur Anzahl der Teilmengen in der Basis-Auswahl. Bei
     * einem Wert von Null wird keine erg�nzende Auswahl erstellt. Ansonsten
     * mu� der Wert gr��er oder gleich Eins sein.
     */
    public static final float FAKTOR_ERG_ANZ = 5.0f;
}

