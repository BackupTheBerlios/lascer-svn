/*
 * Dateiname      : BoolElemVerwalt.java
 * Letzte Änderung: 22. Mai 2006
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

import java.util.Arrays;
import mathCollection.BitMathIntSet;

/**
 * Realisiert eine Verwaltung von Elementen, die nicht-negative ganze Zahlen
 * sind, durch ein boolsches Array. Die Größe der Verwaltung ist im
 * Konstruktor anzugeben und nicht veränderlich.
 *
 * @author  Dietmar Lippold
 */
public class BoolElemVerwalt implements ElementVerwaltung, Cloneable {

    /**
     * Das Feld enthält für jedes möglichen Element die Angabe, ob es in der
     * Verwaltung enthalten ist.
     */
    private boolean[] vorhanden;

    /**
     * Die Anzahl der gespeicherten Elemente.
     */
    private int storedElements = 0;

    /**
     * Das kleinste gespeicherte Element. Wenn der Wert negativ ist, ist der
     * Wert ungültig.
     */
    private int minElement = -1;

    /**
     * Das größte gespeicherte Element. Wenn der Wert negativ ist, ist der
     * Wert ungültig.
     */
    private int maxElement = -1;

    /**
     * Wenn der Wert ungleich Null ist, der aktuelle Wert der Methode
     * <CODE>hashCode<CODE>. Wenn der Wert Null ist, muß er neu berechnet
     * werden.
     */
    private int storedHashCode = 0;

    /**
     * Erzeugt eine neue Instanz, die noch kein Element enthält.
     *
     * @param maxElementAnz  Die maximale Anzahl zu speichernder Elemente,
     *                       d.h. das maximal zu speichernde Element plus
     *                       Eins.
     */
    public BoolElemVerwalt(int maxElementAnz) {
        this.vorhanden = new boolean[maxElementAnz];
    }

    /**
     * Erzeugt eine neue Instanz, die gleich ist zu übergebenen Instanz. Die
     * enthaltenen Elemente werden tief kopiert.
     *
     * @param andereVerwaltung  Die Instanz, zu der die neue Instanz gleich
     *                          sein soll.
     */
    public BoolElemVerwalt(BoolElemVerwalt andereVerwaltung) {

        this.vorhanden = (boolean[]) andereVerwaltung.vorhanden.clone();
        this.storedElements = andereVerwaltung.storedElements;
        this.minElement = andereVerwaltung.minElement;
        this.maxElement = andereVerwaltung.maxElement;
        this.storedHashCode = andereVerwaltung.storedHashCode;
    }

    /**
     * Liefert ein neue Instanz dieser Klasse, die zu <CODE>this</CODE> gleich
     * ist.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return (new BoolElemVerwalt(this));
    }

    /**
     * Liefert den hashCode dieses Objekts.
     *
     * @return  Den hashCode dieses Objekts.
     */
    public int hashCode() {

        if (storedHashCode == 0) {
            storedHashCode = (new BitMathIntSet(toArray())).hashCode();
        }

        return storedHashCode;
    }

    /**
     * Ermittelt, ob das übergebene Objekte eine BoolElemVerwalt ist und zu
     * <CODE>this</CODE> gleich ist.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>BoolElemVerwalt</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das übergebene Objekt eine
     *          <CODE>BoolElemVerwalt</CODE> ist und die gleichen Elemente
     *          wie diese Instanz enthält, anderenfalls <CODE>false</CODE>.
     */
    public boolean equals(Object anderesObjekt) {
        BoolElemVerwalt andereVerwaltung;

        if (anderesObjekt == this) {
            return true;
        }

        if (!(anderesObjekt instanceof BoolElemVerwalt)) {
            return false;
        }

        andereVerwaltung = (BoolElemVerwalt) anderesObjekt;

        if (andereVerwaltung.hashCode() != hashCode()) {
            return false;
        } else {
            return Arrays.equals(andereVerwaltung.vorhanden, vorhanden);
        }
    }

