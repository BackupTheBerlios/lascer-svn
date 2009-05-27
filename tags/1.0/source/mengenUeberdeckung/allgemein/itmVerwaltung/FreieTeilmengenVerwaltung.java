/*
 * Dateiname      : FreieTeilmengenVerwaltung.java
 * Letzte Änderung: 12. August 2006
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


package mengenUeberdeckung.allgemein.itmVerwaltung;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import mathCollection.HashMultiset;

import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Verwaltet die Teilmengen einer Familie. Es werden insbesondere Methoden in
 * Bezug auf die Notwendigkeit der Teilmengen bereit gestellt.
 *
 * @author  Dietmar Lippold
 */
public class FreieTeilmengenVerwaltung
    implements TeilmengenVerwaltung, Cloneable {

    /**
     * Die in der Verwaltung gespeicherten Teilmengen.
     */
    private HashSet teilmengen;

    /**
     * Jeder in der Verwaltung gespeicherten notwendigen Teilmenge ist eine
     * Instanz von <CODE>IndexTeilmenge</CODE> mit den Indices, die die
     * Teilmenge alleine enthält, zugeordnet.
     */
    private HashMap alleNotwendigenItm;

    /**
     * Zu jedem zu überdeckenden Index, der nur in einer Teilmenge enthalten
     * ist, ist die entsprechende Teilmenge gespeichert. Wenn ein Index in
     * keiner oder in mehr als einer Teilmenge enthalten ist, ist der Wert
     * <CODE>null</CODE>.
     */
    private IndexTeilmenge[] einzelneNotwendigeItm;

    /**
     * Die Teilmengen, die der Verwaltung als letzte hinzugefügt wurden.
     */
    private TeilmengenPuffer letzteTeilmengen;

    /**
     * Erzeugt eine Instanz, deren Werte von der übergebenen Instanz kopiert
     * werden. Bei den enthaltenen Teilmengen handelt es sich aber um die
     * gleichen Instanzen.
     *
     * @param andereItmVerwaltung  Eine Teilmengenverwaltung, deren Werte in
     *                             die neue Instanz kopiert werden.
     */
    public FreieTeilmengenVerwaltung(FreieTeilmengenVerwaltung andereItmVerwaltung) {
        HashMap          andereAlleNotwItm;
        IndexTeilmenge[] andereEinzelneNotwItm;
        TeilmengenPuffer andereLetzteTeilmengen;
        Iterator         iter;
        IndexTeilmenge   notwendigeItm;
        IndexTeilmenge   notwIndices;

        this.teilmengen = new HashSet(andereItmVerwaltung.teilmengen);

        andereLetzteTeilmengen = andereItmVerwaltung.letzteTeilmengen;
        this.letzteTeilmengen = (TeilmengenPuffer) andereLetzteTeilmengen.clone();

        andereEinzelneNotwItm = andereItmVerwaltung.einzelneNotwendigeItm;
        this.einzelneNotwendigeItm = (IndexTeilmenge[]) andereEinzelneNotwItm.clone();

        andereAlleNotwItm = andereItmVerwaltung.alleNotwendigenItm;
        this.alleNotwendigenItm = new HashMap(andereAlleNotwItm.size());

        iter = andereAlleNotwItm.keySet().iterator();
        while (iter.hasNext()) {
            notwendigeItm = (IndexTeilmenge) iter.next();
            notwIndices = (IndexTeilmenge) andereAlleNotwItm.get(notwendigeItm);
            this.alleNotwendigenItm.put(notwendigeItm, notwIndices.clone());
        }
    }

    /**
     * Erzeugt eine Instanz, deren Elemente Teilmengen einer Index-Menge der
     * übergebenen Größe sind.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die den
     *                            Teilmengen der neuen Familie zugrunde
     *                            liegt.
     */
    public FreieTeilmengenVerwaltung(int groesseGesamtmenge) {

        teilmengen = new HashSet();
        alleNotwendigenItm = new HashMap();
        letzteTeilmengen = new TeilmengenPuffer();
        einzelneNotwendigeItm = new IndexTeilmenge[groesseGesamtmenge];
        Arrays.fill(einzelneNotwendigeItm, null);
    }

    /**
     * Liefert ein neues Objekt, das zu <CODE>this</CODE> gleich ist. Die
     * Menge der enthaltenen Teilmengen wird dabei neu erzeugt. Bei den
     * enthaltenen Teilmengen handelt es sich aber um die gleichen Instanzen.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return new FreieTeilmengenVerwaltung(this);
    }

    /**
     * Liefert den hashCode der Instanz. Das ist der hashCode eines
     * <CODE>HashSet</CODE> mit den enthaltenen Teilmengen und hängt daher
     * nicht von der Art der Teilmengenverwaltung ab.
     *
     * @return  Den hashCode der Instanz.
     */
    public int hashCode() {
        return teilmengen.hashCode();
    }

    /**
     * Ermittelt, ob das übergebene Objekt eine <CODE>TeilmengenVerwaltung</CODE>
     * ist und die gleichen Teilmengen wie <CODE>this</CODE> enthält. Der Wert
     * hängt daher nicht von der Art der Teilmengenverwaltung ab.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>TeilmengenVerwaltung</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das übergebene Objekt eine
     *          <CODE>TeilmengenVerwaltung</CODE> ist und die gleichen
     *          Teilmengen wie diese Instanz enthält, anderenfalls
     *          <CODE>false</CODE>.
     */
    public boolean equals(Object anderesObjekt) {
        TeilmengenVerwaltung andereItmVerwaltung;

        if (anderesObjekt == this) {
            return true;
        }

        if (!(anderesObjekt instanceof TeilmengenVerwaltung)) {
            return false;
        }

        andereItmVerwaltung = (TeilmengenVerwaltung) anderesObjekt;
        return andereItmVerwaltung.toHashSet().equals(this.teilmengen);
    }

    /**
     * Löscht die Teilmengenverwaltung, d.h. setzt sie auf den Zustand nach
     * der Erzeugung zurück. Dabei werden alle Teilmengen aus der Verwaltung
     * entfernt.
     */
    public void clear() {
        teilmengen.clear();
        alleNotwendigenItm.clear();
        Arrays.fill(einzelneNotwendigeItm, null);
        letzteTeilmengen.clear();
    }

    /**
     * Ermittelt, ob die übergebene Teilmenge in dieser Verwaltung enthalten
     * ist.
     *
     * @param teilmenge  Eine Teilmenge, von der ermittelt wird, ob sie in
     *                   <CODE>this</CODE> enthalten ist.
     *
     * @return  <CODE>true</CODE>, wenn <CODE>this</CODE> die übergebene
     *          Teilmenge enthält, anderenfalls <CODE>false</CODE>.
     */
    public boolean enthaelt(IndexTeilmenge teilmenge) {
        return teilmengen.contains(teilmenge);
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
        IndexTeilmenge andereTeilmenge;
        IndexTeilmenge andereIndices;
        IndexTeilmenge notwIndices;

        if (teilmengen.contains(teilmenge)) return;

        teilmengen.add(teilmenge);
        notwIndices = new IndexTeilmenge(teilmenge.groesseGesamtmenge());

        for (int index = teilmenge.kleinsterEnthaltenerIndex();
             index >= 0;
             index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {

            if (indexUeberdeckAnz[index] == 0) {
                einzelneNotwendigeItm[index] = teilmenge;
                notwIndices.indexAufnehmen(index);
            } else if (indexUeberdeckAnz[index] == 1) {
                andereTeilmenge = einzelneNotwendigeItm[index];
                andereIndices = (IndexTeilmenge) alleNotwendigenItm.get(andereTeilmenge);
                andereIndices.indexLoeschen(index);
                if (andereIndices.groesseTeilmenge() == 0) {
                    alleNotwendigenItm.remove(andereTeilmenge);
                }
                einzelneNotwendigeItm[index] = null;
            }
        }

        if (notwIndices.groesseTeilmenge() > 0) {
            alleNotwendigenItm.put(teilmenge, notwIndices);
        }

        letzteTeilmengen.teilmengeHinzufuegen(teilmenge);
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

        teilmenge = letzteTeilmengen.enthaltendeTeilmenge(index);
        if (teilmenge != null) {
            return teilmenge;
        }

        iterator = iterator();
        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge) iterator.next();
            if (teilmenge.indexIstEnthalten(index)) {
                return teilmenge;
            }
        }
        return null;
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
        IndexTeilmenge andereTeilmenge;
        IndexTeilmenge andereIndices;

        if (!teilmengen.contains(teilmenge)) return;

        letzteTeilmengen.teilmengeEntfernen(teilmenge);

        teilmengen.remove(teilmenge);
        alleNotwendigenItm.remove(teilmenge);

        for (int index = teilmenge.kleinsterEnthaltenerIndex();
             index >= 0;
             index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {

            if (indexUeberdeckAnz[index] == 2) {
                andereTeilmenge = enthaltendeTeilmenge(index);
                andereIndices = (IndexTeilmenge) alleNotwendigenItm.get(andereTeilmenge);
                if (andereIndices == null) {
                    andereIndices = new IndexTeilmenge(teilmenge.groesseGesamtmenge());
                    alleNotwendigenItm.put(andereTeilmenge, andereIndices);
                }
                andereIndices.indexAufnehmen(index);
                einzelneNotwendigeItm[index] = andereTeilmenge;
            } else if (indexUeberdeckAnz[index] == 1) {
                einzelneNotwendigeItm[index] = null;
            }
        }
    }

    /**
     * Liefert die notwendigen Teilmengen der Verwaltung, d.h. die Teilmengen,
     * die mindestens ein Element enthalten, das in keiner anderen Teilmenge
     * der Verwaltung enthalten ist.
     *
     * @return  Ein <CODE>Set</CODE> mit den notwendigen Teilmengen von
     *          <CODE>this</CODE>.
     */
    public Set notwendigeTeilmengen() {
        return alleNotwendigenItm.keySet();
    }

    /**
     * Liefert die nicht notwendigen Teilmengen der Verwaltung, d.h. die
     * Teilmengen, die nur Elemente enthalten, die auch in mindestens einer
     * anderen Teilmenge der Verwaltung enthalten sind.
     *
     * @return  Ein <CODE>Set</CODE> mit den nicht notwendigen Teilmengen von
     *          <CODE>this</CODE>.
     */
    public Set nichtNotwendigeTeilmengen() {
        HashSet        set;
        Iterator       iterator;
        IndexTeilmenge teilmenge;

        set = new HashSet(anzNichtNotwendigeTeilmengen());
        if (anzNichtNotwendigeTeilmengen() > 0) {
            iterator = teilmengen.iterator();
            while (iterator.hasNext()) {
                teilmenge = (IndexTeilmenge) iterator.next();
                if (!alleNotwendigenItm.containsKey(teilmenge)) {
                    set.add(teilmenge);
                }
            }
        }
        return set;
    }

    /**
     * Liefert die notwendigen Teilmengen der Verwaltung, die nach Hinzunahme
     * der übergebenen Teilmenge nicht mehr notwendig wären. Wenn die
     * Teilmenge schon in der Verwaltung enthalten ist, wird eine leere Menge
     * geliefert.
     *
     * @param teilmenge  Die Teilmenge, für die bestimmt werden soll, welche
     *                   derzeit notwendigen Teilmengen nicht mehr notwendig
     *                   würden.
     *
     * @return  Ein <CODE>Set</CODE> mit den Teilmengen, die nach Hinzunahme
     *          der übergebenen Teilmenge nicht mehr notwendig wären.
     */
    public Set neuNichtNotwendigeTeilmengen(IndexTeilmenge teilmenge) {
        HashMultiset   kandidaten;
        HashSet        neuNichtNotwendigeTeilmengen;
        Iterator       itmIter;
        IndexTeilmenge kandidat;
        IndexTeilmenge notwIndices;
        int            zusaetzlichUeberdeckAnz;
        int            alleineUeberdecktAnz;
        boolean        weiterNotwendig;

        neuNichtNotwendigeTeilmengen = new HashSet();
        if (teilmengen.contains(teilmenge)) {
            return neuNichtNotwendigeTeilmengen;
        } else if (anzNotwendigeTeilmengen() <= teilmenge.groesseTeilmenge()) {
            itmIter = alleNotwendigenItm.keySet().iterator();
            while (itmIter.hasNext()) {
                kandidat = (IndexTeilmenge) itmIter.next();
                weiterNotwendig = false;

                notwIndices = (IndexTeilmenge) alleNotwendigenItm.get(kandidat);
                for (int index = notwIndices.kleinsterEnthaltenerIndex();
                     (index >= 0) && !weiterNotwendig;
                     index = notwIndices.naechsterEnthaltenerIndex(index + 1)) {

                    if (!teilmenge.indexIstEnthalten(index)) {
                        weiterNotwendig = true;
                    }
                }

                if (!weiterNotwendig) {
                    neuNichtNotwendigeTeilmengen.add(kandidat);
                }
            }
            return neuNichtNotwendigeTeilmengen;
        } else {
            kandidaten = new HashMultiset();

            for (int index = teilmenge.kleinsterEnthaltenerIndex();
                 index >= 0;
                 index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {

                if (einzelneNotwendigeItm[index] != null) {
                    kandidaten.add(einzelneNotwendigeItm[index]);
                }
            }

            itmIter = kandidaten.toSet().iterator();
            while (itmIter.hasNext()) {
                kandidat = (IndexTeilmenge) itmIter.next();
                notwIndices = (IndexTeilmenge) alleNotwendigenItm.get(kandidat);
                alleineUeberdecktAnz = notwIndices.groesseTeilmenge();
                zusaetzlichUeberdeckAnz = kandidaten.getQuantity(kandidat);
                if (zusaetzlichUeberdeckAnz == alleineUeberdecktAnz) {
                    neuNichtNotwendigeTeilmengen.add(kandidat);
                }
            }

            return neuNichtNotwendigeTeilmengen;
        }
    }

    /**
     * Ermittelt, ob die übergebenen Teilmenge mindestens die übergebene
     * Anzahl von Indices enthält, die in keiner bzw. keiner anderen Teilmenge
     * der Verwaltung enthalten sind. Diese Indices sind genau dann in einer
     * Teilmenge der Verwaltung enthalten, wenn die übergebene Teilmenge in
     * der Verwaltung enthalten ist.
     *
     * @param alleinAnz        Die Anzahl der Indices, von der geprüft wird,
     *                         ob sie die Teilmenge alleine enthält. Der Wert
     *                         muß größer als Null sein.
     * @param teilmenge        Die Teilmenge, für die ermittelt wird, wie
     *                         viele Indices sie als Teilmenge der Verwaltung
     *                         alleine enthält.
     * @param ueberdeckt       Die Indices, die in mindestens einer Teilmenge
     *                         der Verwaltung enthalten sind.
     * @param nichtUeberdeckt  Die Indices, die in keiner Teilmenge der
     *                         Verwaltung enthalten sind.
     *
     * @return  <CODE>true</CODE> wenn die Teilmenge die angegebene Anzahl von
     *          Indices alleine enthält, sonst <CODE>false</CODE>.
     */
    public boolean ueberdecktAlleineAnz(int alleinAnz, IndexTeilmenge teilmenge,
                                        IndexTeilmenge ueberdeckt,
                                        IndexTeilmenge nichtUeberdeckt) {
        IndexTeilmenge notwIndices;

        if (teilmengen.contains(teilmenge)) {
            // Die Teilmenge ist in der Verwaltung enthalten.
            notwIndices = (IndexTeilmenge) alleNotwendigenItm.get(teilmenge);
            if (notwIndices == null) {
                // Die Teilmenge ist nicht notwendig.
                return false;
            } else {
                // Die Teilmenge ist notwendig.
                return (notwIndices.groesseTeilmenge() >= alleinAnz);
            }
        } else {
            // Die Teilmenge ist nicht in der Verwaltung enthalten.
            if (nichtUeberdeckt.groesseTeilmenge() == 0) {
                // Es gibt keinen Index, der in keiner Teilmenge der Familie
                // enthalten ist.
                return false;
            } else if (alleinAnz == 1) {
                return (!teilmenge.istDisjunkt(nichtUeberdeckt));
            } else {
                return (teilmenge.anzNurSelbstEnthalten(ueberdeckt) >= alleinAnz);
            }
        }
    }

    /**
     * Liefert die Anzahl der Indices, die in der übergebenen Teilmenge aber
     * in keiner bzw. keiner anderen Teilmenge der Verwaltung enthalten sind.
     * Die Indices sind genau dann in einer Teilmenge der Verwaltung
     * enthalten, wenn die übergebene Teilmenge in der Verwaltung enthalten
     * ist.
     *
     * @param teilmenge        Die Teilmenge, für die ermittelt wird, wie
     *                         viele Indices sie als Teilmenge der Verwaltung
     *                         alleine enthält.
     * @param ueberdeckt       Die Indices, die in mindestens einer Teilmenge
     *                         der Verwaltung enthalten sind.
     * @param nichtUeberdeckt  Die Indices, die in keiner Teilmenge der
     *                         Verwaltung enthalten sind.
     *
     * @return  Die Anzahl der Indices, die in der übergebenen Teilmenge aber
     *          in keiner bzw. keiner anderen Teilmenge der Verwaltung
     *          enthalten sind.
     */
    public int anzAlleineUeberdeckt(IndexTeilmenge teilmenge,
                                    IndexTeilmenge ueberdeckt,
                                    IndexTeilmenge nichtUeberdeckt) {
        IndexTeilmenge notwIndices;

        if (teilmengen.contains(teilmenge)) {
            // Die Teilmenge ist in der Verwaltung enthalten.
            return anzAlleineUeberdeckt(teilmenge);
        } else {
            // Die Teilmenge ist nicht in der Verwaltung enthalten.
            if (nichtUeberdeckt.groesseTeilmenge() == 0) {
                // Es gibt keinen Index, der in keiner Teilmenge der Familie
                // enthalten ist.
                return 0;
            } else {
                return teilmenge.anzNurSelbstEnthalten(ueberdeckt);
            }
        }
    }

    /**
     * Liefert die Anzahl der Indices, die außer in der übergebenen Teilmenge
     * in keiner anderen Teilmenge der Verwaltung enthalten sind. Wenn die
     * Teilmenge nicht in der Verwaltung enthalten ist, wird der Wert Null
     * geliefert.
     *
     * @param teilmenge  Die Teilmenge, für die ermittelt wird, wie viele
     *                   Indices sie als Teilmenge der Verwaltung alleine
     *                   enthält.
     *
     * @return  Die Anzahl der Indices, die die Teilmenge als einzige in der
     *          Verwaltung enthält. Wenn die Teilmenge nicht in der Verwaltung
     *          enthalten ist, wird der Wert Null geliefert.
     */
    protected int anzAlleineUeberdeckt(IndexTeilmenge teilmenge) {
        IndexTeilmenge notwIndices;

        notwIndices = (IndexTeilmenge) alleNotwendigenItm.get(teilmenge);
        if (notwIndices == null) {
            // Die Teilmenge ist nicht notwendig oder nicht in der Verwaltung
            // enthalten.
            return 0;
        } else {
            // Die Teilmenge ist in der Verwaltung enthalten und notwendig.
            return notwIndices.groesseTeilmenge();
        }
    }

    /**
     * Liefert die Anzahl der notwendigen Teilmengen der Verwaltung, d.h. die
     * Anzahl der Teilmengen, die mindestens ein Element enthalten, das in
     * keiner anderen Teilmenge der Verwaltung enthalten ist. Gegenüber dem
     * Aufruf von <CODE>notwendigeTeilmengen().size()</CODE> ist diese Methode
     * effizienter.
     *
     * @return  Die Anzahl der notwendigen Teilmengen von <CODE>this</CODE>.
     */
    public int anzNotwendigeTeilmengen() {
        return alleNotwendigenItm.size();
    }

    /**
     * Liefert die Anzahl der nicht notwendigen Teilmengen der Verwaltung, d.h.
     * die Anzahl der Teilmengen, die nur Elemente enthalten, die auch in
     * mindestens einer anderen Teilmenge der Verwaltung enthalten sind.
     * Gegenüber dem Aufruf von <CODE>nichtNotwendigeTeilmengen().size()</CODE>
     * ist diese Methode effizienter.
     *
     * @return  Die Anzahl der nicht notwendigen Teilmengen von
     *          <CODE>this</CODE>.
     */
    public int anzNichtNotwendigeTeilmengen() {
        return (groesseFamilie() - anzNotwendigeTeilmengen());
    }

    /**
     * Liefert die Größe der Verwaltung, d.h. die Anzahl der in der Verwaltung
     * enthaltenen Teilmengen. Dies ist die Summe der Werte von
     * <CODE>notwendigeTeilmengenAnz()</CODE> und
     * <CODE>nichtNotwendigeTeilmengenAnz()</CODE>.
     *
     * @return  Die Anzahl Teilmengen von <CODE>this</CODE>.
     */
    public int groesseFamilie() {
        return teilmengen.size();
    }

    /**
     * Liefert einen Iterator über die Teilmengen der Verwaltung. Die
     * Teilmengen werden von diesem in keiner bestimmten Reihenfolge
     * geliefert.
     *
     * @return  Einen Iterator über die Teilmengen der Verwaltung.
     */
    public Iterator iterator() {
        return teilmengen.iterator();
    }

    /**
     * Liefert eine neue Menge mit den Teilmengen der Verwaltung.
     *
     * @return  Ein neues <CODE>HashSet</CODE> mit den Teilmengen von
     *          <CODE>this</CODE>.
     */
    public HashSet toHashSet() {
        return (HashSet) teilmengen.clone();
    }

    /**
     * Liefert ein Array mit den zur Verwaltung gehörenden Teilmengen. Die
     * Reihenfolge der Teilmengen im Array ist nicht festgelegt.
     *
     * @return  Ein Array der Teilmengen von <CODE>this</CODE>.
     */
    public IndexTeilmenge[] toArray() {
        return (IndexTeilmenge[]) teilmengen.toArray(new IndexTeilmenge[0]);
    }
}

