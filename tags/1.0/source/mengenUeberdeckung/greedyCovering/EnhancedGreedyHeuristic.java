/*
 * Dateiname      : EnhancedGreedyHeuristic.java
 * Letzte �nderung: 23. Juli 2006
 * Autoren        : Rene Berleong, Dietmar Lippold
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


package mengenUeberdeckung.greedyCovering;

import java.util.Random;
import java.util.HashSet;

import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.heuristiken.creation.CandidatesCreation;
import mengenUeberdeckung.heuristiken.creation.FewestCandidatesCreation;
import mengenUeberdeckung.heuristiken.creation.MostCandidatesCreation;
import mengenUeberdeckung.heuristiken.selection.CandidateSelection;

/**
 * Berechnet eine L�sung eines "Set Covering Problem" auf der Grundlage
 * eines "Greedy"-Algorithmus nach einer Beschreibung von Elena Marchiori
 * und Adri Steenbeek ("An Iterated Heuristic Algorithm for the Set
 * Covering Problem", 1998).<p>
 *
 * Die Funktionen aus dem Artikel sind weitgehend durch gleichnamige Methoden
 * realisiert worden. Anstatt die Funktion <em>xcover</em> zu implementeren
 * wurde aber die Methode <code>ueberdeckungsHaeufigkeit</code> aus
 * <code>ItmFamilie</code> benutzt, f�r die Funktion <em>coverValue</em> die
 * Methode <code>anzAlleineUeberdeckt</code> aus der gleichen Klasse.
 *
 * @author  Rene Berleong, Dietmar Lippold
 */
public class EnhancedGreedyHeuristic implements UeberdeckungsOptimierung {

    /**
     * Zufallsgenerator.
     */
    private final Random rand;

    /**
     * Das Standard-Verfahren zur Erzeugung der Kandidaten zum Hinzuf�gen zu
     * und Entfernen aus der aktuellen Teill�sung.
     */
    private final CandidatesCreation stdCandidatesCreation;

    /**
     * Ein Verfahren zur Auswahl eines Kandidaten zum Hinzuf�gen zu und zum
     * Entfernen aus der aktuellen Teill�sung.
     */
    private final CandidateSelection candidateSelection;

    /**
     * Gibt an, vie viele Kandidaten (Teilmengen) nach der Erzeugung der
     * Kandidaten aufgenommen werden, bevor neue Kandidaten erzeugt werden.
     * Beim Wert Null werden maximal viele Kandidaten aufgenommen. Es
     * werden maximal so lange Kandidaten aufgenommen, bis eine Teilmenge
     * kein Element mehr neu enth�lt, das also noch in keiner anderen
     * Teilmenge der aktuellen Teill�sung enthalten ist.
     */
    private int candidateSelectNr;

    /**
     * Die Anzahl der bisher verarbeiteten Probleme.
     */
    private int verarbeitProblemAnz = 0;

    /**
     * Erzeugt eine neue Instanz der Klasse. Dazu wird die Anzahl der
     * Kandidaten, die nach jeder Erzeugung aufgenommen werden, bevor neue
     * Kandidaten erzeugt werden, �bergeben.
     *
     * @param candidatesCreation  Instanz zur Erzeugung der Kandidaten zur
     *                            Aufnahme in und Entfernung aus der aktuellen
     *                            Teill�sung. Wenn der Wert <CODE>null</CODE>
     *                            ist, wird das Verfahren erst beim Aufruf der
     *                            Methode <CODE>erzeugung</CODE> in
     *                            Abh�ngigkeit von der Art des Problems
     *                            gew�hlt.
     * @param candidateSelection  Instanz zur Auswahl der Kandidaten zur
     *                            Aufnahme in und zur Entfernung aus der
     *                            aktuellen Teill�ung.
     * @param random              Ein Zufallsgenerator.
     * @param candidateSelectNr   Die Anzahl der Kandidaten, die nach jeder
     *                            Erzeugung aufgenommen werden. Beim Wert Null
     *                            werden maximal viele Kandidaten aufgenommen.
     * @throws IllegalArgumentException  Der Wert candidateSelectNr ist
     *                                   negativ.
     */
    public EnhancedGreedyHeuristic(CandidatesCreation candidatesCreation,
                                   CandidateSelection candidateSelection,
                                   Random random, int candidateSelectNr) {

        if (candidateSelectNr < 0) {
            throw new IllegalArgumentException("candidateSelectNr darf nicht"
                + " negativ sein");
        }

        this.stdCandidatesCreation  = candidatesCreation;
        this.candidateSelection  = candidateSelection;
        this.rand = random;
        this.candidateSelectNr = candidateSelectNr;
    }

