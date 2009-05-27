/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 12. August 2006
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


package mengenUeberdeckung.allgemein.itmVerwaltung;

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold, Edgar Binder
 */
public class Konstanten {

    /**
     * Die maximale Anzahl von notwendigen Teilmengen, die in Methode
     * <CODE>neuNichtNotwendigeTeilmengen</CODE> der Klasse
     * <CODE>FreieTeilmengenVerwaltung</CODE> noch mit einem speziellen
     * Verfahren behandelt wird.
     */
    public static final int MAX_ANZ_SPEZIELL = 4;

    /**
     * Die Anzahl der Teilmengen, die für die Verwaltung der Teilmengen
     * gepuffert werden sollen.
     */
    public static final int GROESSE_TEILMENGEN_PUFFER = 2;
}

