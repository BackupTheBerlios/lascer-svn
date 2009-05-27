/*
 * Dateiname      : SequenceOptimization.java
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

import java.util.LinkedList;
import java.util.Iterator;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.heuristiken.creation.CandidatesCreation;
import mengenUeberdeckung.heuristiken.selection.CandidateSelection;

/**
 * Optimiert eine L�sung zu einem �berdeckungsproblem, indem nacheinander zwei
 * Folgen von Optimierungsverfahren angewendet werden.
 *
 * @author  Dietmar Lippold
 */
public class SequenceOptimization implements Optimization {

    /**
     * Die Liste der anzuwendender Optimierungsverfahren in der Reihenfolge
     * ihrer Anwendung.
     */
    private LinkedList optimierVerfahren = new LinkedList();

    /**
     * F�gt der Folge anzuwendender Optimierungsverfahren einen neues
     * Verfahren, das nach den vorhandenen angewendet wird, hinzu.
     *
     * @param verfahren  Das zus�tzliche Optimierungsverfahren.
     */
    public void addOptimization(Optimization verfahren) {
        optimierVerfahren.add(verfahren);
    }

    /**
     * Liefert eine optimierte L�sung, also eine L�sung mit geringeren
     * Kosten als die �bergebene aktuelle L�sung. Wenn eine solche nicht
     * ermittelt werden kann, wird die �bergebene Familie geliefert.
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
     * @throws IllegalArgumentException  Die �bergebene Familie enth�lt eine
     *                                   nicht notwendige Teilmenge.
     */
    public ItmFamilie optimize(ItmFamilie problem, ItmFamilie actualCover,
                               ItmFamilie bestKnownSolution,
                               CandidatesCreation candidatesCreation,
                               CandidateSelection candidateSelection) {
        ItmFamilie   optimizedCover;
        Optimization optVerfahren;
        Iterator     optIter;

        optimizedCover = actualCover;

        optIter = optimierVerfahren.iterator();
        while (optIter.hasNext()) {
            optVerfahren = (Optimization) optIter.next();
            optimizedCover = optVerfahren.optimize(problem, optimizedCover,
                                                   bestKnownSolution,
                                                   candidatesCreation,
                                                   candidateSelection);
        }

        return optimizedCover;
    }
}

