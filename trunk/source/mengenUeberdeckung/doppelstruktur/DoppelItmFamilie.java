/*
 * Dateiname      : DoppelItmFamilie.java
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

import java.util.Arrays;
import java.text.DecimalFormat;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.SizeMismatchException;
import mengenUeberdeckung.heuristiken.costs.DoubleIndicesCosts;

/**
 * Repr�sentiert eine abstrakte Familie von Teilmengen einer Indexmenge, wobei
 * den Teilmengen jeweils eine zweite Teilmenge zugeordnet ist, die zur
 * Berechnung der Kosten verwendet wird.
 *
 * @author  Dietmar Lippold
 */
public abstract class DoppelItmFamilie extends ItmFamilie implements Cloneable {

    /**
     * Die Instanz zur Ermittlung der Kosten der �berdeckten Kosten-Indices
     * dieser Familie oder einer hinzuzuf�genden oder zu entfernenden
     * Doppel-Teilmenge.
     */
    private DoubleIndicesCosts indicesCosts;

    /**
     * Gibt zu jedem Kosten-Index die H�ufigkeit an, mit der dieser �berdeckt
     * wird.
     */
    private int[] kostenIndexUeberdeckAnz;

    /**
     * Gibt die gesamten Kosten der �berdeckten Kosten-Indices der Familie an.
     */
    private float indexKostenGesamt = 0;

    /**
     * Gibt die Gesamth�ufigkeit an, mit der die Kosten-Indices �berdeckt
     * werden.
     */
    private int kostenIndexGesamtAnz = 0;

