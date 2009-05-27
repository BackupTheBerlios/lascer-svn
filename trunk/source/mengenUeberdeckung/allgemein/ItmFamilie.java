/*
 * Dateiname      : ItmFamilie.java
 * Letzte Änderung: 22. August 2006
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

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;
import java.text.DecimalFormat;

import mengenUeberdeckung.allgemein.itmVerwaltung.TeilmengenVerwaltung;
import mengenUeberdeckung.allgemein.itmVerwaltung.FreieTeilmengenVerwaltung;
import mengenUeberdeckung.allgemein.itmVerwaltung.ErwFreieTeilmengenVerwaltung;
import mengenUeberdeckung.allgemein.itmVerwaltung.FesteTeilmengenVerwaltung;

/**
 * Repräsentiert eine Familie von Teilmengen einer Indexmenge, die
 * <DFN>Gesamtmenge</DFN> genannt wird. Es gibt keine Teilmenge, die mehr als
 * einmal in der Familie enthalten ist.<P>
 *
 * Es kann eine Anzahl von Teilmengen, die in der Familie maximal enthalten
 * sein sollen, vorgegeben werden. Falls diese vorgegeben wird und die Familie
 * mehr Teilmengen enthält, liefert die Methode <CODE>kosten</CODE> einen Wert,
 * der größer ist als die Summe der Kosten der vorgegebenen Anzahl beliebiger
 * Teilmengen.
 *
 * @author  Dietmar Lippold
 */
public class ItmFamilie implements Cloneable {

    /**
     * Die Verwaltung der Teilmengen der Familie.
     */
    private TeilmengenVerwaltung itmVerwaltung;

    /**
     * Die Indices, die von mindestens einer Teilmenge der Familie überdeckt
     * werden.
     */
    private IndexTeilmenge ueberdeckt = null;

    /**
     * Die Indices, die von keiner Teilmenge der Familie überdeckt werden.
     */
    private IndexTeilmenge nichtUeberdeckt = null;

    /**
     * Zu jedem zu überdeckenden Index der Gesamtmenge die Anzahl der
     * Teilmengen, die diesen Index enthalten.
     */
    private int[] indexUeberdeckAnz;

    /**
     * Die Kosten der Teilmengen der Familie.
     */
    private float kosten = 0;

    /**
     * Die maximalen Kosten einer Teilmenge. Dieser Wert hat nur eine
     * Bedeutung, wenn der Wert von <CODE>maxTeilmengenAnz</CODE> größer als
     * Null ist. Dann ist der Wert positiv.
     */
    private float maxTeilmengenKosten = 0;

    /**
     * Die Anzahl der Teilmengen, die die Familie maximal enthalten soll. Der
     * Wert Null steht für eine unbegrenzte Anzahl.
     */
    private int maxTeilmengenAnz = 0;

    /**
     * Die Summe der Größen der Teilmengen der Familie, d.h. die Summe der
     * Anzahlen der in den Teilmengen enthaltenen Elemente.
     */
    private int ueberdeckungsHaeufigkeitSumme = 0;

    /**
     * Die Anzahl der Indices, die in keiner der Teilmengen der Familie
     * enthalten sind.
     */
    private int nichtUeberdecktAnz;

    /**
     * Die Anzahl der Indices, die in genau einer der Teilmengen der
     * Familie enthalten sind.
     */
    private int einfachUeberdecktAnz;

    /**
     * Die Anzahl der Indices, die in mehreren der Teilmengen der Familie
     * enthalten sind.
     */
    private int mehrfachUeberdecktAnz;

    /**
     * Der errechnete HashCode der Instanz. Wenn der Wert Null ist, wird der
     * Wert vor der nächsten Abfrage errechnet.
     */
    private int hashCode = 0;

    /**
     * Erzeugt eine Familie, deren Elemente Teilmengen einer Index-Menge der
     * übergebenen Größe sind.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die den
     *                            Teilmengen der neuen Familie zugrunde
     *                            liegt.
     *
     * @throws IndexOutOfBoundsException  Einer der übergebenen Werte ist
     *                                    kleiner als Null.
     */
    public ItmFamilie(int groesseGesamtmenge) {

        if (groesseGesamtmenge < 0) {
            throw new IndexOutOfBoundsException();
        }

        itmVerwaltung = new FreieTeilmengenVerwaltung(groesseGesamtmenge);

        ueberdeckt = new IndexTeilmenge(groesseGesamtmenge);
        nichtUeberdeckt = new IndexTeilmenge(groesseGesamtmenge);
        for (int i = 0; i < groesseGesamtmenge; i++) {
            nichtUeberdeckt.indexAufnehmen(i);
        }

        indexUeberdeckAnz = new int[groesseGesamtmenge];
        Arrays.fill(indexUeberdeckAnz, 0);
        nichtUeberdecktAnz = groesseGesamtmenge;
        einfachUeberdecktAnz = 0;
        mehrfachUeberdecktAnz = 0;
    }

