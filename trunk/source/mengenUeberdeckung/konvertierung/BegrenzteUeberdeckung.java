/*
 * Dateiname      : BegrenzteUeberdeckung.java
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


package mengenUeberdeckung.konvertierung;

import java.util.Iterator;
import java.util.HashSet;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;

/**
 * Erzeugt eine �berdeckung mit einer vorgegebenen maximalen Anzahl von
 * Teilmengen, soweit m�glich. Die maximale Anzahl von Teilmengen und das zu
 * verwendende Verfahren werden dem Konstruktor �bergeben.
 *
 * @author  Dietmar Lippold
 */
public class BegrenzteUeberdeckung implements UeberdeckungsOptimierung {

    /**
     * Das zu verwendende Verfahren zur Erzeugung einer �berdeckung.
     */
    private UeberdeckungsOptimierung scpVerfahren;

    /**
     * Die maximale Anzahl von Teilmengen, die in der zu erzeugenden
     * �berdeckung enthalten sein sollen.
     */
    private int maxTeilmengenAnz;

    /**
     * Die Anzahl der bisher verarbeiteten Probleme.
     */
    private int verarbeitProblemAnz = 0;

    /**
     * Erzeugt eine neue Instanz der Klasse.
     *
     * @param scpVerfahren      Das Verfahren, das zur Berechnung einer
     *                          �berdeckung verwendet werden soll.
     * @param maxTeilmengenAnz  Die maximale Anzahl von Teilmengen, die in der
     *                          zu erzeugenden �berdeckung enthalten sein
     *                          sollen.
     */
    public BegrenzteUeberdeckung(UeberdeckungsOptimierung scpVerfahren,
                                 int maxTeilmengenAnz) {

        this.scpVerfahren = scpVerfahren;
        this.maxTeilmengenAnz = maxTeilmengenAnz;
    }

    /**
     * Liefert die maximalen Kosten einer Teilmenge beim Hinzuf�gen (wenn die
     * jeweilige Teilmenge noch nicht enthalten ist) oder Entfernen (wenn die
     * jeweilige Teilmenge schon enthalten ist) zu bzw. aus der �bergebenen
     * Familie.
     *
     * @param familie     Die Familie, in Bezug auf die die Kosten ermittelt
     *                    werden sollen.
     * @param teilmengen  Die Teilmengen, zu denen die Kosten ermittelt werden
     *                    sollen.
     *
     * @return  Die maximalen Kosten beim Hinzuf�gem oder Entfernen der
     *          Teilmengen zur Familie.
     */
    private float maxKosten(ItmFamilie familie, HashSet teilmengen) {
        IndexTeilmenge teilmenge;
        Iterator       itmIter;
        float          kosten;
        float          maxKosten;

        maxKosten = 0;
        itmIter = teilmengen.iterator();
        while (itmIter.hasNext()) {
            teilmenge = (IndexTeilmenge) itmIter.next();
            if (familie.enthaelt(teilmenge)) {
                kosten = familie.kostenEntfernen(teilmenge);
            } else {
                kosten = familie.kostenHinzufuegen(teilmenge);
            }
            maxKosten = Math.max(maxKosten, kosten);
        }

        return maxKosten;
    }

