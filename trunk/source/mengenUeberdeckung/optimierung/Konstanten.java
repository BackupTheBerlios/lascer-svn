/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 22. März 2007
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2007
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


package mengenUeberdeckung.optimierung;

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold
 */
public final class Konstanten {

    /**
     * Faktor, mit dem die Anzahl der Teilmengen in der letzten Überdeckung
     * multipliziert wird, um die maximale Länge der Taboo-Liste zu ermitteln.
     */
    public static final float TABOO_FAKTOR = 0.2f;

    /**
     * Beim Optimierungsverfahren <CODE>LocalSearchOptimization</CODE> die
     * Anzahl der zu testenden Längen der Taboo-Liste.
     */
    public static final int TABLOO_LAENGE_ANZ = 5;

    /**
     * Beim Optimierungsverfahren <CODE>LocalSearchOptimization</CODE> der
     * Faktor zur Ermittlung Anzahl der Änderungen der Familie zu jeder zu
     * testenden Länge der Taboo-Liste.
     */
    public static final int TABOO_AENDER_FAKTOR = 10;

    /**
     * Anzahl der redundanten Teilmengen, ab der keine vollständige, d.h.
     * kombinatorische Optimierung sondern nur noch eine Greedy-Optimierung
     * stattfindet.
     */
    public static final int FULL_OPT_BORDER = 8;

    /**
     * Anzahl der Elemente, die Teilmengen maximal alleine überdecken dürfen,
     * um in der Klasse <CODE>IterRemoveOptimization</CODE> testweise entfernt
     * zu werden.
     */
    public static final int MAX_ENTFERN_ANZ = 2;
}