    /**
     * Nimmt das übergebene Element in die Verwaltung auf. Wenn das Element
     * schon in der Verwaltung enthalten ist, ändert sich nichts.
     *
     * @param element  Das Element, der in <CODE>this</CODE> aufgenommern
     *                 werden soll.
     *
     * @throws IndexOutOfBoundsException  Das übergebene Element ist kleiner
     *                                    als Null oder größer als das maximal
     *                                    zu speichernde Element.
     */
    public void elementAufnehmen(int element) {

        if (!vorhanden[element]) {
            // Das Element ist noch nicht enthalten.
            if ((storedElements == 0)
                    || (minElement >= 0) && (element < minElement)) {
                minElement = element;
            }
            if ((storedElements == 0)
                    || (maxElement >= 0) && (element > maxElement)) {
                maxElement = element;
            }

            vorhanden[element] = true;
            storedElements++;

            storedHashCode = 0;
        }
    }

    /**
     * Nimmt die Elemente aus dem übergebenen <CODE>BitMathIntSet</CODE> in
     * die Verwaltung auf. Wenn ein Elemente schon in der Verwaltung enthalten
     * ist, wird es nicht noch einmal aufgenommen.
     *
     * @param hinzuElemente  Ein <CODE>BitMathIntSet</CODE> mit den Elementen,
     *                       die in <CODE>this</CODE> aufgenommen werden
     *                       sollen.
     *
     * @throws IndexOutOfBoundsException  Ein Element des übergebenen Arrays
     *                                    ist größer als das maximal zu
     *                                    speichernde Element.
     */
    public void elementeAufnehmen(BitMathIntSet hinzuElemente) {
        int[] neueElemente;
        int   element;

        if (hinzuElemente.getMaximum() >= vorhanden.length) {
            throw new IndexOutOfBoundsException("Zu großes Element: "
                                                + hinzuElemente.getMaximum());
        }

        neueElemente = hinzuElemente.toArray();

        if ((storedElements == 0)
                || (minElement >= 0) && (hinzuElemente.getMinimum() < minElement)) {
            minElement = hinzuElemente.getMinimum();
        }
        if ((storedElements == 0)
                || (maxElement >= 0) && (hinzuElemente.getMaximum() > maxElement)) {
            maxElement = hinzuElemente.getMaximum();
        }

        for (int i = 0; i < neueElemente.length; i++) {
            element = neueElemente[i];
            if (!vorhanden[element]) {
                // Das Element ist noch nicht enthalten.
                vorhanden[element] = true;
                storedElements++;
            }
        }

        storedHashCode = 0;
    }

    /**
     * Löscht das übergebene Elemente aus der Verwaltung. Wenn das Element
     * nicht in der Verwaltung enthalten ist, ändert sich nichts.
     *
     * @param element  Das Element, der aus <CODE>this</CODE> gelöscht werden
     *                 soll.
     *
     * @throws IndexOutOfBoundsException  Das übergebene Element ist kleiner
     *                                    als Null oder größer als das maximal
     *                                    zu speichernde Element.
     */
    public void elementLoeschen(int element) {

        if (vorhanden[element]) {
            // Das Element ist enthalten.
            vorhanden[element] = false;
            storedElements--;

            if ((minElement >= 0) && (element == minElement)) {
                minElement = -1;
            }
            if ((maxElement >= 0) && (element == maxElement)) {
                maxElement = -1;
            }

            storedHashCode = 0;
        }
    }

    /**
     * Ermittelt, ob das übergebene Element in dieser Verwaltung enthalten
     * ist.
     *
     * @param element  Das Element, von dem ermittelt werden soll, ob es in
     *                 <CODE>this</CODE> enthalten ist.
     *
     * @return  <CODE>true</CODE>, falls das übergebene Element in
     *          <CODE>this</CODE> enthalten ist, anderenfalls
     *          <CODE>false</CODE>.
     *
     * @throws IndexOutOfBoundsException  Das übergebene Element ist kleiner
     *                                    als Null oder größer als das maximal
     *                                    zu speichernde Element.
     */
    public boolean contains(int element) {
        return vorhanden[element];
    }

