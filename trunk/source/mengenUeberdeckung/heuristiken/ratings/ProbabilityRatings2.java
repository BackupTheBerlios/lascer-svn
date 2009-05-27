/*
 * Dateiname      : ProbabilityRatings2.java
 * Letzte Änderung: 25. Juli 2006
 * Autoren        : Edgar Binder, Dietmar Lippold
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


package mengenUeberdeckung.heuristiken.ratings;

import java.util.List;
import java.util.Iterator;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Dient der Bewertung von Teilmengen zur Aufnahme in oder zur Entfernung aus
 * einer partiellen Überdeckung, indem ein Konvergenzverfahren verwendet wird,
 * das die Wahrscheinlichkeiten des Vorkommens der Teilmengen in der Lösung
 * berücksichtigt.
 *
 * TODO: - die Methode wRmv ist noch nicht korrekt implementiert, weil darin
 *         benötigte Information bisher gefehlt hat -> es werden nur die
 *         Teilmengen übergeben, die entfernt werden können.
 *       - Optimierung der Konstanten anzahlIterationen, daempfungsFaktor und
 *         relExponent.
 *
 * @author  Edgar Binder, Dietmar Lippold
 */
public class ProbabilityRatings2 implements CandidateRatings {

    /**
     * Die Anzahl der durchzuführenden Iterationen.
     */
    private int anzahlIterationen = 15;

    /**
     * Der Faktor, mit dem die Veränderungen der Wahrscheinlichkeiten der
     * Teilmengen gedämpft werden.
     */
    private float daempfungsFaktor = 0.0f;

    /**
     * Der Wert, mit dem die Relation des Nutzens und der Kosten einer
     * Teilmenge zur Ermittlung ihrer Wahrscheinlichkeit potenziert wird.
     */
    private float relExponent = 10;

    /**
     * Erzeugt eine neue Instanz und legt die Anzahl der Iterationen im
     * Wahrscheinlichkeitkonvergenzverfahren fest.
     *
     * @param alleTeilmengen  Alle Teilmengen des zugrunde liegenden Problems.
     */
    public ProbabilityRatings2(ItmFamilie alleTeilmengen) {
    }

    /**
     * Legt einen initialen Wert für die Kosten der einzelnen Elemente fest.
     *
     * @param groesseGesamtmenge  Die Größe der Grundmenge, d.h. die
     *                            Gesamtanzahl der Elemente.
     *
     * @return  Ein Array mit den festgelegten initialen Werten, die gleich
     *          sind.
     */
    private float[] elementeKostenInit(int groesseGesamtmenge) {
        float[] elementeKostenInit = new float[groesseGesamtmenge];

        for (int e = 0; e < groesseGesamtmenge; e++) {
            elementeKostenInit[e] = 1;
        }
        return elementeKostenInit;
    }

    /**
     * Ermittelt den Nutzen der Teilmengen aus den Kosten der Elemente, die
     * sie jeweils enthalten.
     *
     * @param elementeKosten  Die Kosten der einzelnen Elemente.
     * @param teilmengen      Die Liste von Teilmengen, deren Nutzen berechnet
     *                        werden soll.
     * @param partialCover    Die bereits vorhandene partielle Überdeckung der
     *                        Elemente.
     *
     * @return  Ein Array mit den Werten des Nutzens der einzelnen Teilmengen.
     */
    private float[] teilmengenNutzen(float[] elementeKosten, List teilmengen,
                                     ItmFamilie partialCover) {
        IndexTeilmenge teilmenge;
        Iterator       iterator;
        float[]        teilmengenNutzen;
        float          summe;
        int            anzahlTeilmengen = teilmengen.size();

        teilmengenNutzen = new float[anzahlTeilmengen];

        iterator = teilmengen.iterator();
        for (int t = 0; t < anzahlTeilmengen; t++) {
            teilmenge = (IndexTeilmenge) iterator.next();

            summe = 0;
            for (int e = teilmenge.kleinsterEnthaltenerIndex();
                 e >= 0;
                 e = teilmenge.naechsterEnthaltenerIndex(e + 1)) {

                if (!partialCover.indexIstUeberdeckt(e)) {
                    summe += elementeKosten[e];
                }
            }

            teilmengenNutzen[t] = summe;
        }

        return teilmengenNutzen;
    }

