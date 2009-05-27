/*
 * Dateiname      : IntWertPraedErzeugung.java
 * Letzte Änderung: 20. September 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2006
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


package lascer.praedikate.erzeugung.intpraed;

import java.util.Iterator;
import java.util.HashSet;
import java.util.ArrayList;

import lascer.praedikate.PraedikatErzeugung;
import lascer.praedikate.einzelne.intpraed.IntWertPraedikat;
import lascer.problemdaten.AttributSammlung;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.attribute.IntAttribut;

/**
 * Implementiert die Methoden zur Erzeugung von Prädikaten zum Vergleich
 * eines ganzzahligen Attributs gegen einen Wert.
 *
 * @author  Dietmar Lippold
 */
public class IntWertPraedErzeugung implements PraedikatErzeugung {

    /**
     * Ermittelt, ob das übergebene Array von Attribut-Werten den unbekannten
     * Wert enthält.
     *
     * @param werte  Ein Array, von dem ermittelt wird, ob es den unbekannten
     *               Attribut-Wert enthält.
     *
     * @return  Die Angabe, ob das übergebene Array von Attribut-Werten den
     *          unbekannten Wert enthält.
     */
    public static boolean unbekWertEnthalten(int[] werte) {

        for (int i = 0; i < werte.length; i++) {
            if (werte[i] == Konstanten.UNBEKANNT_WERT) {
                return true;
            }
        }
        return false;
    }

    /**
     * Die Beispieldaten, zu denen die Prädikate erzeugt werden sollen.
     */
    private Beispieldaten beispieldaten;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param beispieldaten  Die Beispieldaten, zu denen die Prädikate erzeugt
     *                       werden sollen.
     */
    public IntWertPraedErzeugung(Beispieldaten beispieldaten) {
        this.beispieldaten = beispieldaten;
    }

