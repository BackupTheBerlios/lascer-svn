/*
 * Dateiname      : Disjunktion.java
 * Letzte Änderung: 21. September 2006
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


package lascer.konzepte.einzelne;

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;

import mathCollection.BitMathIntSet;

import lascer.konzepte.Konzept;
import lascer.konzepte.KombiKonzept;
import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;

/**
 * Repräsentiert eine Disjunktion, also eine disjunktiv verknüpfte Menge von
 * Konzepten.
 *
 * @author  Dietmar Lippold
 */
public class Disjunktion extends AbstraktesKonzept implements KombiKonzept {

    /**
     * Die der Disjunktion enthaltenen Teilkonzepte.
     */
    private HashSet konzepte = new HashSet();

    /**
     * Die Komplexität des Konzepts.
     */
    private float komplexitaet = -1;

    /**
     * Erzeugt eine neue Instanz, bei der die Attributen nur die default-Werte
     * besitzen.
     *
     * @param posGesamtAnz  Die Anzahl der insgesamt vorhandenen positiven
     *                      Beispiele.
     * @param negGesamtAnz  Die Anzahl der insgesamt vorhandenen negativen
     *                      Beispiele.
     */
    private Disjunktion(int posGesamtAnz, int negGesamtAnz) {
        super(posGesamtAnz, negGesamtAnz);
    }

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param bspDaten  Die dem Konzept zugrunde gelegten Beispieldaten.
     */
    public Disjunktion(Beispieldaten bspDaten) {

        super(bspDaten.posBspAnz(), bspDaten.negBspAnz());

        // Die initiale Disjunktion umfaßt kein Beispiel.
        // Wegen einer ungünstigen Implementierung von java.util.BitSet in
        // JDK 1.4 den Wert 64 zum größten Index addieren.
        setzeErfuellteBsp(new BitMathIntSet(bspDaten.posBspAnz() + 64),
                          new BitMathIntSet(bspDaten.negBspAnz() + 64));
        komplexitaet = Konstanten.INIT_DIS_KOMPLEX;
    }

    /**
     * Liefert eine flache Kopie dieser Disjunktion.
     *
     * @return  Eine flache Kopie dieser Disjunktion.
     */
    public Object clone() {
        Disjunktion kopie;

        kopie = new Disjunktion(posGesamtAnz(), negGesamtAnz());
        kopie.setzeErfuellteBsp((BitMathIntSet) posErfuelltBsp().clone(),
                                (BitMathIntSet) negErfuelltBsp().clone());
        kopie.konzepte.addAll(konzepte);
        kopie.komplexitaet = komplexitaet;

        return kopie;
    }

    /**
     * Liefert den hashCode dieses Objekts.
     *
     * @return  Den hashCode dieses Objekts.
     */
    public int hashCode() {
        return konzepte.hashCode();
    }

    /**
     * Ermittelt, ob das übergebene Objekt eine Disjunktion ist und zu
     * <CODE>this</CODE> gleich ist.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>Disjunktion</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das übergebene Objekt eine
     *          <CODE>Disjunktion</CODE> ist und die gleichen Teilkonzepte
     *          besitzt, anderenfalls <CODE>false</CODE>.
     */
    public boolean equals(Object anderesObjekt) {

        if (anderesObjekt == this) {
            return true;
        }

        if (!(anderesObjekt instanceof Disjunktion)) {
            return false;
        }

        Disjunktion andereDisjunktion = (Disjunktion) anderesObjekt;
        if (!posErfuelltBsp().equals(andereDisjunktion.posErfuelltBsp())
            || !negErfuelltBsp().equals(andereDisjunktion.negErfuelltBsp())) {

            return false;
        }

        return konzepte.equals(andereDisjunktion.konzepte);
    }

