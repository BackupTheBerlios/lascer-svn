/*
 * Dateiname      : QuotDoppelItmFamilie.java
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
import mengenUeberdeckung.heuristiken.costs.FrequencyCosts;
import mengenUeberdeckung.heuristiken.costs.FixedExistentCosts;

/**
 * Repräsentiert eine Familie von Teilmengen einer Indexmenge, wobei den
 * Teilmengen jeweils eine zweite Teilmenge zugeordnet ist, die zur Berechnung
 * der Kosten verwendet wird. Die Kosten der Familie ergeben sich als Quotient
 * der Kosten der enthaltenen Teilmengen selbst und der Kosten der
 * Kosten-Indices, die von keiner Teilmenge überdeckt werden.
 *
 * @author  Dietmar Lippold
 */
public class QuotDoppelItmFamilie extends DoppelItmFamilie implements Cloneable {

    /**
     * Ein Wert, der größer als das Maximum der Kosten aller Kosten-Indices
     * ist.
     */
    private float indexKostenMaxPlus;

    /**
     * Erzeugt eine Instanz, deren Elemente Doppel-Index-Menge der übergebenen
     * Größe sind. Eine maximale Anzahl von Teilmengen wird nicht vorgegeben.
     * Zur Berechnung der Kosten der Kosten-Indices einer Doppel-Teilmenge
     * oder einer Doppel-Familie wird die Klasse <CODE>ExistentCosts</CODE>
     * benutzt.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die den
     *                            Teilmengen der neuen Familie zugrunde
     *                            liegt.
     * @param indexKosten         Die Kosten für die Überdeckung der einzelnen
     *                            Kosten-Indices.
     * @param nachkommaAnz        Die Anzahl der Nachkomma-Stellen, die bei
     *                            Berechnung der Kosten der Kosten-Indices
     *                            berücksichtigt werden sollen. Der Wert muß
     *                            größer oder gleich Null sein.
     *
     * @see mengenUeberdeckung.heuristiken.costs.FixedExistentCosts
     */
    public QuotDoppelItmFamilie(int groesseGesamtmenge, float[] indexKosten,
                                int nachkommaAnz) {
        super(groesseGesamtmenge, new FixedExistentCosts(indexKosten,
                                                         nachkommaAnz));

        // Ermittlung des Wertes für indexKostenMaxPlus
        indexKostenMaxPlus = FrequencyCosts.festkommaSumme(indexKosten,
                                                           nachkommaAnz);
        if (indexKostenMaxPlus == 0) {
            // Die Summe der Kosten der Kosten-Indices wird auch nach
            // Hinzufügen von Teilmengen immer Null sein. Daher wird die
            // Maximalsumme auf Eins gesetzt, damit später die Differenz Null
            // ist.
            indexKostenMaxPlus = 1;
        } else {
            // Die Maximalsumme der Kosten der Kosten-Indices erhöhen, damit
            // die Differenz zur späteren Summe der Kosten positiv ist.
            indexKostenMaxPlus *= Konstanten.MAX_KOSTEN_FAKTOR;
        }
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
    protected QuotDoppelItmFamilie(QuotDoppelItmFamilie andereFamilie,
                                   boolean festeVerwaltung) {
        super(andereFamilie, festeVerwaltung);
        this.indexKostenMaxPlus = andereFamilie.indexKostenMaxPlus;
    }

    /**
     * Erzeugt eine Familie, deren Werte von der übergebenen Teilmenge kopiert
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
     * den allgemeinen Kosten der Teilmengen und den Kosten für die
     * Kosten-Indices, die von keiner Teilmenge überdeckt werden.
     *
     * @return  Die Kosten der enthaltenen Teilmengen.
     */
    public float kostenFamilie() {
        float indexKostenDiff;

        indexKostenDiff = indexKostenMaxPlus - indexKostenGesamt();
        if (indexKostenDiff <= 0) {
            System.err.println("WARNUNG: Rechenungenauigkeit in Methode"
                               + " QuotDoppelItmFamilie.kosten");
            System.err.println("         Konstante MAX_KOSTEN_FAKTOR erhöhen");
            indexKostenDiff = (float) Math.sqrt(Float.MIN_VALUE);
        }

        return (kostenFamilieEinfach() / indexKostenDiff);
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
            System.err.println("         Konstante MAX_KOSTEN_FAKTOR erhöhen");
            indexKostenDiffNeu = (float) Math.sqrt(Float.MIN_VALUE);
        }

        kostenNeu = ((kostenFamilieEinfach() + kostenHinzufuegenEinfach(teilmenge))
                     / indexKostenDiffNeu);

        return (kostenNeu - kostenFamilie());
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
            System.err.println("         Konstante MAX_KOSTEN_FAKTOR erhöhen");
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

