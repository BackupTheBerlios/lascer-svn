/*
 * Dateiname      : ErwFreieTeilmengenVerwaltung.java
 * Letzte Änderung: 15. März 2007
 * Autoren        : Edgar Binder, Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2007
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

import java.util.HashSet;
import java.util.Iterator;

import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Verwaltet die Teilmengen einer Familie. Im Vergleich zur Klasse
 * <CODE>FreieTeilmengenVerwaltung</CODE> wird die Methode
 * {@link #enthaltendeTeilmenge} beschleunigt.
 *
 * @author  Edgar Binder, Dietmar Lippold
 */
public class ErwFreieTeilmengenVerwaltung extends FreieTeilmengenVerwaltung {

    /**
     * Enthält die Teilmengen, die noch nicht in {@link enthaltendeTeilmengen}
     * augenommen wurden.
     */
    private TeilmengenPuffer teilmengenPuffer;

    /**
     * Zu jedem Index die Teilmengen der Familie, die diesen enthalten.
     */
    private HashSet[] enthaltendeTeilmengen;

    /**
     * Erzeugt eine Instanz, deren Werte von der übergebenen Instanz kopiert
     * werden. Bei den enthaltenen Teilmengen handelt es sich aber um die
     * gleichen Instanzen.
     *
     * @param andereItmVerwaltung  Eine Teilmengenverwaltung, deren Werte in
     *                             die neue Instanz kopiert werden.
     */
    public ErwFreieTeilmengenVerwaltung(ErwFreieTeilmengenVerwaltung andereItmVerwaltung) {

        super(andereItmVerwaltung);

        HashSet        enthaltendeItmMenge;
        IndexTeilmenge teilmenge;
        int            groesseGesamtmenge;

        teilmengenPuffer = (TeilmengenPuffer) andereItmVerwaltung.teilmengenPuffer.clone();

        groesseGesamtmenge = andereItmVerwaltung.enthaltendeTeilmengen.length;
        enthaltendeTeilmengen = new HashSet[groesseGesamtmenge];
        for (int i = 0; i < enthaltendeTeilmengen.length; i++) {
            enthaltendeItmMenge = andereItmVerwaltung.enthaltendeTeilmengen[i];
            enthaltendeTeilmengen[i] = (HashSet) enthaltendeItmMenge.clone();
        }
    }

    /**
     * Erzeugt eine Instanz, deren Elemente Teilmengen einer Index-Menge der
     * übergebenen Größe sind.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die den
     *                            Teilmengen der neuen Familie zugrunde liegt.
     */
    public ErwFreieTeilmengenVerwaltung(int groesseGesamtmenge) {

        super(groesseGesamtmenge);

        teilmengenPuffer = new TeilmengenPuffer();

        enthaltendeTeilmengen = new HashSet[groesseGesamtmenge];
        for (int i = 0; i < enthaltendeTeilmengen.length; i++) {
            enthaltendeTeilmengen[i] = new HashSet();
        }
    }

    /**
     * Liefert ein neues Objekt, das zu <CODE>this</CODE> gleich ist. Die
     * Menge der enthaltenen Teilmengen wird dabei neu erzeugt. Bei den
     * enthaltenen Teilmengen handelt es sich aber um die gleichen Instanzen.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return (new ErwFreieTeilmengenVerwaltung(this));
    }

    /**
     * Löscht die Teilmengenverwaltung, d.h. setzt sie auf den Zustand nach
     * der Erzeugung zurück. Dabei werden alle Teilmengen aus der Verwaltung
     * entfernt.
     */
    public void clear() {

        teilmengenPuffer.clear();

        for (int i = 0; i < enthaltendeTeilmengen.length; i++) {
            enthaltendeTeilmengen[i].clear();
        }

        super.clear();
    }

    /**
     * Fügt der Verwaltung die übergebene Teilmenge hinzu. Wenn die Teilmenge
     * schon enthalten ist, ändert sich nichts.
     *
     * @param teilmenge          Die Teilmenge, die <CODE>this</CODE>
     *                           hinzuefügt werden soll.
     * @param indexUeberdeckAnz  Die Häufigkeit, mit der jeder Index bereits
     *                           überdeckt wird.
     */
    public void teilmengeHinzufuegen(IndexTeilmenge teilmenge,
                                     int[] indexUeberdeckAnz) {
        IndexTeilmenge entfernteTeilmenge;

        if (!super.enthaelt(teilmenge)) {
            entfernteTeilmenge = teilmengenPuffer.teilmengeHinzufuegen(teilmenge);

            if (entfernteTeilmenge != null) {
                for (int element = entfernteTeilmenge.kleinsterEnthaltenerIndex();
                     element >= 0;
                     element = entfernteTeilmenge.naechsterEnthaltenerIndex(element + 1)) {
                    enthaltendeTeilmengen[element].add(entfernteTeilmenge);
                }
            }
        }

        super.teilmengeHinzufuegen(teilmenge, indexUeberdeckAnz);
    }

    /**
     * Entfernt die übergebene Teilmenge aus der Verwaltung. Wenn die
     * Teilmenge nicht enthalten ist, ändert sich nichts.
     *
     * @param teilmenge          Die Teilmenge, die aus <CODE>this</CODE>
     *                           entfernt werden soll.
     * @param indexUeberdeckAnz  Die Häufigkeit, mit der jeder Index bisher
     *                           überdeckt wurde.
     */
    public void teilmengeEntfernen(IndexTeilmenge teilmenge,
                                   int[] indexUeberdeckAnz) {

        if (!teilmengenPuffer.teilmengeEntfernen(teilmenge)) {
            // Die Teilmenge war nicht im Puffer enthalten.

            for (int element = teilmenge.kleinsterEnthaltenerIndex();
                 element >= 0;
                 element = teilmenge.naechsterEnthaltenerIndex(element + 1)) {
                enthaltendeTeilmengen[element].remove(teilmenge);
            }
        }

        super.teilmengeEntfernen(teilmenge, indexUeberdeckAnz);
    }

    /**
     * Liefert eine Teilmenge, die den angegebenen Index enthält. Wenn es
     * keine solche Teilmenge gibt, wird <CODE>null</CODE> geliefert.
     *
     * @param index  Der Index, zu dem eine ihn enthaltende Teilmenge
     *               geliefert wird.
     *
     * @return  Eine Teilmenge, die den übergebenen Index enthält.
     */
    protected IndexTeilmenge enthaltendeTeilmenge(int index) {
        Iterator       iterator;
        IndexTeilmenge teilmenge;

        teilmenge = teilmengenPuffer.enthaltendeTeilmenge(index);
        if (teilmenge == null) {
            if (enthaltendeTeilmengen[index].isEmpty()) {
                teilmenge = null;
            } else {
                iterator = enthaltendeTeilmengen[index].iterator();
                teilmenge = (IndexTeilmenge) iterator.next();
            }
        }

        return teilmenge;
    }
}

