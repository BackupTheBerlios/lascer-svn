/*
 * Dateiname      : BestRatingSelection.java
 * Letzte �nderung: 23. Juli 2006
 * Autoren        : Dietmar Lippold, Rene Berleong
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


package mengenUeberdeckung.heuristiken.selection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Date;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.heuristiken.ratings.CandidateRatings;

/**
 * W�hlt die g�nstigste Teilmenge aus einer Familie von Kandidaten von
 * Teilmengen zur Aufnahme in oder zur Entfernung aus einer partiellen
 * �berdeckung entsprechend der Bewertungen der Teilmengen aus. Dies
 * entspricht dem Verfahren aus dem Paper von Elena Marchiori und Adri
 * Steenbeek (1998).
 *
 * @author  Dietmar Lippold, Rene Berleong
 */
public class BestRatingSelection implements CandidateSelection {

    /**
     * Bewertet Kandidaten zur Hinzunahme zu oder zur Entfernung aus einer
     * partiellen �berdeckung.
     */
    private CandidateRatings candidateRatings;

    /**
     * Zufallsgenerator.
     */
    private Random rand;

    /**
     * Wahrscheinlichkeit, mit der die zum Hinzuf�gen auszuw�hlende Teilmenge
     * unter allen Teilmengen, unabh�ngig von ihren Bewertungen, ausgew�hlt
     * wird.
     */
    private float addRand = Konstanten.ADD_RAND;

    /**
     * Wahrscheinlichkeit, mit der die zum Entfernen auszuw�hlende Teilmenge
     * unter allen Teilmengen, unabh�ngig von ihren Bewertungen, ausgew�hlt
     * wird.
     */
    private float rmvRand = Konstanten.RMV_RAND;

    /**
     * Erzeugt eine neue Instanz der Klasse f�r eine weniger zuf�llige
     * Auswahl. Die auszuw�hlende Teilmenge wird dabei ausschlie�lich aus den
     * am besten bzw. am schlechtesten bewerteten Teilmengen ausgew�hlt. Der
     * dabei verwendete Zufallsgenerator wird im Konstruktor erzeugt.
     *
     * @param candidateRatings  Verfahren, das die Kandidaten bewertet.
     */
    public BestRatingSelection(CandidateRatings candidateRatings) {
        this.candidateRatings = candidateRatings;

        this.rand = new Random();
        if (Konstanten.SEED == 0) {
            this.rand.setSeed((new Date()).getTime());
        } else {
            this.rand.setSeed(Konstanten.SEED);
        }

        this.addRand = 0;
        this.rmvRand = 0;
    }

    /**
     * Erzeugt eine neue Instanz der Klasse f�r eine st�rker zuf�llige
     * Auswahl. Die auszuw�hlende Teilmenge wird dabei mit einer gewissen
     * Wahrscheinlichkeit aus allen Teilmengen unabh�ngig von ihrer Bewertung
     * ausgew�hlt.
     *
     * @param candidateRatings  Verfahren, das die Kandidaten bewertet.
     * @param random            Ein Zufallsgenerator.
     */
    public BestRatingSelection(CandidateRatings candidateRatings, Random random) {
        this.candidateRatings = candidateRatings;
        this.rand = random;
    }

    /**
     * Bestimmt das Maximum der �bergebenen Werte.
     *
     * @param values  Ein Array von Werten.
     *
     * @return  Das Maximum der �bergebenen Werte.
     */
    private float maxValue(float[] values) {
        float maxValue;

        maxValue = values[0];
        for (int i = 1; i < values.length; i++) {
            maxValue = Math.max(maxValue, values[i]);
        }
        return maxValue;
    }

