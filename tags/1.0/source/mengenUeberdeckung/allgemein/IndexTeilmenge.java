/*
 * Dateiname      : IndexTeilmenge.java
 * Letzte Änderung: 13. August 2006
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

import mathCollection.BitMathIntSet;

/**
 * Repräsentiert eine Teilmenge der Indices einer Menge fester Größe von
 * Objekten. Die Menge aller Indices wird <DFN>Gesamtmenge</DFN> genannt.
 * Jedes Element der Teilmenge stellt also einen Index dar, der größer
 * oder gleich Null und kleiner oder gleich der Größe der Gesamtmenge minus
 * Eins ist. Jeder Teilmenge ist ein Wert zugeordnet, der <DFN>Kosten</DFN>
 * genannt wird.
 *
 * @author  Dietmar Lippold
 */
public class IndexTeilmenge implements Cloneable {

    /**
     * Die Verwaltung der Elemente.
     */
    private ElementVerwaltung elementVerwalt;

    /**
     * Die der Teilmenge zugeordneten Kosten.
     */
    private float kosten;

    /**
     * Die Größe der zugrunde liegenden Gesamtmenge.
     */
    private int groesseGesamtmenge;

    /**
     * Wenn der Wert ungleich Null ist, der aktuelle Wert der Methode
     * <CODE>hashCode<CODE>. Wenn der Wert Null ist, muß er neu berechnet
     * werden.
     */
    private int storedHashCode = 0;

    /**
     * Erzeugt eine Teilmenge mit der übergebenen Verwaltung der Elemente und
     * den angegebenen Kosten.
     *
     * @param elementVerwalt      Die Verwaltung mit den Elementen.
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die der neuen
     *                            Instanz zugrunde liegt.
     * @param kosten              Die Kosten der Teilmenge.
     *
     * @throws IllegalArgumentException  Der Wert für <CODE>kosten</CODE> ist
     *                                   negativ.
     */
    public IndexTeilmenge(ElementVerwaltung elementVerwalt,
                          int groesseGesamtmenge, float kosten) {

        if (kosten < 0) {
            throw new IllegalArgumentException("Der Wert der Kosten ist"
                                               + " kleiner als Null");
        }

        this.elementVerwalt = elementVerwalt;
        this.kosten = kosten;
        this.groesseGesamtmenge = groesseGesamtmenge;
    }

    /**
     * Erzeugt eine Teilmenge mit den gleichen Elementen wie die übergebene
     * Teilmenge aber mit anderen Kosten. Die Menge der Elemente der
     * übergebenen Teilmenge kann dabei der neuen Teilmenge nur zugewiesen
     * oder sie kann kopiert werden. Wenn sie nur zugewiesen wird, ist eine
     * Veränderung der einen Teilmenge mit einer Veränderung der anderen
     * verbunden.
     *
     * @param teilmenge      Die Teilmenge, deren Elemente kopiert werden.
     * @param kosten         Die Kosten, die <CODE>this</CODE> zugeordnet
     *                       werden. Dieser Wert darf nicht negativ sein.
     * @param cloneElements  Gibt an, ob für die Elemente ein neues Objekt
     *                       erzeugt werden soll. Falls nicht, ist eine
     *                       Veränderung der Elemente des neuen Objekts mit
     *                       einer Veränderung der Elemente des alten Objekts
     *                       verbunden.
     *
     * @throws IllegalArgumentException  Der Wert für <CODE>kosten</CODE> ist
     *                                   negativ.
     */
    public IndexTeilmenge(IndexTeilmenge teilmenge, float kosten,
                          boolean cloneElements) {

        if (kosten < 0) {
            throw new IllegalArgumentException("Der Wert der Kosten ist"
                                               + " kleiner als Null");
        }

        if (cloneElements) {
            this.elementVerwalt = (ElementVerwaltung) teilmenge.elementVerwalt.clone();
        } else {
            this.elementVerwalt = teilmenge.elementVerwalt;
        }
        this.kosten = kosten;
        this.groesseGesamtmenge = teilmenge.groesseGesamtmenge;
        this.storedHashCode = teilmenge.storedHashCode;
    }

    /**
     * Erzeugt eine Teilmenge mit den gleichen Elementen wie die übergebene
     * Teilmenge. Diese Teilmenge ist unabhängig von der übergebenen Teilmenge,
     * d.h. für die enthaltenen Elemente wird ein neues Objekt erzeugt.
     *
     * @param teilmenge  Die Teilmenge, deren Werte kopiert werden.
     */
    public IndexTeilmenge(IndexTeilmenge teilmenge) {
        this(teilmenge, teilmenge.kosten(), true);
    }

