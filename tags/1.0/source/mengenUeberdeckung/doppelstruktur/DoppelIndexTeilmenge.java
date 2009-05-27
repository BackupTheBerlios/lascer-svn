/*
 * Dateiname      : DoppelIndexTeilmenge.java
 * Letzte Änderung: 22. Mai 2006
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


package mengenUeberdeckung.doppelstruktur;

import mathCollection.BitMathIntSet;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ElementVerwaltung;
import mengenUeberdeckung.allgemein.BitIntElemVerwalt;

/**
 * Repräsentiert eine Teilmenge der Indices einer Menge fester Größe von
 * Objekten, die zusätzlich eine Teilmenge einer anderen Menge besitzt, die
 * für die Berechnung der Kosten herangezogen wird.
 *
 * @author  Dietmar Lippold
 */
public class DoppelIndexTeilmenge extends IndexTeilmenge implements Cloneable {

    /**
     * Die Verwaltung der Indices, die für die Berechnung der Kosten der
     * Instanz herangezogen werden.
     */
    private ElementVerwaltung kostIndexVerwalt;

    /**
     * Erzeugt eine Instanz, für die die Verwaltung der Indices, die
     * Verwaltung der Kosten-Indices, die Größe der Gesamtmenge und die Kosten
     * angegeben sind.
     *
     * @param elementVerwalt        Die Verwaltung mit den Indices.
     * @param kostenElementVerwalt  Die Verwaltung mit den Kosten-Indices.
     * @param groesseGesamtmenge    Die Größe der Gesamtmenge, die der neuen
     *                              Instanz zugrunde liegt.
     * @param kosten                Die Kosten, die der neuen Instanz
     *                              zugeordnet sind. Dieser Wert darf nicht
     *                              negativ sein.
     *
     * @throws IllegalArgumentException  Der Wert für <CODE>kosten</CODE> ist
     *                                   negativ.
     */
    public DoppelIndexTeilmenge(ElementVerwaltung elementVerwalt,
                                ElementVerwaltung kostenElementVerwalt,
                                int groesseGesamtmenge, float kosten) {

        super(elementVerwalt, groesseGesamtmenge, kosten);
        this.kostIndexVerwalt = kostenElementVerwalt;
    }

    /**
     * Erzeugt eine Instanz, für die die Kosten, die Größe der Gesamtmenge
     * und die Kosten-Indices angegeben werden. Nach der Erzeugung enthält
     * die Teilmenge noch kein Element.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die der neuen
     *                            Instanz zugrunde liegt.
     * @param kosten              Die Kosten, die <CODE>this</CODE> zugeordnet
     *                            sind. Dieser Wert darf nicht negativ sein.
     * @param kostenIndices       Die Indices, die für die Berechnung der
     *                            Kosten der Teilmenge herangezogen werden.
     * @param speicherEffizient   Gibt an, ob eine Speicher-effiziente und
     *                            damit aber nicht Laufzeit-effiziente
     *                            Speicherung erfolgen soll.
     *
     * @throws IllegalArgumentException  Der Wert für <CODE>kosten</CODE> ist
     *                                   negativ.
     */
    public DoppelIndexTeilmenge(int groesseGesamtmenge, float kosten,
                                BitMathIntSet kostenIndices,
                                boolean speicherEffizient) {

        super(groesseGesamtmenge, kosten, speicherEffizient);
        this.kostIndexVerwalt = new BitIntElemVerwalt(kostenIndices,
                                                      speicherEffizient);
    }

