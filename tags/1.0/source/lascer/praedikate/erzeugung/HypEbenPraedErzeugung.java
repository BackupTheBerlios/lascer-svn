/*
 * Dateiname      : HypEbenPraedErzeugung.java
 * Letzte �nderung: 23. August 2007
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
 * Implementiert die Methoden zur Erzeugung von Pr�dikaten, die Hyperebenen
 * darstellen.
 *
 * @author  Dietmar Lippold
 */
public class HypEbenPraedErzeugung implements PraedikatErzeugung {

    /**
     * Die vollst�ndigen Pr�dikate in Bezug auf die positiven Beispiele.
     */
    private ArrayList posVollPraedikate;

    /**
     * Die vollst�ndigen Pr�dikate in Bezug auf die negativen Beispiele.
     */
    private ArrayList negVollPraedikate;

    /**
     * Eine leere Liste, die f�r die korrekten Pr�dikate steht.
     */
    private ArrayList korrPraedikate = new ArrayList(1);

    /**
     * Die allgemeinen Pr�dikate.
     */
    private ArrayList algPraedikate;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param beispieldaten  Die Beispieldaten, zu denen die Pr�dikate erzeugt
     *                       werden sollen.
     * @param maxPraedAnz    Die maximale Anzahl zu erzeugender Praedikate.
     *                       Ist der Wert Null, werden alle Pr�dikate erzeugt.
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

        // Die konvexe H�lle der positiven Beispiele ermitteln.
        posUnbekWertVorhand = false;
        beispiele = beispieldaten.posBeispiele();
        punkte = new ArrayList(beispiele.size());
        for (int bspNr = 0; bspNr < beispiele.size(); bspNr++) {
            beispiel = (Beispiel) beispiele.get(bspNr);
            if (!beispiel.realWertUnbekannt()) {
                // Das Beispiel enth�lt keinen unbekannten real-Wert.
                punkte.add(HypEbenPraedikat.punkt(attributSammlung, beispiel));
            } else {
                posUnbekWertVorhand = true;
            }
        }
        huelle = new KonvexeHuelle(punkte, maxPraedAnz);
        minAbstand = Genauigkeit.maxAbstandFehler();

        // Die vollst�ndigen Pr�dikate zu den positiven Beispielen erzeugen.
        if (!posUnbekWertVorhand) {
            posVollPraedikate = vollPraedikate(beispieldaten, posBspPunkte,
                                               negBspPunkte, huelle, minAbstand);
        } else {
            posVollPraedikate = new ArrayList(1);
        }

        // Die allgemeinen Pr�dikate zu den positiven Beispielen erzeugen.
        algPraedikate = algPraedikate(beispieldaten, posBspPunkte,
                                      negBspPunkte, huelle, minAbstand);

        // Die konvexe H�lle der negativen Beispiele ermitteln.
        negUnbekWertVorhand = false;
        beispiele = beispieldaten.negBeispiele();
        punkte = new ArrayList(beispiele.size());
        for (int bspNr = 0; bspNr < beispiele.size(); bspNr++) {
            beispiel = (Beispiel) beispiele.get(bspNr);
            if (!beispiel.realWertUnbekannt()) {
                // Das Beispiel enth�lt keinen unbekannten real-Wert.
                punkte.add(HypEbenPraedikat.punkt(attributSammlung, beispiel));
            } else {
                negUnbekWertVorhand = true;
            }
        }
        huelle = new KonvexeHuelle(punkte, maxPraedAnz);
        minAbstand = Genauigkeit.maxAbstandFehler();

        // Die vollst�ndigen Pr�dikate zu den negativen Beispielen erzeugen.
        if (!negUnbekWertVorhand) {
            negVollPraedikate = vollPraedikate(beispieldaten, posBspPunkte,
                                               negBspPunkte, huelle, minAbstand);
        } else {
            negVollPraedikate = new ArrayList(1);
        }

