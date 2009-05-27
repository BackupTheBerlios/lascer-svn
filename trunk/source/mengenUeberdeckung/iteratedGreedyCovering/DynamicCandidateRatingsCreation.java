/*
 * Dateiname      : DynamicCandidateRatingsCreation.java
 * Letzte Änderung: 19. September 2005
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


package mengenUeberdeckung.iteratedGreedyCovering;

import java.util.Random;
import java.util.Date;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.heuristiken.ratings.CandidateRatings;
import mengenUeberdeckung.heuristiken.ratings.ProbabilityRatings;
import mengenUeberdeckung.heuristiken.ratings.ChvatalRating;
import mengenUeberdeckung.heuristiken.ratings.FrequencyRating;
import mengenUeberdeckung.heuristiken.ratings.MarchSteenRating;

/**
 * Dient zur Erzeugung einer Instanz von <CODE>CandidateRatings</CODE>,
 * wobei die Nützlichkeit der Instanz die Erzeugung weiterer Instanzen
 + beeinflussen kann.
 *
 * @author  Dietmar Lippold
 */
public class DynamicCandidateRatingsCreation {

    /**
     * Die Familie des Problems, zu dem ein Bewertungsverfahren geliefert
     * werden soll.
     */
    private ItmFamilie problemFamilie;

    /**
     * Zufallsgenerator.
     */
    private Random rand;

    /**
     * Initialer Wert für den Anteil der Iterationen, in denen die Klasse
     * <CODE>ProbabilityRating</CODE> bei einem unicost-Problem benutzt werden
     * soll.
     */
    private float uProbabilityPortion = Konstanten.U_PROBABILITY_PORTION;

    /**
     * Initialer Wert für den Anteil der Iterationen, in denen die Klasse
     * <CODE>ProbabilityRating</CODE> bei einem multicost-Problem benutzt
     * werden soll.
     */
    private float mProbabilityPortion = Konstanten.M_PROBABILITY_PORTION;

    /**
     * Initialer Wert für den Anteil der Iterationen, in denen die Klasse
     * <CODE>ChvatalRating</CODE> bei einem unicost-Problem benutzt werden
     * soll.
     */
    private float uChvatalPortion = Konstanten.U_CHVATAL_PORTION;

    /**
     * Initialer Wert für den Anteil der Iterationen, in denen die Klasse
     * <CODE>ChvatalRating</CODE> bei einem multicost-Problem benutzt werden
     * soll.
     */
    private float mChvatalPortion = Konstanten.M_CHVATAL_PORTION;

    /**
     * Initialer Wert für den Anteil der Iterationen, in denen die Klasse
     * <CODE>FrequencyRating</CODE> bei einem unicost-Problem benutzt werden
     * soll.
     */
    private float uFrequencyPortion = Konstanten.U_FREQUENCY_PORTION;

    /**
     * Initialer Wert für den Anteil der Iterationen, in denen die Klasse
     * <CODE>FrequencyRating</CODE> bei einem multicost-Problem benutzt werden
     * soll.
     */
    private float mFrequencyPortion = Konstanten.M_FREQUENCY_PORTION;

    /**
     * Initialer Wert für den Anteil der Fälle, in denen bei Verwendung der
     * Klasse <CODE>FrequencyRating</CODE> die absolute Bewertung verwendet
     * wird.  Bei multicost-Problemen wird immer die absolute Bewertung
     * verwendet.
     */
    private float frequencyAbsPortion = Konstanten.FREQUENCY_ABS_PORTION;

    /**
     * Initialer Wert für den Anteil der Fälle, in denen bei Verwendung der
     * Klasse <CODE>MarchSteenRating</CODE> die Strategie 1 zum Hinzufügung
     * und zum Entfernen vewendet werden soll. In den anderen Fällen wird die
     * Strategie 2 verwendet. Bei multicost Problemen wird als add-Strategie
     * immer Strategie 1 verwendet.
     */
    private float strategy1Portion = Konstanten.STRATEGY_1_PORTION;

    /**
     * Gibt an, ob bei der Erzeugung der letzten Instanz von
     * <CODE>ProbabilityRatings</CODE> das Chvatal-Verfahren benutzt wurde.
     */
    private boolean probabilityProcUsed = false;

    /**
     * Gibt an, ob bei der Erzeugung der letzten Instanz von
     * <CODE>CandidateRatings</CODE> das Chvatal-Verfahren benutzt wurde.
     */
    private boolean chvatalProcUsed = false;

