/*
 * Dateiname      : OptSupport.java
 * Letzte Änderung: 26. Juli 2006
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
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.heuristiken.creation.CandidatesCreation;
import mengenUeberdeckung.heuristiken.selection.CandidateSelection;

/**
 * Klasse bietet statische Methoden zur Unterstützung der Optimierung einer
 * Überdeckung.
 *
 * @author  Dietmar Lippold
 */
public class OptSupport {

    /**
     * Ermittelt, ob die übergebene Teilmenge zu jeder der anderen
     * übergebenen Teilmengen disjunkt ist.
     *
     * @param teilmenge         Die Teilmenge, die mit den anderen Teilmengen
     *                          in Bezug auf Disjunktheit verglichen werden
     *                          soll.
     * @param andereTeilmengen  Die anderen Teilmengen, mit denen die
     *                          übergebene Teilmenge verglichen werden soll.
     *
     * @return  <CODE>true</CODE>, wenn die übergebene Teilmenge zu jeder der
     *          anderen Teilmengen disjunkt ist, sonst <CODE>false</CODE>.
     */
    public static boolean isDisjointWithAllItms(IndexTeilmenge teilmenge,
                                                Collection andereTeilmengen) {
        IndexTeilmenge nextItm;
        Iterator       itmIter;

        itmIter = andereTeilmengen.iterator();
        while (itmIter.hasNext()) {
            nextItm = (IndexTeilmenge) itmIter.next();
            if (!teilmenge.istDisjunkt(nextItm)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ermittelt die minimalen Kosten der übergebenen Familie nach Entfernung
     * redundanter Teilmengen.
     *
     * @param redundanteFamilie     Die Familie, deren minimale Kosten nach
     *                              Entfernen der redundanten Teilmengen
     *                              ermittelt werden sollen.
     * @param redundanteTeilmengen  Eine Obermenge der redundanten Teilmengen
     *                              der Familie, die entfernt werden können.
     * @param itmSelektor           Ein Verfahren zur Auswahl einer zu
     *                              entfernenden Teilmenge.
     *
     * @return  Die minimalen Kosten der übergebenen Familie nach Entfernung
     *          redundanter Teilmengen.
     */
    public static float minCostWithoutRedundant(ItmFamilie redundanteFamilie,
                                                List redundanteTeilmengen,
                                                CandidateSelection itmSelektor) {
        IndexTeilmenge entfTeilmenge;
        IndexTeilmenge wegTeilmenge;
        HashSet        redundanteTeilmengenKopie;
        LinkedList     zuEntfernendeItms;
        ArrayList      vollDisjunktItms;
        Iterator       entfIter;
        Iterator       wegIter;
        float          minKosten;
        float          neuKosten;
        int            anzNichtNotwItm;

        minKosten = redundanteFamilie.kostenFamilie();
        anzNichtNotwItm = redundanteFamilie.anzNichtNotwendigeTeilmengen();
        if (anzNichtNotwItm == 0) {
            return minKosten;
        }

        redundanteTeilmengenKopie = new HashSet(redundanteTeilmengen);
        vollDisjunktItms = new ArrayList(anzNichtNotwItm);
        zuEntfernendeItms = new LinkedList();

        // Nur die redundanten und nicht vollständig disjunkten Teilmengen in
        // die Liste der redundanten Teilmengen aufnehmen.
        entfIter = redundanteTeilmengen.iterator();
        while (entfIter.hasNext()) {
            entfTeilmenge = (IndexTeilmenge) entfIter.next();
            redundanteTeilmengenKopie.remove(entfTeilmenge);

            if (!redundanteFamilie.teilmengeIstNotwendig(entfTeilmenge)) {
                if (isDisjointWithAllItms(entfTeilmenge,
                                          redundanteTeilmengenKopie)) {
                    redundanteFamilie.teilmengeEntfernen(entfTeilmenge);
                    vollDisjunktItms.add(entfTeilmenge);
                } else {
                    zuEntfernendeItms.add(entfTeilmenge);
                }
            }

            redundanteTeilmengenKopie.add(entfTeilmenge);
        }

        if (zuEntfernendeItms.isEmpty()) {
            // Alle redundanten Teilmengen waren vollständig disjunkt.
            minKosten = redundanteFamilie.kostenFamilie();
        } else {
            if (zuEntfernendeItms.size() == 1) {
                // Es gibt nur eine redundante Teilmenge.
                entfTeilmenge = (IndexTeilmenge) zuEntfernendeItms.getFirst();
                minKosten = (redundanteFamilie.kostenFamilie()
                             - redundanteFamilie.kostenEntfernen(entfTeilmenge));
            } else if (zuEntfernendeItms.size() > Konstanten.FULL_OPT_BORDER) {
                // Es wird nur die unmittelbar schlechteste redundante
                // Teilmenge entfernt und dann die Prozedur rekursiv
                // aufgerufen.
                entfTeilmenge = itmSelektor.selectRmv(zuEntfernendeItms,
                                                      redundanteFamilie);
                zuEntfernendeItms.remove(entfTeilmenge);
                redundanteFamilie.teilmengeEntfernen(entfTeilmenge);
                minKosten = minCostWithoutRedundant(redundanteFamilie,
                                                    zuEntfernendeItms,
                                                    itmSelektor);
                redundanteFamilie.teilmengeHinzufuegen(entfTeilmenge);
            } else {
                // Es werden alle Teilmengen testweise entfernt und die
                // Prozedur wird jedes Mal rekursiv aufgerufen. Die Teilmengen
                // brauchen in zuEntfernendeItms danach nicht wieder
                // aufgenommen zu werden, da die Reihenfolge der Entfernung
                // ohne Bedeutung ist.
                wegIter = zuEntfernendeItms.iterator();
                while (wegIter.hasNext()) {
                    wegTeilmenge = (IndexTeilmenge) wegIter.next();
                    wegIter.remove();
                    redundanteFamilie.teilmengeEntfernen(wegTeilmenge);
                    neuKosten = minCostWithoutRedundant(redundanteFamilie,
                                                        zuEntfernendeItms,
                                                        itmSelektor);
                    minKosten = Math.min(minKosten, neuKosten);
                    redundanteFamilie.teilmengeHinzufuegen(wegTeilmenge);
                }
            }
        }

        redundanteFamilie.teilmengenHinzufuegen(vollDisjunktItms);
        return minKosten;
    }

    /**
     * Liefert diejenige vollständige Überdeckung mit den geringsten Kosten,
     * die durch Entfernen von redundanten Teilmengen aus der übergebenen
     * Familie erzeugt werden kann. Die übergebene Familie wird dabei
     * verändert.
     *
     * @param redundanteFamilie     Die Familie, aus der die reduzierte
     *                              Familie durch Entfernen von redundanten
     *                              Teilmengen erzeugt werden sollen.
     * @param redundanteTeilmengen  Eine Obermenge der redundanten Teilmengen
     *                              der Familie, die aus der Familie entfernt
     *                              werden können.
     * @param itmSelektor           Ein Verfahren zur Auswahl einer zu
     *                              entfernenden Teilmenge.
     *
     * @return  Die vollständige Überdeckung mit den geringsten Kosten, die
     *          aus der übergebenen Familie erzeugt werden kann.
     */
    public static ItmFamilie bestReducedFamily(ItmFamilie redundanteFamilie,
                                               Collection redundanteTeilmengen,
                                               CandidateSelection itmSelektor) {
        HashSet        redundanteTeilmengenKopie;
        LinkedList     zuEntfernendeItms;
        IndexTeilmenge entfTeilmenge;
        IndexTeilmenge wegTeilmenge;
        IndexTeilmenge besteWegTeilmenge = null;
        Iterator       entfIter;
        Iterator       wegIter;
        float          minKosten;
        float          neuKosten;
        int            anzNichtNotwItm;

        anzNichtNotwItm = redundanteFamilie.anzNichtNotwendigeTeilmengen();
        if (anzNichtNotwItm == 0) {
            return redundanteFamilie;
        }

        minKosten = redundanteFamilie.kostenFamilie();
        redundanteTeilmengenKopie = new HashSet(redundanteTeilmengen);
        zuEntfernendeItms = new LinkedList();

        // Die nicht redundanten Teilmengen aus der Liste der redundanten
        // Teilmengen entfernen und die vollständig disjunkten Teilmengen aus
        // der Familie entfernen.
        entfIter = redundanteTeilmengen.iterator();
        while (entfIter.hasNext()) {
            entfTeilmenge = (IndexTeilmenge) entfIter.next();
            redundanteTeilmengenKopie.remove(entfTeilmenge);

            if (!redundanteFamilie.teilmengeIstNotwendig(entfTeilmenge)) {
                if (isDisjointWithAllItms(entfTeilmenge,
                                          redundanteTeilmengenKopie)) {
                    redundanteFamilie.teilmengeEntfernen(entfTeilmenge);
                } else {
                    zuEntfernendeItms.add(entfTeilmenge);
                }
            }

            redundanteTeilmengenKopie.add(entfTeilmenge);
        }

        // Prüfen, ob es nach der Entfernung der vollständig disjunkten
        // Teilmengen noch weitere Teilmengen gibt.
        if (!zuEntfernendeItms.isEmpty()) {
            if (zuEntfernendeItms.size() == 1) {
                // Es kann nur eine Teilmenge entfernt werden.
                besteWegTeilmenge = (IndexTeilmenge) zuEntfernendeItms.getFirst();
            } else if (zuEntfernendeItms.size() > Konstanten.FULL_OPT_BORDER) {
                // Es wird nur die unmittelbar schlechteste redundante
                // Teilmenge ermittelt.
                besteWegTeilmenge = itmSelektor.selectRmv(zuEntfernendeItms,
                                                          redundanteFamilie);
            } else {
                // Es werden alle Teilmengen testweise entfernt und die
                // Prozedur wird jedes Mal rekursiv aufgerufen. Die Teilmengen
                // brauchen in zuEntfernendeItms danach nicht wieder
                // aufgenommen zu werden, da die Reihenfolge der Entfernung
                // ohne Bedeutung ist.
                wegIter = ((LinkedList) zuEntfernendeItms.clone()).iterator();
                while (wegIter.hasNext()) {
                    wegTeilmenge = (IndexTeilmenge) wegIter.next();
                    wegIter.remove();
                    redundanteFamilie.teilmengeEntfernen(wegTeilmenge);
                    neuKosten = minCostWithoutRedundant(redundanteFamilie,
                                                        zuEntfernendeItms,
                                                        itmSelektor);
                    if (neuKosten < minKosten) {
                        besteWegTeilmenge = wegTeilmenge;
                        minKosten = neuKosten;
                    }
                    redundanteFamilie.teilmengeHinzufuegen(wegTeilmenge);
                }
            }
        }

        // Prüfen, ob außer den evtl. vollständig disjunkten Teilmengen eine
        // redundante Teilmenge entfernt werden konnte.
        if (besteWegTeilmenge == null) {
            // Es gab keine redundante Teilmenge außer evtl. den vollständig
            // disjunkten Teilmengen. Die Familie wird also nicht weiter
            // reduziert.
            return redundanteFamilie;
        } else {
            redundanteFamilie.teilmengeEntfernen(besteWegTeilmenge);
            zuEntfernendeItms.remove(besteWegTeilmenge);
            return bestReducedFamily(redundanteFamilie, zuEntfernendeItms,
                                     itmSelektor);
        }
    }

    /**
     * Vervollständigt <CODE>teilFamilie</CODE> durch einfaches wiederholtes
     * Hinzunehmen von Teilmengen und anschließendes Entfernen unnötiger
     * Teilmengen.
     *
     * @param teilFamilie          Die zu vervollständigende Familie.
     * @param bestKnownSolution    Die bisher beste Lösung des Gesamtproblems.
     *                             Der Wert kann <CODE>null</CODE> sein, wenn
     *                             z.B. noch keine Lösung bekannt ist.
     * @param moeglicheTeilmengen  Die Teilmengen, die zur Vervollständigung
     *                             verwendet werden können.
     * @param erstNichtTeilmengen  Die Teilmengen, die nicht als erste
     *                             Teilmenge zur Teilfamilie hinzugenommen
     *                             werden sollen.
     * @param candidatesCreation   Das aktuelle Verfahren zur Erzeugung der
     *                             Kandidaten zur Aufnahme in und Entfernung
     *                             aus einer Familie.
     * @param candidateSelection   Das aktuelle Verfahren zur Auswahl der
     *                             Kandidaten zur Aufnahme in und zur
     *                             Entfernung aus einer Familie.
     */
    public static void completeFamily(ItmFamilie teilFamilie,
                                      ItmFamilie bestKnownSolution,
                                      Collection moeglicheTeilmengen,
                                      Collection erstNichtTeilmengen,
                                      CandidatesCreation candidatesCreation,
                                      CandidateSelection candidateSelection) {
        HashSet        auswahlTeilmengen;
        HashSet        addCandidates;
        Set            nichtNotwendigeTeilmengen;
        IndexTeilmenge addTeilmenge;
        boolean        ersterDurchlauf;

        ersterDurchlauf = true;
        auswahlTeilmengen = new HashSet(moeglicheTeilmengen);
        auswahlTeilmengen.removeAll(erstNichtTeilmengen);

        while (teilFamilie.anzNichtUeberdeckt() > 0) {
            addCandidates = candidatesCreation.candidatesForAdd(auswahlTeilmengen,
                                                                teilFamilie,
                                                                bestKnownSolution);

            if (ersterDurchlauf) {
                auswahlTeilmengen = new HashSet(moeglicheTeilmengen);
                ersterDurchlauf = false;
            }

            // Im ersten Durchlauf kann es sein, daß keine Kandidaten
            // vorhanden sind.
            if (!addCandidates.isEmpty()) {
                addTeilmenge = candidateSelection.selectAdd(addCandidates,
                                                            teilFamilie);
                teilFamilie.teilmengeHinzufuegen(addTeilmenge);
                auswahlTeilmengen.remove(addTeilmenge);
            }

            // Alle nicht notwendigen Teilmengen aus der Teilfamilie
            // entfernen.
            nichtNotwendigeTeilmengen = teilFamilie.nichtNotwendigeTeilmengen();
            teilFamilie.teilmengenEntfernen(nichtNotwendigeTeilmengen);
            auswahlTeilmengen.addAll(nichtNotwendigeTeilmengen);
        }
    }
}