    /**
     * Erzeugt eine Familie, die zu <CODE>this</CODE> strukturell gleich ist.
     * Sie enthält aber noch keine Teilmenge.
     *
     * @param andereFamilie    Eine Familie, zu der die neue Familie
     *                         strukturell gleich sein soll.
     * @param festeVerwaltung  Gibt an, ob die Teilmengen unbedingt in einer
     *                         festen Teilmengenverwaltung gespeichert werden
     *                         sollen. Falls nicht, wird die Art der
     *                         Verwaltung von der übergebenen Familie
     *                         übernommen. Bei einer festen Verwaltung bildet
     *                         die übergebene Familie deren Grundlage.
     */
    protected ItmFamilie(ItmFamilie andereFamilie, boolean festeVerwaltung) {
        TeilmengenVerwaltung andereVerwaltung;
        float                faktor;
        int                  groesseGesamtmenge;
        int                  elemProItm;

        groesseGesamtmenge = andereFamilie.groesseGesamtmenge();
        andereVerwaltung = andereFamilie.itmVerwaltung;
        if (festeVerwaltung
                || (andereVerwaltung instanceof FesteTeilmengenVerwaltung)) {
            this.itmVerwaltung = new FesteTeilmengenVerwaltung(andereVerwaltung,
                                                               groesseGesamtmenge);
        } else {
            faktor = Konstanten.ITM_VERWALT_AUSWAHL_FAKTOR;
            elemProItm = Math.round(groesseGesamtmenge * andereFamilie.dichteMittel());
            if (elemProItm * faktor < andereFamilie.groesseFamilie()) {
                this.itmVerwaltung = new ErwFreieTeilmengenVerwaltung(groesseGesamtmenge);
            } else {
                this.itmVerwaltung = new FreieTeilmengenVerwaltung(groesseGesamtmenge);
            }
        }
        this.maxTeilmengenAnz = andereFamilie.maxTeilmengenAnz;
        this.maxTeilmengenKosten = andereFamilie.maxTeilmengenKosten;

        this.ueberdeckt = new IndexTeilmenge(groesseGesamtmenge);
        this.nichtUeberdeckt = new IndexTeilmenge(groesseGesamtmenge);
        for (int i = 0; i < groesseGesamtmenge; i++) {
            this.nichtUeberdeckt.indexAufnehmen(i);
        }

        this.indexUeberdeckAnz = new int[groesseGesamtmenge];
        Arrays.fill(this.indexUeberdeckAnz, 0);
        this.nichtUeberdecktAnz = groesseGesamtmenge;
        this.einfachUeberdecktAnz = 0;
        this.mehrfachUeberdecktAnz = 0;
    }

    /**
     * Erzeugt eine Familie, deren Werte von der übergebenen Familie kopiert
     * werden. Bei den enthaltenen Teilmengen handelt es sich aber um die
     * gleichen Instanzen.
     *
     * @param andereFamilie  Eine Familie, deren Werte in die neue Instanz
     *                       kopiert werden.
     */
    protected ItmFamilie(ItmFamilie andereFamilie) {
        TeilmengenVerwaltung andereVerwaltung;

        andereVerwaltung = andereFamilie.itmVerwaltung;
        this.itmVerwaltung = (TeilmengenVerwaltung) andereVerwaltung.clone();
        this.ueberdeckt = (IndexTeilmenge) andereFamilie.ueberdeckt.clone();
        this.nichtUeberdeckt = (IndexTeilmenge) andereFamilie.nichtUeberdeckt.clone();
        this.indexUeberdeckAnz = (int[]) andereFamilie.indexUeberdeckAnz.clone();
        this.kosten = andereFamilie.kosten;
        this.maxTeilmengenKosten = andereFamilie.maxTeilmengenKosten;
        this.maxTeilmengenAnz = andereFamilie.maxTeilmengenAnz;
        this.ueberdeckungsHaeufigkeitSumme = andereFamilie.ueberdeckungsHaeufigkeitSumme;
        this.nichtUeberdecktAnz = andereFamilie.nichtUeberdecktAnz;
        this.einfachUeberdecktAnz = andereFamilie.einfachUeberdecktAnz;
        this.mehrfachUeberdecktAnz = andereFamilie.mehrfachUeberdecktAnz;
        this.hashCode = andereFamilie.hashCode;
    }

    /**
     * Liefert ein neue Instanz, die zu <CODE>this</CODE> strukturell gleich
     * ist. Sie enthält aber noch keine Teilmenge. Ist der übergebene Wert
     * <CODE>false</CODE>, ist die gelieferte Instanz gleich zu einer, die
     * druch Aufruf von <CODE>clone</CODE> und anschließendem Aufruf von
     * <CODE>clear</CODE> entsteht.
     *
     * @param festeVerwaltung  Gibt an, ob die Teilmengen unbedingt in einer
     *                         festen Teilmengenverwaltung gespeichert werden
     *                         sollen. Falls nicht, wird die Art der
     *                         Verwaltung von der übergebenen Familie
     *                         übernommen. Bei einer festen Verwaltung bildet
     *                         die übergebene Familie deren Grundlage.
     *
     * @return  Ein zu diesem Objekt strukturell gleiches Objekt aber ohne
     *          enthaltene Teilmengen.
     */
    public ItmFamilie neueInstanz(boolean festeVerwaltung) {
        return (new ItmFamilie(this, festeVerwaltung));
    }

    /**
     * Liefert ein neues Objekt, das zu <CODE>this</CODE> gleich ist. Die
     * Menge der enthaltenen Teilmengen wird dabei neu erzeugt. Bei den
     * enthaltenen Teilmengen handelt es sich aber um die gleichen Instanzen.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return (new ItmFamilie(this));
    }

    /**
     * Liefert ein neues Objekt, das zu <CODE>this</CODE> bis auf die
     * übergebenen Werte gleich ist. Die Menge der enthaltenen Teilmengen wird
     * dabei neu erzeugt. Bei den enthaltenen Teilmengen handelt es sich aber
     * um die gleichen Instanzen.
     *
     * @param maxItmAnz     Die Anzahl der Teilmengen, die in der Familie
     *                      maximal enthalten sein sollen.
     * @param maxItmKosten  Die maximalen Kosten einer Teilmenge.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt, bei dem aber eine
     *          maximale Anzahl zu enthaltender Teilmengen vorgegeben wurde.
     *
     * @throws IndexOutOfBoundsException  Einer der übergebenen Werte ist
     *                                    kleiner als Null.
     */
    public ItmFamilie clone(int maxItmAnz, float maxItmKosten) {
        ItmFamilie neueFamilie;

        neueFamilie = (ItmFamilie) clone();
        neueFamilie.maxTeilmengenAnz = maxItmAnz;
        neueFamilie.maxTeilmengenKosten = maxItmKosten;

        return neueFamilie;
    }

