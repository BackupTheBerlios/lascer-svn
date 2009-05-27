/*
 * Dateiname      : KonzeptTeilmenge.java
 * Letzte Änderung: 06. September 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2006
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


package lascer.konzepte.mengen;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.BitIntElemVerwalt;

import lascer.konzepte.Konzept;

/**
 * Repräsentiert eine Index-Teilmenge, die zu einem Konzept erzeugt wurde.
 *
 * @author  Dietmar Lippold
 */
public class KonzeptTeilmenge extends IndexTeilmenge implements Cloneable {

    /**
     * Das dieser Teilmenge zugrunde liegende Konzept.
     */
    private Konzept konzept;

    /**
     * Liefert zu einer Collection von Instanzen dieser Klasse eine Menge
     * der enthaltenen Konzepte.
     *
     * @param konzeptTeilmengen  Eine Collection von Instanzen dieser Klasse.
     *
     * @return  Eine Menge mit den Konzepten der übergebenen
     *          <CODE>Collection</CODE> von Konzept-Teilmengen.
     */
    public static HashSet konzepte(Collection konzeptTeilmengen) {
        HashSet          konzepte;
        Iterator         konzItmIter;
        KonzeptTeilmenge konzTeilmenge;

        konzepte = new HashSet(konzeptTeilmengen.size());
        konzItmIter = konzeptTeilmengen.iterator();
        while (konzItmIter.hasNext()) {
            konzTeilmenge = (KonzeptTeilmenge) konzItmIter.next();
            konzepte.add(konzTeilmenge.konzept());
        }
        return konzepte;
    }

    /**
     * Liefert zu einer Collection von Instanzen dieser Klasse eine Menge
     * neuer Instanzen, bei denen Eigenschaft der Speicher-Effizienz neu
     * festgelegt wurde. Die enthaltenen Elemente werden dabei nur flach
     + kopiert.
     *
     * @param konzeptTeilmengen  Eine Collection von Instanzen dieser Klasse.
     * @param speicherEffizient  Gibt an, ob die neuen Teilmengen besonders
     *                           Speicher-effizient aber dadurch weniger
     *                           Laufzeit-effizient sein sollen.
     *
     * @return  Eine Menge mit neuen Instanzen dieser Klasse, die bis auf die
     *          Eigenschaft der Speicher-Effizienz gleich sind zu den
     *          Teilmengen der übergebenen <CODE>Collection</CODE>.
     */
    public static HashSet konvTeilmengen(Collection konzeptTeilmengen,
                                         boolean speicherEffizient) {
        HashSet           konvTeilmengen;
        Iterator          konzItmIter;
        KonzeptTeilmenge  teilmenge;
        BitIntElemVerwalt alteElementVerwalt, neueElementVerwalt;

        konvTeilmengen = new HashSet(konzeptTeilmengen.size());
        konzItmIter = konzeptTeilmengen.iterator();
        while (konzItmIter.hasNext()) {
            teilmenge = (KonzeptTeilmenge) konzItmIter.next();
            konvTeilmengen.add(new KonzeptTeilmenge(teilmenge,
                                                    speicherEffizient));
        }
        return konvTeilmengen;
    }

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge dieser Teilmenge.
     * @param konzept             Das dieser Teilmenge zugrunde gelegte
     *                            Konzept.
     * @param zusatzKomplex       Eine zusätzliche Komplexität, um die die
     *                            Komplexität der Teilmenge erhöht werden
     *                            soll.
     * @param speicherEffizient   Gibt an, ob die Teilmengen besonders
     *                            Speicher-effizient aber dadurch weniger
     *                            Laufzeit-effizient verwaltet werden sollen.
     */
    public KonzeptTeilmenge(int groesseGesamtmenge, Konzept konzept,
                            float zusatzKomplex, boolean speicherEffizient) {

        super(new BitIntElemVerwalt(groesseGesamtmenge, speicherEffizient),
              groesseGesamtmenge, konzept.komplexitaet() + zusatzKomplex);
        this.konzept = konzept;
    }

    /**
     * Liefert eine neu erzeugte Element-Verwaltung, die die selben Elemente
     * wie die übergebene Teilmenge besitzt, für die aber die Art der
     * Speicher-Effizienz neu festgelegt wird.
     *
     * @param teilmenge          Die Teilmenge, deren Elemente übernommen
     *                           werden.
     * @param speicherEffizient  Gibt an, ob die neue Instanz besonders
     *                           Speicher-effizient aber dadurch weniger
     *                           Laufzeit-effizient sein soll.
     *
     * @return  Die neu erzeugte Element-Verwaltung.
     */
    private static BitIntElemVerwalt neueElemVerwalt(KonzeptTeilmenge teilmenge,
                                                     boolean speicherEffizient) {
        BitIntElemVerwalt elemVerwalt;

        elemVerwalt = (BitIntElemVerwalt) teilmenge.elementVerwaltung();
        return (new BitIntElemVerwalt(elemVerwalt, speicherEffizient));
    }

    /**
     * Erzeugt eine Instanz mit den selben Elementen wie die übergebene
     * Teilmenge, bei der aber die Eigenschaft der Speicher-Effizienz neu
     * festgelegt wird. Die enthaltenen Elemente und das Konzept werden nur
     * flach kopiert.
     *
     * @param teilmenge          Die Teilmenge, deren Werte übernommen werden.
     * @param speicherEffizient  Gibt an, ob die neue Instanz besonders
     *                           Speicher-effizient aber dadurch weniger
     *                           Laufzeit-effizient sein soll.
     */
    public KonzeptTeilmenge(KonzeptTeilmenge teilmenge,
                            boolean speicherEffizient) {

        super(neueElemVerwalt(teilmenge, speicherEffizient),
              teilmenge.groesseGesamtmenge(), teilmenge.kosten());
        this.konzept = teilmenge.konzept;
    }

    /**
     * Erzeugt eine Instanz mit den gleichen Elementen wie die übergebene
     * Teilmenge. Für die enthaltenen Elemente wird ein neues Objekt erzeugt,
     * das Konzept wird aber nur flach kopiert.
     *
     * @param teilmenge  Die Teilmenge, deren Werte kopiert werden.
     */
    public KonzeptTeilmenge(KonzeptTeilmenge teilmenge) {

        super(teilmenge);
        this.konzept = teilmenge.konzept;
    }

    /**
     * Liefert ein neue Instanz dieser Klasse, die zu <CODE>this</CODE> gleich
     * ist. Das Konzept wird dabei nur flach kopiert.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return new KonzeptTeilmenge(this);
    }

    /**
     * Liefert das Konzept dieses Objekts.
     *
     * @return  Das Konzept dieses Objekts.
     */
    public Konzept konzept() {
        return konzept;
    }

    /**
     * Liefert eine Darstellung dieses Objekts.
     *
     * @return  Eine Darstellung dieses Objekts.
     */
    public String toString() {
        return (super.toString() + ";" + konzept.toString());
    }
}

