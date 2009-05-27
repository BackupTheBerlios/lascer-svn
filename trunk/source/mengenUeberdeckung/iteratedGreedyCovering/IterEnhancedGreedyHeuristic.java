/*
 * Dateiname      : IterEnhancedGreedyHeuristic.java
 * Letzte Änderung: 22. März 2007
 * Autoren        : Dietmar Lippold, Rene Berleong
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


package mengenUeberdeckung.iteratedGreedyCovering;

import java.util.Set;
import java.util.HashSet;
import java.util.Random;
import java.util.Date;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.greedyCovering.EnhancedGreedyHeuristic;
import mengenUeberdeckung.heuristiken.selection.CandidateSelection;
import mengenUeberdeckung.heuristiken.selection.BestRatingSelection;
import mengenUeberdeckung.heuristiken.creation.CandidatesCreation;
import mengenUeberdeckung.heuristiken.creation.FewestCandidatesCreation;
import mengenUeberdeckung.heuristiken.creation.MostCandidatesCreation;
import mengenUeberdeckung.heuristiken.ratings.CandidateRatings;
import mengenUeberdeckung.heuristiken.ratings.ChvatalRating;
import mengenUeberdeckung.heuristiken.ratings.ProbabilityRatings;
import mengenUeberdeckung.heuristiken.shrinking.FamilyShrinking;
import mengenUeberdeckung.heuristiken.shrinking.UniformShrinking;
import mengenUeberdeckung.optimierung.Optimization;
import mengenUeberdeckung.optimierung.SequenceOptimization;
import mengenUeberdeckung.optimierung.InferiorOptimization;
import mengenUeberdeckung.optimierung.AddOneOptimization;
import mengenUeberdeckung.optimierung.AddTwoOptimization;
import mengenUeberdeckung.optimierung.LocalSearchOptimization;

/**
 * Berechnet eine Lösung eines "Set Covering Problem" durch wiederholtes
 * Anwenden des "Enhancend Greedy" Algorithmus nach einer Beschreibung von
 * Elena Marchiori und Adri Steenbeek ("An Iterated Heuristic Algorithm for
 * the Set Covering Problem", 1998).
 *
 * @author  Dietmar Lippold, Rene Berleong
 */
public class IterEnhancedGreedyHeuristic implements UeberdeckungsOptimierung {

    /**
     * Zufallsgenerator.
     */
    private Random rand;

    /**
     * Das zu verwendende Verfahren zur Optimierung.
     */
    private Optimization optVerfahren;

    /**
     * Das zu verwendende Verfahren zur Verkleinerung einer Überdeckung.
     */
    private FamilyShrinking shrinkVerfahren;

    /**
     * Die Anzahl der bisher verarbeiteten Probleme.
     */
    private int verarbeitProblemAnz = 0;

    /**
     * Die auszuführende Anzahl der Iterationen des
     * Enhanced-Greedy-Verfahrens.
     */
    private int numberOfIterations = Konstanten.NUMBER_OF_ITERATIONS;

    /**
     * Gibt an, ob nach Ermittlung einer Lösung das Optimierungsverfahren
     * der Klasse <CODE>InferiorOptimization</CODE> angewendet werden soll.
     */
    private boolean useInferiorOpt = Konstanten.USE_INFERIOR_OPT;

    /**
     * Gibt an, ob zusätzlich das Optimierungsverfahren der Klasse
     * <CODE>AddOneOptimization</CODE> angewendet werden soll.
     */
    private boolean useAddOneOpt = Konstanten.USE_ADD_ONE_OPT;

    /**
     * Gibt an, ob zusätzlich das Optimierungsverfahren der Klasse
     * <CODE>AddTwoOptimization</CODE> angewendet werden soll.
     */
    private boolean useAddTwoOpt = Konstanten.USE_ADD_TWO_OPT;

    /**
     * Konstruktor der Klasse, bei dem die Anzahl der auszuführenden
     * Iterationen angegeben wird.
     *
     * @param numberOfIterations  Die auszuführende Anzahl der Iterationen des
     *                            Enhanced-Greedy-Verfahrens.
     *
     * @throws IllegalArgumentException  Wenn die übergebene Anzahl der
     *                                   Iterationen kleiner oder gleich Null
     *                                   ist.
     */
    public IterEnhancedGreedyHeuristic(int numberOfIterations) {
        SequenceOptimization sequenceOpt;
        Optimization         localSearchOpt;

        if (numberOfIterations <= 0) {
            throw new IllegalArgumentException("Die Anzahl der Iterationen ist"
                                               + " kleiner oder gleich Null");
        }

        this.numberOfIterations = numberOfIterations;

        rand = new Random();
        if (Konstanten.SEED == 0) {
            rand.setSeed((new Date()).getTime());
        } else {
            rand.setSeed(Konstanten.SEED);
        }

        sequenceOpt = new SequenceOptimization();
        if (Konstanten.USE_INFERIOR_OPT) {
            sequenceOpt.addOptimization(new InferiorOptimization());
        }
        if (Konstanten.USE_ADD_ONE_OPT) {
            sequenceOpt.addOptimization(new AddOneOptimization());
        }
        if (Konstanten.USE_ADD_TWO_OPT) {
            sequenceOpt.addOptimization(new AddTwoOptimization());
        }
        localSearchOpt = new LocalSearchOptimization(rand, false,
                                                     numberOfIterations);
        sequenceOpt.addOptimization(localSearchOpt);
        optVerfahren = sequenceOpt;

        shrinkVerfahren = new UniformShrinking(rand);
    }

