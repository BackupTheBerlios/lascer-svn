/*
 * Dateiname      : BegrenzteUeberdeckung.java
 * Letzte Änderung: 25. Juli 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
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
 * Erzeugt eine Überdeckung mit einer vorgegebenen maximalen Anzahl von
 * Teilmengen, soweit möglich. Die maximale Anzahl von Teilmengen und das zu
 * verwendende Verfahren werden dem Konstruktor übergeben.
 *
 * @author  Dietmar Lippold
 */
public class BegrenzteUeberdeckung implements UeberdeckungsOptimierung {

    /**
     * Das zu verwendende Verfahren zur Erzeugung einer Überdeckung.
     */
    private UeberdeckungsOptimierung scpVerfahren;

    /**
     * Die maximale Anzahl von Teilmengen, die in der zu erzeugenden
     * Überdeckung enthalten sein sollen.
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
     *                          Überdeckung verwendet werden soll.
     * @param maxTeilmengenAnz  Die maximale Anzahl von Teilmengen, die in der
     *                          zu erzeugenden Überdeckung enthalten sein
     *                          sollen.
     */
    public BegrenzteUeberdeckung(UeberdeckungsOptimierung scpVerfahren,
                                 int maxTeilmengenAnz) {

        this.scpVerfahren = scpVerfahren;
        this.maxTeilmengenAnz = maxTeilmengenAnz;
    }

    /**
     * Liefert die maximalen Kosten einer Teilmenge beim Hinzufügen (wenn die
     * jeweilige Teilmenge noch nicht enthalten ist) oder Entfernen (wenn die
     * jeweilige Teilmenge schon enthalten ist) zu bzw. aus der übergebenen
     * Familie.
     *
     * @param familie     Die Familie, in Bezug auf die die Kosten ermittelt
     *                    werden sollen.
     * @param teilmengen  Die Teilmengen, zu denen die Kosten ermittelt werden
     *                    sollen.
     *
     * @return  Die maximalen Kosten beim Hinzufügem oder Entfernen der
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
     * Versucht zu der übergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * übergebenen Familie enthält (wobei die konkreten Elemente Objekte auch
     * von Unterklassen von IndexTeilmenge sein können). Die IndexTeilmengen
     * der Teil-Familie überdecken so viele Elemente der Gesamtmenge wie
     * möglich mit möglichst geringen Kosten, wobei die Anzahl der überdeckten
     * Elemente das primäre Kriterium ist.<P>
     *
     * Es wird versucht, daß die erzeugte Überdeckung maximal so viele
     * Teilmengen enthält, wie beim Aufruf des Konstruktors angegeben wurde.
     * Wenn die übergebene Familie kein Element überdeckt, enthält die
     * erzeugte Familie keine Teilmenge.
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der Überdeckung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine möglichst gute bekannte Überdeckung.
     *                              Der Wert kann <CODE>null</CODE> sein, wenn
     *                              z.B. noch keine Überdeckung bekannt ist.
     *
     * @return  Die beste gefundene Überdeckung oder <CODE>null</CODE>, wenn
     *          keine vollständige Überdeckung existiert.
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

            // Direkte Ermittlung der Lösung.
            ueberdeckung = scpVerfahren.ueberdeckung(familie,
                                                     bekannteUeberdeckung);

        } else if (familie.teilmengenKostenLinear()) {
            // Es ist eine lineare multicost Kostenfunktion vorhanden.

            // Erzeugung einer neuen Familie.
            maxKosten = maxKosten(familie, familie.toHashSet());
            neueFamilie = familie.clone(maxTeilmengenAnz, maxKosten);

            // Ermittlung der neuen Lösung.
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

            // Ermittlung der ersten neuen Lösung.
            ersteUeberdeckung = scpVerfahren.ueberdeckung(neueFamilie,
                                                          newKnownSolution);

            if (ersteUeberdeckung.groesseFamilie() <= maxTeilmengenAnz) {

                ueberdeckung = ersteUeberdeckung;

            } else {

                // Erzeugung einer zweiten neuen Familie mit veränderten
                // maximalen Kosten einer Teilmenge.
                maxKosten = maxKosten(ersteUeberdeckung, familie.toHashSet());
                neueFamilie = familie.clone(maxTeilmengenAnz, maxKosten);
                if (newKnownSolution != null) {
                    // Mit den neuen Kosten eine neue bekannte Lösung
                    // erzeugen.
                    newKnownSolution = bekannteUeberdeckung.clone(maxTeilmengenAnz,
                                                                  maxKosten);
                }

                // Ermittlung der zweiten neuen Lösung.
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
     * Versucht zu der übergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * übergebenen Familie enthält (wobei die konkreten Elemente Objekte auch
     * von Unterklassen von IndexTeilmenge sein können). Die IndexTeilmengen
     * der Teil-Familie überdecken so viele Elemente der Gesamtmenge wie
     * möglich mit möglichst geringen Kosten, wobei die Anzahl der überdeckten
     * Elemente das primäre Kriterium ist.<P>
     *
     * Es wird versucht, daß die erzeugte Überdeckung maximal so viele
     * Teilmengen enthält, wie beim Aufruf des Konstruktors angegeben wurde.
     * Wenn die übergebene Familie kein Element überdeckt, enthält die
     * erzeugte Familie keine Teilmenge.
     *
     * @param familie  Die Familie der Teilmengen, die zur Erzeugung der
     *                 Überdeckung verwendet werden sollen.
     *
     * @return  Die beste gefundene Überdeckung oder <CODE>null</CODE>, wenn
     *          keine vollständige Überdeckung existiert.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie) {
        return ueberdeckung(familie, null);
    }

    /**
     * Liefert einen Text, der Daten zu den bisherigen Erzeugungen der
     * Überdeckungen liefert.
     *
     * @return  Einen Text, der Daten zu den bisherigen Erzeugungen der
     *          Überdeckungen liefert.
     */
    public String statistik() {
        StringBuffer rueckgabe;

        rueckgabe = new StringBuffer();
        rueckgabe.append("Anzahl der bisher verarbeiteten Probleme: ");
        rueckgabe.append(verarbeitProblemAnz);

        return rueckgabe.toString();
    }
}

