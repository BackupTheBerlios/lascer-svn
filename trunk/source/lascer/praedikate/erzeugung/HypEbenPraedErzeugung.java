/*
 * Dateiname      : HypEbenPraedErzeugung.java
 * Letzte Änderung: 23. August 2007
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


package lascer.praedikate.erzeugung;

import java.util.ArrayList;
import java.util.Iterator;
import geometrischeClusterung.Punkt;
import geometrischeClusterung.Vektor;
import geometrischeClusterung.Facette;
import geometrischeClusterung.Hyperebene;
import geometrischeClusterung.Unterraum;
import geometrischeClusterung.KonvexeHuelle;
import geometrischeClusterung.Genauigkeit;

import lascer.praedikate.PraedikatErzeugung;
import lascer.praedikate.einzelne.HypEbenPraedikat;
import lascer.problemdaten.AttributSammlung;
import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;

/**
 * Implementiert die Methoden zur Erzeugung von Prädikaten, die Hyperebenen
 * darstellen.
 *
 * @author  Dietmar Lippold
 */
public class HypEbenPraedErzeugung implements PraedikatErzeugung {

    /**
     * Die vollständigen Prädikate in Bezug auf die positiven Beispiele.
     */
    private ArrayList posVollPraedikate;

    /**
     * Die vollständigen Prädikate in Bezug auf die negativen Beispiele.
     */
    private ArrayList negVollPraedikate;

    /**
     * Eine leere Liste, die für die korrekten Prädikate steht.
     */
    private ArrayList korrPraedikate = new ArrayList(1);

    /**
     * Die allgemeinen Prädikate.
     */
    private ArrayList algPraedikate;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param beispieldaten  Die Beispieldaten, zu denen die Prädikate erzeugt
     *                       werden sollen.
     * @param maxPraedAnz    Die maximale Anzahl zu erzeugender Praedikate.
     *                       Ist der Wert Null, werden alle Prädikate erzeugt.
     */
    public HypEbenPraedErzeugung(Beispieldaten beispieldaten, int maxPraedAnz) {
        AttributSammlung attributSammlung;
        ArrayList        posBspPunkte;
        ArrayList        negBspPunkte;
        KonvexeHuelle    huelle;
        ArrayList        beispiele;
        ArrayList        punkte;
        Beispiel         beispiel;
        double           minAbstand;
        boolean          posUnbekWertVorhand, negUnbekWertVorhand;

        attributSammlung = beispieldaten.attributSammlung();

        // Die Punkte der positiven und negativen Beispiele erzeugen.
        posBspPunkte = HypEbenPraedikat.punkte(attributSammlung,
                                               beispieldaten.posBeispiele());
        negBspPunkte = HypEbenPraedikat.punkte(attributSammlung,
                                               beispieldaten.negBeispiele());

        // Die konvexe Hülle der positiven Beispiele ermitteln.
        posUnbekWertVorhand = false;
        beispiele = beispieldaten.posBeispiele();
        punkte = new ArrayList(beispiele.size());
        for (int bspNr = 0; bspNr < beispiele.size(); bspNr++) {
            beispiel = (Beispiel) beispiele.get(bspNr);
            if (!beispiel.realWertUnbekannt()) {
                // Das Beispiel enthält keinen unbekannten real-Wert.
                punkte.add(HypEbenPraedikat.punkt(attributSammlung, beispiel));
            } else {
                posUnbekWertVorhand = true;
            }
        }
        huelle = new KonvexeHuelle(punkte, maxPraedAnz);
        minAbstand = Genauigkeit.maxAbstandFehler();

        // Die vollständigen Prädikate zu den positiven Beispielen erzeugen.
        if (!posUnbekWertVorhand) {
            posVollPraedikate = vollPraedikate(beispieldaten, posBspPunkte,
                                               negBspPunkte, huelle, minAbstand);
        } else {
            posVollPraedikate = new ArrayList(1);
        }

        // Die allgemeinen Prädikate zu den positiven Beispielen erzeugen.
        algPraedikate = algPraedikate(beispieldaten, posBspPunkte,
                                      negBspPunkte, huelle, minAbstand);

        // Die konvexe Hülle der negativen Beispiele ermitteln.
        negUnbekWertVorhand = false;
        beispiele = beispieldaten.negBeispiele();
        punkte = new ArrayList(beispiele.size());
        for (int bspNr = 0; bspNr < beispiele.size(); bspNr++) {
            beispiel = (Beispiel) beispiele.get(bspNr);
            if (!beispiel.realWertUnbekannt()) {
                // Das Beispiel enthält keinen unbekannten real-Wert.
                punkte.add(HypEbenPraedikat.punkt(attributSammlung, beispiel));
            } else {
                negUnbekWertVorhand = true;
            }
        }
        huelle = new KonvexeHuelle(punkte, maxPraedAnz);
        minAbstand = Genauigkeit.maxAbstandFehler();

        // Die vollständigen Prädikate zu den negativen Beispielen erzeugen.
        if (!negUnbekWertVorhand) {
            negVollPraedikate = vollPraedikate(beispieldaten, posBspPunkte,
                                               negBspPunkte, huelle, minAbstand);
        } else {
            negVollPraedikate = new ArrayList(1);
        }

        // Die allgemeinen Prädikate zu den negativen Beispielen erzeugen.
        algPraedikate.addAll(algPraedikate(beispieldaten, posBspPunkte,
                                           negBspPunkte, huelle, minAbstand));
    }