    /**
     * Gibt an, ob bei der Erzeugung der letzten Instanz von
     * <CODE>CandidateRatings</CODE> das Frequency-Verfahren benutzt wurde.
     */
    private boolean frequencyProcUsed = false;

    /**
     * Gibt an, ob bei der Erzeugung der letzten Instanz von
     * <CODE>CandidateRatings</CODE> das Attribut
     * <CODE>frequencyAbsPortion</CODE> benutzt wurde.
     */
    private boolean frequencyAbsPortionUsed = false;

    /**
     * Gibt an, ob bei der Erzeugung der letzten Instanz von
     * <CODE>CandidateRatings</CODE> das Attribut <CODE>strategy1Portion</CODE>
     * benutzt wurde.
     */
    private boolean strategy1PortionUsed = false;

    /**
     * Gibt an, ob Verfahren zu einem unicost-Problem erzeugt werden sollen.
     */
    private boolean unicostProblem;

    /**
     * Konstruktor der Klasse.
     *
     * @param problemFamilie  Die Familie des Problems, zu dem ein
     *                        Bewertungsverfahren geliefert werden soll.
     * @param unicostProblem  Gibt an, ob ein Verfahren zu einem unicost-Problem
     *                        geliefert werden soll.
     * @param rand            Ein Zufallsgenerator.
     */
    public DynamicCandidateRatingsCreation(ItmFamilie problemFamilie,
                                           boolean unicostProblem, Random rand) {
        this.problemFamilie = problemFamilie;
        this.unicostProblem = unicostProblem;
        this.rand = rand;
    }

    /**
     * Konstruktor der Klasse. Der Zufallsgenerator wird neu erzeugt.
     *
     * @param problemFamilie  Die Familie des Problems, zu dem ein
     *                        Bewertungsverfahren geliefert werden soll.
     * @param unicostProblem  Gibt an, ob Verfahren zu einem unicost-Problem
     *                        erzeugt werden sollen.
     */
    public DynamicCandidateRatingsCreation(ItmFamilie problemFamilie,
                                           boolean unicostProblem) {
        this(problemFamilie, unicostProblem, new Random());

        if (Konstanten.SEED == 0) {
            rand.setSeed((new Date()).getTime());
        } else {
            rand.setSeed(Konstanten.SEED);
        }
    }

    /**
     * Liefert ein zufällig ausgewähltes Verfahren zur Bewertung von
     * Teilmengen.
     *
     * @param probabilityPortion   Die Wahrscheinlichkeit, mit der das Verfahren
     *                             <CODE>ProbabilityRatings</CODE> geliefert
     *                             werden soll.
     * @param chvatalPortion       Die Wahrscheinlichkeit, mit der das Verfahren
     *                             <CODE>ChvatalRating</CODE> geliefert werden
     *                             soll.
     * @param frequencyPortion     Die Wahrscheinlichkeit, mit der das Verfahren
     *                             <CODE>FrequencyRating</CODE> geliefert werden
     *                             soll.
     * @param frequencyAbsPortion  Die Wahrscheinlichkeit, mit der beim Verfahren
     *                             <CODE>FrequencyRating</CODE> die absolute
     *                             Bewertung verwendet werden soll.
     * @param strategy1Portion     Die Wahrscheinlichkeit, mit der beim Verfahren
     *                             <CODE>MarchSteenRating</CODE> die erste
     *                             Strategie verwendet werden soll.
     *
     * @return  Ein zufällig ausgewähltes Verfahren zur Bewertung von
     *          Teilmengen.
     */
    public CandidateRatings nextRating(float probabilityPortion,
                                       float chvatalPortion,
                                       float frequencyPortion,
                                       float frequencyAbsPortion,
                                       float strategy1Portion) {
        float zufallswert;
        int   addStrategyNr;
        int   rmvStrategyNr;

        chvatalProcUsed = false;
        frequencyProcUsed = false;

        zufallswert = rand.nextFloat();
        if (zufallswert < probabilityPortion) {
            probabilityProcUsed = true;
            return (new ProbabilityRatings(problemFamilie));
        } else if (zufallswert < probabilityPortion + chvatalPortion) {
            chvatalProcUsed = true;
            return (new ChvatalRating());
        } else if (zufallswert < (probabilityPortion + chvatalPortion
                                  + frequencyPortion)) {
            frequencyProcUsed = true;
            // Prüfen, ob die absolute oder relative Bewertung benutzt
            // werden soll. Bei multicost Problemen wird immer die absolute
            // Bewertung verwendet.
            if (!unicostProblem || (rand.nextFloat() < frequencyAbsPortion)) {
                frequencyAbsPortionUsed = true;
                return (new FrequencyRating(true));
            } else {
                frequencyAbsPortionUsed = false;
                return (new FrequencyRating(false));
            }
        } else {
            // Die zu verwendenden Strategien bestimmen. Bei multicost
            // Problemen wird als add-Strategie immer Strategie 1 verwendet.
            if (rand.nextFloat() < strategy1Portion) {
                strategy1PortionUsed = true;
                addStrategyNr = 1;
                rmvStrategyNr = 1;
            } else {
                strategy1PortionUsed = false;
                if (!unicostProblem) {
                    addStrategyNr = 1;
                } else {
                    addStrategyNr = 2;
                }
                rmvStrategyNr = 2;
            }
            return (new MarchSteenRating(addStrategyNr, rmvStrategyNr,
                                         unicostProblem));
        }
    }

