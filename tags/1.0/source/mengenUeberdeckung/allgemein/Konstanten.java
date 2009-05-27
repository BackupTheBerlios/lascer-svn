/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 22. August 2006
 * Autoren        : Dietmar Lippold, Edgar Binder
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


package mengenUeberdeckung.allgemein;

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold, Edgar Binder
 */
public class Konstanten {

    /**
     * Der Faktor, mit dem die durchschnittliche Anzahl der Elemente pro
     * Teilmenge multipliziert wird, um den Wert gegen die Größe der Familie
     * zu vergleichen, um zu entscheiden, welche Art von Verwaltung für die
     * Teilmengen verwendet wird. Um so größer der Faktor ist, umso seltener
     * wird <CODE>ErwFreieTeilmengenVerwaltung</CODE> verwendet.
     */
    public static final float ITM_VERWALT_AUSWAHL_FAKTOR = 2.0f;

    /**
     * Die Anzahl der Zeichen, um die der Text einer Statistik bei jeder
     * Stufe der Verschachtelung vom linken Rand aus eingerückt werden soll.
     */
    public static final int EINRUECK_TIEFE = 2;
}

