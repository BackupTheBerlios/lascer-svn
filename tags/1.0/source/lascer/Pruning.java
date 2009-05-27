/*
 * Dateiname      : Pruning.java
 * Letzte Änderung: 30. August 2007
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2007
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


package lascer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;

import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;
import lascer.konzepte.Konzept;
import lascer.konzepte.einzelne.Disjunktion;
import lascer.konzepte.einzelne.Konjunktion;

/**
 * Klasse zur Erzeugung eines reduzierten Datensatzes aufgrund der Bewertungen
 * der Beispiele.
 *
 * @author  Dietmar Lippold
 */
public class Pruning {

    /**
     * Liefert zu einer Disjunktion ein Array mit den Komplexitäten der
     * positiven Beispiele. Die Komplexität eines Beispiels ergibt sich aus
     * der Komplexität der Teilkonzepte, die das Beispiel überdecken.
     *
     * @param disjunktion  Die Disjunktion, entsprechend der die Komplexitäten
     *                     der positiven Beispiele berechnet werden sollen.
     *
     * @return  Die berechneten Bewertungen der positiven Beispiele.
     */
    private float[] disKonzBspKomplex(Disjunktion disjunktion) {
        Iterator konzIter;
        Konzept  konzept;
        float[]  komplexitaeten;
        int[]    elemente;
        float    bspKomplex;
        int      element;

        komplexitaeten = new float[disjunktion.posGesamtAnz()];
        for (int i = 0; i < komplexitaeten.length; i++) {
            komplexitaeten[i] = Float.MAX_VALUE;
        }

        konzIter = disjunktion.teilkonzepte().iterator();
        while (konzIter.hasNext()) {
            konzept = (Konzept) konzIter.next();
            bspKomplex = konzept.komplexitaet() / konzept.posRichtigAnz();

            elemente = konzept.posRichtigBsp().toArray();
            for (int i = 0; i < elemente.length; i++) {
                element = elemente[i];
                komplexitaeten[element] = Math.min(bspKomplex,
                                                   komplexitaeten[element]);
            }
        }

        return komplexitaeten;
    }

    /**
     * Liefert zu einer Konjunktion ein Array mit den Komplexitäten der
     * negativen Beispiele. Die Komplexität eines Beispiels ergibt sich aus
     * der Komplexität der Teilkonzepte, die das Beispiel ausschließen.
     *
     * @param konjunktion  Die Konjunktion, entsprechend der die Komplexitäten
     *                     der negativen Beispiele berechnet werden sollen.
     *
     * @return  Die berechneten Bewertungen der negativen Beispiele.
     */
    private float[] konKonzBspKomplex(Konjunktion konjunktion) {
        Iterator konzIter;
        Konzept  konzept;
        float[]  komplexitaeten;
        int[]    elemente;
        float    bspKomplex;
        int      element;

        komplexitaeten = new float[konjunktion.negGesamtAnz()];
        for (int i = 0; i < komplexitaeten.length; i++) {
            komplexitaeten[i] = Float.MAX_VALUE;
        }

        konzIter = konjunktion.teilkonzepte().iterator();
        while (konzIter.hasNext()) {
            konzept = (Konzept) konzIter.next();
            bspKomplex = konzept.komplexitaet() / konzept.negRichtigAnz();

            elemente = konzept.negRichtigBsp().toArray();
            for (int i = 0; i < elemente.length; i++) {
                element = elemente[i];
                komplexitaeten[element] = Math.min(bspKomplex,
                                                   komplexitaeten[element]);
            }
        }

        return komplexitaeten;
    }

    /**
     * Liefert zu einer disjunktiven Formel ein Array mit den Komplexitäten
     * der negativen Beispiele. Die Komplexität eines Beispiels ergibt sich
     * als Summe der Komplexitäten entsprechend der enthaltenen Konjunktionen.
     *
     * @param disjunktion  Die Disjunktion, entsprechend der die Komplexitäten
     *                     der negativen Beispiele berechnet werden sollen.
     *
     * @return  Die berechneten Bewertungen der negativen Beispiele.
     */
    private float[] disFormBspKomplex(Disjunktion disjunktion) {
        Iterator    konzIter;
        Konzept     konzept;
        Konjunktion konjunktion;
        float[]     einzelKomplexitaeten;
        float[]     summeKomplexitaeten;

        summeKomplexitaeten = new float[disjunktion.negGesamtAnz()];
        for (int i = 0; i < summeKomplexitaeten.length; i++) {
            summeKomplexitaeten[i] = 0;
        }

        konzIter = disjunktion.teilkonzepte().iterator();
        while (konzIter.hasNext()) {
            konzept = (Konzept) konzIter.next();
            if (konzept instanceof Konjunktion) {
                konjunktion = (Konjunktion) konzept;
                einzelKomplexitaeten = konKonzBspKomplex(konjunktion);

                for (int i = 0; i < summeKomplexitaeten.length; i++) {
                    summeKomplexitaeten[i] += einzelKomplexitaeten[i];
                }
            }
        }

        return summeKomplexitaeten;
    }

