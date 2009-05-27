/*
 * Dateiname      : ProbabilityRatings.java
 * Letzte �nderung: 25. Juli 2006
 * Autoren        : Edgar Binder, Dietmar Lippold
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


package mengenUeberdeckung.heuristiken.ratings;

import java.util.List;
import java.util.Iterator;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Dient der Bewertung von Teilmengen zur Aufnahme in oder zur Entfernung aus
 * einer partiellen �berdeckung, indem ein Konvergenzverfahren verwendet wird,
 * das die Wahrscheinlichkeiten des Vorkommens der Teilmengen in der L�sung
 * ber�cksichtigt.
 *
 * TODO: - Optimierung der Konstanten anzahlIterationen, daempfungsFaktor und
 *         relExponent.
 *
 * @author  Edgar Binder, Dietmar Lippold
 */
public class ProbabilityRatings implements CandidateRatings {

    /**
     * Die Anzahl der durchzuf�hrenden Iterationen.
     */
    private int anzahlIterationen = Konstanten.PROB_ITER_NUMBER;

    /**
     * Der Faktor, mit dem die Ver�nderungen der Wahrscheinlichkeiten der
     * Teilmengen ged�mpft werden.
     */
    private float daempfungsFaktor = Konstanten.DAEMPFUNGS_FAKTOR;

    /**
     * Der Wert, mit dem die Relation der allgemeinen Wahrscheinlichkeit und
     * der Kosten einer Teilmenge zur Ermittlung ihrer speziellen
     * (elementbezogenen) Wahrscheinlichkeit potenziert wird.
     */
    private float relExponent;

    /**
     * Gibt an, ob die Wahrscheinlichkeiten der Teilmengen mit einem
     * einheitlichen Wert initialisiert werden sollen.
     */
    private boolean wtInitEinheitlich = Konstanten.WT_INIT_EINHEITLICH;

    /**
     * Gibt an, ob das Produkt-Verfahren angewendet werden soll. Falls nicht,
     * wird das Summen-Verfahren angewendet.
     */
    private boolean prodVerfahren = Konstanten.PROB_PROD_VERFAHREN;

    /**
     * Erzeugt eine neue Instanz und legt die Anzahl der Iterationen im
     * Wahrscheinlichkeitkonvergenzverfahren fest.
     *
     * @param alleTeilmengen  Alle Teilmengen des zugrunde liegenden Problems.
     */
    public ProbabilityRatings(ItmFamilie alleTeilmengen) {
        if (alleTeilmengen.teilmengenKostenGleich()) {
            relExponent = Konstanten.U_REL_EXPONENT;
        } else {
            relExponent = Konstanten.M_REL_EXPONENT;
        }
    }

    /**
     * Erzeugt eine neue Instanz und legt die Anzahl der Iterationen im
     * Wahrscheinlichkeitkonvergenzverfahren fest. Au�erdem wird angegeben,
     * ob die Teilmengen initial die gleiche Wahrscheinlichkeit, ausgew�hlt
     * zu werden, haben sollen.
     *
     * @param alleTeilmengen     Alle Teilmengen des zugrunde liegenden
     *                           Problems.
     * @param wtInitEinheitlich  Gibt an, ob die Teilmengen initial mit der
     *                           gleichen Wahrscheinlichkeit ausgew�hlt werden
     *                           sollen. Falls nicht, h�ngt diese von der
     *                           Anzahl der enthaltenen Elemente ab.
     * @param prodVerfahren      Gibt an, ob das Produkt-Verfahren angewendet
     *                           werden soll. Falls nicht, wird das
     *                           Summen-Verfahren angewendet.
     */
    public ProbabilityRatings(ItmFamilie alleTeilmengen,
                              boolean wtInitEinheitlich, boolean prodVerfahren) {
        this(alleTeilmengen);
        this.wtInitEinheitlich = wtInitEinheitlich;
        this.prodVerfahren = prodVerfahren;
    }

