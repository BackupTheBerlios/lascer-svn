/*
 * Dateiname      : QuotDoppelItmFamilie.java
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


package mengenUeberdeckung.doppelstruktur;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.heuristiken.costs.FrequencyCosts;
import mengenUeberdeckung.heuristiken.costs.FixedExistentCosts;

/**
 * Repr�sentiert eine Familie von Teilmengen einer Indexmenge, wobei den
 * Teilmengen jeweils eine zweite Teilmenge zugeordnet ist, die zur Berechnung
 * der Kosten verwendet wird. Die Kosten der Familie ergeben sich als Quotient
 * der Kosten der enthaltenen Teilmengen selbst und der Kosten der
 * Kosten-Indices, die von keiner Teilmenge �berdeckt werden.
 *
 * @author  Dietmar Lippold
 */
public class QuotDoppelItmFamilie extends DoppelItmFamilie implements Cloneable {

    /**
     * Ein Wert, der gr��er als das Maximum der Kosten aller Kosten-Indices
     * ist.
     */
    private float indexKostenMaxPlus;

    /**
     * Erzeugt eine Instanz, deren Elemente Doppel-Index-Menge der �bergebenen
     * Gr��e sind. Eine maximale Anzahl von Teilmengen wird nicht vorgegeben.
     * Zur Berechnung der Kosten der Kosten-Indices einer Doppel-Teilmenge
     * oder einer Doppel-Familie wird die Klasse <CODE>ExistentCosts</CODE>
     * benutzt.
     *
     * @param groesseGesamtmenge  Die Gr��e der Gesamtmenge, die den
     *                            Teilmengen der neuen Familie zugrunde
     *                            liegt.
     * @param indexKosten         Die Kosten f�r die �berdeckung der einzelnen
     *                            Kosten-Indices.
     * @param nachkommaAnz        Die Anzahl der Nachkomma-Stellen, die bei
     *                            Berechnung der Kosten der Kosten-Indices
     *                            ber�cksichtigt werden sollen. Der Wert mu�
     *                            gr��er oder gleich Null sein.
     *
     * @see mengenUeberdeckung.heuristiken.costs.FixedExistentCosts
     */
    public QuotDoppelItmFamilie(int groesseGesamtmenge, float[] indexKosten,
                                int nachkommaAnz) {
        super(groesseGesamtmenge, new FixedExistentCosts(indexKosten,
                                                         nachkommaAnz));

        // Ermittlung des Wertes f�r indexKostenMaxPlus
        indexKostenMaxPlus = FrequencyCosts.festkommaSumme(indexKosten,
                                                           nachkommaAnz);
        if (indexKostenMaxPlus == 0) {
            // Die Summe der Kosten der Kosten-Indices wird auch nach
            // Hinzuf�gen von Teilmengen immer Null sein. Daher wird die
            // Maximalsumme auf Eins gesetzt, damit sp�ter die Differenz Null
            // ist.
            indexKostenMaxPlus = 1;
        } else {
            // Die Maximalsumme der Kosten der Kosten-Indices erh�hen, damit
            // die Differenz zur sp�teren Summe der Kosten positiv ist.
            indexKostenMaxPlus *= Konstanten.MAX_KOSTEN_FAKTOR;
        }
    }

    /**
     * Erzeugt eine Familie, die zu <CODE>this</CODE> strukturell gleich ist.
     * Sie enth�lt aber noch keine Teilmenge.
     *
     * @param andereFamilie    Eine Familie, zu der die neue Familie
     *                         strukturell gleich sein soll.
     * @param festeVerwaltung  Gibt an, ob die Teilmengen unbedingt in einer
     *                         festen Teilmengenverwaltung gespeichert werden
     *                         sollen. Falls nicht, wird die Art der
     *                         Verwaltung von der �bergebenen Familie
     *                         �bernommen. Bei einer festen Verwaltung bildet
     *                         die �bergebene Familie deren Grundlage.
     */
    protected QuotDoppelItmFamilie(QuotDoppelItmFamilie andereFamilie,
                                   boolean festeVerwaltung) {
        super(andereFamilie, festeVerwaltung);
        this.indexKostenMaxPlus = andereFamilie.indexKostenMaxPlus;
    }

