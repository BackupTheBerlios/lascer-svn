/*
 * Dateiname      : TeilmengenVerwaltung.java
 * Letzte �nderung: 12. August 2006
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


package mengenUeberdeckung.allgemein.itmVerwaltung;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Definiert die Methoden zur Verwaltund der Teilmengen einer Familie. Es
 * werden insbesondere Methoden in Bezug auf die Notwendigkeit der Teilmengen
 * bereit gestellt.
 *
 * @author  Dietmar Lippold
 */
public interface TeilmengenVerwaltung {

    /**
     * Liefert ein neues Objekt, das zu <CODE>this</CODE> gleich ist. Die
     * Menge der enthaltenen Teilmengen wird dabei neu erzeugt. Bei den
     * enthaltenen Teilmengen handelt es sich aber um die gleichen Instanzen.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone();

    /**
     * Liefert den hashCode der Instanz. Das ist der hashCode eines
     * <CODE>HashSet</CODE> mit den enthaltenen Teilmengen und h�ngt daher
     * nicht von der dieses Interface implementierenden Klasse ab.
     *
     * @return  Den hashCode der Instanz.
     */
    public int hashCode();

    /**
     * Ermittelt, ob das �bergebene Objekt eine <CODE>TeilmengenVerwaltung</CODE>
     * ist und die gleichen Teilmengen wie <CODE>this</CODE> enth�lt. Der
     * Wert h�ngt daher nicht von der dieses Interface implementierenden
     * Klasse ab.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>TeilmengenVerwaltung</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das �bergebene Objekt eine
     *          <CODE>TeilmengenVerwaltung</CODE> ist und die gleichen
     *          Teilmengen wie diese Instanz enth�lt, anderenfalls
     *          <CODE>false</CODE>.
     */
    public boolean equals(Object anderesObjekt);

    /**
     * L�scht die Teilmengenverwaltung, d.h. setzt sie auf den Zustand nach
     * der Erzeugung zur�ck. Dabei werden alle Teilmengen aus der Verwaltung
     * entfernt.
     */
    public void clear();

    /**
     * Ermittelt, ob die �bergebene Teilmenge in dieser Verwaltung enthalten
     * ist.
     *
     * @param teilmenge  Eine Teilmenge, von der ermittelt wird, ob sie in
     *                   <CODE>this</CODE> enthalten ist.
     *
     * @return  <CODE>true</CODE>, wenn <CODE>this</CODE> die �bergebene
     *          Teilmenge enth�lt, anderenfalls <CODE>false</CODE>.
     */
    public boolean enthaelt(IndexTeilmenge teilmenge);

    /**
     * F�gt der Verwaltung die �bergebene Teilmenge hinzu. Wenn die Teilmenge
     * schon enthalten ist, �ndert sich nichts.
     *
     * @param teilmenge          Die Teilmenge, die <CODE>this</CODE>
     *                           hinzuef�gt werden soll.
     * @param indexUeberdeckAnz  Die H�ufigkeit, mit der jeder Index bereits
     *                           �berdeckt wird.
     */
    public void teilmengeHinzufuegen(IndexTeilmenge teilmenge,
                                     int[] indexUeberdeckAnz);

    /**
     * Entfernt die �bergebene Teilmenge aus der Verwaltung. Wenn die
     * Teilmenge nicht enthalten ist, �ndert sich nichts.
     *
     * @param teilmenge          Die Teilmenge, die aus <CODE>this</CODE>
     *                           entfernt werden soll.
     * @param indexUeberdeckAnz  Die H�ufigkeit, mit der jeder Index bereits
     *                           �berdeckt wird.
     */
    public void teilmengeEntfernen(IndexTeilmenge teilmenge,
                                   int[] indexUeberdeckAnz);

    /**
     * Liefert die notwendigen Teilmengen der Verwaltung, d.h. die Teilmengen,
     * die mindestens ein Element enthalten, das in keiner anderen Teilmenge
     * der Verwaltung enthalten ist.
     *
     * @return  Ein <CODE>Set</CODE> mit den notwendigen Teilmengen von
     *          <CODE>this</CODE>.
     */
    public Set notwendigeTeilmengen();

    /**
     * Liefert die nicht notwendigen Teilmengen der Verwaltung, d.h. die
     * Teilmengen, die nur Elemente enthalten, die auch in mindestens einer
     * anderen Teilmenge der Verwaltung enthalten sind.
     *
     * @return  Ein <CODE>Set</CODE> mit den nicht notwendigen Teilmengen von
     *          <CODE>this</CODE>.
     */
    public Set nichtNotwendigeTeilmengen();

    /**
     * Liefert die notwendigen Teilmengen der Verwaltung, die nach Hinzunahme
     * der �bergebenen Teilmenge nicht mehr notwendig w�ren. Wenn die
     * Teilmenge schon in der Verwaltung enthalten ist, wird eine leere Menge
     * geliefert.
     *
     * @param teilmenge  Die Teilmenge, f�r die bestimmt werden soll, welche
     *                   derzeit notwendigen Teilmengen nicht mehr notwendig
     *                   w�rden.
     *
     * @return  Ein <CODE>Set</CODE> mit den Teilmengen, die nach Hinzunahme
     *          der �bergebenen Teilmenge nicht mehr notwendig w�ren.
     */
    public Set neuNichtNotwendigeTeilmengen(IndexTeilmenge teilmenge);

