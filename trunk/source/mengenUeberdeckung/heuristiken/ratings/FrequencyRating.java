/*
 * Dateiname      : FrequencyRating.java
 * Letzte �nderung: 25. Juli 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut f�r Intelligente Systeme Universit�t Stuttgart,
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


package mengenUeberdeckung.heuristiken.ratings;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.heuristiken.utility.FrequencyUtility;

/**
 * Diese Klassen implementiert im Kern eine Bewertungsfunktion, bei der die
 * H�ufigkeit der �berdeckung jedes Elements durch die Teilmengen einer
 * Familie ber�cksichttigt wird.
 *
 * @author  Dietmar Lippold
 */
public class FrequencyRating extends SingleCandidateRatings {

    /**
     * Die Klasse zur Berechnung des Nutzens einer Familie und Teilmenge.
     */
    private FrequencyUtility frecUtility;

    /**
     * Der Exponent, der f�r die Berechnung der Potenz der Kosten einer
     * �berdeckung verwendet wird.
     */
    private float costExponent = Konstanten.DEFAULT_REL_COST_EXPONENT;

    /**
     * Gibt an, ob die Bewertungen absolut oder relativ ermittelt werden
     * sollen. Nur bei einer relativen Bewertung wird die Bewertung der
     * <CODE>ItmFamilie</CODE>, in Bezug auf die eine Teilmenge bewertet
     * wird, ber�cksichtigt.
     */
    private boolean absolut;

    /**
     * Erzeugt eine neue Instanz unter Angabe zweier Exponenten.
     *
     * @param frecExponent  Der Exponent, der bei der Berechnung der Potenz
     *                      der H�ufigkeit, mit der ein Element �berdeckt
     *                      wird, verwendet wird.
     * @param costExponent  Der Exponent, der bei der Berechnung der Potenz
     *                      der Kosten einer Teil�berdeckung verwendet wird.
     * @param absolut       Gibt an, ob eine absolute Bewertung erfolgen soll,
     *                      die unabh�ngig von der Bewertung der aktuellen
     *                      Teil�berdeckung ist.
     */
    public FrequencyRating(float frecExponent, float costExponent,
                           boolean absolut) {
        this.costExponent = costExponent;
        this.absolut = absolut;
        this.frecUtility = new FrequencyUtility(true, frecExponent);
    }

    /**
     * Erzeugt eine neue Instanz ohne Angabe eines Exponenten. F�r die
     * Exponenten werden default-Werte verwendet.
     *
     * @param absolut  Gibt an, ob eine absolute Bewertung erfolgen soll,
     *                 die unabh�ngig von der Bewertung der aktuellen
     *                 Teil�berdeckung ist.
     */
    public FrequencyRating(boolean absolut) {
        this.absolut = absolut;
        this.frecUtility = new FrequencyUtility(true);
    }

    /**
     * Liefert den Wert der Potenz zur angegebenen Basis und zum angegebenen
     * Exponenten. In speziellen F�llen erfolgt eine optimierte Berechnung.
     *
     * @param basis     Die Basis der Potenz.
     * @param exponent  Der Exponent der Potenz.
     *
     * @return  Den Wert der berechneten Potenz.
     */
    public static float potenz(float basis, float exponent) {
        if (exponent == 1.0f) {
            return basis;
        } else if (exponent == 0.5f) {
            return (float) Math.sqrt(basis);
        } else {
            return (float) Math.pow(basis, exponent);
        }
    }

    /**
     * Liefert den Funktionswert der Kosten unter Verwendung des vorgegebenen
     * Exponenten.
     *
     * @param kosten  Der Kostenwert, zu dem der Funktionswert geliefert
     *                werden soll.
     *
     * @return  Den Funktionswert der Kosten.
     */
    private float kostenFunkWert(float kosten) {
        return potenz(kosten, costExponent);
    }