    /**
     * Erzeugt eine neue Instanz der Klasse. Nach der Erzeugung der Kandidaten
     * wird jeweils einer aufgenommen, bevor die Kandidaten neu erzeugt werden.
     *
     * @param candidatesCreation  Instanz zur Erzeugung der Kandidaten zur
     *                            Aufnahme in und Entfernung aus der aktuellen
     *                            Teill�sung. Wenn der Wert <CODE>null</CODE>
     *                            ist, wird das Verfahren erst beim Aufruf der
     *                            Methode <CODE>erzeugung</CODE> in
     *                            Abh�ngigkeit von der Art des Problems
     *                            gew�hlt.
     * @param candidateSelection  Instanz zur Auswahl der Kandidaten zur
     *                            Aufnahme in und zur Entfernung aus der
     *                            aktuellen Teill�sung.
     * @param random              Ein Zufallsgenerator.
     */
    public EnhancedGreedyHeuristic(CandidatesCreation candidatesCreation,
                                   CandidateSelection candidateSelection,
                                   Random random) {

        this(candidatesCreation, candidateSelection, random, 1);
    }

    /**
     * Erzeugt eine neue Instanz der Klasse. Nach der Erzeugung der Kandidaten
     * wird jeweils einer aufgenommen, bevor die Kandidaten neu erzeugt werden.
     * Als Verfahren zur Erzeugung der Kandidaten wird erst beim Aufruf der
     * Methode <CODE>erzeugung</CODE> die Klasse <CODE>FewestCandidatesCreation</CODE>
     * oder <CODE>MostCandidatesCreation</CODE> in Abh�ngigkeit von der
     * �bergebenenFamilie gew�hlt. Die erste wird bei einem unicost-Problem,
     * die zweite bei einem multicost-Problem gew�hlt.
     *
     * @param candidateSelection  Instanz zur Auswahl der Kandidaten zur
     *                            Aufnahme in und zur Entfernung aus der
     *                            aktuellen Teill�sung.
     * @param random              Ein Zufallsgenerator.
     */
    public EnhancedGreedyHeuristic(CandidateSelection candidateSelection,
                                   Random random) {

        this(null, candidateSelection, random, 1);
    }