    /**
     * Liefert ein zufällig ausgewähltes Verfahren zur Bewertung von
     * Teilmengen.
     *
     * @return  Ein zufällig ausgewähltes Verfahren zur Bewertung von
     *          Teilmengen.
     */
    public CandidateRatings candidateRatings() {
        CandidateRatings candidateRatings;

        if (unicostProblem) {
            candidateRatings = nextRating(uProbabilityPortion, uChvatalPortion,
                                          uFrequencyPortion, frequencyAbsPortion,
                                          strategy1Portion);
        } else {
            candidateRatings = nextRating(mProbabilityPortion, mChvatalPortion,
                                          mFrequencyPortion, frequencyAbsPortion,
                                          strategy1Portion);
        }
        return candidateRatings;
    }

    /**
     * Verändert die Gewichte der zuletzt benutzten Parameter in Abhängigkeit
     * vom Erfolg der Verwendung der damit erzeugten Instanz.
     *
     * @param success  Gibt an, ob die Verwendung der zuletzt erzeugten
     *                 Instanz von <CODE>CandidateRatings</CODE> erfolgreich
     *                 war.
     */
    public void changeParameters(boolean success) {
        float marchSteenPortion, faktor, summe;

        if (success) {
            faktor = Konstanten.PORTION_CHANGE;
        } else {
            faktor = 1 / Konstanten.PORTION_CHANGE;
        }

        // Berechnung für den bisherigen Anteil des March-Steen-Verfahren.
        if (unicostProblem) {
            marchSteenPortion = 1 - uChvatalPortion - uFrequencyPortion;
        } else {
            marchSteenPortion = 1 - mChvatalPortion - mFrequencyPortion;
        }

        if (probabilityProcUsed) {
            // Zuletzt wurde das Probability-Verfahren benutzt.

            if (unicostProblem) {
                uProbabilityPortion *= faktor;
            } else {
                mProbabilityPortion *= faktor;
            }
        } else if (chvatalProcUsed) {
            // Zuletzt wurde das Chvatal-Verfahren benutzt.

            if (unicostProblem) {
                uChvatalPortion *= faktor;
            } else {
                mChvatalPortion *= faktor;
            }
        } else if (frequencyProcUsed) {
            // Zuletzt wurde das Frequency-Verfahren benutzt.

            if (unicostProblem) {
                uFrequencyPortion *= faktor;
            } else {
                mFrequencyPortion *= faktor;
            }

            if (frequencyAbsPortionUsed && success
                    || !frequencyAbsPortionUsed && !success) {
                frequencyAbsPortion = (float) Math.sqrt(frequencyAbsPortion);
            } else {
                frequencyAbsPortion *= frequencyAbsPortion;
            }
        } else {
            // Zuletzt wurde das March-Steen-Verfahren benutzt.

            marchSteenPortion *= faktor;

            if (strategy1PortionUsed && success
                    || !strategy1PortionUsed && !success) {
                strategy1Portion = (float) Math.sqrt(strategy1Portion);
            } else {
                strategy1Portion *= strategy1Portion;
            }
        }

        // Normierung der Parameter
        if (unicostProblem) {
            summe = (uProbabilityPortion + uChvatalPortion
                     + uFrequencyPortion + marchSteenPortion);
            uProbabilityPortion /= summe;
            uChvatalPortion /= summe;
            uFrequencyPortion /= summe;
        } else {
            summe = (mProbabilityPortion + mChvatalPortion
                     + mFrequencyPortion + marchSteenPortion);
            mProbabilityPortion /= summe;
            mChvatalPortion /= summe;
            mFrequencyPortion /= summe;
        }
    }
}