    /**
     * Liefert den Funktionswert zum Wert der Relation aus Nutzen und Kosten
     * einer Teilmenge.
     *
     * @param relWert  Wert der Relation aus Nutzen und Kosten einer
     *                 Teilmenge.
     *
     * @return  Den Funktionswert, konkret eine Potenz, des übergebenen
     *          Wertes.
     */
    private float relFunkWert(float relWert) {
        return (float) Math.pow(relWert, relExponent);
    }

    /**
     * Ermittelt für jedes Element die Summe der Werte der Relation aus dem
     * Nutzen und den Kosten der einzelnen Teilmengen, die das Element
     * enthalten.
     *
     * @param teilmengenNutzen  Der Nutzen der einzelnen Teilmengen.
     * @param teilmengen        Die Liste der Teilmengen.
     * @param partialCover      Die bereits vorhandene partielle Überdeckung
     *                          der Elemente.
     *
     * @return  Ein Array mit den Summen der Werte der Nutzen-Kosten-Relation
     *          der Teilmengen zu jedem Element.
     */
    private float[] elementeRelSummen(float[] teilmengenNutzen, List teilmengen,
                                      ItmFamilie partialCover) {
        IndexTeilmenge teilmenge;
        Iterator       iterator;
        float[]        elementeRelSummen;
        float          kosten;
        float          relFunkWert;
        int            groesseGesamtmenge = partialCover.groesseGesamtmenge();

        elementeRelSummen = new float[groesseGesamtmenge];
        for (int e = 0; e < groesseGesamtmenge; e++) {
            elementeRelSummen[e] = 0;
        }

        iterator = teilmengen.iterator();
        for (int t = 0; t < teilmengenNutzen.length; t++) {

            teilmenge = (IndexTeilmenge) iterator.next();
            kosten = partialCover.kostenHinzufuegen(teilmenge);
            if (kosten == 0) {
                relFunkWert = Float.MAX_VALUE / teilmengenNutzen.length;
            } else {
                relFunkWert = relFunkWert(teilmengenNutzen[t] / kosten);
            }

            for (int e = teilmenge.kleinsterEnthaltenerIndex();
                 e >= 0;
                 e = teilmenge.naechsterEnthaltenerIndex(e + 1)) {

                elementeRelSummen[e] += relFunkWert;
            }
        }

        for (int e = 0; e < groesseGesamtmenge; e++) {
            if (elementeRelSummen[e] == 0) {
                elementeRelSummen[e] = Float.MIN_VALUE;
            }
        }

        return elementeRelSummen;
    }

    /**
     * Schätzt für jede der Teilmengen die Wahrscheinlichkeit, dass sie in
     * einer minimalen Überdeckung enthalten ist, unter Verwendung des Nutzens
     * der Teilmengen und der Summen der der Relationen aus Nutzen und Kosten
     * der Teilmengen.
     *
     * @param teilmengenNutzen   Der Nutzen der einzelnen Teilmengen.
     * @param elementeRelSummen  Die Summen der der Relationen aus Nutzen und
     *                           Kosten der Teilmengen.
     * @param teilmengen         Die Liste der Teilmengen.
     * @param partialCover       Die bereits vorhandene partielle Überdeckung
     *                           der Elemente.
     *
     * @return  Neue Schätzwerte dafür, dass die einzelnen Teilmengen in der
     *          minimimalen Überdeckung enthalten sind.
     *
     * @see #elementeRelSummen
     */
    private float[] enthaltenWkt(float[] teilmengenNutzen,
                                 float[] elementeRelSummen,
                                 List teilmengen, ItmFamilie partialCover) {
        IndexTeilmenge teilmenge;
        Iterator       iterator;
        float[]        enthaltenWkt;
        float          kosten;
        float          relFunkWert;
        float          produkt;
        float          ueberdeckWkt;
        int            anzahlTeilmengen = teilmengen.size();

        enthaltenWkt = new float[anzahlTeilmengen];
        relFunkWert = 0;

        iterator = teilmengen.listIterator();
        for (int t = 0; t < anzahlTeilmengen; t++) {
            teilmenge = (IndexTeilmenge) iterator.next();
            kosten = partialCover.kostenHinzufuegen(teilmenge);
            if (kosten != 0) {
                relFunkWert = relFunkWert(teilmengenNutzen[t] / kosten);
            }

            produkt = 1;
            for (int e = teilmenge.kleinsterEnthaltenerIndex();
                 e >= 0;
                 e = teilmenge.naechsterEnthaltenerIndex(e + 1)) {

                if (!partialCover.indexIstUeberdeckt(e)) {
                    // ueberdeckWkt gibt die Wahrscheinlichkeit an, daß das
                    // Element e in der Lösung von der Teilmenge t überdeckt
                    // wird, e also in t und t in der Lösung enthalten ist.
                    // Ersteres ist durch den vorhergehenden Vergleich schon
                    // sicher gestellt.
                    if (kosten == 0) {
                        ueberdeckWkt = 1;
                    } else {
                        ueberdeckWkt = relFunkWert / elementeRelSummen[e];
                    }
                    produkt *= (1 - ueberdeckWkt);
                }
            }

            enthaltenWkt[t] = 1 - produkt;
        }

        return enthaltenWkt;
    }