    /**
     * Nimmt das übergebene Konzept in die Disjunktion auf. Wenn es schon
     * enthalten ist, ändert sich nichts.
     *
     * @param konzept  Das in die Disjunktion aufzunehmende Konzept.
     *
     * @throws IllegalArgumentException  Wenn die Anzahl der Beispiele, die
     *                                   dem übergebenen Konzept und die
     *                                   diesem Konzept zugrunde liegen,
     *                                   nicht übereinstimmt.
     */
    public void aufnehmen(Konzept konzept) {

        if ((konzept.posGesamtAnz() != posGesamtAnz())
            || (konzept.negGesamtAnz() != negGesamtAnz())) {

            throw new IllegalArgumentException("Die Anzahl der zugrunde liegenden"
                                               + " Beispiele stimmt nicht überein");
        }

        if (!konzepte.contains(konzept)) {
            setzeErfuellteBsp(posErfuelltBsp().union(konzept.posErfuelltBsp()),
                              negErfuelltBsp().union(konzept.negErfuelltBsp()));
            komplexitaet += konzept.komplexitaet();
            konzepte.add(konzept);
        }
    }

    /**
     * Nimmt die Konzepte der übergebene Collection in die Disjunktion auf,
     * soweit sie noch nicht enthalten sind.
     *
     * @param konzepte  Eine Collection mit Konzepten, die in die Disjunktion
     *                  aufgenommen werden sollen.
     *
     * @throws IllegalArgumentException  Wenn die Anzahl der Beispiele, die
     *                                   den übergebenen Konzepten und die
     *                                   diesem Konzept zugrunde liegen,
     *                                   nicht übereinstimmt.
     */
    public void aufnehmen(Collection konzepte) {
        Iterator konzeptIter = konzepte.iterator();
        while (konzeptIter.hasNext()) {
            aufnehmen((Konzept) konzeptIter.next());
        }
    }

    /**
     * Entfernt das übergebene Konzept als Teilkonzept aus der Disjunktion.
     * Wenn es nicht enthalten ist, ändert sich nichts.
     *
     * @param konzept  Das aus der Disjunktion zu entfernende Konzept.
     *
     * @throws IllegalArgumentException  Wenn die Anzahl der Beispiele, die
     *                                   dem übergebenen Konzept und die
     *                                   diesem Konzept zugrunde liegen,
     *                                   nicht übereinstimmt.
     */
    public void entfernen(Konzept konzept) {
        BitMathIntSet posErfuelltBsp, negErfuelltBsp;
        Konzept       naechstesKonzept;
        Iterator      konzIter;

        if ((konzept.posGesamtAnz() != posGesamtAnz())
            || (konzept.negGesamtAnz() != negGesamtAnz())) {

            throw new IllegalArgumentException("Die Anzahl der zugrunde liegenden"
                                               + " Beispiele stimmt nicht überein");
        }

        konzepte.remove(konzept);

        posErfuelltBsp = new BitMathIntSet();
        negErfuelltBsp = new BitMathIntSet();
        komplexitaet = Konstanten.INIT_DIS_KOMPLEX;

        konzIter = konzepte.iterator();
        while (konzIter.hasNext()) {
            naechstesKonzept = (Konzept) konzIter.next();
            posErfuelltBsp = posErfuelltBsp.union(naechstesKonzept.posErfuelltBsp());
            negErfuelltBsp = negErfuelltBsp.union(naechstesKonzept.negErfuelltBsp());
            komplexitaet += naechstesKonzept.komplexitaet();
        }
        setzeErfuellteBsp(posErfuelltBsp, negErfuelltBsp);
    }

    /**
     * Liefert die Anzahl der Konjunktionen des Konzepts.
     *
     * @return  Die Anzahl der Konjunktionen des Konzepts.
     */
    public int konjunktionsAnz() {
        Iterator konzIter;
        int      anzahl;

        anzahl = 0;
        konzIter = konzepte.iterator();
        while (konzIter.hasNext()) {
            Konzept naechstesKonzept = (Konzept) konzIter.next();
            anzahl += naechstesKonzept.konjunktionsAnz();
        }
        return anzahl;
    }

    /**
     * Liefert den Wert Eins als Anzahl der Disjunktionen des Konzepts.
     *
     * @return  Den Wert Eins.
     */
    public int disjunktionsAnz() {
        return 1;
    }