    /**
     * Liefert zu der übergebenen konvexen Hülle eine Liste der dazu
     * erzeugten vollständigen Prädikate.
     *
     * @param beispieldaten  Die Beipsieldaten, zu denen neue Prädikate
     *                       erzeugt werden.
     * @param posBspPunkte   Die Punkte zu den positiven Beispielen.
     * @param negBspPunkte   Die Punkte zu den negativen Beispielen.
     * @param huelle         Die konvexe Hülle, die zu den Punkten von
     *                       Beispielen erzeugt wurde.
     * @param minAbstand     Der minimale Abstand von der Hyperebene, ab dem
     *                       ein Punkt nicht mehr zur Hyperebene gehört.
     *
     * @return  Eine Liste der erzeugten vollständigen Prädikate.
     */
    private ArrayList vollPraedikate(Beispieldaten beispieldaten,
                                     ArrayList posBspPunkte, ArrayList negBspPunkte,
                                     KonvexeHuelle huelle, double minAbstand) {
        ArrayList  vollPraedikate;
        ArrayList  hyperebenen;
        Unterraum  unterraum;
        Hyperebene hyperebene;
        Vektor     normalenVektor;
        Punkt      enthaltenerPunkt;

        unterraum = huelle.unterraum();
        hyperebenen = huelle.umschliessendeHyperebenen();
        vollPraedikate = new ArrayList(hyperebenen.size());
        for (int hNr = 0; hNr < hyperebenen.size(); hNr++) {
            hyperebene = (Hyperebene) hyperebenen.get(hNr);
            enthaltenerPunkt = hyperebene.enthaltenerPunkt();
            normalenVektor = hyperebene.normalenVektor();
            vollPraedikate.add(new HypEbenPraedikat(beispieldaten,
                                                    posBspPunkte, negBspPunkte,
                                                    unterraum, enthaltenerPunkt,
                                                    normalenVektor, minAbstand));
        }
        return vollPraedikate;
    }

    /**
     * Liefert zu der übergebenen konvexen Hülle eine Liste der dazu
     * erzeugten allgemeinen Prädikate.
     *
     * @param beispieldaten  Die Beipsieldaten, zu denen neue Prädikate
     *                       erzeugt werden.
     * @param posBspPunkte   Die Punkte zu den positiven Beispielen.
     * @param negBspPunkte   Die Punkte zu den negativen Beispielen.
     * @param huelle         Die konvexe Hülle, die zu den Punkten von
     *                       Beispielen erzeugt wurde.
     * @param minAbstand     Der minimale Abstand von der Hyperebene, ab dem
     *                       ein Punkt nicht mehr zur Hyperebene gehört.
     *
     * @return  Eine Liste der erzeugten allgemeinen Prädikate.
     */
    private ArrayList algPraedikate(Beispieldaten beispieldaten,
                                    ArrayList posBspPunkte, ArrayList negBspPunkte,
                                    KonvexeHuelle huelle, double minAbstand) {
        ArrayList  erzPraedikate;
        ArrayList  facetten;
        Unterraum  unterraum;
        Facette    facette;
        Vektor     normalenVektor;
        Punkt      enthaltenerPunkt;

        unterraum = huelle.unterraum();
        facetten = huelle.facetten();
        erzPraedikate = new ArrayList(facetten.size());
        for (int fNr = 0; fNr < facetten.size(); fNr++) {
            facette = (Facette) facetten.get(fNr);
            if (facette.oberhalbPunkte().size() > 0) {
                enthaltenerPunkt = facette.enthaltenerPunkt();
                normalenVektor = facette.normalenVektor();
                erzPraedikate.add(new HypEbenPraedikat(beispieldaten,
                                                       posBspPunkte, negBspPunkte,
                                                       unterraum, enthaltenerPunkt,
                                                       normalenVektor, minAbstand));
            }
        }
        return erzPraedikate;
    }

    /**
     * Liefert einen Iterator über eine leere Liste von Prädikaten, die
     * korrekt sind in Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator über korrekte Prädikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posKorrPraedIter() {
        return korrPraedikate.iterator();
    }

    /**
     * Liefert einen Iterator über eine leere Liste von Prädikaten, die
     * korrekt sind in Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator über korrekte Prädikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negKorrPraedIter() {
        return korrPraedikate.iterator();
    }

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die vollständig sind
     * in Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator über vollständige Prädikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posVollPraedIter() {
        return ((ArrayList) posVollPraedikate.clone()).iterator();
    }

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die vollständig sind
     * in Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator über vollständige Prädikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negVollPraedIter() {
        return ((ArrayList) negVollPraedikate.clone()).iterator();
    }

    /**
     * Liefert einen Iterator über allgemeine erzeugte Prädikate. Diese
     * sind weder vollständig noch korrekt in Bezug auf die positiven oder
     * die negativen Beispiele. Zu einer bestimmten Menge negativer Beispiele
     * werden möglichst viele positive Beispiele von einem Prädikat abgedeckt.
     *
     * @return  Einen Iterator über allgemeine Prädikate für positive
     *          Beispiele.
     */
    public Iterator posAlgPraedIter() {
        return ((ArrayList) algPraedikate.clone()).iterator();
    }

    /**
     * Liefert einen Iterator über allgemeine erzeugte Prädikate. Diese
     * sind weder vollständig noch korrekt in Bezug auf die positiven oder
     * die negativen Beispiele. Zu einer bestimmten Menge positiver Beispiele
     * werden möglichst viele negative Beispiele von einem Prädikat abgedeckt.
     *
     * @return  Einen Iterator über allgemeine Prädikate für negative
     *          Beispiele.
     */
    public Iterator negAlgPraedIter() {
        return ((ArrayList) algPraedikate.clone()).iterator();
    }
}