    /**
     * Liefert die Kosten für die einzelnen Elemente auf Basis der
     * Wahrscheinlichkeiten, mit denen die Teilmengen in der Lösung enthalten
     * sind, und auf Basis der Kosten und der Anzahl der Elemente der
     * Teilmengen.
     *
     * @param enthaltenWkt  Die Wahrscheinlichkeiten, mit denen die Teilmengen
     *                      in der Lösung enthalten sind.
     * @param teilmengen    Die Liste der Teilmengen.
     * @param partialCover  Die bereits vorhandene partielle Überdeckung der
     *                      Elemente.
     *
     * @return  Die Kosten für die einzelnen Elemente.
     */
    private float[] elementeKosten(float[] enthaltenWkt, List teilmengen,
                                   ItmFamilie partialCover) {
        IndexTeilmenge teilmenge;
        Iterator       iterator;
        float[]        elementeKosten;
        float          kosten;
        int            groesseGesamtmenge = partialCover.groesseGesamtmenge();
        int            groesseTeilmenge;

        elementeKosten = new float[groesseGesamtmenge];
        for (int e = 0; e < groesseGesamtmenge; e++) {
            elementeKosten[e] = 0;
        }

        iterator = teilmengen.iterator();
        for (int t = 0; t < enthaltenWkt.length; t++) {

            teilmenge = (IndexTeilmenge) iterator.next();
            kosten = partialCover.kostenHinzufuegen(teilmenge);
            groesseTeilmenge = teilmenge.groesseTeilmenge();

            for (int e = teilmenge.kleinsterEnthaltenerIndex();
                 e >= 0;
                 e = teilmenge.naechsterEnthaltenerIndex(e + 1)) {

                elementeKosten[e] += enthaltenWkt[t] * kosten / groesseTeilmenge;
            }
        }

        return elementeKosten;
    }

    /**
     * Liefert die einheitlichen negierten Wahrscheinlichkeiten für die
     * Entfernung einer der Teilmengen aus der partiellen Überdeckung fest.
     *
     * @param teilmengen  Die Liste von Teilmengen, zu denen die Werte
     *                    geliefert werden sollen.
     *
     * @return  Ein Array mit den festgelegten initialen Wahrscheinlichkeiten.
     */
    private float[] rmvWkt(List teilmengen) {
        float[] rmvWkt;
        float   ergebnisWert;
        int     anzahlTeilmengen;

        anzahlTeilmengen = teilmengen.size();
        rmvWkt = new float[anzahlTeilmengen];

        ergebnisWert = -1.0f / anzahlTeilmengen;

        for (int itmNr = 0; itmNr < anzahlTeilmengen; itmNr++) {
            rmvWkt[itmNr] = ergebnisWert;
        }

        return rmvWkt;
    }

