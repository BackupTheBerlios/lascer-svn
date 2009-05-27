/*
 * Dateiname      : OptimierEntscheidung.java
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

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.allgemein.UeberdeckungsEntscheidung;

/**
 * Verwendet ein vorzugebendes Optimierungsverfahren zur Realisierung eines
 * Entscheidungsverfahren zum Mengen-�berdeckungs-Problem.
 *
 * @author  Dietmar Lippold
 */
public class OptimierEntscheidung implements UeberdeckungsEntscheidung {

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
     * @param scpVerfahren  Das Optimierungs-Verfahren, das zur Berechnung
     *                      einer �berdeckung verwendet werden soll.
     */
    public OptimierEntscheidung(UeberdeckungsOptimierung scpVerfahren) {
        this.scpVerfahren = scpVerfahren;
    }

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
                                   float kostenGrenze) {
        ItmFamilie ueberdeckung;

        verarbeitProblemAnz++;

        ueberdeckung = scpVerfahren.ueberdeckung(familie, bekannteUeberdeckung);
        if ((ueberdeckung.anzNichtUeberdeckt() == 0)
                && (ueberdeckung.kostenFamilie() < kostenGrenze)) {
            return ueberdeckung;
        } else {
            return null;
        }
    }

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
    public ItmFamilie ueberdeckung(ItmFamilie familie, float kostenGrenze) {
        ItmFamilie ueberdeckung;

        verarbeitProblemAnz++;

        ueberdeckung = scpVerfahren.ueberdeckung(familie);
        if ((ueberdeckung.anzNichtUeberdeckt() == 0)
                && (ueberdeckung.kostenFamilie() < kostenGrenze)) {
            return ueberdeckung;
        } else {
            return null;
        }
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

