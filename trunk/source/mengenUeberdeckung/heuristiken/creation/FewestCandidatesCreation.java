/*
 * Dateiname      : FewestCandidatesCreation.java
 * Letzte Änderung: 29. Dezember 2005
 * Autoren        : Dietmar Lippold, Rene Berleong
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2005
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
 * Liefert diejenigen Teilmengen zur Aufnahme in oder zur Entfernung aus
 * einer partiellen Überdeckung, die die größte bzw. kleinste Anzahl von
 * Elementen alleine enthalten. Das Verfahren aus dem Paper von Elena
 * Marchiori und Adri Steenbeek (1998) übernommen.<P>
 *
 * Dieses Verfahren ist nur für unicost-Probleme geeignet.
 *
 * @author  Dietmar Lippold, Rene Berleong
 */
public class FewestCandidatesCreation implements CandidatesCreation {

    /**
     * Liefert die möglichen Kandidaten aus einer gegebenen Familie von
     * Teilmengen zur Ergänzung einer gegebenen partielle Überdeckung. Die
     * Auswahl erfolgt nach dem CoverValue, d.h. nach der Anzahl der Elemente,
     * die eine Teilmenge alleine enthält. Die Kandidaten überdecken
     * mindestens ein Element, das noch von keiner Teilmenge aus
     * <code>partialCover</code> überdeckt wird. Falls es keine solche
     * Teilmenge geben sollte, wird eine leere Menge geliefert.
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
        HashSet        candidates = new HashSet(partialCover.groesseFamilie());
        IndexTeilmenge teilmenge;
        Iterator       iterator;
        int            coverValueMax, coverValue;

        /*
         * Für jede Teilmenge, die nicht in partialCover enthalten ist, wird
         * der coverValue bestimmt und die Teilmengen mit dem größten
         * coverValue werden gespeichert.
         */
        coverValueMax = 1;
        iterator = problemItms.iterator();
        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge)iterator.next();
            if (!partialCover.enthaelt(teilmenge)) {
                coverValue = partialCover.anzAlleineUeberdeckt(teilmenge);
                if (coverValue > coverValueMax) {
                    coverValueMax = coverValue;
                    candidates.clear();
                    candidates.add(teilmenge);
                } else if (coverValue == coverValueMax) {
                    candidates.add(teilmenge);
                }
            }
        }

        return candidates;
    }

    /**
     * Liefert die möglichen Kandidaten aus einer partiellen Überdeckung mit
     * Bezug auf eine gegebene Familie von Teilmengen zur Entfernung aus der
     * partiellen Überdeckung. Die Auswahl erfolgt nach dem CoverValue, d.h.
     * nach der Anzahl der Elemente, die eine Teilmenge alleine enthält.
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
        HashSet        candidates = new HashSet(partialCover.groesseFamilie());
        IndexTeilmenge teilmenge;
        Iterator       iterator;
        int            coverValueMin, coverValue;

        /*
         * Für jede Teilmenge aus partialCover wird der coverValue bestimmt
         * und die Teilmengen mit dem kleinsten coverValue werden gespeichert.
         */
        coverValueMin = partialCover.groesseGesamtmenge() + 1;
        iterator = partialCover.iterator();
        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge)iterator.next();
            coverValue = partialCover.anzAlleineUeberdeckt(teilmenge);
            if (coverValue < coverValueMin) {
                coverValueMin = coverValue;
                candidates.clear();
                candidates.add(teilmenge);
            } else if (coverValue == coverValueMin) {
                candidates.add(teilmenge);
            }
        }

        return candidates;
    }
}

