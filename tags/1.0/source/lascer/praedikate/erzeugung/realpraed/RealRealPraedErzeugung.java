/*
 * Dateiname      : RealRealPraedErzeugung.java
 * Letzte Änderung: 29. August 2006
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
 * Implementiert die Methoden zur Erzeugung von Prädikaten zum Vergleich
 * zweier Fließkomma-Attribute.
 *
 * @author  Dietmar Lippold
 */
public class RealRealPraedErzeugung implements PraedikatErzeugung {

    /**
     * Die Beispieldaten, zu denen die Prädikate erzeugt werden sollen.
     */
    private Beispieldaten beispieldaten;

    /**
     * Die korrekten Prädikate in Bezug auf die positiven Beispiele.
     */
    private ArrayList posKorrPraedikate = new ArrayList();

    /**
     * Die vollständigen Prädikate in Bezug auf die positiven Beispiele.
     */
    private ArrayList posVollPraedikate = new ArrayList();

    /**
     * Die allgemeinen Prädikate in Bezug auf die positiven Beispiele.
     */
    private ArrayList posAlgPraedikate = new ArrayList();

    /**
     * Die korrekten Prädikate in Bezug auf die negativen Beispiele.
     */
    private ArrayList negKorrPraedikate = new ArrayList();

    /**
     * Die vollständigen Prädikate in Bezug auf die negativen Beispiele.
     */
    private ArrayList negVollPraedikate = new ArrayList();

    /**
     * Die allgemeinen Prädikate in Bezug auf die negativen Beispiele.
     */
    private ArrayList negAlgPraedikate = new ArrayList();

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param beispieldaten  Die Beispieldaten, zu denen die Prädikate erzeugt
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

                // Für die positiven Beispiele ermitteln, ob es ein Beispiel
                // mit gleichen und eines mit ungleichen Werten für die beiden
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

                // Für die negativen Beispiele ermitteln, ob es ein Beispiel
                // mit gleichen und eines mit ungleichen Werten für die beiden
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

                // Ein neues Prädikat zum Gleichheits-Vergleich in der
                // zugehörigen Liste speichern.
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
                    // Die allgemeinen Prädikate treffen für mindestens ein
                    // Beispiel der eigenen Klass zu und für mindestens ein
                    // Beispiel der anderen Klasse nicht zu und sie sind
                    // werden korrekt noch vollständig. Die daraus folgenden
                    // Bedingungen sind für beide Klassen gleich.
                    if (posGleich && (negKleiner || negGroesser)
                            && negGleich && (posKleiner || posGroesser)) {
                        posAlgPraedikate.add(neuesPraed);
                        negAlgPraedikate.add(neuesPraed);
                    }
                }

                if (posGleich) {
                    // Prädikate für die positiven Beispiele zu den
                    // Vergleichen kleiner-gleich und größer-gleich speichern.

                    // Kleiner-Gleich Prädikat speichern.
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

                    // Größer-Gleich Prädikat speichern.
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

                // Kleiner Prädikat für die positiven Beispiele speichern.
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

                // Größer Prädikat für die positiven Beispiele speichern.
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
                    // Prädikate für die negativen Beispiele zu den
                    // Vergleichen kleiner-gleich und größer-gleich speichern.

                    // Kleiner-Gleich Prädikat speichern.
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

                    // Größer-Gleich Prädikat speichern.
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

                // Kleiner Prädikat für die negativen Beispiele speichern.
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

                // Größer Prädikat für die negativen Beispiele speichern.
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
     * Liefert einen Iterator über erzeugte Prädikate, die korrekt sind in
     * Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator über korrekte Prädikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posKorrPraedIter() {
        return ((ArrayList) posKorrPraedikate.clone()).iterator();
    }

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die korrekt sind in
     * Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator über korrekte Prädikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negKorrPraedIter() {
        return ((ArrayList) negKorrPraedikate.clone()).iterator();
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
        return ((ArrayList) posAlgPraedikate.clone()).iterator();
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
        return ((ArrayList) negAlgPraedikate.clone()).iterator();
    }
}

