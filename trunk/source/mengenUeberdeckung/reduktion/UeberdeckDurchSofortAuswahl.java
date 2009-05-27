/*
 * Dateiname      : UeberdeckDurchSofortAuswahl.java
 * Letzte �nderung: 25. Juli 2006
 * Autoren        : Dietmar Lippold
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


package mengenUeberdeckung.reduktion;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.heuristiken.utility.ReductionUtility;

/**
 * Erm�glicht die Erzeugung einer �berdeckung einer Indexmenge (Gesamtmenge)
 * durch Teilmengen mittels der Klasse <CODE>SofortigeAuswahl</CODE>. Dabei
 * werden zuerst alle Teilmengen in eine Auswahl aufgenommen und dann wird
 * diese Auswahl so lange reduziert, bis sie nur noch notwendige Teilmengen
 * enth�lt.
 *
 * @author  Dietmar Lippold
 */
public class UeberdeckDurchSofortAuswahl implements UeberdeckungsOptimierung {

    /**
     * Das Verfahren, das zur Berechnung des Nutzens der Teilmengen und der
     * Familie in der Auswahl verwendet werden soll.
     */
    private ReductionUtility reductionUtility;

    /**
     * Die Anzahl der bisher verarbeiteten Probleme.
     */
    private int verarbeitProblemAnz = 0;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param reductionUtility  Das Verfahren, das zur Berechnung des
     *                          Nutzens der Teilmengen und der Familie in
     *                          der Auswahl verwendet werden soll.
     */
    public UeberdeckDurchSofortAuswahl(ReductionUtility reductionUtility) {
        this.reductionUtility = reductionUtility;
    }

    /**
     * Versucht zu der �bergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * �bergebenen Familie enth�lt. Die IndexTeilmengen der Teil-Familie
     * �berdecken so viele Elemente der Gesamtmenge wie m�glich mit m�glichst
     * geringen Kosten, wobei die Anzahl der �berdeckten Elemente das prim�re
     * Kriterium ist.<P>
     *
     * F�r die Erzeugung der �berdeckung wird zu der �bergebenen Famile ein
     * Objekt <CODE>SofortigeAuswahl</CODE> erzeugt, das zuerst alle Elemente
     * der Familie enth�lt. Dann wir die Anzahl der minimal enthaltenen
     * Teilmengen schrittweise bis auf Eins verringert. Dann ist keine nicht
     * notwendige Teilmenge mehr enthalten ist. Die Auswahl besteht dann
     * vollst�ndig aus notwendigen Teilmengen.<P>
     *
     * Wenn die �bergebene L�sung besser ist als die erzeugte Familie, wird
     * die �bergebene L�sung als Ergebnis verwendet.
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der �berdeckung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine m�glichst gute bekannte �berdeckung.
     *                              Der Wert kann <CODE>null</CODE> sein, wenn
     *                              z.B. noch keine �berdeckung bekannt ist.
     *
     * @return  Die beste gefundene �berdeckung.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie,
                                   ItmFamilie bekannteUeberdeckung) {
        SofortigeAuswahl itmAuswahl;
        ItmFamilie       ueberdeckung;
        ItmFamilie       neueFamilie;
        ItmFamilie       auswahl;
        float            bekannteUeberdeckKosten;
        int              auwahlNichtUeberdeckt, uebergebenNichtUeberdeckt;

        verarbeitProblemAnz++;

        neueFamilie = familie.neueInstanz(false);
        itmAuswahl = new SofortigeAuswahl(neueFamilie, familie.groesseFamilie(),
                                          0, reductionUtility);
        itmAuswahl.teilmengenHinzufuegen(familie.toHashSet());
        for (int itmAnz = familie.groesseFamilie() - 1; itmAnz > 0; itmAnz--) {
            itmAuswahl.setzeMinItmAnz(itmAnz);
        }
        auswahl = itmAuswahl.auswahl();

        if (bekannteUeberdeckung == null) {
            ueberdeckung = auswahl;
        } else {
            auwahlNichtUeberdeckt = auswahl.anzNichtUeberdeckt();
            uebergebenNichtUeberdeckt = bekannteUeberdeckung.anzNichtUeberdeckt();
            bekannteUeberdeckKosten = bekannteUeberdeckung.kostenFamilie();
            if ((uebergebenNichtUeberdeckt < auwahlNichtUeberdeckt)
                || ((uebergebenNichtUeberdeckt == auwahlNichtUeberdeckt)
                    && (bekannteUeberdeckKosten < auswahl.kostenFamilie()))) {
                ueberdeckung = bekannteUeberdeckung;
            } else {
                ueberdeckung = auswahl;
            }
        }

        return ueberdeckung;
    }

    /**
     * Versucht zu der �bergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * �bergebenen Familie enth�lt. Die IndexTeilmengen der Teil-Familie
     * �berdecken so viele Elemente der Gesamtmenge wie m�glich mit m�glichst
     * geringen Kosten, wobei die Anzahl der �berdeckten Elemente das prim�re
     * Kriterium ist.<P>
     *
     * F�r die Erzeugung der �berdeckung wird zu der �bergebenen Famile ein
     * Objekt <CODE>SofortigeAuswahl</CODE> erzeugt, das zuerst alle Elemente
     * der Familie enth�lt. Dann wir die Anzahl der minimal enthaltenen
     * Teilmengen schrittweise bis auf Eins verringert. Dann ist keine nicht
     * notwendige Teilmenge mehr enthalten ist. Die Auswahl besteht dann
     * vollst�ndig aus notwendigen Teilmengen.
     *
     * @param familie  Die Familie der Teilmengen, die zur Erzeugung der
     *                 �berdeckung verwendet werden sollen.
     *
     * @return  Die beste gefundene �berdeckung.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie) {
        return ueberdeckung(familie, null);
    }

    /**
     * Liefert einen Text, der Daten zu den bisherigen Erzeugungen der
     * �berdeckungen liefert.
     *
     * @return  Einen Text, der Daten zu den bisherigen Erzeugungen der
     *          �berdeckungen liefert.
     */
    public String statistik() {
        StringBuffer rueckgabe;

        rueckgabe = new StringBuffer();
        rueckgabe.append("Anzahl der bisher verarbeiteten Probleme: ");
        rueckgabe.append(verarbeitProblemAnz);

        return rueckgabe.toString();
    }
}

