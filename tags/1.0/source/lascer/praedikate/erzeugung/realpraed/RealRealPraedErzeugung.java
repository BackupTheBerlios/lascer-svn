/*
 * Dateiname      : RealRealPraedErzeugung.java
 * Letzte �nderung: 29. August 2006
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
import java.util.ArrayList;

import lascer.praedikate.PraedikatErzeugung;
import lascer.praedikate.einzelne.realpraed.RealRealPraedikat;
import lascer.praedikate.einzelne.realpraed.RealRealGlPraedikat;
import lascer.praedikate.einzelne.realpraed.RealRealBgPraedikat;
import lascer.praedikate.einzelne.realpraed.RealRealBuPraedikat;
import lascer.problemdaten.AttributSammlung;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.attribute.RealAttribut;

/**
 * Implementiert die Methoden zur Erzeugung von Pr�dikaten zum Vergleich
 * zweier Flie�komma-Attribute.
 *
 * @author  Dietmar Lippold
 */
public class RealRealPraedErzeugung implements PraedikatErzeugung {

    /**
     * Die Beispieldaten, zu denen die Pr�dikate erzeugt werden sollen.
     */
    private Beispieldaten beispieldaten;

    /**
     * Die korrekten Pr�dikate in Bezug auf die positiven Beispiele.
     */
    private ArrayList posKorrPraedikate = new ArrayList();

    /**
     * Die vollst�ndigen Pr�dikate in Bezug auf die positiven Beispiele.
     */
    private ArrayList posVollPraedikate = new ArrayList();

    /**
     * Die allgemeinen Pr�dikate in Bezug auf die positiven Beispiele.
     */
    private ArrayList posAlgPraedikate = new ArrayList();

    /**
     * Die korrekten Pr�dikate in Bezug auf die negativen Beispiele.
     */
    private ArrayList negKorrPraedikate = new ArrayList();

    /**
     * Die vollst�ndigen Pr�dikate in Bezug auf die negativen Beispiele.
     */
    private ArrayList negVollPraedikate = new ArrayList();

