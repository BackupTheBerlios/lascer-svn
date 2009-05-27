/*
 * Dateiname      : NomWertPraedErzeugung.java
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


package lascer.praedikate.erzeugung;

import java.util.Iterator;
import java.util.ArrayList;

import lascer.praedikate.PraedikatErzeugung;
import lascer.praedikate.einzelne.NomWertPraedikat;
import lascer.praedikate.einzelne.BoolWertPraedikat;
import lascer.problemdaten.AttributSammlung;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.attribute.NominalAttribut;

/**
 * Implementiert die Methoden zur Erzeugung von Pr�dikaten zum Vergleich
 * eines nominalen Attributs gegen einen Wert.
 *
 * @author  Dietmar Lippold
 */
public class NomWertPraedErzeugung implements PraedikatErzeugung {

    /**
     * Die Beispieldaten, zu denen die Pr�dikate erzeugt werden sollen.
     */
    private Beispieldaten beispieldaten;

    /**
     * Gibt die Nummer des Wertes der boolschen Attribute an (Null oder Eins),
     * nur f�r den Pr�dikate, jedoch auch negierte, d.h. invertierte Literale,
     * erzeugt werden sollen. Wenn der Wert negativ ist, werden f�r beide
     * Werte Pr�dikate erzeugt, jedoch keine negierten.
     */
    private int boolWertPraedNummer;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param beispieldaten        Die Beispieldaten, zu denen die Pr�dikate
     *                             erzeugt werden sollen.
     * @param boolWertPraedNummer  Gibt die Nummer des Wertes der boolschen
     *                             Attribute an (Null oder Eins), nur f�r den
     *                             Pr�dikate, auch negierte, erzeugt werden
     *                             sollen. Wenn der Wert negativ ist, werden
     *                             f�r beide Werte Pr�dikate erzeugt, jedoch
     *                             keine negierten.
     *
     * @throws IllegalArgumentException  Wenn <CODE>boolWertPraedNummer</CODE>
     *                                   gr��er als Eins ist.
     */
    public NomWertPraedErzeugung(Beispieldaten beispieldaten,
                                 int boolWertPraedNummer) {
        this.beispieldaten = beispieldaten;
        this.boolWertPraedNummer = boolWertPraedNummer;

        if (boolWertPraedNummer > 1) {
            throw new IllegalArgumentException("boolWertPraedNummer unzul�ssig: "
                                               + boolWertPraedNummer);
        }
    }

