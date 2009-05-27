/*
 * Dateiname      : RealWertPraedErzeugung.java
 * Letzte �nderung: 20. September 2006
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


package lascer.praedikate.erzeugung.realpraed;

import java.util.Iterator;
import java.util.HashSet;
import java.util.ArrayList;

import lascer.praedikate.PraedikatErzeugung;
import lascer.praedikate.einzelne.realpraed.RealWertPraedikat;
import lascer.problemdaten.AttributSammlung;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.attribute.RealAttribut;

/**
 * Implementiert die Methoden zur Erzeugung von Pr�dikaten zum Vergleich
 * eines Flie�komma-Attributs gegen einen Wert.
 *
 * @author  Dietmar Lippold
 */
public class RealWertPraedErzeugung implements PraedikatErzeugung {

    /**
     * Ermittelt, ob das �bergebene Array von Attribut-Werten den unbekannten
     * Wert enth�lt.
     *
     * @param werte  Ein Array, von dem ermittelt wird, ob es den unbekannten
     *               Attribut-Wert enth�lt.
     *
     * @return  Die Angabe, ob das �bergebene Array von Attribut-Werten den
     *          unbekannten Wert enth�lt.
     */
    public static boolean unbekWertEnthalten(float[] werte) {

        for (int i = 0; i < werte.length; i++) {
            if (werte[i] == Konstanten.UNBEKANNT_WERT) {
                return true;
            }
        }
        return false;
    }

    /**
     * Die Beispieldaten, zu denen die Pr�dikate erzeugt werden sollen.
     */
    private Beispieldaten beispieldaten;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param beispieldaten  Die Beispieldaten, zu denen die Pr�dikate erzeugt
     *                       werden sollen.
     */
    public RealWertPraedErzeugung(Beispieldaten beispieldaten) {
        this.beispieldaten = beispieldaten;
    }

