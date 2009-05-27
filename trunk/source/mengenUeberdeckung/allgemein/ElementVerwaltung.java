/*
 * Dateiname      : ElementVerwaltung.java
 * Letzte �nderung: 19. Juni 2006
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


package mengenUeberdeckung.allgemein;

import mathCollection.BitMathIntSet;

/**
 * Definiert die Verwaltung einer Menge von Elementen, die ganze Zahlen gr��er
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
     * Ermittelt, ob das �bergebene Objekte eine ElementVerwaltung ist und zu
     * <CODE>this</CODE> gleich ist.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>ElementVerwaltung</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das �bergebene Objekt eine
     *          <CODE>ElementVerwaltung</CODE> ist und die gleichen Indices
     *          enth�lt, anderenfalls <CODE>false</CODE>.
     */
    public boolean equals(Object anderesObjekt);

    /**
     * Nimmt das �bergebene Element in die Verwaltung auf. Wenn das Element
     * schon in der Verwaltung enthalten ist, �ndert sich nichts.
     *
     * @param element  Das Element, der in <CODE>this</CODE> aufgenommern
     *                 werden soll.
     */
    public void elementAufnehmen(int element);

    /**
     * Nimmt die Elemente aus dem �bergebenen <CODE>BitMathIntSet</CODE> in
     * die Verwaltung auf. Wenn ein Elemente schon in der Verwaltung enthalten
     * ist, wird es nicht noch einmal aufgenommen.
     *
     * @param hinzuElemente  Ein <CODE>BitMathIntSet</CODE> mit den Elementen,
     *                       die in <CODE>this</CODE> aufgenommen werden
     *                       sollen.
     */
    public void elementeAufnehmen(BitMathIntSet hinzuElemente);

    /**
     * L�scht das �bergebene Elemente aus der Verwaltung. Wenn das Element
     * nicht in der Verwaltung enthalten ist, �ndert sich nichts.
     *
     * @param element  Das Element, der aus <CODE>this</CODE> gel�scht werden
     *                 soll.
     */
    public void elementLoeschen(int element);

    /**
     * Ermittelt, ob das �bergebene Element in dieser Verwaltung enthalten
     * ist.
     *
     * @param element  Das Element, von dem ermittelt werden soll, ob es in
     *                 <CODE>this</CODE> enthalten ist.
     *
     * @return  <CODE>true</CODE>, falls das �bergebene Element in
     *          <CODE>this</CODE> enthalten ist, anderenfalls
     *          <CODE>false</CODE>.
     */
    public boolean contains(int element);

    /**
     * Ermittelt, ob diese Verwaltung und die �bergebene Verwaltung kein
     * Element gemeinsam haben.
     *
     * @param andereVerwaltung  Die andere Verwaltung, deren Elemente mit
     *                          denen dieser Verwaltung verglichen werden
     *                          sollen.
     *
     * @return  <CODE>true</CODE> wenn es kein Element gibt, das sowohl in
     *          dieser wie in der �bergebenen Verwaltung enthalten ist, sonst
     *          <CODE>false</CODE>.
     */
    public boolean containsNone(ElementVerwaltung andereVerwaltung);

    /**
     * Ermittelt, ob diese Verwaltung alle Elemente der �bergebene Verwaltung
     * enth�lt.
     *
     * @param andereVerwaltung  Die andere Verwaltung, von deren Elementen
     *                          ermittelt werden soll, ob sie in dieser
     *                          Verwaltung enthalten sind.
     *
     * @return  <CODE>true</CODE> diese Verwaltung alle Elemente der
     *          �bergebene Verwaltung enth�lt, sonst <CODE>false</CODE>.
     */
    public boolean containsAll(ElementVerwaltung andereVerwaltung);

    /**
     * Liefert die Anzahl der Elemente, die in dieser Verwaltung aber nicht in
     * der �bergebenen Verwaltung enthalten sind.
     *
     * @param andereVerwaltung  Die andere Verwaltung, zu der ermittelt werden
     *                          soll, wie viele Elemente dieser Verwaltung sie
     *                          nicht enth�lt.
     *
     * @return  Die Anzahl der Elemente, die in <CODE>this</CODE> aber nicht
     *          in der �bergebenen Verwaltung enthalten sind.
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
     * Liefert das gr��te in der Verwaltung enthaltene Element. Wenn kein
     * Element in der Verwaltung enthalten ist, wird der Wert <CODE>-1</CODE>
     * geliefert.
     *
     * @return  Das gr��te in <CODE>this</CODE> enthaltenen Element.
     */
    public int getMaximum();

    /**
     * Liefert das n�chste in der Verwaltung enthaltene Element.
     *
     * @param element  Das Element, ab dem das n�chste in der Verwaltung
     *                 enthaltene Element ermittelt werden soll. In der Regel
     *                 ist das �bergebene Element das, das zuletzt von dieser
     *                 Methode geliefert wurde.
     *
     * @return  Das kleinste Element, das gr��er als <CODE>element</CODE> ist
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

