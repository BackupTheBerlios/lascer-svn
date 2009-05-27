/*
 * Dateiname      : UeberdeckungsEntscheidung.java
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
 * Beschreibt ein Entscheidungsverfahren zum Mengen-Überdeckungs-Problem
 * (<CITE>set covering problem</CITE>). Dabei sollen die Indices einer
 * Grundmenge (Gesamtmenge) durch Teilmengen einer Familie überdeckt werden.
 * Es soll ermittelt werden, ob es eine vollständige Überdeckung gibt, deren
 * Kosten kleiner sind als ein vorgegebener Wert und falls ja, soll diese
 * Überdeckung geliefert werden.
 *
 * @author  Dietmar Lippold
 */
public interface UeberdeckungsEntscheidung {

    /**
     * Versucht zu der übergebenen Familie eine Teil-Familie zu erzeugen, die
     * alle Indices der Gesamtmenge überdeckt und Kosten besitzt, die geringer
     * sind als der vorgegebene Wert. Wenn eine solche Teil-Familie erzeugt
     * werden kann, enthält sie als Teilmengen einige der übergebenen Familie
     * (die auch Instanzen einer Unterklasse von <CODE>IndexTeilmenge</CODE>
     * sein können).
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der Überdeckung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine möglichst gute bekannte Überdeckung,
     *                              deren Kosten auch höher sein können als
     *                              <CODE>kostenGrenze</CODE>. Der Wert kann
     *                              <CODE>null</CODE> sein, wenn z.B. noch
     *                              keine Überdeckung bekannt ist.
     * @param kostenGrenze          Der Wert, der größer sein soll als die
     *                              Kosten der zu erzeugenden Überdeckung.
     *
     * @return  Eine vollständige Überdeckung mit geringeren Kosten als
     *          vorgegeben oder <CODE>null</CODE>, wenn eine solche nicht
     *          gefunden wurde.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie,
                                   ItmFamilie bekannteUeberdeckung,
                                   float kostenGrenze);

    /**
     * Versucht zu der übergebenen Familie eine Teil-Familie zu erzeugen, die
     * alle Indices der Gesamtmenge überdeckt und Kosten besitzt, die geringer
     * sind als der vorgegebene Wert. Wenn eine solche Teil-Familie erzeugt
     * werden kann, enthält sie als Teilmengen einige der übergebenen Familie
     * (die auch Instanzen einer Unterklasse von <CODE>IndexTeilmenge</CODE>
     * sein können).
     *
     * @param familie       Die Familie der Teilmengen, die zur Erzeugung der
     *                      Überdeckung verwendet werden sollen.
     * @param kostenGrenze  Der Wert, der größer sein soll als die Kosten der
     *                      zu erzeugenden Überdeckung.
     *
     * @return  Eine vollständige Überdeckung mit geringeren Kosten als
     *          vorgegeben oder <CODE>null</CODE>, wenn eine solche nicht
     *          gefunden wurde.
     */
    public ItmFamilie ueberdeckung(ItmFamilie familie, float kostenGrenze);

    /**
     * Liefert einen Text, der Daten zu den bisherigen Erzeugungen der
     * Überdeckungen liefert.
     *
     * @return  Einen Text, der Daten zu den bisherigen Erzeugungen der
     *          Überdeckungen liefert.
     */
    public String statistik();
}