    /**
     * Gibt an, ob eine Teilmenge aus der Teill�sung entfernt werden kann.
     *
     * @param partialCover  Die Teill�sung, in Bezug auf die ein Entfernen
     *                      gepr�ft wird.
     *
     * @return  <code>true</code>, wenn eine Teilmenge aus der Teill�sung
     *          entfernt werden darf, anderenfalls <code>false</code>.
     */
    private boolean removeIsOkay(ItmFamilie partialCover) {

        if (partialCover.groesseFamilie() == 0) {
            return false;
        } else if (partialCover.anzNichtNotwendigeTeilmengen() > 0) {
            return true;
        } else if (rand.nextFloat() < Konstanten.P_RMV) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Vervollst�ndigt die partielle �berdeckung <CODE>partialCover</CODE> mit
     * Hilfe der �bergebenen Teilmengen.
     *
     * @param partialCover        Die zu vervollst�ndigende Teill�sung.
     * @param bekannteUeberdeck   Eine m�glichst gute bekannte �berdeckung.
     *                            Der Wert kann <CODE>null</CODE> sein, wenn
     *                            z.B. noch keine �berdeckung bekannt ist.
     * @param problemItms         Die zur Vervollst�ndigung der �berdeckung zu
     *                            verwendenden Teilmengen.
     * @param candidatesCreation  Das Verfahren zur Erzeugung der Kandidaten
     *                            zur Aufnahem oder zur Entfernung in oder aus
     *                            der aktuellen Teill�sung.
     */
    private void generateBest(ItmFamilie partialCover,
                              ItmFamilie bekannteUeberdeck,
                              HashSet problemItms,
                              CandidatesCreation candidatesCreation) {
        FewestCandidatesCreation fallBackCreation;
        HashSet                  addCandidates, rmvCandidates;
        IndexTeilmenge           addTeilmenge, rmvTeilmenge;
        int                      candidatesToAdd, candidatesAdded;

        fallBackCreation = new FewestCandidatesCreation();
        addCandidates = (HashSet) problemItms.clone();
        while (partialCover.anzNichtUeberdeckt() > 0) {
            // W�hle eine Teilmenge und f�ge sie zu partialCover hinzu.
            if (addCandidates.size() == 0) {
                addCandidates = fallBackCreation.candidatesForAdd(problemItms,
                                                                  partialCover,
                                                                  bekannteUeberdeck);
            } else {
                addCandidates = candidatesCreation.candidatesForAdd(addCandidates,
                                                                    partialCover,
                                                                    bekannteUeberdeck);
                if (addCandidates.size() == 0) {
                    addCandidates = fallBackCreation.candidatesForAdd(problemItms,
                                                                      partialCover,
                                                                      bekannteUeberdeck);
                }
            }

            // Ermitteln, wie viele Kandidaten maximal zu partialCover
            // hinzuzuf�gen sind.
            if (candidateSelectNr == 0) {
                candidatesToAdd = addCandidates.size();
            } else {
                candidatesToAdd = Math.min(addCandidates.size(),
                                           candidateSelectNr);
            }

            // Kandidaten hinzuf�gen.
            candidatesAdded = 0;
            do {
                addTeilmenge = candidateSelection.selectAdd(addCandidates,
                                                            partialCover);

                // Teilmenge nur hinzuf�gen, wenn sie mindestens ein Element
                // neu �berdeckt.
                if (partialCover.anzAlleineUeberdeckt(addTeilmenge) > 0) {
                    partialCover.teilmengeHinzufuegen(addTeilmenge);
                    addCandidates.remove(addTeilmenge);
                    candidatesAdded++;
                }
            } while ((candidatesAdded < candidatesToAdd)
                     && (partialCover.anzAlleineUeberdeckt(addTeilmenge) > 0));

            // Entferne 0 oder mehr Teilmengen aus partialCover.
            while (removeIsOkay(partialCover)) {
                rmvCandidates = candidatesCreation.candidatesForRmv(problemItms,
                                                                    partialCover,
                                                                    bekannteUeberdeck);
                if (rmvCandidates.isEmpty()) {
                    // Wenn keine Kandidaten zur Entfernung vorhanden sind,
                    // aber eine Teilmenge entfernt werden soll, werden alle
                    // enthaltenen Teilmengen zu Kandidaten.
                    rmvCandidates = partialCover.toHashSet();
                }
                rmvTeilmenge = candidateSelection.selectRmv(rmvCandidates,
                                                            partialCover);
                partialCover.teilmengeEntfernen(rmvTeilmenge);
                addCandidates = (HashSet) problemItms.clone();
            }
        }
    }

    /**
     * Versucht, eine m�glichst gute �berdeckung zu erzeugen, wobei von der
     * Teill�sung <code>partialCover</code> ausgegangen wird.
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der �berdeckung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine m�glichst gute bekannte �berdeckung.
     *                              Der Wert kann <CODE>null</CODE> sein, wenn
     *                              z.B. noch keine �berdeckung bekannt ist.
     * @param teilloesung           Die aktuelle Teill�sung.
     *
     * @return  Die beste gefundene �berdeckung oder <CODE>null</CODE>, wenn
     *          keine vollst�ndige �berdeckung existiert.
     *
     * @throws IllegalArgumentException  Wenn die �bergebene L�sung keine
     *                                   L�sung ist.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie,
                                   ItmFamilie bekannteUeberdeckung,
                                   ItmFamilie teilloesung) {
        CandidatesCreation candidatesCreation;
        ItmFamilie         partialCover;
        HashSet            vorhandeneTeilmengen;
        boolean            unicostProblem;

        if ((bekannteUeberdeckung != null)
            && (bekannteUeberdeckung.anzNichtUeberdeckt() > 0)) {

            throw new IllegalArgumentException("Die �bergebene L�sung ist"
                                               + " keine L�sung");
        }

        verarbeitProblemAnz++;

        if (familie.anzNichtUeberdeckt() > 0) {
            // Es existiert keine vollst�ndige �berdeckung.
            return null;
        }

        if (stdCandidatesCreation == null) {
            // Das Verfahren zur Erzeugung der Kandidaten in Abh�ngigkeit
            // davon w�hlen, ob es sich um ein unicost Problem handelt.
            unicostProblem = familie.teilmengenKostenGleich();
            if (unicostProblem) {
                candidatesCreation = new FewestCandidatesCreation();
            } else {
                candidatesCreation = new MostCandidatesCreation();
            }
        } else {
            // Das dem Konstruktor �bergebene Verfahren verwenden.
            candidatesCreation = stdCandidatesCreation;
        }

        partialCover = (ItmFamilie) teilloesung.clone();

        vorhandeneTeilmengen = familie.toHashSet();
        if (bekannteUeberdeckung != null) {
            vorhandeneTeilmengen.addAll(bekannteUeberdeckung.toHashSet());
        }
        generateBest(partialCover, bekannteUeberdeckung,
                     vorhandeneTeilmengen, candidatesCreation);

        return partialCover;
    }

    /**
     * Versucht, eine m�glichst gute �berdeckung zu erzeugen.
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der �berdeckung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine m�glichst gute bekannte �berdeckung.
     *                              Der Wert kann <CODE>null</CODE> sein, wenn
     *                              z.B. noch keine �berdeckung bekannt ist.
     *
     * @return  Die beste gefundene �berdeckung oder <CODE>null</CODE>, wenn
     *          keine vollst�ndige �berdeckung existiert.
     *
     * @throws IllegalArgumentException  Wenn die �bergebene L�sung keine
     *                                   L�sung ist.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie,
                                   ItmFamilie bekannteUeberdeckung) {
        ItmFamilie neueFamilie;

        neueFamilie = familie.neueInstanz(false);
        return ueberdeckung(familie, bekannteUeberdeckung, neueFamilie);
    }

    /**
     * Versucht, eine m�glichst gute �berdeckung zu erzeugen.
     *
     * @param familie  Die Familie der Teilmengen, die zur Erzeugung der
     *                 �berdeckung verwendet werden sollen.
     *
     * @return  Die beste gefundene �berdeckung oder <CODE>null</CODE>, wenn
     *          keine vollst�ndige �berdeckung existiert.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie) {
        return ueberdeckung(familie, null);
    }

    /**
     * Liefert einen Text, der Daten zu den bisherigen Erzeugungen der
     * �berdeckungen liefert.
     *
     * @return  Einen Text, der Daten zu den bisherigen Erzeugungen der
     *          �berdeckungen liefert.
     */
    public String statistik() {
        StringBuffer rueckgabe;

        rueckgabe = new StringBuffer();
        rueckgabe.append("Anzahl der bisher verarbeiteten Probleme: ");
        rueckgabe.append(verarbeitProblemAnz);

        return rueckgabe.toString();
    }
}