    /**
     * Versucht zu der �bergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * �bergebenen Familie enth�lt (wobei die konkreten Elemente Objekte auch
     * von Unterklassen von IndexTeilmenge sein k�nnen). Die IndexTeilmengen
     * der Teil-Familie �berdecken so viele Elemente der Gesamtmenge wie
     * m�glich mit m�glichst geringen Kosten, wobei die Anzahl der �berdeckten
     * Elemente das prim�re Kriterium ist.<P>
     *
     * Es wird versucht, da� die erzeugte �berdeckung maximal so viele
     * Teilmengen enth�lt, wie beim Aufruf des Konstruktors angegeben wurde.
     * Wenn die �bergebene Familie kein Element �berdeckt, enth�lt die
     * erzeugte Familie keine Teilmenge.
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der �berdeckung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine m�glichst gute bekannte �berdeckung.
     *                              Der Wert kann <CODE>null</CODE> sein, wenn
     *                              z.B. noch keine �berdeckung bekannt ist.
     *
     * @return  Die beste gefundene �berdeckung oder <CODE>null</CODE>, wenn
     *          keine vollst�ndige �berdeckung existiert.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie,
                                   ItmFamilie bekannteUeberdeckung) {
        ItmFamilie ueberdeckung;
        ItmFamilie neueFamilie;
        ItmFamilie newKnownSolution;
        ItmFamilie ersteUeberdeckung, zweiteUeberdeckung;
        float      maxKosten, ersteKosten, zweiteKosten;
        int        ersteGroesse, zweiteGroesse;

        verarbeitProblemAnz++;

        if (familie.teilmengenKostenGleich()) {
            // Es ist eine unicost Kostenfunktion vorhanden.

            // Direkte Ermittlung der L�sung.
            ueberdeckung = scpVerfahren.ueberdeckung(familie,
                                                     bekannteUeberdeckung);

        } else if (familie.teilmengenKostenLinear()) {
            // Es ist eine lineare multicost Kostenfunktion vorhanden.

            // Erzeugung einer neuen Familie.
            maxKosten = maxKosten(familie, familie.toHashSet());
            neueFamilie = familie.clone(maxTeilmengenAnz, maxKosten);

            // Ermittlung der neuen L�sung.
            ueberdeckung = scpVerfahren.ueberdeckung(neueFamilie,
                                                     bekannteUeberdeckung);

        } else {
            // Es ist eine nicht-lineare Kostenfunktion vorhanden.

            // Erzeugung einer ersten neuen Familie.
            if (bekannteUeberdeckung == null) {
                maxKosten = maxKosten(familie.neueInstanz(false), familie.toHashSet());
                newKnownSolution = null;
            } else {
                maxKosten = maxKosten(bekannteUeberdeckung, familie.toHashSet());
                newKnownSolution = bekannteUeberdeckung.clone(maxTeilmengenAnz,
                                                              maxKosten);
            }
            neueFamilie = familie.clone(maxTeilmengenAnz, maxKosten);

            // Ermittlung der ersten neuen L�sung.
            ersteUeberdeckung = scpVerfahren.ueberdeckung(neueFamilie,
                                                          newKnownSolution);

            if (ersteUeberdeckung.groesseFamilie() <= maxTeilmengenAnz) {

                ueberdeckung = ersteUeberdeckung;

            } else {

                // Erzeugung einer zweiten neuen Familie mit ver�nderten
                // maximalen Kosten einer Teilmenge.
                maxKosten = maxKosten(ersteUeberdeckung, familie.toHashSet());
                neueFamilie = familie.clone(maxTeilmengenAnz, maxKosten);
                if (newKnownSolution != null) {
                    // Mit den neuen Kosten eine neue bekannte L�sung
                    // erzeugen.
                    newKnownSolution = bekannteUeberdeckung.clone(maxTeilmengenAnz,
                                                                  maxKosten);
                }

                // Ermittlung der zweiten neuen L�sung.
                zweiteUeberdeckung = scpVerfahren.ueberdeckung(neueFamilie,
                                                               newKnownSolution);

                ersteGroesse = ersteUeberdeckung.groesseFamilie();
                zweiteGroesse = zweiteUeberdeckung.groesseFamilie();
                ersteKosten = ersteUeberdeckung.kostenFamilie();
                zweiteKosten = zweiteUeberdeckung.kostenFamilie();
                if ((zweiteGroesse < ersteGroesse)
                    || ((zweiteGroesse == ersteGroesse)
                        && (zweiteKosten < ersteKosten))) {
                    ueberdeckung = zweiteUeberdeckung;
                } else {
                    ueberdeckung = ersteUeberdeckung;
                }
            }
        }

        return ueberdeckung;
    }

    /**
     * Versucht zu der �bergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * �bergebenen Familie enth�lt (wobei die konkreten Elemente Objekte auch
     * von Unterklassen von IndexTeilmenge sein k�nnen). Die IndexTeilmengen
     * der Teil-Familie �berdecken so viele Elemente der Gesamtmenge wie
     * m�glich mit m�glichst geringen Kosten, wobei die Anzahl der �berdeckten
     * Elemente das prim�re Kriterium ist.<P>
     *
     * Es wird versucht, da� die erzeugte �berdeckung maximal so viele
     * Teilmengen enth�lt, wie beim Aufruf des Konstruktors angegeben wurde.
     * Wenn die �bergebene Familie kein Element �berdeckt, enth�lt die
     * erzeugte Familie keine Teilmenge.
     *
     * @param familie  Die Familie der Teilmengen, die zur Erzeugung der
     *                 �berdeckung verwendet werden sollen.
     *
     * @return  Die beste gefundene �berdeckung oder <CODE>null</CODE>, wenn
     *          keine vollst�ndige �berdeckung existiert.
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