    /**
     * Erzeugt eine Teilmenge, der Kosten in Höhe von <CODE>kosten</CODE>
     * zugeordnet sind und deren Gesamtmenge <CODE>groesseGesamtmenge</CODE>
     * Elemente besitzt. Nach der Erzeugung enthält die Teilmenge noch kein
     * Element.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die der neuen
     *                            Instanz zugrunde liegt.
     * @param kosten              Die Kosten, die <CODE>this</CODE> zugeordnet
     *                            sind. Dieser Wert darf nicht negativ sein.
     * @param speicherEffizient   Gibt an, ob eine Speicher-effiziente und
     *                            damit aber nicht Laufzeit-effiziente
     *                            Speicherung erfolgen soll.
     *
     * @throws IllegalArgumentException  Der Wert für <CODE>kosten</CODE> ist
     *                                   negativ.
     */
    public IndexTeilmenge(int groesseGesamtmenge, float kosten,
                          boolean speicherEffizient) {

        if (kosten < 0) {
            throw new IllegalArgumentException("Der Wert der Kosten ist"
                                               + " kleiner als Null");
        }

        this.elementVerwalt = new BitIntElemVerwalt(groesseGesamtmenge,
                                                    speicherEffizient);
        this.kosten = kosten;
        this.groesseGesamtmenge = groesseGesamtmenge;
    }

    /**
     * Erzeugt eine Teilmenge, der Kosten in Höhe von <CODE>kosten</CODE>
     * zugeordnet sind und deren Gesamtmenge <CODE>groesseGesamtmenge</CODE>
     * Elemente besitzt. Nach der Erzeugung enthält die Teilmenge noch kein
     * Element. Die Speicherung der Indices erfolgt Laufzeit-effizient.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die  der neuen
     *                            Instanz zugrunde liegt.
     * @param kosten              Die Kosten, die <CODE>this</CODE> zugeordnet
     *                            sind. Dieser Wert darf nicht negativ sein.
     *
     * @throws IllegalArgumentException  Der Wert für <CODE>kosten</CODE> ist
     *                                   negativ.
     */
    public IndexTeilmenge(int groesseGesamtmenge, float kosten) {
        this(groesseGesamtmenge, kosten, false);
    }

    /**
     * Erzeugt eine Teilmenge, der als Kosten der Wert Eins zugewiesen ist
     * und deren Gesamtmenge <CODE>groesseGesamtmenge</CODE> Elemente besitzt.
     * Nach der Erzeugung enthält die Teilmenge noch kein Element. Die
     * Speicherung der Indices erfolgt Laufzeit-effizient.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die  der neuen
     *                            Instanz zugrunde liegt.
     */
    public IndexTeilmenge(int groesseGesamtmenge) {
        this(groesseGesamtmenge, 1, false);
    }

    /**
     * Liefert die der Menge zugeordneten Kosten.
     *
     * @return  Die <CODE>this</CODE> zugeordneten Kosten.
     */
    protected float kosten() {
        return kosten;
    }

    /**
     * Liefert ein neue Instanz dieser Klasse, die zu <CODE>this</CODE> gleich
     * ist.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return (new IndexTeilmenge(this));
    }

    /**
     * Liefert den hashCode dieses Objekts. Dieser hängt nur von der Größe der
     * Gesamtmenge und von den enthaltenen Indices ab, nicht jedoch von den
     * absoluten Kosten.
     *
     * @return  Den hashCode dieses Objekts.
     */
    public int hashCode() {

        if (storedHashCode == 0) {
            storedHashCode = elementVerwalt.hashCode() + groesseGesamtmenge;
        }

        return storedHashCode;
    }