    /**
     * Erzeugt eine Instanz, für die die Kosten, die Größe der Gesamtmenge
     * und die Kosten-Indices angegeben werden. Nach der Erzeugung enthält
     * die Teilmenge noch kein Element. Die Speicherung der Indices erfolgt
     * Laufzeit-effizient.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die der neuen
     *                            Instanz zugrunde liegt.
     * @param kosten              Die Kosten, die <CODE>this</CODE> zugeordnet
     *                            sind. Dieser Wert darf nicht negativ sein.
     * @param kostenIndices       Die Indices, die für die Berechnung der
     *                            Kosten der Teilmenge herangezogen werden.
     *
     * @throws IllegalArgumentException  Der Wert für <CODE>kosten</CODE> ist
     *                                   negativ.
     */
    public DoppelIndexTeilmenge(int groesseGesamtmenge, float kosten,
                                BitMathIntSet kostenIndices) {
        this(groesseGesamtmenge, kosten, kostenIndices, false);
    }

    /**
     * Erzeugt eine Instanz, für die die Kosten, die Größe der Gesamtmenge
     * und die Kosten-Indices angegeben werden. Nach der Erzeugung enthält
     * die Teilmenge noch kein Element. Die Speicherung der Indices erfolgt
     * Laufzeit-effizient.
     *
     * @param groesseGesamtmenge  Die Größe der Gesamtmenge, die der neuen
     *                            Instanz zugrunde liegt.
     * @param kosten              Die Kosten, die <CODE>this</CODE> zugeordnet
     *                            sind. Dieser Wert darf nicht negativ sein.
     * @param kostenIndices       Die Indices, die für die Berechnung der
     *                            Kosten der Teilmenge herangezogen werden.
     *
     * @throws IllegalArgumentException  Der Wert für <CODE>kosten</CODE> ist
     *                                   negativ.
     */
    public DoppelIndexTeilmenge(int groesseGesamtmenge, float kosten,
                                int[] kostenIndices) {
        this(groesseGesamtmenge, kosten, new BitMathIntSet(kostenIndices));
    }

    /**
     * Erzeugt eine Instanz mit den gleichen Elementen wie die übergebene
     * Teilmenge. Für die enthaltenen Elemente wird ein neues Objekt erzeugt,
     * die Kosten-Indices werden aber nur flach kopiert.
     *
     * @param teilmenge  Die Teilmenge, deren Werte kopiert werden.
     */
    public DoppelIndexTeilmenge(DoppelIndexTeilmenge teilmenge) {

        super(teilmenge);
        this.kostIndexVerwalt = teilmenge.kostIndexVerwalt;
    }

    /**
     * Liefert ein neue Instanz dieser Klasse, die zu <CODE>this</CODE> gleich
     * ist. Die Kosten-Indices werden dabei nur flach kopiert.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return new DoppelIndexTeilmenge(this);
    }

    /**
     * Liefert den hashCode dieses Objekts. Dieser hängt nur von der Größe der
     * Gesamtmenge, von den enthaltenen Indices und von den enthaltenen
     * Kosten-Indices ab, nicht jedoch von den absoluten Kosten.
     *
     * @return  Den hashCode dieses Objekts.
     */
    public int hashCode() {
        return (super.hashCode() + kostIndexVerwalt.hashCode());
    }

    /**
     * Ermittelt, ob das übergebene Objekte eine Teilmenge ist und zu
     * <CODE>this</CODE> gleich ist. Dies ist nur der Fall, wenn das
     * übergebene Objekte eine Instanz dieser Klasse ist.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>DoppelIndexTeilmenge</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das übergebene Objekt eine
     *          <CODE>DoppelIndexTeilmenge</CODE> ist und die gleichen
     *          Indices, Kosten-Indices und Kosten wie diese Instanz enthält,
     *          anderenfalls wird <CODE>false</CODE> geliefert.
     */
    public boolean equals(Object anderesObjekt) {

        if (anderesObjekt == this) {
            return true;
        }

        if (!(anderesObjekt instanceof DoppelIndexTeilmenge)) {
            return false;
        }

        DoppelIndexTeilmenge andereTeilmenge = (DoppelIndexTeilmenge)anderesObjekt;
        return (super.equals(andereTeilmenge)
                && andereTeilmenge.kostIndexVerwalt.equals(kostIndexVerwalt));
    }

