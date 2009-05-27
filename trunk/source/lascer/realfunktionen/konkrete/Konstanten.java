/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 11. Juni 2006
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

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold
 */
public class Konstanten {

    /**
     * Gibt die Komplexität der elementaren Funktion zur Abbildung eines
     * Beispiels auf einen von dessen Attributwerten an.
     */
    public static final float KOMPLEX_PROJEKTION = 1.0f;

    /**
     * Gibt die Komplexität der elementaren Funktion zur Berechnung der
     * Differenz zur Zahl Neun an.
     */
    public static final float KOMPLEX_NEUN_MINUS = 1.5f;

    /**
     * Gibt die Komplexität der elementaren Funktion zur Berechnung einer
     * Summe an.
     */
    public static final float KOMPLEX_ADDITION = 1.0f;

    /**
     * Gibt die Komplexität der elementaren Funktion zur Berechnung einer
     * Differenz an.
     */
    public static final float KOMPLEX_SUBTRAKTION = 1.0f;

    /**
     * Gibt die Komplexität der elementaren Funktion zur Berechnung eines
     * Produktes an.
     */
    public static final float KOMPLEX_MULTIPLIKATION = 1.25f;

    /**
     * Gibt die Komplexität der elementaren Funktion zur Berechnung des
     * Absolutwertes einer Differenz an.
     */
    public static final float KOMPLEX_ABS_SUBST = 1.5f;

    /**
     * Die Repräsentation eines unbekannten Wertes eines real-Attributs.
     */
    public static final float
        UNBEKANNT_WERT = lascer.problemdaten.Konstanten.UNBEKANNT_REAL;

    /**
     * Gibt die maximale Anzahl an elementaren Funktionen an, die die Funktion
     * zur Berechnung der Differenz zur Zahl Neun als Gesamtfunktion maximal
     * haben darf.
     */
    public static final int MAX_ELEMENT_ANZ_NEUN_MINUS = 2;
}

