/*
 * Dateiname      : ChvatalPropShrinking.java
 * Letzte Änderung: 27. Juli 2006
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


package mengenUeberdeckung.heuristiken.shrinking;

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.heuristiken.ratings.ChvatalRating;

/**
 * Erzeugt eine Verkleinerung einer Überdeckung durch Auswahl der Teilmengen
 * proportional zu ihrer Bewertung mittels des Chvatal-Rating.
 *
 * @author  Dietmar Lippold
 */
public class ChvatalPropShrinking implements FamilyShrinking {

    /**
     * Ein Zufallsgenerator.
     */
    private final Random rand;

    /**
     * Erzeugt eine neue Instanz der Klasse.
     *
     * @param random  Ein Zufallsgenerator.
     */
    public ChvatalPropShrinking(Random random) {
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
        ChvatalRating  rating;
        ItmFamilie     partialCover;
        HashSet        pcTeilmengen;
        ArrayList      candidates;
        IndexTeilmenge naechteTeilmenge;
        float[]        bewertungen;
        float          removeAllProb;
        float          bewertSumme;
        float          entfernAnteil;
        float          vergleichsWert;
        int            entfernAnzahl;

        rating = new ChvatalRating();
        entfernAnteil = Konstanten.PROP_REMOVE_PORTION;

        // Die Wahrscheinlichkeit bestimmen, mit der alle Teilmengen bis auf
        // die festzuhaltenden aus der übergebenen Überdeckung für die
        // Erzeugung der Teil-Überdeckung entfernt werden. Diese ist abhängig
        // von der Art des Problems.
        if (problem.teilmengenKostenGleich()) {
            removeAllProb = Konstanten.U_RCV_ZERO_PROP;
        } else if (problem.teilmengenKostenLinear()) {
            removeAllProb = Konstanten.M_RCV_ZERO_PROP;
        } else {
            removeAllProb = Konstanten.N_RCV_ZERO_PROP;
        }

        partialCover = (ItmFamilie) cover.clone();
        pcTeilmengen = partialCover.toHashSet();
        pcTeilmengen.removeAll(fixedCandidates);
        candidates = new ArrayList(pcTeilmengen);

        // Prüfen, ob alle entfernbaren Teilmengen entfernt werden sollen.
        if (rand.nextFloat() < removeAllProb) {
            partialCover.teilmengenEntfernen(candidates);
        } else {
            entfernAnzahl = (int) Math.round(entfernAnteil * candidates.size());

            bewertSumme = 0;
            bewertungen = rating.wRmv(candidates, cover);
            for (int i = 0; i < bewertungen.length; i++) {
                bewertSumme += bewertungen[i];
            }

            do {
                for (int i = 0; i < bewertungen.length; i++) {
                    naechteTeilmenge = (IndexTeilmenge) candidates.get(i);
                    vergleichsWert = entfernAnzahl * bewertungen[i] / bewertSumme;
                    if (rand.nextFloat() < vergleichsWert) {
                        partialCover.teilmengeEntfernen(naechteTeilmenge);
                    }
                }
            } while (partialCover.anzNichtUeberdeckt() == 0);
        }

        return partialCover;
    }
}