    /**
     * Ermittelt, ob diese Verwaltung und die übergebene Verwaltung kein
     * Element gemeinsam haben.
     *
     * @param andereVerwaltung  Die andere Verwaltung, deren Elemente mit
     *                          denen dieser Verwaltung verglichen werden
     *                          sollen.
     *
     * @return  <CODE>true</CODE> wenn es kein Element gibt, das sowohl in
     *          dieser wie in der übergebenen Verwaltung enthalten ist, sonst
     *          <CODE>false</CODE>.
     */
    public boolean containsNone(ElementVerwaltung andereVerwaltung) {
        BitMathIntSet dieseElemente, andereElmente;
        boolean[]     andereVorhanden;
        int           minVglElem, maxVglElem;

        if (andereVerwaltung instanceof BoolElemVerwalt) {
            andereVorhanden = ((BoolElemVerwalt) andereVerwaltung).vorhanden;

            minVglElem = Math.max(getMinimum(), andereVerwaltung.getMinimum());
            maxVglElem = Math.min(getMaximum(), andereVerwaltung.getMaximum());

            for (int i = minVglElem; i <= maxVglElem; i++) {
                if (vorhanden[i] && andereVorhanden[i]) {
                    return false;
                }
            }

            return true;
        } else {
            dieseElemente = new BitMathIntSet(toArray());
            andereElmente = new BitMathIntSet(andereVerwaltung.toArray());
            return dieseElemente.isDisjoint(andereElmente);
        }
    }

    /**
     * Ermittelt, ob diese Verwaltung alle Elemente der übergebene Verwaltung
     * enthält.
     *
     * @param andereVerwaltung  Die andere Verwaltung, von deren Elementen
     *                          ermittelt werden soll, ob sie in dieser
     *                          Verwaltung enthalten sind.
     *
     * @return  <CODE>true</CODE> diese Verwaltung alle Elemente der
     *          übergebene Verwaltung enthält, sonst <CODE>false</CODE>.
     */
    public boolean containsAll(ElementVerwaltung andereVerwaltung) {
        BitMathIntSet dieseElemente, andereElmente;
        boolean[]     andereVorhanden;
        int           minVglElem, maxVglElem;

        if ((getMinimum() > andereVerwaltung.getMinimum())
                || (getMaximum() < andereVerwaltung.getMaximum())) {
            return false;
        } else if (andereVerwaltung instanceof BoolElemVerwalt) {
            andereVorhanden = ((BoolElemVerwalt) andereVerwaltung).vorhanden;

            minVglElem = andereVerwaltung.getMinimum();
            maxVglElem = andereVerwaltung.getMaximum();

            for (int i = minVglElem; i <= maxVglElem; i++) {
                if (!vorhanden[i] && andereVorhanden[i]) {
                    return false;
                }
            }

            return true;
        } else {
            dieseElemente = new BitMathIntSet(toArray());
            andereElmente = new BitMathIntSet(andereVerwaltung.toArray());
            return dieseElemente.isSuperset(andereElmente);
        }
    }

    /**
     * Liefert die Anzahl der Elemente, die in dieser Verwaltung aber nicht in
     * der übergebenen Verwaltung enthalten sind.
     *
     * @param andereVerwaltung  Die andere Verwaltung, zu der ermittelt werden
     *                          soll, wie viele Elemente dieser Verwaltung sie
     *                          nicht enthält.
     *
     * @return  Die Anzahl der Elemente, die in <CODE>this</CODE> aber nicht
     *          in der übergebenen Verwaltung enthalten sind.
     */
    public int containingAlone(ElementVerwaltung andereVerwaltung) {
        BitMathIntSet dieseElemente, andereElmente;
        boolean[]     andereVorhanden;
        int           alleineEnthalt;
        int           minVglElem, midVglElem, maxVglElem;

        if (andereVerwaltung instanceof BoolElemVerwalt) {
            andereVorhanden = ((BoolElemVerwalt) andereVerwaltung).vorhanden;

            minVglElem = getMinimum();
            midVglElem = Math.min(getMaximum(), andereVerwaltung.getMaximum());
            maxVglElem = getMaximum();

            alleineEnthalt = 0;
            for (int i = minVglElem; i <= midVglElem; i++) {
                if (vorhanden[i] && !andereVorhanden[i]) {
                    alleineEnthalt++;
                }
            }
            for (int i = midVglElem + 1; i <= maxVglElem; i++) {
                if (vorhanden[i]) {
                    alleineEnthalt++;
                }
            }
            return alleineEnthalt;
        } else {
            dieseElemente = new BitMathIntSet(toArray());
            andereElmente = new BitMathIntSet(andereVerwaltung.toArray());
            return dieseElemente.difference(andereElmente).size();
        }
    }