    /**
     * Ermittelt, ob diese Teilmenge gleich gut oder besser als die übergebene
     * Teilmenge ist. Diese Methode definiert nur eine Halbordnung auf den
     * Teilmengen, d.h. bei zwei Teilmengen braucht weder die erste noch die
     * zweite besser als die jeweils andere zu sein.
     *
     * @param andereTeilmenge  Eine andere Teilmenge.
     *
     * @return  <CODE>true</CODE>, wenn diese Teilmenge gleich gut oder besser
     *          als die übergebene Teilmenge ist, anderenfalls wird
     *          <CODE>false</CODE> geliefert.
     */
    public boolean istGleichOderBesser(IndexTeilmenge andereTeilmenge) {
        DoppelIndexTeilmenge andereDoppelItm;

        if (!(andereTeilmenge instanceof DoppelIndexTeilmenge)) {
            return false;
        }

        andereDoppelItm = (DoppelIndexTeilmenge) andereTeilmenge;
        return (super.istGleichOderBesser(andereDoppelItm)
                && andereDoppelItm.kostIndexVerwalt.containsAll(kostIndexVerwalt));
    }

    /**
     * Liefert den kleinsten in der Teilmenge enthaltenen Kosten-Index. Wenn
     * kein Kosten-Index in der Teilmenge enthalten ist, wird der Wert -1
     * geliefert.
     *
     * @return  Den kleinsten in <CODE>this</CODE> enthaltenen Kosten-Index.
     */
    public int kleinsterEnthaltKostenIndex() {
        return kostIndexVerwalt.getMinimum();
    }

    /**
     * Liefert den größten in der Teilmenge enthaltenen Kosten-Index. Wenn
     * kein Kosten-Index in der Teilmenge enthalten ist, wird der Wert -1
     * geliefert.
     *
     * @return  Den größten in <CODE>this</CODE> enthaltenen Kosten-Index.
     */
    public int groessterEnthaltKostenIndex() {
        return kostIndexVerwalt.getMaximum();
    }

    /**
     * Liefert den nächsten in dieser Teilmenge enthaltenen Kosten-Index.
     *
     * @param index  Der Kosten-Index, ab dem der nächste in der Teilmenge
     *               enthaltene Kosten-Index ermittelt werden soll.
     *
     * @return  Den kleinsten Kosten-Index, der gleich oder größer als
     *          <CODE>index</CODE> ist und in der Teilmenge enthalten ist.
     *          Falls es so einen Kosten-Index nicht gibt, wird der Wert -1
     *          geliefert.
     */
    public int naechsterEnthaltKostenIndex(int index) {
        return kostIndexVerwalt.getNext(index - 1);
    }

    /**
     * Liefert ein aufsteigend sortiertes Array mit den Kosten-Indices, die in
     * dieser Teilmenge enthalten sind.
     *
     * @return  Ein aufsteigend sortiertes Array mit den Kosten-Indices dieser
     *          Teilmenge. Dieses darf nicht verändert werden.
     */
    public int[] enthalteneKostenIndices() {
        return kostIndexVerwalt.toArray();
    }

    /**
     * Liefert die Verwaltung der Kosten-Indices der Teilmenge.
     *
     * @return  Die Verwaltung der Kosten-Indices der Teilmenge. Diese darf
     *          nicht verändert werden.
     */
    public ElementVerwaltung kostenIndexVerwaltung() {
        return kostIndexVerwalt;
    }

    /**
     * Liefert eine Darstellung der Teilmenge, d.h. der in ihr enthaltenen
     * Elemente, der allgemeinen Kosten und der Kosten-Indices, als String.
     *
     * @return  Einen String, der <CODE>this</CODE> darstellt.
     */
    public String toString() {
        return (super.toString() + ";" + kostIndexVerwalt.toString());
    }
}