    /**
     * Ermittelt, ob alle Werte des Arrays gleich sind zum übergebenen Wert.
     *
     * @param werte           Ein Array, dessen Werte mit dem einzelnen Wert
     *                        verglichen werden.
     * @param vergleichsWert  Der Wert, gegen den die Werte des Arrays
     *                        verglichen werden.
     *
     * @return  <CODE>true</CODE> genau dann, wenn alle Werte des Arrays
     *          gleich sind zum übergebenen Wert, sonst <CODE>false</CODE>.
     */
    private boolean alleWerteGleich(int[] werte, int vergleichsWert) {

        for (int i = 1; i < werte.length; i++) {
            if (werte[i] != vergleichsWert) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ermittelt, ob einer der Werte des Arrays, der nicht der unbekannte Wert
     * ist, ungleich ist zum übergebenen Wert.
     *
     * @param werte           Ein Array, dessen Werte mit dem einzelnen Wert
     *                        verglichen werden.
     * @param vergleichsWert  Der Wert, gegen den die Werte des Arrays
     *                        verglichen werden.
     *
     * @return  <CODE>true</CODE> genau dann, wenn einer der Werte des Arrays,
     *          der nicht der unbekannte Wert ist, ungleich zum übergebenen
     *          Wert ist, sonst <CODE>false</CODE>.
     */
    private boolean bekWertUngleich(int[] werte, int vergleichsWert) {

        for (int i = 1; i < werte.length; i++) {
            if ((werte[i] != vergleichsWert)
                    && (werte[i] != Konstanten.UNBEKANNT_WERT)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die vollständig sind
     * in Bezug auf die positiven oder negativen Beispiele.
     *
     * @param positiv  Gibt an, ob die erzeugten Prädikate vollständig sein
     *                 sollen in Bezug auf die positiven Beispiele. Falls
     *                 nein, sind sie vollständig in Bezug auf die negativen
     *                 Beispiele.
     *
     * @return  Einen Iterator über vollständige Prädikate in Bezug auf die
     *          positiven oder negativen Beispiele.
     */
    private Iterator vollPraedIter(boolean positiv) {
        AttributSammlung attributSammlung;
        ArrayList        praedikate;
        IntAttribut      attribut;
        int[]            inclBspWerte, exclBspWerte;
        int              ersterWert;

        attributSammlung = beispieldaten.attributSammlung();
        praedikate = new ArrayList(attributSammlung.intAttributAnz());
        for (int aNr = 0; aNr < attributSammlung.intAttributAnz(); aNr++) {
            attribut = attributSammlung.getIntAttribut(aNr);
            if (attribut != null) {
                inclBspWerte = beispieldaten.getIntWerte(attribut, positiv);
                exclBspWerte = beispieldaten.getIntWerte(attribut, !positiv);
                if (inclBspWerte.length > 0) {
                    ersterWert = inclBspWerte[0];
                    if ((ersterWert != Konstanten.UNBEKANNT_WERT)
                            && alleWerteGleich(inclBspWerte, ersterWert)
                            && bekWertUngleich(exclBspWerte, ersterWert)) {
                        praedikate.add(new IntWertPraedikat(attribut, ersterWert));
                    }
                }
            }
        }
        return praedikate.iterator();
    }

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die vollständig sind
     * in Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator über vollständige Prädikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posVollPraedIter() {
        return vollPraedIter(true);
    }

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die vollständig sind
     * in Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator über vollständige Prädikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negVollPraedIter() {
        return vollPraedIter(false);
    }

    /**
     * Liefert ein Menge mit Instanzen von <CODE>Integer</CODE>, die aus den
     * übergebenen Werten erzeugt wurden, wobei der unbekannter Wert nicht
     * aufgenommen wurde.
     *
     * @param werte  Die Werte, die als <CODE>Integer</CODE> Instanzen in die
     *               Menge aufgenommen werden.
     *
     * @return  Eine Menge mit <CODE>Integer</CODE> Instanzen der übergebenen
     *          Werte ohne den unbekannten Wert.
     */
    private HashSet werteMenge(int[] werte) {
        HashSet menge;
        boolean unbekWertVorhanden;

        unbekWertVorhanden = beispieldaten.intWertUnbekannt();
        menge = new HashSet(werte.length);
        for (int i = 0; i < werte.length; i++) {
            if (!unbekWertVorhanden || (werte[i] != Konstanten.UNBEKANNT_WERT)) {
                menge.add(new Integer(werte[i]));
            }
        }
        return menge;
    }

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die korrekt sind in
     * Bezug auf die positiven oder negativen Beispiele.
     *
     * @param positiv  Gibt an, ob die erzeugten Prädikate korrekt sein sollen
     *                 in Bezug auf die positiven Beispiele. Falls nein, sind
     *                 sie korrekt in Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator über korrekte Prädikate in Bezug auf die
     *          positiven oder negativen Beispiele.
     */
    private Iterator korrPraedIter(boolean positiv) {
        AttributSammlung attributSammlung;
        int[]            inclBspWerte, exclBspWerte;
        HashSet          inclBspWertSet, exclBspWertSet;
        ArrayList        praedikate;
        IntAttribut      attribut;

        attributSammlung = beispieldaten.attributSammlung();
        praedikate = new ArrayList(attributSammlung.intAttributAnz());
        for (int aNr = 0; aNr < attributSammlung.intAttributAnz(); aNr++) {
            attribut = attributSammlung.getIntAttribut(aNr);
            if (attribut != null) {
                exclBspWerte = beispieldaten.getIntWerte(attribut, !positiv);
                if (!unbekWertEnthalten(exclBspWerte)) {
                    inclBspWerte = beispieldaten.getIntWerte(attribut, positiv);
                    inclBspWertSet = werteMenge(inclBspWerte);
                    exclBspWertSet = werteMenge(exclBspWerte);
                    inclBspWertSet.removeAll(exclBspWertSet);
                    Iterator wertIter = inclBspWertSet.iterator();
                    while (wertIter.hasNext()) {
                        int wert = ((Integer) wertIter.next()).intValue();
                        praedikate.add(new IntWertPraedikat(attribut, wert));
                    }
                }
            }
        }
        return praedikate.iterator();
    }

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die korrekt sind in
     * Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator über korrekte Prädikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posKorrPraedIter() {
        return korrPraedIter(true);
    }

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die korrekt sind in
     * Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator über korrekte Prädikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negKorrPraedIter() {
        return korrPraedIter(false);
    }

    /**
     * Liefert einen Iterator über allgemeine erzeugte Prädikate. Diese
     * sind weder vollständig noch korrekt in Bezug auf die positiven oder
     * die negativen Beispiele.
     *
     * @return  Einen Iterator über allgemeine Prädikate.
     */
    private Iterator algPraedIter() {
        AttributSammlung attributSammlung;
        int[]            posBspWerte, negBspWerte;
        HashSet          posBspWertSet, negBspWertSet, attribWertSet;
        ArrayList        praedikate;
        IntAttribut      attribut;
        boolean          posUnbek, negUnbek;

        attributSammlung = beispieldaten.attributSammlung();
        praedikate = new ArrayList();
        for (int aNr = 0; aNr < attributSammlung.intAttributAnz(); aNr++) {
            attribut = attributSammlung.getIntAttribut(aNr);
            if (attribut != null) {
                posBspWerte = beispieldaten.getIntWerte(attribut, true);
                negBspWerte = beispieldaten.getIntWerte(attribut, false);

                posUnbek = (beispieldaten.intWertUnbekannt()
                            && unbekWertEnthalten(posBspWerte));
                negUnbek = (beispieldaten.intWertUnbekannt()
                            && unbekWertEnthalten(negBspWerte));
                posBspWertSet = werteMenge(posBspWerte);
                negBspWertSet = werteMenge(negBspWerte);
                if (((posBspWertSet.size() > 1) || posUnbek)
                    && ((negBspWertSet.size() > 1) || negUnbek)) {

                    // Die Werte ermitteln, zu denen Attribute erzeugt werden
                    // sollen.
                    if (posUnbek && negUnbek) {
                        posBspWertSet.addAll(negBspWertSet);
                        attribWertSet = posBspWertSet;
                    } else if (posUnbek) {
                        attribWertSet = negBspWertSet;
                    } else if (negUnbek) {
                        attribWertSet = posBspWertSet;
                    } else {
                        posBspWertSet.retainAll(negBspWertSet);
                        attribWertSet = posBspWertSet;
                    }

                    Iterator wertIter = attribWertSet.iterator();
                    while (wertIter.hasNext()) {
                        int wert = ((Integer) wertIter.next()).intValue();
                        praedikate.add(new IntWertPraedikat(attribut, wert));
                    }
                }
            }
        }
        return praedikate.iterator();
    }

    /**
     * Liefert einen Iterator über allgemeine erzeugte Prädikate. Diese sind
     * weder vollständig noch korrekt in Bezug auf die positiven oder die
     * negativen Beispiele. Zu einer bestimmten Menge negativer Beispiele
     * werden möglichst viele positive Beispiele von einem Prädikat abgedeckt.
     *
     * @return  Einen Iterator über allgemeine Prädikate für positive
     *          Beispiele.
     */
    public Iterator posAlgPraedIter() {
        return algPraedIter();
    }

    /**
     * Liefert einen Iterator über allgemeine erzeugte Prädikate. Diese sind
     * weder vollständig noch korrekt in Bezug auf die positiven oder die
     * negativen Beispiele. Zu einer bestimmten Menge positiver Beispiele
     * werden möglichst viele negative Beispiele von einem Prädikat abgedeckt.
     *
     * @return  Einen Iterator über allgemeine Prädikate für positive
     *          Beispiele.
     */
    public Iterator negAlgPraedIter() {
        return algPraedIter();
    }
}