    /**
     * Prüft ob die übergebenen Teilmengen den richtigen Status haben, d.h.
     * ob sie entweder alle in der bisherigen Überdeckung enthalten sind oder
     * keine von ihnen enthalten ist.
     *
     * @param teilmengen       Die Teilmengen, deren Status ueberprüft werden
     *                         soll.
     * @param partialCover     Die bisherige Teilüberdeckung der Grundmenge
     * @param korrekterStatus  Wenn <code>true</code>, dann müssen alle
     *                         Teilmengen enthalten sein, ansonsten darf keine
     *                         davon enthalten sein.
     *
     * @return  Gibt <code>true</code> zurück, falls alle Teilmengen die
     *          richtige Beziehung zur bisherigen Überdeckung besitzen.
     */
    private boolean enthaltStatusRichtig(List teilmengen, ItmFamilie partialCover,
                                         boolean korrekterStatus) {
        IndexTeilmenge teilmenge;
        Iterator       iterator;

        iterator = teilmengen.listIterator();
        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge) iterator.next();
            if (partialCover.enthaelt(teilmenge) != korrekterStatus) {
                return false;
            }
        }

        return true;
    }

    /**
     * Ermittelt, ob einer der Werte gleich Eins ist.
     *
     * @param werte  Die Werte, die geprüft werden.
     *
     * @return  <CODE>true</CODE>, wenn einer der Werte gleich Eins ist, sonst
     *          <CODE>false</CODE>.
     */
    private boolean einWertIstEins(float[] werte) {
        for (int i = 0; i < werte.length; i++) {
            if (werte[i] == 1.0f) {
                return true;
            }
        }
        return false;
    }

    /**
     * Liefert die Bewertungen von Teilmengen zur Ergänzung der übergegebenen
     * partielle Überdeckung. Umso größer die Werte sind, umso besser sind die
     * Teilmengen bewertet, d.h. umso eher sollten sie zu partialCover
     * hinzugenommen werden.
     *
     * @param  teilmengen    Die Teilmengen, die bewertet werden sollen.
     * @param  partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                       Teilmengen bewertet werden sollen.
     *
     * @return  Die Bewertungen der Teilmengen, die größer oder gleich Null
     *          sind.
     *
     * @throws IllegalArgumentException  Eine der übergebenen Teilmengen ist
     *                                   schon in <code>partialCover</code>
     *                                   enthalten.
     */
    public float[] wAdd(List teilmengen, ItmFamilie partialCover) {
        float[] elementeKosten = null;
        float[] teilmengenNutzen = null;
        float[] elementeRelSummen = null;
        float[] enthaltenWkt = null;
        float[] enthaltenWktAlt = null;

        if (!enthaltStatusRichtig(teilmengen, partialCover, false)) {
            throw new IllegalArgumentException("Teilmenge schon enthalten");
        }

        elementeKosten = elementeKostenInit(partialCover.groesseGesamtmenge());

        for (int i = 1; i <= anzahlIterationen; i++) {
            if ((enthaltenWkt == null) || !einWertIstEins(enthaltenWkt)) {
                teilmengenNutzen = teilmengenNutzen(elementeKosten, teilmengen,
                                                    partialCover);
                elementeRelSummen = elementeRelSummen(teilmengenNutzen,
                                                      teilmengen, partialCover);
                enthaltenWkt = enthaltenWkt(teilmengenNutzen, elementeRelSummen,
                                            teilmengen, partialCover);

                // Dämpfung der Veränderungen.
                if (enthaltenWktAlt != null) {
                    for (int t = 0; t < teilmengen.size(); t++) {
                        enthaltenWkt[t] = daempfungsFaktor * enthaltenWktAlt[t]
                                          + (1 - daempfungsFaktor) * enthaltenWkt[t];
                    }
                }
                enthaltenWktAlt = (float[]) enthaltenWkt.clone();

                elementeKosten = elementeKosten(enthaltenWkt, teilmengen,
                                                partialCover);
            }
        }

        return enthaltenWkt;
    }

    /**
     * Liefert die Bewertungen von Teilmengen zur Entfernung aus der
     * übergegebenen partiellen Überdeckung. Umso größer die Werte sind (d.h.
     * umso kleiner die Beträge der Werte sind), umso schlechter sind die
     * Teilmengen bewertet, d.h. umso eher sollten sie aus partialCover
     * entfernt werden.
     *
     * @param  teilmengen    Die Teilmengen, die bewertet werden sollen.
     * @param  partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                       Teilmengen bewertet werden sollen.
     *
     * @return  Die Bewertung der Teilmenge, die kleiner oder gleich Null
     *          ist.
     *
     * @throws IllegalArgumentException  Eine der übergebenen Teilmengen ist
     *                                   schon in <code>partialCover</code>
     *                                   enthalten.
     */
    public float[] wRmv(List teilmengen, ItmFamilie partialCover) {

        if (!enthaltStatusRichtig(teilmengen, partialCover, true)) {
            throw new IllegalArgumentException("Teilmenge nicht enthalten");
        }

        return rmvWkt(teilmengen);
    }
}

