/*
 * Dateiname      : AufteilungDurchUeberdeckung.java
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


package mengenUeberdeckung.aufteilung;

import java.util.Iterator;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;

/**
 * Dient zur Erzeugung einer Aufteilung einer Gesamtmenge in Teilmengen durch
 * ein Verfahren zur Berechnung einer Überdeckung.<P>
 *
 * Es wird die Umsetzung verwendet, die in der Diplomarbeit von Wohlfart
 * von 2004 beschrieben ist. Diese bezieht sich wiederum auf das Buch
 * <CITE>Integer Programming</CITE> von R.S. Garfinkel und G.L. Nemhauser
 * von 1972.
 *
 * @author  Dietmar Lippold
 */
public class AufteilungDurchUeberdeckung implements AufteilungsOptimierung {

    /**
     * Das zu verwendende Verfahren zur Erzeugung einer Überdeckung.
     */
    private UeberdeckungsOptimierung scpVerfahren;

    /**
     * Die Anzahl der bisher verarbeiteten Probleme.
     */
    private int verarbeitProblemAnz = 0;

    /**
     * Erzeugt zu der übergebenen Teilmenge eine neue Teilmenge mit
     * veränderten Kosten, die von den Kosten abhängen, die beim Hinzufügen
     * der übergebenen Teilmenge zur übergebenen Familie entstehen. Wenn alle
     * Teilmengen einer Familie auf diese Weise konvertiert werden, kann mit
     * einem Verfahren zur Erzeugung einer Überdeckung eine Aufteilung erzeugt
     * werden. Die Menge der Elemente der neuen und der übergebenen Teilmenge
     * sind identisch.
     *
     * @param teilmenge     Die zu konvertierende Teilmenge.
     * @param familie       Die Familie, zu der die zu konvertierende
     *                      Teilmenge hinzugenommen werden soll.
     * @param maxGesKosten  Eine Schranke für die maximalen Kosten einer
     *                      Lösung. Diese können z.B. durch die Summe der
     *                      Kosten aller vorhandenen Teilmengen nach oben
     *                      abgeschätzt werden.
     *
     * @return  Die neu erzeugte Teilmenge.
     */
    public static NeuOriginalItm konvertTeilmenge(IndexTeilmenge teilmenge,
                                                  ItmFamilie familie,
                                                  float maxGesKosten) {
        float altKosten, neuKosten;

        altKosten = familie.kostenHinzufuegen(teilmenge);
        neuKosten = altKosten + maxGesKosten * Konstanten.KONV_FAKTOR
                                             * teilmenge.groesseTeilmenge();
        return (new NeuOriginalItm(teilmenge, neuKosten, false));
    }

    /**
     * Erzeugt eine neue Instanz der Klasse.
     *
     * @param scpVerfahren  Das Verfahren, das zur Berechnung einer Überdeckung
     *                      verwendet werden soll.
     */
    public AufteilungDurchUeberdeckung(UeberdeckungsOptimierung scpVerfahren) {
        this.scpVerfahren = scpVerfahren;
    }

    /**
     * Versuch zur übergebenen Familie eine Teil-Familie zu erzeugen, die die
     * Familie durch die in der Teil-Familie enthaltenen Teilmengen aufteilt.
     * Dabei wird jedes Element durch maximal eine Teilmenge überdeckt, es
     * werden möglichst viele Elemente überdeckt und die Kosten der
     * Teil-Familie sind möglichst gering.
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der Aufteilung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine möglichst gute bekannte Überdeckung.
     *                              Dies kann eine Überdeckung sein, die keine
     *                              Aufteilung ist. Es kann auch der Wert
     *                              <CODE>null</CODE> sein, wenn z.B. noch
     *                              keine Lösung bekannt ist.
     *
     * @return  Die beste gefundene Aufteilung oder <code>null</code>, wenn
     *          keine Aufteilung erzeugt werden konnte.
     *
     * @throws IllegalArgumentException  Wenn die Kostenfunktion der
     *                                   übergebenen Familie nicht linear ist.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie,
                                   ItmFamilie bekannteUeberdeckung) {
        ItmFamilie      aufteilung;
        ItmFamilie      aufteilProblem, aufteilBekannt, aufteilLoesung;
        IndexTeilmenge  teilmenge;
        IndexTeilmenge  konvertTeilmenge;
        NeuOriginalItm  neuBewItm;
        Iterator        itmIter, neuItmIter;
        float           gesKosten;

        if (!familie.teilmengenKostenLinear()) {
            throw (new IllegalArgumentException("Kostenfunkion der übergebenen"
                                                + " Familie ist nicht linear."));
        }

        verarbeitProblemAnz++;

        if (familie.anzNichtUeberdeckt() > 0) {
            // Es existiert keine vollständige Überdeckung und daher erst
            // recht keine Aufteilung.
            return null;
        }

        // Ermittlung der maximalen Kosten.
        gesKosten = familie.kostenFamilie();

        // Konvertierung der Teilmengen des Problems.
        aufteilProblem = familie.neueInstanz(false);
        itmIter = familie.iterator();
        while (itmIter.hasNext()) {
            teilmenge = (IndexTeilmenge) itmIter.next();
            konvertTeilmenge = konvertTeilmenge(teilmenge, aufteilProblem,
                                                gesKosten);
            aufteilProblem.teilmengeHinzufuegen(konvertTeilmenge);
        }

        if (bekannteUeberdeckung != null) {
            // Konvertierung der Teilmengen der bekannten Lösung.
            aufteilBekannt = bekannteUeberdeckung.neueInstanz(false);
            itmIter = bekannteUeberdeckung.iterator();
            while (itmIter.hasNext()) {
                teilmenge = (IndexTeilmenge) itmIter.next();
                konvertTeilmenge = konvertTeilmenge(teilmenge, aufteilBekannt,
                                                    gesKosten);
                aufteilBekannt.teilmengeHinzufuegen(konvertTeilmenge);
            }
        } else {
            aufteilBekannt = null;
        }

        // Ermittlung der Lösung.
        aufteilLoesung = scpVerfahren.ueberdeckung(aufteilProblem,
                                                   aufteilBekannt);

        if (aufteilLoesung.anzMehrfachUeberdeckt() > 0) {
            // Es existiert keine Aufteilung.
            aufteilung = null;
        } else {
            // Rückkonvertierung der Teilmengen.
            aufteilung = aufteilLoesung.neueInstanz(false);
            neuItmIter = aufteilLoesung.iterator();
            while (neuItmIter.hasNext()) {
                neuBewItm = (NeuOriginalItm) neuItmIter.next();
                aufteilung.teilmengeHinzufuegen(neuBewItm.originalItm());
            }
        }

        return aufteilung;
    }

    /**
     * Versuch zur übergebenen Familie eine Teil-Familie zu erzeugen, die die
     * Familie durch die in der Teil-Familie enthaltenen Teilmengen aufteilt.
     * Dabei wird jedes Element durch maximal eine Teilmenge überdeckt, es
     * werden möglichst viele Elemente überdeckt und die Kosten der
     * Teil-Familie sind möglichst gering.
     *
     * @param familie  Die Familie der Teilmengen, die zur Erzeugung der
     *                 Aufteilung verwendet werden sollen.
     *
     * @return  Die beste gefundene Aufteilung oder <code>null</code>, wenn
     *          keine Aufteilung erzeugt werden konnte.
     *
     * @throws IllegalArgumentException  Wenn die Kostenfunktion der
     *                                   übergebenen Familie nicht linear ist.
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

