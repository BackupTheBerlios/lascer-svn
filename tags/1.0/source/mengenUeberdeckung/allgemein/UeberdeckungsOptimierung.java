/*
 * Dateiname      : UeberdeckungsOptimierung.java
 * Letzte Änderung: 23. Juli 2006
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


package mengenUeberdeckung.allgemein;

/**
 * Beschreibt ein Optimierungsverfahren zum Mengen-Überdeckungs-Problem
 * (<CITE>set covering problem</CITE>). Dabei sollen die Indices einer
 * Grundmenge (Gesamtmenge) durch Teilmengen einer Familie überdeckt werden.
 * Primäres Kriterium der Optimierung ist die Überdeckung möglichst vieler
 * Indcies, sekundäres Kriterium die Minimierung der Kosten der Familie der
 * ausgewählten Teilmengen. Es ist möglich, daß eine implementierende Klasse
 * nur Probleme, zu denen eine vollständige Überdeckung existiert, optimieren
 * kann und dann eine vollständige Überdeckung mit möglichst geringen Kosten
 * liefert.
 *
 * @author  Dietmar Lippold
 */
public interface UeberdeckungsOptimierung {

    /**
     * Versucht zu der übergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * übergebenen Familie enthält (wobei die konkreten Elemente Objekte auch
     * von Unterklassen von IndexTeilmenge sein können). Die IndexTeilmengen
     * der Teil-Familie überdecken so viele Elemente der Gesamtmenge wie
     * möglich mit möglichst geringen Kosten, wobei die Anzahl der überdeckten
     * Elemente das primäre Kriterium ist.
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der Überdeckung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine möglichst gute bekannte Überdeckung.
     *                              Der Wert kann <CODE>null</CODE> sein, wenn
     *                              z.B. noch keine Überdeckung bekannt ist.
     *
     * @return  Die beste gefundene Überdeckung oder <CODE>null</CODE>, wenn
     *          keine gefunden wurde.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie,
                                   ItmFamilie bekannteUeberdeckung);

    /**
     * Versucht zu der übergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * übergebenen Familie enthält (wobei die konkreten Elemente Objekte auch
     * von Unterklassen von IndexTeilmenge sein können). Die IndexTeilmengen
     * der Teil-Familie überdecken so viele Elemente der Gesamtmenge wie
     * möglich mit möglichst geringen Kosten, wobei die Anzahl der überdeckten
     * Elemente das primäre Kriterium ist.
     *
     * @param familie  Die Familie der Teilmengen, die zur Erzeugung der
     *                 Überdeckung verwendet werden sollen.
     *
     * @return  Die beste gefundene Überdeckung oder <CODE>null</CODE>, wenn
     *          keine gefunden wurde.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie);

    /**
     * Liefert einen Text, der Daten zu den bisherigen Erzeugungen der
     * Überdeckungen liefert.
     *
     * @return  Einen Text, der Daten zu den bisherigen Erzeugungen der
     *          Überdeckungen liefert.
     */
    public String statistik();
}

