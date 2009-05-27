/*
 * Dateiname      : FesteTeilmengenVerwaltung.java
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

import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Verwaltet die Teilmengen einer Familie. Es werden insbesondere Methoden in
 * Bezug auf die Notwendigkeit der Teilmengen bereit gestellt. Diese
 * Implementierung erfordert eine andere Verwaltung als Grundlage, so daß in
 * diese Verwaltung nur Teilmengen aufgenommen werden, die in der anderen
 * Verwaltung zum Zeitpunkt der Erzeugung dieser Verwaltung enthalten sind.
 *
 * @author  Dietmar Lippold
 */
public class FesteTeilmengenVerwaltung
    implements TeilmengenVerwaltung, Cloneable {

    /**
     * Die Teilmengen, die in der Verwaltung gespeichert werden können. Zu
     * jeder Teilmenge ist ihre Nummer angegeben. Diese Map wird nicht
     * geändert.
     */
    private final HashMap potentielleItmMap;

    /**
     * Zu jeder Nummer einer potentiellen Teilmenge ist diese gespeichert.
     * Dieses Array wird nicht geändert.
     */
    private final IndexTeilmenge[] potentielleTeilmengen;

    /**
     * Die in der Verwaltung enthaltenen Teilmengen.
     */
    private HashSet enthalteneTeilmengen;

    /**
     * Zu jeder Nummer einer Teilmenge ist die Anzahl der Indices gespeichert,
     * die diese Teilmenge alleine enthält, durch die sie also notwendig wird.
     * Wenn die Teilmenge zu einer Nummer nicht in der Verwaltung enthalten
     * ist, ist der Wert -1 gespeichert.
     */
    private int[] anzAlleineUeberdeckt;

    /**
     * Zu jeder Nummer einer Teilmenge ist die Anzahl der Indices gespeichert,
     * die diese Teilmenge nicht mehr alleine enthält. Die Werte sind nur in
     * der Methode <CODE>neuNichtNotwendigeTeilmengen</CODE> gültig.
     */
    private int[] nichtMehrAlleinUeberdeckt;

    /**
     * Zu jedem zu überdeckenden Index, der nur in einer Teilmenge enthalten
     * ist, ist die Nummer der entsprechenden Teilmenge gespeichert. Wenn ein
     * Index in keiner oder in mehr als einer Teilmenge enthalten ist, ist der
     * Wert -1.
     */
    private int[] einzelneNotwendigeItm;

    /**
     * Die Nummer der Teilmenge, die der Verwaltung als letzte hinzugefügt
     * wurde, wenn die Teilmenge noch enthalten ist. Beim Wert -1 ist die
     * Teilmenge unbekannt oder es ist keine Teilmenge enthalten.
     */
    private int zuletztHinzuItmNr = -1;

    /**
     * Die Nummer der Teilmenge, die der Verwaltung als vorletzte hinzugefügt
     * wurde, wenn die Teilmenge noch enthalten ist. Beim Wert -1 ist die
     * Teilmenge unbekannt oder es ist keine Teilmenge enthalten.
     */
    private int vorletztHinzuItmNr = -1;

    /**
     * Die Anzahl der notwendigen Teilmengen der Verwaltung, d.h. der
     * Teilmengen, die mindestens einen Index alleine enthalten.
     */
    private int anzNotwendigeTeilmengen = 0;

    /**
     * Erzeugt eine Instanz, deren Werte von der übergebenen Instanz kopiert
     * werden. Bei den enthaltenen Teilmengen handelt es sich aber um die
     * gleichen Instanzen.
     *
     * @param andereItmVerwaltung  Eine Teilmengenverwaltung, deren Werte in
     *                             die neue Instanz kopiert werden.
     */
    public FesteTeilmengenVerwaltung(FesteTeilmengenVerwaltung andereItmVerwaltung) {

        this.potentielleItmMap = andereItmVerwaltung.potentielleItmMap;
        this.potentielleTeilmengen = andereItmVerwaltung.potentielleTeilmengen;
        this.enthalteneTeilmengen = new HashSet(andereItmVerwaltung.enthalteneTeilmengen);
        this.anzAlleineUeberdeckt = (int[]) andereItmVerwaltung.anzAlleineUeberdeckt.clone();
        this.nichtMehrAlleinUeberdeckt = (int[]) andereItmVerwaltung.nichtMehrAlleinUeberdeckt.clone();
        this.einzelneNotwendigeItm = (int[]) andereItmVerwaltung.einzelneNotwendigeItm.clone();
        this.zuletztHinzuItmNr = andereItmVerwaltung.zuletztHinzuItmNr;
        this.vorletztHinzuItmNr = andereItmVerwaltung.vorletztHinzuItmNr;
        this.anzNotwendigeTeilmengen = andereItmVerwaltung.anzNotwendigeTeilmengen;
    }

    /**
     * Erzeugt eine Instanz, deren Elemente Teilmengen einer Index-Menge der
     * übergebenen Größe sind. Die übergebene Verwaltung wird dieser
     * Verwaltung zugrunde gelegt, d.h. es können in diese Verwaltung nur
     * Teilmengen aufgenommen werden, die in der übergebenen Verwaltung
     * aktuell enthalten sind. Die erzeugte Instanz enthält selbst noch keine
     * Teilmenge.
     *
     * @param basisItmVerwaltung  Die Verwaltung der Teilmengen, die dieser
     *                            Verwaltung zugrunde liegen.
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die den
     *                            Teilmengen der neuen Familie zugrunde
     *                            liegt.
     */
    public FesteTeilmengenVerwaltung(TeilmengenVerwaltung basisItmVerwaltung,
                                     int groesseGesamtmenge) {
        IndexTeilmenge teilmenge;
        Iterator       itmIter;
        int            itmNr;

        potentielleItmMap = new HashMap();
        enthalteneTeilmengen = new HashSet();
        potentielleTeilmengen = new IndexTeilmenge[basisItmVerwaltung.groesseFamilie()];
        anzAlleineUeberdeckt = new int[basisItmVerwaltung.groesseFamilie()];
        nichtMehrAlleinUeberdeckt = new int[basisItmVerwaltung.groesseFamilie()];
        einzelneNotwendigeItm = new int[groesseGesamtmenge];

        itmIter = basisItmVerwaltung.iterator();
        itmNr = 0;
        while (itmIter.hasNext()) {
            teilmenge = (IndexTeilmenge) itmIter.next();
            potentielleItmMap.put(teilmenge, new Integer(itmNr));
            potentielleTeilmengen[itmNr] = teilmenge;
            itmNr++;
        }

        Arrays.fill(anzAlleineUeberdeckt, -1);
        Arrays.fill(einzelneNotwendigeItm, -1);
    }

    /**
     * Liefert ein neues Objekt, das zu <CODE>this</CODE> gleich ist. Die
     * Menge der enthaltenen Teilmengen wird dabei neu erzeugt. Bei den
     * enthaltenen Teilmengen handelt es sich aber um die gleichen Instanzen.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return new FesteTeilmengenVerwaltung(this);
    }

    /**
     * Liefert den hashCode der Instanz. Das ist der hashCode eines
     * <CODE>HashSet</CODE> mit den enthaltenen Teilmengen und hängt daher
     * nicht von der Art der Teilmengenverwaltung ab.
     *
     * @return  Den hashCode der Instanz.
     */
    public int hashCode() {
        return enthalteneTeilmengen.hashCode();
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
        return andereItmVerwaltung.toHashSet().equals(this.enthalteneTeilmengen);
    }

    /**
     * Löscht die Teilmengenverwaltung, d.h. setzt sie auf den Zustand nach
     * der Erzeugung zurück. Dabei werden alle Teilmengen aus der Verwaltung
     * entfernt.
     */
    public void clear() {
        enthalteneTeilmengen.clear();
        Arrays.fill(anzAlleineUeberdeckt, -1);
        Arrays.fill(einzelneNotwendigeItm, -1);
        zuletztHinzuItmNr = -1;
        vorletztHinzuItmNr = -1;
        anzNotwendigeTeilmengen = 0;
    }

    /**
     * Liefert die Nummer der potentiellen Teilmenge. Wenn die Teilmenge keine
     * potentiellen Teilmenge ist, wird der Wert -1 geliefert.
     *
     * @param teilmenge  Eine potentielle Teilmenge, deren Nummer geliefert
     *                   werden soll.
     *
     * @return  Die Nummer der potentiellen Teilmenge oder -1, wenn es sich
     *          nicht um eine potentielle Teilmenge handelt.
     */
    private int teilmengenNr(IndexTeilmenge teilmenge) {
        Integer nummer;

        nummer = (Integer) potentielleItmMap.get(teilmenge);
        if (nummer == null) {
            return -1;
        } else {
            return ((Integer) nummer).intValue();
        }
    }

    /**
     * Ermittelt, ob die Teilmenge mit der übergebenen Nummer in dieser
     * Verwaltung enthalten ist.
     *
     * @param teilmengenNr  Die Nummer der Teilmenge, von der ermittelt wird,
     *                      ob sie in <CODE>this</CODE> enthalten ist.
     *
     * @return  <CODE>true</CODE>, wenn <CODE>this</CODE> die Teilmenge mit
     *          der übergebenen Nummer enthält, anderenfalls <CODE>false</CODE>.
     */
    private boolean enthaelt(int teilmengenNr) {

        if (teilmengenNr < 0) {
            return false;
        } else {
            return (anzAlleineUeberdeckt[teilmengenNr] != -1);
        }
    }

    /**
     * Ermittelt, ob die übergebene Teilmenge in dieser Verwaltung
     * enthalten ist.
     *
     * @param teilmenge  Eine Teilmenge, von der ermittelt wird, ob sie in
     *                   <CODE>this</CODE> enthalten ist.
     *
     * @return  <CODE>true</CODE>, wenn <CODE>this</CODE> die übergebene
     *          Teilmenge enthält, anderenfalls <CODE>false</CODE>.
     */
    public boolean enthaelt(IndexTeilmenge teilmenge) {
        return enthaelt(teilmengenNr(teilmenge));
    }

    /**
     * Fügt der Verwaltung die übergebene Teilmenge hinzu. Wenn die Teilmenge
     * schon enthalten ist, ändert sich nichts.
     *
     * @param teilmenge          Die Teilmenge, die <CODE>this</CODE>
     *                           hinzuefügt werden soll.
     * @param indexUeberdeckAnz  Die Häufigkeit, mit der jeder Index bereits
     *                           überdeckt wird.
     *
     * @throws IllegalArgumentException  Wenn eine Teilmenge hinzugefügt
     *                                   werden soll, die keine potentielle
     *                                   Teilmenge ist.
     */
    public void teilmengeHinzufuegen(IndexTeilmenge teilmenge,
                                     int[] indexUeberdeckAnz) {
        int itmNr = teilmengenNr(teilmenge);

        // Wenn es sich um keine potentielle Teilmenge handelt eine Exception
        // werfen.
        if (itmNr == -1) {
            throw new IllegalArgumentException("Teilmenge nicht zulässig");
        }

        // Wenn die Teilmenge schon in der Verwaltung enthalten ist, Methode
        // sofort beenden.
        if (anzAlleineUeberdeckt[itmNr] != -1) return;

        enthalteneTeilmengen.add(teilmenge);
        anzAlleineUeberdeckt[itmNr] = 0;

        for (int index = teilmenge.kleinsterEnthaltenerIndex();
             index >= 0;
             index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {

            if (indexUeberdeckAnz[index] == 0) {
                einzelneNotwendigeItm[index] = itmNr;
                anzAlleineUeberdeckt[itmNr]++;
            } else if (indexUeberdeckAnz[index] == 1) {
                anzAlleineUeberdeckt[einzelneNotwendigeItm[index]]--;
                if (anzAlleineUeberdeckt[einzelneNotwendigeItm[index]] == 0) {
                    anzNotwendigeTeilmengen--;
                }
                einzelneNotwendigeItm[index] = -1;
            }
        }
        if (anzAlleineUeberdeckt[itmNr] > 0) {
            anzNotwendigeTeilmengen++;
        }

        vorletztHinzuItmNr = zuletztHinzuItmNr;
        zuletztHinzuItmNr = itmNr;
    }

    /**
     * Liefert die Nummer einer Teilmenge, die den angegebenen Index enthält.
     * Wenn es keine solche Teilmenge gibt, wird -1 geliefert.
     *
     * @param index  Der Index, zu dem die Nummer einer ihn enthaltenden
     *               Teilmenge geliefert wird.
     *
     * @return  Die Nummer einer Teilmenge, die den übergebenen Index enthält.
     */
    private int enthaltendeTeilmenge(int index) {

        if (enthaelt(zuletztHinzuItmNr)
                && potentielleTeilmengen[zuletztHinzuItmNr].indexIstEnthalten(index)) {
            return zuletztHinzuItmNr;
        }
        if (enthaelt(vorletztHinzuItmNr)
                && potentielleTeilmengen[vorletztHinzuItmNr].indexIstEnthalten(index)) {
            return vorletztHinzuItmNr;
        }

        for (int nr = 0; nr < anzAlleineUeberdeckt.length; nr++) {
            if (anzAlleineUeberdeckt[nr] != -1) {
                // Die Teilmenge mit der Nummer nr ist in der Verwaltung
                // enthalten.
                if (potentielleTeilmengen[nr].indexIstEnthalten(index)) {
                    return nr;
                }
            }
        }
        return -1;
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
        int itmNr             = teilmengenNr(teilmenge);
        int enthaltendeItmNr;

        // Wenn es sich um keine potentielle Teilmenge handelt und diese
        // nicht in der Verwaltung enthalten ist, Methode sofort beenden.
        if ((itmNr == -1) || (anzAlleineUeberdeckt[itmNr] == -1)) return;

        enthalteneTeilmengen.remove(teilmenge);
        if (anzAlleineUeberdeckt[itmNr] > 0) {
            anzNotwendigeTeilmengen--;
        }
        anzAlleineUeberdeckt[itmNr] = -1;

        for (int index = teilmenge.kleinsterEnthaltenerIndex();
             index >= 0;
             index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {

            if (indexUeberdeckAnz[index] == 2) {
                enthaltendeItmNr = enthaltendeTeilmenge(index);
                einzelneNotwendigeItm[index] = enthaltendeItmNr;
                anzAlleineUeberdeckt[enthaltendeItmNr]++;
                if (anzAlleineUeberdeckt[enthaltendeItmNr] == 1) {
                    anzNotwendigeTeilmengen++;
                }
            } else if (indexUeberdeckAnz[index] == 1) {
                einzelneNotwendigeItm[index] = -1;
            }
        }

        if (itmNr == zuletztHinzuItmNr) {
            zuletztHinzuItmNr = -1;
        }
        if (itmNr == vorletztHinzuItmNr) {
            vorletztHinzuItmNr = -1;
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
        HashSet notwendigeTeilmengen = new HashSet(anzNotwendigeTeilmengen);

        if (anzNotwendigeTeilmengen > 0) {
            for (int itmNr = 0; itmNr < anzAlleineUeberdeckt.length; itmNr++) {
                if (anzAlleineUeberdeckt[itmNr] > 0) {
                    // Die Teilmenge mit der Nummer itmNr ist notwendig.
                    notwendigeTeilmengen.add(potentielleTeilmengen[itmNr]);
                }
            }
        }

        return notwendigeTeilmengen;
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
        HashSet nichtNotwendigeTeilmengen;
        int     anzNichtNotwendigeTeilmengen;

        anzNichtNotwendigeTeilmengen = anzNichtNotwendigeTeilmengen();
        nichtNotwendigeTeilmengen = new HashSet(anzNichtNotwendigeTeilmengen);

        if (anzNichtNotwendigeTeilmengen > 0) {
            for (int itmNr = 0; itmNr < anzAlleineUeberdeckt.length; itmNr++) {
                if (anzAlleineUeberdeckt[itmNr] == 0) {
                    // Die Teilmenge mit der Nummer itmNr ist enthalten aber
                    // nicht notwendig.
                    nichtNotwendigeTeilmengen.add(potentielleTeilmengen[itmNr]);
                }
            }
        }

        return nichtNotwendigeTeilmengen;
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
        HashSet neuNichtNotwendigeTeilmengen = new HashSet();
        int     itmNr;

        if (enthaelt(teilmenge)) {
            return neuNichtNotwendigeTeilmengen;
        } else {
            // Werte in nichtMehrAlleinUeberdeckt zu den notwendigen
            // Teilmengen auf Null setzen.
            for (int index = teilmenge.kleinsterEnthaltenerIndex();
                 index >= 0;
                 index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {

                if (einzelneNotwendigeItm[index] != -1) {
                    nichtMehrAlleinUeberdeckt[einzelneNotwendigeItm[index]] = 0;
                }
            }

            // Werte für nichtMehrAlleinUeberdeckt ermitteln.
            for (int index = teilmenge.kleinsterEnthaltenerIndex();
                 index >= 0;
                 index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {

                if (einzelneNotwendigeItm[index] != -1) {
                    // Der Index index wird derzeit noch von einer Teilmenge
                    // alleine überdeckt.
                    nichtMehrAlleinUeberdeckt[einzelneNotwendigeItm[index]]++;
                }
            }

            // Die nicht mehr notwendigen Teilmengen bestimmen.
            for (int index = teilmenge.kleinsterEnthaltenerIndex();
                 index >= 0;
                 index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {

                if (einzelneNotwendigeItm[index] != -1) {
                    itmNr = einzelneNotwendigeItm[index];
                    if ((nichtMehrAlleinUeberdeckt[itmNr] > 0)
                        && (nichtMehrAlleinUeberdeckt[itmNr] == anzAlleineUeberdeckt[itmNr])) {

                        neuNichtNotwendigeTeilmengen.add(potentielleTeilmengen[itmNr]);
                    }
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
     *
     * @throws IllegalArgumentException  Wenn die Teilmenge keine potentielle
     *                                   Teilmenge ist.
     */
    public boolean ueberdecktAlleineAnz(int alleinAnz, IndexTeilmenge teilmenge,
                                        IndexTeilmenge ueberdeckt,
                                        IndexTeilmenge nichtUeberdeckt) {
        int itmNr = teilmengenNr(teilmenge);

        if (itmNr == -1) {
            throw new IllegalArgumentException("Teilmenge nicht zulässig");
        }

        if (anzAlleineUeberdeckt[itmNr] != -1) {
            // Die Teilmenge ist in der Verwaltung enthalten.
            return (anzAlleineUeberdeckt[itmNr] >= alleinAnz);
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
     *
     * @throws IllegalArgumentException  Wenn die Teilmenge keine potentielle
     *                                   Teilmenge ist.
     */
    public int anzAlleineUeberdeckt(IndexTeilmenge teilmenge,
                                    IndexTeilmenge ueberdeckt,
                                    IndexTeilmenge nichtUeberdeckt) {
        int itmNr = teilmengenNr(teilmenge);

        if (itmNr == -1) {
            throw new IllegalArgumentException("Teilmenge nicht zulässig");
        }

        if (anzAlleineUeberdeckt[itmNr] != -1) {
            // Die Teilmenge ist in der Verwaltung enthalten.
            return anzAlleineUeberdeckt[itmNr];
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
     * Liefert die Anzahl der notwendigen Teilmengen der Verwaltung, d.h. die
     * Anzahl der Teilmengen, die mindestens ein Element enthalten, das in
     * keiner anderen Teilmenge der Verwaltung enthalten ist. Gegenüber dem
     * Aufruf von <CODE>notwendigeTeilmengen().size()</CODE> ist diese Methode
     * effizienter.
     *
     * @return  Die Anzahl der notwendigen Teilmengen von <CODE>this</CODE>.
     */
    public int anzNotwendigeTeilmengen() {
        return anzNotwendigeTeilmengen;
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
        return (enthalteneTeilmengen.size() - anzNotwendigeTeilmengen);
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
        return enthalteneTeilmengen.size();
    }

    /**
     * Liefert einen Iterator über die Teilmengen der Verwaltung. Die
     * Teilmengen werden von diesem in keiner bestimmten Reihenfolge
     * geliefert.
     *
     * @return  Einen Iterator über die Teilmengen der Verwaltung.
     */
    public Iterator iterator() {
        return enthalteneTeilmengen.iterator();
    }

    /**
     * Liefert eine neue Menge mit den Teilmengen der Verwaltung.
     *
     * @return  Ein neues <CODE>HashSet</CODE> mit den Teilmengen von
     *          <CODE>this</CODE>.
     */
    public HashSet toHashSet() {
        return (HashSet) enthalteneTeilmengen.clone();
    }

    /**
     * Liefert ein Array mit den zur Verwaltung gehörenden Teilmengen. Die
     * Reihenfolge der Teilmengen im Array ist nicht festgelegt.
     *
     * @return  Ein Array der Teilmengen von <CODE>this</CODE>.
     */
    public IndexTeilmenge[] toArray() {
        return (IndexTeilmenge[]) enthalteneTeilmengen.toArray(new IndexTeilmenge[0]);
    }
}