        // Die allgemeinen Pr�dikate zu den negativen Beispielen erzeugen.
        algPraedikate.addAll(algPraedikate(beispieldaten, posBspPunkte,
                                           negBspPunkte, huelle, minAbstand));
    }

    /**
     * Liefert zu der �bergebenen konvexen H�lle eine Liste der dazu
     * erzeugten vollst�ndigen Pr�dikate.
     *
     * @param beispieldaten  Die Beipsieldaten, zu denen neue Pr�dikate
     *                       erzeugt werden.
     * @param posBspPunkte   Die Punkte zu den positiven Beispielen.
     * @param negBspPunkte   Die Punkte zu den negativen Beispielen.
     * @param huelle         Die konvexe H�lle, die zu den Punkten von
     *                       Beispielen erzeugt wurde.
     * @param minAbstand     Der minimale Abstand von der Hyperebene, ab dem
     *                       ein Punkt nicht mehr zur Hyperebene geh�rt.
     *
     * @return  Eine Liste der erzeugten vollst�ndigen Pr�dikate.
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
     * Liefert zu der �bergebenen konvexen H�lle eine Liste der dazu
     * erzeugten allgemeinen Pr�dikate.
     *
     * @param beispieldaten  Die Beipsieldaten, zu denen neue Pr�dikate
     *                       erzeugt werden.
     * @param posBspPunkte   Die Punkte zu den positiven Beispielen.
     * @param negBspPunkte   Die Punkte zu den negativen Beispielen.
     * @param huelle         Die konvexe H�lle, die zu den Punkten von
     *                       Beispielen erzeugt wurde.
     * @param minAbstand     Der minimale Abstand von der Hyperebene, ab dem
     *                       ein Punkt nicht mehr zur Hyperebene geh�rt.
     *
     * @return  Eine Liste der erzeugten allgemeinen Pr�dikate.
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
     * Liefert einen Iterator �ber eine leere Liste von Pr�dikaten, die
     * korrekt sind in Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator �ber korrekte Pr�dikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posKorrPraedIter() {
        return korrPraedikate.iterator();
    }

    /**
     * Liefert einen Iterator �ber eine leere Liste von Pr�dikaten, die
     * korrekt sind in Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator �ber korrekte Pr�dikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negKorrPraedIter() {
        return korrPraedikate.iterator();
    }

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die vollst�ndig sind
     * in Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator �ber vollst�ndige Pr�dikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posVollPraedIter() {
        return ((ArrayList) posVollPraedikate.clone()).iterator();
    }

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die vollst�ndig sind
     * in Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator �ber vollst�ndige Pr�dikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negVollPraedIter() {
        return ((ArrayList) negVollPraedikate.clone()).iterator();
    }

    /**
     * Liefert einen Iterator �ber allgemeine erzeugte Pr�dikate. Diese
     * sind weder vollst�ndig noch korrekt in Bezug auf die positiven oder
     * die negativen Beispiele. Zu einer bestimmten Menge negativer Beispiele
     * werden m�glichst viele positive Beispiele von einem Pr�dikat abgedeckt.
     *
     * @return  Einen Iterator �ber allgemeine Pr�dikate f�r positive
     *          Beispiele.
     */
    public Iterator posAlgPraedIter() {
        return ((ArrayList) algPraedikate.clone()).iterator();
    }

    /**
     * Liefert einen Iterator �ber allgemeine erzeugte Pr�dikate. Diese
     * sind weder vollst�ndig noch korrekt in Bezug auf die positiven oder
     * die negativen Beispiele. Zu einer bestimmten Menge positiver Beispiele
     * werden m�glichst viele negative Beispiele von einem Pr�dikat abgedeckt.
     *
     * @return  Einen Iterator �ber allgemeine Pr�dikate f�r negative
     *          Beispiele.
     */
    public Iterator negAlgPraedIter() {
        return ((ArrayList) algPraedikate.clone()).iterator();
    }
}

