/*
 * Dateiname      : UniformShrinking.java
 * Letzte Änderung: 27. Juli 2006
 * Autoren        : Dietmar Lippold, Rene Berleong
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


package mengenUeberdeckung.heuristiken.shrinking;

import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Erzeugt eine Verkleinerung einer Überdeckung durch Auswahl der Teilmengen
 * mit gleicher Wahrscheinlichkeit. Der Anteil der auszuwählenden Teilmengen
 * wird vorher ebenfalls zufällig bestimmt.<P>
 *
 * Das Verfahren entspricht der Beschreibung von Elena Marchiori und Adri
 * Steenbeek ("An Iterated Heuristic Algorithm for the Set Covering Problem",
 * 1998).
 *
 * @author  Dietmar Lippold, Rene Berleong
 */
public class UniformShrinking implements FamilyShrinking {

    /**
     * Ein Zufallsgenerator.
     */
    private final Random rand;

    /**
     * Erzeugt eine neue Instanz der Klasse.
     *
     * @param random  Ein Zufallsgenerator.
     */
    public UniformShrinking(Random random) {
        this.rand = random;
    }

    /**
     * Liefert zu der übergebenen Überdeckung eine neu erzeugte echte
     * Teil-Überdeckung, die also nur eine Auswahl der Teilmengen der
     * übergebenen Überdeckung enthält.
     *
     * @param cover            Die Überdeckung, zu der eine Familie mit
     *                         weniger Teilmengen geliefert werden soll.
     * @param problem          Das Problem, zu dem die Überdeckung gehört.
     * @param fixedCandidates  Teilmengen, die aus der Überdeckung nicht
     *                         entfernt werden sollen.
     *
     * @return  Eine Familie, die eine echte Teil-Familie der übergebenen
     *          Überdeckung ist.
     */
    public ItmFamilie partialCover(ItmFamilie cover, ItmFamilie problem,
                                   Collection fixedCandidates) {
        ItmFamilie     partialCover;
        HashSet        candidates;
        Iterator       itmIter;
        IndexTeilmenge nextItm;
        float          rcvZeroProb;
        float          restoreFraction;

        // Die Wahrscheinlichkeit bestimmen, mit der keine Teilmenge der
        // übergebenen Überdeckung, bis auf die festzuhaltenden, für die
        // Erzeugung der Teil-Überdeckung übernommen wird. Diese ist abhängig
        // von der Art des Problems.
        if (problem.teilmengenKostenGleich()) {
            rcvZeroProb = Konstanten.U_RCV_ZERO_PROP;
        } else if (problem.teilmengenKostenLinear()) {
            rcvZeroProb = Konstanten.M_RCV_ZERO_PROP;
        } else {
            rcvZeroProb = Konstanten.N_RCV_ZERO_PROP;
        }

        /*
         * Jetzt wird cover verkleinert. Anstatt wie im oben genannten Paper
         * beschrieben eine neue Familie aufzubauen, werden aus einer Kopie
         * Teilmengen mit der Wahrscheinlichkeit (1 - restoreFraction)
         * entfernt.
         */
        partialCover = (ItmFamilie) cover.clone();
        candidates = partialCover.toHashSet();
        candidates.removeAll(fixedCandidates);
        do {
            if (rand.nextFloat() < rcvZeroProb) {
                restoreFraction = 0;
            } else {
                restoreFraction = Konstanten.RCV_LOW
                                  + ((Konstanten.RCV_HIGH - Konstanten.RCV_LOW)
                                     * rand.nextFloat());
            }

            itmIter = candidates.iterator();
            while (itmIter.hasNext()) {
                nextItm = (IndexTeilmenge) itmIter.next();
                if (rand.nextFloat() > restoreFraction) {
                    partialCover.teilmengeEntfernen(nextItm);
                }
            }
        } while (partialCover.anzNichtUeberdeckt() == 0);

        return partialCover;
    }
}

