/*
 * Dateiname      : SumDoppelItmFamilie.java
 * Letzte Änderung: 25. Juli 2006
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


package mengenUeberdeckung.doppelstruktur;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.heuristiken.costs.DoubleIndicesCosts;
import mengenUeberdeckung.heuristiken.costs.FixedExistentCosts;

/**
 * Repräsentiert eine Familie von Teilmengen einer Indexmenge, wobei den
 * Teilmengen jeweils eine zweite Teilmenge zugeordnet ist, die zur Berechnung
 * der Kosten verwendet wird. Die Kosten der Familie ergeben sich als Summe
 * der Kosten der enthaltenen Teilmengen selbst und der Kosten der
 * Kosten-Indices, die von mindestens einer Teilmenge überdeckt werden.
 *
 * @author  Dietmar Lippold
 */
public class SumDoppelItmFamilie extends DoppelItmFamilie implements Cloneable {

    /**
     * Erzeugt eine Instanz, deren Elemente Doppel-Index-Menge der übergebenen
     * Größe sind. Eine maximale Anzahl von Teilmengen wird nicht vorgegeben.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die den
     *                            Teilmengen der neuen Familie zugrunde
     *                            liegt.
     * @param indicesCosts        Die Instanz zur Ermittlung der Kosten der
     *                            überdeckten Kosten-Indices dieser Familie
     *                            oder einer hinzuzufügenden oder zu
     *                            entfernenden Doppel-Teilmenge.
     */
    public SumDoppelItmFamilie(int groesseGesamtmenge,
                               DoubleIndicesCosts indicesCosts) {
        super(groesseGesamtmenge, indicesCosts);
    }

    /**
     * Erzeugt eine Familie, die zu <CODE>this</CODE> strukturell gleich ist.
     * Sie enthält aber noch keine Teilmenge.
     *
     * @param andereFamilie    Eine Familie, zu der die neue Familie
     *                         strukturell gleich sein soll.
     * @param festeVerwaltung  Gibt an, ob die Teilmengen unbedingt in einer
     *                         festen Teilmengenverwaltung gespeichert werden
     *                         sollen. Falls nicht, wird die Art der
     *                         Verwaltung von der übergebenen Familie
     *                         übernommen. Bei einer festen Verwaltung bildet
     *                         die übergebene Familie deren Grundlage.
     */
    protected SumDoppelItmFamilie(SumDoppelItmFamilie andereFamilie,
                                  boolean festeVerwaltung) {
        super(andereFamilie, festeVerwaltung);
    }

    /**
     * Erzeugt eine Familie, deren Werte von der übergebenen Teilmenge kopiert
     * werden. Bei den enthaltenen Teilmengen und bei den Index-Kosten handelt
     * es sich aber um die gleichen Instanzen.
     *
     * @param andereFamilie  Eine Familie, deren Werte in die neue Instanz
     *                       kopiert werden.
     */
    protected SumDoppelItmFamilie(SumDoppelItmFamilie andereFamilie) {
        super(andereFamilie);
    }

    /**
     * Liefert ein neue Instanz, die zu <CODE>this</CODE> strukturell gleich
     * ist. Sie enthält aber noch keine Teilmenge.
     *
     * @param festeVerwaltung  Gibt an, ob die Teilmengen unbedingt in einer
     *                         festen Teilmengenverwaltung gespeichert werden
     *                         sollen. Falls nicht, wird die Art der
     *                         Verwaltung von der übergebenen Familie
     *                         übernommen. Bei einer festen Verwaltung bildet
     *                         die übergebene Familie deren Grundlage.
     *
     * @return  Ein zu diesem Objekt strukturell gleiches Objekt aber ohne
     *          enthaltene Teilmengen.
     */
    public ItmFamilie neueInstanz(boolean festeVerwaltung) {
        return (new SumDoppelItmFamilie(this, festeVerwaltung));
    }

    /**
     * Liefert ein neues Objekt, das zu <CODE>this</CODE> gleich ist. Die
     * Menge der enthaltenen Teilmengen wird dabei neu erzeugt. Bei den
     * enthaltenen Teilmengen handelt es sich aber um die gleichen Instanzen.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return (new SumDoppelItmFamilie(this));
    }

    /**
     * Liefert die Kosten der enthaltenen Teilmengen. Das ist die Summe aus
     * den allgemeinen Kosten der Teilmengen und den Kosten für die
     * Kosten-Indices, die von mindestens einer Teilmenge überdeckt werden.
     *
     * @return  Die Kosten der enthaltenen Teilmengen.
     */
    public float kostenFamilie() {
        return (kostenFamilieEinfach() + indexKostenGesamt());
    }

