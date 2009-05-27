/*
 * Dateiname      : ItmAuswahlErzeugung.java
 * Letzte Änderung: 08. Februar 2006
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


package mengenUeberdeckung.reduktion;

import java.util.Iterator;
import java.util.Collection;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Die Klasse dient der abstrakten Auswahl von IndexTeilmengen aus einer
 * Familie von vorgegebenen IndexTeilmengen, wobei die Auswahl so erfolgen
 * soll, daß es weiterhin möglich ist, aus den ausgewählten IndexTeilmengen
 * eine möglichst umfassende Überdeckung der Gesamtmenge mit möglichst
 * geringen Kosten zu erzeugen.
 *
 * @author  Dietmar Lippold
 */
public abstract class ItmAuswahlErzeugung {

    /**
     * Die Größe der Gesamtmenge.
     */
    private int groesseGesamtmenge;

    /**
     * Erzeugt eine Instanz zu einer Gesamtmenge der übergebenen Größe.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, d.h. die Anzahl
     *                            ihrer Elemente.
     */
    public ItmAuswahlErzeugung(int groesseGesamtmenge) {
        this.groesseGesamtmenge = groesseGesamtmenge;
    }

    /**
     * Liefert die Größe der Gesamtmenge, d.h. die Anzahl der Elemente, die
     * überdeckt werden sollen.
     *
     * @return  Die Größe der Gesamtmenge, zu der die Auswahl erzeugt werden
     *          soll.
     */
    public int groesseGesamtmenge() {
        return groesseGesamtmenge;
    }

    /**
     * Fügt die übergebene Teilmenge einer Familie hinzu, aus der die
     * Auswahl erzeugt wird. Die übergebene Teilmenge und die enthaltenen
     * Teilmengen müssen zu einer Gesamtmenge gleicher Größe gehören.
     *
     * @param teilmenge  Die der Familie hinzuzufügende Teilmenge.
     *
     * @return  <CODE>true</CODE>, wenn die übergebene Teilmenge in die
     *          Auswahl aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     *
     * @throws SizeMismatchException  Die zur Teilmenge gehörende Gesamtmenge
     *                                hat eine andere Größe als die
     *                                Gesamtmenge von <CODE>this</CODE>.
     *
     * @see mengenUeberdeckung.allgemein.SizeMismatchException
     */
    public abstract boolean teilmengeHinzufuegen(IndexTeilmenge teilmenge);

    /**
     * Fügt eine Folge von Teilmenge einer Familie hinzu, aus der die
     * Auswahl erzeugt wird. Die Folge der Teilmengen liefert der übergebene
     * Iterator. Alle neuen und die enthaltenen Teilmengen müssen zu einer
     * Gesamtmenge gleicher Größe gehören.
     *
     * @param itmIterator  Ein Iterator über die der Familie hinzuzufügenden
     *                     Teilmengen.
     *
     * @return  <CODE>true</CODE>, wenn mindestens eine der Teilmengen in die
     *          Auswahl aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     *
     * @throws SizeMismatchException  Eine der Teilmenge des Iterator gehört
     *                                zu einer Gesamtmenge einer anderen Größe
     *                                als die Größe der Gesamtmenge von
     *                                <CODE>this</CODE>.
     *
     * @see mengenUeberdeckung.allgemein.SizeMismatchException
     */
    public boolean teilmengenHinzufuegen(Iterator itmIterator) {
        boolean hinzu = false;

        while (itmIterator.hasNext()) {
            hinzu |= teilmengeHinzufuegen((IndexTeilmenge)itmIterator.next());
        }
        return hinzu;
    }

    /**
     * Fügt die übergebene Collection von Teilmengen einer Familie hinzu, aus
     * der die Auswahl erzeugt wird. Alle neuen und die enthaltenen Teilmengen
     * müssen zu einer Gesamtmenge gleicher Größe gehören.
     *
     * @param teilmengen  Die der Familie hinzuzufügenden Teilmengen.
     *
     * @return  <CODE>true</CODE>, wenn mindestens eine der Teilmengen in die
     *          Auswahl aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     *
     * @throws SizeMismatchException  Eine der übergebenen Teilmenge gehört zu
     *                                einer Gesamtmenge einer anderen Größe
     *                                als die Größe der Gesamtmenge von
     *                                <CODE>this</CODE>.
     *
     * @see mengenUeberdeckung.allgemein.SizeMismatchException
     */
    public boolean teilmengenHinzufuegen(Collection teilmengen) {
        return teilmengenHinzufuegen(teilmengen.iterator());
    }

    /**
     * Gibt an, ob seit der Existens dieser Instanz die maximale Anzahl der
     * auszuwählenden Teilmengen schon einmal erreicht wurde, so daß eine
     * neue Teilmenge nicht aufgenommen oder eine vorhandene entfernt werden
     * mußte.
     *
     * @return  Die Angabe, ob seit der Existens dieser Instanz die maximale
     *          Anzahl der auszuwählenden Teilmengen schon einmal erreicht
     *          wurde.
     */
    public abstract boolean maxAuswahlWurdeErreicht();

    /**
     * Liefert die Größe der Familie der ausgewählten Teilmengen.
     *
     * @return  Die Größe der Familie der ausgewählten Teilmengen.
     */
    public abstract int groesseAuswahl();

    /**
     * Liefert eine neue Familie, die aus den ausgewählten IndexTeilmengen
     * besteht (die konkret Objekte von Unterklassen von IndexTeilmenge
     * sein können). Dabei soll die Auswahl weiterhin die IndexTeilmengen
     * enthalten, aus denen eine möglichst umfassende Überdeckung der
     * Gesamtmenge mit möglichst geringen Kosten erzeugt werden kann.
     *
     * @return  Eine neu erzeugte Familie, die eine Auswahl der hinzugefügten
     *          Teilmengen (oder von Unterklassen davon) enthält.
     */
    public abstract ItmFamilie auswahl();
}

