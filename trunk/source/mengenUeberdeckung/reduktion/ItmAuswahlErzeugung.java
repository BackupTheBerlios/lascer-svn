/*
 * Dateiname      : ItmAuswahlErzeugung.java
 * Letzte �nderung: 08. Februar 2006
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


package mengenUeberdeckung.reduktion;

import java.util.Iterator;
import java.util.Collection;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Die Klasse dient der abstrakten Auswahl von IndexTeilmengen aus einer
 * Familie von vorgegebenen IndexTeilmengen, wobei die Auswahl so erfolgen
 * soll, da� es weiterhin m�glich ist, aus den ausgew�hlten IndexTeilmengen
 * eine m�glichst umfassende �berdeckung der Gesamtmenge mit m�glichst
 * geringen Kosten zu erzeugen.
 *
 * @author  Dietmar Lippold
 */
public abstract class ItmAuswahlErzeugung {

    /**
     * Die Gr��e der Gesamtmenge.
     */
    private int groesseGesamtmenge;

    /**
     * Erzeugt eine Instanz zu einer Gesamtmenge der �bergebenen Gr��e.
     *
     * @param groesseGesamtmenge  Die Gr��e der Gesamtmenge, d.h. die Anzahl
     *                            ihrer Elemente.
     */
    public ItmAuswahlErzeugung(int groesseGesamtmenge) {
        this.groesseGesamtmenge = groesseGesamtmenge;
    }

    /**
     * Liefert die Gr��e der Gesamtmenge, d.h. die Anzahl der Elemente, die
     * �berdeckt werden sollen.
     *
     * @return  Die Gr��e der Gesamtmenge, zu der die Auswahl erzeugt werden
     *          soll.
     */
    public int groesseGesamtmenge() {
        return groesseGesamtmenge;
    }

    /**
     * F�gt die �bergebene Teilmenge einer Familie hinzu, aus der die
     * Auswahl erzeugt wird. Die �bergebene Teilmenge und die enthaltenen
     * Teilmengen m�ssen zu einer Gesamtmenge gleicher Gr��e geh�ren.
     *
     * @param teilmenge  Die der Familie hinzuzuf�gende Teilmenge.
     *
     * @return  <CODE>true</CODE>, wenn die �bergebene Teilmenge in die
     *          Auswahl aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     *
     * @throws SizeMismatchException  Die zur Teilmenge geh�rende Gesamtmenge
     *                                hat eine andere Gr��e als die
     *                                Gesamtmenge von <CODE>this</CODE>.
     *
     * @see mengenUeberdeckung.allgemein.SizeMismatchException
     */
    public abstract boolean teilmengeHinzufuegen(IndexTeilmenge teilmenge);

    /**
     * F�gt eine Folge von Teilmenge einer Familie hinzu, aus der die
     * Auswahl erzeugt wird. Die Folge der Teilmengen liefert der �bergebene
     * Iterator. Alle neuen und die enthaltenen Teilmengen m�ssen zu einer
     * Gesamtmenge gleicher Gr��e geh�ren.
     *
     * @param itmIterator  Ein Iterator �ber die der Familie hinzuzuf�genden
     *                     Teilmengen.
     *
     * @return  <CODE>true</CODE>, wenn mindestens eine der Teilmengen in die
     *          Auswahl aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     *
     * @throws SizeMismatchException  Eine der Teilmenge des Iterator geh�rt
     *                                zu einer Gesamtmenge einer anderen Gr��e
     *                                als die Gr��e der Gesamtmenge von
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
     * F�gt die �bergebene Collection von Teilmengen einer Familie hinzu, aus
     * der die Auswahl erzeugt wird. Alle neuen und die enthaltenen Teilmengen
     * m�ssen zu einer Gesamtmenge gleicher Gr��e geh�ren.
     *
     * @param teilmengen  Die der Familie hinzuzuf�genden Teilmengen.
     *
     * @return  <CODE>true</CODE>, wenn mindestens eine der Teilmengen in die
     *          Auswahl aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     *
     * @throws SizeMismatchException  Eine der �bergebenen Teilmenge geh�rt zu
     *                                einer Gesamtmenge einer anderen Gr��e
     *                                als die Gr��e der Gesamtmenge von
     *                                <CODE>this</CODE>.
     *
     * @see mengenUeberdeckung.allgemein.SizeMismatchException
     */
    public boolean teilmengenHinzufuegen(Collection teilmengen) {
        return teilmengenHinzufuegen(teilmengen.iterator());
    }

    /**
     * Gibt an, ob seit der Existens dieser Instanz die maximale Anzahl der
     * auszuw�hlenden Teilmengen schon einmal erreicht wurde, so da� eine
     * neue Teilmenge nicht aufgenommen oder eine vorhandene entfernt werden
     * mu�te.
     *
     * @return  Die Angabe, ob seit der Existens dieser Instanz die maximale
     *          Anzahl der auszuw�hlenden Teilmengen schon einmal erreicht
     *          wurde.
     */
    public abstract boolean maxAuswahlWurdeErreicht();

    /**
     * Liefert die Gr��e der Familie der ausgew�hlten Teilmengen.
     *
     * @return  Die Gr��e der Familie der ausgew�hlten Teilmengen.
     */
    public abstract int groesseAuswahl();

    /**
     * Liefert eine neue Familie, die aus den ausgew�hlten IndexTeilmengen
     * besteht (die konkret Objekte von Unterklassen von IndexTeilmenge
     * sein k�nnen). Dabei soll die Auswahl weiterhin die IndexTeilmengen
     * enthalten, aus denen eine m�glichst umfassende �berdeckung der
     * Gesamtmenge mit m�glichst geringen Kosten erzeugt werden kann.
     *
     * @return  Eine neu erzeugte Familie, die eine Auswahl der hinzugef�gten
     *          Teilmengen (oder von Unterklassen davon) enth�lt.
     */
    public abstract ItmFamilie auswahl();
}

