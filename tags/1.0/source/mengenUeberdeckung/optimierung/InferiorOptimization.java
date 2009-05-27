/*
 * Dateiname      : InferiorOptimization.java
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

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.heuristiken.creation.CandidatesCreation;
import mengenUeberdeckung.heuristiken.selection.CandidateSelection;

/**
 * Optimiert eine Lösung zu einem Überdeckungsproblem auf der Grundlage
 * eines Verfahrens für unicost-Probleme von Elena Marchiori und Adri
 * Steenbeek ("An Iterated Heuristic Algorithm for the Set Covering Problem",
 * 1998).
 *
 * @author  Dietmar Lippold
 */
public class InferiorOptimization implements Optimization {

    /**
     * Versucht, die Lösungsmenge durch Entfernen aller Teilmengen, die
     * durch eine bessere Teilmenge ersetzt werden können, zu optimieren.
     * Dazu wird nacheinander jeweils eine neue Teilmenge zur Familie
     * hinzugefügt und ermittelt, ob durch das Entfernen einer Auswahl der
     * dann nicht notwendigen Teilmengen geringere Kosten als am Anfang
     * entstehen. Wenn zu entfernende Teilmengen gefunden werden, wird die
     * Familie nach deren Entfernung mit dem Standard-Verfahren um neue
     * Teilmengen ergänzt.
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
        ItmFamilie     newCover       = null;
        HashSet        inferior       = new HashSet();
        HashSet        problemItms;
        Set            redundanteItms;
        HashSet        addCandidates;
        LinkedList     redundanteItmListe;
        IndexTeilmenge hinzuTeilmenge;
        IndexTeilmenge addTeilmenge;
        Iterator       hinzuIter;
        float          originalKosten;
        float          minKosten;

        if (actualCover.anzNichtNotwendigeTeilmengen() > 0) {
            throw new IllegalArgumentException("Familie enthält nicht"
                                               + " notwendige Teilmengen");
        }

        problemItms = problem.toHashSet();
        originalKosten = actualCover.kostenFamilie();

        /*
         * Es wird geprüft, ob unter den Teilmengen, die noch nicht in
         * actualCover enthalten sind, "superior" Teilmengen sind. Die
         * zugehörigen "inferior" Teilmengen werden in inferior aufgenommen.
         */
        hinzuIter = problemItms.iterator();
        while (hinzuIter.hasNext()) {
            hinzuTeilmenge = (IndexTeilmenge) hinzuIter.next();
            if (!actualCover.enthaelt(hinzuTeilmenge)) {

                /*
                 * Prüfen, ob durch das Hinzufügen der Teilmenge andere
                 * Teilmengen mit höheren Kosten redundant (nicht notwendig)
                 * werden.
                 */
                actualCover.teilmengeHinzufuegen(hinzuTeilmenge);
                if (actualCover.anzNichtNotwendigeTeilmengen() > 1) {

                    redundanteItms = actualCover.nichtNotwendigeTeilmengen();
                    redundanteItms.remove(hinzuTeilmenge);
                    redundanteItmListe = new LinkedList(redundanteItms);
                    minKosten = OptSupport.minCostWithoutRedundant(actualCover,
                                                                   redundanteItmListe,
                                                                   candidateSelection);
                    if (minKosten < originalKosten) {
                        inferior.addAll(redundanteItms);
                    }
                }
                actualCover.teilmengeEntfernen(hinzuTeilmenge);
            }
        }

        /*
         * Jetzt werden alle Elemente in inferior aus newCover entfernt und
         * durch evtl. bessere ersetzt.
         */
        if (inferior.size() > 0) {
            /*
             * Alle Teilmengen in inferior werden aus newCover entfernt.
             */
            newCover = (ItmFamilie)actualCover.clone();
            newCover.teilmengenEntfernen(inferior);

            // newCover vervollständigen.
            OptSupport.completeFamily(newCover, bestKnownSolution, problemItms,
                                      inferior, candidatesCreation, candidateSelection);
        }

        // Falls es eie neue Familie gibt diese nur liefern, wenn sie
        // geringere Kosten hat als die übergebene Familie.
        if ((newCover != null) && (newCover.kostenFamilie() < originalKosten)) {
            return newCover;
        } else {
            return actualCover;
        }
    }
}