    /**
     * Ermittelt, ob das übergebene Objekte eine Teilmenge ist und zu
     * <CODE>this</CODE> gleich ist.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>IndexTeilmenge</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das übergebene Objekt eine
     *          <CODE>IndexTeilmenge</CODE> ist und die gleichen Indices
     *          und kosten wie diese Instanz enthält, anderenfalls wird
     *          <CODE>false</CODE> geliefert.
     */
    public boolean equals(Object anderesObjekt) {
        IndexTeilmenge andereTeilmenge;

        if (anderesObjekt == this) {
            return true;
        }

        if (!(anderesObjekt instanceof IndexTeilmenge)) {
            return false;
        }

        andereTeilmenge = (IndexTeilmenge) anderesObjekt;
        if ((andereTeilmenge.groesseGesamtmenge != groesseGesamtmenge)
                || (andereTeilmenge.kosten != kosten)) {
            return false;
        } else {
            return andereTeilmenge.elementVerwalt.equals(elementVerwalt);
        }
    }

    /**
     * Ermittelt, ob diese Teilmenge gleich gut oder besser als die übergebene
     * Teilmenge ist. Diese Methode definiert nur eine Halbordnung auf den
     * Teilmengen, d.h. bei zwei Teilmengen braucht weder die erste noch die
     * zweite besser als die jeweils andere zu sein.
     *
     * @param andereTeilmenge  Eine andere Teilmenge.
     *
     * @return  <CODE>true</CODE>, wenn diese Teilmenge gleich gut oder besser
     *          als die übergebene Teilmenge ist, anderenfalls
     *          <CODE>false</CODE>.
     */
    public boolean istGleichOderBesser(IndexTeilmenge andereTeilmenge) {

        return ((kosten <= andereTeilmenge.kosten)
                && elementVerwalt.containsAll(andereTeilmenge.elementVerwalt));
    }

    /**
     * Nimmt den übergebenen Index in die Teilmenge auf. Wenn der Index schon
     * in der Teilmenge enthalten ist, ändert sich nichts.
     *
     * @param index  Der Index, der in <CODE>this</CODE> aufgenommern werden
     *               soll.
     *
     * @throws IndexOutOfBoundsException  Der übergebene Index ist kleiner als
     *                                    Null oder größer oder gleich der
     *                                    Größe der Gesamtmenge.
     */
    public void indexAufnehmen(int index) {

        if ((index < 0) || (index >= groesseGesamtmenge)) {
            throw new IndexOutOfBoundsException();
        }

        elementVerwalt.elementAufnehmen(index);
        storedHashCode = 0;
    }

    /**
     * Nimmt die Indices aus dem übergebenen Array in die Teilmenge auf. Wenn
     * ein Index schon in der Teilmenge enthalten ist, wird dieser nicht noch
     * einmal aufgenommen. Ist ein in der Teilmenge noch nicht enthaltener
     * Index mehrmals im Array enthalten, wird er nur einmal aufgenommen.
     *
     * @param hinzuIndices  Ein Array mit den Indices, die in <CODE>this</CODE>
     *                      aufgenommern werden sollen.
     *
     * @throws IndexOutOfBoundsException  Ein Index des übergebenen Arrays
     *                                    ist kleiner als Null oder größer oder
     *                                    gleich der Größe der Gesamtmenge.
     */
    public void indicesAufnehmen(int[] hinzuIndices) {

        for (int i = 0; i < hinzuIndices.length; i++) {
            indexAufnehmen(hinzuIndices[i]);
        }
    }

    /**
     * Nimmt die Indices aus dem übergebenen <CODE>BitMathIntSet</CODE> in die
     * Teilmenge auf. Wenn ein Index schon in der Teilmenge enthalten ist,
     * wird dieser nicht noch einmal aufgenommen.
     *
     * @param hinzuIndices  Ein <CODE>BitMathIntSet</CODE> mit den Indices,
     *                      die in <CODE>this</CODE> aufgenommern werden
     *                      sollen.
     *
     * @throws IndexOutOfBoundsException  Ein Index der übergebenen Menge ist
     *                                    größer oder gleich der Größe der
     *                                    Gesamtmenge dieser Teilmenge.
     */
    public void indicesAufnehmen(BitMathIntSet hinzuIndices) {

        if (hinzuIndices.getMaximum() >= groesseGesamtmenge) {
            throw new IndexOutOfBoundsException();
        }

        elementVerwalt.elementeAufnehmen(hinzuIndices);
        storedHashCode = 0;
    }

    /**
     * Löscht den übergebenen Index aus der Teilmenge. Wenn der Index nicht
     * in der Teilmenge enthalten ist, ändert sich nichts.
     *
     * @param index  Der Index, der aus <CODE>this</CODE> gelöscht werden
     *               soll.
     *
     * @throws IndexOutOfBoundsException  Der übergebene Index ist kleiner als
     *                                    Null oder größer oder gleich der
     *                                    Größe der Gesamtmenge.
     */
    public void indexLoeschen(int index) {

        if ((index < 0) || (index >= groesseGesamtmenge)) {
            throw new IndexOutOfBoundsException();
        }

        elementVerwalt.elementLoeschen(index);
        storedHashCode = 0;
    }