    /**
     * Legt als initiale Wahrscheinlichkeit f�r jede der �bergebenen
     * Teilmengen einen einheitlichen Wert fest.
     *
     * @param teilmengen  Die Liste von Teilmengen, deren Wahrscheinlichkeit
     *                    gesch�tzt werden soll.
     * @param negiert     Gibt an, ob die einzelnen Werte negiert werden
     *                    sollen.
     *
     * @return  Ein Array mit den festgelegten initialen Wahrscheinlichkeiten.
     */
    private float[] enthaltenWktInit(List teilmengen, boolean negiert) {
        float[] ergebnis;
        float   ergebnisWert;
        int     anzahlTeilmengen;

        anzahlTeilmengen = teilmengen.size();
        ergebnis = new float[anzahlTeilmengen];

        if (negiert) {
            ergebnisWert = -1.0f / anzahlTeilmengen;
        } else {
            ergebnisWert = 1.0f / anzahlTeilmengen;
        }

        for (int itmNr = 0; itmNr < anzahlTeilmengen; itmNr++) {
            ergebnis[itmNr] = ergebnisWert;
        }

        return ergebnis;
    }

    /**
     * Legt als initiale Werte f�r die Summen der Wahrscheinlichkeiten der
     * Teilmengen zu den einzelnen Elementen einen einheitlichen Wert fest.
     *
     * @param groesseGesamtmenge  Die Gr��e der Grundmenge, d.h. die
     *                            Gesamtanzahl der Elemente.
     *
     * @return  Ein Array mit den festgelegten initialen Werten.
     */
    private float[] wktSummenInit(int groesseGesamtmenge) {
        float[] wktSummenInit = new float[groesseGesamtmenge];

        for (int e = 0; e < groesseGesamtmenge; e++) {
            wktSummenInit[e] = 1;
        }
        return wktSummenInit;
    }

    /**
     * Liefert den Funktionswert zum Wert der Relation aus allgemeiner
     * Wahrscheinlichkeit und Kosten einer Teilmenge.
     *
     * @param relWert  Wert der Relation aus allgemeiner Wahrscheinlichkeit
     *                 und Kosten einer Teilmenge.
     *
     * @return  Den Funktionswert, konkret eine Potenz, des �bergebenen
     *          Wertes.
     */
    private float relFunkWert(float relWert) {
        if (relExponent == 1.0f) {
            return relWert;
        } else if (relExponent == 2.0f) {
            return relWert * relWert;
        } else {
            return (float) Math.pow(relWert, relExponent);
        }
    }

    /**
     * Berechnet f�r jedes Element e in der Grundmenge die Summe der Werte
     * von enthaltenWkt von den Teilmengen, in denen e enthalten ist.
     *
     * @param enthaltenWktAlt     Die Wahrscheinlichkeiten f�r jede Teilmenge,
     *                            dass sie in einer minimalen �berdeckung
     *                            vorkommt.
     * @param hinzuKosten         Die Kosten f�r das Hinzuf�gen der Teilmengen
     *                            zur partiellen �berdeckung.
     * @param teilmengen          Die Teilmengen, die in der L�sung vorkommen
     *                            k�nnen.
     * @param groesseGesamtmenge  Die Gr��e der Grundmenge, d.h. die
     *                            Gesamtanzahl der Elemente.
     *
     * @return  Ein Array mit dem Wert zu jedem Element.
     */
    private float[] wktSummen(float[] enthaltenWktAlt, float[] hinzuKosten,
                              List teilmengen, int groesseGesamtmenge) {
        IndexTeilmenge teilmenge;
        Iterator       iterator;
        float[]        wktSummen;
        float          relFunkWert;

        wktSummen = new float[groesseGesamtmenge];
        for (int e = 0; e < groesseGesamtmenge; e++) {
            wktSummen[e] = 0;
        }

        iterator = teilmengen.iterator();
        for (int t = 0; t < enthaltenWktAlt.length; t++) {

            if (hinzuKosten[t] == 0) {
                relFunkWert = Float.MAX_VALUE / enthaltenWktAlt.length;
            } else {
                relFunkWert = relFunkWert(enthaltenWktAlt[t] / hinzuKosten[t]);
            }

            teilmenge = (IndexTeilmenge) iterator.next();
            for (int e = teilmenge.kleinsterEnthaltenerIndex();
                 e >= 0;
                 e = teilmenge.naechsterEnthaltenerIndex(e + 1)) {

                wktSummen[e] += relFunkWert;
            }
        }

        for (int e = 0; e < groesseGesamtmenge; e++) {
            if (wktSummen[e] == 0) {
                wktSummen[e] = Float.MIN_VALUE;
            }
        }

        return wktSummen;
    }

