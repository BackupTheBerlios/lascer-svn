/*
 * Dateiname      : FrequencyCosts.java
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
 * Implementiert Funktionen zur Berechnung von Kosten, die sich als Summe von
 * Funktionswerten der Kosten der enthaltenen bzw. hinzugef�gten oder
 * entfernten Indices ergeben. Die Funktionswerte sind Pozenzen der H�ufigkeit
 * der �berdeckung der Indices.
 *
 * @author  Dietmar Lippold
 */
public class FrequencyCosts implements DoubleIndicesCosts {

    /**
     * Enth�lt vorberechnete Potenzen.
     */
    private float[] potenzCache = new float[0];

    /**
     * Gibt zu jedem Kosten-Index die Kosten an, die eine �berdeckung von
     * diesem verursacht.
     */
    private float[] indexKosten;

    /**
     * Die bei einem Aufruf zu addierenden Summanden der Kosten. Vor und nach
     * einem Aufruf haben die Werte keine Bedeutung.
     */
    private float[] kostenSummanden;

    /**
     * Der Exponent, der f�r die Berechnung der Potenz der H�ufigkeit einer
     * �berdeckung verwendet wird.
     */
    private float frecExponent;

    /**
     * Die Anzahl der Nachkomma-Stellen, die bei der Addition ber�cksichtigt
     * werden sollen. Ist der Wert negativ, wird eine Flie�komma-Addition
     * durchgef�hrt.
     */
    private int nachkommaAnz;

    /**
     * Erzeugt eine neue Instanz unter Angabe des Exponenten und der
     * bei der Addition zu ber�cksichtigenden Anzahl von Nachkomma-Stellen.
     *
     * @param indexKosten   Die Kosten f�r die �berdeckung der einzelnen
     *                      Kosten-Indices.
     * @param frecExponent  Der Exponent, der bei der Berechnung der Potenz
     *                      der H�ufigkeit, mit der ein Element �berdeckt
     *                      wird, verwendet wird.
     * @param nachkommaAnz  Die Anzahl der Nachkomma-Stellen, die bei der
     *                      Addition ber�cksichtigt werden sollen. Bei einem
     *                      negativen Wert wird Flie�komma-Addition verwendet.
     */
    public FrequencyCosts(float[] indexKosten, float frecExponent,
                          int nachkommaAnz) {
        this.indexKosten = indexKosten;
        this.frecExponent = frecExponent;
        this.nachkommaAnz = nachkommaAnz;

        kostenSummanden = new float[indexKosten.length];
    }

    /**
     * Erzeugt eine neue Instanz unter Angabe der bei der Addition zu
     * ber�cksichtigenden Anzahl von Nachkomma-Stellen. Als Exponent wird ein
     * Standard-Wert verwendet.
     *
     * @param indexKosten   Die Kosten f�r die �berdeckung der einzelnen
     *                      Kosten-Indices.
     * @param nachkommaAnz  Die Anzahl der Nachkomma-Stellen, die bei der
     *                      Addition ber�cksichtigt werden sollen. Bei einem
     *                      negativen Wert wird Flie�komma-Addition verwendet.
     */
    public FrequencyCosts(float[] indexKosten, int nachkommaAnz) {
        this(indexKosten, Konstanten.DEFAULT_FREQUENCY_EXPONENT, nachkommaAnz);
    }

    /**
     * Erzeugt eine neue Instanz unter Angabe des Exponenten. Als Exponent,
     * der bei der Berechnung der Potenz der H�ufigkeit, mit der ein Element
     * �berdeckt wird, verwendet wird, wird ein Standard-Wert verwendet. Bei
     * den Berechnungen wird eine Flie�komma-Addition verwendet.
     *
     * @param indexKosten  Die Kosten f�r die �berdeckung der einzelnen
     *                     Kosten-Indices.
     */
    public FrequencyCosts(float[] indexKosten) {
        this(indexKosten, Konstanten.DEFAULT_FREQUENCY_EXPONENT, -1);
    }

    /**
     * Liefert die Summe der �bergebenen Werte, wobei die Werte f�r die
     * Addition nach <CODE>double</CODE> gewandelt werden. Das Ergebnis kann
     * aber von der Rechenungenauigkeit beeinflu�t sein und kann auch von der
     * Reihenfolg der Werte im Array abh�ngen.
     *
     * @param werte  Die Werte, deren Summe berechnet werden soll.
     *
     * @return  Die exakte Summe der �bergebenen, gerundeten Werte.
     */
    public static float fliesskommaSumme(float[] werte) {
        double summe;

        summe = 0;
        for (int i = 0; i < werte.length; i++) {
            summe += werte[i];
        }

        return (float) summe;
    }

