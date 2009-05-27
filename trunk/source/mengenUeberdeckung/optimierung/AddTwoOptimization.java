/*
 * Dateiname      : AddTwoOptimization.java
 * Letzte Änderung: 13. August 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
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


package mengenUeberdeckung.optimierung;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collection;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.heuristiken.creation.CandidatesCreation;
import mengenUeberdeckung.heuristiken.selection.CandidateSelection;

/**
 * Optimiert eine Lösung zu einem Überdeckungsproblem durch Hinzunahme zweier
 * Teilmengen und anschließender Entfernung der redundanten Teilmengen.
 *
 * @author  Dietmar Lippold
 */
public class AddTwoOptimization implements Optimization {

    /**
     * Versucht, die Lösungsmenge durch Hinzunahme zweier einzelner Teilmenger
     * einmal zu optimieren. Dazu werden nacheinander jeweils alle
     * Kombinationen von zwei Teilmengen zur Familie hinzugefügt und
     * ermittelt, ob durch das Entfernen einer Auswahl der dann nicht
     * notwendigen Teilmengen geringere Kosten als am Anfang entstehen.
     * Anschließend wir die Familie mit den geringsten Kosten erzeugt.<P>
     *
     * Diese Methode hat quadratischen Aufwand bezüglich der Anzahl der
     * Teilmengen in <CODE>problemItms</CODE>.
     *
     * @param problemItms         Die Teilmengen des ursprünglichen
     *                            SCP-Problems.
     * @param actualCover         Die aktuelle, zu optimierende Lösung. Diese
     *                            darf nur notwendige Teilmengen enthalten.
     * @param bestKnownSolution   Die bisher beste Lösung des Gesamtproblems.
     *                            Der Wert kann <CODE>null</CODE> sein, wenn
     *                            z.B. noch keine Lösung bekannt ist.
     * @param candidatesCreation  Das aktuelle Verfahren zur Erzeugung der
     *                            Kandidaten zur Aufnahme in und Entfernung
     *                            aus einer Familie.
     * @param candidateSelection  Das aktuelle Verfahren zur Auswahl der
     *                            Kandidaten zur Aufnahme in und zur
     *                            Entfernung aus einer Familie.
     *
     * @return  Eine Familie mit geringeren Kosten oder, wenn eine solche
     *          nicht ermittelt werden konnte, <CODE>actualCover</CODE>. Die
     *          gelieferte Familie enthält nur notwendige Teilmengen.
     *
     * @throws IllegalArgumentException  Die übergebene Familie enthält eine
     *                                   nicht notwendige Teilmenge.
     */
    public ItmFamilie optimizeOneTime(Collection problemItms,
                                      ItmFamilie actualCover,
                                      ItmFamilie bestKnownSolution,
                                      CandidatesCreation candidatesCreation,
                                      CandidateSelection candidateSelection) {
        ItmFamilie     newCover;
        HashSet        hinzuTeilmengen2;
        Set            redundanteItms;
        LinkedList     redundanteItmListe;
        IndexTeilmenge hinzuTeilmenge1;
        IndexTeilmenge hinzuTeilmenge2;
        IndexTeilmenge besteHinzuTeilmenge1 = null;
        IndexTeilmenge besteHinzuTeilmenge2 = null;
        Iterator       hinzuIter1;
        Iterator       hinzuIter2;
        float          minKosten;
        float          neuKosten;

        if (actualCover.anzNichtNotwendigeTeilmengen() > 0) {
            throw new IllegalArgumentException("Familie enthält nicht"
                                               + " notwendige Teilmengen");
        }

        /*
         * Es werden die beiden besten hinzuzunehmenden Teilmengen ermittelt.
         * Das sind die, bei deren Hinzunahme durch Reduktion eine Familie mit
         * den geringsten Kosten entsteht.
         */
        hinzuTeilmengen2 = new HashSet(problemItms);
        minKosten = actualCover.kostenFamilie();
        hinzuIter1 = problemItms.iterator();
        while (hinzuIter1.hasNext()) {
            hinzuTeilmenge1 = (IndexTeilmenge)hinzuIter1.next();
            hinzuTeilmengen2.remove(hinzuTeilmenge1);
            if (!actualCover.enthaelt(hinzuTeilmenge1)) {
                actualCover.teilmengeHinzufuegen(hinzuTeilmenge1);

                hinzuIter2 = hinzuTeilmengen2.iterator();
                while (hinzuIter2.hasNext()) {
                    hinzuTeilmenge2 = (IndexTeilmenge)hinzuIter2.next();
                    if (!actualCover.enthaelt(hinzuTeilmenge2)) {

                        /*
                         * Prüfen, ob durch das Hinzufügen der zweiten Teilmenge
                         * andere Teilmengen mit höheren Kosten redundant (nicht
                         * notwendig) werden.
                         */
                        actualCover.teilmengeHinzufuegen(hinzuTeilmenge2);
                        if (actualCover.anzNichtNotwendigeTeilmengen() > 2) {

                            redundanteItms = actualCover.nichtNotwendigeTeilmengen();
                            redundanteItms.remove(hinzuTeilmenge1);
                            redundanteItms.remove(hinzuTeilmenge2);
                            redundanteItmListe = new LinkedList(redundanteItms);
                            neuKosten = OptSupport.minCostWithoutRedundant(actualCover,
                                                                           redundanteItmListe,
                                                                           candidateSelection);
                            if (neuKosten < minKosten) {
                                besteHinzuTeilmenge1 = hinzuTeilmenge1;
                                besteHinzuTeilmenge2 = hinzuTeilmenge2;
                                minKosten = neuKosten;
                            }
                        }
                        actualCover.teilmengeEntfernen(hinzuTeilmenge2);
                    }
                }

                actualCover.teilmengeEntfernen(hinzuTeilmenge1);
            }
        }

        // Wenn durch die Hinzunahme zweier Teilmengen und anschließende
        // Reduktion eine Überdeckung mit geringeren Kosten entsteht, diese
        // liefern.
        if (besteHinzuTeilmenge1 != null) {
            newCover = (ItmFamilie)actualCover.clone();
            newCover.teilmengeHinzufuegen(besteHinzuTeilmenge1);
            newCover.teilmengeHinzufuegen(besteHinzuTeilmenge2);
            redundanteItms = newCover.nichtNotwendigeTeilmengen();
            redundanteItms.remove(besteHinzuTeilmenge1);
            redundanteItms.remove(besteHinzuTeilmenge2);
            return OptSupport.bestReducedFamily(newCover, redundanteItms,
                                                candidateSelection);
        } else {
            return actualCover;
        }
    }

