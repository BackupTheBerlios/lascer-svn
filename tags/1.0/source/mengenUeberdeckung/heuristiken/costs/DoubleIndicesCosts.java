/*
 * Dateiname      : DoubleIndicesCosts.java
 * Letzte �nderung: 14. September 2005
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut f�r Intelligente Systeme Universit�t Stuttgart,
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


package mengenUeberdeckung.heuristiken.costs;

import mengenUeberdeckung.doppelstruktur.DoppelIndexTeilmenge;

/**
 * Definiert Methoden zur Ermittlung der Kosten f�r Instanzen von
 * <CODE>DoppelItmFamilie</CODE> sowie f�r das Hinzuf�gen und Entfernen von
 * Instanzen von <CODE>DoppelIndexTeilmenge</CODE>. Die Bewertungen m�ssen
 * dabei kompatibel sein, d.h. es mu� gelten: Die Bewertung einer partiellen
 * �berdeckung plus der Bewertung einer Doppel-Teilmenge bei Hinzunahme oder
 * Entfernung mu� der Bewertung der resultierenden partiellen �berdeckung
 * entsprechen.
 *
 * @author  Dietmar Lippold
 */
public interface DoubleIndicesCosts {

    /**
     * Liefert die Anzahl der Kosten-Indices. Die zugeh�rigen Index-Kosten
     * werden dem Konstruktor der jeweiligen Klasse �bergeben.
     *
     * @return  Die Anzahl der Kosten-Indices.
     */
    public int costIndexNumber();

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer partiellen
     * �berdeckung. Die Kosten der Familie selbst werden nicht ber�cksichtigt.
     *
     * @param kostenIndexUeberdeckAnz  Die H�ufigkeit, mit der die einzelnen
     *                                 Kosten-Indices von den Teilmengen der
     *                                 partielle �berdeckung �berdeckt werden.
     *
     * @return  Die Kosten aufgrund der Kosten-Indices der partiellen
     *          �berdeckung.
     */
    public float familyCosts(int[] kostenIndexUeberdeckAnz);

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer Doppel-Teilmenge
     * bei deren Hinzuf�gen. Die Kosten der Teilmenge selbst werden nicht
     * ber�cksichtigt. Die Kosten sind ein positiver Wert.
     *
     * @param doppelTeilmenge          Doppel-Teilmenge deren Kosten berechnet
     *                                 werden.
     * @param kostenIndexUeberdeckAnz  Die H�ufigkeit, mit der die einzelnen
     *                                 Kosten-Indices von den Teilmengen der
     *                                 partielle �berdeckung �berdeckt werden.
     *
     * @return  Kosten der Indices beim Entfernen der Teilmenge.
     */
    public float addCosts(DoppelIndexTeilmenge doppelTeilmenge,
                          int[] kostenIndexUeberdeckAnz);

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer Doppel-Teilmenge
     * bei deren Entfernung. Die Kosten der Teilmenge selbst werden nicht
     * ber�cksichtigt. Die Kosten sind ein positiver Wert.
     *
     * @param doppelTeilmenge          Doppel-Teilmenge deren Kosten berechnet
     *                                 werden.
     * @param kostenIndexUeberdeckAnz  Die H�ufigkeit, mit der die einzelnen
     *                                 Kosten-Indices von den Teilmengen der
     *                                 partielle �berdeckung �berdeckt werden.
     *
     * @return  Kosten der Indices beim Entfernen der Teilmenge.
     */
    public float rmvCosts(DoppelIndexTeilmenge doppelTeilmenge,
                          int[] kostenIndexUeberdeckAnz);
}

