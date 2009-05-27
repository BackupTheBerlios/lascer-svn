/*
 * Dateiname      : DoubleIndicesCosts.java
 * Letzte Änderung: 14. September 2005
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


package mengenUeberdeckung.heuristiken.costs;

import mengenUeberdeckung.doppelstruktur.DoppelIndexTeilmenge;

/**
 * Definiert Methoden zur Ermittlung der Kosten für Instanzen von
 * <CODE>DoppelItmFamilie</CODE> sowie für das Hinzufügen und Entfernen von
 * Instanzen von <CODE>DoppelIndexTeilmenge</CODE>. Die Bewertungen müssen
 * dabei kompatibel sein, d.h. es muß gelten: Die Bewertung einer partiellen
 * Überdeckung plus der Bewertung einer Doppel-Teilmenge bei Hinzunahme oder
 * Entfernung muß der Bewertung der resultierenden partiellen Überdeckung
 * entsprechen.
 *
 * @author  Dietmar Lippold
 */
public interface DoubleIndicesCosts {

    /**
     * Liefert die Anzahl der Kosten-Indices. Die zugehörigen Index-Kosten
     * werden dem Konstruktor der jeweiligen Klasse übergeben.
     *
     * @return  Die Anzahl der Kosten-Indices.
     */
    public int costIndexNumber();

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer partiellen
     * Überdeckung. Die Kosten der Familie selbst werden nicht berücksichtigt.
     *
     * @param kostenIndexUeberdeckAnz  Die Häufigkeit, mit der die einzelnen
     *                                 Kosten-Indices von den Teilmengen der
     *                                 partielle Überdeckung überdeckt werden.
     *
     * @return  Die Kosten aufgrund der Kosten-Indices der partiellen
     *          Überdeckung.
     */
    public float familyCosts(int[] kostenIndexUeberdeckAnz);

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer Doppel-Teilmenge
     * bei deren Hinzufügen. Die Kosten der Teilmenge selbst werden nicht
     * berücksichtigt. Die Kosten sind ein positiver Wert.
     *
     * @param doppelTeilmenge          Doppel-Teilmenge deren Kosten berechnet
     *                                 werden.
     * @param kostenIndexUeberdeckAnz  Die Häufigkeit, mit der die einzelnen
     *                                 Kosten-Indices von den Teilmengen der
     *                                 partielle Überdeckung überdeckt werden.
     *
     * @return  Kosten der Indices beim Entfernen der Teilmenge.
     */
    public float addCosts(DoppelIndexTeilmenge doppelTeilmenge,
                          int[] kostenIndexUeberdeckAnz);

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer Doppel-Teilmenge
     * bei deren Entfernung. Die Kosten der Teilmenge selbst werden nicht
     * berücksichtigt. Die Kosten sind ein positiver Wert.
     *
     * @param doppelTeilmenge          Doppel-Teilmenge deren Kosten berechnet
     *                                 werden.
     * @param kostenIndexUeberdeckAnz  Die Häufigkeit, mit der die einzelnen
     *                                 Kosten-Indices von den Teilmengen der
     *                                 partielle Überdeckung überdeckt werden.
     *
     * @return  Kosten der Indices beim Entfernen der Teilmenge.
     */
    public float rmvCosts(DoppelIndexTeilmenge doppelTeilmenge,
                          int[] kostenIndexUeberdeckAnz);
}