    /**
     * Berechnet die Bewertung einer Teilmenge f�r das Hinzuf�gen zu oder f�r
     * das Entfernen aus einer partiellen �berdeckung.
     *
     * @param teilmenge     Teilmenge deren rating berechnet wird.
     * @param currentCover  Akutelle �berdeckung des SCP.
     * @param hinzu         Wenn der Wert <CODE>true</CODE> ist, wird die
     *                      Bewertung f�r das Hinzuf�gen berechnet, ansonsten
     *                      f�r das Entfernen.
     *
     * @return  Bewertung der Teilmenge.
     */
    private float rating(IndexTeilmenge teilmenge, ItmFamilie currentCover,
                         boolean hinzu) {
        float teilmengeNutzen, teilmengeKosten;
        float itmFamilieNutzen, itmFamilieKosten;
        float summeNutzen, summeKosten;
        float familieKostenPotenz, summeKostenPotenz;
        float zaehler, nenner;

        if (!hinzu && (currentCover.groesseFamilie() == 1)) {
            // Es soll die einzige enthaltene Teilmenge entfernt werden.
            return -Float.MAX_VALUE;
        }

        if (hinzu) {
            teilmengeKosten = currentCover.kostenHinzufuegen(teilmenge);
        } else {
            teilmengeKosten = -currentCover.kostenEntfernen(teilmenge);
        }
        if (teilmengeKosten == 0) {
            if (hinzu) {
                return Float.MAX_VALUE;
            } else {
                return -Float.MAX_VALUE;
            }
        } else {
            if (hinzu) {
                teilmengeNutzen = frecUtility.addUtility(teilmenge, currentCover);
            } else {
                teilmengeNutzen = frecUtility.rmvUtility(teilmenge, currentCover);
            }

            if (absolut) {
                return (teilmengeNutzen / teilmengeKosten);
            } else {
                itmFamilieNutzen = frecUtility.familyUtility(currentCover);
                itmFamilieKosten = currentCover.kostenFamilie();
                if (itmFamilieKosten == 0) {
                    return (teilmengeNutzen / teilmengeKosten);
                } else {
                    summeNutzen = itmFamilieNutzen + teilmengeNutzen;
                    summeKosten = itmFamilieKosten + teilmengeKosten;
                    familieKostenPotenz = kostenFunkWert(itmFamilieKosten);
                    summeKostenPotenz = kostenFunkWert(summeKosten);

                    zaehler = summeNutzen * familieKostenPotenz
                              - itmFamilieNutzen * summeKostenPotenz;
                    nenner = summeKostenPotenz * familieKostenPotenz;

                    return (zaehler / nenner);
                }
            }
        }
    }

    /**
     * Berechnet die Bewertung einer Teilmenge zum Hinzuf�gen.
     *
     * @param teilmenge     Teilmenge deren rating berechnet wird.
     * @param currentCover  Akutelle �berdeckung des SCP.
     *
     * @return  Bewertung der Teilmenge.
     *
     * @throws IllegalArgumentException  Die �bergebene Teilmenge ist schon
     *                                   in <code>currentCover</code>
     *                                   enthalten.
     */
    public float wAdd(IndexTeilmenge teilmenge, ItmFamilie currentCover) {

        // Teilmenge darf noch nicht in der �berdeckung enthalten sein:
        if (currentCover.enthaelt(teilmenge)) {
            throw new IllegalArgumentException("Teilmenge schon enthalten");
        }

        return rating(teilmenge, currentCover, true);
    }

    /**
     * Berechnet die Bewertung einer Teilmenge zur Entfernung.
     *
     * @param teilmenge     Teilmenge, deren rating berechnet wird.
     * @param currentCover  Akutelle �berdeckung des SCP.
     *
     * @return  Bewertung der Teilmenge.
     *
     * @throws IllegalArgumentException  Die �bergebene Teilmenge ist schon
     *                                   in <code>currentCover</code>
     *                                   enthalten.
     */
    public float wRmv(IndexTeilmenge teilmenge, ItmFamilie currentCover) {

        // Teilmenge muss in der Ueberdeckung enthalten sein:
        if (!currentCover.enthaelt(teilmenge)) {
            throw new IllegalArgumentException("Teilmenge nicht enthalten");
        }

        return rating(teilmenge, currentCover, false);
    }
}

