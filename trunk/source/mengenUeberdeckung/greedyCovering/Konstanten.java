/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 17. Juli 2005
 * Autoren        : Dietmar Lippold, Rene Berleong
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


package mengenUeberdeckung.greedyCovering;

/**
 * Enthält alle im Algorithmus enthaltene Konstanten (nach einer
 * Beschreibung von Elena Marchiori und Adri Steenbeek ("An Iterated
 * Heuristic Algorithm for the Set Covering Problem", 1998) der Klassen von
 * diesem Package.
 *
 * @author  Dietmar Lippold, Rene Berleong
 */
public final class Konstanten {

    /**
     * Wahrscheinlichkeit, mit welcher das Entfernen einer Teilmenge
     * erlaubt ist.<p>
     *
     * Ursprünglicher Wert: 0.1.<br>
     * Siehe Unterlagen Seite 4, erster Abschnitt, Zeile 3.
     */
    public static final float P_RMV = 0.1f;
}

