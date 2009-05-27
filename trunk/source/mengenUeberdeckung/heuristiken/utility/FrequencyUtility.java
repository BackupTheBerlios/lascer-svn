/*
 * Dateiname      : FrequencyUtility.java
 * Letzte Änderung: 21. August 2005
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


package mengenUeberdeckung.heuristiken.utility;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Diese Klassen implementiert Nutzenfunktionen, bei denen die Häufigkeit der
 * Überdeckung jedes Elements durch die Teilmengen einer Familie
 * berücksichtigt wird.
 *
 * @author  Dietmar Lippold
 */
public class FrequencyUtility implements ReductionUtility {

    /**
     * Enthält vorberechnete Potenzen.
     */
    private float[] potenzCache = new float[0];

    /**
     * Der Exponent, der für die Berechnung der Potenz der Häufigkeit einer
     * Überdeckung verwendet wird.
     */
    private float frecExponent = Konstanten.DEFAULT_FREQUENCY_EXPONENT;

    /**
     * Gibt an, ob bei der Berechnung des Nutzens einer Familie die Anzahl
     * der enthaltenen Elemente sowie beim Nutzen einer Teilmenge die Anzahl
     * der in dieser alleine enthaltenen Elemente berücksichtigt werden soll.
     */
    private boolean withElementCount;

    /**
     * Erzeugt eine neue Instanz ohne Angabe des Exponenten. Als Exponent
     * wird ein Standard-Wert verwendet.
     *
     * @param withElementCount  Gibt an, ob die Anzahl der überdeckten
     *                          Elemente berücksichtigt werden soll.
     */
    public FrequencyUtility(boolean withElementCount) {
        this.withElementCount = withElementCount;
    }

    /**
     * Erzeugt eine neue Instanz unter Angabe des Exponenten.
     *
     * @param withElementCount  Gibt an, ob die Anzahl der überdeckten
     *                          Elemente berücksichtigt werden soll.
     * @param frecExponent      Der Exponent, der bei der Berechnung der Potenz
     *                          der Häufigkeit, mit der ein Element überdeckt
     *                          wird, verwendet wird.
     */
    public FrequencyUtility(boolean withElementCount, float frecExponent) {
        this.withElementCount = withElementCount;
        this.frecExponent = frecExponent;
    }

    /**
     * Liefert den Wert der Potenz mit der angegebenen Basis zum gespeicherten
     * Exponenten.
     *
     * @param basis  Die Basis der Potenz.
     *
     * @return  Den Wert der berechneten Potenz.
     */
    private float potenz(int basis) {
        if (basis >= potenzCache.length) {
            potenzCache = new float[2 * basis + 1];

            for (int i = 0; i < potenzCache.length; i++) {
                potenzCache[i] = (float) Math.pow(i, frecExponent);
            }
        }

        return potenzCache[basis];
    }

    /**
     * Berechnet den Nutzen (die Qualität) einer partiellen Überdeckung.
     *
     * @param currentCover  Die partielle Überdeckung, deren Nutzen berechnet
     *                      werden soll.
     *
     * @return  Den Nutzen der partiellen Überdeckung.
     */
    public float familyUtility(ItmFamilie currentCover) {
        int   groesseGesamtmenge = currentCover.groesseGesamtmenge();
        int   ueberdeckHaeufigkeit;
        float nutzen, summe;

        if (withElementCount) {
            nutzen = currentCover.anzUeberdeckt();
        } else {
            nutzen = 0;
        }

        summe = 0;
        for (int index = 0; index < groesseGesamtmenge; index++) {
            ueberdeckHaeufigkeit = currentCover.ueberdeckungsHaeufigkeit(index);
            summe += potenz(ueberdeckHaeufigkeit);
        }
        nutzen += summe / groesseGesamtmenge;

        return nutzen;
    }