    /**
     * Liefert den hashCode der Instanz.
     *
     * @return  Den hashCode der Instanz.
     */
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = itmVerwaltung.hashCode();
        }
        return hashCode;
    }

    /**
     * Ermittelt, ob das übergebene Objekt eine <CODE>ItmFamilie</CODE> ist
     * und die gleichen Teilmengen wie <CODE>this</CODE> enthält. Die
     * möglicher Weise vorgegebene maximale Anzahl von Teilmengen und die
     * maximalen Kosten einer Teilmenge werden nicht berücksichtigt.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>ItmFamilie</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das übergebene Objekt eine
     *          <CODE>ItmFamilie</CODE> ist und die gleichen Teilmengen
     *          wie diese Instanz enthält, anderenfalls wird <CODE>false</CODE>
     *          geliefert.
     */
    public boolean equals(Object anderesObjekt) {

        if (anderesObjekt == this) {
            return true;
        }

        if (!(anderesObjekt instanceof ItmFamilie)) {
            return false;
        }

        ItmFamilie andereFamilie = (ItmFamilie) anderesObjekt;

        if ((andereFamilie.indexUeberdeckAnz.length != this.indexUeberdeckAnz.length)
                || (andereFamilie.itmVerwaltung.groesseFamilie() != this.itmVerwaltung.groesseFamilie())
                || (andereFamilie.kosten != this.kosten)
                || (andereFamilie.ueberdeckungsHaeufigkeitSumme != this.ueberdeckungsHaeufigkeitSumme)
                || (andereFamilie.nichtUeberdecktAnz != this.nichtUeberdecktAnz)
                || (andereFamilie.einfachUeberdecktAnz != this.einfachUeberdecktAnz)
                || (andereFamilie.mehrfachUeberdecktAnz != this.mehrfachUeberdecktAnz)) {
            return false;
        }

        if (andereFamilie.hashCode() != this.hashCode()) {
            return false;
        }

        if (!Arrays.equals(andereFamilie.indexUeberdeckAnz, this.indexUeberdeckAnz)) {
            return false;
        }

        return andereFamilie.itmVerwaltung.equals(this.itmVerwaltung);
    }

    /**
     * Liefert die Anzahl der Teilmengen, die die Familie maximal enthalten
     * soll. Der Wert Null steht für eine unbegrenzte Anzahl.
     *
     * @return  Die Anzahl der Teilmengen, die die Familie maximal enthalten
     *          soll.
     */
    public int maxTeilmengenAnz() {
        return maxTeilmengenAnz;
    }

    /**
     * Liefert die vorgegebenen maximalen Kosten, die eine Teilmenge haben
     * soll, die in diese Familie aufgenommen wird. Dieser Wert hat nur eine
     * Bedeutung, wenn der Wert der Methode <CODE>maxTeilmengenAnz</CODE>
     * größer als Null ist.
     *
     * @return  Die maximalen Kosten einer Teilmenge.
     */
    public float maxTeilmengenKosten() {
        return maxTeilmengenKosten;
    }

    /**
     * Löscht die Familie, d.h. setzt sie auf den Zustand nach der Erzeugung
     * zurück. Dabei werden alle Teilmengen aus der Familie entfernt.
     */
    public void clear() {
        itmVerwaltung.clear();
        ueberdeckt = new IndexTeilmenge(indexUeberdeckAnz.length);
        nichtUeberdeckt = new IndexTeilmenge(indexUeberdeckAnz.length);
        for (int i = 0; i < indexUeberdeckAnz.length; i++) {
            nichtUeberdeckt.indexAufnehmen(i);
        }
        Arrays.fill(indexUeberdeckAnz, 0);
        kosten = 0;
        ueberdeckungsHaeufigkeitSumme = 0;
        nichtUeberdecktAnz = indexUeberdeckAnz.length;
        einfachUeberdecktAnz = 0;
        mehrfachUeberdecktAnz = 0;
        hashCode = 0;
    }

    /**
     * Ermittelt, ob die übergebene Teilmenge schon in der Familie enthalten
     * ist.
     *
     * @param teilmenge  Eine Teilmenge, von der ermittelt wird, ob sie in
     *                   <CODE>this</CODE> enthalten ist.
     *
     * @return  <CODE>true</CODE>, wenn <CODE>this</CODE> die übergebene
     *          Teilmenge enthält, anderenfalls <CODE>false</CODE>.
     */
    public boolean enthaelt(IndexTeilmenge teilmenge) {
        return itmVerwaltung.enthaelt(teilmenge);
    }

    /**
     * Fügt der Familie die übergebene Teilmenge hinzu. Wenn die Teilmenge
     * schon in der Familie enthalten ist, ändert sich nichts.
     *
     * @param teilmenge  Die Teilmenge, die <CODE>this</CODE> hinzuefügt
     *                   werden soll.
     *
     * @throws SizeMismatchException  Die Gesamtmenge der übergebenen
     *                                Teilmenge hat eine andere Größe als die
     *                                Gesamtmenge dieser Familie.
     */
    public void teilmengeHinzufuegen(IndexTeilmenge teilmenge) {

        if (itmVerwaltung.enthaelt(teilmenge)) return;

        if (teilmenge.groesseGesamtmenge() != indexUeberdeckAnz.length) {
            throw new SizeMismatchException();
        }

        hashCode = 0;
        itmVerwaltung.teilmengeHinzufuegen(teilmenge, indexUeberdeckAnz);

        for (int index = teilmenge.kleinsterEnthaltenerIndex();
             index >= 0;
             index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {
            if (indexUeberdeckAnz[index] == 0) {
                nichtUeberdecktAnz--;
                einfachUeberdecktAnz++;
                ueberdeckt.indexAufnehmen(index);
                nichtUeberdeckt.indexLoeschen(index);
            } else if (indexUeberdeckAnz[index] == 1) {
                einfachUeberdecktAnz--;
                mehrfachUeberdecktAnz++;
            }
            indexUeberdeckAnz[index]++;
        }

        kosten += teilmenge.kosten();
        ueberdeckungsHaeufigkeitSumme += teilmenge.groesseTeilmenge();
    }

    /**
     * Fügt der Familie die übergebenen Teilmengen hinzu. Jede Teilmenge
     * wird der Familie nur einmal hinzugefügt, auch wenn sie mehrmals in
     * der übergebenen Collection enthalten ist.
     *
     * @param neueTeilmengen  Eine Collection von Teilmengen, die
     *                        <CODE>this</CODE> hinzuefügt werden sollen.
     *
     * @throws SizeMismatchException  Die Gesamtmenge einer der übergebenen
     *                                Teilmengen hat eine andere Größe als die
     *                                Gesamtmenge dieser Familie.
     */
    public void teilmengenHinzufuegen(Collection neueTeilmengen) {
        Iterator iterator = neueTeilmengen.iterator();
        IndexTeilmenge teilmenge;

        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge)iterator.next();
            teilmengeHinzufuegen(teilmenge);
        }
    }

    /**
     * Entfernt die übergebene Teilmenge aus der Familie. Wenn die Teilmenge
     * nicht in der Familie enthalten ist, ändert sich nichts.
     *
     * @param teilmenge  Die Teilmenge, die aus <CODE>this</CODE> entfernt
     *                   werden soll.
     */
    public void teilmengeEntfernen(IndexTeilmenge teilmenge) {

        if (!itmVerwaltung.enthaelt(teilmenge)) return;

        hashCode = 0;
        itmVerwaltung.teilmengeEntfernen(teilmenge, indexUeberdeckAnz);

        for (int index = teilmenge.kleinsterEnthaltenerIndex();
             index >= 0;
             index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {
            if (indexUeberdeckAnz[index] == 2) {
                mehrfachUeberdecktAnz--;
                einfachUeberdecktAnz++;
            } else if (indexUeberdeckAnz[index] == 1) {
                einfachUeberdecktAnz--;
                nichtUeberdecktAnz++;
                ueberdeckt.indexLoeschen(index);
                nichtUeberdeckt.indexAufnehmen(index);
            }
            indexUeberdeckAnz[index]--;
        }

        kosten -= teilmenge.kosten();
        // Prüfen, ob die neuen Kosten wegen Rechenungenauigkeit negativ sind
        if (kosten < 0) {
            kosten = 0;
        }
        ueberdeckungsHaeufigkeitSumme -= teilmenge.groesseTeilmenge();
    }

    /**
     * Entfernt die übergebenen Teilmengen aus der Familie. Jede Teilmenge
     * kann maximal einmal entfernt werden. Wenn versucht wird, eine Teilmenge
     * zu entfernen, die nicht in der Familie enthalten ist, wird die Familie
     * nicht verändert.
     *
     * @param alteTeilmengen  Eine Collection von Teilmengen, die aus
     *                        <CODE>this</CODE> entfernt werden sollen.
     */
    public void teilmengenEntfernen(Collection alteTeilmengen) {
        Iterator iterator = alteTeilmengen.iterator();
        IndexTeilmenge teilmenge;

        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge)iterator.next();
            teilmengeEntfernen(teilmenge);
        }
    }

    /**
     * Liefert die notwendigen Teilmengen der Familie, d.h. die Teilmengen,
     * die mindestens ein Element enthalten, das in keiner anderen Teilmenge
     * der Familie enthalten ist.
     *
     * @return  Ein <CODE>Set</CODE> mit den notwendigen Teilmengen von
     *          <CODE>this</CODE>.
     */
    public Set notwendigeTeilmengen() {
        return itmVerwaltung.notwendigeTeilmengen();
    }

    /**
     * Liefert die nicht notwendigen Teilmengen der Familie, d.h. die
     * Teilmengen, die nur Elemente enthalten, die auch in mindestens einer
     * anderen Teilmenge der Familie enthalten sind.
     *
     * @return  Ein <CODE>Set</CODE> mit den nicht notwendigen Teilmengen von
     *          <CODE>this</CODE>.
     */
    public Set nichtNotwendigeTeilmengen() {
        return itmVerwaltung.nichtNotwendigeTeilmengen();
    }

    /**
     * Liefert die notwendigen Teilmengen der Familie, die nach Hinzunahme der
     * übergebenen Teilmenge nicht mehr notwendig wären. Wenn die Teilmenge
     * schon in der Familie enthalten ist, wird eine leere Menge geliefert.
     *
     * @param teilmenge  Die Teilmenge, für die bestimmt werden soll, welche
     *                   derzeit notwendigen Teilmengen nicht mehr notwendig
     *                   würden.
     *
     * @return  Ein <CODE>Set</CODE> mit den Teilmengen, die nach Hinzunahme
     *          der übergebenen Teilmenge nicht mehr notwendig wären.
     */
    public Set neuNichtNotwendigeTeilmengen(IndexTeilmenge teilmenge) {
        if (einfachUeberdecktAnz == 0) {
            return (new HashSet(1));
        } else {
            return itmVerwaltung.neuNichtNotwendigeTeilmengen(teilmenge);
        }
    }

    /**
     * Ermittelt, ob die übergebene Teilmenge notwendig ist. Dies ist genau
     * dann der Fall, wenn sie einen Index enthält, der in keiner, wenn sie
     * nicht zur Familie gehört, bzw. der in keiner anderen, wenn sie zur
     * Familie gehört, Teilmenge der Familie enthalten ist.
     *
     * @param teilmenge  Die Teilmenge, von der geprüft werden soll, ob sie
     *                   notwendig ist.
     *
     * @return  <CODE>true</CODE> wenn die Teilmenge notwendig ist, sonst
     *          <CODE>false</CODE>.
     */
    public boolean teilmengeIstNotwendig(IndexTeilmenge teilmenge) {
        return ueberdecktAlleineAnz(teilmenge, 1);
    }

    /**
     * Ermittelt, ob die übergebenen Teilmenge mindestens die übergebene
     * Anzahl von Indices enthält, die in keiner bzw. keiner anderen Teilmenge
     * der Familie enthalten sind. Diese Indices sind genau dann in einer
     * Teilmenge der Familie enthalten, wenn die übergebene Teilmenge in der
     * Familie enthalten ist.
     *
     * @param teilmenge  Die Teilmenge, von der geprüft werden soll, ob sie
     *                   die angegebene Anzahl von Indices alleine enthält.
     * @param alleinAnz  Die Anzahl der Indices, von der geprüft wird, ob sie
     *                   die Teilmenge alleine enthält. Der Wert muß größer
     *                   als Null sein.
     *
     * @return  <CODE>true</CODE> wenn die Teilmenge die angegebene Anzahl von
     *          Indices alleine enthält, sonst <CODE>false</CODE>.
     */
    public boolean ueberdecktAlleineAnz(IndexTeilmenge teilmenge, int alleinAnz) {
        return itmVerwaltung.ueberdecktAlleineAnz(alleinAnz, teilmenge,
                                                  ueberdeckt, nichtUeberdeckt);
    }

    /**
     * Liefert die Anzahl der Indices, die in der übergebenen Teilmenge aber
     * in keiner bzw. keiner anderen Teilmenge der Familie enthalten sind. Die
     * Indices sind genau dann in einer Teilmenge der Verwaltung enthalten,
     * wenn die übergebene Teilmenge in der Familie enthalten ist.
     *
     * @param teilmenge  Die Teilmenge, von der die Anzahl der Indices
     *                   ermittelt werden soll, die sie alleine enthält.
     *
     * @return  Die Anzahl der Indices, die außer in der übergebenen Teilmenge
     *          in keiner Teilmenge von <CODE>this</CODE> enthalten sind.
     */
    public int anzAlleineUeberdeckt(IndexTeilmenge teilmenge) {
        return itmVerwaltung.anzAlleineUeberdeckt(teilmenge, ueberdeckt,
                                                  nichtUeberdeckt);
    }

    /**
     * Ermittelt, ob der übergebene Index in mindestens einer der Teilmengen
     * der Familie enthalten ist.
     *
     * @param index  Die Index, zu dem ermittelt werden soll, ob er in
     *               mindestens einer Teilmenge von <CODE>this</CODE> enthalten
     *               ist.
     *
     * @return  <CODE>true</CODE>, der Index in mindestens einer Teilmende
     *          von <CODE>this</CODE> enthalten ist, anderenfalls
     *          <CODE>false</CODE>.
     */
    public boolean indexIstUeberdeckt(int index) {
        return (indexUeberdeckAnz[index] > 0);
    }

    /**
     * Liefert ein aufsteigend sortiertes Array mit den Indices, die in
     * mindestens einer Teilmenge dieser Familie enthalten sind.
     *
     * @return  Ein aufsteigend sortiertes Array mit den überdeckten Indices
     *          dieser Familie. Dieses darf nicht verändert werden.
     */
    public int[] ueberdeckteIndices() {
        return ueberdeckt.enthalteneIndices();
    }

    /**
     * Liefert die Anzahl der Teilmengen, die den übergebenen Index enthalten.
     *
     * @param index  Die Index, zu dem ermittelt werden soll, in wie vielen
     *               Teilmengen von <CODE>this</CODE> er enthalten ist.
     *
     * @return  Die Anzahl der Teilmengen von <CODE>this</CODE>, in denen der
     *          übergebene Index enthalten ist.
     */
    public int ueberdeckungsHaeufigkeit(int index) {
        return indexUeberdeckAnz[index];
    }

    /**
     * Liefert die durchschnittliche Häufigkeit, mit der ein Index überdeckt
     * wird, d.h. die durchschnittliche Anzahl der Teilmengen, in denen ein
     * Index enthalten ist.
     *
     * @return  Die durchschnittliche Häufigkeit, mit der ein Index durch eine
     *          Teilmenge dieser Familie überdeckt wird.
     */
    public float ueberdeckungsHaeufigkeitMittel() {
        return ((float) ueberdeckungsHaeufigkeitSumme) / groesseGesamtmenge();
    }

    /**
     * Liefert die durchschnittliche Anzahl der Indices, die eine Teilmenge
     * dieser Familie enthält.
     *
     * @return  Die durchschnittliche Anzahl der Indices, die in den
     *          Teilmengen von <CODE>this</CODE> enthaltenen sind.
     */
    public float itmIndexAnzMittel() {
        return ((float) ueberdeckungsHaeufigkeitSumme) / groesseFamilie();
    }

    /**
     * Liefert die durchschnittliche Dichte der Familie, d.h. den
     * durchschnittliche Anteil der in den Teilmengen enthaltenen Indices.
     *
     * @return  Den durchschnittlichen Anteil der Indices, die in den
     *          Teilmengen von <CODE>this</CODE> enthaltenen sind.
     */
    public float dichteMittel() {
        return ueberdeckungsHaeufigkeitMittel() / groesseFamilie();
    }

    /**
     * Liefert die Anzahl der Indices, die in mindestens einer der Teilmengen
     * der Familie enthalten sind.
     *
     * @return  Die Anzahl der Indices, die in mindestens einer Teilmenge von
     *          <CODE>this</CODE> enthalten sind.
     */
    public int anzUeberdeckt() {
        return indexUeberdeckAnz.length - nichtUeberdecktAnz;
    }

    /**
     * Liefert die Anzahl der Indices, die in keiner der Teilmengen der
     * Familie enthalten sind.
     *
     * @return  Die Anzahl der Indices, die in keiner Teilmenge von
     *          <CODE>this</CODE> enthalten sind.
     */
    public int anzNichtUeberdeckt() {
        return nichtUeberdecktAnz;
    }

    /**
     * Liefert die Anzahl der Indices, die in genau einer der Teilmengen der
     * Familie enthalten sind.
     *
     * @return  Die Anzahl der Indices, die in genau einer Teilmenge von
     *          <CODE>this</CODE> enthalten sind.
     */
    public int anzEinfachUeberdeckt() {
        return einfachUeberdecktAnz;
    }

    /**
     * Liefert die Anzahl der Indices, die in mehreren der Teilmengen
     * der Familie enthalten sind.
     *
     * @return  Die Anzahl der Indices, die in mehr als einer Teilmenge von
     *          <CODE>this</CODE> enthalten sind.
     */
    public int anzMehrfachUeberdeckt() {
        return mehrfachUeberdecktAnz;
    }

    /**
     * Liefert die Anzahl der notwendigen Teilmengen der Familie, d.h. die
     * Anzahl der Teilmengen, die mindestens ein Element enthalten, das in
     * keiner anderen Teilmenge der Familie enthalten ist. Gegenüber dem Aufruf
     * von <CODE>notwendigeTeilmengen().size()</CODE> ist diese Methode
     * effizienter.
     *
     * @return  Die Anzahl der notwendigen Teilmengen von <CODE>this</CODE>.
     */
    public int anzNotwendigeTeilmengen() {
        return itmVerwaltung.anzNotwendigeTeilmengen();
    }

    /**
     * Liefert die Anzahl der nicht notwendigen Teilmengen der Familie, d.h.
     * die Anzahl der Teilmengen, die nur Elemente enthalten, die auch in
     * mindestens einer anderen Teilmenge der Familie enthalten sind.
     * Gegenüber dem Aufruf von <CODE>nichtNotwendigeTeilmengen().size()</CODE>
     * ist diese Methode effizienter.
     *
     * @return  Die Anzahl der nicht notwendigen Teilmengen von <CODE>this</CODE>.
     */
    public int anzNichtNotwendigeTeilmengen() {
        return itmVerwaltung.anzNichtNotwendigeTeilmengen();
    }

    /**
     * Liefert die Größe der Familie, d.h. die Anzahl der in der Familie
     * enthaltenen Teilmengen. Dies ist die Summe der Werte von
     * <CODE>notwendigeTeilmengenAnz()</CODE> und
     * <CODE>nichtNotwendigeTeilmengenAnz()</CODE>.
     *
     * @return  Die Anzahl Teilmengen von <CODE>this</CODE>.
     */
    public int groesseFamilie() {
        return itmVerwaltung.groesseFamilie();
    }

    /**
     * Liefert die Größe der Gesamtmenge, d.h. die Anzahl ihrer Elemente.
     *
     * @return  Die Anzahl Indices der Gesamtmenge.
     */
    public int groesseGesamtmenge() {
        return indexUeberdeckAnz.length;
    }

    /**
     * Ermittelt, ob die Kosten der einzelnen enthaltenen Teilmengen in allen
     * Situationen gleich sind.
     *
     * @return  <CODE>true</CODE> genau dann, wenn die Kosten der einzelnen
     *          Teilmengen in allen Situationen gleich sind, sonst
     *          <CODE>false</CODE>.
     */
    public boolean teilmengenKostenGleich() {
        Iterator       itmIter;
        IndexTeilmenge nextItm;
        float          itmKosten;

        if (itmVerwaltung.groesseFamilie() == 0) {
            return true;
        } else if (maxTeilmengenAnz > 0) {
            return false;
        } else {
            itmIter = itmVerwaltung.iterator();
            nextItm = (IndexTeilmenge) itmIter.next();
            itmKosten = nextItm.kosten();
            while (itmIter.hasNext()) {
                nextItm = (IndexTeilmenge) itmIter.next();
                if (nextItm.kosten() != itmKosten) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Ermittelt, ob die Kosten dieser Familie linear von den Kosten der
     * enthaltenen Teilmengen abhängen, wenn die Kosten der Teilmengen also
     * unabhängig voneinander sind in Bezug auf die Kosten dieser Familie.
     *
     * @return  <CODE>true</CODE> genau dann, die Kosten dieser Familie linear
     *          von den Kosten der enthaltenen Teilmengen abhängen, sonst
     *          <CODE>false</CODE>.
     */
    public boolean teilmengenKostenLinear() {
        return ((itmVerwaltung.groesseFamilie() == 0) || (maxTeilmengenAnz == 0));
    }

    /**
     * Liefert die Kosten der enthaltenen Teilmengen. Wenn keine maximale
     * Anzahl von Teilmengen vorgegeben ist oder wenn die aktuelle Anzahl
     * nicht größer ist, sind die Kosten die Summe der den Teilmengen
     * zugeordneten Kosten. Wenn die Familie mehr Teilmengen enthält als sie
     * enthalten soll, erhöhten sich die Kosten zusätzlich.
     *
     * @return  Die Kosten der enthaltenen Teilmengen.
     */
    public float kostenFamilie() {

        if ((maxTeilmengenAnz == 0)
                || (itmVerwaltung.groesseFamilie() <= maxTeilmengenAnz)) {
            return kosten;
        } else {
            return kosten + itmVerwaltung.groesseFamilie() * maxTeilmengenKosten;
        }
    }

    /**
     * Liefert den Wert, um den sich die Kosten der Familie durch Hinzufügen
     * der übergebenen Teilmenge erhöhen. Wenn keine maximale Anzahl von
     * Teilmengen vorgegeben ist oder wenn die aktuelle Anzahl kleiner ist,
     * ist der Wert gleich den Kosten der übergebenen Teilmenge. Anderenfalls
     * erhöht sich der Wert zusätzlich.
     *
     * @param teilmenge  Die Teilmenge, für die die Kosten durch Hinzufügen zur
     *                   Familie ermittelt werden sollen.
     *
     * @return  Der Wert, um den sich die Kosten der Familie durch Hinzufügen
     *          der übergebenen Teilmenge erhöhen. Wenn die übergebene
     *          Teilmenge schon in der Familie enthalten ist, wird der Wert
     *          Null geliefert.
     */
    public float kostenHinzufuegen(IndexTeilmenge teilmenge) {
        int tmAnz;

        if (itmVerwaltung.enthaelt(teilmenge)) {
            return 0;
        }

        tmAnz = itmVerwaltung.groesseFamilie();
        if ((maxTeilmengenAnz == 0) || (tmAnz < maxTeilmengenAnz)) {
            return teilmenge.kosten();
        } else if (tmAnz == maxTeilmengenAnz) {
            return teilmenge.kosten() + (tmAnz + 1) * maxTeilmengenKosten;
        } else {
            return teilmenge.kosten() + maxTeilmengenKosten;
        }
    }

    /**
     * Liefert den Wert, um den sich die Kosten der Familie durch Entfernen
     * der übergebenen Teilmenge verringern. Wenn keine maximale Anzahl von
     * Teilmengen vorgegeben ist oder wenn die aktuelle Anzahl nicht größer
     * ist, ist der Wert gleich den Kosten der übergebenen Teilmenge.
     * Anderenfalls erhöht sich der Wert zusätzlich.
     *
     * @param teilmenge  Die Teilmenge, für die die Kosten durch Entfernen aus
     *                   der Familie ermittelt werden sollen.
     *
     * @return  Der Wert, um den sich die Kosten der Familie durch Entfernen
     *          der übergebenen Teilmenge verringern. Wenn die übergebene
     *          Teilmenge nicht in der Familie enthalten ist, wird der Wert
     *          Null geliefert.
     */
    public float kostenEntfernen(IndexTeilmenge teilmenge) {
        int tmAnz;

        if (!itmVerwaltung.enthaelt(teilmenge)) {
            return 0;
        }

        tmAnz = itmVerwaltung.groesseFamilie();
        if ((maxTeilmengenAnz == 0) || (tmAnz <= maxTeilmengenAnz)) {
            return teilmenge.kosten();
        } else if (tmAnz - 1 == maxTeilmengenAnz) {
            return teilmenge.kosten() + tmAnz * maxTeilmengenKosten;
        } else {
            return teilmenge.kosten() + maxTeilmengenKosten;
        }
    }

    /**
     * Liefert einen Iterator über die Teilmengen der Familie. Die
     * Teilmengen werden von diesem in keiner bestimmten Reihenfolge
     * geliefert.
     *
     * @return  Einen Iterator über die Teilmengen der Familie.
     */
    public Iterator iterator() {
        return itmVerwaltung.iterator();
    }

    /**
     * Liefert ein neues HashSet mit den Teilmengen der Familie.
     *
     * @return  Ein neues <CODE>HashSet</CODE> mit den Teilmengen von
     *          <CODE>this</CODE>.
     */
    public HashSet toHashSet() {
        return itmVerwaltung.toHashSet();
    }

    /**
     * Liefert ein Array mit den zur Familie gehörenden Teilmengen. Die
     * Reihenfolge der Teilmengen im Array ist nicht festgelegt.
     *
     * @return  Ein Array der Teilmengen von <CODE>this</CODE>.
     */
    public IndexTeilmenge[] toArray() {
        return itmVerwaltung.toArray();
    }

    /**
     * Liefert eine Darstellung der Famile in einem Library-Format. Dies ist
     * so aufgebaut, daß in der ersten Zeile die Anzahl der Elemente der
     * Gesamtmenge steht und dann in jeder weiteren Zeile die Darstellung
     * einer Teilmenge.
     *
     * @return  Einen String, der <CODE>this</CODE> in einem Library-Format
     *          darstellt.
     */
    public String toLibFormat() {
        Iterator       iterator;
        IndexTeilmenge teilmenge;
        StringBuffer   rueckgabe;
        String         newline = System.getProperty("line.separator");

        rueckgabe = new StringBuffer();

        // In der ersten Zeile steht die Anzahl der Elemente der Gesamtmenge.
        rueckgabe.append(String.valueOf(groesseGesamtmenge()) + newline);

        // Dann folgen die Teilmengen, jede in einer eigenen Zeile.
        iterator = itmVerwaltung.iterator();
        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge)iterator.next();
            rueckgabe.append(teilmenge.toString() + newline);
        }

        return rueckgabe.toString();
    }

    /**
     * Liefert eine Darstellung der Famile, d.h. der in ihr enthaltenen
     * Teilmengen, als String.
     *
     * @return  Einen String, der <CODE>this</CODE> darstellt.
     */
    public String toString() {
        Iterator       iterator;
        IndexTeilmenge teilmenge;
        StringBuffer   rueckgabe;

        iterator = itmVerwaltung.iterator();
        if (!iterator.hasNext()) {
            return "{}";
        }

        rueckgabe = new StringBuffer();
        teilmenge = (IndexTeilmenge)iterator.next();
        rueckgabe.append("{" + teilmenge.toString());
        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge)iterator.next();
            rueckgabe.append(", " + teilmenge.toString());
        }
        rueckgabe.append("}");

        return rueckgabe.toString();
    }

    /**
     * Liefert einen Text, der die Werte <CODE>anzNichtUeberdeckt()</CODE>,
     * <CODE>anzEinfachUeberdeckt()</CODE> und <CODE>anzMehrfachUeberdeckt()</CODE>
     * jeweils mit einer Erläuterung enthält.
     *
     * @param einrueckStufen  die Anzahl der Stufen, um die die Statistik vom
     *                        linken Rand eingerückt werden soll.
     *
     * @return  Einen Text zur Statistik der Familie.
     */
    public String statistik(int einrueckStufen) {
        String  ausgabe        = "\n";
        String  einrueck       = "";
        int     notwendigeAnz;
        int     einrueckTiefe;

        einrueckTiefe = einrueckStufen * Konstanten.EINRUECK_TIEFE;
        for (int i = 0; i < einrueckTiefe; i++) {
            einrueck += " ";
        }

        if (maxTeilmengenAnz == 0) {
            ausgabe += einrueck + "Die Familie kann unbegrenzt viele"
                                + " Teilmengen enthalten\n";
        } else {
            ausgabe += einrueck + "Die Familie sollte max. "
                                + maxTeilmengenAnz
                                + " Teilmengen mit max. Kosten von "
                                + maxTeilmengenKosten
                                + " enthalten\n";
        }
        ausgabe += einrueck + "Die Familie enthält " + groesseFamilie() + " Teilmengen";
        ausgabe += " mit Gesamtkosten von " + kostenFamilie() + "\n";
        notwendigeAnz = anzNotwendigeTeilmengen();
        if (notwendigeAnz == 0) {
            ausgabe += einrueck + "Von diesen ist keine notwendig.\n";
        } else if (notwendigeAnz == groesseFamilie()) {
            ausgabe += einrueck + "Diese sind alle notwendig.\n";
        } else {
            ausgabe += einrueck + "Von diesen sind " + notwendigeAnz + " notwendig und ";
            ausgabe += (groesseFamilie() - notwendigeAnz) + " nicht notwendig\n";
        }
        ausgabe += "\n";

        ausgabe += einrueck + "Sie versuchen insgesamt zu überdecken\n";
        ausgabe += einrueck + groesseGesamtmenge() + " Indices, von denen\n";
        ausgabe += einrueck + anzNichtUeberdeckt() + " nicht überdeckt werden,\n";
        ausgabe += einrueck + anzEinfachUeberdeckt() + " einfach überdeckt werden und\n";
        ausgabe += einrueck + anzMehrfachUeberdeckt() + " mehrfach überdeckt werden.\n";
        ausgabe += einrueck + "Durchschnittlich wird jeder Index etwa ";
        ausgabe += (new DecimalFormat("0.0")).format(ueberdeckungsHaeufigkeitMittel());
        ausgabe += " mal überdeckt.\n";
        ausgabe += einrueck + "Durchschnittlich enthält jede Teilmenge ";
        ausgabe += (new DecimalFormat("0.0")).format(itmIndexAnzMittel());
        ausgabe += " Indices.\n";
        ausgabe += einrueck + "Die durchschnittliche Dichte beträgt ";
        ausgabe += (new DecimalFormat("0.00")).format(dichteMittel() * 100);
        ausgabe += " Prozent.\n";

        return ausgabe;
    }

    /**
     * Testet einige Prozeduren mittels Hinzunahme und Entfernen mehrerer
     * Teilmengen.
     *
     * @param args  Die Standard-Argumente einer main-Methode.
     */
    public static void main(String[] args) {
        IndexTeilmenge itm1, itm2, itm3;
        ItmFamilie     itmFamilie;
        int            groesseGesamtmenge = 6;

        System.out.println("Test einiger Methoden der Klasse ItmFamilie\n");

        System.out.print("Erzeugung einer ItmFamilie mit der Gesamtgroesse ");
        System.out.println(groesseGesamtmenge + "\n");
        itmFamilie = new ItmFamilie(groesseGesamtmenge);

        System.out.print("Hinzufuegen folgender Index-Teilmengen mit der");
        System.out.println(" Gesamtgroesse " + groesseGesamtmenge + ":");

        itm1 = new IndexTeilmenge(groesseGesamtmenge);
        itm1.indicesAufnehmen(new int[] {2, 3, 5});
        System.out.println(itm1.toString());
        itmFamilie.teilmengeHinzufuegen(itm1);
        itm2 = new IndexTeilmenge(groesseGesamtmenge);
        itm2.indicesAufnehmen(new int[] {0, 1, 3, 5});
        System.out.println(itm2.toString());
        itmFamilie.teilmengeHinzufuegen(itm2);
        itm3 = new IndexTeilmenge(groesseGesamtmenge);
        itm3.indexAufnehmen(2);
        itm3.indexAufnehmen(4);
        System.out.println(itm3.toString());
        itmFamilie.teilmengeHinzufuegen(itm3);

        System.out.println();
        System.out.println("Statistik der resultierenden ItmFamilie:");
        System.out.print(itmFamilie.statistik(1));
        System.out.println();

        System.out.println("Entfernen der Index-Teilmenge ");
        System.out.println(itm2.toString());
        itmFamilie.teilmengeEntfernen(itm2);

        System.out.println();
        System.out.println("Statistik der resultierenden ItmFamilie:");
        System.out.print(itmFamilie.statistik(1));
        System.out.println();
    }
}