    /**
     * Liefert die Anzahl der einzelnen Literale des Konzepts, die also direkt
     * in diesem Konzept enthalten sind.
     *
     * @return  Die Anzahl der einzelnen Literale des Konzepts, die also
     *          direkt in diesem Konzept enthalten sind.
     */
    public int einzelLiteralAnz() {
        Iterator konzIter;
        int      anzahl;

        anzahl = 0;
        konzIter = konzepte.iterator();
        while (konzIter.hasNext()) {
            Konzept naechstesKonzept = (Konzept) konzIter.next();
            if (naechstesKonzept instanceof Literal) {
                anzahl++;
            }
        }
        return anzahl;
    }

    /**
     * Liefert die Anzahl der positiven Literale des Konzepts insgesamt.
     *
     * @return  Die Anzahl der positiven Literale des Konzepts insgesamt.
     */
    public int posLiteralAnz() {
        Iterator konzIter;
        int      anzahl;

        anzahl = 0;
        konzIter = konzepte.iterator();
        while (konzIter.hasNext()) {
            Konzept naechstesKonzept = (Konzept) konzIter.next();
            anzahl += naechstesKonzept.posLiteralAnz();
        }
        return anzahl;
    }

    /**
     * Liefert die Anzahl der negativen (invertierten) Literale des Konzepts
     * insgesamt.
     *
     * @return  Die Anzahl der negativen (invertierten) Literale des Konzepts
     *          insgesamt.
     */
    public int negLiteralAnz() {
        Iterator konzIter;
        int      anzahl;

        anzahl = 0;
        konzIter = konzepte.iterator();
        while (konzIter.hasNext()) {
            Konzept naechstesKonzept = (Konzept) konzIter.next();
            anzahl += naechstesKonzept.negLiteralAnz();
        }
        return anzahl;
    }

    /**
     * Liefert die Komplexität des Konzepts.
     *
     * @return  Die Komplexität des Konzepts.
     */
    public float komplexitaet() {
        return komplexitaet;
    }

    /**
     * Ermitteln, ob das Konzept auf das übergebene Beispiel zutrifft bzw. für
     * dieses erfüllt ist.
     *
     * @param beispiel  Ein Beispiel, für das ermittelt werden soll, ob das
     *                  Konzept darauf zutrifft.
     *
     * @return  Die Angabe, ob das Konzept auf das übergebene Beispiel
     *          zutrifft.
     */
    public boolean trifftZu(Beispiel beispiel) {
        Iterator konzIter;
        boolean  trifftZu;

        trifftZu = false;
        konzIter = konzepte.iterator();
        while (!trifftZu && konzIter.hasNext()) {
            Konzept naechstesKonzept = (Konzept) konzIter.next();
            trifftZu = naechstesKonzept.trifftZu(beispiel);
        }
        return trifftZu;
    }

    /**
     * Liefert eine Menge der Teilkonzepte dieses Konzepts.
     *
     * @return  Eine Menge der Teilkonzepte dieses Konzepts.
     */
    public HashSet teilkonzepte() {
        return konzepte;
    }

    /**
     * Liefert eine Beschreibung des Konzepts.
     *
     * @return  Eine Beschreibung des Konzepts.
     */
    public String toString() {
        StringBuffer beschreibung = new StringBuffer();
        ArrayList    konzeptListe;
        Iterator     konzIter;

        if (konzepte.isEmpty()) {
            beschreibung.append("false");
        } else {
            konzeptListe = sortierteKonzepte(konzepte);

            beschreibung.append("OR(");
            konzIter = konzeptListe.iterator();
            do {
                Konzept naechstesKonzept = (Konzept) konzIter.next();
                beschreibung.append(naechstesKonzept.toString() + ", ");
            } while (konzIter.hasNext());
            beschreibung.deleteCharAt(beschreibung.length() - 1);
            beschreibung.deleteCharAt(beschreibung.length() - 1);
            beschreibung.append(")");
        }
        return beschreibung.toString();
    }
}