    /**
     * Liefert zu einer konjunktiven Formel ein Array mit den Komplexitäten
     * der positiven Beispiele. Die Komplexität eines Beispiels ergibt sich
     * als Summe der Komplexitäten entsprechend der enthaltenen
     * Disjunktionen.
     *
     * @param konjunktion  Die Konjunktion, entsprechend der die Komplexitäten
     *                     der positiven Beispiele berechnet werden sollen.
     *
     * @return  Die berechneten Bewertungen der positiven Beispiele.
     */
    private float[] konFormBspKomplex(Konjunktion konjunktion) {
        Iterator    konzIter;
        Konzept     konzept;
        Disjunktion disjunktion;
        float[]     einzelKomplexitaeten;
        float[]     summeKomplexitaeten;

        summeKomplexitaeten = new float[konjunktion.posGesamtAnz()];
        for (int i = 0; i < summeKomplexitaeten.length; i++) {
            summeKomplexitaeten[i] = 0;
        }

        konzIter = konjunktion.teilkonzepte().iterator();
        while (konzIter.hasNext()) {
            konzept = (Konzept) konzIter.next();
            if (konzept instanceof Disjunktion) {
                disjunktion = (Disjunktion) konzept;
                einzelKomplexitaeten = disKonzBspKomplex(disjunktion);

                for (int i = 0; i < summeKomplexitaeten.length; i++) {
                    summeKomplexitaeten[i] += einzelKomplexitaeten[i];
                }
            }
        }

        return summeKomplexitaeten;
    }

    /**
     * Liefert zu einem Datensatz und einer Formel einen neu erzeugten
     * Datensatz, aus dem einige Beispiele mit der größten Komplexität
     * entfernt wurden.
     *
     * @param bspDaten       Der Datensatz, aus dem der zu erzeugende
     *                       Datensatz abgeleitet wird.
     * @param formel         Die Formel, entsprechend der die Komplexität der
     *                       Beispiele bestimmt wird.
     * @param posEntfernAnt  Der Anteil der positiven Beispiele, die nicht
     *                       übernommen werden soll, in Prozent.
     * @param negEntfernAnt  Der Anteil der negativen Beispiele, die nicht
     *                       übernommen werden soll, in Prozent.
     *
     * @return  Den neu erzeugten Datensatz.
     */
    public Beispieldaten reduzierteDaten(Beispieldaten bspDaten, Konzept formel,
                                         float posEntfernAnt, float negEntfernAnt) {
        Beispieldaten neueDaten;
        ArrayList     posBeispiele, negBeispiele;
        Beispiel      beispiel;
        float[]       posBspBew, negBspBew;
        float         posSchwellwert, negSchwellwert;
        int           posEntfernAnz, negEntfernAnz;

        posEntfernAnz = Math.round(posEntfernAnt / 100 * bspDaten.posBspAnz());
        negEntfernAnz = Math.round(negEntfernAnt / 100 * bspDaten.negBspAnz());

        if (formel instanceof Disjunktion) {

            // Berechnung des Schwellwertes für die positiven Beispiele.
            if (posEntfernAnz == 0) {
                posSchwellwert = Float.MAX_VALUE;
            } else {
                posBspBew = disKonzBspKomplex((Disjunktion) formel);
                Arrays.sort(posBspBew);
                posSchwellwert = posBspBew[posBspBew.length - posEntfernAnz];
            }

            // Berechnung der Bewertungen der positiven Beispiele in deren
            // Reihenfolge in den Beispieldaten.
            posBspBew = disKonzBspKomplex((Disjunktion) formel);

            // Berechnung des Schwellwertes für die negativen Beispiele.
            if (negEntfernAnz == 0) {
                negSchwellwert = Float.MAX_VALUE;
            } else {
                negBspBew = disFormBspKomplex((Disjunktion) formel);
                Arrays.sort(negBspBew);
                negSchwellwert = negBspBew[negBspBew.length - negEntfernAnz];
            }

            // Berechnung der Bewertungen der negativen Beispiele in deren
            // Reihenfolge in den Beispieldaten.
            negBspBew = disFormBspKomplex((Disjunktion) formel);

        } else if (formel instanceof Konjunktion) {

            // Berechnung des Schwellwertes für die positiven Beispiele.
            if (posEntfernAnz == 0) {
                posSchwellwert = Float.MAX_VALUE;
            } else {
                posBspBew = konFormBspKomplex((Konjunktion) formel);
                Arrays.sort(posBspBew);
                posSchwellwert = posBspBew[posBspBew.length - posEntfernAnz];
            }

            // Berechnung der Bewertungen der positiven Beispiele in deren
            // Reihenfolge in den Beispieldaten.
            posBspBew = konFormBspKomplex((Konjunktion) formel);

            // Berechnung des Schwellwertes für die negativen Beispiele.
            if (negEntfernAnz == 0) {
                negSchwellwert = Float.MAX_VALUE;
            } else {
                negBspBew = konKonzBspKomplex((Konjunktion) formel);
                Arrays.sort(negBspBew);
                negSchwellwert = negBspBew[negBspBew.length - negEntfernAnz];
            }

            // Berechnung der Bewertungen der negativen Beispiele in deren
            // Reihenfolge in den Beispieldaten.
            negBspBew = konKonzBspKomplex((Konjunktion) formel);

        } else {

            throw new IllegalArgumentException("Konzept ist von umbekanntem Typ");

        }

        neueDaten = new Beispieldaten(bspDaten.name(),
                                      bspDaten.attributSammlung());

        // Aufnehmen der positiven Beispiele, deren Bewertung unter dem
        // Schwellwert liegt.
        posBeispiele = bspDaten.posBeispiele();
        for (int i = 0; i < posBeispiele.size(); i++) {
            if (posBspBew[i] < posSchwellwert) {
                beispiel = (Beispiel) posBeispiele.get(i);
                neueDaten.beispielAufnehmen(beispiel, true);
            }
        }

        // Aufnehmen der negativen Beispiele, deren Bewertung unter dem
        // Schwellwert liegt.
        negBeispiele = bspDaten.negBeispiele();
        for (int i = 0; i < negBeispiele.size(); i++) {
            if (negBspBew[i] < negSchwellwert) {
                beispiel = (Beispiel) negBeispiele.get(i);
                neueDaten.beispielAufnehmen(beispiel, false);
            }
        }

        return neueDaten;
    }
}

