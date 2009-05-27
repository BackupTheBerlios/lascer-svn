/*
 * Dateiname      : UeberdeckungsOptimierung.java
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


package mengenUeberdeckung.allgemein;

/**
 * Beschreibt ein Optimierungsverfahren zum Mengen-�berdeckungs-Problem
 * (<CITE>set covering problem</CITE>). Dabei sollen die Indices einer
 * Grundmenge (Gesamtmenge) durch Teilmengen einer Familie �berdeckt werden.
 * Prim�res Kriterium der Optimierung ist die �berdeckung m�glichst vieler
 * Indcies, sekund�res Kriterium die Minimierung der Kosten der Familie der
 * ausgew�hlten Teilmengen. Es ist m�glich, da� eine implementierende Klasse
 * nur Probleme, zu denen eine vollst�ndige �berdeckung existiert, optimieren
 * kann und dann eine vollst�ndige �berdeckung mit m�glichst geringen Kosten
 * liefert.
 *
 * @author  Dietmar Lippold
 */
public interface UeberdeckungsOptimierung {

    /**
     * Versucht zu der �bergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * �bergebenen Familie enth�lt (wobei die konkreten Elemente Objekte auch
     * von Unterklassen von IndexTeilmenge sein k�nnen). Die IndexTeilmengen
     * der Teil-Familie �berdecken so viele Elemente der Gesamtmenge wie
     * m�glich mit m�glichst geringen Kosten, wobei die Anzahl der �berdeckten
     * Elemente das prim�re Kriterium ist.
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der �berdeckung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine m�glichst gute bekannte �berdeckung.
     *                              Der Wert kann <CODE>null</CODE> sein, wenn
     *                              z.B. noch keine �berdeckung bekannt ist.
     *
     * @return  Die beste gefundene �berdeckung oder <CODE>null</CODE>, wenn
     *          keine gefunden wurde.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie,
                                   ItmFamilie bekannteUeberdeckung);

    /**
     * Versucht zu der �bergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * �bergebenen Familie enth�lt (wobei die konkreten Elemente Objekte auch
     * von Unterklassen von IndexTeilmenge sein k�nnen). Die IndexTeilmengen
     * der Teil-Familie �berdecken so viele Elemente der Gesamtmenge wie
     * m�glich mit m�glichst geringen Kosten, wobei die Anzahl der �berdeckten
     * Elemente das prim�re Kriterium ist.
     *
     * @param familie  Die Familie der Teilmengen, die zur Erzeugung der
     *                 �berdeckung verwendet werden sollen.
     *
     * @return  Die beste gefundene �berdeckung oder <CODE>null</CODE>, wenn
     *          keine gefunden wurde.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie);

    /**
     * Liefert einen Text, der Daten zu den bisherigen Erzeugungen der
     * �berdeckungen liefert.
     *
     * @return  Einen Text, der Daten zu den bisherigen Erzeugungen der
     *          �berdeckungen liefert.
     */
    public String statistik();
}

