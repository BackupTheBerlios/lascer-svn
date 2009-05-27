/*
 * Dateiname      : UeberdeckNachSofortAuswahl.java
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


package mengenUeberdeckung.reduktion;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.heuristiken.utility.ReductionUtility;

/**
 * Die Klasse erzeugt eine �berdeckung nach der vorherigen Reduktion einer
 * Familie von Teilmengen mittels der Klasse <CODE>SofortigeAuswahl</CODE>.
 * Die Anzahl der Teilmengen in der Auswahl kann entweder absolut oder relativ
 * in Bezug auf die f�r eine vollst�ndige �berdeckung minimal notwendige
 * Anzahl erfolgen.

 * @author  Dietmar Lippold
 */
public class UeberdeckNachSofortAuswahl implements UeberdeckungsOptimierung {

    /**
     * Das Verfahren, das zur Erzeugung der �berdeckung verwendet werden soll.
     */
    private UeberdeckungsOptimierung scpVerfahren;

    /**
     * Das Verfahren, das zur Berechnung des Nutzens der Teilmengen und der
     * Familie der prim�re und der erg�nzten Auswahl verwendet werden soll.
     */
    private ReductionUtility reductionUtility;

    /**
     * Das Verh�ltnis der Anzahl der Teilmengen in der erg�nzenden und der
     * prim�ren Auswahl. Der Wert ist nicht negativ.
     */
    private float faktorErgAnz;

    /**
     * Die minimale Anzahl von Teilmengen in der prim�ren Auswahl. Bei einem
     * negtiven Wert findet keine Auswahl statt und alle Teilmengen werden
     * aufgenommen.
     */
    private int minItmAnzahl;

    /**
     * Die minimale H�ufigkeit, mit der jedes Element �berdeckt sein soll.
     * Ein Wert von Null bedeutet, da� keine minimale H�uigkeit gefordert
     * wird.
     */
    private int minUeberdeckAnz;

    /**
     * Die Anzahl der bisher verarbeiteten Probleme.
     */
    private int verarbeitProblemAnz = 0;

    /**
     * Erzeugt eine neue Instanz. Um eine absolute Anzahl der Teilmengen in
     * der Auswahl anzugeben, gibt man diese als <CODE>minItmAnzahl</CODE>
     * und gibt f�r <CODE>faktorErgAnz</CODE> den Wert Null an. Um eine
     * relative Anzahl anzugeben, gibt man f�r <CODE>minItmAnzahl</CODE> den
     * Wert Null an und f�r <CODE>faktorErgAnz</CODE> den relativen Faktor.
     * Wenn der Wert <CODE>minItmAnzahl</CODE> negativ ist, findet keine
     * Auswahl statt und es werden alle Teilmengen gespeichert.
     *
     * @param minItmAnzahl      Die minimale Anzahl der Teilmengen, die in
     *                          der prim�ren Auswahl enthalten sein soll. Bei
     *                          einem negativen Wert werden alle Teilmengen
     *                          aufgenommen und es findet keine Auswahl statt.
     * @param minUeberdeckAnz   Die minimale H�ufigkeit, mit der jedes
     *                          Element �berdeckt sein soll. Ein Wert von
     *                          Null bedeutet, da� keine minimale H�uigkeit
     *                          gefordert wird.
     * @param faktorErgAnz      Das Verh�ltnis der Anzahl der Teilmengen der
     *                          erg�nzten Auswahl und der prim�ren Auswahl.
     * @param reductionUtility  Das Verfahren, das zur Berechnung des
     *                          Nutzens der Teilmengen und der Familie der
     *                          prim�ren und der erg�nzten Auswahl verwendet
     *                          werden soll.
     * @param scpVerfahren      Das Verfahren, das zur Berechnung der
     *                          �berdeckung benutzt werden soll.
     *
     * @throws IllegalArgumentException  <CODE>faktorErgAnz</CODE> hat einen
     *                                   negativen Wert.
     */
    public UeberdeckNachSofortAuswahl(int minItmAnzahl, int minUeberdeckAnz,
                                      float faktorErgAnz,
                                      ReductionUtility reductionUtility,
                                      UeberdeckungsOptimierung scpVerfahren) {
        this.minItmAnzahl = minItmAnzahl;
        this.minUeberdeckAnz = minUeberdeckAnz;
        this.faktorErgAnz = faktorErgAnz;
        this.reductionUtility = reductionUtility;
        this.scpVerfahren = scpVerfahren;
    }

    /**
     * Versucht zu der �bergebenen Familie zuerst eine Auswahl und dann mit
     * dieser Auswahl eine �berdeckung zu erzeugen. Bei jedem Aufruf wird eine
     * neue Auswahl und eine neue �berdeckung der mit jeweiligen Familie
     * erzeugt.
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
        ItmAuswahlErzeugung auswahlVerfahren;
        ItmFamilie neueFamilie;

        verarbeitProblemAnz++;

        neueFamilie = familie.neueInstanz(false);
        auswahlVerfahren = new SofortigeAuswahl(neueFamilie, minItmAnzahl,
                                                minUeberdeckAnz, faktorErgAnz,
                                                reductionUtility);
        auswahlVerfahren.teilmengenHinzufuegen(familie.toHashSet());

        return scpVerfahren.ueberdeckung(auswahlVerfahren.auswahl(),
                                         bekannteUeberdeckung);
    }

    /**
     * Versucht zu der �bergebenen Familie zuerst eine Auswahl und dann mit
     * dieser Auswahl eine �berdeckung zu erzeugen. Bei jedem Aufruf wird eine
     * neue Auswahl und eine neue �berdeckung der mit jeweiligen Familie
     * erzeugt.
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