    /**
     * Konstruktor der Klasse. Für die Anzahl der Iterationen und die Auswahl
     * der Optimierungsverfahren werden default-Werte verwendet.
     */
    public IterEnhancedGreedyHeuristic() {
        this(Konstanten.NUMBER_OF_ITERATIONS);
    }

    /**
     * Konstruktor der Klasse, bei dem ein Zufallsgenerator und die Anzahl der
     * auszuführenden Iterationen angegeben wird.
     *
     * @param random              Der zu verwendende Zufallsgenerator.
     * @param numberOfIterations  Die auszuführende Anzahl der Iterationen des
     *                            Enhanced-Greedy-Verfahrens.
     *
     * @throws IllegalArgumentException  Wenn die übergebene Anzahl der
     *                                   Iterationen kleiner oder gleich Null
     *                                   ist.
     */
    public IterEnhancedGreedyHeuristic(Random random, int numberOfIterations) {

        this(numberOfIterations);

        this.rand = random;
    }

    /**
     * Konstruktor der Klasse, bei dem ein Zufallsgenerator, die Anzahl der
     * auszuführenden Iterationen und das zu verwendende Optimierungsverfahren
     * angegeben wird.
     *
     * @param random              Der zu verwendende Zufallsgenerator.
     * @param numberOfIterations  Die auszuführende Anzahl der Iterationen des
     *                            Enhanced-Greedy-Verfahrens.
     * @param optVerfahren        Das zu verwendende Optimierungsverfahren.
     *
     * @throws IllegalArgumentException  Wenn die übergebene Anzahl der
     *                                   Iterationen kleiner oder gleich Null
     *                                   ist.
     */
    public IterEnhancedGreedyHeuristic(Random random, int numberOfIterations,
                                       Optimization optVerfahren) {

        if (numberOfIterations <= 0) {
            throw new IllegalArgumentException("Die Anzahl der Iterationen ist"
                                               + " kleiner oder gleich Null");
        }

        this.rand = random;
        this.numberOfIterations = numberOfIterations;
        this.optVerfahren = optVerfahren;
        this.shrinkVerfahren = new UniformShrinking(random);
    }