    /**
     * Sch�tzt f�r jede der Teilmengen die Wahrscheinlichkeit, dass sie in
     * einer minimalen �berdeckung enthalten ist, unter Verwendung der Werte
     * von <code>wktSummen</code>. Tats�chlich wird diese Methode daf�r
     * eingesetzt, um aus den vorher berechneten Werten f�r enthaltenWkt neue
     * (und hoffentlich genauere) Werte zu berechnen.
     *
     * @param wktSummen        Die Summen der Wahrscheinlichkeiten f�r die
     *                         einzelnen Elemente.
     * @param enthaltenWktAlt  Die Wahrscheinlichkeiten der Teilmengen aus der
     *                         vorherigen Iteration.
     * @param hinzuKosten      Die Kosten f�r das Hinzuf�gen der Teilmengen
     *                         zur partiellen �berdeckung.
     * @param teilmengen       Die Teilmegen, deren Wahrscheinlichkeit, dass
     *                         sie in einer minimalen �berdeckung vorkommen,
     *                         berechnet werden soll.
     * @param partialCover     Die bereits vorhandene partielle �berdeckung
     *                         der Elemente.
     * @param prodFormel       Gibt an, ob die Produkt-Formel verwendet werden
     *                         soll. Falls nicht, wird die Summen-Formel
     *                         verwendet.
     *
     * @return  Neue Sch�tzwerte daf�r, dass die einzelnen Teilmengen in der
     *          minimimalen �berdeckung aufgenommen werden.
     *
     * @see #wktSummen
     */
    private float[] enthaltenWkt(float[] wktSummen, float[] enthaltenWktAlt,
                                 float[] hinzuKosten, List teilmengen,
                                 ItmFamilie partialCover, boolean prodFormel) {
        IndexTeilmenge teilmenge;
        Iterator       iterator;
        float[]        enthaltenWkt;
        float          produkt;
        float          summe;
        float          relFunkWert;
        float          ueberdeckWkt;
        int            anzahlTeilmengen = enthaltenWktAlt.length;

        enthaltenWkt = new float[anzahlTeilmengen];
        relFunkWert = 0;

        iterator = teilmengen.iterator();
        for (int t = 0; t < anzahlTeilmengen; t++) {

            if (hinzuKosten[t] != 0) {
                relFunkWert = relFunkWert(enthaltenWktAlt[t] / hinzuKosten[t]);
            }

            produkt = 1;
            summe = 0;
            teilmenge = (IndexTeilmenge) iterator.next();
            for (int e = teilmenge.kleinsterEnthaltenerIndex();
                 e >= 0;
                 e = teilmenge.naechsterEnthaltenerIndex(e + 1)) {

                if (!partialCover.indexIstUeberdeckt(e)) {
                    // ueberdeckWkt gibt die Wahrscheinlichkeit an, da� das
                    // Element e in der L�sung von der Teilmenge t �berdeckt
                    // wird, e also in t und t in der L�sung enthalten ist.
                    // Ersteres ist durch den vorhergehenden Vergleich schon
                    // sicher gestellt.
                    if (hinzuKosten[t] == 0) {
                        ueberdeckWkt = 1;
                    } else {
                        ueberdeckWkt = relFunkWert / wktSummen[e];
                    }
                    if (prodFormel) {
                        produkt *= (1 - ueberdeckWkt);
                    } else {
                        summe += ueberdeckWkt;
                    }
                }
            }

            if (prodFormel) {
                enthaltenWkt[t] = 1 - produkt;
            } else {
                enthaltenWkt[t] = summe;
            }
        }

        return enthaltenWkt;
    }