    /**
     * Ermittelt, ob die �bergebenen Teilmenge mindestens die �bergebene
     * Anzahl von Indices enth�lt, die in keiner bzw. keiner anderen Teilmenge
     * der Verwaltung enthalten sind. Diese Indices sind genau dann in einer
     * Teilmenge der Verwaltung enthalten, wenn die �bergebene Teilmenge in
     * der Verwaltung enthalten ist.
     *
     * @param alleinAnz        Die Anzahl der Indices, von der gepr�ft wird,
     *                         ob sie die Teilmenge alleine enth�lt. Der Wert
     *                         mu� gr��er als Null sein.
     * @param teilmenge        Die Teilmenge, f�r die ermittelt wird, wie
     *                         viele Indices sie als Teilmenge der Verwaltung
     *                         alleine enth�lt.
     * @param ueberdeckt       Die Indices, die in mindestens einer Teilmenge
     *                         der Verwaltung enthalten sind.
     * @param nichtUeberdeckt  Die Indices, die in keiner Teilmenge der
     *                         Verwaltung enthalten sind.
     *
     * @return  <CODE>true</CODE> wenn die Teilmenge die angegebene Anzahl von
     *          Indices alleine enth�lt, sonst <CODE>false</CODE>.
     */
    public boolean ueberdecktAlleineAnz(int alleinAnz, IndexTeilmenge teilmenge,
                                        IndexTeilmenge ueberdeckt,
                                        IndexTeilmenge nichtUeberdeckt);

    /**
     * Liefert die Anzahl der Indices, die in der �bergebenen Teilmenge aber
     * in keiner bzw. keiner anderen Teilmenge der Verwaltung enthalten sind.
     * Die Indices sind genau dann in einer Teilmenge der Verwaltung
     * enthalten, wenn die �bergebene Teilmenge in der Verwaltung enthalten
     * ist.
     *
     * @param teilmenge        Die Teilmenge, f�r die ermittelt wird, wie
     *                         viele Indices sie als Teilmenge der Verwaltung
     *                         alleine enth�lt.
     * @param ueberdeckt       Die Indices, die in mindestens einer Teilmenge
     *                         der Verwaltung enthalten sind.
     * @param nichtUeberdeckt  Die Indices, die in keiner Teilmenge der
     *                         Verwaltung enthalten sind.
     *
     * @return  Die Anzahl der Indices, die in der �bergebenen Teilmenge aber
     *          in keiner bzw. keiner anderen Teilmenge der Verwaltung
     *          enthalten sind.
     */
    public int anzAlleineUeberdeckt(IndexTeilmenge teilmenge,
                                    IndexTeilmenge ueberdeckt,
                                    IndexTeilmenge nichtUeberdeckt);

    /**
     * Liefert die Anzahl der notwendigen Teilmengen der Verwaltung, d.h. die
     * Anzahl der Teilmengen, die mindestens ein Element enthalten, das in
     * keiner anderen Teilmenge der Verwaltung enthalten ist. Gegen�ber dem
     * Aufruf von <CODE>notwendigeTeilmengen().size()</CODE> ist diese Methode
     * effizienter.
     *
     * @return  Die Anzahl der notwendigen Teilmengen von <CODE>this</CODE>.
     */
    public int anzNotwendigeTeilmengen();

    /**
     * Liefert die Anzahl der nicht notwendigen Teilmengen der Verwaltung, d.h.
     * die Anzahl der Teilmengen, die nur Elemente enthalten, die auch in
     * mindestens einer anderen Teilmenge der Verwaltung enthalten sind.
     * Gegen�ber dem Aufruf von <CODE>nichtNotwendigeTeilmengen().size()</CODE>
     * ist diese Methode effizienter.
     *
     * @return  Die Anzahl der nicht notwendigen Teilmengen von
     *          <CODE>this</CODE>.
     */
    public int anzNichtNotwendigeTeilmengen();

    /**
     * Liefert die Gr��e der Verwaltung, d.h. die Anzahl der in der Verwaltung
     * enthaltenen Teilmengen. Dies ist die Summe der Werte von
     * <CODE>notwendigeTeilmengenAnz()</CODE> und
     * <CODE>nichtNotwendigeTeilmengenAnz()</CODE>.
     *
     * @return  Die Anzahl Teilmengen von <CODE>this</CODE>.
     */
    public int groesseFamilie();

    /**
     * Liefert einen Iterator �ber die Teilmengen der Verwaltung. Die
     * Teilmengen werden von diesem in keiner bestimmten Reihenfolge
     * geliefert.
     *
     * @return  Einen Iterator �ber die Teilmengen der Verwaltung.
     */
    public Iterator iterator();

    /**
     * Liefert eine neue Menge mit den Teilmengen der Verwaltung.
     *
     * @return  Ein neues <CODE>HashSet</CODE> mit den Teilmengen von
     *          <CODE>this</CODE>.
     */
    public HashSet toHashSet();

    /**
     * Liefert ein Array mit den zur Verwaltung geh�renden Teilmengen. Die
     * Reihenfolge der Teilmengen im Array ist nicht festgelegt.
     *
     * @return  Ein Array der Teilmengen von <CODE>this</CODE>.
     */
    public IndexTeilmenge[] toArray();
}