    /**
     * Ermittelt, ob alle Werte des Arrays gleich sind zum �bergebenen Wert.
     *
     * @param werte           Ein Array, dessen Werte mit dem einzelnen Wert
     *                        verglichen werden.
     * @param vergleichsWert  Der Wert, gegen den die Werte des Arrays
     *                        verglichen werden.
     *
     * @return  <CODE>true</CODE> genau dann, wenn alle Werte des Arrays
     *          gleich sind zum �bergebenen Wert, sonst <CODE>false</CODE>.
     */
    private boolean alleWerteGleich(float[] werte, float vergleichsWert) {

        for (int i = 1; i < werte.length; i++) {
            if (werte[i] != vergleichsWert) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ermittelt, ob einer der Werte des Arrays, der nicht der unbekannte Wert
     * ist, ungleich ist zum �bergebenen Wert.
     *
     * @param werte           Ein Array, dessen Werte mit dem einzelnen Wert
     *                        verglichen werden.
     * @param vergleichsWert  Der Wert, gegen den die Werte des Arrays
     *                        verglichen werden.
     *
     * @return  <CODE>true</CODE> genau dann, wenn einer der Werte des Arrays,
     *          der nicht der unbekannte Wert ist, ungleich zum �bergebenen
     *          Wert ist, sonst <CODE>false</CODE>.
     */
    private boolean bekWertUngleich(float[] werte, float vergleichsWert) {

        for (int i = 1; i < werte.length; i++) {
            if ((werte[i] != vergleichsWert)
                    && (werte[i] != Konstanten.UNBEKANNT_WERT)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die vollst�ndig sind
     * in Bezug auf die positiven oder negativen Beispiele.
     *
     * @param positiv  Gibt an, ob die erzeugten Pr�dikate vollst�ndig sein
     *                 sollen in Bezug auf die positiven Beispiele. Falls
     *                 nein, sind sie vollst�ndig in Bezug auf die negativen
     *                 Beispiele.
     *
     * @return  Einen Iterator �ber vollst�ndige Pr�dikate in Bezug auf die
     *          positiven oder negativen Beispiele.
     */
    private Iterator vollPraedIter(boolean positiv) {
        AttributSammlung attributSammlung;
        ArrayList        praedikate;
        RealAttribut     attribut;
        float[]          inclBspWerte, exclBspWerte;
        float            ersterWert;

        attributSammlung = beispieldaten.attributSammlung();
        praedikate = new ArrayList(attributSammlung.realAttributAnz());
        for (int aNr = 0; aNr < attributSammlung.realAttributAnz(); aNr++) {
            attribut = attributSammlung.getRealAttribut(aNr);
            if (attribut != null) {
                inclBspWerte = beispieldaten.getRealWerte(attribut, positiv);
                exclBspWerte = beispieldaten.getRealWerte(attribut, !positiv);
                if (inclBspWerte.length > 0) {
                    ersterWert = inclBspWerte[0];
                    if ((ersterWert != Konstanten.UNBEKANNT_WERT)
                            && alleWerteGleich(inclBspWerte, ersterWert)
                            && bekWertUngleich(exclBspWerte, ersterWert)) {
                        praedikate.add(new RealWertPraedikat(attribut, ersterWert));
                    }
                }
            }
        }
        return praedikate.iterator();
    }

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die vollst�ndig sind
     * in Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator �ber vollst�ndige Pr�dikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posVollPraedIter() {
        return vollPraedIter(true);
    }

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die vollst�ndig sind
     * in Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator �ber vollst�ndige Pr�dikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negVollPraedIter() {
        return vollPraedIter(false);
    }

    /**
     * Liefert ein Menge mit Instanzen von <CODE>Float</CODE>, die aus den
     * �bergebenen Werten erzeugt wurden, wobei der unbekannter Wert nicht
     * aufgenommen wurde.
     *
     * @param werte  Die Werte, die als <CODE>Float</CODE> Instanzen in die
     *               Menge aufgenommen werden.
     *
     * @return  Eine Menge mit <CODE>Float</CODE> Instanzen der �bergebenen
     *          Werte ohne den unbekannten Wert.
     */
    private HashSet werteMenge(float[] werte) {
        HashSet menge;
        boolean unbekWertVorhanden;

        unbekWertVorhanden = beispieldaten.realWertUnbekannt();
        menge = new HashSet(werte.length);
        for (int i = 0; i < werte.length; i++) {
            if (!unbekWertVorhanden || (werte[i] != Konstanten.UNBEKANNT_WERT)) {
                menge.add(new Float(werte[i]));
            }
        }
        return menge;
    }

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die korrekt sind in
     * Bezug auf die positiven oder negativen Beispiele.
     *
     * @param positiv  Gibt an, ob die erzeugten Pr�dikate korrekt sein sollen
     *                 in Bezug auf die positiven Beispiele. Falls nein, sind
     *                 sie korrekt in Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator �ber korrekte Pr�dikate in Bezug auf die
     *          positiven oder negativen Beispiele.
     */
    private Iterator korrPraedIter(boolean positiv) {
        AttributSammlung attributSammlung;
        float[]          inclBspWerte, exclBspWerte;
        HashSet          inclBspWertSet, exclBspWertSet;
        ArrayList        praedikate;
        RealAttribut     attribut;

        attributSammlung = beispieldaten.attributSammlung();
        praedikate = new ArrayList(attributSammlung.realAttributAnz());
        for (int aNr = 0; aNr < attributSammlung.realAttributAnz(); aNr++) {
            attribut = attributSammlung.getRealAttribut(aNr);
            if (attribut != null) {
                exclBspWerte = beispieldaten.getRealWerte(attribut, !positiv);
                if (!unbekWertEnthalten(exclBspWerte)) {
                    inclBspWerte = beispieldaten.getRealWerte(attribut, positiv);
                    inclBspWertSet = werteMenge(inclBspWerte);
                    exclBspWertSet = werteMenge(exclBspWerte);
                    inclBspWertSet.removeAll(exclBspWertSet);
                    Iterator wertIter = inclBspWertSet.iterator();
                    while (wertIter.hasNext()) {
                        float wert = ((Float) wertIter.next()).floatValue();
                        praedikate.add(new RealWertPraedikat(attribut, wert));
                    }
                }
            }
        }
        return praedikate.iterator();
    }

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die korrekt sind in
     * Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator �ber korrekte Pr�dikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posKorrPraedIter() {
        return korrPraedIter(true);
    }

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die korrekt sind in
     * Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator �ber korrekte Pr�dikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negKorrPraedIter() {
        return korrPraedIter(false);
    }

    /**
     * Liefert einen Iterator �ber allgemeine erzeugte Pr�dikate. Diese
     * sind weder vollst�ndig noch korrekt in Bezug auf die positiven oder
     * die negativen Beispiele.
     *
     * @return  Einen Iterator �ber allgemeine Pr�dikate.
     */
    private Iterator algPraedIter() {
        AttributSammlung attributSammlung;
        float[]          posBspWerte, negBspWerte;
        HashSet          posBspWertSet, negBspWertSet, attribWertSet;
        ArrayList        praedikate;
        RealAttribut     attribut;
        boolean          posUnbek, negUnbek;

        attributSammlung = beispieldaten.attributSammlung();
        praedikate = new ArrayList();
        for (int aNr = 0; aNr < attributSammlung.realAttributAnz(); aNr++) {
            attribut = attributSammlung.getRealAttribut(aNr);
            if (attribut != null) {
                posBspWerte = beispieldaten.getRealWerte(attribut, true);
                negBspWerte = beispieldaten.getRealWerte(attribut, false);

                posUnbek = (beispieldaten.realWertUnbekannt()
                            && unbekWertEnthalten(posBspWerte));
                negUnbek = (beispieldaten.realWertUnbekannt()
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
                        float wert = ((Float) wertIter.next()).floatValue();
                        praedikate.add(new RealWertPraedikat(attribut, wert));
                    }
                }
            }
        }
        return praedikate.iterator();
    }

    /**
     * Liefert einen Iterator �ber allgemeine erzeugte Pr�dikate. Diese sind
     * weder vollst�ndig noch korrekt in Bezug auf die positiven oder die
     * negativen Beispiele. Zu einer bestimmten Menge negativer Beispiele
     * werden m�glichst viele positive Beispiele von einem Pr�dikat abgedeckt.
     *
     * @return  Einen Iterator �ber allgemeine Pr�dikate f�r positive
     *          Beispiele.
     */
    public Iterator posAlgPraedIter() {
        return algPraedIter();
    }

    /**
     * Liefert einen Iterator �ber allgemeine erzeugte Pr�dikate. Diese sind
     * weder vollst�ndig noch korrekt in Bezug auf die positiven oder die
     * negativen Beispiele. Zu einer bestimmten Menge positiver Beispiele
     * werden m�glichst viele negative Beispiele von einem Pr�dikat abgedeckt.
     *
     * @return  Einen Iterator �ber allgemeine Pr�dikate f�r positive
     *          Beispiele.
     */
    public Iterator negAlgPraedIter() {
        return algPraedIter();
    }
}