    /**
     * Ermittelt f�r jede �bergebene Teilmenge die Kosten, die bei deren
     * Hinzuf�gen zur Teil�berdeckung entstehen. Diese Werte werden noch durch
     * einen Wert geteilt, um die absolute Gr��e der Werte zu verringern.
     *
     * @param teilmengen    Die Teilmengen, deren Kosten beim Hinzuf�gen zur
     *                      Teil�berdeckung ermittelt werden sollen.
     * @param partialCover  Die bisherige Teil�berdeckung der Grundmenge.
     *
     * @return  Ein Array, in dem f�r jede Teilmenge Kosten f�r deren
     *          Hinzuf�gen zur Teil�berdeckung angegeben sind.
     */
    private float[] hinzufuegenKosten(List teilmengen, ItmFamilie partialCover) {
        IndexTeilmenge teilmenge;
        Iterator       iterator;
        float[]        hinzuKosten;
        float          maxKosten;
        int            nummer;

        hinzuKosten = new float[teilmengen.size()];

        maxKosten = 0;
        nummer = 0;
        iterator = teilmengen.iterator();
        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge) iterator.next();
            hinzuKosten[nummer] = partialCover.kostenHinzufuegen(teilmenge);
            if ((hinzuKosten[nummer] > maxKosten)
                    && (hinzuKosten[nummer] < Float.MAX_VALUE)) {
                maxKosten = hinzuKosten[nummer];
            }
            nummer++;
        }

        if (maxKosten > 0) {
            for (int nr = 0; nr < hinzuKosten.length; nr++) {
                hinzuKosten[nr] /= maxKosten;
            }
        }

        return hinzuKosten;
    }

    /**
     * Pr�ft ob die �bergebenen Teilmengen den richtigen Status haben, d.h.
     * ob sie entweder alle in der bisherigen �berdeckung enthalten sind oder
     * keine von ihnen enthalten ist.
     *
     * @param teilmengen       Die Teilmengen, deren Status ueberpr�ft werden
     *                         soll.
     * @param partialCover     Die bisherige Teil�berdeckung der Grundmenge.
     * @param korrekterStatus  Wenn <code>true</code>, dann m�ssen alle
     *                         Teilmengen enthalten sein, ansonsten darf keine
     *                         davon enthalten sein.
     *
     * @return  Gibt <code>true</code> zur�ck, falls alle Teilmengen die
     *          richtige Beziehung zur bisherigen �berdeckung besitzen.
     */
    private boolean enthaltStatusRichtig(List teilmengen, ItmFamilie partialCover,
                                         boolean korrekterStatus) {
        IndexTeilmenge teilmenge;
        Iterator       iterator;

        iterator = teilmengen.iterator();
        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge) iterator.next();
            if (partialCover.enthaelt(teilmenge) != korrekterStatus) {
                return false;
            }
        }

        return true;
    }

    /**
     * Ermittelt, ob bei Verwendung des Produkt-Verfahrens einer der Werte
     * gleich Eins ist. Bei Verwendung des Summen-Verfahrens wird immer
     * der Wert <CODE>false</CODE> geliefert.
     *
     * @param werte  Die Werte, die gepr�ft werden.
     *
     * @return  <CODE>true</CODE>, wenn einer der Werte gleich Eins ist und
     *          das Produkt-Verfahren verwendet wird, sonst <CODE>false</CODE>.
     */
    private boolean einWertIstEins(float[] werte) {
        if (!prodVerfahren) {
            return false;
        } else {
            for (int i = 0; i < werte.length; i++) {
                if (werte[i] == 1.0f) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Liefert die Bewertungen von Teilmengen zur Erg�nzung der �bergegebenen
     * partielle �berdeckung. Umso gr��er die Werte sind, umso besser sind die
     * Teilmengen bewertet, d.h. umso eher sollten sie zu partialCover
     * hinzugenommen werden.
     *
     * @param  teilmengen    Die Teilmengen, die bewertet werden sollen.
     * @param  partialCover  Die partielle �berdeckung, in Bezug auf die die
     *                       Teilmengen bewertet werden sollen.
     *
     * @return  Die Bewertungen der Teilmengen, die gr��er oder gleich Null
     *          sind.
     *
     * @throws IllegalArgumentException  Eine der �bergebenen Teilmengen ist
     *                                   schon in <code>partialCover</code>
     *                                   enthalten.
     */
    public float[] wAdd(List teilmengen, ItmFamilie partialCover) {
        float[] hinzuKosten;
        float[] enthaltenWkt = null;
        float[] wktSummen = null;
        float[] enthaltenWktAlt = null;
        int     groesseGesamtmenge = partialCover.groesseGesamtmenge();

        if (!enthaltStatusRichtig(teilmengen, partialCover, false)) {
            throw new IllegalArgumentException("Teilmenge schon enthalten");
        }

        hinzuKosten = hinzufuegenKosten(teilmengen, partialCover);

        enthaltenWkt = enthaltenWktInit(teilmengen, false);
        if (!wtInitEinheitlich) {
            // Die initialen Werte f�r enthaltenWkt ergeben sich jetzt f�r
            // jede Teilmenge ausschlie�lich aus der Anzahl der Elemente der
            // Teilmenge.
            wktSummen = wktSummenInit(groesseGesamtmenge);
            enthaltenWkt = enthaltenWkt(wktSummen, enthaltenWkt, hinzuKosten,
                                        teilmengen, partialCover, prodVerfahren);
        }

        for (int i = 1; i <= anzahlIterationen; i++) {
            if (!einWertIstEins(enthaltenWkt)) {
                enthaltenWktAlt = (float[]) enthaltenWkt.clone();
                wktSummen = wktSummen(enthaltenWkt, hinzuKosten, teilmengen,
                                      groesseGesamtmenge);
                enthaltenWkt = enthaltenWkt(wktSummen, enthaltenWkt, hinzuKosten,
                                            teilmengen, partialCover, prodVerfahren);

                // D�mpfung der Ver�nderungen.
                for (int t = 0; t < teilmengen.size(); t++) {
                    enthaltenWkt[t] = daempfungsFaktor * enthaltenWktAlt[t]
                                      + (1 - daempfungsFaktor) * enthaltenWkt[t];
                }
            }
        }

        return enthaltenWkt;
    }

    /**
     * Liefert die Bewertungen von Teilmengen zur Entfernung aus der
     * �bergegebenen partiellen �berdeckung. Umso gr��er die Werte sind (d.h.
     * umso kleiner die Betr�ge der Werte sind), umso schlechter sind die
     * Teilmengen bewertet, d.h. umso eher sollten sie aus partialCover
     * entfernt werden.
     *
     * @param  teilmengen    Die Teilmengen, die bewertet werden sollen.
     * @param  partialCover  Die partielle �berdeckung, in Bezug auf die die
     *                       Teilmengen bewertet werden sollen.
     *
     * @return  Die Bewertung der Teilmenge, die kleiner oder gleich Null
     *          ist.
     *
     * @throws IllegalArgumentException  Eine der �bergebenen Teilmengen ist
     *                                   schon in <code>partialCover</code>
     *                                   enthalten.
     */
    public float[] wRmv(List teilmengen, ItmFamilie partialCover) {

        if (!enthaltStatusRichtig(teilmengen, partialCover, true)) {
            throw new IllegalArgumentException("Teilmenge nicht enthalten");
        }

        return enthaltenWktInit(teilmengen, true);
    }
}