    /**
     * Löscht die Indices aus dem übergebenen Array aus der Teilmenge. Wenn
     * ein Index nicht in der Teilmenge enthalten ist, wird dieser Index
     * übergangen.
     *
     * @param loeschIndecies  Ein Array mit den Indices, die aus
     *                        <CODE>this</CODE> gelöscht werden sollen.
     *
     * @throws IndexOutOfBoundsException  Ein Index des übergebenen Arrays
     *                                    ist kleiner als Null oder größer oder
     *                                    gleich der Größe der Gesamtmenge.
     */
    public void indicesLoeschen(int[] loeschIndecies) {

        for (int i = 0; i < loeschIndecies.length; i++) {
            indexLoeschen(loeschIndecies[i]);
        }
    }

    /**
     * Ermittelt, ob der übergebene Index in dieser Teilmenge enthalten ist.
     *
     * @param index  Der Index, von dem ermittelt werden soll, ob er in
     *               <CODE>this</CODE> enthalten ist.
     *
     * @return  <CODE>true</CODE>, falls der übergebene Index in
     *          <CODE>this</CODE> enthalten ist, anderenfalls
     *          <CODE>false</CODE>.
     *
     * @throws IndexOutOfBoundsException  Der übergebene Index ist kleiner als
     *                                    Null oder größer oder gleich der
     *                                    Größe der Gesamtmenge.
     */
    public boolean indexIstEnthalten(int index) {

        if ((index < 0) || (index >= groesseGesamtmenge)) {
            throw new IndexOutOfBoundsException();
        }

        return elementVerwalt.contains(index);
    }

    /**
     * Liefert den kleinsten in der Teilmenge enthaltenen Index. Wenn kein
     * Index in der Teilmenge enthalten ist, wird der Wert <CODE>-1</CODE>
     * geliefert.
     *
     * @return  Den kleinsten in <CODE>this</CODE> enthaltenen Index.
     */
    public int kleinsterEnthaltenerIndex() {
        return elementVerwalt.getMinimum();
    }

    /**
     * Liefert den größten in der Teilmenge enthaltenen Index. Wenn kein
     * Index in der Teilmenge enthalten ist, wird der Wert <CODE>-1</CODE>
     * geliefert.
     *
     * @return  Den größten in <CODE>this</CODE> enthaltenen Index.
     */
    public int groessterEnthaltenerIndex() {
        return elementVerwalt.getMaximum();
    }

    /**
     * Liefert den nächsten in dieser Teilmenge enthaltenen Index.
     *
     * @param index  Der Index, ab dem der nächste in der Teilmenge enthaltene
     *               Index ermittelt werden soll.
     *
     * @return  Den kleinsten Index, der gleich oder größer als
     *          <CODE>index</CODE> ist und in der Teilmenge enthalten ist.
     *          Falls es so einen Index nicht gibt, wird der Wert -1
     *          geliefert.
     */
    public int naechsterEnthaltenerIndex(int index) {
        return elementVerwalt.getNext(index - 1);
    }

    /**
     * Liefert ein aufsteigend sortiertes Array mit den Indices, die in dieser
     * Teilmenge enthalten sind.
     *
     * @return  Ein aufsteigend sortiertes Array mit den Indices dieser
     *          Teilmenge. Dieses darf nicht verändert werden.
     */
    public int[] enthalteneIndices() {
        return elementVerwalt.toArray();
    }

    /**
     * Liefert die Verwaltung der Elemente der Teilmenge.
     *
     * @return  Die Verwaltung der Elemente der Teilmenge. Diese darf nicht
     *          verändert werden.
     */
    public ElementVerwaltung elementVerwaltung() {
        return elementVerwalt;
    }

    /**
     * Ermittelt, ob diese Teilmenge und die übergebene Teilmenge kein Element
     * gemeinsam haben.
     *
     * @param andereTeilmenge  Die andere Teilmenge, deren Elemente mit denen
     *                         dieser Teilmenge verglichen werden.
     *
     * @return  <CODE>true</CODE> wenn es kein Element gibt, das sowohl in
     *          dieser wie in der übergebenen Teilmenge enthalten ist, sonst
     *          <CODE>false</CODE>.
     */
    public boolean istDisjunkt(IndexTeilmenge andereTeilmenge) {
        return elementVerwalt.containsNone(andereTeilmenge.elementVerwalt);
    }