    /**
     * Liefert den Wert, um den sich die Kosten der Familie durch Hinzufügen
     * der übergebenen Teilmenge erhöhen. Dies ist die Summe aus den
     * allgemeinen Kosten der Teilmenge und den Kosten der Kosten-Indices, die
     * von der Teilmenge überdeckt werden.
     *
     * @param teilmenge  Die Teilmenge, für die die Kosten durch Hinzufügen zur
     *                   Familie ermittelt werden sollen.
     *
     * @return  Der Wert, um den sich die Kosten der Familie durch Hinzufügen
     *          der übergebenen Teilmenge erhöhen. Wenn die übergebene
     *          Teilmenge schon in der Familie enthalten ist, wird der Wert
     *          Null geliefert.
     *
     * @throws IllegalArgumentException  Die übergebene Teilmenge ist nicht vom
     *                                   Typ <CODE>DoppelIndexTeilmenge</CODE>.
     */
    public float kostenHinzufuegen(IndexTeilmenge teilmenge) {
        DoppelIndexTeilmenge doppelTeilmenge;
        float                itmIndexKosten;

        if (!(teilmenge instanceof DoppelIndexTeilmenge)) {
            throw new IllegalArgumentException();
        }

        if (enthaelt(teilmenge)) {
            return 0;
        }

        doppelTeilmenge = (DoppelIndexTeilmenge) teilmenge;
        itmIndexKosten = indicesCosts().addCosts(doppelTeilmenge,
                                                 kostenIndexUeberdeckAnz());
        return (kostenHinzufuegenEinfach(teilmenge) + itmIndexKosten);
    }

    /**
     * Liefert den Wert, um den sich die Kosten der Familie durch Entfernen
     * der übergebenen Teilmenge verringern. Dies ist die Summe aus den
     * allgemeinen Kosten der Teilmenge und den Kosten der Kosten-Indices, die
     * von der Teilmenge überdeckt werden.
     *
     * @param teilmenge  Die Teilmenge, für die die Kosten durch Entfernen aus
     *                   der Familie ermittelt werden sollen.
     *
     * @return  Der Wert, um den sich die Kosten der Familie durch Entfernen
     *          der übergebenen Teilmenge verringern. Wenn die übergebene
     *          Teilmenge nicht in der Familie enthalten ist, wird der Wert
     *          Null geliefert.
     *
     * @throws IllegalArgumentException  Die übergebene Teilmenge ist nicht vom
     *                                   Typ <CODE>DoppelIndexTeilmenge</CODE>.
     */
    public float kostenEntfernen(IndexTeilmenge teilmenge) {
        DoppelIndexTeilmenge doppelTeilmenge;
        float                itmIndexKosten;

        if (!(teilmenge instanceof DoppelIndexTeilmenge)) {
            throw new IllegalArgumentException();
        }

        if (!enthaelt(teilmenge)) {
            return 0;
        }

        doppelTeilmenge = (DoppelIndexTeilmenge) teilmenge;
        itmIndexKosten = indicesCosts().rmvCosts(doppelTeilmenge,
                                                 kostenIndexUeberdeckAnz());
        return (kostenEntfernenEinfach(teilmenge) + itmIndexKosten);
    }

    /**
     * Testet einige Prozeduren mittels Hinzunahme und Entfernen mehrerer
     * Teilmengen.
     *
     * @param args  Ein leeres Array von Standard-Argumenten.
     */
    public static void main(String[] args) {
        SumDoppelItmFamilie doppelItmFamilie;
        DoubleIndicesCosts  indexKostenFunktionen;
        float[]             testIndexKosten;
        int                 groesseGesamtmenge;

        groesseGesamtmenge = 6;
        testIndexKosten = new float[groesseGesamtmenge];
        for (int i = 0; i < groesseGesamtmenge; i++) {
            testIndexKosten[i] = 1;
        }
        indexKostenFunktionen = new FixedExistentCosts(testIndexKosten,
                                                       groesseGesamtmenge);
        doppelItmFamilie = new SumDoppelItmFamilie(groesseGesamtmenge,
                                                   indexKostenFunktionen);
        test(doppelItmFamilie);
    }
}