    /**
     * Berechnet den Nutzen (die Qualität) einer Teilmengen für das
     * Hinzufügen zu oder Entfernen aus einer partiellen Überdeckung. Beim
     * Hinzufügen wird ein positiver Wert, beim Entfernen ein negativer Wert
     * geliefert.<P>
     *
     * Die Methode liefert auch dann einen sinnvollen (potentiellen) Wert
     * für das Hinzufügen, wenn die Teilmenge in der Familie schon enthalten
     * ist, und für das Entfernen, wenn die Teilmenge nicht enthalten ist.
     * Ist letzterem Fall werden Elemente, die in der Teinmenge aber nicht
     * in der Familie (in einer Teilmenge der Familie) enthalten sind, nicht
     * berücksichtigt.
     *
     * @param teilmenge     Die Teilmenge, deren Nutzen berechnet werden soll.
     * @param currentCover  Die partielle Überdeckung, zu der die Teilmenge
     *                      hinzugefügt oder aus der sie entfernt werden soll.
     * @param hinzu         Wenn der Wert <CODE>true</CODE> ist, wird der
     *                      Nutzen für das Hinzufügen berechnet, ansonsten
     *                      für das Entfernen.
     *
     * @return  Den Nutzen der hinzuzufügenden oder zu entfernenden Teilmenge.
     */
    protected float itmUtility(IndexTeilmenge teilmenge, ItmFamilie currentCover,
                               boolean hinzu) {
        float summe, wertAlt, wertNeu;
        int   alleinAnz, ueberdeckHaeufigkeit;

        alleinAnz = 0;
        summe = 0;
        for (int index = teilmenge.kleinsterEnthaltenerIndex();
             index >= 0;
             index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {

            ueberdeckHaeufigkeit = currentCover.ueberdeckungsHaeufigkeit(index);

            // Gegebenenfalls prüfen, ob das Element von der übergebenen
            // Teilmenge alleine überdeckt wird.
            if (withElementCount) {
                if (hinzu && (ueberdeckHaeufigkeit == 0)) {
                    alleinAnz += 1;
                } else if ((!hinzu) && (ueberdeckHaeufigkeit == 1)) {
                    alleinAnz -= 1;
                }
            }

            wertAlt = potenz(ueberdeckHaeufigkeit);
            if (hinzu) {
                wertNeu = potenz(ueberdeckHaeufigkeit + 1);
            } else {
                if (ueberdeckHaeufigkeit == 0) {
                    // Es handelt sich um eine potentielle Bewertung (die
                    // Teilmenge ist nicht in der Familie enthalten) und
                    // das Element ist in keiner Teilmenge der Familie
                    // enthalten, kann also nicht entfernt werden.
                    wertNeu = 0;
                } else {
                    wertNeu = potenz(ueberdeckHaeufigkeit - 1);
                }
            }
            summe += (wertNeu - wertAlt);
        }

        return alleinAnz + summe / currentCover.groesseGesamtmenge();
    }

    /**
     * Berechnet den potentiellen Nutzen einer Teilmenge bei deren Hinzufügen,
     * d.h. wenn die Teilmenge schon in der Familie enthalten ist. Dies ist
     * ein positiver Wert.
     *
     * @param teilmenge     Teilmenge deren Nutzen berechnet wird.
     * @param currentCover  Akutelle Überdeckung des SCP.
     *
     * @return  Nutzen der Teilmenge.
     */
    public float addUtilityPot(IndexTeilmenge teilmenge, ItmFamilie currentCover) {
        return itmUtility(teilmenge, currentCover, true);
    }

    /**
     * Berechnet den Nutzen einer Teilmenge bei deren Hinzufügen. Dies ist ein
     * positiver Wert.
     *
     * @param teilmenge     Teilmenge deren Nutzen berechnet wird.
     * @param currentCover  Akutelle Überdeckung des SCP.
     *
     * @return  Nutzen der Teilmenge.
     *
     * @throws IllegalArgumentException  Die übergebene Teilmenge ist schon
     *                                   in <code>currentCover</code>
     *                                   enthalten.
     */
    public float addUtility(IndexTeilmenge teilmenge, ItmFamilie currentCover) {

        // Teilmenge darf noch nicht in der Überdeckung enthalten sein.
        if (currentCover.enthaelt(teilmenge)) {
            throw new IllegalArgumentException("Teilmenge schon enthalten");
        }

        return itmUtility(teilmenge, currentCover, true);
    }

    /**
     * Berechnet den Nutzen einer Teilmenge bei deren Entfernung. Dies ist ein
     * negativer Wert.
     *
     * @param teilmenge     Teilmenge, deren Nutzen berechnet wird.
     * @param currentCover  Akutelle Überdeckung des SCP.
     *
     * @return  Nutzen der Teilmenge.
     *
     * @throws IllegalArgumentException  Die übergebene Teilmenge ist nicht
     *                                   in <code>currentCover</code>
     *                                   enthalten.
     */
    public float rmvUtility(IndexTeilmenge teilmenge, ItmFamilie currentCover) {

        // Teilmenge muss in der Ueberdeckung enthalten sein.
        if (!currentCover.enthaelt(teilmenge)) {
            throw new IllegalArgumentException("Teilmenge nicht enthalten");
        }

        return itmUtility(teilmenge, currentCover, false);
    }
}

