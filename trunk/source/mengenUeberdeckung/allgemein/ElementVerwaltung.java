/*
 * Dateiname      : ElementVerwaltung.java
 * Letzte Änderung: 19. Juni 2006
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
 * Definiert die Verwaltung einer Menge von Elementen, die ganze Zahlen größer
 * oder gleich Null sind.
 *
 * @author  Dietmar Lippold
 */
public interface ElementVerwaltung {

    /**
     * Liefert ein neue Instanz dieser Klasse, die zu <CODE>this</CODE> gleich
     * ist.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone();

    /**
     * Liefert den hashCode dieses Objekts.
     *
     * @return  Den hashCode dieses Objekts.
     */
    public int hashCode();

    /**
     * Ermittelt, ob das übergebene Objekte eine ElementVerwaltung ist und zu
     * <CODE>this</CODE> gleich ist.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>ElementVerwaltung</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das übergebene Objekt eine
     *          <CODE>ElementVerwaltung</CODE> ist und die gleichen Indices
     *          enthält, anderenfalls <CODE>false</CODE>.
     */
    public boolean equals(Object anderesObjekt);

    /**
     * Nimmt das übergebene Element in die Verwaltung auf. Wenn das Element
     * schon in der Verwaltung enthalten ist, ändert sich nichts.
     *
     * @param element  Das Element, der in <CODE>this</CODE> aufgenommern
     *                 werden soll.
     */
    public void elementAufnehmen(int element);

    /**
     * Nimmt die Elemente aus dem übergebenen <CODE>BitMathIntSet</CODE> in
     * die Verwaltung auf. Wenn ein Elemente schon in der Verwaltung enthalten
     * ist, wird es nicht noch einmal aufgenommen.
     *
     * @param hinzuElemente  Ein <CODE>BitMathIntSet</CODE> mit den Elementen,
     *                       die in <CODE>this</CODE> aufgenommen werden
     *                       sollen.
     */
    public void elementeAufnehmen(BitMathIntSet hinzuElemente);

    /**
     * Löscht das übergebene Elemente aus der Verwaltung. Wenn das Element
     * nicht in der Verwaltung enthalten ist, ändert sich nichts.
     *
     * @param element  Das Element, der aus <CODE>this</CODE> gelöscht werden
     *                 soll.
     */
    public void elementLoeschen(int element);

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
    public boolean contains(int element);

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
    public boolean containsNone(ElementVerwaltung andereVerwaltung);

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
    public boolean containsAll(ElementVerwaltung andereVerwaltung);

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
    public int containingAlone(ElementVerwaltung andereVerwaltung);

    /**
     * Liefert das kleinste in der Verwaltung enthaltene Element. Wenn kein
     * Element in der Verwaltung enthalten ist, wird der Wert <CODE>-1</CODE>
     * geliefert.
     *
     * @return  Das kleinsten in <CODE>this</CODE> enthaltenen Element.
     */
    public int getMinimum();

    /**
     * Liefert das größte in der Verwaltung enthaltene Element. Wenn kein
     * Element in der Verwaltung enthalten ist, wird der Wert <CODE>-1</CODE>
     * geliefert.
     *
     * @return  Das größte in <CODE>this</CODE> enthaltenen Element.
     */
    public int getMaximum();

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
    public int getNext(int element);

    /**
     * Liefert die Anzahl der Elemente dieser Verwaltung.
     *
     * @return  Die Anzahl der Elemente von <CODE>this</CODE>.
     */
    public int size();

    /**
     * Liefert ein Arry mit den aufsteigend geordneten Elementen dieser
     * Verwalung.
     *
     * @return  Ein Arry mit den aufsteigend geordneten Elementen dieser
     *          Verwalung.
     */
    public int[] toArray();

    /**
     * Liefert eine Mengen-Darstellung der Verwaltung, d.h. der in ihr
     * enthaltenen Elemente, als String.
     *
     * @return  Einen String, der <CODE>this</CODE> darstellt.
     */
    public String toString();
}

