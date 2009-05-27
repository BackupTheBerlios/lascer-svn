/*
 * Dateiname      : IterRemoveOptimization.java
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
import java.util.HashSet;
import java.util.Collection;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.heuristiken.creation.CandidatesCreation;
import mengenUeberdeckung.heuristiken.selection.CandidateSelection;

/**
 * Optimiert eine Lösung zu einem unicost-Überdeckungsproblem durch
 * wiederholte Entfernung von Teilmengen mit jeweils einer bestimmten Anzahl
 * von Indices, die diese alleine überdecken.
 *
 * @author  Dietmar Lippold
 */
public class IterRemoveOptimization implements Optimization {

    /**
     * Versucht, die Lösungsmenge zu optimieren, indem ein Mal Teilmengen, die
     * nur eine geringe Anzahl von Elementen alleine überdecken, entfernt
     * werden und die Familie anschließend wieder vervollständigt wird.
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
     */
    public ItmFamilie optimizeOneTime(Collection problemItms,
                                      ItmFamilie actualCover,
                                      ItmFamilie bestKnownSolution,
                                      CandidatesCreation candidatesCreation,
                                      CandidateSelection candidateSelection) {
        ItmFamilie     nextCover;
        HashSet        entfernTeilmengen;
        Iterator       itmIter;
        IndexTeilmenge teilmenge;
        int            maxAlleineUeberdeck, alleineUeberdeck;
        int            maxIndexAnz, indexAnz;

        // Die maximale Anzahl von Indices, die eine Teilmenge alleine
        // überdeckt, bestimmen.
        maxAlleineUeberdeck = -1;
        itmIter = actualCover.iterator();
        while (itmIter.hasNext()) {
            teilmenge = (IndexTeilmenge) itmIter.next();
            alleineUeberdeck = actualCover.anzAlleineUeberdeckt(teilmenge);
            maxAlleineUeberdeck = Math.max(maxAlleineUeberdeck, alleineUeberdeck);
        }

        // Für jede Anzahl von Indices bis zu einer Konstanten die Teilmengen
        // entfernen, die maximal so viele Indices alleine überdecken.
        // Aufhören, wenn eine bessere Überdeckung gefunden wurde.
        nextCover = actualCover;
        maxIndexAnz = Math.min(maxAlleineUeberdeck - 1, Konstanten.MAX_ENTFERN_ANZ);
        indexAnz = 1;
        while ((indexAnz <= maxIndexAnz)
               && (nextCover.kostenFamilie() >= actualCover.kostenFamilie())) {

            // Die als nächstes zu entfernenden Teilmengen bestimmen.
            entfernTeilmengen = new HashSet();
            itmIter = actualCover.iterator();
            while (itmIter.hasNext()) {
                teilmenge = (IndexTeilmenge) itmIter.next();
                alleineUeberdeck = actualCover.anzAlleineUeberdeckt(teilmenge);
                if (alleineUeberdeck <= indexAnz) {
                    entfernTeilmengen.add(teilmenge);
                }
            }

            // Die Teilmengen entfernen und die Familie vervollständigen.
            nextCover = (ItmFamilie) actualCover.clone();
            nextCover.teilmengenEntfernen(entfernTeilmengen);
            OptSupport.completeFamily(nextCover, bestKnownSolution,
                                      problemItms, entfernTeilmengen,
                                      candidatesCreation, candidateSelection);

            indexAnz++;
        }

        if (nextCover.kostenFamilie() < actualCover.kostenFamilie()) {
            return nextCover;
        } else {
            return actualCover;
        }
    }

    /**
     * Versucht, die Lösungsmenge zu optimieren, indem wiederholt Teilmengen,
     * die nur eine geringe Anzahl von Elementen alleine überdecken, entfernt
     * werden und die Familie anschließend wieder vervollständigt wird. Dies
     * geschieht so lange, bis in einem Durchgang keine Verbesserung mehr
     * erfolgt. Wenn es sich bei dem übergebenen Problem nicht um eine
     * unicost-Familie handelt, findet keine Optimierung statt.
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
     */
    public ItmFamilie optimize(ItmFamilie problem, ItmFamilie actualCover,
                               ItmFamilie bestKnownSolution,
                               CandidatesCreation candidatesCreation,
                               CandidateSelection candidateSelection) {
        ItmFamilie optimizedCover, lastCover;

        if (!problem.teilmengenKostenGleich()) {
            return actualCover;
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

