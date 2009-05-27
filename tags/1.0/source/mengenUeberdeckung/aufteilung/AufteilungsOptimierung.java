/*
 * Dateiname      : AufteilungsOptimierung.java
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


package mengenUeberdeckung.aufteilung;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;

/**
 * Beschreibt ein Optimierungsverfahren zum Mengen-Aufteilungs-Problem
 * (<CITE>set partitioning problem</CITE>). Dabei sollen die Indices einer
 * Grundmenge (Gesamtmenge) durch disjunkte Teilmengen einer Familie �berdeckt
 * werden. Es darf also kein Index von zwei der ausgew�hlten Teilmengen
 * �berdeckt werden. Prim�res Kriterium der Optimierung ist die �berdeckung
 * m�glichst vieler Indcies, sekund�res Kriterium die Minimierung der Kosten
 * der Familie der ausgew�hlten Teilmengen. Es ist m�glich, da� eine
 * implementierende Klasse nur Probleme, zu denen eine vollst�ndige
 * �berdeckung existiert, optimieren kann und dann versucht, eine vollst�ndige
 * �berdeckung, die eine Aufteilung ist, mit m�glichst geringen Kosten zu
 * liefern.
 *
 * @author  Dietmar Lippold
 */
public interface AufteilungsOptimierung extends UeberdeckungsOptimierung {

    /**
     * Versucht zu der �bergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * �bergebenen Familie enth�lt (wobei die konkreten Elemente Objekte auch
     * von Unterklassen von IndexTeilmenge sein k�nnen). Die IndexTeilmengen
     * der Teil-Familie �berdecken so viele Elemente der Gesamtmenge wie
     * m�glich mit m�glichst geringen Kosten, ohne da� ein Element mehrfach
     * �berdeckt wird. Die maximal einmalige �berdeckung jedes Elements ist
     * dabei erforderlich, die Anzahl der �berdeckten Elemente ein sekund�res
     * Kriterium und die Kosten ein terti�re Kriterium.
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der Aufteilung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine m�glichst gute bekannte �berdeckung.
     *                              Dies kann eine �berdeckung sein, die keine
     *                              Aufteilung ist. Es kann auch der Wert
     *                              <CODE>null</CODE> sein, wenn z.B. noch
     *                              keine L�sung bekannt ist.
     *
     * @return  Die beste gefundene Aufteilung oder <code>null</code>, wenn
     *          keine Aufteilung erzeugt werden konnte.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie,
                                   ItmFamilie bekannteUeberdeckung);

    /**
     * Versucht zu der �bergebenen Familie eine Teil-Familie zu erzeugen, die
     * als neues Objekt Elemente (vom Typ <CODE>IndexTeilmenge</CODE>) der
     * �bergebenen Familie enth�lt (wobei die konkreten Elemente Objekte auch
     * von Unterklassen von IndexTeilmenge sein k�nnen). Die IndexTeilmengen
     * der Teil-Familie �berdecken so viele Elemente der Gesamtmenge wie
     * m�glich mit m�glichst geringen Kosten, ohne da� ein Element mehrfach
     * �berdeckt wird. Die maximal einmalige �berdeckung jedes Elements ist
     * dabei erforderlich, die Anzahl der �berdeckten Elemente ein sekund�res
     * Kriterium und die Kosten ein terti�re Kriterium.
     *
     * @param familie  Die Familie der Teilmengen, die zur Erzeugung der
     *                 Aufteilung verwendet werden sollen.
     *
     * @return  Die beste gefundene Aufteilung oder <code>null</code>, wenn
     *          keine Aufteilung erzeugt werden konnte.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie);

    /**
     * Liefert einen Text, der Daten zu den bisherigen Erzeugungen der
     * Aufteilungen liefert.
     *
     * @return  Einen Text, der Daten zu den bisherigen Erzeugungen der
     *          Aufteilungen liefert.
     */
    public String statistik();
}