    /**
     * Wendet den "Iterated Enhanced Greedy Heuristic" Algorithmus auf
     * <code>familie</code> an.
     *
     * @param familie               Die Familie von Teilmengen, die zur
     *                              Erzeugung der Überdeckung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine möglichst gute bekannte Überdeckung.
     *                              Der Wert kann <CODE>null</CODE> sein, wenn
     *                              z.B. noch keine Überdeckung bekannt ist.
     *
     * @return  Die beste gefundene Überdeckung oder <CODE>null</CODE>, wenn
     *          keine vollständige Überdeckung existiert.
     *
     * @throws IllegalArgumentException  Wenn die übergebene Lösung keine
     *                                   Lösung ist.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie,
                                   ItmFamilie bekannteUeberdeckung) {
        EnhancedGreedyHeuristic   egh;
        InferiorOptimization      inferiorOpt;
        AddOneOptimization        addOneOptimization;
        AddTwoOptimization        addTwoOptimization;
        CandidatesCreation        candidatesCreation;
        CandidateRatingsCreation  candidateRatingsCreation;
        CandidateRatings          candidateRatings;
        CandidateSelection        candidateSelection;
        ItmFamilie                bestCover;
        ItmFamilie                erwFamilie;
        ItmFamilie                notwendigeItmFamilie;
        ItmFamilie                partialCover;
        ItmFamilie                optimizedCover;
        ItmFamilie                lastCover;
        HashSet                   besteTeilmengen;
        Set                       notwendigeTeilmengen;
        boolean                   unicostProblem;
        boolean                   linearCostProblem;

        if ((bekannteUeberdeckung != null)
            && (bekannteUeberdeckung.anzNichtUeberdeckt() > 0)) {

            throw new IllegalArgumentException("Die übergebene Lösung ist"
                                               + " keine Lösung");
        }

        verarbeitProblemAnz++;

        if (familie.anzNichtUeberdeckt() > 0) {
            // Es existiert keine vollständige Überdeckung.
            return null;
        }

        if (familie.groesseGesamtmenge() == 0) {
            // Es gibt kein zu überdeckendes Element. Daher ist die beste
            // Überdeckung die leere Familie.
            return familie.neueInstanz(false);
        }

        bestCover = bekannteUeberdeckung;

        // Gegebenenfalls eine um die Teilmengen von bekannteUeberdeckung
        // erweiterte Familie erzeugen.
        if (bekannteUeberdeckung == null) {
            erwFamilie = familie;
        } else {
            besteTeilmengen = bekannteUeberdeckung.toHashSet();
            besteTeilmengen.removeAll(familie.toHashSet());
            if (besteTeilmengen.size() == 0) {
                // Alle Teilmengen von bekannteUeberdeckung sind in familie
                // enthalten.
                erwFamilie = familie;
            } else {
                // Es gibt mindestens eine Teilmenge von bekannteUeberdeckung,
                // die nicht in familie enthalten ist.
                erwFamilie = (ItmFamilie) familie.clone();
                erwFamilie.teilmengenHinzufuegen(besteTeilmengen);
            }
        }

        // Ermittlung der notwendigen Teilmengen.
        notwendigeTeilmengen = erwFamilie.notwendigeTeilmengen();

        // Prüfen, ob die notwendigen Teilmengen schon eine Lösung darstellen.
        if (notwendigeTeilmengen.size() > 0) {
            notwendigeItmFamilie = erwFamilie.neueInstanz(false);
            notwendigeItmFamilie.teilmengenHinzufuegen(notwendigeTeilmengen);
            if (notwendigeItmFamilie.anzNichtUeberdeckt() == 0) {
                // Die notwendigen Teilmengen stellen schon die Lösung dar.
                return notwendigeItmFamilie;
            }
        }

        // An dieser Stelle kann die Teilmengenverwaltung der übergebenen
        // Familie fixiert werden.
//        partialCover = erwFamilie.neueInstanz(true);
        partialCover = erwFamilie.neueInstanz(false);
        partialCover.teilmengenHinzufuegen(notwendigeTeilmengen);

        // Ermitteln, um welche Art von Problem es sich handelt.
        unicostProblem = erwFamilie.teilmengenKostenGleich();
        linearCostProblem = erwFamilie.teilmengenKostenLinear();

        candidateRatingsCreation = new CandidateRatingsCreation(erwFamilie,
                                                                unicostProblem,
                                                                rand);
        candidateRatings = null;
        candidateSelection = null;

        for (int i = 0; i < numberOfIterations; i++) {

            // In der ersten Iteration wird ein vorgegebenes Verfahren
            // benutzt und es erfolgt in BestRatingSelection keine zufällige
            // Auswahl.
            if (i == 0) {
                if (linearCostProblem) {
                    candidateRatings = new ProbabilityRatings(erwFamilie);
                } else {
                    candidateRatings = new ChvatalRating();
                }
                candidateSelection = new BestRatingSelection(candidateRatings);
            } else if ((i == 1) && !linearCostProblem) {
                candidateSelection = new BestRatingSelection(candidateRatings, rand);
            } else if (linearCostProblem) {
                candidateRatings = candidateRatingsCreation.candidateRatings();
                candidateSelection = new BestRatingSelection(candidateRatings, rand);
            }

            if (!unicostProblem
                    || (candidateRatings instanceof ProbabilityRatings)) {
                candidatesCreation  = new MostCandidatesCreation();
            } else {
                candidatesCreation  = new FewestCandidatesCreation();
            }

            egh = new EnhancedGreedyHeuristic(candidatesCreation,
                                              candidateSelection, rand);
            partialCover = egh.ueberdeckung(erwFamilie, bestCover, partialCover);

            // Die Optimierung der Lösung.
            partialCover = optVerfahren.optimize(erwFamilie, partialCover,
                                                 bestCover, candidatesCreation,
                                                 candidateSelection);

            // Falls partialCover gleich gut wie oder besser als bestCover
            // ist, ersetze bestCover durch partialCover.
            if ((bestCover == null)
                    || (partialCover.kostenFamilie() <= bestCover.kostenFamilie())) {
                bestCover = (ItmFamilie) partialCover.clone();
            }

            // Jetzt wird bestCover verkleinert. Dabei werden jedoch keine
            // notwendigen Teilmengen entfernt.
            partialCover = shrinkVerfahren.partialCover(bestCover, erwFamilie,
                                                        notwendigeTeilmengen);
        }

        return bestCover;
    }

    /**
     * Wendet den "Iterated Enhanced Greedy Heuristic" Algorithmus auf
     * <code>familie</code> an.
     *
     * @param familie  Die Familie von Teilmengen, aus der eine Überdeckung
     *                 gefunden werden soll.
     *
     * @return  Die beste gefundene Überdeckung oder <CODE>null</CODE>, wenn
     *          keine vollständige Überdeckung existiert.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie) {
        return ueberdeckung(familie, null);
    }

    /**
     * Liefert einen Text, der Daten zu den bisherigen Erzeugungen der
     * Überdeckungen liefert.
     *
     * @return  Einen Text, der Daten zu den bisherigen Erzeugungen der
     *          Überdeckungen liefert.
     */
    public String statistik() {
        StringBuffer rueckgabe;

        rueckgabe = new StringBuffer();
        rueckgabe.append("Anzahl der bisher verarbeiteten Probleme: ");
        rueckgabe.append(verarbeitProblemAnz);

        return rueckgabe.toString();
    }
}

