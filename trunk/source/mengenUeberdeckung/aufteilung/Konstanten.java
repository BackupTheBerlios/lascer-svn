/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 28. Dezember 2004
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
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


package mengenUeberdeckung.aufteilung;

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus dem Package
 * <CODE>mengenUeberdeckung.allgemein</CODE>.
 *
 * @author  Dietmar Lippold
 */
public class Konstanten {

    /**
     * Der Faktor, mit bei der Konvertierung eines Aufteilungsproblems in
     * ein Überdeckungsproblem die Obergenze der Kosten einer Überdeckung
     * multipliziert wird. Er muß größer als 1 sein.
     */
    public static final float KONV_FAKTOR = 1.1f;
}

