/*
 * Dateiname      : NomNomPraedErzeugung.java
 * Letzte Änderung: 28. August 2006
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

import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;

import lascer.praedikate.PraedikatErzeugung;
import lascer.praedikate.einzelne.NomNomPraedikat;
import lascer.problemdaten.AttributSammlung;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.attribute.NominalAttribut;

/**
 * Implementiert die Methoden zur Erzeugung von Prädikaten zum Vergleich
 * zweier nominaler Attribute.
 *
 * @author  Dietmar Lippold
 */
public class NomNomPraedErzeugung implements PraedikatErzeugung {

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
     * Die korrekten Prädikate in Bezug auf die negativen Beispiele.
     */
    private ArrayList negKorrPraedikate = new ArrayList();

    /**
     * Die vollständigen Prädikate in Bezug auf die negativen Beispiele.
     */
    private ArrayList negVollPraedikate = new ArrayList();

    /**
     * Die allgemeinen Prädikate in Bezug auf die positiven und die negativen
     * Beispiele.
     */
    private ArrayList algPraedikate = new ArrayList();

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param beispieldaten  Die Beispieldaten, zu denen die Prädikate erzeugt
     *                       werden sollen.
     */
    public NomNomPraedErzeugung(Beispieldaten beispieldaten) {
        AttributSammlung attributSammlung;
        NominalAttribut  attribut1, attribut2;
        NomNomPraedikat  nomNomPraed;
        ArrayList        attribWerte1, attribWerte2;
        String[]         posBspWerte1, posBspWerte2;
        String[]         negBspWerte1, negBspWerte2;
        int              nomAttribAnz;
        int              posBspNr, negBspNr;
        boolean          unbekWertVorhanden;
        boolean          posGleich, posUngleich, posUnbek;
        boolean          negGleich, negUngleich, negUnbek;

        unbekWertVorhanden = beispieldaten.nominalWertUnbekannt();
        attributSammlung = beispieldaten.attributSammlung();
        nomAttribAnz = attributSammlung.nominalAttributAnz();
        for (int a1Nr = 0; a1Nr < nomAttribAnz - 1; a1Nr++) {
            attribut1 = attributSammlung.getNominalAttribut(a1Nr);
            attribWerte1 = new ArrayList(Arrays.asList(attribut1.werte()));
            for (int a2Nr = a1Nr + 1; a2Nr < nomAttribAnz; a2Nr++) {
                attribut2 = attributSammlung.getNominalAttribut(a2Nr);
                attribWerte2 = new ArrayList(Arrays.asList(attribut2.werte()));
                attribWerte2.retainAll(attribWerte1);
                if (attribWerte2.size() > 0) {
                    // Beide Attribute besitzen mindestens einen gleichen
                    // Wert.
                    posGleich = false;
                    posUngleich = false;
                    posUnbek = false;
                    negGleich = false;
                    negUngleich = false;
                    negUnbek = false;

                    // Für die positiven Beispiele ermitteln, ob es ein
                    // Beispiel mit gleichen und eines mit ungleichen Werten
                    // für die beiden Attribute gibt.
                    posBspNr = 0;
                    posBspWerte1 = beispieldaten.getNominalWerte(attribut1, true);
                    posBspWerte2 = beispieldaten.getNominalWerte(attribut2, true);
                    while ((posBspNr < posBspWerte1.length)
                           && (!posGleich || (unbekWertVorhanden && !posUnbek))) {
                        if (unbekWertVorhanden
                            && ((posBspWerte1[posBspNr].equals(Konstanten.UNBEKANNT_WERT)
                                || posBspWerte2[posBspNr].equals(Konstanten.UNBEKANNT_WERT)))) {
                            // Mindestens einer der beiden Werte ist unbekannt.
                            posUnbek = true;
                        } else {
                            if (posBspWerte1[posBspNr].equals(posBspWerte2[posBspNr])) {
                                posGleich = true;
                            } else {
                                posUngleich = true;
                            }
                        }
                        posBspNr++;
                    }

                    // Für die negativen Beispiele ermitteln, ob es ein
                    // Beispiel mit gleichen und eines mit ungleichen Werten
                    // für die beiden Attribute gibt.
                    negBspNr = 0;
                    negBspWerte1 = beispieldaten.getNominalWerte(attribut1, false);
                    negBspWerte2 = beispieldaten.getNominalWerte(attribut2, false);
                    while ((negBspNr < negBspWerte1.length)
                           && (!negGleich || (unbekWertVorhanden && !negUnbek))) {
                        if (unbekWertVorhanden
                            && ((negBspWerte1[negBspNr].equals(Konstanten.UNBEKANNT_WERT)
                                || negBspWerte2[negBspNr].equals(Konstanten.UNBEKANNT_WERT)))) {
                            // Mindestens einer der beiden Werte ist unbekannt.
                            negUnbek = true;
                        } else {
                            if (negBspWerte1[negBspNr].equals(negBspWerte2[negBspNr])) {
                                negGleich = true;
                            } else {
                                negUngleich = true;
                            }
                        }
                        negBspNr++;
                    }

                    // Ein neues Prädikat in der zugehörigen Liste speichern.
                    nomNomPraed = new NomNomPraedikat(attribut1, attribut2);
                    if (posGleich && !negGleich && !negUnbek) {
                        posKorrPraedikate.add(nomNomPraed);
                    } else if (!posUngleich && !posUnbek && negUngleich) {
                        posVollPraedikate.add(nomNomPraed);
                    } else if (negGleich && !posGleich && !posUnbek) {
                        negKorrPraedikate.add(nomNomPraed);
                    } else if (!negUngleich && !negUnbek && posUngleich) {
                        negVollPraedikate.add(nomNomPraed);
                    } else {
                        // Die allgemeinen Prädikate treffen für mindestens
                        // ein Beispiel der eigenen Klass zu und für
                        // mindestens ein Beispiel der anderen Klasse nicht zu
                        // und sie sind werden korrekt noch vollständig. Die
                        // daraus folgenden Bedingungen sind für beide Klassen
                        // gleich.
                        if (posGleich && negUngleich && negGleich && posUngleich) {
                            algPraedikate.add(nomNomPraed);
                        }
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

