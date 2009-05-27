/*
 * Dateiname      : PartielleUeberdeckung.java
 * Letzte �nderung: 23. Juli 2006
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
import java.util.HashMap;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;

/**
 * Erzeugt eine �berdeckung mit einem im Konstruktor �bergebenen Verfahren,
 * wobei das Problem vorher in ein l�sbares Problem konvertiert wird und die
 * L�sung r�ckkonvertiert wird. Die Konvertierung erfolgt, indem Kopien der
 * Teilmengen die Indices hinzugef�gt werden, die in keiner Teilmenge
 * enthalten sind.
 *
 * @author  Dietmar Lippold
 */
public class PartielleUeberdeckung implements UeberdeckungsOptimierung {

    /**
     * Das zu verwendende Verfahren zur Erzeugung einer �berdeckung.
     */
    private UeberdeckungsOptimierung scpVerfahren;

    /**
     * Die Anzahl der bisher verarbeiteten Probleme.
     */
    private int verarbeitProblemAnz = 0;

    /**
     * Erzeugt eine neue Instanz der Klasse.
     *
     * @param scpVerfahren  Das Verfahren, das zur Berechnung einer
     *                      �berdeckung verwendet werden soll.
     */
    public PartielleUeberdeckung(UeberdeckungsOptimierung scpVerfahren) {
        this.scpVerfahren = scpVerfahren;
    }

    /**
     * Erzeugt zu der �bergebenen Familie ein Array mit den Indices der
     * zugrunde liegenden Gesamtmenge, die in keiner der Teilmengen enthalten
     + sind.
     *
     * @param familie  Die Familie der Teilmengen, zu der die fehlenden
     *                 Indeices ermittelt werden soll.
     *
     * @return  Die fehlenden Indices.
     */
    private int[] fehlendeIndices(ItmFamilie familie) {
        int[] abbildung = new int[familie.groesseGesamtmenge()];
        int[] rueckgabe;
        int   fehlAnz = 0;

        for (int index = 0; index < familie.groesseGesamtmenge(); index++) {
            if (!familie.indexIstUeberdeckt(index)) {
                abbildung[fehlAnz] = index;
                fehlAnz++;
            }
        }

        rueckgabe = new int[fehlAnz];
        System.arraycopy(abbildung, 0, rueckgabe, 0, fehlAnz);

        return rueckgabe;
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
     * Dieser Methode kann insbesondere eine Familie �bergeben werden, in der
     * Elemente enthalten sind, die von keiner Teilmenge �berdeckt werden.
     * Es wird dann die beste partielle �berdeckung erzeugt. Wenn die
     * �bergebene Familie gar kein Element �berdeckt, enth�lt die erzeugte
     * Familie keine Teilmenge.
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
        ItmFamilie      teilUeberdeckung;
        ItmFamilie      erweiterteFamilie;
        ItmFamilie      konvProblem, konvLoesung;
        ItmFamilie      neueBekannteLoesung;
        HashMap         neuOrigItmMap;
        IndexTeilmenge  origTeilmenge;
        IndexTeilmenge  neueTeilmenge;
        Iterator        itmIter, neuItmIter;
        int[]           fehlendeIndices;

        verarbeitProblemAnz++;

        if (bekannteUeberdeckung != null) {
            erweiterteFamilie = (ItmFamilie) familie.clone();
            erweiterteFamilie.teilmengenHinzufuegen(bekannteUeberdeckung.toHashSet());
        } else {
            erweiterteFamilie = familie;
        }

        if (erweiterteFamilie.anzUeberdeckt() == 0) {
            // Die beste Teill�sung ist leer.
            teilUeberdeckung = erweiterteFamilie.neueInstanz(false);
        } else if (erweiterteFamilie.anzNichtUeberdeckt() == 0) {
            // Es ist keine Konvertierung notwendig.
            teilUeberdeckung = scpVerfahren.ueberdeckung(erweiterteFamilie,
                                                         bekannteUeberdeckung);
        } else {
            // Initialisierung der lokalen Variablen.
            neuOrigItmMap = new HashMap(erweiterteFamilie.groesseFamilie());
            konvProblem = erweiterteFamilie.neueInstanz(false);
            fehlendeIndices = fehlendeIndices(erweiterteFamilie);

            // Konvertierung der Teilmengen, die aufgenommen werden k�nnen.
            itmIter = erweiterteFamilie.iterator();
            while (itmIter.hasNext()) {
                origTeilmenge = (IndexTeilmenge) itmIter.next();
                neueTeilmenge = (IndexTeilmenge) origTeilmenge.clone();
                neueTeilmenge.indicesAufnehmen(fehlendeIndices);
                konvProblem.teilmengeHinzufuegen(neueTeilmenge);
                neuOrigItmMap.put(neueTeilmenge, origTeilmenge);
            }

            if (bekannteUeberdeckung != null) {
                // Konvertierung der Teilmengen der besten bekannten L�sung.
                neueBekannteLoesung = bekannteUeberdeckung.neueInstanz(false);
                itmIter = bekannteUeberdeckung.iterator();
                while (itmIter.hasNext()) {
                    origTeilmenge = (IndexTeilmenge) itmIter.next();
                    neueTeilmenge = (IndexTeilmenge) origTeilmenge.clone();
                    neueTeilmenge.indicesAufnehmen(fehlendeIndices);
                    neueBekannteLoesung.teilmengeHinzufuegen(neueTeilmenge);
                }
            } else {
                neueBekannteLoesung = null;
            }

            // Ermittlung der neuen L�sung.
            konvLoesung = scpVerfahren.ueberdeckung(konvProblem,
                                                    neueBekannteLoesung);

            // R�ckkonvertierung der Teilmengen.
            teilUeberdeckung = erweiterteFamilie.neueInstanz(false);
            neuItmIter = konvLoesung.iterator();
            while (neuItmIter.hasNext()) {
                neueTeilmenge = (IndexTeilmenge) neuItmIter.next();
                origTeilmenge = (IndexTeilmenge) neuOrigItmMap.get(neueTeilmenge);
                teilUeberdeckung.teilmengeHinzufuegen(origTeilmenge);
            }
        }

        return teilUeberdeckung;
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
     * Dieser Methode kann insbesondere eine Familie �bergeben werden, in der
     * Elemente enthalten sind, die von keiner Teilmenge �berdeckt werden.
     * Es wird dann die beste partielle �berdeckung erzeugt. Wenn die
     * �bergebene Familie gar kein Element �berdeckt, enth�lt die erzeugte
     * Familie keine Teilmenge.
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

