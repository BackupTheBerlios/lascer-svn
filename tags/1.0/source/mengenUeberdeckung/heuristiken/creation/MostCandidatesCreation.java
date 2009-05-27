/*
 * Dateiname      : MostCandidatesCreation.java
 * Letzte Änderung: 25. Juli 2006
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


package mengenUeberdeckung.heuristiken.creation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Liefert eine möglichst große Anzahl von Teilmengen als sinnvolle Kandidaten
 * zur Aufnahme in oder zur Entfernung aus einer partiellen Überdeckung. Zur
 * Aufnahme werden alle Teilmengen geliefert, die mindestens ein Element neu
 * überdecken, das also bis dahin noch nicht überdeckt wurde. Zur Entfernung
 * werden alle Teilmengen der aktuellen Teilüberdeckung geliefert, die nicht
 * notwendig sind, die also nur Elemente enthalten, die auch von anderen
 * Teilmengen überdeckt werden.<P>
 *
 * Dieses Verfahren ist primär für multicost-Probleme geeignet.
 *
 * @author  Dietmar Lippold
 */
public class MostCandidatesCreation implements CandidatesCreation {

    /**
     * Eine Instanz zur Erzeugung von  Kandidaten, die benutzt wird, wenn
     * diese Klasse selbst keine Kandidaten erzeugen kann. Diese erzeugt
     * Kandidaten, die eine maximale Anzahl von Elementen alleine überdecken.
     */
    private FewestCandidatesCreation fallBackCreation = new FewestCandidatesCreation();

    /**
     * Liefert die möglichen Kandidaten aus einer gegebenen Familie von
     * Teilmengen zur Ergänzung einer gegebenen partiellen Überdeckung. Es
     * werden alle Teilmengen aus <code>problemItms</code> geliefert, die
     * mindestens ein Element enthalten, das noch in keiner Teilmenge aus
     * <code>partialCover</code> enthalten ist. Falls die Anzahl der
     * Teilmengen in der Familie beschränkt ist, werden nur Teilmengen
     * geliefert, die eine bestimmte Mindestanzahl von noch nicht überdeckten
     * Elementen enthalten.
     *
     * @param problemItms        Die Kandidaten der Teilmengen.
     * @param partialCover       Die schon vorhandene partielle Überdeckung.
     * @param bestKnownSolution  Die bisher beste Lösung des Gesamtproblems.
     *                           Der Wert kann <CODE>null</CODE> sein, wenn
     *                           z.B. noch keine Lösung bekannt ist.
     *
     * @return  Menge, welche die Kandidaten enthält.
     */
    public HashSet candidatesForAdd(Collection problemItms,
                                    ItmFamilie partialCover,
                                    ItmFamilie bestKnownSolution) {
        HashSet        neueTeilmengen, candidates;
        IndexTeilmenge neueTeilmenge;
        Iterator       itmIter;
        float          anzNichtUeberdeckt;
        float          besteKosten;
        int            minUeberdeckAnz, restItmAnz;
        boolean        beschraenkt;

        neueTeilmengen = new HashSet(problemItms);
        neueTeilmengen.removeAll(partialCover.toHashSet());

        minUeberdeckAnz = 1;
        beschraenkt = (partialCover.maxTeilmengenAnz() > 0);
        if (beschraenkt) {
            restItmAnz = partialCover.maxTeilmengenAnz() - partialCover.groesseFamilie();
            if (restItmAnz <= 0) {
                // Die Überscheitung der vorgegebenen maximalen Anzahl von
                // Teilmengen ist unvermeidlich.
                return fallBackCreation.candidatesForAdd(problemItms, partialCover,
                                                         bestKnownSolution);
            } else {
                anzNichtUeberdeckt = (float) partialCover.anzNichtUeberdeckt();
                minUeberdeckAnz = (int) Math.ceil(anzNichtUeberdeckt / restItmAnz);
            }
        }

        if (bestKnownSolution != null) {
            besteKosten = bestKnownSolution.kostenFamilie();
        } else {
            besteKosten = 0;
        }
        candidates = new HashSet(neueTeilmengen.size());
        itmIter = neueTeilmengen.iterator();
        while (itmIter.hasNext()) {
            neueTeilmenge = (IndexTeilmenge)itmIter.next();
            if (partialCover.ueberdecktAlleineAnz(neueTeilmenge, minUeberdeckAnz)
                && ((bestKnownSolution == null)
                    || (partialCover.kostenHinzufuegen(neueTeilmenge) < besteKosten))) {
                candidates.add(neueTeilmenge);
            }
        }

        if (candidates.isEmpty()) {
            // Es gab keine Teilmenge, die die eigentlich erforderliche
            // Anzahl von Elementen alleine überdeckt.
            return fallBackCreation.candidatesForAdd(problemItms, partialCover,
                                                     bestKnownSolution);
        } else {
            return candidates;
        }
    }

    /**
     * Liefert die nicht notwendigen Teilmengen einer partiellen Überdeckung
     * als mögliche Kandidaten zur Entfernung aus der partiellen Überdeckung.
     *
     * @param problemItms        Die Kandidaten der Teilmengen.
     * @param partialCover       Die schon vorhandene partielle Überdeckung.
     * @param bestKnownSolution  Die bisher beste Lösung des Gesamtproblems.
     *                           Der Wert kann <CODE>null</CODE> sein, wenn
     *                           z.B. noch keine Lösung bekannt ist.
     *
     * @return  Menge, welche die Kandidaten enthält.
     */
    public HashSet candidatesForRmv(Collection problemItms,
                                    ItmFamilie partialCover,
                                    ItmFamilie bestKnownSolution) {
        return new HashSet(partialCover.nichtNotwendigeTeilmengen());
    }
}