    /**
     * Die allgemeinen Pr�dikate in Bezug auf die negativen Beispiele.
     */
    private ArrayList negAlgPraedikate = new ArrayList();

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param beispieldaten  Die Beispieldaten, zu denen die Pr�dikate erzeugt
     *                       werden sollen.
     */
    public RealRealPraedErzeugung(Beispieldaten beispieldaten) {
        AttributSammlung  attributSammlung;
        RealAttribut      attribut1, attribut2;
        RealRealPraedikat neuesPraed;
        float[]           posBspWerte1, posBspWerte2;
        float[]           negBspWerte1, negBspWerte2;
        int               attribAnz;
        int               posBspNr, negBspNr;
        boolean           unbekWertVorhanden;
        boolean           posKleiner, posGroesser, posGleich, posUnbek;
        boolean           negKleiner, negGroesser, negGleich, negUnbek;

        unbekWertVorhanden = beispieldaten.realWertUnbekannt();
        attributSammlung = beispieldaten.attributSammlung();
        attribAnz = attributSammlung.realAttributAnz();
        for (int a1Nr = 0; a1Nr < attribAnz - 1; a1Nr++) {
            attribut1 = attributSammlung.getRealAttribut(a1Nr);
            for (int a2Nr = a1Nr + 1; a2Nr < attribAnz; a2Nr++) {
                attribut2 = attributSammlung.getRealAttribut(a2Nr);

                posKleiner = false;
                posGroesser = false;
                posGleich = false;
                posUnbek = false;

                negKleiner = false;
                negGroesser = false;
                negGleich = false;
                negUnbek = false;

                // F�r die positiven Beispiele ermitteln, ob es ein Beispiel
                // mit gleichen und eines mit ungleichen Werten f�r die beiden
                // Attribute gibt.
                posBspNr = 0;
                posBspWerte1 = beispieldaten.getRealWerte(attribut1, true);
                posBspWerte2 = beispieldaten.getRealWerte(attribut2, true);
                while ((posBspNr < posBspWerte1.length)
                       && (!posKleiner || !posGroesser || !posGleich
                           || (unbekWertVorhanden && !posUnbek))) {
                    if (unbekWertVorhanden
                        && ((posBspWerte1[posBspNr] == Konstanten.UNBEKANNT_WERT)
                            || (posBspWerte2[posBspNr] == Konstanten.UNBEKANNT_WERT))) {
                        // Mindestens einer der beiden Werte ist unbekannt.
                        posUnbek = true;
                    } else {
                        // Keiner der beiden Werte ist unbekannt.
                        if (posBspWerte1[posBspNr] < posBspWerte2[posBspNr]) {
                            posKleiner = true;
                        } else if (posBspWerte1[posBspNr] > posBspWerte2[posBspNr]) {
                            posGroesser = true;
                        } else {
                            posGleich = true;
                        }
                    }
                    posBspNr++;
                }

                // F�r die negativen Beispiele ermitteln, ob es ein Beispiel
                // mit gleichen und eines mit ungleichen Werten f�r die beiden
                // Attribute gibt.
                negBspNr = 0;
                negBspWerte1 = beispieldaten.getRealWerte(attribut1, false);
                negBspWerte2 = beispieldaten.getRealWerte(attribut2, false);
                while ((negBspNr < negBspWerte1.length)
                       && (!negKleiner || !negGroesser || !negGleich
                           || (unbekWertVorhanden && !negUnbek))) {
                    if (unbekWertVorhanden
                        && ((negBspWerte1[negBspNr] == Konstanten.UNBEKANNT_WERT)
                            || (negBspWerte2[negBspNr] == Konstanten.UNBEKANNT_WERT))) {
                        // Mindestens einer der beiden Werte ist unbekannt.
                        negUnbek = true;
                    } else {
                        // Keiner der beiden Werte ist unbekannt.
                        if (negBspWerte1[negBspNr] < negBspWerte2[negBspNr]) {
                            negKleiner = true;
                        } else if (negBspWerte1[negBspNr] > negBspWerte2[negBspNr]) {
                            negGroesser = true;
                        } else {
                            negGleich = true;
                        }
                    }
                    negBspNr++;
                }

                // Ein neues Pr�dikat zum Gleichheits-Vergleich in der
                // zugeh�rigen Liste speichern.
                neuesPraed = new RealRealGlPraedikat(attribut1, attribut2);
                if (posGleich && !negGleich && !negUnbek) {
                    posKorrPraedikate.add(neuesPraed);
                } else if (!posKleiner && !posGroesser && !posUnbek
                           && (negKleiner || negGroesser)) {
                    posVollPraedikate.add(neuesPraed);
                } else if (negGleich && !posGleich && !posUnbek) {
                    negKorrPraedikate.add(neuesPraed);
                } else if (!negKleiner && !negGroesser && !negUnbek
                           && (posKleiner || posGroesser)) {
                    negVollPraedikate.add(neuesPraed);
                } else {
                    // Die allgemeinen Pr�dikate treffen f�r mindestens ein
                    // Beispiel der eigenen Klass zu und f�r mindestens ein
                    // Beispiel der anderen Klasse nicht zu und sie sind
                    // werden korrekt noch vollst�ndig. Die daraus folgenden
                    // Bedingungen sind f�r beide Klassen gleich.
                    if (posGleich && (negKleiner || negGroesser)
                            && negGleich && (posKleiner || posGroesser)) {
                        posAlgPraedikate.add(neuesPraed);
                        negAlgPraedikate.add(neuesPraed);
                    }
                }

                if (posGleich) {
                    // Pr�dikate f�r die positiven Beispiele zu den
                    // Vergleichen kleiner-gleich und gr��er-gleich speichern.

                    // Kleiner-Gleich Pr�dikat speichern.
                    neuesPraed = new RealRealBgPraedikat(attribut1, attribut2,
                                                         true);
                    if (posKleiner && !negKleiner && !negGleich && !negUnbek) {
                        posKorrPraedikate.add(neuesPraed);
                    } else if (!posGroesser && !posUnbek && negGroesser) {
                        posVollPraedikate.add(neuesPraed);
                    } else {
                        if (posKleiner && negGroesser
                                && (negKleiner || negGleich) && posGroesser) {
                            posAlgPraedikate.add(neuesPraed);
                        }
                    }

                    // Gr��er-Gleich Pr�dikat speichern.
                    neuesPraed = new RealRealBgPraedikat(attribut1, attribut2,
                                                         false);
                    if (posGroesser && !negGroesser && !negGleich && !negUnbek) {
                        posKorrPraedikate.add(neuesPraed);
                    } else if (!posKleiner && !posUnbek && negKleiner) {
                        posVollPraedikate.add(neuesPraed);
                    } else {
                        if (posGroesser && negKleiner
                                && (negGroesser || negGleich) && posKleiner) {
                            posAlgPraedikate.add(neuesPraed);
                        }
                    }
                }

                // Kleiner Pr�dikat f�r die positiven Beispiele speichern.
                neuesPraed = new RealRealBuPraedikat(attribut1, attribut2, true);
                if (posKleiner && !negKleiner && !negUnbek) {
                    posKorrPraedikate.add(neuesPraed);
                } else if (!posGleich && !posGroesser && !posUnbek
                           && (negGleich || negGroesser)) {
                    posVollPraedikate.add(neuesPraed);
                } else {
                    if (posKleiner && (negGleich || negGroesser)
                            && negKleiner && (posGleich || posGroesser)) {
                        posAlgPraedikate.add(neuesPraed);
                    }
                }

                // Gr��er Pr�dikat f�r die positiven Beispiele speichern.
                neuesPraed = new RealRealBuPraedikat(attribut1, attribut2, false);
                if (posGroesser && !negGroesser && !negUnbek) {
                    posKorrPraedikate.add(neuesPraed);
                } else if (!posGleich && !posKleiner && !posUnbek
                           && (negGleich || negKleiner)) {
                    posVollPraedikate.add(neuesPraed);
                } else {
                    if (posGroesser && (negGleich || negKleiner)
                            && negGroesser && (posGleich || posKleiner)) {
                        posAlgPraedikate.add(neuesPraed);
                    }
                }

                if (negGleich) {
                    // Pr�dikate f�r die negativen Beispiele zu den
                    // Vergleichen kleiner-gleich und gr��er-gleich speichern.

                    // Kleiner-Gleich Pr�dikat speichern.
                    neuesPraed = new RealRealBgPraedikat(attribut1, attribut2,
                                                         true);
                    if (negKleiner && !posKleiner && !posGleich && !posUnbek) {
                        negKorrPraedikate.add(neuesPraed);
                    } else if (!negGroesser && !negUnbek && posGroesser) {
                        negVollPraedikate.add(neuesPraed);
                    } else {
                        if (negKleiner && posGroesser
                                && (posKleiner || posGleich) && negGroesser) {
                            negAlgPraedikate.add(neuesPraed);
                        }
                    }

                    // Gr��er-Gleich Pr�dikat speichern.
                    neuesPraed = new RealRealBgPraedikat(attribut1, attribut2,
                                                         false);
                    if (negGroesser && !posGroesser && !posGleich && !posUnbek) {
                        negKorrPraedikate.add(neuesPraed);
                    } else if (!negKleiner && !negUnbek && posKleiner) {
                        negVollPraedikate.add(neuesPraed);
                    } else {
                        if (negGroesser && posKleiner
                                && (posGroesser || posGleich) && negKleiner) {
                            negAlgPraedikate.add(neuesPraed);
                        }
                    }
                }

                // Kleiner Pr�dikat f�r die negativen Beispiele speichern.
                neuesPraed = new RealRealBuPraedikat(attribut1, attribut2, true);
                if (negKleiner && !posKleiner && !posUnbek) {
                    negKorrPraedikate.add(neuesPraed);
                } else if (!negGleich && !negGroesser && !negUnbek
                           && (posGleich || posGroesser)) {
                    negVollPraedikate.add(neuesPraed);
                } else {
                    if (negKleiner && (posGleich || posGroesser)
                            && posKleiner && (negGleich || negGroesser)) {
                        negAlgPraedikate.add(neuesPraed);
                    }
                }

                // Gr��er Pr�dikat f�r die negativen Beispiele speichern.
                neuesPraed = new RealRealBuPraedikat(attribut1, attribut2, false);
                if (negGroesser && !posGroesser && !posUnbek) {
                    negKorrPraedikate.add(neuesPraed);
                } else if (!negGleich && !negKleiner && !negUnbek
                           && (posGleich || posKleiner)) {
                    negVollPraedikate.add(neuesPraed);
                } else {
                    if (negGroesser && (posGleich || posKleiner)
                            && posGroesser && (negGleich || negKleiner)) {
                        negAlgPraedikate.add(neuesPraed);
                    }
                }
            }
        }
    }

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die korrekt sind in
     * Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator �ber korrekte Pr�dikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posKorrPraedIter() {
        return ((ArrayList) posKorrPraedikate.clone()).iterator();
    }

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die korrekt sind in
     * Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator �ber korrekte Pr�dikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negKorrPraedIter() {
        return ((ArrayList) negKorrPraedikate.clone()).iterator();
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
        return ((ArrayList) posAlgPraedikate.clone()).iterator();
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
        return ((ArrayList) negAlgPraedikate.clone()).iterator();
    }
}

