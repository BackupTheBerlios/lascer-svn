/*
 * Dateiname      : AddOneSolutionSet.java
 * Letzte �nderung: 26. Juli 2006
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


package mengenUeberdeckung.iteratedGreedyCovering;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.optimierung.OptSupport;
import mengenUeberdeckung.heuristiken.ratings.CandidateRatings;
import mengenUeberdeckung.heuristiken.ratings.ChvatalRating;
import mengenUeberdeckung.heuristiken.selection.CandidateSelection;
import mengenUeberdeckung.heuristiken.selection.BestRatingSelection;

/**
 * Beinhaltet eine Methode zur Erzeugung weiterer L�sungen zu einem
 * �berdeckungsproblem, indem zu einer vorgegebenen L�sung jeweils eine
 * Teilmenge hinzugenommen und redundante Teilmengen entfernt werden.
 *
 * @author  Dietmar Lippold
 */
public class AddOneSolutionSet implements SolutionSetGeneration {

    /**
     * Objekt mit Methoden zur Auswahl eines Kandidaten zum Hinzuf�gen zu und
     * zur Entfernung aus einer Familie.
     */
    private CandidateSelection candidateSelection;

    /**
     * Erzeugt eine neue Instanz der Klasse.
     *
     * @param candidateSelection  Instanz zur Auswahl der Kandidaten zur
     *                            Aufnahme in und zur Entfernung aus einer
     *                            Familie.
     */
    public AddOneSolutionSet(CandidateSelection candidateSelection) {
        this.candidateSelection = candidateSelection;
    }

    /**
     * Erzeugt eine neue Instanz der Klasse. F�r die Erzeugung des Verfahrens
     * zur Auswahl der Teilmengen wird angegeben, ob es sich um ein
     * unicost-Problem handelt.
     *
     * @param unicostProblem  Gibt an, ob Verfahren zu einem unicost-Problem
     *                        erzeugt werden sollen.
     */
    public AddOneSolutionSet(boolean unicostProblem) {
        CandidateRatings candidateRatings;

        candidateRatings = new ChvatalRating();
        this.candidateSelection = new BestRatingSelection(candidateRatings);
    }

    /**
     * Liefert zu einer vorgegebenen L�sung eines �berdeckungsproblems weitere
     * L�sungen, indem nacheinander jeweils eine neue Teilmenge zur L�sung
     * hinzugef�gt und redundante Teilmengen entfernt werden.
     *
     * @param problemItms  Die Teilmengen des urspr�nglichen SCP-Problems.
     * @param actualCover  Die aktuelle L�sung.
     *
     * @return  Eine Menge von Instanzen von <CODE>HashSet</CODE>, die weitere
     *          L�sungen zum �berdeckungsproblem darstellen. Jedes HashSet
     *          enth�lt dabei die Teilmengen einer <CODE>ItmFamilie</CODE>,
     *          die eine weitere L�sung ist.
     */
    public HashSet generatedSet(Collection problemItms, ItmFamilie actualCover) {
        HashSet        familySet;
        ItmFamilie     coverCopy;
        ItmFamilie     newCover;
        ItmFamilie     newSolution;
        Set            redundanteItms;
        IndexTeilmenge hinzuTeilmenge;
        Iterator       hinzuIter;

        familySet = new HashSet(problemItms.size());
        coverCopy = (ItmFamilie) actualCover.clone();
        hinzuIter = problemItms.iterator();
        while (hinzuIter.hasNext()) {
            hinzuTeilmenge = (IndexTeilmenge) hinzuIter.next();
            if (!coverCopy.enthaelt(hinzuTeilmenge)) {

                /*
                 * Pr�fen, ob durch das Hinzuf�gen der Teilmenge andere
                 * Teilmengen redundant (nicht notwendig) werden.
                 */
                coverCopy.teilmengeHinzufuegen(hinzuTeilmenge);
                if (coverCopy.anzNichtNotwendigeTeilmengen() > 1) {

                    newCover = (ItmFamilie) coverCopy.clone();
                    redundanteItms = newCover.nichtNotwendigeTeilmengen();
                    redundanteItms.remove(hinzuTeilmenge);
                    newSolution = OptSupport.bestReducedFamily(newCover,
                                                               redundanteItms,
                                                               candidateSelection);
                    familySet.add(newSolution.toHashSet());
                }
                coverCopy.teilmengeEntfernen(hinzuTeilmenge);
            }
        }
        return familySet;
    }
}