    /**
     * Liefert die Anzahl der Indices, die in <CODE>this</CODE> aber nicht in
     * der übergebenen Teilmenge enthalten sind.
     *
     * @param andereTeilmenge  Die Teilmenge, in der die Indices nicht
     *                         enthalten sein sollen.
     *
     * @return  Die Anzahl der Indices, die nur diese und nicht die
     *          übergebene Teilmenge enthält.
     */
    public int anzNurSelbstEnthalten(IndexTeilmenge andereTeilmenge) {
        return elementVerwalt.containingAlone(andereTeilmenge.elementVerwalt);
    }

    /**
     * Liefert die Größe der Gesamtmenge, d.h. die Anzahl ihrer Elemente.
     *
     * @return  Die Anzahl Indices der Gesamtmenge.
     */
    public int groesseGesamtmenge() {
        return groesseGesamtmenge;
    }

    /**
     * Liefert die Größe der Teilmenge, d.h. die Anzahl ihrer Elemente.
     *
     * @return  Die Anzahl Indices von <CODE>this</CODE>.
     */
    public int groesseTeilmenge() {
        return elementVerwalt.size();
    }

    /**
     * Liefert eine Darstellung der Teilmenge, d.h. der in ihr enthaltenen
     * Elemente und Kosten, als String.
     *
     * @return  Einen String, der <CODE>this</CODE> darstellt.
     */
    public String toString() {
        StringBuffer ausgabe = new StringBuffer();

        if (groesseTeilmenge() == 0) {
            return ("{}:" + kosten());
        }

        ausgabe.append(elementVerwalt.toString());
        ausgabe.append(":" + kosten());

        return ausgabe.toString();
    }

    /**
     * Testet einige Prozeduren der mittels Hinzunahme und Entfernen einzelner
     * Indices.
     *
     * @param args  Die Standard-Argumente einer main-Methode.
     */
    public static void main(String[] args) {
        IndexTeilmenge itm;

        System.out.println("Test einiger Methoden der Klasse IndexTeilmenge\n");

        System.out.println("Erzeugung einer Index-Teilmenge der Gesamtgroesse 6"
                           + " und Standardkosten\n");
        itm = new IndexTeilmenge(6);

        System.out.println("Aufnahme der Indices 2, 3 und 5");
        itm.indexAufnehmen(2);
        itm.indexAufnehmen(3);
        itm.indexAufnehmen(5);
        System.out.print("Resultierende Index-Teilmenge: ");
        System.out.println(itm.toString());
        System.out.println();

        System.out.println("Loeschen des Elements 2");
        itm.indexLoeschen(2);
        System.out.print("Resultierende Index-Teilmenge: ");
        System.out.println(itm.toString());
        System.out.println();

        System.out.println("Aufnahme des Index 2 und Loeschen des Index 3");
        itm.indexAufnehmen(2);
        itm.indexLoeschen(3);
        System.out.print("Resultierende Index-Teilmenge: ");
        System.out.println(itm.toString());
        System.out.println();

        System.out.println("Aufnahme des Index 3 und Loeschen des Index 5");
        itm.indexAufnehmen(3);
        itm.indexLoeschen(5);
        System.out.print("Resultierende Index-Teilmenge: ");
        System.out.println(itm.toString());
        System.out.println();

        System.out.println("Zweimalige Aufnahme des Index 5");
        itm.indexAufnehmen(5);
        itm.indexAufnehmen(5);
        System.out.print("Resultierende Index-Teilmenge: ");
        System.out.println(itm.toString());
        System.out.println();

        System.out.print("Groesse der Index-Teilmenge = ");
        System.out.println(itm.groesseTeilmenge());
        System.out.println();

        System.out.print("Kleinster Enthaltener Index = ");
        System.out.println(itm.kleinsterEnthaltenerIndex());
        System.out.println();

        System.out.print("Groesster Enthaltener Index = ");
        System.out.println(itm.groessterEnthaltenerIndex());
        System.out.println();

        System.out.print("Kosten der Index-Teilmenge = ");
        System.out.println(itm.kosten());
        System.out.println();
    }
}

