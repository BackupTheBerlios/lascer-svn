/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 27. Juli 2006
 * Autoren        : Dietmar Lippold, Rene Berleong
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2006
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


package mengenUeberdeckung.heuristiken.shrinking;

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold, Rene Berleong
 */
public final class Konstanten {

    /**
     * Für die Klasse <CODE>ChvatalPropShrinking</CODE> der Anteil der
     * Teilmengen der Überdeckung, der für die Teil-Überdeckung entfernt
     * werden soll.
     */
    public static final float PROP_REMOVE_PORTION = 0.3f;

    /**
     * Für die Klasse <CODE>UniformShrinking</CODE> die untere Schranke für
     * "restore-fraction" p, welche die Wahrscheinlichkeit dafür angibt, dass
     * eine Teilmenge der aktuellen Lösungsmenge in die Ausgangsmenge der
     * nächsten Iteration aufgenommen wird.<p>
     *
     * Ursprünglicher Wert: 0.6.<br>
     * Siehe Artikel Seite 5, letzter Abschnitt, Zeile 9.
     */
    public static final float RCV_LOW = 0.6f;

    /**
     * Für die Klasse <CODE>UniformShrinking</CODE> die obere Schranke für
     * "restore-fraction" p, welche die Wahrscheinlichkeit dafür angibt, dass
     * eine Teilmenge der aktuellen Lösungsmenge in die Ausgangsmenge der
     * nächsten Iteration aufgenommen wird.<p>
     *
     * Ursprünglicher Wert: 0.8.<br>
     * Siehe Artikel Seite 5, letzter Abschnitt, Zeile 9.
     */
    public static final float RCV_HIGH = 0.8f;

    /**
     * Die Wahrscheinlichkeit, mit der bei einem unicost-Problem keine
     * Teilmenge der aktuellen Lösungsmenge in die Ausgangsmenge der nächsten
     * Iteration aufgenommen wird.<p>
     *
     * Im ursprünglichen Paper war dieser Wert implizit Null.
     */
    public static final float U_RCV_ZERO_PROP = 0.2f;

    /**
     * Die Wahrscheinlichkeit, mit der bei einem multicost-Problem keine
     * Teilmenge der aktuellen Lösungsmenge in die Ausgangsmenge der nächsten
     * Iteration aufgenommen wird.<p>
     *
     * Im ursprünglichen Paper war dieser Wert implizit Null.
     */
    public static final float M_RCV_ZERO_PROP = 0.0f;

    /**
     * Die Wahrscheinlichkeit, mit der bei einem non-linear-Problem keine
     * Teilmenge der aktuellen Lösungsmenge in die Ausgangsmenge der nächsten
     * Iteration aufgenommen wird.<p>
     *
     * Im ursprünglichen Paper war dieser Wert implizit Null.
     */
    public static final float N_RCV_ZERO_PROP = 0.0f;
}

