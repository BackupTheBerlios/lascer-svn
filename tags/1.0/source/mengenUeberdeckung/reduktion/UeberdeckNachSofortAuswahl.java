/*
 * Dateiname      : UeberdeckNachSofortAuswahl.java
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


package mengenUeberdeckung.reduktion;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.heuristiken.utility.ReductionUtility;

/**
 * Die Klasse erzeugt eine Überdeckung nach der vorherigen Reduktion einer
 * Familie von Teilmengen mittels der Klasse <CODE>SofortigeAuswahl</CODE>.
 * Die Anzahl der Teilmengen in der Auswahl kann entweder absolut oder relativ
 * in Bezug auf die für eine vollständige Überdeckung minimal notwendige
 * Anzahl erfolgen.

 * @author  Dietmar Lippold
 */
public class UeberdeckNachSofortAuswahl implements UeberdeckungsOptimierung {

    /**
     * Das Verfahren, das zur Erzeugung der Überdeckung verwendet werden soll.
     */
    private UeberdeckungsOptimierung scpVerfahren;

    /**
     * Das Verfahren, das zur Berechnung des Nutzens der Teilmengen und der
     * Familie der primäre und der ergänzten Auswahl verwendet werden soll.
     */
    private ReductionUtility reductionUtility;

    /**
     * Das Verhältnis der Anzahl der Teilmengen in der ergänzenden und der
     * primären Auswahl. Der Wert ist nicht negativ.
     */
    private float faktorErgAnz;

    /**
     * Die minimale Anzahl von Teilmengen in der primären Auswahl. Bei einem
     * negtiven Wert findet keine Auswahl statt und alle Teilmengen werden
     * aufgenommen.
     */
    private int minItmAnzahl;

    /**
     * Die minimale Häufigkeit, mit der jedes Element überdeckt sein soll.
     * Ein Wert von Null bedeutet, daß keine minimale Häuigkeit gefordert
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
     * und gibt für <CODE>faktorErgAnz</CODE> den Wert Null an. Um eine
     * relative Anzahl anzugeben, gibt man für <CODE>minItmAnzahl</CODE> den
     * Wert Null an und für <CODE>faktorErgAnz</CODE> den relativen Faktor.
     * Wenn der Wert <CODE>minItmAnzahl</CODE> negativ ist, findet keine
     * Auswahl statt und es werden alle Teilmengen gespeichert.
     *
     * @param minItmAnzahl      Die minimale Anzahl der Teilmengen, die in
     *                          der primären Auswahl enthalten sein soll. Bei
     *                          einem negativen Wert werden alle Teilmengen
     *                          aufgenommen und es findet keine Auswahl statt.
     * @param minUeberdeckAnz   Die minimale Häufigkeit, mit der jedes
     *                          Element überdeckt sein soll. Ein Wert von
     *                          Null bedeutet, daß keine minimale Häuigkeit
     *                          gefordert wird.
     * @param faktorErgAnz      Das Verhältnis der Anzahl der Teilmengen der
     *                          ergänzten Auswahl und der primären Auswahl.
     * @param reductionUtility  Das Verfahren, das zur Berechnung des
     *                          Nutzens der Teilmengen und der Familie der
     *                          primären und der ergänzten Auswahl verwendet
     *                          werden soll.
     * @param scpVerfahren      Das Verfahren, das zur Berechnung der
     *                          Überdeckung benutzt werden soll.
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
     * Versucht zu der übergebenen Familie zuerst eine Auswahl und dann mit
     * dieser Auswahl eine Überdeckung zu erzeugen. Bei jedem Aufruf wird eine
     * neue Auswahl und eine neue Überdeckung der mit jeweiligen Familie
     * erzeugt.
     *
     * @param familie               Die Familie der Teilmengen, die zur
     *                              Erzeugung der Überdeckung verwendet werden
     *                              sollen.
     * @param bekannteUeberdeckung  Eine möglichst gute bekannte Überdeckung.
     *                              Der Wert kann <CODE>null</CODE> sein, wenn
     *                              z.B. noch keine Überdeckung bekannt ist.
     *
     * @return  Die beste gefundene Überdeckung.
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
     * Versucht zu der übergebenen Familie zuerst eine Auswahl und dann mit
     * dieser Auswahl eine Überdeckung zu erzeugen. Bei jedem Aufruf wird eine
     * neue Auswahl und eine neue Überdeckung der mit jeweiligen Familie
     * erzeugt.
     *
     * @param familie  Die Familie der Teilmengen, die zur Erzeugung der
     *                 Überdeckung verwendet werden sollen.
     *
     * @return  Die beste gefundene Überdeckung.
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