    /**
     * Erzeugt eine Familie, deren Werte von der �bergebenen Teilmenge kopiert
     * werden. Bei den enthaltenen Teilmengen und bei den Index-Kosten handelt
     * es sich aber um die gleichen Instanzen.
     *
     * @param andereFamilie  Eine Familie, deren Werte in die neue Instanz
     *                       kopiert werden.
     */
    protected QuotDoppelItmFamilie(QuotDoppelItmFamilie andereFamilie) {
        super(andereFamilie);
        this.indexKostenMaxPlus = andereFamilie.indexKostenMaxPlus;
    }

    /**
     * Liefert ein neue Instanz, die zu <CODE>this</CODE> strukturell gleich
     * ist. Sie enth�lt aber noch keine Teilmenge.
     *
     * @param festeVerwaltung  Gibt an, ob die Teilmengen unbedingt in einer
     *                         festen Teilmengenverwaltung gespeichert werden
     *                         sollen. Falls nicht, wird die Art der
     *                         Verwaltung von der �bergebenen Familie
     *                         �bernommen. Bei einer festen Verwaltung bildet
     *                         die �bergebene Familie deren Grundlage.
     *
     * @return  Ein zu diesem Objekt strukturell gleiches Objekt aber ohne
     *          enthaltene Teilmengen.
     */
    public ItmFamilie neueInstanz(boolean festeVerwaltung) {
        return (new QuotDoppelItmFamilie(this, festeVerwaltung));
    }

    /**
     * Liefert ein neues Objekt, das zu <CODE>this</CODE> gleich ist. Die
     * Menge der enthaltenen Teilmengen wird dabei neu erzeugt. Bei den
     * enthaltenen Teilmengen handelt es sich aber um die gleichen Instanzen.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return (new QuotDoppelItmFamilie(this));
    }

    /**
     * Liefert die Kosten der enthaltenen Teilmengen. Das ist der Quotient aus
     * den allgemeinen Kosten der Teilmengen und den Kosten f�r die
     * Kosten-Indices, die von keiner Teilmenge �berdeckt werden.
     *
     * @return  Die Kosten der enthaltenen Teilmengen.
     */
    public float kostenFamilie() {
        float indexKostenDiff;

        indexKostenDiff = indexKostenMaxPlus - indexKostenGesamt();
        if (indexKostenDiff <= 0) {
            System.err.println("WARNUNG: Rechenungenauigkeit in Methode"
                               + " QuotDoppelItmFamilie.kosten");
            System.err.println("         Konstante MAX_KOSTEN_FAKTOR erh�hen");
            indexKostenDiff = (float) Math.sqrt(Float.MIN_VALUE);
        }

        return (kostenFamilieEinfach() / indexKostenDiff);
    }

    /**
     * Liefert den Wert, um den sich die Kosten der Familie durch Hinzuf�gen
     * der �bergebenen Teilmenge erh�hen. Dies ist die Summe aus den
     * allgemeinen Kosten der Teilmenge und den Kosten der Kosten-Indices, die
     * von der Teilmenge �berdeckt werden.
     *
     * @param teilmenge  Die Teilmenge, f�r die die Kosten durch Hinzuf�gen zur
     *                   Familie ermittelt werden sollen.
     *
     * @return  Der Wert, um den sich die Kosten der Familie durch Hinzuf�gen
     *          der �bergebenen Teilmenge erh�hen. Wenn die �bergebene
     *          Teilmenge schon in der Familie enthalten ist, wird der Wert
     *          Null geliefert.
     *
     * @throws IllegalArgumentException  Die �bergebene Teilmenge ist nicht vom
     *                                   Typ <CODE>DoppelIndexTeilmenge</CODE>.
     */
    public float kostenHinzufuegen(IndexTeilmenge teilmenge) {
        DoppelIndexTeilmenge doppelTeilmenge;
        float itmIndexKosten;
        float kostenNeu;
        float indexKostenDiffNeu;

        if (!(teilmenge instanceof DoppelIndexTeilmenge)) {
            throw new IllegalArgumentException();
        }

        if (enthaelt(teilmenge)) {
            return 0;
        }

        doppelTeilmenge = (DoppelIndexTeilmenge) teilmenge;
        itmIndexKosten = indicesCosts().addCosts(doppelTeilmenge,
                                                 kostenIndexUeberdeckAnz());

        indexKostenDiffNeu = (indexKostenMaxPlus
                              - (indexKostenGesamt() + itmIndexKosten));
        if (indexKostenDiffNeu <= 0) {
            System.err.println("WARNUNG: Rechenungenauigkeit in Methode"
                               + " QuotDoppelItmFamilie.hinzufuegenKosten");
            System.err.println("         Konstante MAX_KOSTEN_FAKTOR erh�hen");
            indexKostenDiffNeu = (float) Math.sqrt(Float.MIN_VALUE);
        }

        kostenNeu = ((kostenFamilieEinfach() + kostenHinzufuegenEinfach(teilmenge))
                     / indexKostenDiffNeu);

        return (kostenNeu - kostenFamilie());
    }

