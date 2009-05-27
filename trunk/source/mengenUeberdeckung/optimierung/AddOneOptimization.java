/*
 * Dateiname      : AddOneOptimization.java
 * Letzte �nderung: 13. August 2006
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


package mengenUeberdeckung.optimierung;

import java.util.Iterator;
import java.util.Set;
import java.util.LinkedList;
import java.util.Collection;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.heuristiken.creation.CandidatesCreation;
import mengenUeberdeckung.heuristiken.selection.CandidateSelection;

/**
 * Optimiert eine L�sung zu einem �berdeckungsproblem durch Hinzunahme einer
 * Teilmenge und anschlie�ender Entfernung der redundanten Teilmengen.
 *
 * @author  Dietmar Lippold
 */
public class AddOneOptimization implements Optimization {

    /**
     * Versucht, die L�sungsmenge durch Hinzunahme einer einzelnen Teilmenge
     * einmal zu optimieren. Dazu wird nacheinander jeweils eine neue
     * Teilmenge zur Familie hinzugef�gt und ermittelt, ob durch das Entfernen
     * einer Auswahl der dann nicht notwendigen Teilmengen geringere Kosten
     * als am Anfang entstehen. Anschlie�end wir die Familie mit den
     * geringsten Kosten erzeugt.
     *
     * @param problemItms         Die Teilmengen des urspr�nglichen
     *                            SCP-Problems.
     * @param actualCover         Die aktuelle, zu optimierende L�sung. Diese
     *                            darf nur notwendige Teilmengen enthalten.
     * @param bestKnownSolution   Die bisher beste L�sung des Gesamtproblems.
     *                            Der Wert kann <CODE>null</CODE> sein, wenn
     *                            z.B. noch keine L�sung bekannt ist.
     * @param candidatesCreation  Das aktuelle Verfahren zur Erzeugung der
     *                            Kandidaten zur Aufnahme in und Entfernung
     *                            aus einer Familie.
     * @param candidateSelection  Das aktuelle Verfahren zur Auswahl der
     *                            Kandidaten zur Aufnahme in und zur
     *                            Entfernung aus einer Familie.
     *
     * @return  Eine Familie mit geringeren Kosten oder, wenn eine solche
     *          nicht ermittelt werden konnte, <CODE>actualCover</CODE>. Die
     *          gelieferte Familie enth�lt nur notwendige Teilmengen.
     *
     * @throws IllegalArgumentException  Die �bergebene Familie enth�lt eine
     *                                   nicht notwendige Teilmenge.
     */
    public ItmFamilie optimizeOneTime(Collection problemItms,
                                      ItmFamilie actualCover,
                                      ItmFamilie bestKnownSolution,
                                      CandidatesCreation candidatesCreation,
                                      CandidateSelection candidateSelection) {
        ItmFamilie     newCover;
        Set            redundanteItms;
        LinkedList     redundanteItmListe;
        IndexTeilmenge hinzuTeilmenge;
        IndexTeilmenge besteHinzuTeilmenge = null;
        Iterator       hinzuIter;
        float          minKosten;
        float          neuKosten;

        if (actualCover.anzNichtNotwendigeTeilmengen() > 0) {
            throw new IllegalArgumentException("Familie enth�lt nicht"
                                               + " notwendige Teilmengen");
        }

        /*
         * Es wird die beste hinzuzunehmende Teilmenge ermittelt. Das ist
         * die, bei deren Hinzunahme durch Reduktion eine Familie mit den
         * geringsten Kosten entsteht.
         */
        minKosten = actualCover.kostenFamilie();
        hinzuIter = problemItms.iterator();
        while (hinzuIter.hasNext()) {
            hinzuTeilmenge = (IndexTeilmenge) hinzuIter.next();
            if (!actualCover.enthaelt(hinzuTeilmenge)) {

                /*
                 * Pr�fen, ob durch das Hinzuf�gen der Teilmenge andere
                 * Teilmengen mit h�heren Kosten redundant (nicht notwendig)
                 * werden.
                 */
                actualCover.teilmengeHinzufuegen(hinzuTeilmenge);
                if (actualCover.anzNichtNotwendigeTeilmengen() > 1) {

                    redundanteItms = actualCover.nichtNotwendigeTeilmengen();
                    redundanteItms.remove(hinzuTeilmenge);
                    redundanteItmListe = new LinkedList(redundanteItms);
                    neuKosten = OptSupport.minCostWithoutRedundant(actualCover,
                                                                   redundanteItmListe,
                                                                   candidateSelection);
                    if (neuKosten < minKosten) {
                        besteHinzuTeilmenge = hinzuTeilmenge;
                        minKosten = neuKosten;
                    }
                }
                actualCover.teilmengeEntfernen(hinzuTeilmenge);
            }
        }

        // Wenn durch die Hinzunahme einer Teilmenge und anschlie�ende
        // Reduktion eine �berdeckung mit geringeren Kosten entsteht, diese
        // liefern.
        if (besteHinzuTeilmenge != null) {
            newCover = (ItmFamilie) actualCover.clone();
            newCover.teilmengeHinzufuegen(besteHinzuTeilmenge);
            redundanteItms = newCover.nichtNotwendigeTeilmengen();
            redundanteItms.remove(besteHinzuTeilmenge);
            return OptSupport.bestReducedFamily(newCover, redundanteItms,
                                                candidateSelection);
        } else {
            return actualCover;
        }
    }

    /**
     * Versucht, die L�sungsmenge durch Hinzunahme einer einzelnen Teilmenge
     * zu optimieren. Dazu wird nacheinander jeweils eine neue Teilmenge zur
     * Familie hinzugef�gt und ermittelt, ob durch das Entfernen einer Auswahl
     * der dann nicht notwendigen Teilmengen geringere Kosten als am Anfang
     * entstehen. Anschlie�end wir die Familie mit den geringsten Kosten
     * erzeugt.
     *
     * @param problem             Das zu Grunde liegende SCP-Problem.
     * @param actualCover         Die aktuelle, zu optimierende L�sung. Diese
     *                            darf nur notwendige Teilmengen enthalten.
     * @param bestKnownSolution   Die bisher beste L�sung des Gesamtproblems.
     *                            Der Wert kann <CODE>null</CODE> sein, wenn
     *                            z.B. noch keine L�sung bekannt ist.
     * @param candidatesCreation  Das aktuelle Verfahren zur Erzeugung der
     *                            Kandidaten zur Aufnahme in und Entfernung
     *                            aus einer Familie.
     * @param candidateSelection  Das aktuelle Verfahren zur Auswahl der
     *                            Kandidaten zur Aufnahme in und zur
     *                            Entfernung aus einer Familie.
     *
     * @return  Eine Familie mit geringeren Kosten oder, wenn eine solche
     *          nicht ermittelt werden konnte, <CODE>actualCover</CODE>. Die
     *          gelieferte Familie enth�lt nur notwendige Teilmengen.
     *
     * @throws IllegalArgumentException  Die aktuelle L�sung enth�lt eine
     *                                   nicht notwendige Teilmenge.
     */
    public ItmFamilie optimize(ItmFamilie problem, ItmFamilie actualCover,
                               ItmFamilie bestKnownSolution,
                               CandidatesCreation candidatesCreation,
                               CandidateSelection candidateSelection) {
        ItmFamilie optimizedCover, lastCover;

        if (actualCover.anzNichtNotwendigeTeilmengen() > 0) {
            throw new IllegalArgumentException("Familie enth�lt nicht"
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

