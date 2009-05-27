/*
 * Dateiname      : UeberdeckungsEntscheidung.java
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
 * Beschreibt ein Entscheidungsverfahren zum Mengen-�berdeckungs-Problem
 * (<CITE>set covering problem</CITE>). Dabei sollen die Indices einer
 * Grundmenge (Gesamtmenge) durch Teilmengen einer Familie �berdeckt werden.
 * Es soll ermittelt werden, ob es eine vollst�ndige �berdeckung gibt, deren
 * Kosten kleiner sind als ein vorgegebener Wert und falls ja, soll diese
 * �berdeckung geliefert werden.
 *
 * @author  Dietmar Lippold
 */
public interface UeberdeckungsEntscheidung {

    /**
     * Versucht zu der �bergebenen Familie eine Teil-Familie zu erzeugen, die
     * alle Indices der Gesamtmenge �berdeckt und Kosten besitzt, die geringer
     * sind als der vorgegebene Wert. Wenn eine solche Teil-Familie erzeugt
     * werden kann, enth�lt sie als Teilmengen einige der �bergebenen Familie
     * (die auch Instanzen einer Unterklasse von <CODE>IndexTeilmenge</CODE>
     * sein k�nnen).
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der �berdeckung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine m�glichst gute bekannte �berdeckung,
     *                              deren Kosten auch h�her sein k�nnen als
     *                              <CODE>kostenGrenze</CODE>. Der Wert kann
     *                              <CODE>null</CODE> sein, wenn z.B. noch
     *                              keine �berdeckung bekannt ist.
     * @param kostenGrenze          Der Wert, der gr��er sein soll als die
     *                              Kosten der zu erzeugenden �berdeckung.
     *
     * @return  Eine vollst�ndige �berdeckung mit geringeren Kosten als
     *          vorgegeben oder <CODE>null</CODE>, wenn eine solche nicht
     *          gefunden wurde.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie,
                                   ItmFamilie bekannteUeberdeckung,
                                   float kostenGrenze);

    /**
     * Versucht zu der �bergebenen Familie eine Teil-Familie zu erzeugen, die
     * alle Indices der Gesamtmenge �berdeckt und Kosten besitzt, die geringer
     * sind als der vorgegebene Wert. Wenn eine solche Teil-Familie erzeugt
     * werden kann, enth�lt sie als Teilmengen einige der �bergebenen Familie
     * (die auch Instanzen einer Unterklasse von <CODE>IndexTeilmenge</CODE>
     * sein k�nnen).
     *
     * @param familie       Die Familie der Teilmengen, die zur Erzeugung der
     *                      �berdeckung verwendet werden sollen.
     * @param kostenGrenze  Der Wert, der gr��er sein soll als die Kosten der
     *                      zu erzeugenden �berdeckung.
     *
     * @return  Eine vollst�ndige �berdeckung mit geringeren Kosten als
     *          vorgegeben oder <CODE>null</CODE>, wenn eine solche nicht
     *          gefunden wurde.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie, float kostenGrenze);

    /**
     * Liefert einen Text, der Daten zu den bisherigen Erzeugungen der
     * �berdeckungen liefert.
     *
     * @return  Einen Text, der Daten zu den bisherigen Erzeugungen der
     *          �berdeckungen liefert.
     */
    public String statistik();
}