    /**
     * Liefert den Wert, um den sich die Kosten der Familie durch Entfernen
     * der �bergebenen Teilmenge verringern. Dies ist die Summe aus den
     * allgemeinen Kosten der Teilmenge und den Kosten der Kosten-Indices, die
     * von der Teilmenge �berdeckt werden.
     *
     * @param teilmenge  Die Teilmenge, f�r die die Kosten durch Entfernen aus
     *                   der Familie ermittelt werden sollen.
     *
     * @return  Der Wert, um den sich die Kosten der Familie durch Entfernen
     *          der �bergebenen Teilmenge verringern. Wenn die �bergebene
     *          Teilmenge nicht in der Familie enthalten ist, wird der Wert
     *          Null geliefert.
     *
     * @throws IllegalArgumentException  Die �bergebene Teilmenge ist nicht vom
     *                                   Typ <CODE>DoppelIndexTeilmenge</CODE>.
     */
    public float kostenEntfernen(IndexTeilmenge teilmenge) {
        DoppelIndexTeilmenge doppelTeilmenge;
        float itmIndexKosten;
        float kostenNeu;
        float indexKostenDiffNeu;

        if (!(teilmenge instanceof DoppelIndexTeilmenge)) {
            throw new IllegalArgumentException();
        }

        if (!enthaelt(teilmenge)) {
            return 0;
        }

        doppelTeilmenge = (DoppelIndexTeilmenge) teilmenge;
        itmIndexKosten = indicesCosts().rmvCosts(doppelTeilmenge,
                                                 kostenIndexUeberdeckAnz());

        indexKostenDiffNeu = (indexKostenMaxPlus
                              - (indexKostenGesamt() - itmIndexKosten));
        if (indexKostenDiffNeu <= 0) {
            System.err.println("WARNUNG: Rechenungenauigkeit in Methode"
                               + " QuotDoppelItmFamilie.entfernenKosten");
            System.err.println("         Konstante MAX_KOSTEN_FAKTOR erh�hen");
            indexKostenDiffNeu = (float) Math.sqrt(Float.MIN_VALUE);
        }

        kostenNeu = ((kostenFamilieEinfach() - kostenEntfernenEinfach(teilmenge))
                     / indexKostenDiffNeu);

        return (kostenFamilie() - kostenNeu);
    }

    /**
     * Testet einige Prozeduren mittels Hinzunahme und Entfernen mehrerer
     * Teilmengen.
     *
     * @param args  Ein leeres Array von Standard-Argumenten.
     */
    public static void main(String[] args) {
        QuotDoppelItmFamilie doppelItmFamilie;
        float[]              testIndexKosten;
        int                  groesseGesamtmenge;

        groesseGesamtmenge = 6;
        testIndexKosten = new float[groesseGesamtmenge];
        for (int i = 0; i < groesseGesamtmenge; i++) {
            testIndexKosten[i] = 1;
        }
        doppelItmFamilie = new QuotDoppelItmFamilie(groesseGesamtmenge,
                                                    testIndexKosten, -1);
        test(doppelItmFamilie);
    }
}

