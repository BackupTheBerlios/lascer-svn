/*
 * Dateiname      : BitIntElemVerwalt.java
 * Letzte Änderung: 26. Mai 2006
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
 * Realisiert eine Verwaltungen von Elementen, die nicht-negative ganze
 * Zahlen sind, durch Bits oder Integer-Werte.
 *
 * @author  Dietmar Lippold
 */
public class BitIntElemVerwalt implements ElementVerwaltung, Cloneable {

    /**
     * Das Feld enthält für jedes mögliche Element die Angabe, ob es in der
     * Verwaltung enthalten ist.
     */
    private BitMathIntSet elemente;

    /**
     * Ein Array mit den aufsteigend geordneten Elementen der Verwaltung. Die
     * Elemente werden nur in einer Laufzeit-effizienten Instanz gespeichert.
     * Ansonsten ist der Wert Wert <CODE>null</CODE>.
     */
    private int[] elementListe = null;

    /**
     * Die Position des zuletzt gelieferten Elements aus
     * <CODE>elementListe</CODE>.
     */
    private int letzteElementPos = -1;

    /**
     * Gibt an, ob eine Speicher-effiziente Speicherung der Elemente mit
     * Nachteilen für die Laufzeit-Effizienz erfolgen soll.
     */
    private boolean speicherEffizient;

    /**
     * Erzeugt eine neue Instanz, die die übergebenen Elemente enthält.
     *
     * @param elemente           Die initial enthaltenen Elemente. Die Menge
     *                           wird ohne Kopieren als Attribut gespeichert,
     *                           darf also in der Regel anschließend nicht
     *                           verändert werden.
     * @param speicherEffizient  Gibt an, ob eine Speicher-effiziente und
     *                           damit aber nicht Laufzeit-effiziente
     *                           Speicherung erfolgen soll.
     */
    public BitIntElemVerwalt(BitMathIntSet elemente,
                             boolean speicherEffizient) {

        this.elemente = elemente;
        this.speicherEffizient = speicherEffizient;
    }

    /**
     * Erzeugt eine neue Instanz, die noch kein Element enthält.
     *
     * @param maxElementAnz      Die maximale Anzahl zu speichernder Elemente.
     * @param speicherEffizient  Gibt an, ob eine Speicher-effiziente und
     *                           damit aber nicht Laufzeit-effiziente
     *                           Speicherung erfolgen soll.
     */
    public BitIntElemVerwalt(int maxElementAnz, boolean speicherEffizient) {

        this.elemente = new BitMathIntSet(maxElementAnz);
        this.speicherEffizient = speicherEffizient;
    }

    /**
     * Erzeugt eine neue Instanz, die noch kein Element enthält und die nicht
     * extra Speicher-effizient ist.
     *
     * @param maxElementAnz  Die maximale Anzahl zu speichernder Elemente.
     */
    public BitIntElemVerwalt(int maxElementAnz) {
        this(maxElementAnz, false);
    }

    /**
     * Erzeugt eine neue Instanz, die gleich ist zu übergebenen Instanz. Die
     * enthaltenen Elemente werden tief kopiert.
     *
     * @param andereVerwaltung  Die Instanz, zu der die neue Instanz gleich
     *                          sein soll.
     */
    public BitIntElemVerwalt(BitIntElemVerwalt andereVerwaltung) {

        this.elemente = (BitMathIntSet) andereVerwaltung.elemente.clone();
        if (andereVerwaltung.elementListe != null) {
            this.elementListe = (int[]) andereVerwaltung.elementListe.clone();
        }
        this.letzteElementPos = andereVerwaltung.letzteElementPos;
        this.speicherEffizient = andereVerwaltung.speicherEffizient;
    }

    /**
     * Erzeugt eine neue Instanz, die bis auf die Speicher-Effizienz identisch
     * ist mit der übergebenen Instanz. Die enthaltenen Elemente werden flach
     * kopiert.
     *
     * @param andereVerwaltung   Die Instanz, zu der die neue Instanz bis auf
     *                           die Speicher-Effizienz identisch sein soll.
     * @param speicherEffizient  Gibt an, ob eine Speicher-effiziente und
     *                           damit aber nicht Laufzeit-effiziente
     *                           Speicherung erfolgen soll.
     */
    public BitIntElemVerwalt(BitIntElemVerwalt andereVerwaltung,
                             boolean speicherEffizient) {

        this.elemente = andereVerwaltung.elemente;
        if (!speicherEffizient) {
            this.elementListe = andereVerwaltung.elementListe;
            this.letzteElementPos = andereVerwaltung.letzteElementPos;
        }
        this.speicherEffizient = speicherEffizient;
    }

    /**
     * Liefert ein neue Instanz dieser Klasse, die zu <CODE>this</CODE> gleich
     * ist.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return (new BitIntElemVerwalt(this));
    }

    /**
     * Liefert den hashCode dieses Objekts.
     *
     * @return  Den hashCode dieses Objekts.
     */
    public int hashCode() {
        return elemente.hashCode();
    }

