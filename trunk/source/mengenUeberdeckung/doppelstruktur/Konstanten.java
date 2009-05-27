/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 05. August 2005
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2005
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


package mengenUeberdeckung.doppelstruktur;

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus dem Package
 * <CODE>mengenUeberdeckung.doppelstruktur</CODE>.
 *
 * @author  Dietmar Lippold
 */
public class Konstanten {

    /**
     * Ein Faktor, mit dem die Summe der Kosten aller Kosten-Indices
     * multipliziert wird, um eine obere Grenze für die Summe der Kosten der
     * Kosten-Indices einer Teilmenge zu erhalten. Der Wert solle ein wenig
     * größer als Eins sein.
     */
    public static final float MAX_KOSTEN_FAKTOR = 1.000001f;

    /**
     * Die Anzahl der Zeichen, um die der Text einer Statistik bei jeder
     * Stufe der Verschachtelung vom linken Rand aus eingerückt werden soll.
     */
    public static final int EINRUECK_TIEFE = mengenUeberdeckung.allgemein.Konstanten.EINRUECK_TIEFE;
}