    /**
     * Liefert den g�nstigsten Kandidaten aus einer Familie von Teilmengen
     * zur Erg�nzung einer gegebenen partielle �berdeckung entsprechend der
     * Bewertungen der Teilmengen. Dazu wird eine Teilmenge zuf�llig aus den
     * Teilmegen mit etwa der maximalen Bewertung aller Teilmengen ausgew�hlt.
     * Mit einer geringen Wahrscheinlichkeit wird eine Teilmenge zuf�llig
     * aus allen Teilmengen ausgew�hlt.<p>
     *
     * Auf eine Begrenzung der Anzahl Kandidaten wird im Gegensatz zu dem im
     * Paper beschriebenen Verfahren verzichtet, da dies das Ergebniss nicht
     * ver�ndern w�rde.
     *
     * @param candidates    Die Teilmengen, unter denen eine ausgew�hlt werden
     *                      soll.
     * @param partialCover  Die schon vorhandene partielle �berdeckung.
     *
     * @return  Die g�nstigste Teilmenge zur Erg�nzung der �bergegebenen
     *          partielle �berdeckung
     */
    public IndexTeilmenge selectAdd(Collection candidates, ItmFamilie partialCover) {
        ArrayList      candidateList;
        ArrayList      favoriten;
        IndexTeilmenge teilmenge;
        float[]        addValues;
        float          addValueMax;
        float          cmpValue;
        int            candidateAnz;

        /*
         * Mit der Wahrscheinlichkeit Konstanten.ADD_RAND werden f�r die
         * Kandidaten keine AddValues bestimmt und die Favoriten werden
         * zuf�llig ausgesucht.
         */
        favoriten = new ArrayList();
        if ((rand.nextFloat() < addRand) || (candidates.size() == 1)) {
            favoriten.addAll(candidates);
        } else {
            /*
             * AddValues f�r die Kandidaten werden berechnet.
             */
            candidateList = new ArrayList(candidates);
            addValues = candidateRatings.wAdd(candidateList, partialCover);
            addValueMax = maxValue(addValues);
            if (addValueMax > 0) {
                cmpValue = addValueMax / (1.0f + Konstanten.CMP_INTERVAL);
            } else {
                cmpValue = addValueMax * (1.0f + Konstanten.CMP_INTERVAL);
            }

            /*
             * Die Favoriten werden bestimmt.
             */
            candidateAnz = candidateList.size();
            for (int candidateNr = 0; candidateNr < candidateAnz; candidateNr++) {
                teilmenge = (IndexTeilmenge) candidateList.get(candidateNr);
                if (addValues[candidateNr] >= cmpValue) {
                    favoriten.add(teilmenge);
                }
            }
        }

        return (IndexTeilmenge) favoriten.get(rand.nextInt(favoriten.size()));
    }

    /**
     * Liefert den g�nstigsten Kandidaten aus einer Familie von Teilmengen
     * zur Entfernung aus der partiellen �berdeckung entsprechend der
     * Bewertungen der Teilmengen. Dazu wird eine Teilmenge zuf�llig aus den
     * Teilmegen mit etwa der maximalen Bewertung aller Teilmengen ausgew�hlt.
     * Mit einer geringen Wahrscheinlichkeit wird eine Teilmenge zuf�llig aus
     * allen Teilmengen ausgew�hlt.
     *
     * @param candidates    Die Teilmengen, unter denen eine ausgew�hlt werden
     *                      soll.
     * @param partialCover  Die schon vorhandene partielle �berdeckung.
     *
     * @return  Die g�nstigste Teilmenge zur Entfernung aus der �bergegebenen
     *          partielle �berdeckung
     */
    public IndexTeilmenge selectRmv(Collection candidates, ItmFamilie partialCover) {
        ArrayList      candidateList;
        ArrayList      favoriten;
        IndexTeilmenge teilmenge;
        float[]        rmvValues;
        float          rmvValueMax;
        float          cmpValue;
        int            candidateAnz;

        /*
         * Mit der Wahrscheinlichkeit Konstanten.ADD_RAND werden f�r die
         * Kandidaten keine RmvValues bestimmt und die Favoriten werden
         * zuf�llig ausgesucht.
         */
        favoriten = new ArrayList();
        if ((rand.nextFloat() < rmvRand) || (candidates.size() == 1)) {
            favoriten.addAll(candidates);
        } else {
            /*
             * RmvValues f�r die Kandidaten werden berechnet.
             */
            candidateList = new ArrayList(candidates);
            rmvValues = candidateRatings.wRmv(candidateList, partialCover);
            rmvValueMax = maxValue(rmvValues);
            if (rmvValueMax > 0) {
                cmpValue = rmvValueMax / (1.0f + Konstanten.CMP_INTERVAL);
            } else {
                cmpValue = rmvValueMax * (1.0f + Konstanten.CMP_INTERVAL);
            }

            /*
             * Die Favoriten werden bestimmt.
             */
            candidateAnz = candidateList.size();
            for (int candidateNr = 0; candidateNr < candidateAnz; candidateNr++) {
                teilmenge = (IndexTeilmenge) candidateList.get(candidateNr);
                if (rmvValues[candidateNr] >= cmpValue) {
                    favoriten.add(teilmenge);
                }
            }
        }

        return (IndexTeilmenge) favoriten.get(rand.nextInt(favoriten.size()));
    }
}