    /**
     * Versucht, die Lösungsmenge durch Hinzunahme zweier einzelner Teilmenger
     * zu optimieren. Dazu werden nacheinander jeweils alle Kombinationen von
     * zwei Teilmengen zur Familie hinzugefügt und ermittelt, ob durch das
     * Entfernen einer Auswahl der dann nicht notwendigen Teilmengen geringere
     * Kosten als am Anfang entstehen. Anschließend wir die Familie mit den
     * geringsten Kosten erzeugt.<P>
     *
     * Diese Methode hat quadratischen Aufwand bezüglich der Anzahl der
     * Teilmengen in <CODE>problemItms</CODE>.
     *
     * @param problem             Das zu Grunde liegende SCP-Problem.
     * @param actualCover         Die aktuelle, zu optimierende Lösung. Diese
     *                            darf nur notwendige Teilmengen enthalten.
     * @param bestKnownSolution   Die bisher beste Lösung des Gesamtproblems.
     *                            Der Wert kann <CODE>null</CODE> sein, wenn
     *                            z.B. noch keine Lösung bekannt ist.
     * @param candidatesCreation  Das aktuelle Verfahren zur Erzeugung der
     *                            Kandidaten zur Aufnahme in und Entfernung
     *                            aus einer Familie.
     * @param candidateSelection  Das aktuelle Verfahren zur Auswahl der
     *                            Kandidaten zur Aufnahme in und zur
     *                            Entfernung aus einer Familie.
     *
     * @return  Eine Familie mit geringeren Kosten oder, wenn eine solche
     *          nicht ermittelt werden konnte, <CODE>actualCover</CODE>. Die
     *          gelieferte Familie enthält nur notwendige Teilmengen.
     *
     * @throws IllegalArgumentException  Die übergebene Familie enthält eine
     *                                   nicht notwendige Teilmenge.
     */
    public ItmFamilie optimize(ItmFamilie problem, ItmFamilie actualCover,
                               ItmFamilie bestKnownSolution,
                               CandidatesCreation candidatesCreation,
                               CandidateSelection candidateSelection) {
        ItmFamilie optimizedCover, lastCover;

        if (actualCover.anzNichtNotwendigeTeilmengen() > 0) {
            throw new IllegalArgumentException("Familie enthält nicht"
                                               + " notwendige Teilmengen");
        }

        optimizedCover = actualCover;
        do {
            lastCover = optimizedCover;
            optimizedCover = optimizeOneTime(problem.toHashSet(),
                                             lastCover, bestKnownSolution,
                                             candidatesCreation,
                                             candidateSelection);
        } while (optimizedCover.kostenFamilie() < lastCover.kostenFamilie());

        return optimizedCover;
    }
}