    /**
     * Ermittelt, ob das übergebene Objekte eine BitIntElemVerwalt ist und zu
     * <CODE>this</CODE> gleich ist.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>BitIntElemVerwalt</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das übergebene Objekt eine
     *          <CODE>BitIntElemVerwalt</CODE> ist und die gleichen Elemente
     *          wie diese Instanz enthält, anderenfalls CODE>false</CODE>.
     */
    public boolean equals(Object anderesObjekt) {
        BitIntElemVerwalt andereVerwaltung;

        if (anderesObjekt == this) {
            return true;
        }

        if (!(anderesObjekt instanceof BitIntElemVerwalt)) {
            return false;
        }

        andereVerwaltung = (BitIntElemVerwalt) anderesObjekt;
        return elemente.equals(andereVerwaltung.elemente);
    }

    /**
     * Nimmt das übergebene Element in die Verwaltung auf. Wenn das Element
     * schon in der Verwaltung enthalten ist, ändert sich nichts.
     *
     * @param element  Das Element, der in <CODE>this</CODE> aufgenommern
     *                 werden soll.
     */
    public void elementAufnehmen(int element) {

        if (!elemente.contains(element)) {
            elemente.add(element);
            elementListe = null;
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
     */
    public void elementeAufnehmen(BitMathIntSet hinzuElemente) {

        elemente.addAll(hinzuElemente);
        elementListe = null;
    }

    /**
     * Löscht das übergebene Elemente aus der Verwaltung. Wenn das Element
     * nicht in der Verwaltung enthalten ist, ändert sich nichts.
     *
     * @param element  Das Element, der aus <CODE>this</CODE> gelöscht werden
     *                 soll.
     */
    public void elementLoeschen(int element) {

        if (elemente.contains(element)) {
            elemente.remove(element);
            elementListe = null;
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
     */
    public boolean contains(int element) {
        return elemente.contains(element);
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
        BitMathIntSet andereElmente;

        if (andereVerwaltung instanceof BitIntElemVerwalt) {
            andereElmente = ((BitIntElemVerwalt) andereVerwaltung).elemente;
        } else {
            andereElmente = new BitMathIntSet(andereVerwaltung.toArray());
        }
        return elemente.isDisjoint(andereElmente);
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
        BitMathIntSet andereElmente;

        if (andereVerwaltung instanceof BitIntElemVerwalt) {
            andereElmente = ((BitIntElemVerwalt) andereVerwaltung).elemente;
        } else {
            andereElmente = new BitMathIntSet(andereVerwaltung.toArray());
        }
        return elemente.isSuperset(andereElmente);
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
        BitMathIntSet andereElmente;

        if (andereVerwaltung instanceof BitIntElemVerwalt) {
            andereElmente = ((BitIntElemVerwalt) andereVerwaltung).elemente;
        } else {
            andereElmente = new BitMathIntSet(andereVerwaltung.toArray());
        }
        return elemente.difference(andereElmente).size();
    }

    /**
     * Liefert das kleinsten in der Verwaltung enthaltene Element. Wenn kein
     * Element in der Verwaltung enthalten ist, wird der Wert <CODE>-1</CODE>
     * geliefert.
     *
     * @return  Das kleinsten in <CODE>this</CODE> enthaltenen Element.
     */
    public int getMinimum() {
        int minElement;

        if (elementListe != null) {
            if (elementListe.length == 0) {
                return -1;
            } else {
                letzteElementPos = 0;
                return elementListe[0];
            }
        } else {
            minElement = elemente.getMinimum();

            if (!speicherEffizient) {
                if (elementListe == null) {
                    elementListe = elemente.toArray();
                }
                letzteElementPos = 0;
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

        if (elementListe != null) {
            if (elementListe.length == 0) {
                return -1;
            } else {
                return elementListe[elementListe.length - 1];
            }
        } else {
            return elemente.getMaximum();
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

        if ((elementListe != null)
                && (elementListe[letzteElementPos] == element)) {
            if (letzteElementPos + 1 < elementListe.length) {
                letzteElementPos++;
                return elementListe[letzteElementPos];
            } else {
                // Es gibt kein weiteres enthaltenes Element.
                return -1;
            }
        } else {
            return elemente.getNext(element + 1);
        }
    }

    /**
     * Liefert die Anzahl der Elemente dieser Verwaltung.
     *
     * @return  Die Anzahl der Elemente von <CODE>this</CODE>.
     */
    public int size() {

        if (elementListe != null) {
            return elementListe.length;
        } else {
            return elemente.size();
        }
    }

    /**
     * Liefert ein Arry mit den aufsteigend geordneten Elementen dieser
     * Verwaltung.
     *
     * @return  Ein Arry mit den aufsteigend geordneten Elementen dieser
     *          Verwaltung.
     */
    public int[] toArray() {

        if (!speicherEffizient) {
            if (elementListe == null) {
                elementListe = elemente.toArray();
            }
            return elementListe;
        } else {
            return elemente.toArray();
        }
    }

    /**
     * Liefert eine Mengen-Darstellung der Verwaltung, d.h. der in ihr
     * enthaltenen Elemente, als String.
     *
     * @return  Einen String, der <CODE>this</CODE> darstellt.
     */
    public String toString() {
        return elemente.toString();
    }
}