    /**
     * Liefert das kleinsten in der Verwaltung enthaltene Element. Wenn kein
     * Element in der Verwaltung enthalten ist, wird der Wert <CODE>-1</CODE>
     * geliefert.
     *
     * @return  Das kleinsten in <CODE>this</CODE> enthaltenen Element.
     */
    public int getMinimum() {
        int element;

        if (storedElements == 0) {
            return -1;
        } else {
            if (minElement < 0) {
                element = 0;
                while (!vorhanden[element]) {
                    element++;
                }
                minElement = element;
            }

            return minElement;
        }
    }

    /**
     * Liefert das größte in der Verwaltung enthaltene Element. Wenn kein
     * Element in der Verwaltung enthalten ist, wird der Wert <CODE>-1</CODE>
     * geliefert.
     *
     * @return  Das größte in <CODE>this</CODE> enthaltenen Element.
     */
    public int getMaximum() {
        int element;

        if (storedElements == 0) {
            return -1;
        } else {
            if (maxElement < 0) {
                element = vorhanden.length - 1;
                while (!vorhanden[element]) {
                    element--;
                }
                maxElement = element;
            }

            return maxElement;
        }
    }

    /**
     * Liefert das nächste in der Verwaltung enthaltene Element.
     *
     * @param element  Das Element, ab dem das nächste in der Verwaltung
     *                 enthaltene Element ermittelt werden soll. In der Regel
     *                 ist das übergebene Element das, das zuletzt von dieser
     *                 Methode geliefert wurde.
     *
     * @return  Das kleinste Element, das größer als <CODE>element</CODE> ist
     *          und in der Verwaltung enthalten ist. Falls es so ein Element
     *          nicht gibt, wird der Wert -1 geliefert.
     */
    public int getNext(int element) {
        int naechstElement;

        if (element >= getMaximum()) {
            // Entweder ist kein Kosten-Element vorhanden oder der Index ist
            // größer als oder gleich dem größten Kosten-Element.
            return -1;
        } else if (element < getMinimum()) {
            // Das Element ist kleiner als das kleinste Kosten-Element.
            return getMinimum();
        } else {
            // Das Element ist zumindest kleiner als das größte
            // Kosten-Element.
            naechstElement = element + 1;
            while (!vorhanden[naechstElement]) {
                naechstElement++;
            }
            return naechstElement;
        }
    }

    /**
     * Liefert die Anzahl der Elemente dieser Verwaltung.
     *
     * @return  Die Anzahl der Elemente von <CODE>this</CODE>.
     */
    public int size() {
        return storedElements;
    }

    /**
     * Liefert ein Arry mit den aufsteigend geordneten Elementen dieser
     * Verwaltung.
     *
     * @return  Ein Arry mit den aufsteigend geordneten Elementen dieser
     *          Verwaltung.
     */
    public int[] toArray() {
        int[] enthalteneElemente = new int[storedElements];
        int   indexNr;
        int   naechstElement;

        indexNr = 0;
        naechstElement = 0;
        while (indexNr < storedElements) {
            if (vorhanden[naechstElement]) {
                enthalteneElemente[indexNr] = naechstElement;
                indexNr++;
            }
            naechstElement++;
        }
        return enthalteneElemente;
    }

    /**
     * Liefert eine Mengen-Darstellung der Verwaltung, d.h. der in ihr
     * enthaltenen Elemente, als String.
     *
     * @return  Einen String, der <CODE>this</CODE> darstellt.
     */
    public String toString() {
        return (new BitMathIntSet(toArray())).toString();
    }
}