    /**
     * Liefert die exakte Summe der �bergebenen, auf die angegebene Anzahl von
     * Nachkommastellen gerundeten Werte. Diese ist nicht von der
     * Rechenungenauigkeit beeinflu�t und ist unabh�ngig von der Reihenfolge
     * der Werte im Array. Alle Werte und die Summe m�ssen kleiner als
     * <CODE>LONG.MAX_VALUE / faktor</CODE> sein, wobei <CODE>faktor</CODE>
     * die Zehnerpotenz ist, deren Exponent die angegebene Anzahl von
     * Nachkommastellen ist.
     *
     * @param werte         Die Werte, deren Summe berechnet werden soll.
     * @param nachkommaAnz  Die Anzahl der Nachkommastellen, auf die die Werte
     *                      vor der Addition gerundet werden sollen.
     *
     * @return  Die exakte Summe der �bergebenen, gerundeten Werte.
     */
    public static float festkommaSumme(float[] werte, int nachkommaAnz) {
        long faktor;
        long summe;

        faktor = 1;
        for (int i = 0; i < nachkommaAnz; i++) {
            faktor *= 10;
        }

        summe = 0;
        for (int i = 0; i < werte.length; i++) {
            summe += Math.round((double) werte[i] * faktor);
        }

        return (float) (((double) summe) / faktor);
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
     * Liefert den Wert der Potenz mit der angegebenen Basis zum Exponenten
     * <CODE>frecExponent</CODE>.
     *
     * @param basis  Die Basis der Potenz.
     *
     * @return  Den Wert der berechneten Potenz.
     */
    private float potenz(int basis) {

        if (basis >= potenzCache.length) {
            potenzCache = new float[2 * basis + 1];

            for (int b = 0; b < potenzCache.length; b++) {
                potenzCache[b] = (float) Math.pow(b, frecExponent);
            }
        }

        return potenzCache[basis];
    }

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer partiellen
     * �berdeckung. Das ist die Summe von Funktionswerten der Kosten der
     * Kosten-Indices, die von mindestens einer Teilmenge �berdeckt werden.
     * Die Funktionswerte sind Potenzen der H�ufigkeit der �berdeckung der
     * Indices. Die Kosten der Familie selbst werden nicht ber�cksichtigt.
     *
     * @param kostenIndexUeberdeckAnz  Die H�ufigkeit, mit der die einzelnen
     *                                 Kosten-Indices von den Teilmengen der
     *                                 partielle �berdeckung �berdeckt werden.
     *
     * @return  Die Kosten aufgrund der Kosten-Indices der partiellen
     *          �berdeckung.
     */
    public float familyCosts(int[] kostenIndexUeberdeckAnz) {
        int     ueberdeckAnz;

        for (int i = 0; i < kostenIndexUeberdeckAnz.length; i++) {
            ueberdeckAnz = kostenIndexUeberdeckAnz[i];
            kostenSummanden[i] = indexKosten[i] * potenz(ueberdeckAnz);
        }

        if (nachkommaAnz < 0) {
            return fliesskommaSumme(kostenSummanden);
        } else {
            return festkommaSumme(kostenSummanden, nachkommaAnz);
        }
    }

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer Doppel-Teilmenge
     * bei deren Hinzuf�gen oder Entfernen. Das ist die Summe der Differenzen
     * von Funktionswerten der Kosten der Kosten-Indices vor und nach dem
     * Hinzuf�gen bzw. Entfernen. Die Funktionswerte sind Potenzen der
     * H�ufigkeit der �berdeckung der Indices. Die Kosten der Teilmenge selbst
     * werden nicht ber�cksichtigt. Die Kosten sind ein positiver Wert.
     *
     * @param doppelTeilmenge          Doppel-Teilmenge deren Kosten berechnet
     *                                 werden.
     * @param kostenIndexUeberdeckAnz  Die H�ufigkeit, mit der die einzelnen
     *                                 Kosten-Indices von den Teilmengen der
     *                                 partielle �berdeckung �berdeckt werden.
     * @param hinzu                    Wenn der Wert <CODE>true</CODE> ist,
     *                                 werden die Kosten f�r das Hinzuf�gen
     *                                 berechnet, ansonsten f�r das Entfernen.
     *
     * @return  Kosten der Indices beim Entfernen der Teilmenge.
     */
    private float indicesCosts(DoppelIndexTeilmenge doppelTeilmenge,
                               int[] kostenIndexUeberdeckAnz,
                               boolean hinzu) {
        float wertAlt, wertNeu;
        int   ueberdeckAnz;

        for (int index = doppelTeilmenge.kleinsterEnthaltKostenIndex();
             index >= 0;
             index = doppelTeilmenge.naechsterEnthaltKostenIndex(index + 1)) {
            ueberdeckAnz = kostenIndexUeberdeckAnz[index];
            wertAlt = indexKosten[index] * potenz(ueberdeckAnz);
            if (hinzu) {
                wertNeu = indexKosten[index] * potenz(ueberdeckAnz + 1);
            } else {
                wertNeu = indexKosten[index] * potenz(ueberdeckAnz - 1);
            }
            kostenSummanden[index] = wertNeu - wertAlt;
        }

        if (nachkommaAnz < 0) {
            return fliesskommaSumme(kostenSummanden);
        } else {
            return festkommaSumme(kostenSummanden, nachkommaAnz);
        }
    }

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer Doppel-Teilmenge
     * bei deren Hinzuf�gen. Das ist die Summe der Differenzen von
     * Funktionswerten der Kosten der Kosten-Indices vor und nach dem
     * Hinzuf�gen. Die Funktionswerte sind Potenzen der H�ufigkeit der
     * �berdeckung der Indices. Die Kosten der Teilmenge selbst werden nicht
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
                          int[] kostenIndexUeberdeckAnz) {

        return indicesCosts(doppelTeilmenge, kostenIndexUeberdeckAnz, true);
    }

    /**
     * Berechnet die Kosten aufgrund der Kosten-Indices einer Doppel-Teilmenge
     * bei deren Entfernung. Das ist die Summe der Differenzen von
     * Funktionswerten der Kosten der Kosten-Indices vor und nach dem
     * Entfernen. Die Funktionswerte sind Potenzen der H�ufigkeit der
     * �berdeckung der Indices. Die Kosten der Teilmenge selbst werden nicht
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
                          int[] kostenIndexUeberdeckAnz) {

        return indicesCosts(doppelTeilmenge, kostenIndexUeberdeckAnz, false);
    }
}