    /**
     * Erzeugt eine Instanz, deren Elemente Doppel-Index-Menge der �bergebenen
     * Gr��e sind. Eine maximale Anzahl von Teilmengen wird nicht vorgegeben.
     *
     * @param groesseGesamtmenge  Die Gr��e der Gesamtmenge, die den
     *                            Teilmengen der neuen Familie zugrunde
     *                            liegt.
     * @param indicesCosts        Die Instanz zur Ermittlung der Kosten der
     *                            �berdeckten Kosten-Indices dieser Familie
     *                            oder einer hinzuzuf�genden oder zu
     *                            entfernenden Doppel-Teilmenge.
     */
    public DoppelItmFamilie(int groesseGesamtmenge,
                            DoubleIndicesCosts indicesCosts) {
        super(groesseGesamtmenge);
        this.indicesCosts = indicesCosts;
        this.kostenIndexUeberdeckAnz = new int[indicesCosts.costIndexNumber()];
        Arrays.fill(this.kostenIndexUeberdeckAnz, 0);
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
    protected DoppelItmFamilie(DoppelItmFamilie andereFamilie,
                               boolean festeVerwaltung) {
        super(andereFamilie, festeVerwaltung);
        this.indicesCosts = andereFamilie.indicesCosts;
        this.kostenIndexUeberdeckAnz = new int[indicesCosts.costIndexNumber()];
        Arrays.fill(this.kostenIndexUeberdeckAnz, 0);
    }

    /**
     * Erzeugt eine Familie, deren Werte von der �bergebenen Teilmenge kopiert
     * werden. Bei den enthaltenen Teilmengen und bei den Index-Kosten handelt
     * es sich aber um die gleichen Instanzen.
     *
     * @param andereFamilie  Eine Familie, deren Werte in die neue Instanz
     *                       kopiert werden.
     */
    protected DoppelItmFamilie(DoppelItmFamilie andereFamilie) {
        super(andereFamilie);
        this.indicesCosts = andereFamilie.indicesCosts;
        this.kostenIndexUeberdeckAnz = (int[])andereFamilie.kostenIndexUeberdeckAnz.clone();
        this.indexKostenGesamt = andereFamilie.indexKostenGesamt;
        this.kostenIndexGesamtAnz = andereFamilie.kostenIndexGesamtAnz;
    }

    /**
     * Liefert die Instanz zur Ermittlung der Kosten der �berdeckten
     * Kosten-Indices dieser Familie oder einer hinzuzuf�genden oder zu
     * entfernenden Doppel-Teilmenge.
     *
     * @return  Die Instanz zur Ermittlung der Kosten der �berdeckten
     *          Kosten-Indices.
     */
    protected DoubleIndicesCosts indicesCosts() {
        return indicesCosts;
    }

    /**
     * Liefert ein Array, in dem zu jedem Kosten-Index die H�ufigkeit
     * angegeben ist, mit der dieser �berdeckt wird.
     *
     * @return  Ein Array mit der �berdeckungsh�ufigkeit der Kosten-Indices.
     */
    protected int[] kostenIndexUeberdeckAnz() {
        return kostenIndexUeberdeckAnz;
    }

    /**
     * Liefert die gesamten Kosten der �berdeckten Kosten-Indices der Familie.
     *
     * @return  Die gesamten Kosten der �berdeckten Kosten-Indices der
     *          Familie.
     */
    protected float indexKostenGesamt() {
        return indexKostenGesamt;
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
    public abstract ItmFamilie neueInstanz(boolean festeVerwaltung);

    /**
     * Liefert ein neues Objekt, das zu <CODE>this</CODE> gleich ist. Die
     * Menge der enthaltenen Teilmengen wird dabei neu erzeugt. Bei den
     * enthaltenen Teilmengen handelt es sich aber um die gleichen Instanzen.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public abstract Object clone();

    /**
     * Liefert den hashCode der Instanz.
     *
     * @return  Den hashCode der Instanz.
     */
    public int hashCode() {
        return (super.hashCode() + kostenIndexGesamtAnz);
    }

    /**
     * Ermittelt, ob das �bergebene Objekt eine <CODE>DoppelItmFamilie</CODE>
     * mit den gleichen Kosten f�r die Kosten-Indices ist und die gleichen
     * Teilmengen wie <CODE>this</CODE> enth�lt.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>DoppelItmFamilie</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das �bergebene Objekt eine
     *          <CODE>DoppelItmFamilie</CODE> mit den gleichen Kosten f�r die
     *          Kosten-Indicesist und die gleichen Teilmenge wie diese Instanz
     *          enth�lt, anderenfalls wird <CODE>false</CODE> geliefert.
     */
    public boolean equals(Object anderesObjekt) {
        DoppelItmFamilie andereDoppelFamilie;

        if (anderesObjekt == this) {
            return true;
        }

        if (anderesObjekt.getClass() != getClass()) {
            return false;
        }

        andereDoppelFamilie = (DoppelItmFamilie) anderesObjekt;
        if (!Arrays.equals(andereDoppelFamilie.kostenIndexUeberdeckAnz,
                           this.kostenIndexUeberdeckAnz)) {
            return false;
        }

        return super.equals(anderesObjekt);
    }

    /**
     * L�scht die Familie, d.h. setzt sie auf den Zustand nach der Erzeugung
     * zur�ck. Dabei werden alle Teilmengen aus der Familie entfernt. Die
     * Index-Kosten werden nicht ver�ndert.
     */
    public void clear() {
        super.clear();
        Arrays.fill(kostenIndexUeberdeckAnz, 0);
        indexKostenGesamt = 0;
        kostenIndexGesamtAnz = 0;
    }

    /**
     * F�gt der Familie die �bergebene Teilmenge hinzu. Wenn die Teilmenge
     * schon in der Familie enthalten ist, �ndert sich nichts.
     *
     * @param teilmenge  Die Teilmenge, die <CODE>this</CODE> hinzuef�gt
     *                   werden soll.
     *
     * @throws SizeMismatchException     Die Gesamtmenge der �bergebenen
     *                                   Teilmenge hat eine andere Gr��e als
     *                                   die Gesamtmenge dieser Familie oder
     *                                   einer der Kosten-Indices ist gr��er
     *                                   als zul�ssig.
     * @throws IllegalArgumentException  Die �bergebene Teilmenge ist nicht vom
     *                                   Typ <CODE>DoppelIndexTeilmenge</CODE>.
     *
     * @see import mengenUeberdeckung.allgemein.SizeMismatchException
     */
    public void teilmengeHinzufuegen(IndexTeilmenge teilmenge) {
        DoppelIndexTeilmenge doppelTeilmenge;

        if (enthaelt(teilmenge)) return;

        if (!(teilmenge instanceof DoppelIndexTeilmenge)) {
            throw new IllegalArgumentException();
        }
        doppelTeilmenge = (DoppelIndexTeilmenge) teilmenge;

        if (doppelTeilmenge.groessterEnthaltKostenIndex() > kostenIndexUeberdeckAnz.length - 1) {
            throw new SizeMismatchException("Kosten-Indices sind gr��er als"
                                            + " zul�ssig");
        }

        super.teilmengeHinzufuegen(teilmenge);

        for (int index = doppelTeilmenge.kleinsterEnthaltKostenIndex();
             index >= 0;
             index = doppelTeilmenge.naechsterEnthaltKostenIndex(index + 1)) {
            kostenIndexUeberdeckAnz[index]++;
            kostenIndexGesamtAnz++;
        }

        indexKostenGesamt = indicesCosts.familyCosts(kostenIndexUeberdeckAnz);
    }

    /**
     * Entfernt die �bergebene Teilmenge aus der Familie. Wenn die Teilmenge
     * nicht in der Familie enthalten ist, �ndert sich nichts.
     *
     * @param teilmenge  Die Teilmenge, die aus <CODE>this</CODE> entfernt
     *                   werden soll.
     *
     * @throws IllegalArgumentException  Die �bergebene Teilmenge ist nicht vom
     *                                   Typ <CODE>DoppelIndexTeilmenge</CODE>.
     */
    public void teilmengeEntfernen(IndexTeilmenge teilmenge) {
        DoppelIndexTeilmenge doppelTeilmenge;

        if (!enthaelt(teilmenge)) return;

        if (!(teilmenge instanceof DoppelIndexTeilmenge)) {
            throw new IllegalArgumentException();
        }

        super.teilmengeEntfernen(teilmenge);

        doppelTeilmenge = (DoppelIndexTeilmenge) teilmenge;
        for (int index = doppelTeilmenge.kleinsterEnthaltKostenIndex();
             index >= 0;
             index = doppelTeilmenge.naechsterEnthaltKostenIndex(index + 1)) {
            kostenIndexUeberdeckAnz[index]--;
            kostenIndexGesamtAnz--;
        }

        indexKostenGesamt = indicesCosts.familyCosts(kostenIndexUeberdeckAnz);
    }

    /**
     * Ermittelt, ob die Kosten der einzelnen enthaltenen Teilmengen in allen
     * Situationen gleich sind.
     *
     * @return  F�r diese Klasse ist der Wert immer <CODE>false</CODE>.
     */
    public boolean teilmengenKostenGleich() {
        return false;
    }

    /**
     * Ermittelt, ob die Kosten dieser Familie linear von den Kosten der
     * enthaltenen Teilmengen abh�ngen, wenn die Kosten der Teilmengen also
     * unabh�ngig voneinander sind in Bezug auf die Kosten dieser Familie.
     *
     * @return  F�r diese Klasse ist der Wert immer <CODE>false</CODE>.
     */
    public boolean teilmengenKostenLinear() {
        return false;
    }

    /**
     * Liefert die einfachen Kosten der enthaltenen Teilmengen, d.h. ohne
     * Ber�cksichtigung der Kosten durch die �berdeckung der Kosten-Indices.
     *
     * @return  Die einfachen Kosten der enthaltenen Teilmengen.
     */
    protected float kostenFamilieEinfach() {
        return super.kostenFamilie();
    }

    /**
     * Liefert den Wert, um den sich die einfachen Kosten der Familie, d.h.
     * die Kosten ohne Ber�cksichtigung der Kosten-Indices, durch Hinzuf�gen
     * der �bergebenen Teilmenge erh�hen.
     *
     * @param teilmenge  Die Teilmenge, f�r die die einfachen Kosten durch
     *                   Hinzuf�gen zur Familie ermittelt werden sollen.
     *
     * @return  Der Wert, um den sich die einfachen Kosten der Familie durch
     *          Hinzuf�gen der �bergebenen Teilmenge erh�hen. Wenn die
     *          �bergebene Teilmenge schon in der Familie enthalten ist, wird
     *          der Wert Null geliefert.
     */
    protected float kostenHinzufuegenEinfach(IndexTeilmenge teilmenge) {
        return super.kostenHinzufuegen(teilmenge);
    }

    /**
     * Liefert den Wert, um den sich die einfachen Kosten der Familie, d.h.
     * die Kosten ohne Ber�cksichtigung der Kosten-Indices, durch Entfernen
     * der �bergebenen Teilmenge verringern.
     *
     * @param teilmenge  Die Teilmenge, f�r die die einfachen Kosten durch
     *                   Entfernen aus der Familie ermittelt werden sollen.
     *
     * @return  Der Wert, um den sich die einfachen Kosten der Familie durch
     *          Entfernen der �bergebenen Teilmenge verringern. Wenn die
     *          �bergebene Teilmenge nicht in der Familie enthalten ist, wird
     *          der Wert Null geliefert.
     */
    protected float kostenEntfernenEinfach(IndexTeilmenge teilmenge) {
        return super.kostenEntfernen(teilmenge);
    }

    /**
     * Liefert die Kosten der enthaltenen Teilmengen.
     *
     * @return  Die Kosten der enthaltenen Teilmengen.
     */
    public abstract float kostenFamilie();

    /**
     * Liefert den Wert, um den sich die Kosten der Familie durch Hinzuf�gen
     * der �bergebenen Teilmenge erh�hen.
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
    public abstract float kostenHinzufuegen(IndexTeilmenge teilmenge);

    /**
     * Liefert den Wert, um den sich die Kosten der Familie durch Entfernen
     * der �bergebenen Teilmenge verringern.
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
    public abstract float kostenEntfernen(IndexTeilmenge teilmenge);

    /**
     * Liefert eine Darstellung der Famile in einem Library-Format. Dies ist
     * so aufgebaut, da� in der ersten Zeile die Anzahl der Elemente der
     * Gesamtmenge und die Anzahl der Kosten-Indices steht und dann in jeder
     * weiteren Zeile die Darstellung einer Teilmenge.
     *
     * @return  Einen String, der <CODE>this</CODE> in einem Library-Format
     *          darstellt.
     */
    public String toLibFormat() {
        StringBuffer rueckgabe;
        String       newline = System.getProperty("line.separator");
        int          firstNewlinePos;

        rueckgabe = new StringBuffer(super.toLibFormat());

        // Am Ende der ersten Zeile die Anzahl der Kosten-Indices einf�gen.
        firstNewlinePos = rueckgabe.indexOf(newline);
        rueckgabe.insert(firstNewlinePos, " ");
        rueckgabe.insert(firstNewlinePos + 1, kostenIndexUeberdeckAnz.length);

        return rueckgabe.toString();
    }

    /**
     * Liefert eine Statistik zu den enthaltenen Teilmengen.
     *
     * @param einrueckStufen  Die Anzahl der Stufen, um die die Statistik vom
     *                        linken Rand einger�ckt werden soll.
     *
     * @return  Einen Text zur Statistik der Familie.
     */
    public String statistik(int einrueckStufen) {
        DecimalFormat decFormat = new DecimalFormat("0.0");
        String  ausgabe;
        String  einrueck = "";
        int     einrueckTiefe = einrueckStufen * Konstanten.EINRUECK_TIEFE;
        int     anzUeberdeckt;

        for (int i = 0; i < einrueckTiefe; i++) {
            einrueck += " ";
        }

        anzUeberdeckt = 0;
        for (int i = 0; i < kostenIndexUeberdeckAnz.length; i++) {
            if (kostenIndexUeberdeckAnz[i] > 0) {
                anzUeberdeckt++;
            }
        }

        ausgabe = super.statistik(einrueckStufen);
        ausgabe += "\n";
        ausgabe += einrueck + "Anzahl der Kosten-Indices"
                            + "                                : "
                            + kostenIndexUeberdeckAnz.length
                            + "\n";
        ausgabe += einrueck + "Anzahl der �berdeckten Kosten-Indices"
                            + "                    : "
                            + anzUeberdeckt
                            + "\n";
        ausgabe += einrueck + "Kosten unabh�ngig von �berdeckten Kosten-Indices"
                            + "         : "
                            + kostenFamilieEinfach()
                            + "\n";
        ausgabe += einrueck + "Summe der Kosten der �berdeckten Kosten-Indices"
                            + "          : "
                            + indexKostenGesamt
                            + "\n";
        ausgabe += einrueck + "Gesamth�ufigkeit der �berdeckung der"
                            + " Kosten-Indices      : "
                            + kostenIndexGesamtAnz
                            + "\n";
        ausgabe += einrueck + "Durchschn. H�ufigkeit der �berdeckung der"
                            + " Kosten-Indices : "
                            + decFormat.format((float)kostenIndexGesamtAnz
                                               / kostenIndexUeberdeckAnz.length)
                            + "\n";
        return ausgabe;
    }

    /**
     * Testet einige Prozeduren mittels Hinzunahme und Entfernen mehrerer
     * Teilmengen.
     *
     * @param doppelItmFamilie  Die Familie, zu der die Doppel-Teilmengen zum
     *                          Test hinzugef�gt werden. Deren Gesamtgr��e mu�
     *                          mindestens 6 sein.
     */
    protected static void test(DoppelItmFamilie doppelItmFamilie) {
        DoppelIndexTeilmenge ditm1, ditm2, ditm3;
        int[]                kostIndices1, kostIndices2, kostIndices3;
        int                  groesseGesamtmenge;

        System.out.println("Test einiger Methoden der Klasse DoppelItmFamilie\n");

        groesseGesamtmenge = doppelItmFamilie.groesseGesamtmenge();
        System.out.print("Erzeugung einer DoppelItmFamilie mit der Gesamtgroesse ");
        System.out.println(groesseGesamtmenge + "\n");

        System.out.print("Hinzufuegen folgender Doppel-Index-Teilmengen mit der");
        System.out.println(" Gesamtgroesse " + groesseGesamtmenge + ":");

        kostIndices1 = new int[] {1};
        ditm1 = new DoppelIndexTeilmenge(groesseGesamtmenge, 0.1f, kostIndices1);
        ditm1.indicesAufnehmen(new int[] {2, 3, 5});
        System.out.println(ditm1.toString());
        doppelItmFamilie.teilmengeHinzufuegen(ditm1);

        kostIndices2 = new int[] {2, 3};
        ditm2 = new DoppelIndexTeilmenge(groesseGesamtmenge, 0.1f, kostIndices2);
        ditm2.indicesAufnehmen(new int[] {0, 1, 3, 5});
        System.out.println(ditm2.toString());
        doppelItmFamilie.teilmengeHinzufuegen(ditm2);

        kostIndices3 = new int[] {3, 4, 5};
        ditm3 = new DoppelIndexTeilmenge(groesseGesamtmenge, 0.1f, kostIndices3);
        ditm3.indexAufnehmen(2);
        ditm3.indexAufnehmen(4);
        System.out.println(ditm3.toString());
        doppelItmFamilie.teilmengeHinzufuegen(ditm3);

        System.out.println();
        System.out.println("Statistik der resultierenden DoppelItmFamilie:");
        System.out.print(doppelItmFamilie.statistik(1));
        System.out.println();

        System.out.println("Entfernen der Doppel-Index-Teilmenge ");
        System.out.println(ditm2.toString());
        doppelItmFamilie.teilmengeEntfernen(ditm2);

        System.out.println();
        System.out.println("Statistik der resultierenden DoppelItmFamilie:");
        System.out.print(doppelItmFamilie.statistik(1));
        System.out.println();
    }
}

