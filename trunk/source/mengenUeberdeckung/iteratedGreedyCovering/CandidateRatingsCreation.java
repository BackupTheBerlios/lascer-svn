/*
 * Dateiname      : CandidateRatingsCreation.java
 * Letzte Änderung: 26. Juli 2005
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
 * Dient zur Erzeugung einer Instanz von <CODE>CandidateRatings</CODE>.
 *
 * @author  Dietmar Lippold
 */
public class CandidateRatingsCreation {

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
    public CandidateRatingsCreation(ItmFamilie problemFamilie,
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
    public CandidateRatingsCreation(ItmFamilie problemFamilie,
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
     *                             <CODE>ChvatalRatings</CODE> geliefert werden
     *                             soll.
     * @param frequencyPortion     Die Wahrscheinlichkeit, mit der das Verfahren
     *                             <CODE>FrequencyRatings</CODE> geliefert werden
     *                             soll.
     * @param frequencyAbsPortion  Die Wahrscheinlichkeit, mit der beim Verfahren
     *                             <CODE>FrequencyRatings</CODE> die absolute
     *                             Bewertung verwendet werden soll.
     * @param strategy1Portion     Die Wahrscheinlichkeit, mit der beim Verfahren
     *                             <CODE>MarchSteenRatings</CODE> die erste
     *                             Strategie verwendet werden soll.
     *
     * @return  Ein zufällig ausgewähltes Verfahren zur Bewertung von
     *          Teilmengen.
     */
    public CandidateRatings nextRatings(float probabilityPortion,
                                        float chvatalPortion,
                                        float frequencyPortion,
                                        float frequencyAbsPortion,
                                        float strategy1Portion) {
        float zufallswert;
        int   addStrategyNr;
        int   rmvStrategyNr;

        zufallswert = rand.nextFloat();
        if (zufallswert < probabilityPortion) {
            return (new ProbabilityRatings(problemFamilie));
        } else if (zufallswert < probabilityPortion + chvatalPortion) {
            return (new ChvatalRating());
        } else if (zufallswert < (probabilityPortion + chvatalPortion
                                  + frequencyPortion)) {
            // Prüfen, ob die absolute oder relative Bewertung benutzt
            // werden soll. Bei multicost Problemen wird immer die absolute
            // Bewertung verwendet.
            if (!unicostProblem || (rand.nextFloat() < frequencyAbsPortion)) {
                return (new FrequencyRating(true));
            } else {
                return (new FrequencyRating(false));
            }
        } else {
            // Die zu verwendenden Strategien bestimmen. Bei multicost
            // Problemen wird als add-Strategie immer Strategie 1 verwendet.
            if (!unicostProblem || (rand.nextFloat() < strategy1Portion)) {
                addStrategyNr = 1;
            } else {
                addStrategyNr = 2;
            }
            if (rand.nextFloat() < strategy1Portion) {
                rmvStrategyNr = 1;
            } else {
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
            candidateRatings = nextRatings(Konstanten.U_PROBABILITY_PORTION,
                                           Konstanten.U_CHVATAL_PORTION,
                                           Konstanten.U_FREQUENCY_PORTION,
                                           Konstanten.FREQUENCY_ABS_PORTION,
                                           Konstanten.STRATEGY_1_PORTION);
        } else {
            candidateRatings = nextRatings(Konstanten.M_PROBABILITY_PORTION,
                                           Konstanten.M_CHVATAL_PORTION,
                                           Konstanten.M_FREQUENCY_PORTION,
                                           Konstanten.FREQUENCY_ABS_PORTION,
                                           Konstanten.STRATEGY_1_PORTION);
        }
        return candidateRatings;
    }
}