    /**
     * Ermittelt, ob in einem Array von Werten nur ein bestimmter Wert
     * vorkommt, also alle Werte gleich diesem Wert sind.
     *
     * @param werte  Ein Array, dessen Werte mit dem Testwert verglichen
     *               werden.
     * @param wert   Der Wert, gegen den die Werte aus dem Array verglichen
     *               werden.
     *
     * @return  <CODE>true</CODE> wenn alle Werte im Array gleich zum Testwert
     *          sind, sonst <CODE>false</CODE>.
     */
    private boolean nurEnthaltenWert(String[] werte, String wert) {

        for (int i = 0; i < werte.length; i++) {
            if (!werte[i].equals(wert)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ermittelt, ob der �bergebene Wert im �bergebenen Array vorkommt.
     *
     * @param werte  Das Array, von dem gepr�ft wird, ob es den �bergebenen
     *               Wert enth�lt.
     * @param wert   Der Wert, von dem gepr�ft wird, ob er im �bergebenen
     *               Array enthalten ist.
     *
     * @return  <CODE>true</CODE>, wenn der �bergebene Wert im �bergebenen
     *          Array enthalten ist, anderenfalls <CODE>false</CODE>.
     */
    private boolean wertEnthalten(String[] werte, String wert) {

        for (int i = 0; i < werte.length; i++) {
            if (werte[i].equals(wert)) {
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
        String[]         moeglichWerte;
        String[]         inclBspWerte, exclBspWerte;
        ArrayList        praedikate;
        NominalAttribut  attribut;
        String           wert;

        attributSammlung = beispieldaten.attributSammlung();
        praedikate = new ArrayList(attributSammlung.nominalAttributAnz());
        for (int aNr = 0; aNr < attributSammlung.nominalAttributAnz(); aNr++) {
            attribut = attributSammlung.getNominalAttribut(aNr);
            if (attribut != null) {
                moeglichWerte = attribut.werte();
                inclBspWerte = beispieldaten.getNominalWerte(attribut, positiv);
                exclBspWerte = beispieldaten.getNominalWerte(attribut, !positiv);

                if (!beispieldaten.nominalWertUnbekannt()
                        || !wertEnthalten(inclBspWerte, Konstanten.UNBEKANNT_WERT)) {
                    if (moeglichWerte.length == 2) {
                        // Es handelt sich um ein boolsches Attribut.
                        if (boolWertPraedNummer < 0) {
                            for (int wNr = 0; wNr < moeglichWerte.length; wNr++) {
                                wert = moeglichWerte[wNr];
                                if (nurEnthaltenWert(inclBspWerte, wert)
                                        && !nurEnthaltenWert(exclBspWerte, wert)) {
                                    praedikate.add(new BoolWertPraedikat(attribut, wert));
                                }
                            }
                        } else {
                            // boolWertPraedNummer mu� 0 oder 1 sein.
                            wert = moeglichWerte[boolWertPraedNummer];
                            if (nurEnthaltenWert(inclBspWerte, wert)
                                    && !nurEnthaltenWert(exclBspWerte, wert)) {
                                praedikate.add(new BoolWertPraedikat(attribut, wert));
                            }
                        }
                    } else {
                        // Es handelt sich um kein boolsches Attribut.
                        for (int wNr = 0; wNr < moeglichWerte.length; wNr++) {
                            wert = moeglichWerte[wNr];
                            if (nurEnthaltenWert(inclBspWerte, wert)
                                    && !nurEnthaltenWert(exclBspWerte, wert)) {
                                praedikate.add(new NomWertPraedikat(attribut, wert));
                            }
                        }
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
        String[]         moeglichWerte;
        String[]         inclBspWerte, exclBspWerte;
        ArrayList        praedikate;
        NominalAttribut  attribut;
        String           wert;

        attributSammlung = beispieldaten.attributSammlung();
        praedikate = new ArrayList(attributSammlung.nominalAttributAnz());
        for (int aNr = 0; aNr < attributSammlung.nominalAttributAnz(); aNr++) {
            attribut = attributSammlung.getNominalAttribut(aNr);
            if (attribut != null) {
                moeglichWerte = attribut.werte();
                inclBspWerte = beispieldaten.getNominalWerte(attribut, positiv);
                exclBspWerte = beispieldaten.getNominalWerte(attribut, !positiv);

                if (!beispieldaten.nominalWertUnbekannt()
                        || !wertEnthalten(exclBspWerte, Konstanten.UNBEKANNT_WERT)) {
                    if (moeglichWerte.length == 2) {
                        // Es handelt sich um ein boolsches Attribut.
                        if (boolWertPraedNummer < 0) {
                            for (int wNr = 0; wNr < moeglichWerte.length; wNr++) {
                                wert = moeglichWerte[wNr];
                                if (!wertEnthalten(exclBspWerte, wert)
                                        && wertEnthalten(inclBspWerte, wert)) {
                                    praedikate.add(new BoolWertPraedikat(attribut, wert));
                                }
                            }
                        } else {
                            // boolWertPraedNummer mu� 0 oder 1 sein.
                            wert = moeglichWerte[boolWertPraedNummer];
                            if (!wertEnthalten(exclBspWerte, wert)
                                    && wertEnthalten(inclBspWerte, wert)) {
                                praedikate.add(new BoolWertPraedikat(attribut, wert));
                            }
                        }
                    } else {
                        // Es handelt sich um kein boolsches Attribut.
                        for (int wNr = 0; wNr < moeglichWerte.length; wNr++) {
                            wert = moeglichWerte[wNr];
                            if (!wertEnthalten(exclBspWerte, wert)
                                    && wertEnthalten(inclBspWerte, wert)) {
                                praedikate.add(new NomWertPraedikat(attribut, wert));
                            }
                        }
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
        String[]         moeglichWerte;
        String[]         posBspWerte, negBspWerte;
        ArrayList        praedikate;
        NominalAttribut  attribut;
        String           wert;
        boolean          posUnbek, negUnbek;

        attributSammlung = beispieldaten.attributSammlung();
        praedikate = new ArrayList();
        for (int aNr = 0; aNr < attributSammlung.nominalAttributAnz(); aNr++) {
            attribut = attributSammlung.getNominalAttribut(aNr);
            if (attribut != null) {
                moeglichWerte = attribut.werte();
                posBspWerte = beispieldaten.getNominalWerte(attribut, true);
                negBspWerte = beispieldaten.getNominalWerte(attribut, false);
                posUnbek = (beispieldaten.nominalWertUnbekannt()
                            && wertEnthalten(posBspWerte, Konstanten.UNBEKANNT_WERT));
                negUnbek = (beispieldaten.nominalWertUnbekannt()
                            && wertEnthalten(negBspWerte, Konstanten.UNBEKANNT_WERT));

                for (int wNr = 0; wNr < moeglichWerte.length; wNr++) {
                    if ((moeglichWerte.length != 2)
                            || (boolWertPraedNummer < 0)
                            || (wNr == boolWertPraedNummer)) {
                        wert = moeglichWerte[wNr];
                        if ((wertEnthalten(posBspWerte, wert) || posUnbek)
                                && (wertEnthalten(negBspWerte, wert) || negUnbek)
                                && !nurEnthaltenWert(posBspWerte, wert)
                                && !nurEnthaltenWert(negBspWerte, wert)) {
                            if (moeglichWerte.length == 2) {
                                praedikate.add(new BoolWertPraedikat(attribut, wert));
                            } else {
                                praedikate.add(new NomWertPraedikat(attribut, wert));
                            }
                        }
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

