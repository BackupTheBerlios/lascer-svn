/*
 * Dateiname      : FixedExistentCosts.java
 * Letzte �nderung: 15. September 2005
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
 * Implementiert Funktionen zur Berechnung von Kosten, die sich als Summe der
 * Kosten der enthaltenen bzw. hinzugef�gten oder entfernten Indices ergeben.
 * Dabei wird Festkomma-Addition verwendet.
 *
 * @author  Dietmar Lippold
 */
public class FixedExistentCosts implements DoubleIndicesCosts {

    /**
     * Gibt zu jedem Kosten-Index die Kosten, multipliziert mit
     * <CODE>zehnerPotenz</CODE>, an, die eine �berdeckung von diesem
     * verursacht.
     */
    private long[] indexKosten;

    /**
     * Die Potenz zur Basis Zehn mit der dem Konstruktor �bergebenen Anzahl
     * von Nachkomma-Stellen als Exponenten.
     */
    private long zehnerPotenz;

    /**
     * Liefert eine neue Instanz. F�r die Berechnungen kann die Anzahl der
     * Nachkomma-Stellen bei der Festkomma-Addition angegeben werden.
     *
     * @param indexKosten   Die Kosten f�r die �berdeckung der einzelnen
     *                      Kosten-Indices.
     * @param nachkommaAnz  Die Anzahl der Nachkomma-Stellen, die bei der
     *                      Addition ber�cksichtigt werden sollen. Bei einem
     *                      negativen Wert wird Flie�komma-Addition verwendet.
     */
    public FixedExistentCosts(float[] indexKosten, int nachkommaAnz) {

        zehnerPotenz = 1;
        for (int i = 0; i < nachkommaAnz; i++) {
            zehnerPotenz *= 10;
        }

        this.indexKosten = new long[indexKosten.length];
        for (int i = 0; i < indexKosten.length; i++) {
            this.indexKosten[i] = Math.round((double) indexKosten[i] * zehnerPotenz);
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
     * �berdeckung. Das ist die Summe der Kosten der Kosten-Indices, die von
     * mindestens einer Teilmenge �berdeckt werden. Die Kosten der Familie
     * selbst werden nicht ber�cksichtigt.
     *
     * @param kostenIndexUeberdeckAnz  Die H�ufigkeit, mit der die einzelnen
     *                                 Kosten-Indices von den Teilmengen der
     *                                 partielle �berdeckung �berdeckt werden.
     *
     * @return  Die Kosten aufgrund der Kosten-Indices der partiellen
     *          �berdeckung.
     */
    public float familyCosts(int[] kostenIndexUeberdeckAnz) {
        long summe;

        summe = 0;
        for (int i = 0; i < kostenIndexUeberdeckAnz.length; i++) {
            if (kostenIndexUeberdeckAnz[i] > 0) {
                summe += indexKosten[i];
            }
        }

        return (float) (((double) summe) / zehnerPotenz);
    }

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer Doppel-Teilmenge
     * bei deren Hinzuf�gen. Dies ist die Summe der Kosten der Kosten-Indices,
     * die erstmalig �berdeckt werden. Die Kosten der Teilmenge selbst werden
     * nicht ber�cksichtigt. Die Kosten sind ein positiver Wert.
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
                          int[] kostenIndexUeberdeckAnz) {
        long summe;

        summe = 0;
        for (int index = doppelTeilmenge.kleinsterEnthaltKostenIndex();
             index >= 0;
             index = doppelTeilmenge.naechsterEnthaltKostenIndex(index + 1)) {

            if (kostenIndexUeberdeckAnz[index] == 0) {
                // Der Kosten-Index ist noch in keiner Teilmenge enthalten.
                summe += indexKosten[index];
            }
        }

        return (float) (((double) summe) / zehnerPotenz);
    }

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer Doppel-Teilmenge
     * bei deren Entfernung. Dies ist die Summe der Kosten der Kosten-Indices,
     * die von der Teilmenge alleine �berdeckt werden. Die Kosten der
     * Teilmenge selbst werden nicht ber�cksichtigt. Die Kosten sind ein
     * positiver Wert.
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
                          int[] kostenIndexUeberdeckAnz) {
        long summe;

        summe = 0;
        for (int index = doppelTeilmenge.kleinsterEnthaltKostenIndex();
             index >= 0;
             index = doppelTeilmenge.naechsterEnthaltKostenIndex(index + 1)) {

            if (kostenIndexUeberdeckAnz[index] == 1) {
                // Der Kosten-Index ist in der Teilmenge alleine enthalten.
                summe += indexKosten[index];
            }
        }

        return (float) (((double) summe) / zehnerPotenz);
    }
}

