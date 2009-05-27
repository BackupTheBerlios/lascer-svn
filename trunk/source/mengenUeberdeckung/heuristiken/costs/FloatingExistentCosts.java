/*
 * Dateiname      : FloatingExistentCosts.java
 * Letzte Änderung: 15. September 2005
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
 * Implementiert Funktionen zur Berechnung von Kosten, die sich als Summe der
 * Kosten der enthaltenen bzw. hinzugefügten oder entfernten Indices ergeben.
 * Dabei wird Fließkomma-Addition verwendet.
 *
 * @author  Dietmar Lippold
 */
public class FloatingExistentCosts implements DoubleIndicesCosts {

    /**
     * Gibt zu jedem Kosten-Index die Kosten an, die eine Überdeckung von
     * diesem verursacht.
     */
    private double[] indexKosten;

    /**
     * Liefert eine neue Instanz.
     *
     * @param indexKosten   Die Kosten für die Überdeckung der einzelnen
     *                      Kosten-Indices.
     */
    public FloatingExistentCosts(float[] indexKosten) {

        for (int i = 0; i < indexKosten.length; i++) {
            this.indexKosten[i] = indexKosten[i];
        }
    }

    /**
     * Liefert die Anzahl der Kosten-Indices.
     *
     * @return  Die Anzahl der Kosten-Indices.
     */
    public int costIndexNumber() {
        return indexKosten.length;
    }

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer partiellen
     * Überdeckung. Das ist die Summe der Kosten der Kosten-Indices, die von
     * mindestens einer Teilmenge überdeckt werden. Die Kosten der Familie
     * selbst werden nicht berücksichtigt.
     *
     * @param kostenIndexUeberdeckAnz  Die Häufigkeit, mit der die einzelnen
     *                                 Kosten-Indices von den Teilmengen der
     *                                 partielle Überdeckung überdeckt werden.
     *
     * @return  Die Kosten aufgrund der Kosten-Indices der partiellen
     *          Überdeckung.
     */
    public float familyCosts(int[] kostenIndexUeberdeckAnz) {
        double summe;

        summe = 0;
        for (int i = 0; i < kostenIndexUeberdeckAnz.length; i++) {
            if (kostenIndexUeberdeckAnz[i] > 0) {
                summe += indexKosten[i];
            }
        }

        return (float) summe;
    }

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer Doppel-Teilmenge
     * bei deren Hinzufügen. Dies ist die Summe der Kosten der Kosten-Indices,
     * die erstmalig überdeckt werden. Die Kosten der Teilmenge selbst werden
     * nicht berücksichtigt. Die Kosten sind ein positiver Wert.
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
                          int[] kostenIndexUeberdeckAnz) {
        double summe;

        summe = 0;
        for (int index = doppelTeilmenge.kleinsterEnthaltKostenIndex();
             index >= 0;
             index = doppelTeilmenge.naechsterEnthaltKostenIndex(index + 1)) {

            if (kostenIndexUeberdeckAnz[index] == 0) {
                // Der Kosten-Index ist noch in keiner Teilmenge enthalten.
                summe += indexKosten[index];
            }
        }

        return (float) summe;
    }

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer Doppel-Teilmenge
     * bei deren Entfernung. Dies ist die Summe der Kosten der Kosten-Indices,
     * die von der Teilmenge alleine überdeckt werden. Die Kosten der
     * Teilmenge selbst werden nicht berücksichtigt. Die Kosten sind ein
     * positiver Wert.
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
                          int[] kostenIndexUeberdeckAnz) {
        double summe;

        summe = 0;
        for (int index = doppelTeilmenge.kleinsterEnthaltKostenIndex();
             index >= 0;
             index = doppelTeilmenge.naechsterEnthaltKostenIndex(index + 1)) {

            if (kostenIndexUeberdeckAnz[index] == 1) {
                // Der Kosten-Index ist in der Teilmenge alleine enthalten.
                summe += indexKosten[index];
            }
        }

        return (float) summe;
    }
}

